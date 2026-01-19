/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.falcon;

import org.bouncycastle.pqc.crypto.falcon.FPREngine;
import org.bouncycastle.pqc.crypto.falcon.FalconCodec;
import org.bouncycastle.pqc.crypto.falcon.FalconFFT;
import org.bouncycastle.pqc.crypto.falcon.FalconFPR;
import org.bouncycastle.pqc.crypto.falcon.FalconSmallPrime;
import org.bouncycastle.pqc.crypto.falcon.FalconSmallPrimeList;
import org.bouncycastle.pqc.crypto.falcon.FalconVrfy;
import org.bouncycastle.pqc.crypto.falcon.SHAKE256;

class FalconKeyGen {
    FPREngine fpr;
    FalconSmallPrimeList primes;
    FalconFFT fft;
    FalconCodec codec;
    FalconVrfy vrfy;
    private short[] REV10 = new short[]{0, 512, 256, 768, 128, 640, 384, 896, 64, 576, 320, 832, 192, 704, 448, 960, 32, 544, 288, 800, 160, 672, 416, 928, 96, 608, 352, 864, 224, 736, 480, 992, 16, 528, 272, 784, 144, 656, 400, 912, 80, 592, 336, 848, 208, 720, 464, 976, 48, 560, 304, 816, 176, 688, 432, 944, 112, 624, 368, 880, 240, 752, 496, 1008, 8, 520, 264, 776, 136, 648, 392, 904, 72, 584, 328, 840, 200, 712, 456, 968, 40, 552, 296, 808, 168, 680, 424, 936, 104, 616, 360, 872, 232, 744, 488, 1000, 24, 536, 280, 792, 152, 664, 408, 920, 88, 600, 344, 856, 216, 728, 472, 984, 56, 568, 312, 824, 184, 696, 440, 952, 120, 632, 376, 888, 248, 760, 504, 1016, 4, 516, 260, 772, 132, 644, 388, 900, 68, 580, 324, 836, 196, 708, 452, 964, 36, 548, 292, 804, 164, 676, 420, 932, 100, 612, 356, 868, 228, 740, 484, 996, 20, 532, 276, 788, 148, 660, 404, 916, 84, 596, 340, 852, 212, 724, 468, 980, 52, 564, 308, 820, 180, 692, 436, 948, 116, 628, 372, 884, 244, 756, 500, 1012, 12, 524, 268, 780, 140, 652, 396, 908, 76, 588, 332, 844, 204, 716, 460, 972, 44, 556, 300, 812, 172, 684, 428, 940, 108, 620, 364, 876, 236, 748, 492, 1004, 28, 540, 284, 796, 156, 668, 412, 924, 92, 604, 348, 860, 220, 732, 476, 988, 60, 572, 316, 828, 188, 700, 444, 956, 124, 636, 380, 892, 252, 764, 508, 1020, 2, 514, 258, 770, 130, 642, 386, 898, 66, 578, 322, 834, 194, 706, 450, 962, 34, 546, 290, 802, 162, 674, 418, 930, 98, 610, 354, 866, 226, 738, 482, 994, 18, 530, 274, 786, 146, 658, 402, 914, 82, 594, 338, 850, 210, 722, 466, 978, 50, 562, 306, 818, 178, 690, 434, 946, 114, 626, 370, 882, 242, 754, 498, 1010, 10, 522, 266, 778, 138, 650, 394, 906, 74, 586, 330, 842, 202, 714, 458, 970, 42, 554, 298, 810, 170, 682, 426, 938, 106, 618, 362, 874, 234, 746, 490, 1002, 26, 538, 282, 794, 154, 666, 410, 922, 90, 602, 346, 858, 218, 730, 474, 986, 58, 570, 314, 826, 186, 698, 442, 954, 122, 634, 378, 890, 250, 762, 506, 1018, 6, 518, 262, 774, 134, 646, 390, 902, 70, 582, 326, 838, 198, 710, 454, 966, 38, 550, 294, 806, 166, 678, 422, 934, 102, 614, 358, 870, 230, 742, 486, 998, 22, 534, 278, 790, 150, 662, 406, 918, 86, 598, 342, 854, 214, 726, 470, 982, 54, 566, 310, 822, 182, 694, 438, 950, 118, 630, 374, 886, 246, 758, 502, 1014, 14, 526, 270, 782, 142, 654, 398, 910, 78, 590, 334, 846, 206, 718, 462, 974, 46, 558, 302, 814, 174, 686, 430, 942, 110, 622, 366, 878, 238, 750, 494, 1006, 30, 542, 286, 798, 158, 670, 414, 926, 94, 606, 350, 862, 222, 734, 478, 990, 62, 574, 318, 830, 190, 702, 446, 958, 126, 638, 382, 894, 254, 766, 510, 1022, 1, 513, 257, 769, 129, 641, 385, 897, 65, 577, 321, 833, 193, 705, 449, 961, 33, 545, 289, 801, 161, 673, 417, 929, 97, 609, 353, 865, 225, 737, 481, 993, 17, 529, 273, 785, 145, 657, 401, 913, 81, 593, 337, 849, 209, 721, 465, 977, 49, 561, 305, 817, 177, 689, 433, 945, 113, 625, 369, 881, 241, 753, 497, 1009, 9, 521, 265, 777, 137, 649, 393, 905, 73, 585, 329, 841, 201, 713, 457, 969, 41, 553, 297, 809, 169, 681, 425, 937, 105, 617, 361, 873, 233, 745, 489, 1001, 25, 537, 281, 793, 153, 665, 409, 921, 89, 601, 345, 857, 217, 729, 473, 985, 57, 569, 313, 825, 185, 697, 441, 953, 121, 633, 377, 889, 249, 761, 505, 1017, 5, 517, 261, 773, 133, 645, 389, 901, 69, 581, 325, 837, 197, 709, 453, 965, 37, 549, 293, 805, 165, 677, 421, 933, 101, 613, 357, 869, 229, 741, 485, 997, 21, 533, 277, 789, 149, 661, 405, 917, 85, 597, 341, 853, 213, 725, 469, 981, 53, 565, 309, 821, 181, 693, 437, 949, 117, 629, 373, 885, 245, 757, 501, 1013, 13, 525, 269, 781, 141, 653, 397, 909, 77, 589, 333, 845, 205, 717, 461, 973, 45, 557, 301, 813, 173, 685, 429, 941, 109, 621, 365, 877, 237, 749, 493, 1005, 29, 541, 285, 797, 157, 669, 413, 925, 93, 605, 349, 861, 221, 733, 477, 989, 61, 573, 317, 829, 189, 701, 445, 957, 125, 637, 381, 893, 253, 765, 509, 1021, 3, 515, 259, 771, 131, 643, 387, 899, 67, 579, 323, 835, 195, 707, 451, 963, 35, 547, 291, 803, 163, 675, 419, 931, 99, 611, 355, 867, 227, 739, 483, 995, 19, 531, 275, 787, 147, 659, 403, 915, 83, 595, 339, 851, 211, 723, 467, 979, 51, 563, 307, 819, 179, 691, 435, 947, 115, 627, 371, 883, 243, 755, 499, 1011, 11, 523, 267, 779, 139, 651, 395, 907, 75, 587, 331, 843, 203, 715, 459, 971, 43, 555, 299, 811, 171, 683, 427, 939, 107, 619, 363, 875, 235, 747, 491, 1003, 27, 539, 283, 795, 155, 667, 411, 923, 91, 603, 347, 859, 219, 731, 475, 987, 59, 571, 315, 827, 187, 699, 443, 955, 123, 635, 379, 891, 251, 763, 507, 1019, 7, 519, 263, 775, 135, 647, 391, 903, 71, 583, 327, 839, 199, 711, 455, 967, 39, 551, 295, 807, 167, 679, 423, 935, 103, 615, 359, 871, 231, 743, 487, 999, 23, 535, 279, 791, 151, 663, 407, 919, 87, 599, 343, 855, 215, 727, 471, 983, 55, 567, 311, 823, 183, 695, 439, 951, 119, 631, 375, 887, 247, 759, 503, 1015, 15, 527, 271, 783, 143, 655, 399, 911, 79, 591, 335, 847, 207, 719, 463, 975, 47, 559, 303, 815, 175, 687, 431, 943, 111, 623, 367, 879, 239, 751, 495, 1007, 31, 543, 287, 799, 159, 671, 415, 927, 95, 607, 351, 863, 223, 735, 479, 991, 63, 575, 319, 831, 191, 703, 447, 959, 127, 639, 383, 895, 255, 767, 511, 1023};
    final long[] gauss_1024_12289 = new long[]{1283868770400643928L, 6416574995475331444L, 4078260278032692663L, 2353523259288686585L, 1227179971273316331L, 575931623374121527L, 242543240509105209L, 91437049221049666L, 30799446349977173L, 9255276791179340L, 2478152334826140L, 590642893610164L, 125206034929641L, 23590435911403L, 3948334035941L, 586753615614L, 77391054539L, 9056793210L, 940121950L, 86539696L, 7062824L, 510971L, 32764L, 1862L, 94L, 4L, 0L};
    final int[] MAX_BL_SMALL = new int[]{1, 1, 2, 2, 4, 7, 14, 27, 53, 106, 209};
    final int[] MAX_BL_LARGE = new int[]{2, 2, 5, 7, 12, 21, 40, 78, 157, 308};
    final int[] bitlength_avg = new int[]{4, 11, 24, 50, 102, 202, 401, 794, 1577, 3138, 6308};
    final int[] bitlength_std = new int[]{0, 1, 1, 1, 1, 2, 4, 5, 8, 13, 25};
    final int DEPTH_INT_FG = 4;

    FalconKeyGen() {
        this.fpr = new FPREngine();
        this.primes = new FalconSmallPrimeList();
        this.fft = new FalconFFT();
        this.codec = new FalconCodec();
        this.vrfy = new FalconVrfy();
    }

    private static int mkn(int n2) {
        return 1 << n2;
    }

    int modp_set(int n2, int n3) {
        int n4 = n2;
        n4 += n3 & -(n4 >>> 31);
        return n4;
    }

    int modp_norm(int n2, int n3) {
        return n2 - (n3 & (n2 - (n3 + 1 >>> 1) >>> 31) - 1);
    }

    int modp_ninv31(int n2) {
        int n3 = 2 - n2;
        n3 *= 2 - n2 * n3;
        n3 *= 2 - n2 * n3;
        n3 *= 2 - n2 * n3;
        n3 *= 2 - n2 * n3;
        return Integer.MAX_VALUE & -n3;
    }

    int modp_R(int n2) {
        return Integer.MIN_VALUE - n2;
    }

    int modp_add(int n2, int n3, int n4) {
        int n5 = n2 + n3 - n4;
        n5 += n4 & -(n5 >>> 31);
        return n5;
    }

