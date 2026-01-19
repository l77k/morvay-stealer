/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.prng;

public class ThreadedSeedGenerator {
    public byte[] generateSeed(int n2, boolean bl) {
        SeedGenerator seedGenerator = new SeedGenerator();
        return seedGenerator.generateSeed(n2, bl);
    }

    private static class SeedGenerator
    implements Runnable {
        private volatile int counter = 0;
        private volatile boolean stop = false;

        private SeedGenerator() {
        }

        @Override
        public void run() {
            while (!this.stop) {
                ++this.counter;
            }
        }

        public byte[] generateSeed(int n2, boolean bl) {
            Thread thread2 = new Thread(this);
            byte[] byArray = new byte[n2];
            this.counter = 0;
            this.stop = false;
            int n3 = 0;
            thread2.start();
            int n4 = bl ? n2 : n2 * 8;
            for (int i2 = 0; i2 < n4; ++i2) {
                while (this.counter == n3) {
                    try {
                        Thread.sleep(1L);
                    }
                    catch (InterruptedException interruptedException) {}
                }
                n3 = this.counter;
                if (bl) {
                    byArray[i2] = (byte)(n3 & 0xFF);
                    continue;
                }
                int n5 = i2 / 8;
                byArray[n5] = (byte)(byArray[n5] << 1 | n3 & 1);
            }
            this.stop = true;
            return byArray;
        }
    }
}

