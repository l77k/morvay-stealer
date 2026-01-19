/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package org.jsoup.select;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import org.jsoup.internal.StringUtil;
import org.jsoup.nodes.Element;
import org.jsoup.select.Evaluator;
import org.jspecify.annotations.Nullable;

public abstract class CombiningEvaluator
extends Evaluator {
    final ArrayList<Evaluator> evaluators = new ArrayList();
    final List<Evaluator> sortedEvaluators = new ArrayList<Evaluator>();
    int num = 0;
    int cost = 0;

    CombiningEvaluator() {
    }

    CombiningEvaluator(Collection<Evaluator> evaluators) {
        this();
        this.evaluators.addAll(evaluators);
        this.updateEvaluators();
    }

    @Override
    protected void reset() {
        for (Evaluator evaluator : this.evaluators) {
            evaluator.reset();
        }
        super.reset();
    }

    @Override
    protected int cost() {
        return this.cost;
    }

    @Nullable Evaluator rightMostEvaluator() {
        return this.num > 0 ? this.evaluators.get(this.num - 1) : null;
    }

    void replaceRightMostEvaluator(Evaluator replacement) {
        this.evaluators.set(this.num - 1, replacement);
        this.updateEvaluators();
    }

    void updateEvaluators() {
        this.num = this.evaluators.size();
        this.cost = 0;
        for (Evaluator evaluator : this.evaluators) {
            this.cost += evaluator.cost();
        }
        this.sortedEvaluators.clear();
        this.sortedEvaluators.addAll(this.evaluators);
        this.sortedEvaluators.sort(Comparator.comparingInt(Evaluator::cost));
    }

    public static final class Or
    extends CombiningEvaluator {
        public Or(Collection<Evaluator> evaluators) {
            if (this.num > 1) {
                this.evaluators.add(new And(evaluators));
            } else {
                this.evaluators.addAll(evaluators);
            }
            this.updateEvaluators();
        }

        Or(Evaluator ... evaluators) {
            this(Arrays.asList(evaluators));
        }

        Or() {
        }

        public void add(Evaluator e2) {
            this.evaluators.add(e2);
            this.updateEvaluators();
        }

        @Override
        public boolean matches(Element root, Element node) {
            for (int i2 = 0; i2 < this.num; ++i2) {
                Evaluator s2 = (Evaluator)this.sortedEvaluators.get(i2);
                if (!s2.matches(root, node)) continue;
                return true;
            }
            return false;
        }

        public String toString() {
            return StringUtil.join(this.evaluators, ", ");
        }
    }

    public static final class And
    extends CombiningEvaluator {
        public And(Collection<Evaluator> evaluators) {
            super(evaluators);
        }

        And(Evaluator ... evaluators) {
            this(Arrays.asList(evaluators));
        }

        @Override
        public boolean matches(Element root, Element element) {
            for (int i2 = 0; i2 < this.num; ++i2) {
                Evaluator s2 = (Evaluator)this.sortedEvaluators.get(i2);
                if (s2.matches(root, element)) continue;
                return false;
            }
            return true;
        }

        public String toString() {
            return StringUtil.join(this.evaluators, "");
        }
    }
}

