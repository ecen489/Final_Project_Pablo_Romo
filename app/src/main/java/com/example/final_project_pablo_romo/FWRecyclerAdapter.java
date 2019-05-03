package com.example.final_project_pablo_romo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FWRecyclerAdapter extends RecyclerView.Adapter<FWRecyclerAdapter.FWViewHolder> {

    private FoodWeek FW;
    private Context con;

    public FWRecyclerAdapter(Context c, FoodWeek f) {
        con = c;
        FW = f;
    }

    @Override
    public FWViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.foodweek_list_item,parent,false);
        FWViewHolder FWV = new FWViewHolder(view);
        return FWV;
    }

    @Override
    public void onBindViewHolder(FWViewHolder holder, int position) {
        // use holder to add individual things

    }

    @Override
    public int getItemCount() {
        return 7;
    }

    public static class FWViewHolder extends RecyclerView.ViewHolder {

        // Actual Data
        TextView day;
        TextView bname;
        TextView bcals;
        TextView bprotein;
        TextView bcarbs;
        TextView bfat;
        TextView lname;
        TextView lcals;
        TextView lprotein;
        TextView lcarbs;
        TextView lfat;
        TextView dname;
        TextView dcals;
        TextView dprotein;
        TextView dcarbs;
        TextView dfat;

        // Totals
        TextView totalcals;
        TextView totalprotein;
        TextView totalcarbs;
        TextView totalfat;

        RelativeLayout fwparentlayout;

        public FWViewHolder(View itemView) {
            super(itemView);

            // Actual Data
            day = itemView.findViewById(R.id.fwday);
            bname = itemView.findViewById(R.id.fwBname);
            bcals = itemView.findViewById(R.id.fwBcals);
            bprotein = itemView.findViewById(R.id.fwBprotein);
            bcarbs = itemView.findViewById(R.id.fwBcarbs);
            bfat = itemView.findViewById(R.id.fwBfat);
            lname = itemView.findViewById(R.id.fwLname);
            lcals = itemView.findViewById(R.id.fwLcals);
            lprotein = itemView.findViewById(R.id.fwLprotein);
            lcarbs = itemView.findViewById(R.id.fwLcarbs);
            lfat = itemView.findViewById(R.id.fwLfat);
            dname = itemView.findViewById(R.id.fwDname);
            dcals = itemView.findViewById(R.id.fwDcals);
            dprotein = itemView.findViewById(R.id.fwDprotein);
            dcarbs = itemView.findViewById(R.id.fwDcarbs);
            dfat = itemView.findViewById(R.id.fwDfat);

            // Totals
            totalcals = itemView.findViewById(R.id.tcals);

            fwparentlayout = itemView.findViewById(R.id.fw_parent_layout);
        }
    }
}
