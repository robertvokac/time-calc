package org.nanoboot.utils.timecalc.persistence.impl.sqlite;

import org.nanoboot.utils.timecalc.app.Main;
import org.nanoboot.utils.timecalc.app.TimeCalcException;
import org.nanoboot.utils.timecalc.entity.WorkingDay;
import org.nanoboot.utils.timecalc.persistence.api.WorkingDayRepositoryApi;
import org.nanoboot.utils.timecalc.utils.common.Utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Robert Vokac
 * @since 23.02.2024
 */
public class WorkingDayRepositorySQLiteImpl implements WorkingDayRepositoryApi {

    private final SqliteConnectionFactory sqliteConnectionFactory;

    public WorkingDayRepositorySQLiteImpl(SqliteConnectionFactory sqliteConnectionFactory) {
        this.sqliteConnectionFactory = sqliteConnectionFactory;
    }

    @Override
    public void create(WorkingDay workingDay) {
        System.out.println("Going to create: " + workingDay.toString());

        if(Main.ONLY_ACTIVITIES_WINDOW_IS_ALLOWED || !Utils.askYesNo(null, "Do you want to create new Working Day? " + workingDay, "Creation of new Working Day")) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb
                .append("INSERT INTO ")
                .append(WorkingDayTable.TABLE_NAME)
                .append(" VALUES (?,?,?,?, ?,?,?,?, ?,?,?,?,?)");

        String sql = sb.toString();

        try (
                Connection connection = sqliteConnectionFactory.createConnection(); PreparedStatement stmt = connection.prepareStatement(sql);) {
            int i = 0;

            stmt.setString(++i, workingDay.getId());
            stmt.setInt(++i, workingDay.getYear());
            stmt.setInt(++i, workingDay.getMonth());
            stmt.setInt(++i, workingDay.getDay());
            //
            stmt.setInt(++i, workingDay.getArrivalHour());
            stmt.setInt(++i, workingDay.getArrivalMinute());
            stmt.setInt(++i, workingDay.getOvertimeHour());
            stmt.setInt(++i, workingDay.getOvertimeMinute());
            //
            stmt.setInt(++i, workingDay.getWorkingTimeInMinutes());
            stmt.setInt(++i, workingDay.getPauseTimeInMinutes());
            stmt.setString(++i, workingDay.getNote());
            stmt.setInt(++i, workingDay.isTimeOff() ? 1 : 0);
            stmt.setInt(++i, workingDay.getForgetOvertime());

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
    public List<WorkingDay> list(int year, int month, int day) {

        List<WorkingDay> result = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb
                .append("SELECT * FROM ")
                .append(WorkingDayTable.TABLE_NAME)
                .append(" WHERE ")
                .append(WorkingDayTable.YEAR).append("=? AND ")
                .append(WorkingDayTable.MONTH).append("=? ");
        if (day != 0) {
            sb.append(" AND ").append(WorkingDayTable.DAY).append("=? ");
        }

        String sql = sb.toString();
        int i = 0;
        ResultSet rs = null;
        try (
                Connection connection = sqliteConnectionFactory.createConnection(); PreparedStatement stmt = connection.prepareStatement(sql);) {

            //System.err.println(stmt.toString());
            stmt.setInt(++i, year);
            stmt.setInt(++i, month);
            if (day != 0) {
                stmt.setInt(++i, day);
            }
            rs = stmt.executeQuery();

            while (rs.next()) {
                result.add(extractWorkingDayFromResultSet(rs));
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
//
    }

    @Override
    public void update(WorkingDay workingDay) {
        List<WorkingDay> list =
                list(workingDay.getYear(), workingDay.getMonth(),
                        workingDay.getDay());
        if(list.isEmpty()) {
            create(workingDay);
            return;
        }

        System.out.println("Going to update: " + workingDay.toString());

        if(list.get(0).toString().equals(workingDay.toString())) {
            System.out.println("Nothing to update.");
            return;
        }
        if(Main.ONLY_ACTIVITIES_WINDOW_IS_ALLOWED || !Utils.askYesNo(null, "Do you want to update this Working Day? " + workingDay, "Update of Working Day")) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb
                .append("UPDATE ")
                .append(WorkingDayTable.TABLE_NAME)
                .append(" SET ")
                .append(WorkingDayTable.ARRIVAL_HOUR).append("=?, ")
                .append(WorkingDayTable.ARRIVAL_MINUTE).append("=?, ")
                .append(WorkingDayTable.OVERTIME_HOUR).append("=?, ")
                .append(WorkingDayTable.OVERTIME_MINUTE).append("=?, ")
                .append(WorkingDayTable.WORKING_TIME_IN_MINUTES).append("=?, ")
                .append(WorkingDayTable.PAUSE_TIME_IN_MINUTES).append("=?, ")
                .append(WorkingDayTable.NOTE).append("=?, ")
                .append(WorkingDayTable.TIME_OFF).append("=?, ")
                .append(WorkingDayTable.FORGET_OVERTIME).append("=? ")
                .append(" WHERE ").append(
                WorkingDayTable.ID).append("=?");

        String sql = sb.toString();
        //System.err.println(sql);
        try (
                Connection connection = sqliteConnectionFactory.createConnection(); PreparedStatement stmt = connection.prepareStatement(sql);) {
            int i = 0;
            stmt.setInt(++i, workingDay.getArrivalHour());
            stmt.setInt(++i, workingDay.getArrivalMinute());
            stmt.setInt(++i, workingDay.getOvertimeHour());
            stmt.setInt(++i, workingDay.getOvertimeMinute());
            stmt.setInt(++i, workingDay.getWorkingTimeInMinutes());
            stmt.setInt(++i, workingDay.getPauseTimeInMinutes());
            stmt.setString(++i, workingDay.getNote());
            stmt.setInt(++i, workingDay.isTimeOff() ? 1 : 0);
            stmt.setInt(++i, workingDay.getForgetOvertime());

            stmt.setString(++i, workingDay.getId());

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
    public WorkingDay read(int year, int month, int day) {
        List<WorkingDay> list = list(year, month, day);
        return list.isEmpty() ? null : list.get(0);
    }

    private WorkingDay extractWorkingDayFromResultSet(final ResultSet rs) throws SQLException {
        return new WorkingDay(
                rs.getString(WorkingDayTable.ID),
                rs.getInt(WorkingDayTable.YEAR),
                rs.getInt(WorkingDayTable.MONTH),
                rs.getInt(WorkingDayTable.DAY),
                rs.getInt(WorkingDayTable.ARRIVAL_HOUR),
                rs.getInt(WorkingDayTable.ARRIVAL_MINUTE),
                rs.getInt(WorkingDayTable.OVERTIME_HOUR),
                rs.getInt(WorkingDayTable.OVERTIME_MINUTE),
                rs.getInt(WorkingDayTable.WORKING_TIME_IN_MINUTES),
                rs.getInt(WorkingDayTable.PAUSE_TIME_IN_MINUTES),
                rs.getString(WorkingDayTable.NOTE),
                rs.getInt(WorkingDayTable.TIME_OFF) != 0,
                rs.getInt(WorkingDayTable.FORGET_OVERTIME)
        );
    }

    @Override
    public List<String> getYears() {
        
        List<String> result = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb
                .append("SELECT distinct ").append(WorkingDayTable.YEAR). append(" FROM ")
                .append(WorkingDayTable.TABLE_NAME);

        String sql = sb.toString();
        int i = 0;
        ResultSet rs = null;
        try (
                Connection connection = sqliteConnectionFactory.createConnection(); PreparedStatement stmt = connection.prepareStatement(sql);) {

            rs = stmt.executeQuery();

            while (rs.next()) {
                result.add(rs.getString(WorkingDayTable.YEAR));
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
    public void delete(int year, int month, int day) {
        StringBuilder sb = new StringBuilder();
        sb
                .append("DELETE ").append(" FROM ")
                .append(WorkingDayTable.TABLE_NAME);
        sb.append(" WHERE ");
        sb.append(WorkingDayTable.YEAR).append("=? AND ");
        sb.append(WorkingDayTable.MONTH).append("=? AND ");
        ;
        sb.append(WorkingDayTable.DAY).append("=? ");
        ;

        String sql = sb.toString();
        int i = 0;
        try (
                Connection connection = sqliteConnectionFactory
                        .createConnection();
                PreparedStatement stmt = connection.prepareStatement(sql);) {
            stmt.setInt(++i, year);
            stmt.setInt(++i, month);
            stmt.setInt(++i, day);
            stmt.execute();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        } catch (ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public int getTotalOvertimeForDayInMinutes(int year, int month, int day) {

        StringBuilder sb = new StringBuilder();
        sb
                .append("SELECT (sum(OVERTIME_HOUR)*60 + sum(OVERTIME_MINUTE) - sum(FORGET_OVERTIME)) as total_overtime FROM ")
                .append(WorkingDayTable.TABLE_NAME)
                .append(" WHERE ")
                .append(WorkingDayTable.YEAR).append(" * 10000 + ")
                .append(WorkingDayTable.MONTH).append("* 100 + ")
        .append(WorkingDayTable.DAY).append(" <= ? ");

        String sql = sb.toString();
        System.out.println(sql);
        int i = 0;
        ResultSet rs = null;
        try (
                Connection connection = sqliteConnectionFactory.createConnection(); PreparedStatement stmt = connection.prepareStatement(sql);) {

            //System.err.println(stmt.toString());
            stmt.setInt(++i, year * 10000 + month * 100 + day);
            rs = stmt.executeQuery();
            System.out.println(stmt);
            while (rs.next()) {
                return rs.getInt("total_overtime");
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
        throw new IllegalStateException();
    }

}
