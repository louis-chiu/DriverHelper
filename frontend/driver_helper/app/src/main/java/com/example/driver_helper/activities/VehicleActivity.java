package com.example.driver_helper.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.driver_helper.R;
import com.example.driver_helper.pojo.MaintenanceRecord;
import com.example.driver_helper.pojo.Record;
import com.example.driver_helper.pojo.RefuelingRecord;
import com.example.driver_helper.pojo.Vehicle;
import com.example.driver_helper.vehicle.adapters.RecordAdapter;

import java.util.List;

public class VehicleActivity extends AppCompatActivity {
    ImageView ivLogo;
    TextView tvName, tvModel, tvDate, tvMileage, tvExpense, tvBrand;
    RecyclerView rvMaintenance, rvRefueling;

    LinearLayoutManager lmMaintenanceList, lmRefuelingList;
    RecordAdapter maintenanceAdapter, refuelingAdapter;

    Vehicle vehicle;
    List<Record> lstMaintenance;
    List<Record> lstRefueling;

    Bundle bundle, bundleMain;
    Intent intent, intentMain;



    @SuppressLint({"WrongViewCast", "CutPasteId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle);

        ivLogo = findViewById(R.id.vehicle_ivLogo);
        tvName = findViewById(R.id.vehicle_tvName);
        tvModel = findViewById(R.id.vehicle_tvModel);
        tvDate = findViewById(R.id.vehicle_tvDate);
        tvMileage = findViewById(R.id.vehicle_tvMileage);
        tvExpense = findViewById(R.id.vehicle_tvExpense);
        tvBrand = findViewById(R.id.vehicle_tvBrand);
        rvMaintenance = findViewById(R.id.vehicle_rvMaintenance);
        rvRefueling = findViewById(R.id.vehicle_rvRefueling);

        // get Data From Intent
        bundle = new Bundle();
        intent = new Intent(this, SettingActivity.class);
        intentMain = this.getIntent();

        vehicle = (Vehicle) intentMain.getSerializableExtra("Vehicle");
        lstMaintenance = (List<Record>) intentMain.getSerializableExtra("MaintenanceList");
        lstRefueling = (List<Record>) intentMain.getSerializableExtra("RefuelingList");

        // Show Data in Screen
        ivLogo.setImageResource(getLogoSrc(vehicle));
        tvName.setText(String.valueOf(vehicle.getName()));
        tvModel.setText(String.valueOf(vehicle.getModel()));
        tvDate.setText(String.valueOf(vehicle.getMfd()));
        tvBrand.setText(String.valueOf(vehicle.getBrand()));
        tvExpense.setText(String.valueOf(getExpense(lstMaintenance)+ getExpense(lstRefueling)));

        // Maintenance Record RecyclerView
        lmMaintenanceList = new LinearLayoutManager(this);
        maintenanceAdapter = new RecordAdapter(VehicleActivity.this, lstMaintenance);
        rvMaintenance.setLayoutManager(lmMaintenanceList);
        rvMaintenance.setAdapter(maintenanceAdapter);

        // Refueling Record RecyclerView
        lmRefuelingList = new LinearLayoutManager(this);
        refuelingAdapter = new RecordAdapter(VehicleActivity.this, lstRefueling);
        rvRefueling.setLayoutManager(lmRefuelingList);
        rvRefueling.setAdapter(refuelingAdapter);

    }

    private int getLogoSrc(Vehicle vehicle){
        if ("Car".equals(vehicle.getType())) {
            return R.drawable.car;
        } else if ("Scooter".equals(vehicle.getType())){
            return R.drawable.scooter;
        }else if ("Motorcycle".equals(vehicle.getType())) {
            return R.drawable.motorcycle;
        }
        return 0;
    }
    public static int getExpense(List<Record> lst){
        int cost = 0;
        for (Record r:lst) {
            cost += r.getPrice();
        }
        return cost;
    }

}