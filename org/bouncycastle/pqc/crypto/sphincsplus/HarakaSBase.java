/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.sphincsplus;

import org.bouncycastle.util.Arrays;

class HarakaSBase {
    protected long[][] haraka512_rc = new long[][]{{2652350495371256459L, -4767360454786055294L, -2778808723033108313L, -6138960262205972599L, 4944264682582508575L, 5312892415214084856L, 390034814247088728L, 2584105839607850161L}, {-2829930801980875922L, 9137660425067592590L, 7974068014816832049L, -4665944065725157058L, 2602240152241800734L, -1525694355931290902L, 8634660511727056099L, 1757945485816280992L}, {1181946526362588450L, -2765192619992380293L, 3395396416743122529L, -5116273100549372423L, -1285454309797503998L, -3363297609815171261L, -8360835858392998991L, -2371352336613968487L}, {-2500853454776756032L, 8465221333286591414L, 8817016078209461823L, 9067727467981428858L, 4244107674518258433L, -4347326460570889538L, 1711371409274742987L, 6486926172609168623L}, {1689001080716996467L, -491496126278250673L, 1273395568185090836L, 5805238412293617850L, -3441289770925384855L, 4592753210857527691L, 7062886034259989751L, -7974393977033172556L}, {-797818098819718290L, -41460260651793472L, 476036171179798187L, 7391697506481003962L, -855662275170689475L, -3489340839585811635L, -4891525734487956488L, 9110006695579921767L}, {-886938081943560790L, 4212830408327159617L, -3546674487567282635L, -1955379422127038289L, 3174578079917510314L, 5156046680874954380L, -318545805834821831L, -6176414008149462342L}, {2529785914229181047L, 2966313764524854080L, 6363694428402697361L, 8292109690175819701L, -8497546332135459587L, -3211108476154815616L, -5526938793786642321L, -4975969843627057770L}, {3357847021085574721L, -4764837212565187058L, -626391829400648692L, 2124133995575340009L, 7425858999829294301L, -3432032868905637771L, 1119301198758921294L, 1907812968586478892L}, {-8986524826712832802L, 3356175496741300052L, -5764600317639896362L, 4002747967109689317L, -8718925159733497197L, -1938063772587374661L, -8003749789895945835L, 7302960353763723932L}};
    protected int[][] haraka256_rc = new int[10][8];
    protected final byte[] buffer = new byte[64];
    protected int off = 0;

    protected HarakaSBase() {
    }

    protected void reset() {
        this.off = 0;
        Arrays.clear(this.buffer);
    }

    private void brRangeDec32Le(byte[] byArray, int[] nArray, int n2) {
        for (int i2 = 0; i2 < nArray.length; ++i2) {
            int n3 = n2 + (i2 << 2);
            nArray[i2] = byArray[n3] & 0xFF | byArray[n3 + 1] << 8 & 0xFF00 | byArray[n3 + 2] << 16 & 0xFF0000 | byArray[n3 + 3] << 24;
        }
    }

    protected void interleaveConstant(long[] lArray, byte[] byArray, int n2) {
        int[] nArray = new int[16];
        this.brRangeDec32Le(byArray, nArray, n2);
        for (int i2 = 0; i2 < 4; ++i2) {
            this.brAesCt64InterleaveIn(lArray, i2, nArray, i2 << 2);
        }
        this.brAesCt64Ortho(lArray);
    }

    protected void interleaveConstant32(int[] nArray, byte[] byArray, int n2) {
        for (int i2 = 0; i2 < 4; ++i2) {
            nArray[i2 << 1] = this.brDec32Le(byArray, n2 + (i2 << 2));
            nArray[(i2 << 1) + 1] = this.brDec32Le(byArray, n2 + (i2 << 2) + 16);
        }
        this.brAesCtOrtho(nArray);
    }

    private int brDec32Le(byte[] byArray, int n2) {
        return byArray[n2] & 0xFF | byArray[n2 + 1] << 8 & 0xFF00 | byArray[n2 + 2] << 16 & 0xFF0000 | byArray[n2 + 3] << 24;
    }

