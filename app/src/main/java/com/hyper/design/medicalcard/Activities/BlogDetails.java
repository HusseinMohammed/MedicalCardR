package com.hyper.design.medicalcard.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyper.design.medicalcard.R;
import com.hyper.design.medicalcard.dataProcessBlogs.KeyBlog;
import com.squareup.picasso.Picasso;

import static com.hyper.design.medicalcard.AppConstant.IMAGE_URL_VARIABLE;

public class BlogDetails extends AppCompatActivity {

    public static int id;
    ImageView ivBlogDetails;
    TextView tvTitleBlogDetails, tvNameBlogDetails, tvDateBlogDetails, tvDetailsBlogDetails;
    ScrollView scrollView;

    private String imageUrlStatic = "uploads/blogitem/source/"
            , imageUrlVariable = IMAGE_URL_VARIABLE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_detials);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /*ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);*/

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        ivBlogDetails = (ImageView) findViewById(R.id.iv_blog_item);
        tvTitleBlogDetails = (TextView) findViewById(R.id.tv_title_blog);
        tvNameBlogDetails = (TextView) findViewById(R.id.tv_blog_written_by);
        tvDateBlogDetails = (TextView) findViewById(R.id.tv_blog_date);
        tvDetailsBlogDetails = (TextView) findViewById(R.id.tv_blog_details);
        scrollView = (ScrollView) findViewById(R.id.sv_blog_details);
        Intent intent = getIntent();

        id = intent.getIntExtra(KeyBlog.idBlog, 0);
        System.out.println(id);

        String url = intent.getStringExtra(KeyBlog.image);
        Log.i("url = ", url);
        //Toast.makeText(this, url, Toast.LENGTH_LONG).show();
        if(url.equals("")){
            ivBlogDetails.setImageResource(R.drawable.blogging);
        } else {
            Picasso.with(getApplicationContext()).load(imageUrlVariable + imageUrlStatic + url).into(ivBlogDetails);
        }

        String title = intent.getStringExtra(KeyBlog.title_en);
        tvTitleBlogDetails.setText(title);
        String name = intent.getStringExtra(KeyBlog.nameBlog);
        tvNameBlogDetails.setText(name);
        String date = intent.getStringExtra(KeyBlog.date);
        tvDateBlogDetails.setText(date);
        String details = intent.getStringExtra(KeyBlog.descriptionEng);
        tvDetailsBlogDetails.setText(Html.fromHtml(details));

    }

    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == android.R.id.home)
        {
            Intent intent = new Intent(BlogDetails.this, BlogActivity.class);
            startActivity(intent);
            finish();
        }
        /*Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);*/
        return super.onOptionsItemSelected(item);//true
    }

}
