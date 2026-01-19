/*
 * Decompiled with CFR 0.152.
 */
package org.sqlite;

import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.sqlite.SQLiteErrorCode;
import org.sqlite.core.DB;

public class ExtendedCommand {
    public static SQLExtension parse(String sql) throws SQLException {
        if (sql == null) {
            return null;
        }
        if (sql.length() > 5 && sql.substring(0, 6).toLowerCase().equals("backup")) {
            return BackupCommand.parse(sql);
        }
        if (sql.length() > 6 && sql.substring(0, 7).toLowerCase().equals("restore")) {
            return RestoreCommand.parse(sql);
        }
        return null;
    }

    public static String removeQuotation(String s2) {
        if (s2 == null) {
            return s2;
        }
        if (s2.startsWith("\"") && s2.endsWith("\"") || s2.startsWith("'") && s2.endsWith("'")) {
            return s2.length() >= 2 ? s2.substring(1, s2.length() - 1) : s2;
        }
        return s2;
    }

    public static class RestoreCommand
    implements SQLExtension {
        public final String targetDB;
        public final String srcFile;
        private static Pattern restoreCmd = Pattern.compile("restore(\\s+(\"[^\"]*\"|'[^']*'|\\S+))?\\s+from\\s+(\"[^\"]*\"|'[^']*'|\\S+)", 2);

        public RestoreCommand(String targetDB, String srcFile) {
            this.targetDB = targetDB;
            this.srcFile = srcFile;
        }

        public static RestoreCommand parse(String sql) throws SQLException {
            Matcher m2;
            if (sql != null && (m2 = restoreCmd.matcher(sql)).matches()) {
                String dbName = ExtendedCommand.removeQuotation(m2.group(2));
                String dest = ExtendedCommand.removeQuotation(m2.group(3));
                if (dbName == null || dbName.length() == 0) {
                    dbName = "main";
                }
                return new RestoreCommand(dbName, dest);
            }
            throw new SQLException("syntax error: " + sql);
        }

        @Override
        public void execute(DB db) throws SQLException {
            int rc = db.restore(this.targetDB, this.srcFile, null);
            if (rc != SQLiteErrorCode.SQLITE_OK.code) {
                throw DB.newSQLException(rc, "Restore failed");
            }
        }
    }

    public static class BackupCommand
    implements SQLExtension {
        public final String srcDB;
        public final String destFile;
        private static Pattern backupCmd = Pattern.compile("backup(\\s+(\"[^\"]*\"|'[^']*'|\\S+))?\\s+to\\s+(\"[^\"]*\"|'[^']*'|\\S+)", 2);

        public BackupCommand(String srcDB, String destFile) {
            this.srcDB = srcDB;
            this.destFile = destFile;
        }

        public static BackupCommand parse(String sql) throws SQLException {
            Matcher m2;
            if (sql != null && (m2 = backupCmd.matcher(sql)).matches()) {
                String dbName = ExtendedCommand.removeQuotation(m2.group(2));
                String dest = ExtendedCommand.removeQuotation(m2.group(3));
                if (dbName == null || dbName.length() == 0) {
                    dbName = "main";
                }
                return new BackupCommand(dbName, dest);
            }
            throw new SQLException("syntax error: " + sql);
        }

        @Override
        public void execute(DB db) throws SQLException {
            int rc = db.backup(this.srcDB, this.destFile, null);
            if (rc != SQLiteErrorCode.SQLITE_OK.code) {
                throw DB.newSQLException(rc, "Backup failed");
            }
        }
    }

    public static interface SQLExtension {
        public void execute(DB var1) throws SQLException;
    }
}

