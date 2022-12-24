package com.example.driver_helper.main.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.driver_helper.R;
import com.example.driver_helper.activities.VehicleActivity;
import com.example.driver_helper.pojo.MaintenanceRecord;
import com.example.driver_helper.pojo.Record;
import com.example.driver_helper.pojo.RefuelingRecord;
import com.example.driver_helper.pojo.Vehicle;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class VehicleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    int row_index;
    private List<Vehicle> lstVehicle;
    private Map<Long,List<Record>> mapMaintenanceRecord;
    private Map<Long,List<Record>> mapRefuelingRecord;
    private Context context;


    private Intent intent;


    public VehicleAdapter(Context context, List<Vehicle> lstVehicle){
        this.context = context;
        this.lstVehicle = lstVehicle;
    }

    public VehicleAdapter(Context context, List<Vehicle> lstVehicle, Map<Long, List<Record>> mapMaintenanceRecord, Map<Long, List<Record>> mapRefuelingRecord) {
        this.lstVehicle = lstVehicle;
        this.mapMaintenanceRecord = mapMaintenanceRecord;
        this.mapRefuelingRecord = mapRefuelingRecord;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vehicle, parent, false);

        // add a 新增車輛 Object

        return new VehicleAdapter.ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        VehicleAdapter.ItemViewHolder itemViewHolder = (VehicleAdapter.ItemViewHolder) holder;
        Vehicle vehicle;

        // if position is the last element of the lstVehicle
        // vehicle will be create a new item
        if (position == lstVehicle.size()) {
            vehicle = new Vehicle("新增車輛", "add");
        }else{
             vehicle= lstVehicle.get(position);
        }

        Log.w("TestingSize", String.valueOf(lstVehicle.size()) );
        Log.w("TestingPosition", String.valueOf(position));


        List<Record> vehicleRefuelingRecords = mapRefuelingRecord.get(vehicle.getId());
        List<Record> vehicleMaintenanceRecords = mapMaintenanceRecord.get(vehicle.getId());

        itemViewHolder.tv.setText(vehicle.getName());
        if ("Car".equals(vehicle.getType())) {
            itemViewHolder.iv.setImageResource(R.drawable.car);
        } else if ("Scooter".equals(vehicle.getType())){
            itemViewHolder.iv.setImageResource(R.drawable.scooter);
        }else if ("Motorcycle".equals(vehicle.getType())){
            itemViewHolder.iv.setImageResource(R.drawable.motorcycle);
        }else if("add".equals(vehicle.getType())){
            itemViewHolder.iv.setImageResource(R.drawable.add);
        }

        if (position < lstVehicle.size()){
            // Click LinearLayout navigate to VehicleActivity
            itemViewHolder.ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    intent = new Intent(view.getContext(), VehicleActivity.class);
                    intent.putExtra("Vehicle",  vehicle);
                    intent.putExtra("RefuelingList", (Serializable) vehicleRefuelingRecords);
                    intent.putExtra("MaintenanceList", (Serializable) vehicleMaintenanceRecords);
                    itemViewHolder.ll.getContext().startActivity(intent);
                }
            });
        }else{
            // popup menu ?
            Log.w("ChiuAddCar", "Nothing");
        }


    }

    @Override
    public int getItemCount() {
        // ADD button will be the last item
        return lstVehicle.size()+1;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{
        ImageView iv;
        TextView tv;
        LinearLayout ll;
        public ItemViewHolder(View v){
            super(v);
            iv = v.findViewById(R.id.imageViewVehicle);
            tv = v.findViewById(R.id.textViewVehicle);
            ll = v.findViewById(R.id.LinearLayoutVehicle);

        }
    }


}
