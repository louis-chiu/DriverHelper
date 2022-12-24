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
import com.example.driver_helper.pojo.Vehicle;

import java.util.List;
import java.util.Objects;

public class VehicleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    int row_index;
    private List<Vehicle> lstVehicle;
    private Context context;

    public VehicleAdapter(Context context, List<Vehicle> lstVehicle){
        this.context = context;
        this.lstVehicle = lstVehicle;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vehicle, parent, false);
        return new VehicleAdapter.ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        VehicleAdapter.ItemViewHolder itemViewHolder = (VehicleAdapter.ItemViewHolder) holder;
        itemViewHolder.tv.setText(lstVehicle.get(position).getName());
        if ("Car".equals(lstVehicle.get(position).getType())) {
            itemViewHolder.iv.setImageResource(R.drawable.car);
        } else if ("Scooter".equals(lstVehicle.get(position).getType())){
            itemViewHolder.iv.setImageResource(R.drawable.scooter);
        }else if ("Motorcycle".equals(lstVehicle.get(position).getType())){
            itemViewHolder.iv.setImageResource(R.drawable.motorcycle);
        }else if("add".equals(lstVehicle.get(position).getType())){
            itemViewHolder.iv.setImageResource(R.drawable.add);
        }
    }

    @Override
    public int getItemCount() {
        return lstVehicle.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{
        ImageView iv;
        TextView tv;

        public ItemViewHolder(View v){
            super(v);
            iv = v.findViewById(R.id.imageViewVehicle);
            tv = v.findViewById(R.id.textViewVehicle);

        }
    }

}
