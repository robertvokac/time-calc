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

/**
 *
 * @author <a href="mailto:robertvokac@robertvokac.com">Robert Vokac</a>
 */
public class WorkingDayTable {

    public static final String TABLE_NAME = "WORKING_DAY";

    public static final String ID = "ID";
    public static final String YEAR = "YEAR";
    public static final String MONTH = "MONTH";
    public static final String DAY = "DAY";

    public static final String ARRIVAL_HOUR = "ARRIVAL_HOUR";
    public static final String ARRIVAL_MINUTE = "ARRIVAL_MINUTE";
    public static final String OVERTIME_HOUR = "OVERTIME_HOUR";
    public static final String OVERTIME_MINUTE = "OVERTIME_MINUTE";
    public static final String PAUSE_START_HOUR = "PAUSE_START_HOUR";
    public static final String PAUSE_START_MINUTE = "PAUSE_START_MINUTE";
    public static final String WORKING_TIME_IN_MINUTES = "WORKING_TIME_IN_MINUTES";
    public static final String PAUSE_TIME_IN_MINUTES = "PAUSE_TIME_IN_MINUTES";
    public static final String NOTE = "NOTE";
    public static final String TIME_OFF = "TIME_OFF";
    public static final String FORGET_OVERTIME = "FORGET_OVERTIME";

    private WorkingDayTable() {
        //Not meant to be instantiated.
    }

}
