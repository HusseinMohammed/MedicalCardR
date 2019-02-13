package com.hyper.design.medicalcard.tabbed;

/**
 * Created by Hyper Design Hussien on 12/31/2017.
 */

public class RowItems {
    public String reviewId;
    public String reviewName;
    public String reviewComment;

    public RowItems() {
    }

    public RowItems(String reviewId, String reviewName, String reviewComment) {
        this.reviewId = reviewId;
        this.reviewName = reviewName;
        this.reviewComment = reviewComment;
    }

    public String getReviewId() {
        return reviewId;
    }

    public String getReviewName() {
        return reviewName;
    }

    public String getReviewComment() {
        return reviewComment;
    }

    public void setReviewName(String reviewName) {
        this.reviewName = reviewName;
    }

    public void setReviewComment(String reviewComment) {
        this.reviewComment = reviewComment;
    }

}
