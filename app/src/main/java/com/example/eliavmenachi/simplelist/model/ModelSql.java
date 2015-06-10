//package com.example.eliavmenachi.simplelist.model;
//
//import android.content.Context;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//
//import java.util.List;
//
///**
// * Created by eliav.menachi on 25/03/2015.
// */
//public class ModelSql {
//    final static int VERSION = 4;
//
//    Helper sqlDb;
//
//    public void init(Context context){
//        if (sqlDb == null){
//            sqlDb = new Helper(context);
//        }
//    }
//
//    public List<Student> getAllStudents(){
//        SQLiteDatabase db = sqlDb.getReadableDatabase();
//        return StudentSql.getAllStudents(db);
//    }
//
//    public Student getStudentById(String id){
//        SQLiteDatabase db = sqlDb.getReadableDatabase();
//        return StudentSql.getStudentById(db,id);
//    }
//
//    public void add(Student st){
//        SQLiteDatabase db = sqlDb.getWritableDatabase();
//        StudentSql.add(db,st);
//    }
//
//
//    class Helper extends SQLiteOpenHelper{
//        public Helper(Context context) {
//            super(context, "database.db", null, VERSION);
//        }
//
//        @Override
//        public void onCreate(SQLiteDatabase db) {
//            StudentSql.create(db);
//        }
//
//        @Override
//        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//            StudentSql.drop(db);
//            onCreate(db);
//        }
//    }
//}
