package com.example.driver_helper.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.driver_helper.R;
import com.example.driver_helper.pojo.MaintenanceRecord;
import com.example.driver_helper.pojo.RefuelingRecord;
import com.example.driver_helper.pojo.Vehicle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class DataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    int row_index;
    private List<Vehicle> lstVehicle;
    Map<Long,List<MaintenanceRecord>> mapMaintenanceRecord;
    Map<Long,List<RefuelingRecord>> mapRefuelingRecord;
    private Context context;

    public DataAdapter(Context context, List<Vehicle> lstVehicle, Map<Long, List<MaintenanceRecord>> mapMaintenanceRecord, Map<Long, List<RefuelingRecord>> mapRefuelingRecord) {
        this.lstVehicle = lstVehicle;
        this.mapMaintenanceRecord = mapMaintenanceRecord;
        this.mapRefuelingRecord = mapRefuelingRecord;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_page, parent, false);
        return new DataAdapter.PageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DataAdapter.PageViewHolder pageViewHolder = (DataAdapter.PageViewHolder) holder;
        Vehicle vehicle = lstVehicle.get(position);
        List<MaintenanceRecord> lstMaintenance = mapMaintenanceRecord.get(vehicle.getId());

        pageViewHolder.tvCarName.setText(vehicle.getName());
        pageViewHolder.tvName1.setText("總支出");
        pageViewHolder.tvName2.setText("總里程");
        pageViewHolder.tvName3.setText("上筆支出");
        pageViewHolder.tvNumber2.setText(String.valueOf(vehicle.getMileage()));

        if(lstMaintenance != null && lstMaintenance.size() != 0) {
            pageViewHolder.tvNumber1.setText(String.valueOf(calTotalCost(lstMaintenance, mapRefuelingRecord.get(vehicle.getId()))));
            pageViewHolder.tvNumber3.setText(String.valueOf(lstMaintenance.get(lstMaintenance.size() - 1).getPrice()));
        }else{
            pageViewHolder.tvNumber1.setText("0");
            pageViewHolder.tvNumber3.setText("0");
        }
    }

    @Override
    public int getItemCount() {
        return lstVehicle.size();
    }

    public class PageViewHolder extends RecyclerView.ViewHolder{
        TextView tvNumber1, tvNumber2, tvNumber3;
        TextView tvName1, tvName2, tvName3, tvCarName;

        public PageViewHolder(View v){
            super(v);
            tvCarName = v.findViewById(R.id.textViewCarName);
            tvNumber1 = v.findViewById(R.id.textViewNumber1);
            tvNumber2 = v.findViewById(R.id.textViewNumber2);
            tvNumber3 = v.findViewById(R.id.textViewNumber3);
            tvName1 = v.findViewById(R.id.textViewName1);
            tvName2 = v.findViewById(R.id.textViewName2);
            tvName3 = v.findViewById(R.id.textViewName3);


        }
    }

    private int calTotalCost(List<MaintenanceRecord> lstMaintenanceRecord, List<RefuelingRecord> lstRefuelingRecord){
        int cost = 0;
        for (MaintenanceRecord m:lstMaintenanceRecord) {
            cost += m.getPrice();
        }
        for (RefuelingRecord r:lstRefuelingRecord) {
            cost += r.getPrice();
        }
        return cost ;
    }
}
