package com.example.eliavmenachi.simplelist.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.parse.ParseGeoPoint;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by eliav.menachi on 25/03/2015.
 */
public class Model {
    private final static Model instance = new Model();
    Context context;
    //ModelSql model = new ModelSql();
    ModelParse model = new ModelParse();

    private Model(){
    }

    public static Model getInstance(){
        return instance;
    }

    public void init(Context context){
        this.context = context;
        model.init(context);
    }

    public List<Employee> getAllStudents(){
        return model.getAllEmployees();
    }

    public void getAllPostsByCompanyAsync(GetPostsListener listener)
    {
        model.getCompaniesPostsAsync(listener);
    }

    public void updateEmployeeAtWork() {
        model.updateEmployeeAtWork();
    }

    public interface GetEmployeeListener{
        public void onResult(List<Employee> employees);
    }

    public interface GetCompanyLocation{
        public void onResult(ParseGeoPoint loc);
    }

    public void GetCompanyLoc(GetCompanyLocation listener)
    {
        model.getCompanyLocation(listener);
    }

    public void getAllStudentsAsynch(GetEmployeeListener listener){
        model.getAllEmployeesAsynch(listener);
    }

    public interface GetEmployee{
        public void onResult(Employee employee);
    }

    public void getStudentById(String id,GetEmployee listener){
        model.getEmployeeById(id,listener);
    }

    public void add(Employee st){
        model.add(st);
    }

    public void addPost(Post pt){
        model.addPost(pt);
    }

    public void addCompany(Company cmp, String adminName, String adminPass){
        String newCompanyId = model.addCompany(cmp);
        model.createAdminForCompany(newCompanyId, adminName, adminPass);
    }

    public interface GetPostsListener{
        public void onResult(List<Post> posts);
    }

    public void saveImage(final Bitmap imageBitmap, final String imageName) {
        saveImageToFile(imageBitmap,imageName); // synchronously save image locally
        Thread d = new Thread(new Runnable() {  // asynchronously save image to parse
            @Override
            public void run() {
                model.saveImage(imageBitmap,imageName);
            }
        });
        d.start();
    }

    public interface LoadImageListener{
        public void onResult(Bitmap imageBmp);
    }

    public void loadImage(final String imageName, final LoadImageListener listener) {
        AsyncTask<String,String,Bitmap> task = new AsyncTask<String, String, Bitmap >() {
            @Override
            protected Bitmap doInBackground(String... params) {
                Bitmap bmp = loadImageFromFile(imageName);              //first try to fin the image on the device
                if (bmp == null) {                                      //if image not found - try downloading it from parse
                    bmp = model.loadImage(imageName);
                    if (bmp != null) saveImageToFile(bmp,imageName);    //save the image locally for next time
                }
                return bmp;
            }

            @Override
            protected void onPostExecute(Bitmap result) {
                listener.onResult(result);
            }
        };
        task.execute();
    }

    private void saveImageToFile(Bitmap imageBitmap, String imageFileName){
        FileOutputStream fos;
        OutputStream out = null;
        try {
            File dir = context.getExternalFilesDir(null);
            out = new FileOutputStream(new File(dir,imageFileName));
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Bitmap loadImageFromFile(String fileName){
        String str = null;
        Bitmap bitmap = null;
        try {
            File dir = context.getExternalFilesDir(null);
            InputStream inputStream = new FileInputStream(new File(dir,fileName));
            bitmap = BitmapFactory.decodeStream(inputStream);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
