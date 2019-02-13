package com.hyper.design.medicalcard.tabbed;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hyper.design.medicalcard.Helper.SessionManager;
import com.hyper.design.medicalcard.R;

import java.util.List;

/**
 * Created by Hyper Design Hussien on 12/31/2017.
 */

public class CustomAdapter extends BaseAdapter {

    Context context;
    List<RowItems> rowItemsList;

    private SessionManager session;
    public String sessionLanguage;

    public CustomAdapter(Context context, List<RowItems> rowItemsList) {
        this.context = context;
        this.rowItemsList = rowItemsList;
        session = new SessionManager(context); //getApplicationContext()
    }

    @Override
    public int getCount() {
        return rowItemsList.size();
    }

    @Override
    public Object getItem(int i) {
        return rowItemsList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return rowItemsList.indexOf(getItem(i));
    }

    //Private view holder class
    private class ViewHolder {
        TextView tvName;
        TextView tvComment;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        // session manager
        sessionLanguage = session.isLanguageIn();

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if(convertView == null){
            convertView = mInflater.inflate(R.layout.list_items, null);
            holder = new ViewHolder();

            holder.tvName = (TextView) convertView.findViewById(R.id.tv_account_name);
            holder.tvComment = (TextView) convertView.findViewById(R.id.tv_comment);

            holder.tvName.setTextColor(Color.BLACK);
            holder.tvComment.setTextColor(Color.BLACK);

            /*if(session.isLanguageIn().equals("en")){

            } else {

            }*/


            RowItems rowItems = rowItemsList.get(i);

            holder.tvName.setText(rowItems.getReviewName());
            holder.tvComment.setText(rowItems.getReviewComment());

            /*for(int y = 0; y < rowItemsList.size(); y++){
                RowItems rowItems = rowItemsList.get(i);

                holder.tvName.setText(rowItems.getReviewName());
                holder.tvComment.setText(rowItems.getReviewComment());
            }*/

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }

}