    protected void haraka512Perm(byte[] byArray) {
        int n2;
        int n3;
        int[] nArray = new int[16];
        long[] lArray = new long[8];
        this.brRangeDec32Le(this.buffer, nArray, 0);
        for (n3 = 0; n3 < 4; ++n3) {
            this.brAesCt64InterleaveIn(lArray, n3, nArray, n3 << 2);
        }
        this.brAesCt64Ortho(lArray);
        for (n3 = 0; n3 < 5; ++n3) {
            for (n2 = 0; n2 < 2; ++n2) {
                this.brAesCt64BitsliceSbox(lArray);
                this.shiftRows(lArray);
                this.mixColumns(lArray);
                this.addRoundKey(lArray, this.haraka512_rc[(n3 << 1) + n2]);
            }
            for (n2 = 0; n2 < 8; ++n2) {
                long l2 = lArray[n2];
                lArray[n2] = (l2 & 0x1000100010001L) << 5 | (l2 & 0x2000200020002L) << 12 | (l2 & 0x4000400040004L) >>> 1 | (l2 & 0x8000800080008L) << 6 | (l2 & 0x20002000200020L) << 9 | (l2 & 0x40004000400040L) >>> 4 | (l2 & 0x80008000800080L) << 3 | (l2 & 0x2100210021002100L) >>> 5 | (l2 & 0x210021002100210L) << 2 | (l2 & 0x800080008000800L) << 4 | (l2 & 0x1000100010001000L) >>> 12 | (l2 & 0x4000400040004000L) >>> 10 | (l2 & 0x8400840084008400L) >>> 3;
            }
        }
        this.brAesCt64Ortho(lArray);
        for (n3 = 0; n3 < 4; ++n3) {
            this.brAesCt64InterleaveOut(nArray, lArray, n3);
        }
        for (n3 = 0; n3 < 16; ++n3) {
            for (n2 = 0; n2 < 4; ++n2) {
                byArray[(n3 << 2) + n2] = (byte)(nArray[n3] >>> (n2 << 3) & 0xFF);
            }
        }
    }

    protected void haraka256Perm(byte[] byArray) {
        int n2;
        int[] nArray = new int[8];
        this.interleaveConstant32(nArray, this.buffer, 0);
        for (n2 = 0; n2 < 5; ++n2) {
            int n3;
            for (n3 = 0; n3 < 2; ++n3) {
                HarakaSBase.brAesCtBitsliceSbox(nArray);
                this.shiftRows32(nArray);
                this.mixColumns32(nArray);
                this.addRoundKey32(nArray, this.haraka256_rc[(n2 << 1) + n3]);
            }
            for (n3 = 0; n3 < 8; ++n3) {
                int n4 = nArray[n3];
                nArray[n3] = n4 & 0x81818181 | (n4 & 0x2020202) << 1 | (n4 & 0x4040404) << 2 | (n4 & 0x8080808) << 3 | (n4 & 0x10101010) >>> 3 | (n4 & 0x20202020) >>> 2 | (n4 & 0x40404040) >>> 1;
            }
        }
        this.brAesCtOrtho(nArray);
        for (n2 = 0; n2 < 4; ++n2) {
            this.brEnc32Le(byArray, nArray[n2 << 1], n2 << 2);
            this.brEnc32Le(byArray, nArray[(n2 << 1) + 1], (n2 << 2) + 16);
        }
    }

    private void brEnc32Le(byte[] byArray, int n2, int n3) {
        for (int i2 = 0; i2 < 4; ++i2) {
            byArray[n3 + i2] = (byte)(n2 >> (i2 << 3));
        }
    }

    private void brAesCt64InterleaveIn(long[] lArray, int n2, int[] nArray, int n3) {
        long l2 = (long)nArray[n3] & 0xFFFFFFFFL;
        long l3 = (long)nArray[n3 + 1] & 0xFFFFFFFFL;
        long l4 = (long)nArray[n3 + 2] & 0xFFFFFFFFL;
        long l5 = (long)nArray[n3 + 3] & 0xFFFFFFFFL;
        l2 |= l2 << 16;
        l3 |= l3 << 16;
        l4 |= l4 << 16;
        l5 |= l5 << 16;
        l2 &= 0xFFFF0000FFFFL;
        l3 &= 0xFFFF0000FFFFL;
        l4 &= 0xFFFF0000FFFFL;
        l5 &= 0xFFFF0000FFFFL;
        l2 |= l2 << 8;
        l3 |= l3 << 8;
        l4 |= l4 << 8;
        l5 |= l5 << 8;
        lArray[n2] = (l2 &= 0xFF00FF00FF00FFL) | (l4 &= 0xFF00FF00FF00FFL) << 8;
        lArray[n2 + 4] = (l3 &= 0xFF00FF00FF00FFL) | (l5 &= 0xFF00FF00FF00FFL) << 8;
    }

