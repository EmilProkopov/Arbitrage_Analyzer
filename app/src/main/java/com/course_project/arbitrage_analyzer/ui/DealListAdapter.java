package com.course_project.arbitrage_analyzer.ui;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.course_project.arbitrage_analyzer.R;
import com.course_project.arbitrage_analyzer.model.DealListData;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

//Adapter for RecyclerView.
public class DealListAdapter extends RecyclerView.Adapter <DealListAdapter.DealViewHolder> {

    private DealListData data;

    public DealListAdapter(DealListData dldata) {

        this.data = dldata;
    }

    @NonNull
    @Override
    public DealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View tv = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        return new DealViewHolder(tv);
    }

    @Override
    public void onBindViewHolder(@NonNull DealViewHolder holder, int position) {

        DecimalFormat df = new DecimalFormat("0",
                DecimalFormatSymbols.getInstance(Locale.ENGLISH));
        df.setMaximumFractionDigits(9);

        String text = data.getTypes().get(position)+" "+df.format(data.getAmounts().get(position))
                +" at "+data.getNames().get(position)
                +"\nfor price of "+df.format(data.getPrices().get(position));

        holder.dealTextView.setText(text);
    }

    @Override
    public int getItemCount() {

        return data.getPrices().size();
    }

    public static class DealViewHolder extends RecyclerView.ViewHolder {

        public TextView dealTextView;

        public DealViewHolder(View itemView) {
            super(itemView);
            dealTextView = itemView.findViewById(R.id.temp);
        }
    }

}
