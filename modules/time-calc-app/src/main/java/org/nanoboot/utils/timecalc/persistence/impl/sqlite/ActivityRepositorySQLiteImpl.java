package org.nanoboot.utils.timecalc.persistence.impl.sqlite;

import org.nanoboot.utils.timecalc.app.TimeCalcException;
import org.nanoboot.utils.timecalc.entity.Activity;
import org.nanoboot.utils.timecalc.persistence.api.ActivityRepositoryApi;
import org.nanoboot.utils.timecalc.utils.common.Utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;

/**
 * @author Robert Vokac
 * @since 23.02.2024
 */
public class ActivityRepositorySQLiteImpl implements ActivityRepositoryApi {

    private final SqliteConnectionFactory sqliteConnectionFactory;

    public ActivityRepositorySQLiteImpl(SqliteConnectionFactory sqliteConnectionFactory) {
        this.sqliteConnectionFactory = sqliteConnectionFactory;
    }

    private Activity activityInClipboard = null;
    @Override
    public void create(Activity activity) {

        StringBuilder sb = new StringBuilder();
        sb
                .append("INSERT INTO ")
                .append(ActivityTable.TABLE_NAME)
                .append(" VALUES (?,?,?,?,?, ?,?,?,?,? ,?)");

        String sql = sb.toString();

        try (
                Connection connection = sqliteConnectionFactory.createConnection(); PreparedStatement stmt = connection.prepareStatement(sql);) {
            int i = 0;

            stmt.setString(++i, activity.getId());
            stmt.setInt(++i, activity.getYear());
            stmt.setInt(++i, activity.getMonth());
            stmt.setInt(++i, activity.getDay());
            //
            stmt.setString(++i, activity.getName());
            stmt.setString(++i, activity.getComment());
            stmt.setString(++i, activity.getTicket());
            stmt.setInt(++i, activity.getSpentHours());
            stmt.setInt(++i, activity.getSpentMinutes());
            stmt.setString(++i, activity.getFlags());
            stmt.setInt(++i, activity.getSortkey());

            //
            stmt.execute();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            throw new TimeCalcException(ex);
        }

    }