    private static void brAesCtBitsliceSbox(int[] nArray) {
        int n2 = nArray[7];
        int n3 = nArray[6];
        int n4 = nArray[5];
        int n5 = nArray[4];
        int n6 = nArray[3];
        int n7 = nArray[2];
        int n8 = nArray[1];
        int n9 = nArray[0];
        int n10 = n5 ^ n7;
        int n11 = n2 ^ n8;
        int n12 = n2 ^ n5;
        int n13 = n2 ^ n7;
        int n14 = n3 ^ n4;
        int n15 = n14 ^ n9;
        int n16 = n15 ^ n5;
        int n17 = n11 ^ n10;
        int n18 = n15 ^ n2;
        int n19 = n15 ^ n8;
        int n20 = n19 ^ n13;
        int n21 = n6 ^ n17;
        int n22 = n21 ^ n7;
        int n23 = n21 ^ n3;
        int n24 = n22 ^ n9;
        int n25 = n22 ^ n14;
        int n26 = n23 ^ n12;
        int n27 = n9 ^ n26;
        int n28 = n25 ^ n26;
        int n29 = n25 ^ n13;
        int n30 = n14 ^ n26;
        int n31 = n11 ^ n30;
        int n32 = n2 ^ n30;
        int n33 = n17 & n22;
        int n34 = n20 & n24;
        int n35 = n34 ^ n33;
        int n36 = n16 & n9;
        int n37 = n36 ^ n33;
        int n38 = n11 & n30;
        int n39 = n19 & n15;
        int n40 = n39 ^ n38;
        int n41 = n18 & n27;
        int n42 = n41 ^ n38;
        int n43 = n12 & n26;
        int n44 = n10 & n28;
        int n45 = n44 ^ n43;
        int n46 = n13 & n25;
        int n47 = n46 ^ n43;
        int n48 = n35 ^ n45;
        int n49 = n37 ^ n47;
        int n50 = n40 ^ n45;
        int n51 = n42 ^ n47;
        int n52 = n48 ^ n23;
        int n53 = n49 ^ n29;
        int n54 = n50 ^ n31;
        int n55 = n51 ^ n32;
        int n56 = n52 ^ n53;
        int n57 = n52 & n54;
        int n58 = n55 ^ n57;
        int n59 = n56 & n58;
        int n60 = n59 ^ n53;
        int n61 = n54 ^ n55;
        int n62 = n53 ^ n57;
        int n63 = n62 & n61;
        int n64 = n63 ^ n55;
        int n65 = n54 ^ n64;
        int n66 = n58 ^ n64;
        int n67 = n55 & n66;
        int n68 = n67 ^ n65;
        int n69 = n58 ^ n67;
        int n70 = n60 & n69;
        int n71 = n56 ^ n70;
        int n72 = n71 ^ n68;
        int n73 = n60 ^ n64;
        int n74 = n60 ^ n71;
        int n75 = n64 ^ n68;
        int n76 = n73 ^ n72;
        int n77 = n75 & n22;
        int n78 = n68 & n24;
        int n79 = n64 & n9;
        int n80 = n74 & n30;
        int n81 = n71 & n15;
        int n82 = n60 & n27;
        int n83 = n73 & n26;
        int n84 = n76 & n28;
        int n85 = n72 & n25;
        int n86 = n75 & n17;
        int n87 = n68 & n20;
        int n88 = n64 & n16;
        int n89 = n74 & n11;
        int n90 = n71 & n19;
        int n91 = n60 & n18;
        int n92 = n73 & n12;
        int n93 = n76 & n10;
        int n94 = n72 & n13;
        int n95 = n92 ^ n93;
        int n96 = n87 ^ n88;
        int n97 = n82 ^ n90;
        int n98 = n86 ^ n87;
        int n99 = n79 ^ n89;
        int n100 = n79 ^ n82;
        int n101 = n84 ^ n85;
        int n102 = n77 ^ n80;
        int n103 = n83 ^ n84;
        int n104 = n93 ^ n94;
        int n105 = n89 ^ n97;
        int n106 = n99 ^ n102;
        int n107 = n81 ^ n95;
        int n108 = n80 ^ n103;
        int n109 = n95 ^ n106;
        int n110 = n91 ^ n106;
        int n111 = n101 ^ n107;
        int n112 = n98 ^ n107;
        int n113 = n81 ^ n108;
        int n114 = n110 ^ n111;
        int n115 = n78 ^ n112;
        int n116 = n108 ^ n112;
        int n117 = n105 ^ ~n111;
        int n118 = n97 ^ ~n109;
        int n119 = n113 ^ n114;
        int n120 = n102 ^ n115;
        int n121 = n100 ^ n115;
        int n122 = n96 ^ n114;
        int n123 = n113 ^ ~n120;
        int n124 = n104 ^ ~n119;
        nArray[7] = n116;
        nArray[6] = n123;
        nArray[5] = n124;
        nArray[4] = n120;
        nArray[3] = n121;
        nArray[2] = n122;
        nArray[1] = n117;
        nArray[0] = n118;
    }