    int modp_sub(int n2, int n3, int n4) {
        int n5 = n2 - n3;
        n5 += n4 & -(n5 >>> 31);
        return n5;
    }

    int modp_montymul(int n2, int n3, int n4, int n5) {
        long l2 = this.toUnsignedLong(n2) * this.toUnsignedLong(n3);
        long l3 = (l2 * (long)n5 & this.toUnsignedLong(Integer.MAX_VALUE)) * (long)n4;
        int n6 = (int)(l2 + l3 >>> 31) - n4;
        n6 += n4 & -(n6 >>> 31);
        return n6;
    }

    int modp_R2(int n2, int n3) {
        int n4 = this.modp_R(n2);
        n4 = this.modp_add(n4, n4, n2);
        n4 = this.modp_montymul(n4, n4, n2, n3);
        n4 = this.modp_montymul(n4, n4, n2, n3);
        n4 = this.modp_montymul(n4, n4, n2, n3);
        n4 = this.modp_montymul(n4, n4, n2, n3);
        n4 = this.modp_montymul(n4, n4, n2, n3);
        n4 = n4 + (n2 & -(n4 & 1)) >>> 1;
        return n4;
    }

    int modp_Rx(int n2, int n3, int n4, int n5) {
        --n2;
        int n6 = n5;
        int n7 = this.modp_R(n3);
        int n8 = 0;
        while (1 << n8 <= n2) {
            if ((n2 & 1 << n8) != 0) {
                n7 = this.modp_montymul(n7, n6, n3, n4);
            }
            n6 = this.modp_montymul(n6, n6, n3, n4);
            ++n8;
        }
        return n7;
    }

    int modp_div(int n2, int n3, int n4, int n5, int n6) {
        int n7 = n4 - 2;
        int n8 = n6;
        for (int i2 = 30; i2 >= 0; --i2) {
            n8 = this.modp_montymul(n8, n8, n4, n5);
            int n9 = this.modp_montymul(n8, n3, n4, n5);
            n8 ^= (n8 ^ n9) & -(n7 >>> i2 & 1);
        }
        n8 = this.modp_montymul(n8, 1, n4, n5);
        return this.modp_montymul(n2, n8, n4, n5);
    }

    void modp_mkgm2(int[] nArray, int n2, int[] nArray2, int n3, int n4, int n5, int n6, int n7) {
        int n8;
        int n9;
        int n10 = FalconKeyGen.mkn(n4);
        int n11 = this.modp_R2(n6, n7);
        n5 = this.modp_montymul(n5, n11, n6, n7);
        for (n9 = n4; n9 < 10; ++n9) {
            n5 = this.modp_montymul(n5, n5, n6, n7);
        }
        int n12 = this.modp_div(n11, n5, n6, n7, this.modp_R(n6));
        n9 = 10 - n4;
        int n13 = n8 = this.modp_R(n6);
        for (int i2 = 0; i2 < n10; ++i2) {
            short s2 = this.REV10[i2 << n9];
            nArray[n2 + s2] = n13;
            nArray2[n3 + s2] = n8;
            n13 = this.modp_montymul(n13, n5, n6, n7);
            n8 = this.modp_montymul(n8, n12, n6, n7);
        }
    }

    void modp_NTT2_ext(int[] nArray, int n2, int n3, int[] nArray2, int n4, int n5, int n6, int n7) {
        int n8;
        if (n5 == 0) {
            return;
        }
        int n9 = n8 = FalconKeyGen.mkn(n5);
        for (int i2 = 1; i2 < n8; i2 <<= 1) {
            int n10 = n9 >> 1;
            int n11 = 0;
            int n12 = 0;
            while (n11 < i2) {
                int n13 = nArray2[n4 + i2 + n11];
                int n14 = n2 + n12 * n3;
                int n15 = n14 + n10 * n3;
                int n16 = 0;
                while (n16 < n10) {
                    int n17 = nArray[n14];
                    int n18 = this.modp_montymul(nArray[n15], n13, n6, n7);
                    nArray[n14] = this.modp_add(n17, n18, n6);
                    nArray[n15] = this.modp_sub(n17, n18, n6);
                    ++n16;
                    n14 += n3;
                    n15 += n3;
                }
                ++n11;
                n12 += n9;
            }
            n9 = n10;
        }
    }

    void modp_iNTT2_ext(int[] nArray, int n2, int n3, int[] nArray2, int n4, int n5, int n6, int n7) {
        if (n5 == 0) {
            return;
        }
        int n8 = FalconKeyGen.mkn(n5);
        int n9 = 1;
        for (int i2 = n8; i2 > 1; i2 >>= 1) {
            int n10 = i2 >> 1;
            int n11 = n9 << 1;
            int n12 = 0;
            int n13 = 0;
            while (n12 < n10) {
                int n14 = nArray2[n4 + n10 + n12];
                int n15 = n2 + n13 * n3;
                int n16 = n15 + n9 * n3;
                int n17 = 0;
                while (n17 < n9) {
                    int n18 = nArray[n15];
                    int n19 = nArray[n16];
                    nArray[n15] = this.modp_add(n18, n19, n6);
                    nArray[n16] = this.modp_montymul(this.modp_sub(n18, n19, n6), n14, n6, n7);
                    ++n17;
                    n15 += n3;
                    n16 += n3;
                }
                ++n12;
                n13 += n11;
            }
            n9 = n11;
        }
        int n20 = 1 << 31 - n5;
        int n21 = 0;
        int n22 = n2;
        while (n21 < n8) {
            nArray[n22] = this.modp_montymul(nArray[n22], n20, n6, n7);
            ++n21;
            n22 += n3;
        }
    }

    void modp_NTT2(int[] nArray, int n2, int[] nArray2, int n3, int n4, int n5, int n6) {
        this.modp_NTT2_ext(nArray, n2, 1, nArray2, n3, n4, n5, n6);
    }

    void modp_iNTT2(int[] nArray, int n2, int[] nArray2, int n3, int n4, int n5, int n6) {
        this.modp_iNTT2_ext(nArray, n2, 1, nArray2, n3, n4, n5, n6);
    }

    void modp_poly_rec_res(int[] nArray, int n2, int n3, int n4, int n5, int n6) {
        int n7 = 1 << n3 - 1;
        for (int i2 = 0; i2 < n7; ++i2) {
            int n8 = nArray[n2 + (i2 << 1) + 0];
            int n9 = nArray[n2 + (i2 << 1) + 1];
            nArray[n2 + i2] = this.modp_montymul(this.modp_montymul(n8, n9, n4, n5), n6, n4, n5);
        }
    }

    int zint_sub(int[] nArray, int n2, int[] nArray2, int n3, int n4, int n5) {
        int n6 = 0;
        int n7 = -n5;
        for (int i2 = 0; i2 < n4; ++i2) {
            int n8 = nArray[n2 + i2];
            int n9 = n8 - nArray2[n3 + i2] - n6;
            n6 = n9 >>> 31;
            n8 ^= (n9 & Integer.MAX_VALUE ^ n8) & n7;
            nArray[n2 + i2] = n8;
        }
        return n6;
    }

    int zint_mul_small(int[] nArray, int n2, int n3, int n4) {
        int n5 = 0;
        for (int i2 = 0; i2 < n3; ++i2) {
            long l2 = this.toUnsignedLong(nArray[n2 + i2]) * this.toUnsignedLong(n4) + (long)n5;
            nArray[n2 + i2] = (int)l2 & Integer.MAX_VALUE;
            n5 = (int)(l2 >> 31);
        }
        return n5;
    }

    int zint_mod_small_unsigned(int[] nArray, int n2, int n3, int n4, int n5, int n6) {
        int n7 = 0;
        int n8 = n3;
        while (n8-- > 0) {
            n7 = this.modp_montymul(n7, n6, n4, n5);
            int n9 = nArray[n2 + n8] - n4;
            n9 += n4 & -(n9 >>> 31);
            n7 = this.modp_add(n7, n9, n4);
        }
        return n7;
    }

    int zint_mod_small_signed(int[] nArray, int n2, int n3, int n4, int n5, int n6, int n7) {
        if (n3 == 0) {
            return 0;
        }
        int n8 = this.zint_mod_small_unsigned(nArray, n2, n3, n4, n5, n6);
        n8 = this.modp_sub(n8, n7 & -(nArray[n2 + n3 - 1] >>> 30), n4);
        return n8;
    }

    void zint_add_mul_small(int[] nArray, int n2, int[] nArray2, int n3, int n4, int n5) {
        int n6 = 0;
        for (int i2 = 0; i2 < n4; ++i2) {
            int n7 = nArray[n2 + i2];
            int n8 = nArray2[n3 + i2];
            long l2 = this.toUnsignedLong(n8) * this.toUnsignedLong(n5) + this.toUnsignedLong(n7) + this.toUnsignedLong(n6);
            nArray[n2 + i2] = (int)l2 & Integer.MAX_VALUE;
            n6 = (int)(l2 >>> 31);
        }
        nArray[n2 + n4] = n6;
    }

    void zint_norm_zero(int[] nArray, int n2, int[] nArray2, int n3, int n4) {
        int n5 = 0;
        int n6 = 0;
        int n7 = n4;
        while (n7-- > 0) {
            int n8 = nArray[n2 + n7];
            int n9 = nArray2[n3 + n7] >>> 1 | n6 << 30;
            n6 = nArray2[n3 + n7] & 1;
            int n10 = n9 - n8;
            n10 = -n10 >>> 31 | -(n10 >>> 31);
            n5 |= n10 & (n5 & 1) - 1;
        }
        this.zint_sub(nArray, n2, nArray2, n3, n4, n5 >>> 31);
    }

    void zint_rebuild_CRT(int[] nArray, int n2, int n3, int n4, int n5, FalconSmallPrime[] falconSmallPrimeArray, int n6, int[] nArray2, int n7) {
        int n8;
        int n9;
        nArray2[n7 + 0] = falconSmallPrimeArray[0].p;
        for (n9 = 1; n9 < n3; ++n9) {
            int n10 = falconSmallPrimeArray[n9].p;
            int n11 = falconSmallPrimeArray[n9].s;
            int n12 = this.modp_ninv31(n10);
            int n13 = this.modp_R2(n10, n12);
            int n14 = 0;
            n8 = n2;
            while (n14 < n5) {
                int n15 = nArray[n8 + n9];
                int n16 = this.zint_mod_small_unsigned(nArray, n8, n9, n10, n12, n13);
                int n17 = this.modp_montymul(n11, this.modp_sub(n15, n16, n10), n10, n12);
                this.zint_add_mul_small(nArray, n8, nArray2, n7, n9, n17);
                ++n14;
                n8 += n4;
            }
            nArray2[n7 + n9] = this.zint_mul_small(nArray2, n7, n9, n10);
        }
        if (n6 != 0) {
            n9 = 0;
            n8 = n2;
            while (n9 < n5) {
                this.zint_norm_zero(nArray, n8, nArray2, n7, n3);
                ++n9;
                n8 += n4;
            }
        }
    }

