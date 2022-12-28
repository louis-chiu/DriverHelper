package com.example.driver_helper.main.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.driver_helper.R;
import com.example.driver_helper.activities.MainActivity;
import com.example.driver_helper.activities.VehicleActivity;
import com.example.driver_helper.pojo.MaintenanceRecord;
import com.example.driver_helper.pojo.Record;
import com.example.driver_helper.pojo.RefuelingRecord;
import com.example.driver_helper.pojo.Vehicle;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class DataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    int row_index;
    private List<Vehicle> lstVehicle;
    public static Map<Long,List<Record>> mapMaintenanceRecord;
    public static Map<Long,List<Record>> mapRefuelingRecord;
    private Context context;

    public DataAdapter(Context context, List<Vehicle> lstVehicle, Map<Long, List<Record>> mapMaintenanceRecord, Map<Long, List<Record>> mapRefuelingRecord) {
        this.lstVehicle = lstVehicle;
        DataAdapter.mapMaintenanceRecord = mapMaintenanceRecord;
        DataAdapter.mapRefuelingRecord = mapRefuelingRecord;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.page_vhicle_data, parent, false);
        return new DataAdapter.PageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DataAdapter.PageViewHolder pageViewHolder = (DataAdapter.PageViewHolder) holder;
        Vehicle vehicle = lstVehicle.get(position);
        List<Record> lstMaintenance = mapMaintenanceRecord.get(vehicle.getId());
        List<Record> lstRefueling = mapRefuelingRecord.get(vehicle.getId());
        pageViewHolder.tvCarName.setText("  "+ vehicle.getName());

        if (lstVehicle.isEmpty()||lstVehicle.size()==0) {
            TextView tvEmpty = ((Activity)context).findViewById(R.id.tvVehicleEmpty);
            tvEmpty.setVisibility(View.VISIBLE);
            ViewPager2 vp = ((Activity)context).findViewById(R.id.ViewPager2DataList);
            vp.setVisibility(View.GONE);
        }else{
            TextView tvEmpty = ((Activity)context).findViewById(R.id.tvVehicleEmpty);
            tvEmpty.setVisibility(View.GONE);
            ViewPager2 vp = ((Activity)context).findViewById(R.id.ViewPager2DataList);
            vp.setVisibility(View.VISIBLE);
        }
        //設定圖片及大小
        Drawable leftDrawable = ContextCompat.getDrawable(context, VehicleActivity.getLogoSrc(context ,vehicle));
        leftDrawable.setBounds(0, 0, 90, 90);
        pageViewHolder.tvCarName.setCompoundDrawables(leftDrawable, null, null, null);

        pageViewHolder.tvName1.setText("總支出");
        pageViewHolder.tvName2.setText("總里程");
        pageViewHolder.tvName3.setText("已陪伴您");
        pageViewHolder.tvNumber1.setText(String.valueOf(VehicleActivity.getExpense(lstMaintenance) + VehicleActivity.getExpense(lstRefueling)));
        pageViewHolder.tvNumber2.setText(String.valueOf(vehicle.getMileage()));

        // 計算日期
        int daysBetween = 0;
        try {
             daysBetween = daysBetween(new SimpleDateFormat("yyyy-MM-dd").parse(vehicle.getMfd()),
                    new Date());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        pageViewHolder.tvNumber3.setText(String.valueOf(daysBetween));
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
    public static int daysBetween(Date date1, Date date2){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date1);
        long time1 = cal.getTimeInMillis();
        cal.setTime(date2);
        long time2 = cal.getTimeInMillis();
        long between_days=(time2-time1)/(1000*3600*24);
        return Integer.parseInt(String.valueOf(between_days));
    }


}
