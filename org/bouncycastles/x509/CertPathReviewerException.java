/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.x509;

import java.security.cert.CertPath;
import org.bouncycastle.i18n.ErrorBundle;
import org.bouncycastle.i18n.LocalizedException;

public class CertPathReviewerException
extends LocalizedException {
    private int index = -1;
    private CertPath certPath = null;

    public CertPathReviewerException(ErrorBundle errorBundle, Throwable throwable) {
        super(errorBundle, throwable);
    }

    public CertPathReviewerException(ErrorBundle errorBundle) {
        super(errorBundle);
    }

    public CertPathReviewerException(ErrorBundle errorBundle, Throwable throwable, CertPath certPath, int n2) {
        super(errorBundle, throwable);
        if (certPath == null || n2 == -1) {
            throw new IllegalArgumentException();
        }
        if (n2 < -1 || n2 >= certPath.getCertificates().size()) {
            throw new IndexOutOfBoundsException();
        }
        this.certPath = certPath;
        this.index = n2;
    }

    public CertPathReviewerException(ErrorBundle errorBundle, CertPath certPath, int n2) {
        super(errorBundle);
        if (certPath == null || n2 == -1) {
            throw new IllegalArgumentException();
        }
        if (n2 < -1 || n2 >= certPath.getCertificates().size()) {
            throw new IndexOutOfBoundsException();
        }
        this.certPath = certPath;
        this.index = n2;
    }

    public CertPath getCertPath() {
        return this.certPath;
    }

    public int getIndex() {
        return this.index;
    }
}