    void zint_negate(int[] nArray, int n2, int n3, int n4) {
        int n5 = n4;
        int n6 = -n4 >>> 1;
        for (int i2 = 0; i2 < n3; ++i2) {
            int n7 = nArray[n2 + i2];
            n7 = (n7 ^ n6) + n5;
            nArray[n2 + i2] = n7 & Integer.MAX_VALUE;
            n5 = n7 >>> 31;
        }
    }

    int zint_co_reduce(int[] nArray, int n2, int[] nArray2, int n3, int n4, long l2, long l3, long l4, long l5) {
        long l6 = 0L;
        long l7 = 0L;
        for (int i2 = 0; i2 < n4; ++i2) {
            int n5 = nArray[n2 + i2];
            int n6 = nArray2[n3 + i2];
            long l8 = (long)n5 * l2 + (long)n6 * l3 + l6;
            long l9 = (long)n5 * l4 + (long)n6 * l5 + l7;
            if (i2 > 0) {
                nArray[n2 + i2 - 1] = (int)l8 & Integer.MAX_VALUE;
                nArray2[n3 + i2 - 1] = (int)l9 & Integer.MAX_VALUE;
            }
            l6 = l8 >> 31;
            l7 = l9 >> 31;
        }
        nArray[n2 + n4 - 1] = (int)l6;
        nArray2[n3 + n4 - 1] = (int)l7;
        int n7 = (int)(l6 >>> 63);
        int n8 = (int)(l7 >>> 63);
        this.zint_negate(nArray, n2, n4, n7);
        this.zint_negate(nArray2, n3, n4, n8);
        return n7 | n8 << 1;
    }

    void zint_finish_mod(int[] nArray, int n2, int n3, int[] nArray2, int n4, int n5) {
        int n6;
        int n7 = 0;
        for (n6 = 0; n6 < n3; ++n6) {
            n7 = nArray[n2 + n6] - nArray2[n4 + n6] - n7 >>> 31;
        }
        int n8 = -n5 >>> 1;
        int n9 = -(n5 | 1 - n7);
        n7 = n5;
        for (n6 = 0; n6 < n3; ++n6) {
            int n10 = nArray[n2 + n6];
            int n11 = (nArray2[n4 + n6] ^ n8) & n9;
            n10 = n10 - n11 - n7;
            nArray[n2 + n6] = n10 & Integer.MAX_VALUE;
            n7 = n10 >>> 31;
        }
    }

    void zint_co_reduce_mod(int[] nArray, int n2, int[] nArray2, int n3, int[] nArray3, int n4, int n5, int n6, long l2, long l3, long l4, long l5) {
        long l6 = 0L;
        long l7 = 0L;
        int n7 = (nArray[n2 + 0] * (int)l2 + nArray2[n3 + 0] * (int)l3) * n6 & Integer.MAX_VALUE;
        int n8 = (nArray[n2 + 0] * (int)l4 + nArray2[n3 + 0] * (int)l5) * n6 & Integer.MAX_VALUE;
        for (int i2 = 0; i2 < n5; ++i2) {
            int n9 = nArray[n2 + i2];
            int n10 = nArray2[n3 + i2];
            long l8 = (long)n9 * l2 + (long)n10 * l3 + (long)nArray3[n4 + i2] * this.toUnsignedLong(n7) + l6;
            long l9 = (long)n9 * l4 + (long)n10 * l5 + (long)nArray3[n4 + i2] * this.toUnsignedLong(n8) + l7;
            if (i2 > 0) {
                nArray[n2 + i2 - 1] = (int)l8 & Integer.MAX_VALUE;
                nArray2[n3 + i2 - 1] = (int)l9 & Integer.MAX_VALUE;
            }
            l6 = l8 >> 31;
            l7 = l9 >> 31;
        }
        nArray[n2 + n5 - 1] = (int)l6;
        nArray2[n3 + n5 - 1] = (int)l7;
        this.zint_finish_mod(nArray, n2, n5, nArray3, n4, (int)(l6 >>> 63));
        this.zint_finish_mod(nArray2, n3, n5, nArray3, n4, (int)(l7 >>> 63));
    }

    int zint_bezout(int[] nArray, int n2, int[] nArray2, int n3, int[] nArray3, int n4, int[] nArray4, int n5, int n6, int[] nArray5, int n7) {
        int n8;
        int n9;
        if (n6 == 0) {
            return 0;
        }
        int n10 = n2;
        int n11 = n3;
        int n12 = n7;
        int n13 = n12 + n6;
        int n14 = n13 + n6;
        int n15 = n14 + n6;
        int n16 = this.modp_ninv31(nArray3[n4 + 0]);
        int n17 = this.modp_ninv31(nArray4[n5 + 0]);
        System.arraycopy(nArray3, n4, nArray5, n14, n6);
        System.arraycopy(nArray4, n5, nArray5, n15, n6);
        nArray[n10 + 0] = 1;
        nArray2[n11 + 0] = 0;
        for (n9 = 1; n9 < n6; ++n9) {
            nArray[n10 + n9] = 0;
            nArray2[n11 + n9] = 0;
        }
        System.arraycopy(nArray4, n5, nArray5, n12, n6);
        System.arraycopy(nArray3, n4, nArray5, n13, n6);
        int n18 = n13 + 0;
        nArray5[n18] = nArray5[n18] - 1;
        for (int i2 = 62 * n6 + 30; i2 >= 30; i2 -= 30) {
            int n19;
            int n20;
            n9 = -1;
            int n21 = -1;
            int n22 = 0;
            int n23 = 0;
            int n24 = 0;
            int n25 = 0;
            n8 = n6;
            while (n8-- > 0) {
                n20 = nArray5[n14 + n8];
                n19 = nArray5[n15 + n8];
                n22 ^= (n22 ^ n20) & n9;
                n23 ^= (n23 ^ n20) & n21;
                n24 ^= (n24 ^ n19) & n9;
                n25 ^= (n25 ^ n19) & n21;
                n21 = n9;
                n9 &= ((n20 | n19) + Integer.MAX_VALUE >>> 31) - 1;
            }
            n23 |= n22 & n21;
            n25 |= n24 & n21;
            long l2 = (this.toUnsignedLong(n22 &= ~n21) << 31) + this.toUnsignedLong(n23);
            long l3 = (this.toUnsignedLong(n24 &= ~n21) << 31) + this.toUnsignedLong(n25);
            int n26 = nArray5[n14 + 0];
            int n27 = nArray5[n15 + 0];
            long l4 = 1L;
            long l5 = 0L;
            long l6 = 0L;
            long l7 = 1L;
            for (int i3 = 0; i3 < 31; ++i3) {
                long l8 = l3 - l2;
                n20 = (int)((l8 ^ (l2 ^ l3) & (l2 ^ l8)) >>> 63);
                n19 = n26 >> i3 & 1;
                int n28 = n27 >> i3 & 1;
                int n29 = n19 & n28 & n20;
                int n30 = n19 & n28 & ~n20;
                int n31 = n29 | n19 ^ 1;
                n26 -= n27 & -n29;
                l2 -= l3 & -this.toUnsignedLong(n29);
                l4 -= l6 & -((long)n29);
                l5 -= l7 & -((long)n29);
                n27 -= n26 & -n30;
                l3 -= l2 & -this.toUnsignedLong(n30);
                l6 -= l4 & -((long)n30);
                l7 -= l5 & -((long)n30);
                n26 += n26 & n31 - 1;
                l4 += l4 & (long)n31 - 1L;
                l5 += l5 & (long)n31 - 1L;
                l2 ^= (l2 ^ l2 >> 1) & -this.toUnsignedLong(n31);
                n27 += n27 & -n31;
                l6 += l6 & -((long)n31);
                l7 += l7 & -((long)n31);
                l3 ^= (l3 ^ l3 >> 1) & this.toUnsignedLong(n31) - 1L;
            }
            int n32 = this.zint_co_reduce(nArray5, n14, nArray5, n15, n6, l4, l5, l6, l7);
            l4 -= l4 + l4 & -((long)(n32 & 1));
            l5 -= l5 + l5 & -((long)(n32 & 1));
            l6 -= l6 + l6 & -((long)(n32 >>> 1));
            l7 -= l7 + l7 & -((long)(n32 >>> 1));
            this.zint_co_reduce_mod(nArray, n10, nArray5, n12, nArray4, n5, n6, n17, l4, l5, l6, l7);
            this.zint_co_reduce_mod(nArray2, n11, nArray5, n13, nArray3, n4, n6, n16, l4, l5, l6, l7);
        }
        int n33 = nArray5[n14 + 0] ^ 1;
        for (n8 = 1; n8 < n6; ++n8) {
            n33 |= nArray5[n14 + n8];
        }
        return 1 - ((n33 | -n33) >>> 31) & nArray3[n4 + 0] & nArray4[n5 + 0];
    }

    void zint_add_scaled_mul_small(int[] nArray, int n2, int n3, int[] nArray2, int n4, int n5, int n6, int n7, int n8) {
        if (n5 == 0) {
            return;
        }
        int n9 = -(nArray2[n4 + n5 - 1] >>> 30) >>> 1;
        int n10 = 0;
        int n11 = 0;
        for (int i2 = n7; i2 < n3; ++i2) {
            int n12;
            int n13 = i2 - n7;
            int n14 = n13 < n5 ? nArray2[n4 + n13] : n9;
            int n15 = n14 << n8 & Integer.MAX_VALUE | n10;
            n10 = n14 >>> 31 - n8;
            long l2 = this.toUnsignedLong(n15) * (long)n6 + this.toUnsignedLong(nArray[n2 + i2]) + (long)n11;
            nArray[n2 + i2] = (int)l2 & Integer.MAX_VALUE;
            n11 = n12 = (int)(l2 >>> 31);
        }
    }

    void zint_sub_scaled(int[] nArray, int n2, int n3, int[] nArray2, int n4, int n5, int n6, int n7) {
        if (n5 == 0) {
            return;
        }
        int n8 = -(nArray2[n4 + n5 - 1] >>> 30) >>> 1;
        int n9 = 0;
        int n10 = 0;
        for (int i2 = n6; i2 < n3; ++i2) {
            int n11 = i2 - n6;
            int n12 = n11 < n5 ? nArray2[n4 + n11] : n8;
            int n13 = n12 << n7 & Integer.MAX_VALUE | n9;
            n9 = n12 >>> 31 - n7;
            int n14 = nArray[n2 + i2] - n13 - n10;
            nArray[n2 + i2] = n14 & Integer.MAX_VALUE;
            n10 = n14 >>> 31;
        }
    }

