package com.example.driver_helper.main.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.driver_helper.R;
import com.example.driver_helper.activities.ExpenseStatementActivity;
import com.example.driver_helper.activities.VehicleActivity;
import com.example.driver_helper.pojo.MaintenanceRecord;
import com.example.driver_helper.pojo.Record;
import com.example.driver_helper.pojo.RefuelingRecord;
import com.example.driver_helper.pojo.Tool;
import com.example.driver_helper.pojo.Vehicle;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class ToolAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Tool> lstTool;
    private Context context;
    private Intent intent;
    List<Vehicle> lstVehicle;
    Map<Long,List<Record>> mapMaintenanceRecord;
    Map<Long,List<Record>> mapRefuelingRecord;

    public ToolAdapter(Context context, List<Tool> lstTool, List<Vehicle> lstVehicle, Map<Long, List<Record>> mapMaintenanceRecord, Map<Long, List<Record>> mapRefuelingRecord) {
        this.lstTool = lstTool;
        this.context = context;
        this.lstVehicle = lstVehicle;
        this.mapMaintenanceRecord = mapMaintenanceRecord;
        this.mapRefuelingRecord = mapRefuelingRecord;
    }



    public ToolAdapter(Context context, List<Tool> lstTool){
        this.context = context;
        this.lstTool = lstTool;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tool, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        Tool tool = lstTool.get(position);

        itemViewHolder.tv.setText(tool.getName());
        itemViewHolder.iv.setImageResource(tool.getLogo());

        if ("支出報表".equals(tool.getName())){
            itemViewHolder.ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    intent = new Intent(view.getContext(), ExpenseStatementActivity.class);
                    intent.putExtra("Maintenance", (Serializable) mapMaintenanceRecord);
                    intent.putExtra("Refueling", (Serializable) mapRefuelingRecord);
                    intent.putExtra("Vehicle", (Serializable) lstVehicle);
                    itemViewHolder.ll.getContext().startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return lstTool.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{
        ImageView iv;
        TextView tv;
        LinearLayout ll;
        public ItemViewHolder(View v){
            super(v);
            iv = v.findViewById(R.id.imageViewTool);
            tv = v.findViewById(R.id.textViewTool);
            ll= v.findViewById(R.id.LinearLayoutTool);
        }
    }
}

