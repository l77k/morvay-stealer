/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.internal;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Cleaner {
    private static final Cleaner INSTANCE = new Cleaner();
    private final ReferenceQueue<Object> referenceQueue = new ReferenceQueue();
    private final Thread cleanerThread = new Thread(){

        @Override
        public void run() {
            while (true) {
                try {
                    while (true) {
                        Reference ref;
                        if (!((ref = Cleaner.this.referenceQueue.remove()) instanceof CleanerRef)) {
                            continue;
                        }
                        ((CleanerRef)ref).clean();
                    }
                }
                catch (InterruptedException ex) {
                }
                catch (Exception ex) {
                    Logger.getLogger(Cleaner.class.getName()).log(Level.SEVERE, null, ex);
                    continue;
                }
                break;
            }
        }
    };
    private CleanerRef firstCleanable;

    public static Cleaner getCleaner() {
        return INSTANCE;
    }

    private Cleaner() {
        this.cleanerThread.setName("JNA Cleaner");
        this.cleanerThread.setDaemon(true);
        this.cleanerThread.start();
    }

    public synchronized Cleanable register(Object obj, Runnable cleanupTask2) {
        return this.add(new CleanerRef(this, obj, this.referenceQueue, cleanupTask2));
    }

    private synchronized CleanerRef add(CleanerRef ref) {
        if (this.firstCleanable == null) {
            this.firstCleanable = ref;
        } else {
            ref.setNext(this.firstCleanable);
            this.firstCleanable.setPrevious(ref);
            this.firstCleanable = ref;
        }
        return ref;
    }

    private synchronized boolean remove(CleanerRef ref) {
        boolean inChain = false;
        if (ref == this.firstCleanable) {
            this.firstCleanable = ref.getNext();
            inChain = true;
        }
        if (ref.getPrevious() != null) {
            ref.getPrevious().setNext(ref.getNext());
        }
        if (ref.getNext() != null) {
            ref.getNext().setPrevious(ref.getPrevious());
        }
        if (ref.getPrevious() != null || ref.getNext() != null) {
            inChain = true;
        }
        ref.setNext(null);
        ref.setPrevious(null);
        return inChain;
    }

    public static interface Cleanable {
        public void clean();
    }

    private static class CleanerRef
    extends PhantomReference<Object>
    implements Cleanable {
        private final Cleaner cleaner;
        private final Runnable cleanupTask;
        private CleanerRef previous;
        private CleanerRef next;

        public CleanerRef(Cleaner cleaner, Object referent, ReferenceQueue<? super Object> q2, Runnable cleanupTask2) {
            super(referent, q2);
            this.cleaner = cleaner;
            this.cleanupTask = cleanupTask2;
        }

        @Override
        public void clean() {
            if (this.cleaner.remove(this)) {
                this.cleanupTask.run();
            }
        }

        CleanerRef getPrevious() {
            return this.previous;
        }

        void setPrevious(CleanerRef previous) {
            this.previous = previous;
        }

        CleanerRef getNext() {
            return this.next;
        }

        void setNext(CleanerRef next) {
            this.next = next;
        }
    }
}