    private void shiftRows32(int[] nArray) {
        for (int i2 = 0; i2 < 8; ++i2) {
            int n2 = nArray[i2];
            nArray[i2] = n2 & 0xFF | (n2 & 0xFC00) >>> 2 | (n2 & 0x300) << 6 | (n2 & 0xF00000) >>> 4 | (n2 & 0xF0000) << 4 | (n2 & 0xC0000000) >>> 6 | (n2 & 0x3F000000) << 2;
        }
    }

    private void mixColumns32(int[] nArray) {
        int n2 = nArray[0];
        int n3 = nArray[1];
        int n4 = nArray[2];
        int n5 = nArray[3];
        int n6 = nArray[4];
        int n7 = nArray[5];
        int n8 = nArray[6];
        int n9 = nArray[7];
        int n10 = n2 >>> 8 | n2 << 24;
        int n11 = n3 >>> 8 | n3 << 24;
        int n12 = n4 >>> 8 | n4 << 24;
        int n13 = n5 >>> 8 | n5 << 24;
        int n14 = n6 >>> 8 | n6 << 24;
        int n15 = n7 >>> 8 | n7 << 24;
        int n16 = n8 >>> 8 | n8 << 24;
        int n17 = n9 >>> 8 | n9 << 24;
        nArray[0] = n9 ^ n17 ^ n10 ^ this.rotr16(n2 ^ n10);
        nArray[1] = n2 ^ n10 ^ n9 ^ n17 ^ n11 ^ this.rotr16(n3 ^ n11);
        nArray[2] = n3 ^ n11 ^ n12 ^ this.rotr16(n4 ^ n12);
        nArray[3] = n4 ^ n12 ^ n9 ^ n17 ^ n13 ^ this.rotr16(n5 ^ n13);
        nArray[4] = n5 ^ n13 ^ n9 ^ n17 ^ n14 ^ this.rotr16(n6 ^ n14);
        nArray[5] = n6 ^ n14 ^ n15 ^ this.rotr16(n7 ^ n15);
        nArray[6] = n7 ^ n15 ^ n16 ^ this.rotr16(n8 ^ n16);
        nArray[7] = n8 ^ n16 ^ n17 ^ this.rotr16(n9 ^ n17);
    }

    private void addRoundKey32(int[] nArray, int[] nArray2) {
        nArray[0] = nArray[0] ^ nArray2[0];
        nArray[1] = nArray[1] ^ nArray2[1];
        nArray[2] = nArray[2] ^ nArray2[2];
        nArray[3] = nArray[3] ^ nArray2[3];
        nArray[4] = nArray[4] ^ nArray2[4];
        nArray[5] = nArray[5] ^ nArray2[5];
        nArray[6] = nArray[6] ^ nArray2[6];
        nArray[7] = nArray[7] ^ nArray2[7];
    }

    private int rotr16(int n2) {
        return n2 << 16 | n2 >>> 16;
    }

