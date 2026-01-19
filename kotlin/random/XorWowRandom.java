/*
 * Decompiled with CFR 0.152.
 */
package kotlin.random;

import java.io.Serializable;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.random.Random;
import kotlin.random.RandomKt;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u000e\b\u0000\u0018\u0000 \u00122\u00020\u00012\u00060\u0002j\u0002`\u0003:\u0001\u0012B\u0017\b\u0010\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0007B7\b\u0000\u0012\u0006\u0010\b\u001a\u00020\u0005\u0012\u0006\u0010\t\u001a\u00020\u0005\u0012\u0006\u0010\n\u001a\u00020\u0005\u0012\u0006\u0010\u000b\u001a\u00020\u0005\u0012\u0006\u0010\f\u001a\u00020\u0005\u0012\u0006\u0010\r\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u000eJ\u0010\u0010\u000f\u001a\u00020\u00052\u0006\u0010\u0010\u001a\u00020\u0005H\u0016J\b\u0010\u0011\u001a\u00020\u0005H\u0016R\u000e\u0010\r\u001a\u00020\u0005X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0005X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0005X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0005X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0005X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0005X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0013"}, d2={"Lkotlin/random/XorWowRandom;", "Lkotlin/random/Random;", "Ljava/io/Serializable;", "Lkotlin/io/Serializable;", "seed1", "", "seed2", "(II)V", "x", "y", "z", "w", "v", "addend", "(IIIIII)V", "nextBits", "bitCount", "nextInt", "Companion", "kotlin-stdlib"})
public final class XorWowRandom
extends Random
implements Serializable {
    @NotNull
    private static final Companion Companion = new Companion(null);
    private int x;
    private int y;
    private int z;
    private int w;
    private int v;
    private int addend;
    @Deprecated
    private static final long serialVersionUID = 0L;

    public XorWowRandom(int x2, int y2, int z2, int w2, int v2, int addend) {
        int n2;
        this.x = x2;
        this.y = y2;
        this.z = z2;
        this.w = w2;
        this.v = v2;
        this.addend = addend;
        int n3 = n2 = (this.x | this.y | this.z | this.w | this.v) != 0 ? 1 : 0;
        if (n2 == 0) {
            boolean bl = false;
            String string = "Initial state must have at least one non-zero element.";
            throw new IllegalArgumentException(string.toString());
        }
        n2 = 64;
        int n4 = 0;
        while (n4 < n2) {
            int it = n4++;
            boolean bl = false;
            this.nextInt();
        }
    }

    public XorWowRandom(int seed1, int seed2) {
        this(seed1, seed2, 0, 0, ~seed1, seed1 << 10 ^ seed2 >>> 4);
    }

    @Override
    public int nextInt() {
        int v0;
        int t2 = this.x;
        t2 ^= t2 >>> 2;
        this.x = this.y;
        this.y = this.z;
        this.z = this.w;
        this.w = v0 = this.v;
        this.v = t2 = t2 ^ t2 << 1 ^ v0 ^ v0 << 4;
        this.addend += 362437;
        return t2 + this.addend;
    }

    @Override
    public int nextBits(int bitCount) {
        return RandomKt.takeUpperBits(this.nextInt(), bitCount);
    }

    @Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\b\u0082\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0005"}, d2={"Lkotlin/random/XorWowRandom$Companion;", "", "()V", "serialVersionUID", "", "kotlin-stdlib"})
    private static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}

