package com.hyper.design.medicalcard.AdaptersRecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.hyper.design.medicalcard.Activities.DoctorProfile;
import com.hyper.design.medicalcard.Activities.RecyclerViewOfDoctors;
import com.hyper.design.medicalcard.dataProcess.DoctorContacts;
import com.hyper.design.medicalcard.R;
import com.hyper.design.medicalcard.dataProcess.KeyDoctorContacts;
import com.google.gson.Gson;
import com.hyper.design.medicalcard.dataProcessDoctorProfile.KeyDoctorProfile;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.hyper.design.medicalcard.AppConstant.IMAGE_URL_VARIABLE;

/**
 * Created by dell on 17/12/2017.
 */

public class RecyclerViewOfDoctorsAdapter extends RecyclerView.Adapter<RecyclerViewOfDoctorsAdapter.MyViewHolder> {

    public ArrayList<DoctorContacts> doctorContactsArrayList;
    private Context context = null;
    public DoctorContacts doctorContacts = new DoctorContacts();
    RecyclerViewOfDoctors recyclerViewOfDoctors;
    private String imageUrlStatic = "uploads/item/resize200/"
            , imageUrlVariable = IMAGE_URL_VARIABLE;  //http://192.168.1.4/medicalcard/ //http:http://hyper-design.com/newmedicalcard/

    String strSpinCategory, strSpinSpecialist;
    public String dataStr = null;

    public RecyclerViewOfDoctorsAdapter(ArrayList<DoctorContacts> doctorContacts, Context context, RecyclerViewOfDoctors recyclerViewOfDoctors){
        this.doctorContactsArrayList = doctorContacts;
        this.context = context;
        this.recyclerViewOfDoctors = recyclerViewOfDoctors;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_items_doctor,
                parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        doctorContacts = doctorContactsArrayList.get(position);

        holder.cardViewDoctor.setTag(position);

        Log.d("ratingmm", doctorContacts.getDoctorRate() + " " + doctorContacts.getDoctorName());
        holder.ratingBar.setRating(doctorContacts.getDoctorRate());

        Log.i("tvDoctorName", doctorContacts.getDoctorName());
        holder.tvDoctorName.setText(doctorContacts.getDoctorName());
        Log.i("tvDoctorProfession", doctorContacts.getDoctorProfession());
        holder.tvDoctorProfession.setText(doctorContacts.getDoctorProfession());
        Log.i("tvDiscount", doctorContacts.getDiscountPercentage());
        holder.tvDiscount.setText(doctorContacts.getDiscountPercentage());
        Log.i("tvDoctorSpecialist", doctorContacts.getDoctorSpecialist());
        holder.tvDoctorSpecialist.setText(doctorContacts.getDoctorSpecialist());
        /*holder.tvFees.setText(*//*doctorContacts.getDiscountPercentage1()*//*100+"");*/
        Log.i("tvDoctorLocation", doctorContacts.getDoctorLocation());
        holder.tvDoctorLocation.setText(doctorContacts.getDoctorLocation());
        Log.i("tvDoctorPhone", doctorContacts.getDoctorPhone());
        holder.tvDoctorPhone.setText(doctorContacts.getDoctorPhone());
        Log.i("tvCategory", doctorContacts.getDoctorCategory());
        holder.tvCategory.setText(doctorContacts.getDoctorCategory());

        String url = doctorContacts.getDoctorStringImage();

        if(url.equals("")){
            holder.ivDoctor.setImageResource(R.drawable.doctor_image);
        } else {
            Picasso.with(context).load(imageUrlVariable + imageUrlStatic + doctorContacts.getDoctorStringImage()).into(holder.ivDoctor);//
        }
        /*holder.tvDoctorName.setText(doctorContacts.get(position).getDoctorName());
        holder.tvDoctorProfession.setText(doctorContacts.get(position).getDoctorProfession());
        holder.tvDiscount.setText(doctorContacts.get(position).getDiscountPercentage());
        holder.tvDoctorSpecialist.setText(doctorContacts.get(position).getDoctorSpecialist());
        holder.tvDoctorLocation.setText(doctorContacts.get(position).getDoctorLocation());
        holder.tvDoctorPhone.setText(doctorContacts.get(position).getDoctorPhone());*/
    }

