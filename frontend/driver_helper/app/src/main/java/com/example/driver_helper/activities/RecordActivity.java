package com.example.driver_helper.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.driver_helper.R;
import com.example.driver_helper.main.adapters.VehicleAdapter;
import com.example.driver_helper.pojo.MaintenanceRecord;
import com.example.driver_helper.pojo.Record;
import com.example.driver_helper.pojo.RefuelingRecord;
import com.example.driver_helper.pojo.Vehicle;
import com.example.driver_helper.vehicle.adapters.RecordAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RecordActivity extends AppCompatActivity {

    TextView tvTotal, tvCarName, tvRecord;

    Vehicle vehicle;
    Record record;
    String recordType;

    Intent intent;
    List<Record> lstRecord, lstBackup;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    public static RecordAdapter adapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        recyclerView = findViewById(R.id.record_recyclerView);

        intent = getIntent();
        vehicle = (Vehicle) intent.getSerializableExtra("Vehicle");

        // Avoid Create a new Car
        if(intent.getSerializableExtra("RecordList") != null) {
            lstRecord = (List<Record>) intent.getSerializableExtra("RecordList");
        }else{
            lstRecord = new ArrayList<>();

        // Backup
        }if(intent.getSerializableExtra("Backup") != null) {
            lstBackup = (List<Record>) intent.getSerializableExtra("Backup");
        }else{
            lstBackup = new ArrayList<>();
        }

        recordType = intent.getStringExtra("RecordType");

        tvRecord = findViewById(R.id.record_tvRecord);

//        Drawable carNameDrawable = ContextCompat.getDrawable(this, VehicleActivity.getLogoSrc(this, vehicle));
//        carNameDrawable.setBounds(0, 0, 120, 120);
//        tvCarName.setCompoundDrawables(carNameDrawable, null, null, null);

        int recordSrc = 0;

        if ("Refueling".equals(recordType)){
            recordSrc = R.drawable.gas_pump;
            tvRecord.setText("加油紀錄");
        }else if ("Maintenance".equals(recordType)){
            recordSrc = R.drawable.wrench;
            tvRecord.setText("保養紀錄");
        }else{
            recordSrc = R.drawable.warning;
        }

        Drawable RecordDrawable = ContextCompat.getDrawable(this, recordSrc);
        RecordDrawable.setBounds(0, 0, 120, 120);
        tvRecord.setCompoundDrawables(RecordDrawable, null, null, null);

        layoutManager = new LinearLayoutManager(this);
        adapter = new RecordAdapter(this, lstRecord, vehicle, RecordAdapter.TYPE_COMPLEX);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void onPause() {
        super.onPause();

        intent = new Intent(this, VehicleActivity.class);
        if("Refueling".equals(recordType)){
            intent.putExtra("Vehicle",  vehicle);
            intent.putExtra("RefuelingList", (Serializable) lstRecord);
            intent.putExtra("MaintenanceList", (Serializable) lstBackup);
        }else if("Maintenance".equals(recordType)){
            intent.putExtra("Vehicle",  vehicle);
            intent.putExtra("MaintenanceList", (Serializable) lstRecord);
            intent.putExtra("RefuelingList", (Serializable) lstBackup);
        }
        startActivity(intent);
    }
}