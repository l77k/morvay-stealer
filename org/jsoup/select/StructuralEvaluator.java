/*
 * Decompiled with CFR 0.152.
 */
package org.jsoup.select;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.Map;
import org.jsoup.internal.Functions;
import org.jsoup.internal.SoftPool;
import org.jsoup.internal.StringUtil;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.NodeIterator;
import org.jsoup.select.CombiningEvaluator;
import org.jsoup.select.Evaluator;

abstract class StructuralEvaluator
extends Evaluator {
    final Evaluator evaluator;
    final ThreadLocal<IdentityHashMap<Element, IdentityHashMap<Element, Boolean>>> threadMemo = ThreadLocal.withInitial(IdentityHashMap::new);

    public StructuralEvaluator(Evaluator evaluator) {
        this.evaluator = evaluator;
    }

    boolean memoMatches(Element root, Element element) {
        Map rootMemo = this.threadMemo.get();
        Map memo = rootMemo.computeIfAbsent(root, Functions.identityMapFunction());
        return memo.computeIfAbsent(element, key -> this.evaluator.matches(root, (Element)key));
    }

    @Override
    protected void reset() {
        this.threadMemo.get().clear();
        this.evaluator.reset();
        super.reset();
    }

    static class ImmediatePreviousSibling
    extends StructuralEvaluator {
        public ImmediatePreviousSibling(Evaluator evaluator) {
            super(evaluator);
        }

        @Override
        public boolean matches(Element root, Element element) {
            if (root == element) {
                return false;
            }
            Element prev = element.previousElementSibling();
            return prev != null && this.memoMatches(root, prev);
        }

        @Override
        protected int cost() {
            return 2 + this.evaluator.cost();
        }

        public String toString() {
            return String.format("%s + ", this.evaluator);
        }
    }

    static class PreviousSibling
    extends StructuralEvaluator {
        public PreviousSibling(Evaluator evaluator) {
            super(evaluator);
        }

        @Override
        public boolean matches(Element root, Element element) {
            if (root == element) {
                return false;
            }
            for (Element sib = element.firstElementSibling(); sib != null && sib != element; sib = sib.nextElementSibling()) {
                if (!this.memoMatches(root, sib)) continue;
                return true;
            }
            return false;
        }

        @Override
        protected int cost() {
            return 3 * this.evaluator.cost();
        }

        public String toString() {
            return String.format("%s ~ ", this.evaluator);
        }
    }

    static class ImmediateParentRun
    extends Evaluator {
        final ArrayList<Evaluator> evaluators = new ArrayList();
        int cost = 2;

        public ImmediateParentRun(Evaluator evaluator) {
            this.evaluators.add(evaluator);
            this.cost += evaluator.cost();
        }

        void add(Evaluator evaluator) {
            this.evaluators.add(evaluator);
            this.cost += evaluator.cost();
        }

        @Override
        public boolean matches(Element root, Element element) {
            if (element == root) {
                return false;
            }
            for (int i2 = this.evaluators.size() - 1; i2 >= 0; --i2) {
                if (element == null) {
                    return false;
                }
                Evaluator eval = this.evaluators.get(i2);
                if (!eval.matches(root, element)) {
                    return false;
                }
                element = element.parent();
            }
            return true;
        }

        @Override
        protected int cost() {
            return this.cost;
        }

        @Override
        protected void reset() {
            for (Evaluator evaluator : this.evaluators) {
                evaluator.reset();
            }
            super.reset();
        }

        public String toString() {
            return StringUtil.join(this.evaluators, " > ");
        }
    }

    static class Ancestor
    extends StructuralEvaluator {
        public Ancestor(Evaluator evaluator) {
            super(evaluator);
        }

        @Override
        public boolean matches(Element root, Element element) {
            if (root == element) {
                return false;
            }
            for (Element parent = element.parent(); parent != null; parent = parent.parent()) {
                if (this.memoMatches(root, parent)) {
                    return true;
                }
                if (parent == root) break;
            }
            return false;
        }

        @Override
        protected int cost() {
            return 8 * this.evaluator.cost();
        }

        public String toString() {
            return String.format("%s ", this.evaluator);
        }
    }

    static class Not
    extends StructuralEvaluator {
        public Not(Evaluator evaluator) {
            super(evaluator);
        }

        @Override
        public boolean matches(Element root, Element element) {
            return !this.memoMatches(root, element);
        }

        @Override
        protected int cost() {
            return 2 + this.evaluator.cost();
        }

        public String toString() {
            return String.format(":not(%s)", this.evaluator);
        }
    }

    static class Is
    extends StructuralEvaluator {
        public Is(Evaluator evaluator) {
            super(evaluator);
        }

        @Override
        public boolean matches(Element root, Element element) {
            return this.evaluator.matches(root, element);
        }

        @Override
        protected int cost() {
            return 2 + this.evaluator.cost();
        }

        public String toString() {
            return String.format(":is(%s)", this.evaluator);
        }
    }

    static class Has
    extends StructuralEvaluator {
        static final SoftPool<NodeIterator<Element>> ElementIterPool = new SoftPool<NodeIterator>(() -> new NodeIterator<Element>(new Element("html"), Element.class));
        private final boolean checkSiblings;

        public Has(Evaluator evaluator) {
            super(evaluator);
            this.checkSiblings = Has.evalWantsSiblings(evaluator);
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean matches(Element root, Element element) {
            if (this.checkSiblings) {
                for (Element sib = element.firstElementSibling(); sib != null; sib = sib.nextElementSibling()) {
                    if (sib == element || !this.evaluator.matches(element, sib)) continue;
                    return true;
                }
            }
            NodeIterator<Element> it = ElementIterPool.borrow();
            it.restart(element);
            try {
                while (it.hasNext()) {
                    Element el = (Element)it.next();
                    if (el == element) continue;
                    if (!this.evaluator.matches(element, el)) continue;
                    boolean bl = true;
                    return bl;
                }
            }
            finally {
                ElementIterPool.release(it);
            }
            return false;
        }

        private static boolean evalWantsSiblings(Evaluator eval) {
            if (eval instanceof CombiningEvaluator) {
                CombiningEvaluator ce = (CombiningEvaluator)eval;
                for (Evaluator innerEval : ce.evaluators) {
                    if (!(innerEval instanceof PreviousSibling) && !(innerEval instanceof ImmediatePreviousSibling)) continue;
                    return true;
                }
            }
            return false;
        }

        @Override
        protected int cost() {
            return 10 * this.evaluator.cost();
        }

        public String toString() {
            return String.format(":has(%s)", this.evaluator);
        }
    }

    static class Root
    extends Evaluator {
        Root() {
        }

        @Override
        public boolean matches(Element root, Element element) {
            return root == element;
        }

        @Override
        protected int cost() {
            return 1;
        }

        public String toString() {
            return "";
        }
    }
}

