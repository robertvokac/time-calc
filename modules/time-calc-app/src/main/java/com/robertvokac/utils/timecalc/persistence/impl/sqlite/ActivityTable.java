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
class ActivityTable {

    public static final String TABLE_NAME = "ACTIVITY";

    public static final String ID = "ID";
    public static final String YEAR = "YEAR";
    public static final String MONTH = "MONTH";
    public static final String DAY = "DAY";

    public static final String NAME = "NAME";
    public static final String COMMENT = "COMMENT";
    public static final String TICKET = "TICKET";
    public static final String SPENT_HOURS = "SPENT_HOURS";
    public static final String SPENT_MINUTES = "SPENT_MINUTES";
    public static final String FLAGS = "FLAGS";
    public static final String SORTKEY = "SORTKEY";

    private ActivityTable() {
        //Not meant to be instantiated.
    }

}
