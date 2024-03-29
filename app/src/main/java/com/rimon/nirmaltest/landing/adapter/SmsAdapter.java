package com.rimon.nirmaltest.landing.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rimon.nirmaltest.R;
import com.rimon.nirmaltest.appconfig.Constances;
import com.rimon.nirmaltest.landing.model.Sms;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class SmsAdapter extends RecyclerView.Adapter<SmsAdapter.ViewHolder> {

    List<Sms> smsArrayList = new ArrayList<>();
    List<Sms> deletedItems = new ArrayList<>();

    public SmsAdapter(List<Sms> smsArrayList) {
        this.smsArrayList = smsArrayList;
    }

    @NonNull
    @Override
    public SmsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View v = layoutInflater.inflate(R.layout.content_sms,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SmsAdapter.ViewHolder holder, int position) {
        Sms sms = smsArrayList.get(position);
        holder.tvBody.setText(sms.getMsg());
        holder.tvHeading.setText(sms.getAddress()+" : ");
       /* if(Constances.isValidPhoneNumber(sms.getAddress())){
            System.out.println("Yes this is a valid no ");
            holder.llSmsView.setVisibility(View.GONE);
        }else {
            if(sms.getMsg().contains("OTP")){
                holder.llSmsView.setVisibility(View.GONE);
                //hideItem(position);
            }else {
                holder.llSmsView.setVisibility(View.VISIBLE);
                holder.tvBody.setText(sms.getMsg());
                holder.tvHeading.setText(sms.getAddress()+" : ");
            }

        }*/


    }

    public void hideItem(final int position) {
        deletedItems.add(position, smsArrayList.get(position));
        smsArrayList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return smsArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvHeading;
        TextView tvBody;
        LinearLayout llSmsView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            llSmsView = itemView.findViewById(R.id.llSmsView);
            tvHeading = itemView.findViewById(R.id.tvHeading);
            tvBody = itemView.findViewById(R.id.tvBody);
        }
    }
}
