package com.example.eliavmenachi.simplelist.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseACL;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class ModelParse {


    public void init(Context context) {
        Parse.initialize(context, "XVWymanigAubTFuWH2t0Un36CCQBiwdQkT9yYjjF", "PDsc4qAgyyFyHWO0mutnGc2Q2OsIJ6YyOuK9C6E9");
        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
    }

    public List<Employee> getAllEmployees() {
        ParseUser currentUser = ParseUser.getCurrentUser();
        String companyId = currentUser.get("companyId").toString();
        List<Employee> employees = new LinkedList<Employee>();

        ParseQuery query = new ParseQuery("Employee");
        query.whereEqualTo("companyId", companyId);
        try {
            List<ParseObject> data = query.find();
            for (ParseObject po : data) {
                String id = po.getString("serialId");
                String name = po.getString("name");
                String address = po.getString("address");
                String imageName = po.getString("imageName");
                String phone = po.getString("phone");
                String dep = po.getString("department");
                boolean atWork = po.getBoolean("atWork");
                Employee emp = new Employee(id, name, address, imageName, phone, dep, atWork);
                employees.add(emp);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return employees;
        }
        return employees;
    }

    public void getEmployeeById(String id, final Model.GetEmployee listener) {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Employee");
        query.whereEqualTo("serialId", id);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                Employee emp = null;
                if (e == null && parseObjects.size() > 0) {
                    ParseObject po = parseObjects.get(0);
                    String id = po.getString("serialId");
                    String name = po.getString("name");
                    String address = po.getString("address");
                    String imageName = po.getString("imageName");
                    String dep = po.getString("department");
                    String phone = po.getString("phone");
                    boolean atWork = po.getBoolean("atWork");
                    emp = new Employee(id, name, address, imageName, phone, dep, atWork);
                }
                listener.onResult(emp);
            }
        });
    }

    public void getCompanyLocation(final Model.GetCompanyLocation listener) {
        ParseUser currentUser = ParseUser.getCurrentUser();
        String companyId = currentUser.get("companyId").toString();

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Company");
        query.whereEqualTo("objectId", companyId);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                ParseGeoPoint loc = null;
                if (e == null && parseObjects.size() > 0) {
                    ParseObject po = parseObjects.get(0);
                    loc = po.getParseGeoPoint("location");
                }
                listener.onResult(loc);
            }
        });
    }



    public void add(Employee emp) {
        ParseUser currentUser = ParseUser.getCurrentUser();
        String companyId = currentUser.get("companyId").toString();

        ParseObject empObject = new ParseObject("Employee");
        empObject.put("serialId", emp.id);
        empObject.put("name", emp.name);
        empObject.put("address", emp.address);
        empObject.put("imageName", emp.imageName);
        empObject.put("phone", emp.phone);
        empObject.put("department", emp.department);
        empObject.put("companyId", companyId);
        empObject.put("atWork", emp.isAtWork);

        try {
            empObject.save();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void addPost(Post post) {
        ParseObject pstObject = new ParseObject("Post");
        pstObject.put("postComment", post.postComment);
        pstObject.put("postTitle", post.postTitle);
        pstObject.put("company", ParseUser.getCurrentUser().get("companyId").toString());
        pstObject.put("createdDate", new Date());

        try {
            pstObject.save();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String addCompany(Company company) {
        ParseObject pstObject = new ParseObject("Company");
        pstObject.put("name", company.getName());
        pstObject.put("location", company.location);

        try {
            pstObject.save();
            return pstObject.getObjectId();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return pstObject.getObjectId();
    }

    public void createAdminForCompany(String newCompanyId, String adminName, String adminPass) {
        ParseUser newAdmin = new ParseUser();

        newAdmin.setUsername(adminName);
        newAdmin.setPassword(adminPass);
        newAdmin.put("companyId", newCompanyId);
        newAdmin.put("admin", true);

        newAdmin.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    // Hooray! Let them use the app now.
                } else {
                    // Sign up didn't succeed. Look at the ParseException
                    // to figure out what went wrong
                }
            }
        });
    }

    public void getAllEmployeesAsynch(final Model.GetEmployeeListener listener) {
        ParseUser currentUser = ParseUser.getCurrentUser();
        String companyId = currentUser.get("companyId").toString();

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Employee");
        query.whereEqualTo("companyId", companyId);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                List<Employee> emps = new LinkedList<Employee>();
                if (e == null) {
                    for (ParseObject em : parseObjects) {
                        String eid = em.getString("serialId");
                        String ename = em.getString("name");
                        String address = em.getString("address");
                        String imageName = em.getString("imageName");
                        String phone = em.getString("phone");
                        String dep = em.getString("department");
                        boolean atWork = em.getBoolean("atWork");
                        Employee emp = new Employee(eid, ename, address, imageName, phone, dep, atWork);
                        emps.add(emp);
                    }
                }
                listener.onResult(emps);
            }
        });
    }

    public void getCompaniesPostsAsync(final Model.GetPostsListener listener) {
        ParseUser currentUser = ParseUser.getCurrentUser();
        String companyId = currentUser.get("companyId").toString();

        ParseQuery<ParseObject> queryPosts = new ParseQuery<ParseObject>("Post");
        queryPosts.whereEqualTo("company", companyId);
        queryPosts.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                List<Post> posts = new LinkedList<Post>();
                if (e == null) {
                    for (ParseObject co : parseObjects) {
                        String id = co.getObjectId();
                        String post = co.getString("postComment");
                        String title = co.getString("postTitle");
                        Date createdAt = co.getCreatedAt();
                        Post p = new Post(id, post, title, createdAt);
                        posts.add(p);
                    }
                }
                listener.onResult(posts);
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
