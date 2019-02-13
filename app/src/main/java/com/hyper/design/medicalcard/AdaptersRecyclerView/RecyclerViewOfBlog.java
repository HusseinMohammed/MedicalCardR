package com.hyper.design.medicalcard.AdaptersRecyclerView;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyper.design.medicalcard.Activities.BlogActivity;
import com.hyper.design.medicalcard.Activities.BlogDetails;
import com.hyper.design.medicalcard.R;
import com.hyper.design.medicalcard.dataProcessBlogs.Blog;
import com.hyper.design.medicalcard.dataProcessBlogs.KeyBlog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import static com.hyper.design.medicalcard.AppConstant.IMAGE_URL_VARIABLE;

/**
 * Created by Hyper Design Hussien on 1/2/2018.
 */

public class RecyclerViewOfBlog extends RecyclerView.Adapter<RecyclerViewOfBlog.MyViewHolder> {

    public ArrayList<Blog> blogArrayList;
    private Context context = null;
    public Blog blog = new Blog();
    public RecyclerViewOfBlog recyclerViewOfBlog;
    BlogActivity blogActivity;
    private String imageUrlStatic = "uploads/blogitem/source/"
            , imageUrlVariable = IMAGE_URL_VARIABLE;

    RecyclerViewOfBlog(){

    }

    public RecyclerViewOfBlog(ArrayList<Blog> blogArrayList, Context context, BlogActivity blogActivity){
        this.blogArrayList = blogArrayList;
        this.context = context;
        this.blogActivity = blogActivity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_items_blog,
                parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        blog = blogArrayList.get(position);

        holder.cardViewBlog.setTag(position);

        holder.tvBlogTitle.setText(blog.getTitleEng());
        holder.tvBlogWrittenBy.setText(blog.getNameBlog());
        holder.tvBlogDate.setText(blog.getDateBlog());
        holder.tvBlogDetails.setText(Html.fromHtml(blog.getDetailsBlog()));

        String url = blog.getImageBlog();
        Log.i("url = ", url);
        //Toast.makeText(context, url, Toast.LENGTH_LONG).show();
        if(url.equals("")){
            holder.ivBlog.setImageResource(R.drawable.blogging);
        } else {
            Picasso.with(context).load(imageUrlVariable + imageUrlStatic + blog.getImageBlog()).into(holder.ivBlog);//
        }
    }

    public int getItemCount() {
        return blogArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView cardViewBlog;
        ImageView ivBlog;
        TextView tvBlogTitle, tvBlogWrittenBy, tvBlogDate, tvBlogDetails;

        public MyViewHolder(View itemView) {
            super(itemView);

            cardViewBlog = (CardView) itemView.findViewById(R.id.cv_blog);
            ivBlog = (ImageView) itemView.findViewById(R.id.iv_blog_item);
            tvBlogTitle = (TextView) itemView.findViewById(R.id.tv_title_blog);
            tvBlogWrittenBy = (TextView) itemView.findViewById(R.id.tv_blog_written_by);
            tvBlogDate = (TextView) itemView.findViewById(R.id.tv_blog_date);
            tvBlogDetails = (TextView) itemView.findViewById(R.id.tv_blog_details);
            cardViewBlog.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position =(int) view.getTag();
            blog = blogArrayList.get(position);

            Intent intent = new Intent(blogActivity, BlogDetails.class);

            intent.putExtra(KeyBlog.idBlog, blog.getIdBlog());
            intent.putExtra(KeyBlog.image, blog.getImageBlog());
            intent.putExtra(KeyBlog.title_en, blog.getTitleEng());
            intent.putExtra(KeyBlog.nameBlog, blog.getNameBlog());
            intent.putExtra(KeyBlog.date, blog.getDateBlog());
            intent.putExtra(KeyBlog.descriptionEng, blog.getDetailsBlog());

            blogActivity.startActivity(intent);

        }
    }


}