    int zint_one_to_plain(int[] nArray, int n2) {
        int n3 = nArray[n2 + 0];
        n3 |= (n3 & 0x40000000) << 1;
        return n3;
    }

    void poly_big_to_fp(FalconFPR[] falconFPRArray, int n2, int[] nArray, int n3, int n4, int n5, int n6) {
        int n7 = FalconKeyGen.mkn(n6);
        if (n4 == 0) {
            for (int i2 = 0; i2 < n7; ++i2) {
                falconFPRArray[n2 + i2] = this.fpr.fpr_zero;
            }
            return;
        }
        int n8 = 0;
        while (n8 < n7) {
            int n9 = -(nArray[n3 + n4 - 1] >>> 30);
            int n10 = n9 >>> 1;
            int n11 = n9 & 1;
            FalconFPR falconFPR = this.fpr.fpr_zero;
            FalconFPR falconFPR2 = this.fpr.fpr_one;
            for (int i3 = 0; i3 < n4; ++i3) {
                int n12 = (nArray[n3 + i3] ^ n10) + n11;
                n11 = n12 >>> 31;
                n12 &= Integer.MAX_VALUE;
                n12 -= n12 << 1 & n9;
                falconFPR = this.fpr.fpr_add(falconFPR, this.fpr.fpr_mul(this.fpr.fpr_of(n12), falconFPR2));
                falconFPR2 = this.fpr.fpr_mul(falconFPR2, this.fpr.fpr_ptwo31);
            }
            falconFPRArray[n2 + n8] = falconFPR;
            ++n8;
            n3 += n5;
        }
    }

    int poly_big_to_small(byte[] byArray, int n2, int[] nArray, int n3, int n4, int n5) {
        int n6 = FalconKeyGen.mkn(n5);
        for (int i2 = 0; i2 < n6; ++i2) {
            int n7 = this.zint_one_to_plain(nArray, n3 + i2);
            if (n7 < -n4 || n7 > n4) {
                return 0;
            }
            byArray[n2 + i2] = (byte)n7;
        }
        return 1;
    }

    void poly_sub_scaled(int[] nArray, int n2, int n3, int n4, int[] nArray2, int n5, int n6, int n7, int[] nArray3, int n8, int n9, int n10, int n11) {
        int n12 = FalconKeyGen.mkn(n11);
        for (int i2 = 0; i2 < n12; ++i2) {
            int n13 = -nArray3[n8 + i2];
            int n14 = n2 + i2 * n4;
            int n15 = n5;
            for (int i3 = 0; i3 < n12; ++i3) {
                this.zint_add_scaled_mul_small(nArray, n14, n3, nArray2, n15, n6, n13, n9, n10);
                if (i2 + i3 == n12 - 1) {
                    n14 = n2;
                    n13 = -n13;
                } else {
                    n14 += n4;
                }
                n15 += n7;
            }
        }
    }

    void poly_sub_scaled_ntt(int[] nArray, int n2, int n3, int n4, int[] nArray2, int n5, int n6, int n7, int[] nArray3, int n8, int n9, int n10, int n11, int[] nArray4, int n12) {
        int n13;
        int n14;
        int n15;
        int n16 = FalconKeyGen.mkn(n11);
        int n17 = n6 + 1;
        int n18 = n12;
        int n19 = n18 + FalconKeyGen.mkn(n11);
        int n20 = n19 + FalconKeyGen.mkn(n11);
        int n21 = n20 + n16 * n17;
        FalconSmallPrime[] falconSmallPrimeArray = FalconSmallPrimeList.PRIMES;
        for (n15 = 0; n15 < n17; ++n15) {
            int n22;
            int n23 = falconSmallPrimeArray[n15].p;
            int n24 = this.modp_ninv31(n23);
            int n25 = this.modp_R2(n23, n24);
            int n26 = this.modp_Rx(n6, n23, n24, n25);
            this.modp_mkgm2(nArray4, n18, nArray4, n19, n11, falconSmallPrimeArray[n15].g, n23, n24);
            for (n22 = 0; n22 < n16; ++n22) {
                nArray4[n21 + n22] = this.modp_set(nArray3[n8 + n22], n23);
            }
            this.modp_NTT2(nArray4, n21, nArray4, n18, n11, n23, n24);
            n22 = 0;
            n14 = n5;
            n13 = n20 + n15;
            while (n22 < n16) {
                nArray4[n13] = this.zint_mod_small_signed(nArray2, n14, n6, n23, n24, n25, n26);
                ++n22;
                n14 += n7;
                n13 += n17;
            }
            this.modp_NTT2_ext(nArray4, n20 + n15, n17, nArray4, n18, n11, n23, n24);
            n22 = 0;
            n13 = n20 + n15;
            while (n22 < n16) {
                nArray4[n13] = this.modp_montymul(this.modp_montymul(nArray4[n21 + n22], nArray4[n13], n23, n24), n25, n23, n24);
                ++n22;
                n13 += n17;
            }
            this.modp_iNTT2_ext(nArray4, n20 + n15, n17, nArray4, n19, n11, n23, n24);
        }
        this.zint_rebuild_CRT(nArray4, n20, n17, n17, n16, falconSmallPrimeArray, 1, nArray4, n21);
        n15 = 0;
        n13 = n2;
        n14 = n20;
        while (n15 < n16) {
            this.zint_sub_scaled(nArray, n13, n3, nArray4, n14, n17, n9, n10);
            ++n15;
            n13 += n4;
            n14 += n17;
        }
    }

    long get_rng_u64(SHAKE256 sHAKE256) {
        byte[] byArray = new byte[8];
        sHAKE256.inner_shake256_extract(byArray, 0, byArray.length);
        return (long)byArray[0] & 0xFFL | ((long)byArray[1] & 0xFFL) << 8 | ((long)byArray[2] & 0xFFL) << 16 | ((long)byArray[3] & 0xFFL) << 24 | ((long)byArray[4] & 0xFFL) << 32 | ((long)byArray[5] & 0xFFL) << 40 | ((long)byArray[6] & 0xFFL) << 48 | ((long)byArray[7] & 0xFFL) << 56;
    }

    int mkgauss(SHAKE256 sHAKE256, int n2) {
        int n3 = 1 << 10 - n2;
        int n4 = 0;
        for (int i2 = 0; i2 < n3; ++i2) {
            long l2 = this.get_rng_u64(sHAKE256);
            int n5 = (int)(l2 >>> 63);
            int n6 = (int)((l2 &= Long.MAX_VALUE) - this.gauss_1024_12289[0] >>> 63);
            int n7 = 0;
            l2 = this.get_rng_u64(sHAKE256);
            l2 &= Long.MAX_VALUE;
            for (int i3 = 1; i3 < this.gauss_1024_12289.length; ++i3) {
                int n8 = (int)(l2 - this.gauss_1024_12289[i3] >>> 63) ^ 1;
                n7 |= i3 & -(n8 & (n6 ^ 1));
                n6 |= n8;
            }
            n7 = (n7 ^ -n5) + n5;
            n4 += n7;
        }
        return n4;
    }

    int poly_small_sqnorm(byte[] byArray, int n2, int n3) {
        int n4 = FalconKeyGen.mkn(n3);
        int n5 = 0;
        int n6 = 0;
        for (int i2 = 0; i2 < n4; ++i2) {
            byte by = byArray[n2 + i2];
            n6 |= (n5 += by * by);
        }
        return n5 | -(n6 >>> 31);
    }

    void poly_small_to_fp(FalconFPR[] falconFPRArray, int n2, byte[] byArray, int n3, int n4) {
        int n5 = FalconKeyGen.mkn(n4);
        for (int i2 = 0; i2 < n5; ++i2) {
            falconFPRArray[n2 + i2] = this.fpr.fpr_of(byArray[n3 + i2]);
        }
    }

