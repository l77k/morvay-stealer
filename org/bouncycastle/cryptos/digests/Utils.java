/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.digests;

import org.bouncycastle.crypto.CryptoServiceProperties;
import org.bouncycastle.crypto.CryptoServicePurpose;
import org.bouncycastle.crypto.Digest;

class Utils {
    Utils() {
    }

    static CryptoServiceProperties getDefaultProperties(Digest digest, CryptoServicePurpose cryptoServicePurpose) {
        return new DefaultProperties(digest.getDigestSize() * 4, digest.getAlgorithmName(), cryptoServicePurpose);
    }

    static CryptoServiceProperties getDefaultProperties(Digest digest, int n2, CryptoServicePurpose cryptoServicePurpose) {
        return new DefaultPropertiesWithPRF(digest.getDigestSize() * 4, n2, digest.getAlgorithmName(), cryptoServicePurpose);
    }

    private static class DefaultProperties
    implements CryptoServiceProperties {
        private final int bitsOfSecurity;
        private final String algorithmName;
        private final CryptoServicePurpose purpose;

        public DefaultProperties(int n2, String string, CryptoServicePurpose cryptoServicePurpose) {
            this.bitsOfSecurity = n2;
            this.algorithmName = string;
            this.purpose = cryptoServicePurpose;
        }

        @Override
        public int bitsOfSecurity() {
            return this.bitsOfSecurity;
        }

        @Override
        public String getServiceName() {
            return this.algorithmName;
        }

        @Override
        public CryptoServicePurpose getPurpose() {
            return this.purpose;
        }

        @Override
        public Object getParams() {
            return null;
        }
    }

    private static class DefaultPropertiesWithPRF
    implements CryptoServiceProperties {
        private final int bitsOfSecurity;
        private final int prfBitsOfSecurity;
        private final String algorithmName;
        private final CryptoServicePurpose purpose;

        public DefaultPropertiesWithPRF(int n2, int n3, String string, CryptoServicePurpose cryptoServicePurpose) {
            this.bitsOfSecurity = n2;
            this.prfBitsOfSecurity = n3;
            this.algorithmName = string;
            this.purpose = cryptoServicePurpose;
        }

        @Override
        public int bitsOfSecurity() {
            if (this.purpose == CryptoServicePurpose.PRF) {
                return this.prfBitsOfSecurity;
            }
            return this.bitsOfSecurity;
        }

        @Override
        public String getServiceName() {
            return this.algorithmName;
        }

        @Override
        public CryptoServicePurpose getPurpose() {
            return this.purpose;
        }

        @Override
        public Object getParams() {
            return null;
        }
    }
}

