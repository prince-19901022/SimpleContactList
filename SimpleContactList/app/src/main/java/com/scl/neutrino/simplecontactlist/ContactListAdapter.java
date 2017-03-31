package com.scl.neutrino.simplecontactlist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Nutrino on 3/31/2017.
 */

public class ContactListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ArrayList<Person> dataSrc;
    private Context context;
    private FooterListener fl;

    private static final int FOOTER_VIEW = 1;

    public ContactListAdapter(ArrayList<Person> dataSrc, Context context) {
        this.dataSrc = dataSrc;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView;
        MyViewHolder mHolder;
        FooterVH fHolder;
        if(viewType == FOOTER_VIEW){

            itemView = inflater.inflate(R.layout.footer_layout , parent , false);
            fHolder = new FooterVH(itemView);
            return fHolder;
        }

        itemView = inflater.inflate(R.layout.contact_list_row, parent , false);
        mHolder = new MyViewHolder(itemView);

        return mHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof MyViewHolder){
            MyViewHolder mHolder = (MyViewHolder) holder;
            mHolder.nameView.setText(dataSrc.get(position).getPersonName());
            mHolder.contactView.setText(dataSrc.get(position).getContact());
        }
    }

    @Override
    public int getItemCount() {

        return dataSrc.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {

        if(position == dataSrc.size()){

            return FOOTER_VIEW;
        }

        return super.getItemViewType(position);
    }

    private class FooterVH extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView footerText;

        public FooterVH(View itemView) {
            super(itemView);
            footerText = (TextView) itemView.findViewById(R.id.footerText);

            itemView.setOnClickListener(this);
            fl = (FooterListener) context;
        }

        @Override
        public void onClick(View v) {

            if(! fl.OnLoadMore()){

                footerText.setText("NO MORE RESULTS TO SHOW");
            }
        }
    }

    private class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView nameView;
        private TextView contactView;


        public MyViewHolder(View itemView) {
            super(itemView);

            nameView = (TextView) itemView.findViewById(R.id.personName);
            contactView = (TextView) itemView.findViewById(R.id.contact);
        }
    }
}
