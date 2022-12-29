package com.example.driver_helper.expense.statement.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.driver_helper.R;
import com.example.driver_helper.activities.ExpenseStatementActivity;
import com.example.driver_helper.main.adapters.DataAdapter;
import com.example.driver_helper.pojo.MaintenanceRecord;
import com.example.driver_helper.pojo.Record;
import com.example.driver_helper.pojo.RefuelingRecord;
import com.example.driver_helper.pojo.Vehicle;
import com.example.driver_helper.vehicle.adapters.RecordAdapter;

import java.util.List;
import java.util.Map;

public class VehiclesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    private Map<String,List<Record>> mapRecord;
    private List<Vehicle> lstVehicle;
    private Context context;

    public VehiclesAdapter(Context context, Map<String,List<Record>> mapRecord,List<Vehicle> lstVehicle ){
        this.context = context;
        this.mapRecord = mapRecord;
        this.lstVehicle = lstVehicle;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.page_expense_statement, parent, false);
        return new VehiclesAdapter.PageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        VehiclesAdapter.PageViewHolder pageViewHolder = (VehiclesAdapter.PageViewHolder) holder;

        Vehicle vehicle = lstVehicle.get(position);
        pageViewHolder.rvRecord.setAdapter(new RecordAdapter(context, mapRecord.get(vehicle.getName()), vehicle, RecordAdapter.TYPE_STATEMENT));
        pageViewHolder.rvRecord.setRecycledViewPool(viewPool);

    }

    @Override
    public int getItemCount() {
        return mapRecord.size();
    }

    public class PageViewHolder extends RecyclerView.ViewHolder{
        RecyclerView rvRecord;

        public PageViewHolder(View v){
            super(v);
            rvRecord = v.findViewById(R.id.expenseStatement_rvRecord);
        }
    }
}
