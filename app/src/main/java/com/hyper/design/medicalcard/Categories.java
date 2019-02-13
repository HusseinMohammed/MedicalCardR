package com.hyper.design.medicalcard;

/**
 * Created by dell on 15/12/2017.
 */

public class Categories {
    private int id;
    private String categoryName;

    public Categories(){}

    public Categories(int id, String name){
        this.id = id;
        this.categoryName = name;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setName(String name){
        this.categoryName = name;
    }

    public int getId(){
        return this.id;
    }

    public String getName(){
        return this.categoryName;
    }
}
