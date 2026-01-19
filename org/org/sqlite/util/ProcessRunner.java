/*
 * Decompiled with CFR 0.152.
 */
package org.sqlite.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

public class ProcessRunner {
    String runAndWaitFor(String command) throws IOException, InterruptedException {
        Process p2 = Runtime.getRuntime().exec(command);
        p2.waitFor();
        return ProcessRunner.getProcessOutput(p2);
    }

    String runAndWaitFor(String command, long timeout2, TimeUnit unit) throws IOException, InterruptedException {
        Process p2 = Runtime.getRuntime().exec(command);
        p2.waitFor(timeout2, unit);
        return ProcessRunner.getProcessOutput(p2);
    }

    static String getProcessOutput(Process process) throws IOException {
        try (InputStream in = process.getInputStream();){
            int readLen;
            ByteArrayOutputStream b2 = new ByteArrayOutputStream();
            byte[] buf = new byte[32];
            while ((readLen = in.read(buf, 0, buf.length)) >= 0) {
                b2.write(buf, 0, readLen);
            }
            String string = b2.toString();
            return string;
        }
    }
}

