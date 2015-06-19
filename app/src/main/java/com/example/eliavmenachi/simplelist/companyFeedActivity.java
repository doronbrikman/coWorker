package com.example.eliavmenachi.simplelist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.example.eliavmenachi.simplelist.model.Post;
import com.example.eliavmenachi.simplelist.model.Model;
import com.parse.ParseUser;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.BaseAdapter;

import java.util.LinkedList;
import java.util.List;


public class companyFeedActivity extends Activity {
    Button logout;
    ListView myList;
    List<Post> data = new LinkedList<Post>();
    PostAdapter adapter;
    ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_feed);

        progressBar = (ProgressBar) findViewById(R.id.postProgressBar);
        progressBar.setVisibility(View.VISIBLE);

        // Retrieve current user from Parse.com
        ParseUser currentUser = ParseUser.getCurrentUser();
        // Convert currentUser into String
        String   struser = currentUser.getUsername().toString();
        TextView txtuser = (TextView) findViewById(R.id.txtuser);

        txtuser.setText("You are logged in as " + struser);
        logout = (Button) findViewById(R.id.logout);

        logout.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                // Logout current user
                ParseUser.logOut();
                Intent intent = new Intent(
                        companyFeedActivity.this,
                        LoginActivity.class);
                startActivity(intent);
            }
        });

        Model.getInstance().getAllPostsByCompanyAsync(new Model.GetPostsListener() {
            @Override
            public void onResult(List<Post> posts) {
                progressBar.setVisibility(View.GONE);
                data = posts;
                adapter.notifyDataSetChanged();
            }
        });

        myList = (ListView) findViewById(R.id.postslistView);

        adapter = new PostAdapter();
        myList.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_company_feed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class PostAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = getLayoutInflater();
                convertView = inflater.inflate(R.layout.post_layout, null);
            }

            TextView post = (TextView) convertView.findViewById(R.id.postText);
            TextView title = (TextView) convertView.findViewById(R.id.titleText);

            Post pt = data.get(position);
            convertView.setTag(position);
            post.setText(pt.getPost());
            title.setText(pt.getTitle());

            return convertView;
        }
    }
}
