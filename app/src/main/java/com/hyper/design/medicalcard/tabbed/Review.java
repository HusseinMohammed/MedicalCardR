package com.hyper.design.medicalcard.tabbed;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.hyper.design.medicalcard.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hyper Design Hussien on 12/31/2017.
 */

public class Review extends Fragment implements AdapterView.OnItemClickListener {

    String[] accountNames, accountComments;
    List<RowItems> rowItems;
    ListView listView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        listView = (ListView) getActivity().findViewById(R.id.lv_reviews);
        rowItems = new ArrayList<RowItems>();

        accountNames = getResources().getStringArray(R.array.account_name);

        accountComments = getResources().getStringArray(R.array.comments);
        return inflater.inflate(R.layout.tab_reviews, container, false); //super.onCreateView(inflater, container, savedInstanceState)
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rowItems = new ArrayList<RowItems>();

        accountNames = getResources().getStringArray(R.array.account_name);

        accountComments = getResources().getStringArray(R.array.comments);

        for (int i = 0; i < accountNames.length; i++){
            //RowItems items = new RowItems(accountNames[i], accountComments[i]);
            //rowItems.add(items);
        }

        listView = (ListView) getActivity().findViewById(R.id.lv_reviews);
        CustomAdapter customAdapter = new CustomAdapter(this.getContext(), rowItems);
        listView.setAdapter(customAdapter);

        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}
