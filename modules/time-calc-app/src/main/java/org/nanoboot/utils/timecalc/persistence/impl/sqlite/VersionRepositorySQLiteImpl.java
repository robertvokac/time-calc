package org.nanoboot.utils.timecalc.persistence.impl.sqlite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.nanoboot.utils.timecalc.app.TimeCalcException;
import org.nanoboot.utils.timecalc.persistence.api.VersionRepositoryApi;

/**
 * @author Robert Vokac
 * @since 23.02.2024
 */
public class VersionRepositorySQLiteImpl implements VersionRepositoryApi {

    private final SqliteConnectionFactory sqliteConnectionFactory;

    public VersionRepositorySQLiteImpl(SqliteConnectionFactory sqliteConnectionFactory) {
        this.sqliteConnectionFactory = sqliteConnectionFactory;
    }

    @Override
    public int read() {
        StringBuilder sb = new StringBuilder();
        sb
                .append("SELECT * FROM ")
                .append(VersionTable.TABLE_NAME);

        String sql = sb.toString();
        //        System.err.println(sql);
        ResultSet rs = null;
        try (
                Connection connection = sqliteConnectionFactory.createConnection(); PreparedStatement stmt = connection.prepareStatement(sql);) {

            System.err.println(stmt.toString());
            rs = stmt.executeQuery();

            while (rs.next()) {
                return rs.getInt(VersionTable.VALUE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        throw new TimeCalcException("Version was not found in database.");
    }

    @Override
    public void update(int newVersion) {

        StringBuilder sb = new StringBuilder();
        sb
                .append("UPDATE ")
                .append(VersionTable.TABLE_NAME)
                .append(" SET ")
                .append(VersionTable.VALUE).append("=").append(newVersion);

        String sql = sb.toString();
        //System.err.println(sql);
        try (
                Connection connection = sqliteConnectionFactory.createConnection(); PreparedStatement stmt = connection.prepareStatement(sql);) {

            int numberOfUpdatedRows = stmt.executeUpdate();
            System.out.println("numberOfUpdatedRows=" + numberOfUpdatedRows);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }
}