    void make_fg_step(int[] nArray, int n2, int n3, int n4, int n5, int n6) {
        int n7;
        int n8;
        int n9;
        int n10;
        int n11;
        int n12;
        int n13;
        int n14;
        int n15 = 1 << n3;
        int n16 = n15 >> 1;
        int n17 = this.MAX_BL_SMALL[n4];
        int n18 = this.MAX_BL_SMALL[n4 + 1];
        FalconSmallPrime[] falconSmallPrimeArray = FalconSmallPrimeList.PRIMES;
        int n19 = n2;
        int n20 = n19 + n16 * n18;
        int n21 = n20 + n16 * n18;
        int n22 = n21 + n15 * n17;
        int n23 = n22 + n15 * n17;
        int n24 = n23 + n15;
        int n25 = n24 + n15;
        System.arraycopy(nArray, n2, nArray, n21, 2 * n15 * n17);
        for (n14 = 0; n14 < n17; ++n14) {
            n13 = falconSmallPrimeArray[n14].p;
            n12 = this.modp_ninv31(n13);
            n11 = this.modp_R2(n13, n12);
            this.modp_mkgm2(nArray, n23, nArray, n24, n3, falconSmallPrimeArray[n14].g, n13, n12);
            n10 = 0;
            n9 = n21 + n14;
            while (n10 < n15) {
                nArray[n25 + n10] = nArray[n9];
                ++n10;
                n9 += n17;
            }
            if (n5 == 0) {
                this.modp_NTT2(nArray, n25, nArray, n23, n3, n13, n12);
            }
            n10 = 0;
            n9 = n19 + n14;
            while (n10 < n16) {
                n8 = nArray[n25 + (n10 << 1) + 0];
                n7 = nArray[n25 + (n10 << 1) + 1];
                nArray[n9] = this.modp_montymul(this.modp_montymul(n8, n7, n13, n12), n11, n13, n12);
                ++n10;
                n9 += n18;
            }
            if (n5 != 0) {
                this.modp_iNTT2_ext(nArray, n21 + n14, n17, nArray, n24, n3, n13, n12);
            }
            n10 = 0;
            n9 = n22 + n14;
            while (n10 < n15) {
                nArray[n25 + n10] = nArray[n9];
                ++n10;
                n9 += n17;
            }
            if (n5 == 0) {
                this.modp_NTT2(nArray, n25, nArray, n23, n3, n13, n12);
            }
            n10 = 0;
            n9 = n20 + n14;
            while (n10 < n16) {
                n8 = nArray[n25 + (n10 << 1) + 0];
                n7 = nArray[n25 + (n10 << 1) + 1];
                nArray[n9] = this.modp_montymul(this.modp_montymul(n8, n7, n13, n12), n11, n13, n12);
                ++n10;
                n9 += n18;
            }
            if (n5 != 0) {
                this.modp_iNTT2_ext(nArray, n22 + n14, n17, nArray, n24, n3, n13, n12);
            }
            if (n6 != 0) continue;
            this.modp_iNTT2_ext(nArray, n19 + n14, n18, nArray, n24, n3 - 1, n13, n12);
            this.modp_iNTT2_ext(nArray, n20 + n14, n18, nArray, n24, n3 - 1, n13, n12);
        }
        this.zint_rebuild_CRT(nArray, n21, n17, n17, n15, falconSmallPrimeArray, 1, nArray, n23);
        this.zint_rebuild_CRT(nArray, n22, n17, n17, n15, falconSmallPrimeArray, 1, nArray, n23);
        for (n14 = n17; n14 < n18; ++n14) {
            int n26;
            n13 = falconSmallPrimeArray[n14].p;
            n12 = this.modp_ninv31(n13);
            n11 = this.modp_R2(n13, n12);
            n10 = this.modp_Rx(n17, n13, n12, n11);
            this.modp_mkgm2(nArray, n23, nArray, n24, n3, falconSmallPrimeArray[n14].g, n13, n12);
            n9 = 0;
            n8 = n21;
            while (n9 < n15) {
                nArray[n25 + n9] = this.zint_mod_small_signed(nArray, n8, n17, n13, n12, n11, n10);
                ++n9;
                n8 += n17;
            }
            this.modp_NTT2(nArray, n25, nArray, n23, n3, n13, n12);
            n9 = 0;
            n8 = n19 + n14;
            while (n9 < n16) {
                n7 = nArray[n25 + (n9 << 1) + 0];
                n26 = nArray[n25 + (n9 << 1) + 1];
                nArray[n8] = this.modp_montymul(this.modp_montymul(n7, n26, n13, n12), n11, n13, n12);
                ++n9;
                n8 += n18;
            }
            n9 = 0;
            n8 = n22;
            while (n9 < n15) {
                nArray[n25 + n9] = this.zint_mod_small_signed(nArray, n8, n17, n13, n12, n11, n10);
                ++n9;
                n8 += n17;
            }
            this.modp_NTT2(nArray, n25, nArray, n23, n3, n13, n12);
            n9 = 0;
            n8 = n20 + n14;
            while (n9 < n16) {
                n7 = nArray[n25 + (n9 << 1) + 0];
                n26 = nArray[n25 + (n9 << 1) + 1];
                nArray[n8] = this.modp_montymul(this.modp_montymul(n7, n26, n13, n12), n11, n13, n12);
                ++n9;
                n8 += n18;
            }
            if (n6 != 0) continue;
            this.modp_iNTT2_ext(nArray, n19 + n14, n18, nArray, n24, n3 - 1, n13, n12);
            this.modp_iNTT2_ext(nArray, n20 + n14, n18, nArray, n24, n3 - 1, n13, n12);
        }
    }

    void make_fg(int[] nArray, int n2, byte[] byArray, int n3, byte[] byArray2, int n4, int n5, int n6, int n7) {
        int n8 = FalconKeyGen.mkn(n5);
        int n9 = n2;
        int n10 = n9 + n8;
        FalconSmallPrime[] falconSmallPrimeArray = FalconSmallPrimeList.PRIMES;
        int n11 = falconSmallPrimeArray[0].p;
        for (int i2 = 0; i2 < n8; ++i2) {
            nArray[n9 + i2] = this.modp_set(byArray[n3 + i2], n11);
            nArray[n10 + i2] = this.modp_set(byArray2[n4 + i2], n11);
        }
        if (n6 == 0 && n7 != 0) {
            int n12 = falconSmallPrimeArray[0].p;
            int n13 = this.modp_ninv31(n12);
            int n14 = n10 + n8;
            int n15 = n14 + n8;
            this.modp_mkgm2(nArray, n14, nArray, n15, n5, falconSmallPrimeArray[0].g, n12, n13);
            this.modp_NTT2(nArray, n9, nArray, n14, n5, n12, n13);
            this.modp_NTT2(nArray, n10, nArray, n14, n5, n12, n13);
            return;
        }
        for (int i3 = 0; i3 < n6; ++i3) {
            this.make_fg_step(nArray, n2, n5 - i3, i3, i3 != 0 ? 1 : 0, i3 + 1 < n6 || n7 != 0 ? 1 : 0);
        }
    }

    int solve_NTRU_deepest(int n2, byte[] byArray, int n3, byte[] byArray2, int n4, int[] nArray, int n5) {
        int n6 = this.MAX_BL_SMALL[n2];
        FalconSmallPrime[] falconSmallPrimeArray = FalconSmallPrimeList.PRIMES;
        int n7 = n5;
        int n8 = n7 + n6;
        int n9 = n8 + n6;
        int n10 = n9 + n6;
        int n11 = n10 + n6;
        this.make_fg(nArray, n9, byArray, n3, byArray2, n4, n2, n2, 0);
        this.zint_rebuild_CRT(nArray, n9, n6, n6, 2, falconSmallPrimeArray, 0, nArray, n11);
        if (this.zint_bezout(nArray, n8, nArray, n7, nArray, n9, nArray, n10, n6, nArray, n11) == 0) {
            return 0;
        }
        int n12 = 12289;
        if (this.zint_mul_small(nArray, n7, n6, n12) != 0 || this.zint_mul_small(nArray, n8, n6, n12) != 0) {
            return 0;
        }
        return 1;
    }

