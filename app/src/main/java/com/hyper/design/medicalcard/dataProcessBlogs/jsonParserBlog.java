package com.hyper.design.medicalcard.dataProcessBlogs;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.hyper.design.medicalcard.Helper.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Hyper Design Hussien on 1/2/2018.
 */

public class jsonParserBlog {
    ArrayList<Blog> blogArrayList;
    Blog blog;
    SharedPreferences settings;
    public String localLanguage;
    private SessionManager session;
    public String sessionLanguage;

    public jsonParserBlog(Context context) {
        settings = context.getSharedPreferences("CommonPrefs", 0);
        localLanguage = settings.getString("locale","");
        session = new SessionManager(context);
        sessionLanguage = session.isLanguageIn();
        Log.i("locallllll", localLanguage);
    }

    public ArrayList<Blog> jsonProcessBlog(JSONObject jsonObject1){
        blogArrayList = new ArrayList<>();
        blog = new Blog();
        localLanguage = new String();

        /*settings = getSharedPreferences("CommonPrefs",Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        localLanguage = settings.getString("locale","");
        Log.i("locallllll", localLanguage);*/

        try {
            JSONArray jsonArray = jsonObject1.getJSONArray("blogCategory");

            for (int i = 0; i < jsonArray.length(); i++){

                JSONObject jsonObject = jsonArray.getJSONObject(i);

                if(sessionLanguage.equals("en")){
                    blog = new Blog(jsonObject.getInt(KeyBlog.idBlog), jsonObject.getString(KeyBlog.image), jsonObject.getString(KeyBlog.title_en),
                            jsonObject.getString(KeyBlog.nameBlog), jsonObject.getString(KeyBlog.date), jsonObject.getString(KeyBlog.descriptionEng));

                    blogArrayList.add(blog);
                } else {
                    blog = new Blog(jsonObject.getInt(KeyBlog.idBlog), jsonObject.getString(KeyBlog.image), jsonObject.getString(KeyBlog.title_ar),
                            jsonObject.getString(KeyBlog.nameBlog), jsonObject.getString(KeyBlog.date), jsonObject.getString(KeyBlog.descriptionArab));

                    blogArrayList.add(blog);
                }

                /*blog = new Blog(jsonObject.getInt(KeyBlog.idBlog), jsonObject.getString(KeyBlog.image), jsonObject.getString(KeyBlog.title_en),
                        jsonObject.getString(KeyBlog.nameBlog), jsonObject.getString(KeyBlog.date), jsonObject.getString(KeyBlog.descriptionEng));

                blogArrayList.add(blog);*/

            }

        } catch (JSONException e){
            e.printStackTrace();
        }
        return blogArrayList;
    }

    public ArrayList<Blog> getBlogArrayList(){
        return blogArrayList;
    }

}
