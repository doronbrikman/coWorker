//package com.example.eliavmenachi.simplelist.model;
//
//import android.content.ContentValues;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//
//import java.util.LinkedList;
//import java.util.List;
//
///**
// * Created by eliav.menachi on 13/05/2015.
// */
//public class StudentSql {
//    final static String STUDENT_TABLE = "students";
//    final static String STUDENT_TABLE_ID = "_id";
//    final static String STUDENT_TABLE_NAME = "name";
//    final static String STUDENT_TABLE_ADDRESS = "address";
//    final static String STUDENT_TABLE_IMAGE_NAME = "image_name";
//    final static String STUDENT_TABLE_PHONE = "phone";
//    final static String STUDENT_TABLE_CHECKABLE = "checkable";
//
//    static public void create(SQLiteDatabase db) {
//        db.execSQL("create table " + STUDENT_TABLE + " (" +
//                STUDENT_TABLE_ID + " TEXT PRIMARY KEY," +
//                STUDENT_TABLE_NAME + " TEXT," +
//                STUDENT_TABLE_ADDRESS + " TEXT," +
//                STUDENT_TABLE_IMAGE_NAME + " TEXT," +
//                STUDENT_TABLE_PHONE + " TEXT," +
//                STUDENT_TABLE_CHECKABLE + " BOOLEAN);");
//    }
//
//    public static void drop(SQLiteDatabase db) {
//        db.execSQL("drop table " + STUDENT_TABLE + ";");
//    }
//
//    public static List<Student> getAllStudents(SQLiteDatabase db) {
//        Cursor cursor = db.query(STUDENT_TABLE, null, null, null, null, null, null);
//        List<Student> students = new LinkedList<Student>();
//
//        if (cursor.moveToFirst()) {
//            int idIndex = cursor.getColumnIndex(STUDENT_TABLE_ID);
//            int nameIndex = cursor.getColumnIndex(STUDENT_TABLE_NAME);
//            int addressIndex = cursor.getColumnIndex(STUDENT_TABLE_ADDRESS);
//            int imageNameIndex = cursor.getColumnIndex(STUDENT_TABLE_IMAGE_NAME);
//            int phoneIndex = cursor.getColumnIndex(STUDENT_TABLE_PHONE);
//            int checkableIndex = cursor.getColumnIndex(STUDENT_TABLE_CHECKABLE);
//            do {
//                String id = cursor.getString(idIndex);
//                String name = cursor.getString(nameIndex);
//                String address = cursor.getString(addressIndex);
//                String imageName = cursor.getString(imageNameIndex);
//                String phone = cursor.getString(phoneIndex);
//                int checkable = cursor.getInt(checkableIndex); //0 false / 1 true
//                Student st = new Student(id, name, address, imageName, phone, checkable == 1);
//                students.add(st);
//            } while (cursor.moveToNext());
//        }
//        return students;
//    }
//
//    public static Student getStudentById(SQLiteDatabase db, String id) {
//        String where = STUDENT_TABLE_ID + " = ?";
//        String[] args = {id};
//        Cursor cursor = db.query(STUDENT_TABLE, null, where, args, null, null, null);
//
//        if (cursor.moveToFirst()) {
//            int idIndex = cursor.getColumnIndex(STUDENT_TABLE_ID);
//            int nameIndex = cursor.getColumnIndex(STUDENT_TABLE_NAME);
//            int addressIndex = cursor.getColumnIndex(STUDENT_TABLE_ADDRESS);
//            int imageNameIndex = cursor.getColumnIndex(STUDENT_TABLE_IMAGE_NAME);
//            int phoneIndex = cursor.getColumnIndex(STUDENT_TABLE_PHONE);
//            int checkableIndex = cursor.getColumnIndex(STUDENT_TABLE_CHECKABLE);
//            String _id = cursor.getString(idIndex);
//            String name = cursor.getString(nameIndex);
//            String address = cursor.getString(addressIndex);
//            String imageName = cursor.getString(imageNameIndex);
//            String phone = cursor.getString(phoneIndex);
//            int checkable = cursor.getInt(checkableIndex); //0 false / 1 true
//            Student st = new Student(_id, name, address, imageName, phone, checkable == 1);
//            return st;
//        }
//        return null;
//    }
//
//    public static void add(SQLiteDatabase db, Student st) {
//        ContentValues values = new ContentValues();
//        values.put(STUDENT_TABLE_ID, st.getId());
//        values.put(STUDENT_TABLE_NAME, st.getName());
//        values.put(STUDENT_TABLE_ADDRESS, st.getAddress());
//        values.put(STUDENT_TABLE_IMAGE_NAME, st.getImageName());
//        values.put(STUDENT_TABLE_PHONE, st.getPhone());
//        if (st.isChecked()) {
//            values.put(STUDENT_TABLE_CHECKABLE, 1);
//        } else {
//            values.put(STUDENT_TABLE_CHECKABLE, 0);
//        }
//        db.insert(STUDENT_TABLE, STUDENT_TABLE_ID, values);
//    }
//}
