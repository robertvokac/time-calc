package org.nanoboot.utils.timecalc.persistence.impl.sqlite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.nanoboot.utils.timecalc.entity.WorkingDay;
import org.nanoboot.utils.timecalc.persistence.api.ActivityRepositoryApi;

import java.util.List;
import org.nanoboot.utils.timecalc.app.TimeCalcException;
import org.nanoboot.utils.timecalc.entity.Activity;

/**
 * @author Robert Vokac
 * @since 23.02.2024
 */
public class ActivityRepositorySQLiteImpl implements ActivityRepositoryApi {

    private final SqliteConnectionFactory sqliteConnectionFactory;

    public ActivityRepositorySQLiteImpl(SqliteConnectionFactory sqliteConnectionFactory) {
        this.sqliteConnectionFactory = sqliteConnectionFactory;
    }
    
    
    @Override
    public void create(Activity activity) {
                

        StringBuilder sb = new StringBuilder();
        sb
                .append("INSERT INTO ")
                .append(ActivityTable.TABLE_NAME)
                .append(" VALUES (?,?,?,?, ?,?,?,?,?)");

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
    public void update(Activity activity) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public WorkingDay read(String id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void delete(String id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

//
//    @Override
//    public List<WorkingDay> list(int year, int month, int day) {
//
//        List<WorkingDay> result = new ArrayList<>();
//        StringBuilder sb = new StringBuilder();
//        sb
//                .append("SELECT * FROM ")
//                .append(WorkingDayTable.TABLE_NAME)
//                .append(" WHERE ")
//                .append(WorkingDayTable.YEAR).append("=? AND ")
//                .append(WorkingDayTable.MONTH).append("=? ");
//        if (day != 0) {
//            sb.append(" AND ").append(WorkingDayTable.DAY).append("=? ");
//        }
//
//        String sql = sb.toString();
//        int i = 0;
//        ResultSet rs = null;
//        try (
//                Connection connection = sqliteConnectionFactory.createConnection(); PreparedStatement stmt = connection.prepareStatement(sql);) {
//
//            //System.err.println(stmt.toString());
//            stmt.setInt(++i, year);
//            stmt.setInt(++i, month);
//            if (day != 0) {
//                stmt.setInt(++i, day);
//            }
//            rs = stmt.executeQuery();
//
//            while (rs.next()) {
//                result.add(extractWorkingDayFromResultSet(rs));
//            }
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//            throw new RuntimeException(e);
//        } catch (ClassNotFoundException ex) {
//            System.out.println(ex.getMessage());
//            throw new RuntimeException(ex);
//        } finally {
//            try {
//                if (rs != null) {
//                    rs.close();
//                }
//            } catch (SQLException ex) {
//                System.out.println(ex.getMessage());
//                throw new RuntimeException(ex);
//            }
//        }
//        return result;
////
//    }
//
//    @Override
//    public void update(WorkingDay workingDay) {
//        if(list(workingDay.getYear(), workingDay.getMonth(),workingDay.getDay()).isEmpty()) {
//            create(workingDay);
//            return;
//        }
//        System.out.println("Going to update: " + workingDay.toString());
//
//        StringBuilder sb = new StringBuilder();
//        sb
//                .append("UPDATE ")
//                .append(WorkingDayTable.TABLE_NAME)
//                .append(" SET ")
//                .append(WorkingDayTable.ARRIVAL_HOUR).append("=?, ")
//                .append(WorkingDayTable.ARRIVAL_MINUTE).append("=?, ")
//                .append(WorkingDayTable.OVERTIME_HOUR).append("=?, ")
//                .append(WorkingDayTable.OVERTIME_MINUTE).append("=?, ")
//                .append(WorkingDayTable.WORKING_TIME_IN_MINUTES).append("=?, ")
//                .append(WorkingDayTable.PAUSE_TIME_IN_MINUTES).append("=?, ")
//                .append(WorkingDayTable.NOTE).append("=? ")
//                .append(" WHERE ").append(
//                WorkingDayTable.ID).append("=?");
//
//        String sql = sb.toString();
//        //System.err.println(sql);
//        try (
//                Connection connection = sqliteConnectionFactory.createConnection(); PreparedStatement stmt = connection.prepareStatement(sql);) {
//            int i = 0;
//            stmt.setInt(++i, workingDay.getArrivalHour());
//            stmt.setInt(++i, workingDay.getArrivalMinute());
//            stmt.setInt(++i, workingDay.getOvertimeHour());
//            stmt.setInt(++i, workingDay.getOvertimeMinute());
//            stmt.setInt(++i, workingDay.getWorkingTimeInMinutes());
//            stmt.setInt(++i, workingDay.getPauseTimeInMinutes());
//            stmt.setString(++i, workingDay.getNote());
//
//            stmt.setString(++i, workingDay.getId());
//
//            int numberOfUpdatedRows = stmt.executeUpdate();
//            //System.out.println("numberOfUpdatedRows=" + numberOfUpdatedRows);
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//            throw new RuntimeException(e);
//        } catch (ClassNotFoundException ex) {
//            ex.printStackTrace();
//            throw new TimeCalcException(ex);
//        }
//    }
//
//    @Override
//    public WorkingDay read(int year, int month, int day) {
//        List<WorkingDay> list = list(year, month, day);
//        return list.isEmpty() ? null : list.get(0);
//    }
//
//    private WorkingDay extractWorkingDayFromResultSet(final ResultSet rs) throws SQLException {
//        return new WorkingDay(
//                rs.getString(WorkingDayTable.ID),
//                rs.getInt(WorkingDayTable.YEAR),
//                rs.getInt(WorkingDayTable.MONTH),
//                rs.getInt(WorkingDayTable.DAY),
//                rs.getInt(WorkingDayTable.ARRIVAL_HOUR),
//                rs.getInt(WorkingDayTable.ARRIVAL_MINUTE),
//                rs.getInt(WorkingDayTable.OVERTIME_HOUR),
//                rs.getInt(WorkingDayTable.OVERTIME_MINUTE),
//                rs.getInt(WorkingDayTable.WORKING_TIME_IN_MINUTES),
//                rs.getInt(WorkingDayTable.PAUSE_TIME_IN_MINUTES),
//                rs.getString(WorkingDayTable.NOTE)
//        );
//    }
//
    @Override
    public List<String> getYears() {
        
        List<String> result = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb
                .append("SELECT distinct ").append(WorkingDayTable.YEAR). append(" FROM ")
                .append(ActivityTable.TABLE_NAME);

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
    public List<Activity> list(int year, int month, int day) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }


}