    private void brAesCt64Ortho(long[] lArray) {
        this.Swapn(lArray, 1, 0, 1);
        this.Swapn(lArray, 1, 2, 3);
        this.Swapn(lArray, 1, 4, 5);
        this.Swapn(lArray, 1, 6, 7);
        this.Swapn(lArray, 2, 0, 2);
        this.Swapn(lArray, 2, 1, 3);
        this.Swapn(lArray, 2, 4, 6);
        this.Swapn(lArray, 2, 5, 7);
        this.Swapn(lArray, 4, 0, 4);
        this.Swapn(lArray, 4, 1, 5);
        this.Swapn(lArray, 4, 2, 6);
        this.Swapn(lArray, 4, 3, 7);
    }

    private void brAesCtOrtho(int[] nArray) {
        this.Swapn32(nArray, 1, 0, 1);
        this.Swapn32(nArray, 1, 2, 3);
        this.Swapn32(nArray, 1, 4, 5);
        this.Swapn32(nArray, 1, 6, 7);
        this.Swapn32(nArray, 2, 0, 2);
        this.Swapn32(nArray, 2, 1, 3);
        this.Swapn32(nArray, 2, 4, 6);
        this.Swapn32(nArray, 2, 5, 7);
        this.Swapn32(nArray, 4, 0, 4);
        this.Swapn32(nArray, 4, 1, 5);
        this.Swapn32(nArray, 4, 2, 6);
        this.Swapn32(nArray, 4, 3, 7);
    }

    private void Swapn32(int[] nArray, int n2, int n3, int n4) {
        int n5 = 0;
        int n6 = 0;
        switch (n2) {
            case 1: {
                n5 = 0x55555555;
                n6 = -1431655766;
                break;
            }
            case 2: {
                n5 = 0x33333333;
                n6 = -858993460;
                break;
            }
            case 4: {
                n5 = 0xF0F0F0F;
                n6 = -252645136;
            }
        }
        int n7 = nArray[n3];
        int n8 = nArray[n4];
        nArray[n3] = n7 & n5 | (n8 & n5) << n2;
        nArray[n4] = (n7 & n6) >>> n2 | n8 & n6;
    }

    private void Swapn(long[] lArray, int n2, int n3, int n4) {
        long l2 = 0L;
        long l3 = 0L;
        switch (n2) {
            case 1: {
                l2 = 0x5555555555555555L;
                l3 = -6148914691236517206L;
                break;
            }
            case 2: {
                l2 = 0x3333333333333333L;
                l3 = -3689348814741910324L;
                break;
            }
            case 4: {
                l2 = 0xF0F0F0F0F0F0F0FL;
                l3 = -1085102592571150096L;
                break;
            }
            default: {
                return;
            }
        }
        long l4 = lArray[n3];
        long l5 = lArray[n4];
        lArray[n3] = l4 & l2 | (l5 & l2) << n2;
        lArray[n4] = (l4 & l3) >>> n2 | l5 & l3;
    }