    @Override
    public void delete(String id) {
        System.out.println("Going to delete: " + id);
        Activity activityToBeDeleted = read(id);
        if(!Utils.askYesNo(null, "Do you really want to delete this activity? " + read(id), "Deletion of activity")) {
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb
                .append("DELETE FROM ")
                .append(ActivityTable.TABLE_NAME)
                .append(" WHERE ").append(
                ActivityTable.ID).append("=?");

        String sql = sb.toString();
        //System.err.println(sql);
        try (
                Connection connection = sqliteConnectionFactory.createConnection(); PreparedStatement stmt = connection.prepareStatement(sql);) {
            int i = 0;

            stmt.setString(++i, id);

            int numberOfUpdatedRows = stmt.executeUpdate();
            //System.out.println("numberOfUpdatedRows=" + numberOfUpdatedRows);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            throw new TimeCalcException(ex);
        }


    }

    @Override
    public List<Activity> list(int year, int month, int day) {

        List<Activity> result = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb
                .append("SELECT * FROM ")
                .append(ActivityTable.TABLE_NAME)
                .append(" WHERE ")
                .append(ActivityTable.YEAR).append("=? AND ")
                .append(ActivityTable.MONTH).append("=? AND ")
                .append(ActivityTable.DAY).append("=? ");

        String sql = sb.toString();
        int i = 0;
        ResultSet rs = null;
        try (
                Connection connection = sqliteConnectionFactory.createConnection(); PreparedStatement stmt = connection.prepareStatement(sql);) {

            stmt.setInt(++i, year);
            stmt.setInt(++i, month);
            stmt.setInt(++i, day);
            rs = stmt.executeQuery();

            while (rs.next()) {
                result.add(extractActivityFromResultSet(rs));
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
                throw new RuntimeException(ex);
            }
        }
        return result;
//
    }

    @Override
    public List<Activity> list(String ticket) {

        List<Activity> result = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb
                .append("SELECT * FROM ")
                .append(ActivityTable.TABLE_NAME)
                .append(" WHERE ")
                .append(ActivityTable.TICKET).append("=? ");

        String sql = sb.toString();
        int i = 0;
        ResultSet rs = null;
        try (
                Connection connection = sqliteConnectionFactory.createConnection(); PreparedStatement stmt = connection.prepareStatement(sql);) {

            stmt.setString(++i, ticket);
            rs = stmt.executeQuery();

            while (rs.next()) {
                result.add(extractActivityFromResultSet(rs));
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
                throw new RuntimeException(ex);
            }
        }
        return result;
    }

        @Override
    public void update(Activity activity) {
        System.out.println("Going to update: " + activity.toString());

        StringBuilder sb = new StringBuilder();
        sb
                .append("UPDATE ")
                .append(ActivityTable.TABLE_NAME)
                .append(" SET ")
                .append(ActivityTable.NAME).append("=?, ")
                .append(ActivityTable.COMMENT).append("=?, ")
                .append(ActivityTable.TICKET).append("=?, ")
                .append(ActivityTable.SPENT_HOURS).append("=?, ")
                .append(ActivityTable.SPENT_MINUTES).append("=?, ")
                .append(ActivityTable.FLAGS).append("=?, ")
                .append(ActivityTable.SORTKEY).append("=? ")
                .append(" WHERE ").append(
                ActivityTable.ID).append("=?");

        String sql = sb.toString();
        //System.err.println(sql);
        try (
                Connection connection = sqliteConnectionFactory.createConnection(); PreparedStatement stmt = connection.prepareStatement(sql);) {
            int i = 0;
            stmt.setString(++i, activity.getName());
            stmt.setString(++i, activity.getComment());
            stmt.setString(++i, activity.getTicket());
            stmt.setInt(++i, activity.getSpentHours());
            stmt.setInt(++i, activity.getSpentMinutes());
            stmt.setString(++i, activity.getFlags());
            stmt.setInt(++i, activity.getSortkey());

            stmt.setString(++i, activity.getId());

            int numberOfUpdatedRows = stmt.executeUpdate();
            //System.out.println("numberOfUpdatedRows=" + numberOfUpdatedRows);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            throw new TimeCalcException(ex);
        }
    }

    @Override
    public Activity read(String id) {

        List<Activity> result = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb
                .append("SELECT * FROM ")
                .append(ActivityTable.TABLE_NAME)
                .append(" WHERE ")
                .append(ActivityTable.ID).append("=? ");

        String sql = sb.toString();
        int i = 0;
        ResultSet rs = null;
        try (
                Connection connection = sqliteConnectionFactory.createConnection(); PreparedStatement stmt = connection.prepareStatement(sql);) {

            stmt.setString(++i, id);
            rs = stmt.executeQuery();

            while (rs.next()) {
                result.add(extractActivityFromResultSet(rs));
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
                throw new RuntimeException(ex);
            }
        }

        return result.stream().findFirst().orElse(null);

    }

    private Activity extractActivityFromResultSet(final ResultSet rs) throws SQLException {
        return new Activity(
                rs.getString(ActivityTable.ID),
                rs.getInt(ActivityTable.YEAR),
                rs.getInt(ActivityTable.MONTH),
                rs.getInt(ActivityTable.DAY),
                rs.getString(ActivityTable.NAME),
                rs.getString(ActivityTable.COMMENT),
                rs.getString(ActivityTable.TICKET),
                rs.getInt(ActivityTable.SPENT_HOURS),
                rs.getInt(ActivityTable.SPENT_MINUTES),
                rs.getString(ActivityTable.FLAGS),
                rs.getInt(ActivityTable.SORTKEY)
        );
    }

    @Override
    public List<String> getYears() {
        
        List<String> result = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb
                .append("SELECT distinct ").append(ActivityTable.YEAR). append(" FROM ")
                .append(ActivityTable.TABLE_NAME);

        String sql = sb.toString();
        int i = 0;
        ResultSet rs = null;
        try (
                Connection connection = sqliteConnectionFactory.createConnection(); PreparedStatement stmt = connection.prepareStatement(sql);) {

            rs = stmt.executeQuery();

            while (rs.next()) {
                result.add(rs.getString(ActivityTable.YEAR));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        } catch (ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
            throw new RuntimeException(ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
                throw new RuntimeException(ex);
            }
        }
        return result;
    }

    @Override
    public void putToClipboard(Activity activity) {
        this.activityInClipboard = activity;
    }

    @Override
    public Activity getFromClipboard() {
        if(this.activityInClipboard == null) {
            return null;
        }
        Activity a = new Activity(
                null,
                2000, 1,1,
                activityInClipboard.getName(),
                activityInClipboard.getComment(),
                activityInClipboard.getTicket(),
                0,0, "", 1);
        return activityInClipboard;
    }

    @Override
    public int getNextSortkey(int year, int month, int day) {
        OptionalInt optional =
                list(year, month, day).stream().map(Activity::getSortkey)
                        .mapToInt(e -> e).max();
        if (optional.isPresent()) {
            System.out.println("getLargestSortkey=" +optional.getAsInt());
            int result = optional.getAsInt() + 10;
            if(result % 10 != 0) {
                while(result % 10 != 0) {
                    result++;
                }
            }
            return result;
        } else {
            return 10;
        }
    }

}
