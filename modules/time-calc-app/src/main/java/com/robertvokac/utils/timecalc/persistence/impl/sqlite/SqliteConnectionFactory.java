///////////////////////////////////////////////////////////////////////////////////////////////
// bit-inspector: Tool detecting bit rots in files.
// Copyright (C) 2023-2023 the original author or authors.
//
// This program is free software; you can redistribute it and/or
// modify it under the terms of the GNU General Public License
// as published by the Free Software Foundation; version 2
// of the License only.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
///////////////////////////////////////////////////////////////////////////////////////////////
package com.robertvokac.utils.timecalc.persistence.impl.sqlite;

import com.robertvokac.utils.timecalc.persistence.api.ConnectionFactory;
import com.robertvokac.utils.timecalc.utils.common.FileConstants;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import com.robertvokac.utils.timecalc.app.TimeCalcException;

/**
 *
 * @author <a href="mailto:robertvokac@robertvokac.com">Robert Vokac</a>
 */
public class SqliteConnectionFactory implements ConnectionFactory {

    private final String jdbcUrl;

    private static String createJdbcUrl(String directoryWhereSqliteFileIs) {
        return "jdbc:sqlite:" + directoryWhereSqliteFileIs + "/" + "time-calc.sqlite3?foreign_keys=on;";
    }

    public SqliteConnectionFactory() {
        this(FileConstants.TC_DIRECTORY.getName());
    }

    private SqliteConnectionFactory(String directoryWhereSqliteFileIs) {
        this.jdbcUrl = createJdbcUrl(directoryWhereSqliteFileIs);
    }

    public Connection createConnection() throws ClassNotFoundException {
        try {
            Class.forName("org.sqlite.JDBC");

            Connection conn = DriverManager.getConnection(jdbcUrl);

            return conn;

        } catch (SQLException ex) {
            if (true) {
                throw new RuntimeException(ex);
            }
            System.err.println(ex.getMessage());
            return null;
        }
    }
    public void runSQL(String sql) {
        String[] array = sql.split(";");

        for (String s : array) {
            if (s.trim().isEmpty()) {
                continue;
            }
            System.err.println("Going to run this SQL \"" + s + "\"");
            try (
                    Connection connection = createConnection(); PreparedStatement stmt = connection.prepareStatement(s);) {

                System.err.println(stmt.toString());
                stmt.execute();

            } catch (SQLException e) {
                System.out.println(e.getMessage());
                throw new RuntimeException(e);
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
                throw new TimeCalcException(ex);
            }
        }

    }

}
