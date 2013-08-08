package com.me.commonlibrary.database;

public class ColumnsDefinition {
     public static enum Constraint{
         UNIQUE("UNIQUE"), NOT("NOT"), NULL("NULL"), CHECK("CHECK"), FOREIGN_KEY("FOREIGN KEY"), PRIMARY_KEY(
                 "PRIMARY KEY");
         
         private String value;
         private Constraint(String value)
         {
             this.value=value;
         }
         @Override
         public String toString(){
             return value;
         }
     }
     
     public static enum DataType
     {
         NULL,INTEGER,REAL,TEXT,BLOB
     }
     
     private String mColumnName;
     private Constraint mConstraint;
     private DataType mDataType;
    public ColumnsDefinition(String mColumnName, Constraint mContraint, DataType mDataType) {

        super();
        this.mColumnName = mColumnName;
        this.mConstraint = mContraint;
        this.mDataType = mDataType;
    }
    public String getmColumnName() {
    
        return mColumnName;
    }
    public void setmColumnName(String mColumnName) {
    
        this.mColumnName = mColumnName;
    }
    public Constraint getmConstraint() {
    
        return mConstraint;
    }
    public void setmConstraint(Constraint mConstraint) {
    
        this.mConstraint = mConstraint;
    }
    public DataType getmDataType() {
    
        return mDataType;
    }
    public void setmDataType(DataType mDataType) {
    
        this.mDataType = mDataType;
    }
     
     
}
