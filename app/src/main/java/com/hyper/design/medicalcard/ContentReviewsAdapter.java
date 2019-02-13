package com.hyper.design.medicalcard;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 29/12/2017.
 */

public class ContentReviewsAdapter extends ArrayAdapter {
    List list = new ArrayList();

    public ContentReviewsAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    public void add(ContentReviews object) {
        super.add(object);
        list.add(object);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View viewRow;
        viewRow = convertView;
        ContactHolder contactHolder;

        if(viewRow == null){
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            viewRow = layoutInflater.inflate(R.layout.row_items_reviews, parent, false);

            contactHolder = new ContactHolder();
            contactHolder.tvReviewName =(TextView) viewRow.findViewById(R.id.tv_account_name_reviews);
            contactHolder.tvReviewComment =(TextView) viewRow.findViewById(R.id.tv_account_comment_reviews);
            contactHolder.rbReviewRate =(RatingBar) viewRow.findViewById(R.id.rating);

            viewRow.setTag(contactHolder);
        }
        else {
            contactHolder = (ContactHolder) viewRow.getTag();
        }

        ContentReviews contentReviews = (ContentReviews) this.getItem(position);
        contactHolder.tvReviewName.setText(contentReviews.getReviewName());
        contactHolder.tvReviewComment.setText(contentReviews.getReviewComment());
        contactHolder.rbReviewRate.setNumStars(contentReviews.getReviewRate());

        return viewRow;
    }

    static class ContactHolder
    {
        TextView tvReviewName, tvReviewComment;
        RatingBar rbReviewRate;
    }
}