    private void brAesCt64BitsliceSbox(long[] lArray) {
        long l2 = lArray[7];
        long l3 = lArray[6];
        long l4 = lArray[5];
        long l5 = lArray[4];
        long l6 = lArray[3];
        long l7 = lArray[2];
        long l8 = lArray[1];
        long l9 = lArray[0];
        long l10 = l5 ^ l7;
        long l11 = l2 ^ l8;
        long l12 = l2 ^ l5;
        long l13 = l2 ^ l7;
        long l14 = l3 ^ l4;
        long l15 = l14 ^ l9;
        long l16 = l15 ^ l5;
        long l17 = l11 ^ l10;
        long l18 = l15 ^ l2;
        long l19 = l15 ^ l8;
        long l20 = l19 ^ l13;
        long l21 = l6 ^ l17;
        long l22 = l21 ^ l7;
        long l23 = l21 ^ l3;
        long l24 = l22 ^ l9;
        long l25 = l22 ^ l14;
        long l26 = l23 ^ l12;
        long l27 = l9 ^ l26;
        long l28 = l25 ^ l26;
        long l29 = l25 ^ l13;
        long l30 = l14 ^ l26;
        long l31 = l11 ^ l30;
        long l32 = l2 ^ l30;
        long l33 = l17 & l22;
        long l34 = l20 & l24;
        long l35 = l34 ^ l33;
        long l36 = l16 & l9;
        long l37 = l36 ^ l33;
        long l38 = l11 & l30;
        long l39 = l19 & l15;
        long l40 = l39 ^ l38;
        long l41 = l18 & l27;
        long l42 = l41 ^ l38;
        long l43 = l12 & l26;
        long l44 = l10 & l28;
        long l45 = l44 ^ l43;
        long l46 = l13 & l25;
        long l47 = l46 ^ l43;
        long l48 = l35 ^ l45;
        long l49 = l37 ^ l47;
        long l50 = l40 ^ l45;
        long l51 = l42 ^ l47;
        long l52 = l48 ^ l23;
        long l53 = l49 ^ l29;
        long l54 = l50 ^ l31;
        long l55 = l51 ^ l32;
        long l56 = l52 ^ l53;
        long l57 = l52 & l54;
        long l58 = l55 ^ l57;
        long l59 = l56 & l58;
        long l60 = l59 ^ l53;
        long l61 = l54 ^ l55;
        long l62 = l53 ^ l57;
        long l63 = l62 & l61;
        long l64 = l63 ^ l55;
        long l65 = l54 ^ l64;
        long l66 = l58 ^ l64;
        long l67 = l55 & l66;
        long l68 = l67 ^ l65;
        long l69 = l58 ^ l67;
        long l70 = l60 & l69;
        long l71 = l56 ^ l70;
        long l72 = l71 ^ l68;
        long l73 = l60 ^ l64;
        long l74 = l60 ^ l71;
        long l75 = l64 ^ l68;
        long l76 = l73 ^ l72;
        long l77 = l75 & l22;
        long l78 = l68 & l24;
        long l79 = l64 & l9;
        long l80 = l74 & l30;
        long l81 = l71 & l15;
        long l82 = l60 & l27;
        long l83 = l73 & l26;
        long l84 = l76 & l28;
        long l85 = l72 & l25;
        long l86 = l75 & l17;
        long l87 = l68 & l20;
        long l88 = l64 & l16;
        long l89 = l74 & l11;
        long l90 = l71 & l19;
        long l91 = l60 & l18;
        long l92 = l73 & l12;
        long l93 = l76 & l10;
        long l94 = l72 & l13;
        long l95 = l92 ^ l93;
        long l96 = l87 ^ l88;
        long l97 = l82 ^ l90;
        long l98 = l86 ^ l87;
        long l99 = l79 ^ l89;
        long l100 = l79 ^ l82;
        long l101 = l84 ^ l85;
        long l102 = l77 ^ l80;
        long l103 = l83 ^ l84;
        long l104 = l93 ^ l94;
        long l105 = l89 ^ l97;
        long l106 = l99 ^ l102;
        long l107 = l81 ^ l95;
        long l108 = l80 ^ l103;
        long l109 = l95 ^ l106;
        long l110 = l91 ^ l106;
        long l111 = l101 ^ l107;
        long l112 = l98 ^ l107;
        long l113 = l81 ^ l108;
        long l114 = l110 ^ l111;
        long l115 = l78 ^ l112;
        long l116 = l108 ^ l112;
        long l117 = l105 ^ (l111 ^ 0xFFFFFFFFFFFFFFFFL);
        long l118 = l97 ^ (l109 ^ 0xFFFFFFFFFFFFFFFFL);
        long l119 = l113 ^ l114;
        long l120 = l102 ^ l115;
        long l121 = l100 ^ l115;
        long l122 = l96 ^ l114;
        long l123 = l113 ^ (l120 ^ 0xFFFFFFFFFFFFFFFFL);
        long l124 = l104 ^ (l119 ^ 0xFFFFFFFFFFFFFFFFL);
        lArray[7] = l116;
        lArray[6] = l123;
        lArray[5] = l124;
        lArray[4] = l120;
        lArray[3] = l121;
        lArray[2] = l122;
        lArray[1] = l117;
        lArray[0] = l118;
    }

