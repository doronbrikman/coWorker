package com.example.eliavmenachi.simplelist.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.LinkedList;
import java.util.List;

public class ModelParse {


    public void init(Context context) {
        Parse.initialize(context, "XVWymanigAubTFuWH2t0Un36CCQBiwdQkT9yYjjF", "PDsc4qAgyyFyHWO0mutnGc2Q2OsIJ6YyOuK9C6E9");
    }

    public List<Employee> getAllEmployees() {
        ParseQuery q = new ParseQuery("company");
        List<Employee> students = new LinkedList<Employee>();
        ParseQuery query = new ParseQuery("employee");
        try {
            List<ParseObject> data = query.find();
            for (ParseObject po : data) {
                String id = po.getString("empid");
                String name = po.getString("name");
                String address = po.getString("address");
                String imageName = po.getString("imageName");
                String phone = po.getString("phone");
                String dep = po.getString("department");
                String comp = po.getString("company");
                boolean atWork = po.getBoolean("atWork");
                Employee emp = new Employee(id, name, address, imageName, phone, dep, atWork);
                students.add(emp);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return students;
        }
        return students;
    }

    public void getStudentById(String id, final Model.GetStudent listener) {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("student");
        query.whereEqualTo("stid", id);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                Employee emp = null;
                if (e == null && parseObjects.size() > 0) {
                    ParseObject po = parseObjects.get(0);
                    String id = po.getString("empid");
                    String name = po.getString("name");
                    String address = po.getString("address");
                    String imageName = po.getString("imageName");
                    String dep = po.getString("department");
                    String comp = po.getString("company");
                    String phone = po.getString("phone");
                    boolean atWork = po.getBoolean("atWork");
                    emp = new Employee(id, name, address, imageName, phone, dep, atWork);
                }
                listener.onResult(emp);
            }
        });
    }


    public void add(Employee emp) {
        ParseObject empObject = new ParseObject("employee");
        empObject.put("empid", emp.id);
        empObject.put("name", emp.name);
        empObject.put("address", emp.address);
        //empObject.put("imageName", emp.imageName);
        empObject.put("phone", emp.phone);
        empObject.put("department", emp.department);
        empObject.put("atWork", emp.isAtWork);

        try {
            empObject.save();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void getAllCompaniesAsynch(final Model.GetCompaniesListener listener) {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Company");
//        query.whereEqualTo("name", "Microsoft");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                List<Company> companies = new LinkedList<Company>();

                if (e == null) {
                    List<Employee> emps = new LinkedList<Employee>();
                    for (ParseObject co : parseObjects) {
                        String id = co.getObjectId();
                        String name = co.getString("name");
                        JSONArray jsonMainArr = co.getJSONArray("employees");
                        for (int i = 0; i < jsonMainArr.length(); i++) {
                            JSONObject po = null;
                            try {
                                po = jsonMainArr.getJSONObject(i);
                                String eid = po.getString("id");
                                String ename = po.getString("name");
                                String address = po.getString("address");
                                String imageName = po.getString("imageName");
                                String phone = po.getString("phone");
                                String dep = po.getString("department");
                                String comp = po.getString("company");
                                boolean atWork = po.getBoolean("atWork");
                                Employee emp = new Employee(eid, ename, address, imageName, phone, dep, atWork);
                                emps.add(emp);
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                        }
                        Company com = new Company(id, name, emps);
                        companies.add(com);
                    }
                }
                listener.onResult(companies);
            }
        });
    }


    public void saveImage(Bitmap imageBitmap, String imageName) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        ParseFile file = new ParseFile(imageName, byteArray);
        try {
            file.save();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ParseObject images = new ParseObject("images");
        images.put("name", imageName);
        images.put("image", file);
        try {
            images.save();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public Bitmap loadImage(String imageName) {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("images");
        query.whereEqualTo("name", imageName);
        try {
            List<ParseObject> list = query.find();
            if (list.size() > 0) {
                ParseObject po = list.get(0);
                ParseFile pf = po.getParseFile("image");
                byte[] data = pf.getData();
                Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                return bmp;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