    int solve_NTRU_intermediate(int n2, byte[] byArray, int n3, byte[] byArray2, int n4, int n5, int[] nArray, int n6) {
        int n7;
        int n8;
        int n9;
        int n10;
        int n11;
        int n12;
        int n13;
        int n14;
        int n15;
        int n16;
        int n17;
        int n18;
        int n19 = n2 - n5;
        int n20 = 1 << n19;
        int n21 = n20 >> 1;
        int n22 = this.MAX_BL_SMALL[n5];
        int n23 = this.MAX_BL_SMALL[n5 + 1];
        int n24 = this.MAX_BL_LARGE[n5];
        FalconSmallPrime[] falconSmallPrimeArray = FalconSmallPrimeList.PRIMES;
        int n25 = n6;
        int n26 = n25 + n23 * n21;
        int n27 = n26 + n23 * n21;
        this.make_fg(nArray, n27, byArray, n3, byArray2, n4, n2, n5, 1);
        int n28 = n6;
        int n29 = n28 + n20 * n24;
        int n30 = n29 + n20 * n24;
        System.arraycopy(nArray, n27, nArray, n30, 2 * n20 * n22);
        n27 = n30;
        int n31 = n27 + n22 * n20;
        n30 = n31 + n22 * n20;
        System.arraycopy(nArray, n25, nArray, n30, 2 * n21 * n23);
        n25 = n30;
        n26 = n25 + n21 * n23;
        for (n18 = 0; n18 < n24; ++n18) {
            n17 = falconSmallPrimeArray[n18].p;
            n16 = this.modp_ninv31(n17);
            n15 = this.modp_R2(n17, n16);
            n14 = this.modp_Rx(n23, n17, n16, n15);
            n13 = 0;
            n12 = n25;
            n11 = n26;
            n10 = n28 + n18;
            n9 = n29 + n18;
            while (n13 < n21) {
                nArray[n10] = this.zint_mod_small_signed(nArray, n12, n23, n17, n16, n15, n14);
                nArray[n9] = this.zint_mod_small_signed(nArray, n11, n23, n17, n16, n15, n14);
                ++n13;
                n12 += n23;
                n11 += n23;
                n10 += n24;
                n9 += n24;
            }
        }
        for (n18 = 0; n18 < n24; ++n18) {
            int n32;
            int n33;
            n17 = falconSmallPrimeArray[n18].p;
            n16 = this.modp_ninv31(n17);
            n15 = this.modp_R2(n17, n16);
            if (n18 == n22) {
                this.zint_rebuild_CRT(nArray, n27, n22, n22, n20, falconSmallPrimeArray, 1, nArray, n30);
                this.zint_rebuild_CRT(nArray, n31, n22, n22, n20, falconSmallPrimeArray, 1, nArray, n30);
            }
            n14 = n30;
            n13 = n14 + n20;
            n12 = n13 + n20;
            n11 = n12 + n20;
            this.modp_mkgm2(nArray, n14, nArray, n13, n19, falconSmallPrimeArray[n18].g, n17, n16);
            if (n18 < n22) {
                n33 = 0;
                n8 = n27 + n18;
                n7 = n31 + n18;
                while (n33 < n20) {
                    nArray[n12 + n33] = nArray[n8];
                    nArray[n11 + n33] = nArray[n7];
                    ++n33;
                    n8 += n22;
                    n7 += n22;
                }
                this.modp_iNTT2_ext(nArray, n27 + n18, n22, nArray, n13, n19, n17, n16);
                this.modp_iNTT2_ext(nArray, n31 + n18, n22, nArray, n13, n19, n17, n16);
            } else {
                n32 = this.modp_Rx(n22, n17, n16, n15);
                n33 = 0;
                n8 = n27;
                n7 = n31;
                while (n33 < n20) {
                    nArray[n12 + n33] = this.zint_mod_small_signed(nArray, n8, n22, n17, n16, n15, n32);
                    nArray[n11 + n33] = this.zint_mod_small_signed(nArray, n7, n22, n17, n16, n15, n32);
                    ++n33;
                    n8 += n22;
                    n7 += n22;
                }
                this.modp_NTT2(nArray, n12, nArray, n14, n19, n17, n16);
                this.modp_NTT2(nArray, n11, nArray, n14, n19, n17, n16);
            }
            n10 = n11 + n20;
            n9 = n10 + n21;
            n33 = 0;
            n8 = n28 + n18;
            n7 = n29 + n18;
            while (n33 < n21) {
                nArray[n10 + n33] = nArray[n8];
                nArray[n9 + n33] = nArray[n7];
                ++n33;
                n8 += n24;
                n7 += n24;
            }
            this.modp_NTT2(nArray, n10, nArray, n14, n19 - 1, n17, n16);
            this.modp_NTT2(nArray, n9, nArray, n14, n19 - 1, n17, n16);
            n33 = 0;
            n8 = n28 + n18;
            n7 = n29 + n18;
            while (n33 < n21) {
                n32 = nArray[n12 + (n33 << 1) + 0];
                int n34 = nArray[n12 + (n33 << 1) + 1];
                int n35 = nArray[n11 + (n33 << 1) + 0];
                int n36 = nArray[n11 + (n33 << 1) + 1];
                int n37 = this.modp_montymul(nArray[n10 + n33], n15, n17, n16);
                int n38 = this.modp_montymul(nArray[n9 + n33], n15, n17, n16);
                nArray[n8 + 0] = this.modp_montymul(n36, n37, n17, n16);
                nArray[n8 + n24] = this.modp_montymul(n35, n37, n17, n16);
                nArray[n7 + 0] = this.modp_montymul(n34, n38, n17, n16);
                nArray[n7 + n24] = this.modp_montymul(n32, n38, n17, n16);
                ++n33;
                n8 += n24 << 1;
                n7 += n24 << 1;
            }
            this.modp_iNTT2_ext(nArray, n28 + n18, n24, nArray, n13, n19, n17, n16);
            this.modp_iNTT2_ext(nArray, n29 + n18, n24, nArray, n13, n19, n17, n16);
        }
        this.zint_rebuild_CRT(nArray, n28, n24, n24, n20, falconSmallPrimeArray, 1, nArray, n30);
        this.zint_rebuild_CRT(nArray, n29, n24, n24, n20, falconSmallPrimeArray, 1, nArray, n30);
        FalconFPR[] falconFPRArray = new FalconFPR[n20];
        FalconFPR[] falconFPRArray2 = new FalconFPR[n20];
        FalconFPR[] falconFPRArray3 = new FalconFPR[n20];
        FalconFPR[] falconFPRArray4 = new FalconFPR[n20];
        FalconFPR[] falconFPRArray5 = new FalconFPR[n20 >> 1];
        int[] nArray2 = new int[n20];
        int n39 = n22 > 10 ? 10 : n22;
        this.poly_big_to_fp(falconFPRArray3, 0, nArray, n27 + n22 - n39, n39, n22, n19);
        this.poly_big_to_fp(falconFPRArray4, 0, nArray, n31 + n22 - n39, n39, n22, n19);
        int n40 = 31 * (n22 - n39);
        int n41 = this.bitlength_avg[n5] - 6 * this.bitlength_std[n5];
        int n42 = this.bitlength_avg[n5] + 6 * this.bitlength_std[n5];
        this.fft.FFT(falconFPRArray3, 0, n19);
        this.fft.FFT(falconFPRArray4, 0, n19);
        this.fft.poly_invnorm2_fft(falconFPRArray5, 0, falconFPRArray3, 0, falconFPRArray4, 0, n19);
        this.fft.poly_adj_fft(falconFPRArray3, 0, n19);
        this.fft.poly_adj_fft(falconFPRArray4, 0, n19);
        int n43 = n24;
        int n44 = 31 * n24;
        int n45 = n44 - n41;
        while (true) {
            FalconFPR falconFPR;
            n39 = n43 > 10 ? 10 : n43;
            n17 = 31 * (n43 - n39);
            this.poly_big_to_fp(falconFPRArray, 0, nArray, n28 + n43 - n39, n39, n24, n19);
            this.poly_big_to_fp(falconFPRArray2, 0, nArray, n29 + n43 - n39, n39, n24, n19);
            this.fft.FFT(falconFPRArray, 0, n19);
            this.fft.FFT(falconFPRArray2, 0, n19);
            this.fft.poly_mul_fft(falconFPRArray, 0, falconFPRArray3, 0, n19);
            this.fft.poly_mul_fft(falconFPRArray2, 0, falconFPRArray4, 0, n19);
            this.fft.poly_add(falconFPRArray2, 0, falconFPRArray, 0, n19);
            this.fft.poly_mul_autoadj_fft(falconFPRArray2, 0, falconFPRArray5, 0, n19);
            this.fft.iFFT(falconFPRArray2, 0, n19);
            n16 = n45 - n17 + n40;
            if (n16 < 0) {
                n16 = -n16;
                falconFPR = this.fpr.fpr_two;
            } else {
                falconFPR = this.fpr.fpr_onehalf;
            }
            FalconFPR falconFPR2 = this.fpr.fpr_one;
            while (n16 != 0) {
                if ((n16 & 1) != 0) {
                    falconFPR2 = this.fpr.fpr_mul(falconFPR2, falconFPR);
                }
                n16 >>= 1;
                falconFPR = this.fpr.fpr_sqr(falconFPR);
            }
            for (n18 = 0; n18 < n20; ++n18) {
                FalconFPR falconFPR3 = this.fpr.fpr_mul(falconFPRArray2[n18], falconFPR2);
                if (!this.fpr.fpr_lt(this.fpr.fpr_mtwo31m1, falconFPR3) || !this.fpr.fpr_lt(falconFPR3, this.fpr.fpr_ptwo31m1)) {
                    return 0;
                }
                nArray2[n18] = (int)this.fpr.fpr_rint(falconFPR3);
            }
            n13 = n45 / 31;
            n14 = n45 % 31;
            if (n5 <= 4) {
                this.poly_sub_scaled_ntt(nArray, n28, n43, n24, nArray, n27, n22, n22, nArray2, 0, n13, n14, n19, nArray, n30);
                this.poly_sub_scaled_ntt(nArray, n29, n43, n24, nArray, n31, n22, n22, nArray2, 0, n13, n14, n19, nArray, n30);
            } else {
                this.poly_sub_scaled(nArray, n28, n43, n24, nArray, n27, n22, n22, nArray2, 0, n13, n14, n19);
                this.poly_sub_scaled(nArray, n29, n43, n24, nArray, n31, n22, n22, nArray2, 0, n13, n14, n19);
            }
            n15 = n45 + n42 + 10;
            if (n15 < n44 && n43 * 31 >= (n44 = n15) + 31) {
                --n43;
            }
            if (n45 <= 0) break;
            if ((n45 -= 25) >= 0) continue;
            n45 = 0;
        }
        if (n43 < n22) {
            n18 = 0;
            while (n18 < n20) {
                n16 = -(nArray[n28 + n43 - 1] >>> 30) >>> 1;
                for (n17 = n43; n17 < n22; ++n17) {
                    nArray[n28 + n17] = n16;
                }
                n16 = -(nArray[n29 + n43 - 1] >>> 30) >>> 1;
                for (n17 = n43; n17 < n22; ++n17) {
                    nArray[n29 + n17] = n16;
                }
                ++n18;
                n28 += n24;
                n29 += n24;
            }
        }
        n18 = 0;
        n8 = n6;
        n7 = n6;
        while (n18 < n20 << 1) {
            System.arraycopy(nArray, n7, nArray, n8, n22);
            ++n18;
            n8 += n22;
            n7 += n24;
        }
        return 1;
    }

