package com.hyper.design.medicalcard.Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.hyper.design.medicalcard.listviewAdapter.ContentReviews;
import com.hyper.design.medicalcard.listviewAdapter.ContentReviewsAdapter;
import com.hyper.design.medicalcard.R;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Hyper Design Hussien on 12/26/2017.
 */

public class TabReviews extends Fragment {

    ListView listView;

    RatingBar ratingBar;
    TextView textView;
    ProgressBar mProgressBar;
    ContentReviewsAdapter contentReviewsAdapter;

    private int mProgressStatus = 0;

    private Handler mHandler = new Handler();

    JSONArray jsonArray;
    JSONObject jsonObject;

    public TabReviews() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //populateListView();
        //Create List Item
        String[] foods = {"Meat","Popcorn","vegetables","fruits","potato","milk"};

        int[] d = {0,1};

        //Build Adapter
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this.getContext(), //Context for fragment
                R.layout.reviews_tab, //Layout to use
                foods); // Items to be displayed

        // Configure the list view
        listView = (ListView) getActivity().findViewById(R.id.lv_reviews);

        ratingBar = (RatingBar) getActivity().findViewById(R.id.rating_reviews);
        //ratingBar.setNumStars(5);

        textView = (TextView) getActivity().findViewById(R.id.tv_account_name_reviews);
        //textView.setText("Excellent Doctor");

        textView = (TextView) getActivity().findViewById(R.id.tv_account_comment_reviews);

        mProgressBar = (ProgressBar) getActivity().findViewById(R.id.pb_list_reviews);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(mProgressStatus < 100){
                    mProgressStatus++;
                    android.os.SystemClock.sleep(50);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mProgressBar.setProgress(mProgressStatus);
                        }
                    });
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }
        }).start();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.reviews_tab, container, false);

        //Create List Item
        String[] foods = {"Meat","Popcorn","vegetables","fruits","potato","milk"};

        int[] d = {0,1};

        //Build Adapter
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this.getContext(), //Context for fragment
                R.layout.reviews_tab, //Layout to use
                foods); // Items to be displayed

        // Configure the list view
        listView = (ListView) getActivity().findViewById(R.id.lv_reviews);

        //populateListView();

        /*ListAdapter listAdapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_list_item_1, foods);
        //ListView listView =(ListView) getActivity().findViewById(R.id.lv_reviews);
        listView.setAdapter(listAdapter);*/

        ratingBar = (RatingBar) getActivity().findViewById(R.id.rating_reviews);
        //ratingBar.setNumStars(5);

        textView = (TextView) getActivity().findViewById(R.id.tv_account_name_reviews);
        //textView.setText("Excellent Doctor");

        textView = (TextView) getActivity().findViewById(R.id.tv_account_comment_reviews);

        mProgressBar = (ProgressBar) getActivity().findViewById(R.id.pb_list_reviews);

        return rootView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /*String[] foods = {"Meat","Popcorn","vegetables","fruits","potato","milk"};
        ListAdapter listAdapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_list_item_1, foods);
        ListView listView =(ListView) getActivity().findViewById(R.id.lv_reviews);
        listView.setAdapter(listAdapter);*/

        populateListView();

        ratingBar = (RatingBar) getActivity().findViewById(R.id.rating);
        ratingBar.setNumStars(5);

        textView = (TextView) getActivity().findViewById(R.id.tv_account_name_reviews);
        //textView.setText("Doctor");
        //textView.setText("Excellent Doctor");

        textView = (TextView) getActivity().findViewById(R.id.tv_account_comment_reviews);

        mProgressBar = (ProgressBar) getActivity().findViewById(R.id.pb_list_reviews);
        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String food = String.valueOf(adapterView.getItemAtPosition(i));
                Toast.makeText(getContext(), food, Toast.LENGTH_LONG).show();
            }
        });*/
    }

    private void populateListView(){
        //Create List Item
        String[] foods = {"Meat","Popcorn","vegetables","fruits","potato","milk"};

        int[] d = {0,1};

        //Build Adapter
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this.getContext(), //Context for fragment
                R.layout.reviews_tab, //Layout to use
                foods); // Items to be displayed

        // Configure the list view
        listView = (ListView) getActivity().findViewById(R.id.lv_reviews);
        listView.setAdapter(arrayAdapter);

        contentReviewsAdapter = new ContentReviewsAdapter(this.getContext(), R.layout.row_items_reviews);
        listView.setAdapter(contentReviewsAdapter);

        String reviewName ="", reviewComment = "";
        int reviewRate = 0;

        int i = 0;
        while(i < 5){

            ContentReviews contentReviews = new ContentReviews(reviewName, reviewComment, reviewRate);
            contentReviewsAdapter.add(contentReviews);
            i++;
        }


    }

}
