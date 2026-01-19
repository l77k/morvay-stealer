/*
 * Decompiled with CFR 0.152.
 */
package org.sqlite;

import java.sql.Connection;
import java.sql.SQLException;
import org.sqlite.SQLiteConnection;
import org.sqlite.core.DB;

public abstract class Collation {
    private SQLiteConnection conn;
    private DB db;

    public static final void create(Connection conn, String name, Collation f2) throws SQLException {
        if (conn == null || !(conn instanceof SQLiteConnection)) {
            throw new SQLException("connection must be to an SQLite db");
        }
        if (conn.isClosed()) {
            throw new SQLException("connection closed");
        }
        f2.conn = (SQLiteConnection)conn;
        f2.db = f2.conn.getDatabase();
        if (f2.db.create_collation(name, f2) != 0) {
            throw new SQLException("error creating collation");
        }
    }

    public static final void destroy(Connection conn, String name) throws SQLException {
        if (conn == null || !(conn instanceof SQLiteConnection)) {
            throw new SQLException("connection must be to an SQLite db");
        }
        ((SQLiteConnection)conn).getDatabase().destroy_collation(name);
    }

    protected abstract int xCompare(String var1, String var2);
}

