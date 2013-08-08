package com.me.commonlibrary.database;

import com.me.commonlibrary.database.ColumnsDefinition.Constraint;
import com.me.commonlibrary.database.ColumnsDefinition.DataType;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.util.ArrayList;

public class SQLiteTable {
    String mTableName;
    ArrayList<ColumnsDefinition> mColumnsDefinitions=new ArrayList<ColumnsDefinition>();
    public SQLiteTable(String tableName)
    {
        mTableName=tableName;
        mColumnsDefinitions.add(new ColumnsDefinition(BaseColumns._ID, Constraint.PRIMARY_KEY, DataType.INTEGER));
        
    }
    public SQLiteTable addColumn(ColumnsDefinition columnsDefinition) {
        mColumnsDefinitions.add(columnsDefinition);
        return this;
    }
    public void create(SQLiteDatabase db)
    {
        String formatter = " %s";
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("CREATE TABLE ");
        stringBuilder.append(mTableName);
        stringBuilder.append("(");
        int columnCount = mColumnsDefinitions.size();
        int index = 0;
        for (ColumnsDefinition columnsDefinition : mColumnsDefinitions) {
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
        db.execSQL(stringBuilder.toString());
    }
    public void delete(final SQLiteDatabase db)
    {
      db.execSQL("DROP TABLE IF EXISTS "+mTableName);
    }
}
