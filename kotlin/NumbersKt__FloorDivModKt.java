/*
 * Decompiled with CFR 0.152.
 */
package kotlin;

import kotlin.Metadata;
import kotlin.NumbersKt__BigIntegersKt;
import kotlin.SinceKotlin;
import kotlin.internal.InlineOnly;

@Metadata(mv={1, 6, 0}, k=5, xi=49, d1={"\u0000 \n\u0000\n\u0002\u0010\b\n\u0002\u0010\u0005\n\u0000\n\u0002\u0010\t\n\u0002\u0010\n\n\u0000\n\u0002\u0010\u0006\n\u0002\u0010\u0007\n\u0000\u001a\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0002H\u0087\b\u001a\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0001H\u0087\b\u001a\u0015\u0010\u0000\u001a\u00020\u0004*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004H\u0087\b\u001a\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0005H\u0087\b\u001a\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0002H\u0087\b\u001a\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0001H\u0087\b\u001a\u0015\u0010\u0000\u001a\u00020\u0004*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0087\b\u001a\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0005H\u0087\b\u001a\u0015\u0010\u0000\u001a\u00020\u0004*\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0087\b\u001a\u0015\u0010\u0000\u001a\u00020\u0004*\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0001H\u0087\b\u001a\u0015\u0010\u0000\u001a\u00020\u0004*\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0004H\u0087\b\u001a\u0015\u0010\u0000\u001a\u00020\u0004*\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0005H\u0087\b\u001a\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00052\u0006\u0010\u0003\u001a\u00020\u0002H\u0087\b\u001a\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00052\u0006\u0010\u0003\u001a\u00020\u0001H\u0087\b\u001a\u0015\u0010\u0000\u001a\u00020\u0004*\u00020\u00052\u0006\u0010\u0003\u001a\u00020\u0004H\u0087\b\u001a\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00052\u0006\u0010\u0003\u001a\u00020\u0005H\u0087\b\u001a\u0015\u0010\u0006\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0002H\u0087\b\u001a\u0015\u0010\u0006\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0001H\u0087\b\u001a\u0015\u0010\u0006\u001a\u00020\u0004*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004H\u0087\b\u001a\u0015\u0010\u0006\u001a\u00020\u0005*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0005H\u0087\b\u001a\u0015\u0010\u0006\u001a\u00020\u0007*\u00020\u00072\u0006\u0010\u0003\u001a\u00020\u0007H\u0087\b\u001a\u0015\u0010\u0006\u001a\u00020\u0007*\u00020\u00072\u0006\u0010\u0003\u001a\u00020\bH\u0087\b\u001a\u0015\u0010\u0006\u001a\u00020\u0007*\u00020\b2\u0006\u0010\u0003\u001a\u00020\u0007H\u0087\b\u001a\u0015\u0010\u0006\u001a\u00020\b*\u00020\b2\u0006\u0010\u0003\u001a\u00020\bH\u0087\b\u001a\u0015\u0010\u0006\u001a\u00020\u0002*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0002H\u0087\b\u001a\u0015\u0010\u0006\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0001H\u0087\b\u001a\u0015\u0010\u0006\u001a\u00020\u0004*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0087\b\u001a\u0015\u0010\u0006\u001a\u00020\u0005*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0005H\u0087\b\u001a\u0015\u0010\u0006\u001a\u00020\u0002*\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0087\b\u001a\u0015\u0010\u0006\u001a\u00020\u0001*\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0001H\u0087\b\u001a\u0015\u0010\u0006\u001a\u00020\u0004*\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0004H\u0087\b\u001a\u0015\u0010\u0006\u001a\u00020\u0005*\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0005H\u0087\b\u001a\u0015\u0010\u0006\u001a\u00020\u0002*\u00020\u00052\u0006\u0010\u0003\u001a\u00020\u0002H\u0087\b\u001a\u0015\u0010\u0006\u001a\u00020\u0001*\u00020\u00052\u0006\u0010\u0003\u001a\u00020\u0001H\u0087\b\u001a\u0015\u0010\u0006\u001a\u00020\u0004*\u00020\u00052\u0006\u0010\u0003\u001a\u00020\u0004H\u0087\b\u001a\u0015\u0010\u0006\u001a\u00020\u0005*\u00020\u00052\u0006\u0010\u0003\u001a\u00020\u0005H\u0087\b\u00a8\u0006\t"}, d2={"floorDiv", "", "", "other", "", "", "mod", "", "", "kotlin-stdlib"}, xs="kotlin/NumbersKt")
class NumbersKt__FloorDivModKt
extends NumbersKt__BigIntegersKt {
    @SinceKotlin(version="1.5")
    @InlineOnly
    private static final int floorDiv(byte $this$floorDiv, byte other) {
        byte by = $this$floorDiv;
        byte by2 = other;
        int n2 = by / by2;
        if ((by ^ by2) < 0 && n2 * by2 != by) {
            --n2;
        }
        return n2;
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    private static final byte mod(byte $this$mod, byte other) {
        byte by = $this$mod;
        byte by2 = other;
        int n2 = by % by2;
        return (byte)(n2 + (by2 & ((n2 ^ by2) & (n2 | -n2)) >> 31));
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    private static final int floorDiv(byte $this$floorDiv, short other) {
        byte by = $this$floorDiv;
        short s2 = other;
        int n2 = by / s2;
        if ((by ^ s2) < 0 && n2 * s2 != by) {
            --n2;
        }
        return n2;
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    private static final short mod(byte $this$mod, short other) {
        byte by = $this$mod;
        short s2 = other;
        int n2 = by % s2;
        return (short)(n2 + (s2 & ((n2 ^ s2) & (n2 | -n2)) >> 31));
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    private static final int floorDiv(byte $this$floorDiv, int other) {
        byte by = $this$floorDiv;
        int n2 = by / other;
        if ((by ^ other) < 0 && n2 * other != by) {
            --n2;
        }
        return n2;
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    private static final int mod(byte $this$mod, int other) {
        int n2 = $this$mod % other;
        return n2 + (other & ((n2 ^ other) & (n2 | -n2)) >> 31);
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    private static final long floorDiv(byte $this$floorDiv, long other) {
        long l2 = $this$floorDiv;
        long l3 = l2 / other;
        if ((l2 ^ other) < 0L && l3 * other != l2) {
            l3 += -1L;
        }
        return l3;
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    private static final long mod(byte $this$mod, long other) {
        long l2 = (long)$this$mod % other;
        return l2 + (other & ((l2 ^ other) & (l2 | -l2)) >> 63);
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    private static final int floorDiv(short $this$floorDiv, byte other) {
        short s2 = $this$floorDiv;
        byte by = other;
        int n2 = s2 / by;
        if ((s2 ^ by) < 0 && n2 * by != s2) {
            --n2;
        }
        return n2;
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    private static final byte mod(short $this$mod, byte other) {
        short s2 = $this$mod;
        byte by = other;
        int n2 = s2 % by;
        return (byte)(n2 + (by & ((n2 ^ by) & (n2 | -n2)) >> 31));
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    private static final int floorDiv(short $this$floorDiv, short other) {
        short s2 = $this$floorDiv;
        short s3 = other;
        int n2 = s2 / s3;
        if ((s2 ^ s3) < 0 && n2 * s3 != s2) {
            --n2;
        }
        return n2;
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    private static final short mod(short $this$mod, short other) {
        short s2 = $this$mod;
        short s3 = other;
        int n2 = s2 % s3;
        return (short)(n2 + (s3 & ((n2 ^ s3) & (n2 | -n2)) >> 31));
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    private static final int floorDiv(short $this$floorDiv, int other) {
        short s2 = $this$floorDiv;
        int n2 = s2 / other;
        if ((s2 ^ other) < 0 && n2 * other != s2) {
            --n2;
        }
        return n2;
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    private static final int mod(short $this$mod, int other) {
        int n2 = $this$mod % other;
        return n2 + (other & ((n2 ^ other) & (n2 | -n2)) >> 31);
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    private static final long floorDiv(short $this$floorDiv, long other) {
        long l2 = $this$floorDiv;
        long l3 = l2 / other;
        if ((l2 ^ other) < 0L && l3 * other != l2) {
            l3 += -1L;
        }
        return l3;
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    private static final long mod(short $this$mod, long other) {
        long l2 = (long)$this$mod % other;
        return l2 + (other & ((l2 ^ other) & (l2 | -l2)) >> 63);
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    private static final int floorDiv(int $this$floorDiv, byte other) {
        int n2 = $this$floorDiv;
        byte by = other;
        int n3 = n2 / by;
        if ((n2 ^ by) < 0 && n3 * by != n2) {
            --n3;
        }
        return n3;
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    private static final byte mod(int $this$mod, byte other) {
        int n2 = $this$mod;
        byte by = other;
        int n3 = n2 % by;
        return (byte)(n3 + (by & ((n3 ^ by) & (n3 | -n3)) >> 31));
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    private static final int floorDiv(int $this$floorDiv, short other) {
        int n2 = $this$floorDiv;
        short s2 = other;
        int n3 = n2 / s2;
        if ((n2 ^ s2) < 0 && n3 * s2 != n2) {
            --n3;
        }
        return n3;
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    private static final short mod(int $this$mod, short other) {
        int n2 = $this$mod;
        short s2 = other;
        int n3 = n2 % s2;
        return (short)(n3 + (s2 & ((n3 ^ s2) & (n3 | -n3)) >> 31));
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    private static final int floorDiv(int $this$floorDiv, int other) {
        int q2 = $this$floorDiv / other;
        if (($this$floorDiv ^ other) < 0 && q2 * other != $this$floorDiv) {
            --q2;
        }
        return q2;
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    private static final int mod(int $this$mod, int other) {
        int r2 = $this$mod % other;
        return r2 + (other & ((r2 ^ other) & (r2 | -r2)) >> 31);
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    private static final long floorDiv(int $this$floorDiv, long other) {
        long l2 = $this$floorDiv;
        long l3 = l2 / other;
        if ((l2 ^ other) < 0L && l3 * other != l2) {
            l3 += -1L;
        }
        return l3;
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    private static final long mod(int $this$mod, long other) {
        long l2 = (long)$this$mod % other;
        return l2 + (other & ((l2 ^ other) & (l2 | -l2)) >> 63);
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    private static final long floorDiv(long $this$floorDiv, byte other) {
        long l2 = $this$floorDiv;
        long l3 = other;
        long l4 = l2 / l3;
        if ((l2 ^ l3) < 0L && l4 * l3 != l2) {
            l4 += -1L;
        }
        return l4;
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    private static final byte mod(long $this$mod, byte other) {
        long l2 = $this$mod;
        long l3 = other;
        long l4 = l2 % l3;
        return (byte)(l4 + (l3 & ((l4 ^ l3) & (l4 | -l4)) >> 63));
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    private static final long floorDiv(long $this$floorDiv, short other) {
        long l2 = $this$floorDiv;
        long l3 = other;
        long l4 = l2 / l3;
        if ((l2 ^ l3) < 0L && l4 * l3 != l2) {
            l4 += -1L;
        }
        return l4;
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    private static final short mod(long $this$mod, short other) {
        long l2 = $this$mod;
        long l3 = other;
        long l4 = l2 % l3;
        return (short)(l4 + (l3 & ((l4 ^ l3) & (l4 | -l4)) >> 63));
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    private static final long floorDiv(long $this$floorDiv, int other) {
        long l2 = $this$floorDiv;
        long l3 = other;
        long l4 = l2 / l3;
        if ((l2 ^ l3) < 0L && l4 * l3 != l2) {
            l4 += -1L;
        }
        return l4;
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    private static final int mod(long $this$mod, int other) {
        long l2 = $this$mod;
        long l3 = other;
        long l4 = l2 % l3;
        return (int)(l4 + (l3 & ((l4 ^ l3) & (l4 | -l4)) >> 63));
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    private static final long floorDiv(long $this$floorDiv, long other) {
        long q2 = $this$floorDiv / other;
        if (($this$floorDiv ^ other) < 0L && q2 * other != $this$floorDiv) {
            long l2 = q2;
            q2 = l2 + -1L;
        }
        return q2;
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    private static final long mod(long $this$mod, long other) {
        long r2 = $this$mod % other;
        return r2 + (other & ((r2 ^ other) & (r2 | -r2)) >> 63);
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    private static final float mod(float $this$mod, float other) {
        float r2 = $this$mod % other;
        return !(r2 == 0.0f) && !(Math.signum(r2) == Math.signum(other)) ? r2 + other : r2;
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    private static final double mod(float $this$mod, double other) {
        double d2 = (double)$this$mod % other;
        return !(d2 == 0.0) && !(Math.signum(d2) == Math.signum(other)) ? d2 + other : d2;
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    private static final double mod(double $this$mod, float other) {
        double d2 = $this$mod;
        double d3 = other;
        double d4 = d2 % d3;
        return !(d4 == 0.0) && !(Math.signum(d4) == Math.signum(d3)) ? d4 + d3 : d4;
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    private static final double mod(double $this$mod, double other) {
        double r2 = $this$mod % other;
        return !(r2 == 0.0) && !(Math.signum(r2) == Math.signum(other)) ? r2 + other : r2;
    }
}

