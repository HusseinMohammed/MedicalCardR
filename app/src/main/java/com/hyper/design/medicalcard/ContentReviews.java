package com.hyper.design.medicalcard;

/**
 * Created by dell on 29/12/2017.
 */

public class ContentReviews {
    public String reviewName, reviewComment;
    int reviewRate;

    public ContentReviews(){

    }

    public ContentReviews(String reviewName, String reviewComment, int reviewRate) {
        this.reviewName = reviewName;
        this.reviewComment = reviewComment;
        this.reviewRate = reviewRate;
    }

    public void setReviewName(String reviewName) {
        this.reviewName = reviewName;
    }

    public void setReviewComment(String reviewComment) {
        this.reviewComment = reviewComment;
    }

    public void setReviewRate(int reviewRate) {
        this.reviewRate = reviewRate;
    }

    public String getReviewName() {
        return reviewName;
    }

    public String getReviewComment() {
        return reviewComment;
    }

    public int getReviewRate() {
        return reviewRate;
    }
}