    int solve_NTRU_binary_depth1(int n2, byte[] byArray, int n3, byte[] byArray2, int n4, int[] nArray, int n5) {
        int n6;
        int n7;
        int n8;
        int n9;
        int n10;
        int n11;
        int n12;
        int n13;
        int n14;
        int n15;
        int n16 = 1;
        int n17 = 1 << n2;
        int n18 = n2 - n16;
        int n19 = 1 << n18;
        int n20 = n19 >> 1;
        int n21 = this.MAX_BL_SMALL[n16];
        int n22 = this.MAX_BL_SMALL[n16 + 1];
        int n23 = this.MAX_BL_LARGE[n16];
        int n24 = n5;
        int n25 = n24 + n22 * n20;
        int n26 = n25 + n22 * n20;
        int n27 = n26 + n23 * n19;
        for (n15 = 0; n15 < n23; ++n15) {
            n14 = FalconSmallPrimeList.PRIMES[n15].p;
            n13 = this.modp_ninv31(n14);
            n12 = this.modp_R2(n14, n13);
            n11 = this.modp_Rx(n22, n14, n13, n12);
            n10 = 0;
            n9 = n24;
            n8 = n25;
            n7 = n26 + n15;
            n6 = n27 + n15;
            while (n10 < n20) {
                nArray[n7] = this.zint_mod_small_signed(nArray, n9, n22, n14, n13, n12, n11);
                nArray[n6] = this.zint_mod_small_signed(nArray, n8, n22, n14, n13, n12, n11);
                ++n10;
                n9 += n22;
                n8 += n22;
                n7 += n23;
                n6 += n23;
            }
        }
        System.arraycopy(nArray, n26, nArray, n5, n23 * n19);
        n26 = n5;
        System.arraycopy(nArray, n27, nArray, n26 + n23 * n19, n23 * n19);
        n27 = n26 + n23 * n19;
        int n28 = n27 + n23 * n19;
        int n29 = n28 + n21 * n19;
        int n30 = n29 + n21 * n19;
        for (n15 = 0; n15 < n23; ++n15) {
            int n31;
            n14 = FalconSmallPrimeList.PRIMES[n15].p;
            n13 = this.modp_ninv31(n14);
            n12 = this.modp_R2(n14, n13);
            n11 = n30;
            n10 = n11 + n17;
            n9 = n10 + n19;
            n8 = n9 + n17;
            this.modp_mkgm2(nArray, n11, nArray, n10, n2, FalconSmallPrimeList.PRIMES[n15].g, n14, n13);
            for (n31 = 0; n31 < n17; ++n31) {
                nArray[n9 + n31] = this.modp_set(byArray[n3 + n31], n14);
                nArray[n8 + n31] = this.modp_set(byArray2[n4 + n31], n14);
            }
            this.modp_NTT2(nArray, n9, nArray, n11, n2, n14, n13);
            this.modp_NTT2(nArray, n8, nArray, n11, n2, n14, n13);
            for (int i2 = n2; i2 > n18; --i2) {
                this.modp_poly_rec_res(nArray, n9, i2, n14, n13, n12);
                this.modp_poly_rec_res(nArray, n8, i2, n14, n13, n12);
            }
            if (n16 > 0) {
                System.arraycopy(nArray, n10, nArray, n11 + n19, n19);
                n10 = n11 + n19;
                System.arraycopy(nArray, n9, nArray, n10 + n19, n19);
                n9 = n10 + n19;
                System.arraycopy(nArray, n8, nArray, n9 + n19, n19);
                n8 = n9 + n19;
            }
            n7 = n8 + n19;
            n6 = n7 + n20;
            n31 = 0;
            int n32 = n26 + n15;
            int n33 = n27 + n15;
            while (n31 < n20) {
                nArray[n7 + n31] = nArray[n32];
                nArray[n6 + n31] = nArray[n33];
                ++n31;
                n32 += n23;
                n33 += n23;
            }
            this.modp_NTT2(nArray, n7, nArray, n11, n18 - 1, n14, n13);
            this.modp_NTT2(nArray, n6, nArray, n11, n18 - 1, n14, n13);
            n31 = 0;
            n32 = n26 + n15;
            n33 = n27 + n15;
            while (n31 < n20) {
                int n34 = nArray[n9 + (n31 << 1) + 0];
                int n35 = nArray[n9 + (n31 << 1) + 1];
                int n36 = nArray[n8 + (n31 << 1) + 0];
                int n37 = nArray[n8 + (n31 << 1) + 1];
                int n38 = this.modp_montymul(nArray[n7 + n31], n12, n14, n13);
                int n39 = this.modp_montymul(nArray[n6 + n31], n12, n14, n13);
                nArray[n32 + 0] = this.modp_montymul(n37, n38, n14, n13);
                nArray[n32 + n23] = this.modp_montymul(n36, n38, n14, n13);
                nArray[n33 + 0] = this.modp_montymul(n35, n39, n14, n13);
                nArray[n33 + n23] = this.modp_montymul(n34, n39, n14, n13);
                ++n31;
                n32 += n23 << 1;
                n33 += n23 << 1;
            }
            this.modp_iNTT2_ext(nArray, n26 + n15, n23, nArray, n10, n18, n14, n13);
            this.modp_iNTT2_ext(nArray, n27 + n15, n23, nArray, n10, n18, n14, n13);
            if (n15 >= n21) continue;
            this.modp_iNTT2(nArray, n9, nArray, n10, n18, n14, n13);
            this.modp_iNTT2(nArray, n8, nArray, n10, n18, n14, n13);
            n31 = 0;
            n32 = n28 + n15;
            n33 = n29 + n15;
            while (n31 < n19) {
                nArray[n32] = nArray[n9 + n31];
                nArray[n33] = nArray[n8 + n31];
                ++n31;
                n32 += n21;
                n33 += n21;
            }
        }
        this.zint_rebuild_CRT(nArray, n26, n23, n23, n19 << 1, FalconSmallPrimeList.PRIMES, 1, nArray, n30);
        this.zint_rebuild_CRT(nArray, n28, n21, n21, n19 << 1, FalconSmallPrimeList.PRIMES, 1, nArray, n30);
        FalconFPR[] falconFPRArray = new FalconFPR[n19];
        FalconFPR[] falconFPRArray2 = new FalconFPR[n19];
        this.poly_big_to_fp(falconFPRArray, 0, nArray, n26, n23, n23, n18);
        this.poly_big_to_fp(falconFPRArray2, 0, nArray, n27, n23, n23, n18);
        System.arraycopy(nArray, n28, nArray, n5, 2 * n21 * n19);
        n28 = n5;
        n29 = n28 + n21 * n19;
        FalconFPR[] falconFPRArray3 = new FalconFPR[n19];
        FalconFPR[] falconFPRArray4 = new FalconFPR[n19];
        this.poly_big_to_fp(falconFPRArray3, 0, nArray, n28, n21, n21, n18);
        this.poly_big_to_fp(falconFPRArray4, 0, nArray, n29, n21, n21, n18);
        this.fft.FFT(falconFPRArray, 0, n18);
        this.fft.FFT(falconFPRArray2, 0, n18);
        this.fft.FFT(falconFPRArray3, 0, n18);
        this.fft.FFT(falconFPRArray4, 0, n18);
        FalconFPR[] falconFPRArray5 = new FalconFPR[n19];
        FalconFPR[] falconFPRArray6 = new FalconFPR[n19 >> 1];
        this.fft.poly_add_muladj_fft(falconFPRArray5, 0, falconFPRArray, 0, falconFPRArray2, 0, falconFPRArray3, 0, falconFPRArray4, 0, n18);
        this.fft.poly_invnorm2_fft(falconFPRArray6, 0, falconFPRArray3, 0, falconFPRArray4, 0, n18);
        this.fft.poly_mul_autoadj_fft(falconFPRArray5, 0, falconFPRArray6, 0, n18);
        this.fft.iFFT(falconFPRArray5, 0, n18);
        for (n15 = 0; n15 < n19; ++n15) {
            FalconFPR falconFPR = falconFPRArray5[n15];
            if (!this.fpr.fpr_lt(falconFPR, this.fpr.fpr_ptwo63m1) || !this.fpr.fpr_lt(this.fpr.fpr_mtwo63m1, falconFPR)) {
                return 0;
            }
            falconFPRArray5[n15] = this.fpr.fpr_of(this.fpr.fpr_rint(falconFPR));
        }
        this.fft.FFT(falconFPRArray5, 0, n18);
        this.fft.poly_mul_fft(falconFPRArray3, 0, falconFPRArray5, 0, n18);
        this.fft.poly_mul_fft(falconFPRArray4, 0, falconFPRArray5, 0, n18);
        this.fft.poly_sub(falconFPRArray, 0, falconFPRArray3, 0, n18);
        this.fft.poly_sub(falconFPRArray2, 0, falconFPRArray4, 0, n18);
        this.fft.iFFT(falconFPRArray, 0, n18);
        this.fft.iFFT(falconFPRArray2, 0, n18);
        n26 = n5;
        n27 = n26 + n19;
        for (n15 = 0; n15 < n19; ++n15) {
            nArray[n26 + n15] = (int)this.fpr.fpr_rint(falconFPRArray[n15]);
            nArray[n27 + n15] = (int)this.fpr.fpr_rint(falconFPRArray2[n15]);
        }
        return 1;
    }

    int solve_NTRU_binary_depth0(int n2, byte[] byArray, int n3, byte[] byArray2, int n4, int[] nArray, int n5) {
        int n6;
        int n7;
        int n8;
        int n9 = 1 << n2;
        int n10 = n9 >> 1;
        int n11 = FalconSmallPrimeList.PRIMES[0].p;
        int n12 = this.modp_ninv31(n11);
        int n13 = this.modp_R2(n11, n12);
        int n14 = n5;
        int n15 = n14 + n10;
        int n16 = n15 + n10;
        int n17 = n16 + n9;
        int n18 = n17 + n9;
        int n19 = n18 + n9;
        this.modp_mkgm2(nArray, n18, nArray, n19, n2, FalconSmallPrimeList.PRIMES[0].g, n11, n12);
        for (n8 = 0; n8 < n10; ++n8) {
            nArray[n14 + n8] = this.modp_set(this.zint_one_to_plain(nArray, n14 + n8), n11);
            nArray[n15 + n8] = this.modp_set(this.zint_one_to_plain(nArray, n15 + n8), n11);
        }
        this.modp_NTT2(nArray, n14, nArray, n18, n2 - 1, n11, n12);
        this.modp_NTT2(nArray, n15, nArray, n18, n2 - 1, n11, n12);
        for (n8 = 0; n8 < n9; ++n8) {
            nArray[n16 + n8] = this.modp_set(byArray[n3 + n8], n11);
            nArray[n17 + n8] = this.modp_set(byArray2[n4 + n8], n11);
        }
        this.modp_NTT2(nArray, n16, nArray, n18, n2, n11, n12);
        this.modp_NTT2(nArray, n17, nArray, n18, n2, n11, n12);
        for (n8 = 0; n8 < n9; n8 += 2) {
            n7 = nArray[n16 + n8 + 0];
            n6 = nArray[n16 + n8 + 1];
            int n20 = nArray[n17 + n8 + 0];
            int n21 = nArray[n17 + n8 + 1];
            int n22 = this.modp_montymul(nArray[n14 + (n8 >> 1)], n13, n11, n12);
            int n23 = this.modp_montymul(nArray[n15 + (n8 >> 1)], n13, n11, n12);
            nArray[n16 + n8 + 0] = this.modp_montymul(n21, n22, n11, n12);
            nArray[n16 + n8 + 1] = this.modp_montymul(n20, n22, n11, n12);
            nArray[n17 + n8 + 0] = this.modp_montymul(n6, n23, n11, n12);
            nArray[n17 + n8 + 1] = this.modp_montymul(n7, n23, n11, n12);
        }
        this.modp_iNTT2(nArray, n16, nArray, n19, n2, n11, n12);
        this.modp_iNTT2(nArray, n17, nArray, n19, n2, n11, n12);
        n15 = n14 + n9;
        int n24 = n15 + n9;
        System.arraycopy(nArray, n16, nArray, n14, 2 * n9);
        int n25 = n24 + n9;
        int n26 = n25 + n9;
        int n27 = n26 + n9;
        int n28 = n27 + n9;
        this.modp_mkgm2(nArray, n24, nArray, n25, n2, FalconSmallPrimeList.PRIMES[0].g, n11, n12);
        this.modp_NTT2(nArray, n14, nArray, n24, n2, n11, n12);
        this.modp_NTT2(nArray, n15, nArray, n24, n2, n11, n12);
        int n29 = this.modp_set(byArray[n3 + 0], n11);
        nArray[n28 + 0] = n29;
        nArray[n27 + 0] = n29;
        for (n8 = 1; n8 < n9; ++n8) {
            nArray[n27 + n8] = this.modp_set(byArray[n3 + n8], n11);
            nArray[n28 + n9 - n8] = this.modp_set(-byArray[n3 + n8], n11);
        }
        this.modp_NTT2(nArray, n27, nArray, n24, n2, n11, n12);
        this.modp_NTT2(nArray, n28, nArray, n24, n2, n11, n12);
        for (n8 = 0; n8 < n9; ++n8) {
            n7 = this.modp_montymul(nArray[n28 + n8], n13, n11, n12);
            nArray[n25 + n8] = this.modp_montymul(n7, nArray[n14 + n8], n11, n12);
            nArray[n26 + n8] = this.modp_montymul(n7, nArray[n27 + n8], n11, n12);
        }
        int n30 = this.modp_set(byArray2[n4 + 0], n11);
        nArray[n28 + 0] = n30;
        nArray[n27 + 0] = n30;
        for (n8 = 1; n8 < n9; ++n8) {
            nArray[n27 + n8] = this.modp_set(byArray2[n4 + n8], n11);
            nArray[n28 + n9 - n8] = this.modp_set(-byArray2[n4 + n8], n11);
        }
        this.modp_NTT2(nArray, n27, nArray, n24, n2, n11, n12);
        this.modp_NTT2(nArray, n28, nArray, n24, n2, n11, n12);
        for (n8 = 0; n8 < n9; ++n8) {
            n7 = this.modp_montymul(nArray[n28 + n8], n13, n11, n12);
            nArray[n25 + n8] = this.modp_add(nArray[n25 + n8], this.modp_montymul(n7, nArray[n15 + n8], n11, n12), n11);
            nArray[n26 + n8] = this.modp_add(nArray[n26 + n8], this.modp_montymul(n7, nArray[n27 + n8], n11, n12), n11);
        }
        this.modp_mkgm2(nArray, n24, nArray, n27, n2, FalconSmallPrimeList.PRIMES[0].g, n11, n12);
        this.modp_iNTT2(nArray, n25, nArray, n27, n2, n11, n12);
        this.modp_iNTT2(nArray, n26, nArray, n27, n2, n11, n12);
        for (n8 = 0; n8 < n9; ++n8) {
            nArray[n24 + n8] = this.modp_norm(nArray[n25 + n8], n11);
            nArray[n25 + n8] = this.modp_norm(nArray[n26 + n8], n11);
        }
        FalconFPR[] falconFPRArray = new FalconFPR[3 * n9];
        int n31 = 0;
        int n32 = n31 + n9;
        int n33 = n32 + n9;
        for (n8 = 0; n8 < n9; ++n8) {
            falconFPRArray[n33 + n8] = this.fpr.fpr_of(nArray[n25 + n8]);
        }
        this.fft.FFT(falconFPRArray, n33, n2);
        System.arraycopy(falconFPRArray, n33, falconFPRArray, n32, n10);
        n33 = n32 + n10;
        for (n8 = 0; n8 < n9; ++n8) {
            falconFPRArray[n33 + n8] = this.fpr.fpr_of(nArray[n24 + n8]);
        }
        this.fft.FFT(falconFPRArray, n33, n2);
        this.fft.poly_div_autoadj_fft(falconFPRArray, n33, falconFPRArray, n32, n2);
        this.fft.iFFT(falconFPRArray, n33, n2);
        for (n8 = 0; n8 < n9; ++n8) {
            nArray[n24 + n8] = this.modp_set((int)this.fpr.fpr_rint(falconFPRArray[n33 + n8]), n11);
        }
        n25 = n24 + n9;
        n26 = n25 + n9;
        n27 = n26 + n9;
        n28 = n27 + n9;
        this.modp_mkgm2(nArray, n25, nArray, n26, n2, FalconSmallPrimeList.PRIMES[0].g, n11, n12);
        for (n8 = 0; n8 < n9; ++n8) {
            nArray[n27 + n8] = this.modp_set(byArray[n3 + n8], n11);
            nArray[n28 + n8] = this.modp_set(byArray2[n4 + n8], n11);
        }
        this.modp_NTT2(nArray, n24, nArray, n25, n2, n11, n12);
        this.modp_NTT2(nArray, n27, nArray, n25, n2, n11, n12);
        this.modp_NTT2(nArray, n28, nArray, n25, n2, n11, n12);
        for (n8 = 0; n8 < n9; ++n8) {
            n6 = this.modp_montymul(nArray[n24 + n8], n13, n11, n12);
            nArray[n14 + n8] = this.modp_sub(nArray[n14 + n8], this.modp_montymul(n6, nArray[n27 + n8], n11, n12), n11);
            nArray[n15 + n8] = this.modp_sub(nArray[n15 + n8], this.modp_montymul(n6, nArray[n28 + n8], n11, n12), n11);
        }
        this.modp_iNTT2(nArray, n14, nArray, n26, n2, n11, n12);
        this.modp_iNTT2(nArray, n15, nArray, n26, n2, n11, n12);
        for (n8 = 0; n8 < n9; ++n8) {
            nArray[n14 + n8] = this.modp_norm(nArray[n14 + n8], n11);
            nArray[n15 + n8] = this.modp_norm(nArray[n15 + n8], n11);
        }
        return 1;
    }

