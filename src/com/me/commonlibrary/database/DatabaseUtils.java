package com.me.commonlibrary.database;

import com.me.commonlibrary.database.ColumnsDefinition.Constraint;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public class DatabaseUtils {
    public static void createTable1(final SQLiteDatabase db, final String tableName,
            final String[] columnsDefinition) {
        String queryStr = "CREATE TABLE " + tableName + "(" + BaseColumns._ID
                + " INTEGER  PRIMARY KEY ,";
        // Add the columns now, Increase by 2
        for (int i = 0; i < (columnsDefinition.length - 1); i += 2) {
            if (i != 0) {
                queryStr += ",";
            }
            queryStr += columnsDefinition[i] + " " + columnsDefinition[i + 1];
        }

        queryStr += ");";

        db.execSQL(queryStr);
    }

    public static void deleteTable1(final SQLiteDatabase db, final String tableName) {
        db.execSQL("DROP TABLE IF EXISTS " + tableName);
    }

    public static void createTable(final SQLiteDatabase db, final SQLiteTable table) {
        String formatter = " %s";
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("CREATE TABLE ");
        stringBuilder.append(table.mTableName);
        stringBuilder.append("(");
        int columnCount = table.mColumnsDefinitions.size();
        int index = 0;
        for (ColumnsDefinition columnsDefinition : table.mColumnsDefinitions) {
            stringBuilder.append(columnsDefinition.getmColumnName()).append(
                    String.format(formatter, columnsDefinition.getmDataType().name()));
            Constraint constraint = columnsDefinition.getmConstraint();

            if (constraint != null) {
                stringBuilder.append(String.format(formatter, constraint.toString()));
            }
            if (index < columnCount - 1) {
                stringBuilder.append(",");
            }
            index++;
        }
        stringBuilder.append(");");
        //Log.e("sql", stringBuilder.toString());
        // db.execSQL(stringBuilder.toString());
    }
}