    @Override
    public int getItemCount() {
        return doctorContactsArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView cardViewDoctor;
        ImageView ivDoctor;
        RatingBar ratingBar;
        TextView tvDoctorName, tvDoctorProfession, tvDiscount,
                 tvDoctorSpecialist, tvCategory, tvDoctorLocation, tvDoctorPhone;

        public MyViewHolder(View itemView) {
            super(itemView);
            cardViewDoctor = (CardView) itemView.findViewById(R.id.cv_doctors);
            ivDoctor = (ImageView) itemView.findViewById(R.id.iv_doctor_profile_image);
            tvDoctorName = (TextView) itemView.findViewById(R.id.tv_doctor_name);
            tvDoctorProfession = (TextView) itemView.findViewById(R.id.tv_doctor_job);
            tvDiscount = (TextView) itemView.findViewById(R.id.tv_discount);
            tvDoctorSpecialist = (TextView) itemView.findViewById(R.id.tv_specialist);
            /*tvFees = (TextView) itemView.findViewById(R.id.t);*/
            tvDoctorLocation = (TextView) itemView.findViewById(R.id.tv_location);
            tvDoctorPhone = (TextView) itemView.findViewById(R.id.tv_doctor_phone);
            tvCategory = (TextView) itemView.findViewById(R.id.tv_doctor_job);
            ratingBar = (RatingBar) itemView.findViewById(R.id.rating);


            cardViewDoctor.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position =(int) view.getTag();
            doctorContacts = doctorContactsArrayList.get(position);

            Bundle bundle1 = new Bundle();
            /*bundle.putString("edttext", "From Activity");*/
            // set Fragmentclass Arguments
            Bundle bundle = recyclerViewOfDoctors.getIntent().getExtras();

            strSpinCategory = bundle.getString("spinCategory");
            System.out.println(strSpinCategory);
            strSpinSpecialist = bundle.getString("spinSpecialist");
            dataStr = new Gson().toJson(doctorContactsArrayList);

            Intent intent = new Intent(recyclerViewOfDoctors, DoctorProfile.class);
            intent.putExtra("spinCategory", strSpinCategory);
            intent.putExtra("spinSpecialist", strSpinSpecialist);
            intent.putExtra("dataStr", dataStr);
            intent.putExtra(KeyDoctorContacts.idKey, doctorContacts.getId());
            intent.putExtra(KeyDoctorContacts.imageKey, doctorContacts.getDoctorStringImage());
            intent.putExtra(KeyDoctorContacts.nameEngKey, doctorContacts.getDoctorName());
            intent.putExtra(KeyDoctorContacts.nameEngKey, doctorContacts.getDoctorName());
            intent.putExtra(KeyDoctorContacts.profKey, doctorContacts.getDoctorProfession());
            intent.putExtra(KeyDoctorContacts.discKey, doctorContacts.getDiscountPercentage());
            intent.putExtra(KeyDoctorContacts.specKey, doctorContacts.getDoctorSpecialist());
            intent.putExtra(KeyDoctorContacts.locKeyRegion, doctorContacts.getDoctorLocation());
            intent.putExtra(KeyDoctorContacts.phoneKey, doctorContacts.getDoctorPhone());
            intent.putExtra(KeyDoctorContacts.categoryKey, doctorContacts.getDoctorCategory());
            intent.putExtra(KeyDoctorContacts.rateKey, doctorContacts.getDoctorRate());

            //intent.putExtra(KeyDoctorContacts.addressEngKey, doctorContacts.getDoctorAddress());


           /* bundle.putString(KeyDoctorContacts.imageKey, doctorContacts.getDoctorStringImage());
            bundle.putString(KeyDoctorContacts.nameAraKey, doctorContacts.getDoctorName());
            bundle.putString(KeyDoctorContacts.profKey, doctorContacts.getDoctorProfession());
            bundle.putString(KeyDoctorContacts.discKey, doctorContacts.getDiscountPercentage()+"");
            bundle.putString(KeyDoctorContacts.specKey, doctorContacts.getDoctorSpecialist());
            bundle.putString(KeyDoctorContacts.feesKey, 100 + "" *//*doctorContacts.getDiscountPercentage1()*//*);
            bundle.putString(KeyDoctorContacts.locKey, doctorContacts.getDoctorLocation());
//            intent.putExtra(KeyDoctorContacts.locKey1, doctorContacts.getDoctorLocation());
//            intent.putExtra(KeyDoctorContacts.locKey2, doctorContacts.getDoctorLocation());
            bundle.putString(KeyDoctorContacts.phoneKey, doctorContacts.getDoctorPhone());

            DoctorProfileFragment doctorProfileFragment = new DoctorProfileFragment();
            doctorProfileFragment.setArguments(bundle);*/

            recyclerViewOfDoctors.startActivity(intent);

        }
    }

}
