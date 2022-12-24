package com.example.driver_helper.vehicle.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.driver_helper.R;

import com.example.driver_helper.activities.VehicleActivity;
import com.example.driver_helper.pojo.Record;
import com.example.driver_helper.pojo.Vehicle;

import java.sql.Statement;
import java.util.List;

public class RecordAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<Record>lstRecord;
    private String vehicleName;
    private Context context;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private boolean isStatement = false;


    public RecordAdapter(Context context, List<Record> lstRecord){
        this.context = context;
        this.lstRecord = lstRecord;
    }

    public RecordAdapter(Context context, List<Record> lstRecord,String vehicleName, boolean isStatement) {
        this.lstRecord = lstRecord;
        this.context = context;
        this.vehicleName = vehicleName;
        this.isStatement = isStatement;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            // Here Inflating your recyclerview item layout
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expense, parent, false);
            return new ItemViewHolder(itemView);
        } else if (viewType == TYPE_HEADER && !isStatement) {
            // Here Inflating your header view
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_total_expense, parent, false);
            return new HeaderViewHolder(itemView);
        }else if (isStatement) {
            // Here Inflating your header view
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_total_expense_statement, parent, false);
            return new StatementHeaderViewHolder(itemView);
        }
        else return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder){
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            itemViewHolder.tvDate.setText(lstRecord.get(position-1).getDate());
            itemViewHolder.tvItem.setText(lstRecord.get(position-1).getItem());
            itemViewHolder.tvPrice.setText("$"+lstRecord.get(position - 1).getPrice());
        }else if (holder instanceof HeaderViewHolder){
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            headerViewHolder.tvTotalExpense.setText(VehicleActivity.getExpense(lstRecord)+ " 元");
        }else if (holder instanceof StatementHeaderViewHolder){
            StatementHeaderViewHolder headerViewHolder = (StatementHeaderViewHolder) holder;

            headerViewHolder.tvHeader.setText(vehicleName);
            headerViewHolder.tvTotalExpense.setText(VehicleActivity.getExpense(lstRecord)+ " 元");

        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }
    @Override
    public int getItemCount() {
        return lstRecord.size()+1;
    }

    private class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView tvTotalExpense;

        public HeaderViewHolder(View v) {
            super(v);
            tvTotalExpense =  v.findViewById(R.id.headerExpense_tvTotalExpense);
        }
    }

    private class StatementHeaderViewHolder extends RecyclerView.ViewHolder {
        TextView tvTotalExpense, tvHeader;

        public StatementHeaderViewHolder(View v) {
            super(v);
            tvHeader = v.findViewById(R.id.headerExpenseStatement_tvHeader);
            tvTotalExpense =  v.findViewById(R.id.headerExpenseStatement_tvTotalExpense);
        }
    }
    public class ItemViewHolder extends RecyclerView.ViewHolder{
        TextView tvPrice, tvDate, tvItem, tvNotes;

        public ItemViewHolder(View v){
            super(v);
            tvDate = v.findViewById(R.id.itemExpense_tvDate);
            tvItem = v.findViewById(R.id.itemExpense_tvItem);
            tvPrice = v.findViewById(R.id.itemExpense_tvPrice);

        }
    }

}
