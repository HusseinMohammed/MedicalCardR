package com.hyper.design.medicalcard.dataProcessBlogs;

/**
 * Created by Hyper Design Hussien on 1/2/2018.
 */

public class Blog {
    public int id;

    public String imageBlog;
    public String titleEng;
    public String titleArab;

    public String nameBlog;
    public String dateBlog;
    public String detailsBlog;

    public Blog(){

    }

    public Blog(int id, String imageBlog, String titleEng, String nameBlog, String dateBlog, String detailsBlog) {
        this.id = id;
        this.imageBlog = imageBlog;
        this.titleEng = titleEng;
        this.nameBlog = nameBlog;
        this.dateBlog = dateBlog;
        this.detailsBlog = detailsBlog;
    }

    public int getIdBlog() {
        return id;
    }

    public String getImageBlog() {
        return imageBlog;
    }

    public String getTitleEng() {
        return titleEng;
    }

    public String getTitleArab() { return titleArab; }

    public String getNameBlog() {
        return nameBlog;
    }

    public String getDateBlog() {
        return dateBlog;
    }

    public String getDetailsBlog() {
        return detailsBlog;
    }
}
