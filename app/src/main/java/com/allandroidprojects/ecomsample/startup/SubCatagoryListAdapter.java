package com.allandroidprojects.ecomsample.startup;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.allandroidprojects.ecomsample.R;
import com.allandroidprojects.ecomsample.options.CartListActivity;
import com.allandroidprojects.ecomsample.utility.Catagories;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

public    class SubCatagoryListAdapter extends   RecyclerView.Adapter<SubCatagoryListAdapter.ChildViewHolder>{
    public List<Catagories> list =  new ArrayList<>();

    public SubCatagoryListAdapter(List<Catagories> list) {
        this.list = list;

    }

    public static class ChildViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final SimpleDraweeView mImageView;



        public TextView product_name;
        // public  TextView product_des;
        public  TextView product_price;
        public RelativeLayout view_all;
        public LinearLayout con;


        public ChildViewHolder(View view) {
            super(view);
            mView = view;
            mImageView = (SimpleDraweeView) view.findViewById(R.id.image1);
            product_name = (TextView) view.findViewById(R.id.catagory_name);
            view_all = (RelativeLayout) view.findViewById(R.id.view_all);
            con = (LinearLayout) view.findViewById(R.id.con);


        }
    }

    @NonNull
    @Override
    public SubCatagoryListAdapter.ChildViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_catagory_item, parent, false);
        return new ChildViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChildViewHolder holder, int position) {

        final Catagories catagories = list.get(position);
        holder.product_name.setText(catagories.getName());
        final Uri uri = Uri.parse("https://static.pexels.com/photos/5949/food-nature-autumn-nuts-medium.jpg");
        holder.mImageView.setImageURI(uri);
        if (list.size() == 1){
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.bottomMargin = 25;
            holder.con.setLayoutParams(params);


        }
        holder.con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.gotoprodDetails(catagories.getId());
            }
        });
//        if (catagories.getImage()!=null){
//            final Uri uri = Uri.parse(catagories.getImage());
//            holder.mImageView.setImageURI(uri);
//        }else {
//            final Uri uri = Uri.parse("https://static.pexels.com/photos/5949/food-nature-autumn-nuts-medium.jpg");
//            holder.mImageView.setImageURI(uri);
//
//        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