    int solve_NTRU(int n2, byte[] byArray, int n3, byte[] byArray2, int n4, byte[] byArray3, int n5, byte[] byArray4, int n6, int n7, int[] nArray, int n8) {
        int n9;
        int n10;
        int n11 = FalconKeyGen.mkn(n2);
        if (this.solve_NTRU_deepest(n2, byArray3, n5, byArray4, n6, nArray, n8) == 0) {
            return 0;
        }
        if (n2 <= 2) {
            n10 = n2;
            while (n10-- > 0) {
                if (this.solve_NTRU_intermediate(n2, byArray3, n5, byArray4, n6, n10, nArray, n8) != 0) continue;
                return 0;
            }
        } else {
            n10 = n2;
            while (n10-- > 2) {
                if (this.solve_NTRU_intermediate(n2, byArray3, n5, byArray4, n6, n10, nArray, n8) != 0) continue;
                return 0;
            }
            if (this.solve_NTRU_binary_depth1(n2, byArray3, n5, byArray4, n6, nArray, n8) == 0) {
                return 0;
            }
            if (this.solve_NTRU_binary_depth0(n2, byArray3, n5, byArray4, n6, nArray, n8) == 0) {
                return 0;
            }
        }
        if (byArray2 == null) {
            n4 = 0;
            byArray2 = new byte[n11];
        }
        if (this.poly_big_to_small(byArray, n3, nArray, n8, n7, n2) == 0 || this.poly_big_to_small(byArray2, n4, nArray, n8 + n11, n7, n2) == 0) {
            return 0;
        }
        int n12 = n8;
        int n13 = n12 + n11;
        int n14 = n13 + n11;
        int n15 = n14 + n11;
        int n16 = n15 + n11;
        FalconSmallPrime[] falconSmallPrimeArray = FalconSmallPrimeList.PRIMES;
        int n17 = falconSmallPrimeArray[0].p;
        int n18 = this.modp_ninv31(n17);
        this.modp_mkgm2(nArray, n16, nArray, n8, n2, falconSmallPrimeArray[0].g, n17, n18);
        for (n9 = 0; n9 < n11; ++n9) {
            nArray[n12 + n9] = this.modp_set(byArray2[n4 + n9], n17);
        }
        for (n9 = 0; n9 < n11; ++n9) {
            nArray[n13 + n9] = this.modp_set(byArray3[n5 + n9], n17);
            nArray[n14 + n9] = this.modp_set(byArray4[n6 + n9], n17);
            nArray[n15 + n9] = this.modp_set(byArray[n3 + n9], n17);
        }
        this.modp_NTT2(nArray, n13, nArray, n16, n2, n17, n18);
        this.modp_NTT2(nArray, n14, nArray, n16, n2, n17, n18);
        this.modp_NTT2(nArray, n15, nArray, n16, n2, n17, n18);
        this.modp_NTT2(nArray, n12, nArray, n16, n2, n17, n18);
        int n19 = this.modp_montymul(12289, 1, n17, n18);
        for (n9 = 0; n9 < n11; ++n9) {
            n10 = this.modp_sub(this.modp_montymul(nArray[n13 + n9], nArray[n12 + n9], n17, n18), this.modp_montymul(nArray[n14 + n9], nArray[n15 + n9], n17, n18), n17);
            if (n10 == n19) continue;
            return 0;
        }
        return 1;
    }

    void poly_small_mkgauss(SHAKE256 sHAKE256, byte[] byArray, int n2, int n3) {
        int n4 = FalconKeyGen.mkn(n3);
        int n5 = 0;
        for (int i2 = 0; i2 < n4; ++i2) {
            int n6;
            block4: {
                while (true) {
                    if ((n6 = this.mkgauss(sHAKE256, n3)) < -127 || n6 > 127) {
                        continue;
                    }
                    if (i2 != n4 - 1) break;
                    if ((n5 ^ n6 & 1) == 0) {
                        continue;
                    }
                    break block4;
                    break;
                }
                n5 ^= n6 & 1;
            }
            byArray[n2 + i2] = (byte)n6;
        }
    }

    void keygen(SHAKE256 sHAKE256, byte[] byArray, int n2, byte[] byArray2, int n3, byte[] byArray3, int n4, byte[] byArray4, int n5, short[] sArray, int n6, int n7) {
        int n8 = FalconKeyGen.mkn(n7);
        SHAKE256 sHAKE2562 = sHAKE256;
        while (true) {
            int[] nArray;
            int n9;
            int n10;
            int n11;
            int n12;
            int n13;
            int n14;
            FalconFPR[] falconFPRArray = new FalconFPR[3 * n8];
            this.poly_small_mkgauss(sHAKE2562, byArray, n2, n7);
            this.poly_small_mkgauss(sHAKE2562, byArray2, n3, n7);
            int n15 = 1 << this.codec.max_fg_bits[n7] - 1;
            for (n14 = 0; n14 < n8; ++n14) {
                if (byArray[n2 + n14] < n15 && byArray[n2 + n14] > -n15 && byArray2[n3 + n14] < n15 && byArray2[n3 + n14] > -n15) continue;
                n15 = -1;
                break;
            }
            if (n15 < 0 || ((long)(n13 = (n12 = this.poly_small_sqnorm(byArray, n2, n7)) + (n11 = this.poly_small_sqnorm(byArray2, n3, n7)) | -((n12 | n11) >>> 31)) & 0xFFFFFFFFL) >= 16823L) continue;
            int n16 = 0;
            int n17 = n16 + n8;
            int n18 = n17 + n8;
            this.poly_small_to_fp(falconFPRArray, n16, byArray, n2, n7);
            this.poly_small_to_fp(falconFPRArray, n17, byArray2, n3, n7);
            this.fft.FFT(falconFPRArray, n16, n7);
            this.fft.FFT(falconFPRArray, n17, n7);
            this.fft.poly_invnorm2_fft(falconFPRArray, n18, falconFPRArray, n16, falconFPRArray, n17, n7);
            this.fft.poly_adj_fft(falconFPRArray, n16, n7);
            this.fft.poly_adj_fft(falconFPRArray, n17, n7);
            this.fft.poly_mulconst(falconFPRArray, n16, this.fpr.fpr_q, n7);
            this.fft.poly_mulconst(falconFPRArray, n17, this.fpr.fpr_q, n7);
            this.fft.poly_mul_autoadj_fft(falconFPRArray, n16, falconFPRArray, n18, n7);
            this.fft.poly_mul_autoadj_fft(falconFPRArray, n17, falconFPRArray, n18, n7);
            this.fft.iFFT(falconFPRArray, n16, n7);
            this.fft.iFFT(falconFPRArray, n17, n7);
            FalconFPR falconFPR = this.fpr.fpr_zero;
            for (n14 = 0; n14 < n8; ++n14) {
                falconFPR = this.fpr.fpr_add(falconFPR, this.fpr.fpr_sqr(falconFPRArray[n16 + n14]));
                falconFPR = this.fpr.fpr_add(falconFPR, this.fpr.fpr_sqr(falconFPRArray[n17 + n14]));
            }
            if (!this.fpr.fpr_lt(falconFPR, this.fpr.fpr_bnorm_max)) continue;
            short[] sArray2 = new short[2 * n8];
            if (sArray == null) {
                n10 = 0;
                sArray = sArray2;
                n9 = n10 + n8;
            } else {
                n10 = n6;
                n9 = 0;
            }
            if (this.vrfy.compute_public(sArray, n10, byArray, n2, byArray2, n3, n7, sArray2, n9) != 0 && this.solve_NTRU(n7, byArray3, n4, byArray4, n5, byArray, n2, byArray2, n3, n15 = (1 << this.codec.max_FG_bits[n7] - 1) - 1, nArray = n7 > 2 ? new int[28 * n8] : new int[28 * n8 * 3], 0) != 0) break;
        }
    }

    private long toUnsignedLong(int n2) {
        return (long)n2 & 0xFFFFFFFFL;
    }
}

