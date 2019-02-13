package com.hyper.design.medicalcard.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.hyper.design.medicalcard.AdaptersRecyclerView.RecyclerViewOfBlog;
import com.hyper.design.medicalcard.MainActivity;
import com.hyper.design.medicalcard.R;
import com.hyper.design.medicalcard.dataProcessBlogs.Blog;
import com.hyper.design.medicalcard.dataProcessBlogs.jsonParserBlog;
import com.hyper.design.medicalcard.network_hus.MySingletonhus;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.hyper.design.medicalcard.AppConstant.SERVER_URL;

public class BlogActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerViewOfBlog recyclerViewOfBlog;
    public ArrayList<Blog> blogArrayList;
    public static final String SERVER_MEDICAL_URL = SERVER_URL;
    public static final String SERVER_URL_BLOG= "/blogcategory";

    private ProgressBar pbBlog;

    public Blog blog;

    private int blogId;

    private String blogStringUrl;

    private String doctorName;
    private String doctorProfession;
    private String discountPercentage;

    private String doctorSpecialist;
    private int discountPercentage1;

    jsonParserBlog jsonParserBlog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /*ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);*/

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        recyclerView = (RecyclerView) findViewById(R.id.rv_blog);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        pbBlog = (ProgressBar) findViewById(R.id.pbBlog);

        blog = new Blog();
        blogArrayList = new ArrayList<>();
        jsonParserBlog = new jsonParserBlog(this);

        showProgress(true);
        getRegionAndCategory();

    }

    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == android.R.id.home)
        {
            Intent intent = new Intent(BlogActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        /*Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);*/
        return super.onOptionsItemSelected(item);//true
    }

    private void getRegionAndCategory(){

        final RequestQueue requestQueue = Volley.newRequestQueue(BlogActivity.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, SERVER_MEDICAL_URL + SERVER_URL_BLOG,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonArray) {

                        Log.i("response", jsonArray.toString());

                        blogArrayList = jsonParserBlog.jsonProcessBlog(jsonArray);

                        for(int i = 0; i < blogArrayList.size(); i++){
                            System.out.println(blogArrayList.get(i).getDetailsBlog());
                        }
                        recyclerMain();

                        showProgress(false);
                        requestQueue.stop();
                        ;

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                requestQueue.stop();

            }
        });

        MySingletonhus.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    private void recyclerMain(){

        recyclerView = (RecyclerView) findViewById(R.id.rv_blog);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        //System.out.println(jsonParser.getDoctorContactsArrayList().get(0).getDoctorName());
        recyclerViewOfBlog = new RecyclerViewOfBlog(blogArrayList, getApplicationContext(), this);
        recyclerView.setAdapter(recyclerViewOfBlog);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        recyclerViewOfBlog.notifyDataSetChanged();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(BlogActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            recyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
            recyclerView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    recyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            pbBlog.setVisibility(show ? View.VISIBLE : View.GONE);
            pbBlog.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    pbBlog.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });

        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            pbBlog.setVisibility(show ? View.VISIBLE : View.GONE);
            recyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


}