    private void shiftRows(long[] lArray) {
        for (int i2 = 0; i2 < lArray.length; ++i2) {
            long l2 = lArray[i2];
            lArray[i2] = l2 & 0xFFFFL | (l2 & 0xFFF00000L) >>> 4 | (l2 & 0xF0000L) << 12 | (l2 & 0xFF0000000000L) >>> 8 | (l2 & 0xFF00000000L) << 8 | (l2 & 0xF000000000000000L) >>> 12 | (l2 & 0xFFF000000000000L) << 4;
        }
    }

    private void mixColumns(long[] lArray) {
        long l2 = lArray[0];
        long l3 = lArray[1];
        long l4 = lArray[2];
        long l5 = lArray[3];
        long l6 = lArray[4];
        long l7 = lArray[5];
        long l8 = lArray[6];
        long l9 = lArray[7];
        long l10 = l2 >>> 16 | l2 << 48;
        long l11 = l3 >>> 16 | l3 << 48;
        long l12 = l4 >>> 16 | l4 << 48;
        long l13 = l5 >>> 16 | l5 << 48;
        long l14 = l6 >>> 16 | l6 << 48;
        long l15 = l7 >>> 16 | l7 << 48;
        long l16 = l8 >>> 16 | l8 << 48;
        long l17 = l9 >>> 16 | l9 << 48;
        lArray[0] = l9 ^ l17 ^ l10 ^ this.rotr32(l2 ^ l10);
        lArray[1] = l2 ^ l10 ^ l9 ^ l17 ^ l11 ^ this.rotr32(l3 ^ l11);
        lArray[2] = l3 ^ l11 ^ l12 ^ this.rotr32(l4 ^ l12);
        lArray[3] = l4 ^ l12 ^ l9 ^ l17 ^ l13 ^ this.rotr32(l5 ^ l13);
        lArray[4] = l5 ^ l13 ^ l9 ^ l17 ^ l14 ^ this.rotr32(l6 ^ l14);
        lArray[5] = l6 ^ l14 ^ l15 ^ this.rotr32(l7 ^ l15);
        lArray[6] = l7 ^ l15 ^ l16 ^ this.rotr32(l8 ^ l16);
        lArray[7] = l8 ^ l16 ^ l17 ^ this.rotr32(l9 ^ l17);
    }

    private long rotr32(long l2) {
        return l2 << 32 | l2 >>> 32;
    }

    private void addRoundKey(long[] lArray, long[] lArray2) {
        lArray[0] = lArray[0] ^ lArray2[0];
        lArray[1] = lArray[1] ^ lArray2[1];
        lArray[2] = lArray[2] ^ lArray2[2];
        lArray[3] = lArray[3] ^ lArray2[3];
        lArray[4] = lArray[4] ^ lArray2[4];
        lArray[5] = lArray[5] ^ lArray2[5];
        lArray[6] = lArray[6] ^ lArray2[6];
        lArray[7] = lArray[7] ^ lArray2[7];
    }

    private void brAesCt64InterleaveOut(int[] nArray, long[] lArray, int n2) {
        long l2 = lArray[n2] & 0xFF00FF00FF00FFL;
        long l3 = lArray[n2 + 4] & 0xFF00FF00FF00FFL;
        long l4 = lArray[n2] >>> 8 & 0xFF00FF00FF00FFL;
        long l5 = lArray[n2 + 4] >>> 8 & 0xFF00FF00FF00FFL;
        l2 |= l2 >>> 8;
        l3 |= l3 >>> 8;
        l4 |= l4 >>> 8;
        l5 |= l5 >>> 8;
        nArray[n2 <<= 2] = (int)((l2 &= 0xFFFF0000FFFFL) | l2 >>> 16);
        nArray[n2 + 1] = (int)((l3 &= 0xFFFF0000FFFFL) | l3 >>> 16);
        nArray[n2 + 2] = (int)((l4 &= 0xFFFF0000FFFFL) | l4 >>> 16);
        nArray[n2 + 3] = (int)((l5 &= 0xFFFF0000FFFFL) | l5 >>> 16);
    }

    protected static void xor(byte[] byArray, int n2, byte[] byArray2, int n3, byte[] byArray3, int n4, int n5) {
        for (int i2 = 0; i2 < n5; ++i2) {
            byArray3[n4 + i2] = (byte)(byArray[n2 + i2] ^ byArray2[n3 + i2]);
        }
    }
}

