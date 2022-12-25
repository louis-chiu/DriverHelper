package com.example.driver_helper.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.driver_helper.R;
import com.example.driver_helper.expense.statement.adapters.VehiclesAdapter;
import com.example.driver_helper.main.adapters.DataAdapter;
import com.example.driver_helper.pojo.MaintenanceRecord;
import com.example.driver_helper.pojo.Record;
import com.example.driver_helper.pojo.RefuelingRecord;
import com.example.driver_helper.pojo.Vehicle;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpenseStatementActivity extends AppCompatActivity {
    private PieChart pieChart;

    List<Vehicle> lstVehicle;
    Map<Long,List<Record>> mapMaintenanceRecord;
    Map<Long,List<Record>> mapRefuelingRecord;
    Map<String, List<Record>> mapExpense = new HashMap<>();
    List<Record> lstRecord, lstMaintenance, lstRefueling;

    ViewPager2 vpRecord;
    VehiclesAdapter vehiclesAdapter;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_statement);


        // get Data from Intent
        intent = this.getIntent();
        lstVehicle = (List<Vehicle>) intent.getSerializableExtra("Vehicle");
        mapMaintenanceRecord = (Map<Long, List<Record>>) intent.getSerializableExtra("Maintenance");
        mapRefuelingRecord = (Map<Long, List<Record>>) intent.getSerializableExtra("Refueling");


        pieChart = findViewById(R.id.expenseStatement_pieChart);
        ArrayList<PieEntry> entries = new ArrayList<>();

        for (Vehicle vehicle:lstVehicle) {
            int expense = 0;
            lstRecord = new ArrayList<>();

            lstRefueling  = mapRefuelingRecord.get(vehicle.getId());
            lstMaintenance = mapMaintenanceRecord.get(vehicle.getId());

            expense = VehicleActivity.getExpense(lstRefueling) +
                    VehicleActivity.getExpense(lstMaintenance);

            // add data to lstRecord
            lstRecord.addAll(lstRefueling);
            lstRecord.addAll(lstMaintenance);

            // put data to mapExpense
            mapExpense.put(vehicle.getName(), lstRecord);

            // load Pie Chart Data
            entries.add(new PieEntry(expense, vehicle.getName()));
        }
        loadPieCharData(entries);
        setupPieChart();

        vpRecord = findViewById(R.id.expenseStatement_vpRecord);
        vehiclesAdapter = new VehiclesAdapter(ExpenseStatementActivity.this, mapExpense , lstVehicle);
        vpRecord.setAdapter(vehiclesAdapter);


    }



    private void  loadPieCharData(ArrayList<PieEntry> entries){

        // prepare Pie Chart Color
        ArrayList<Integer> colors = new ArrayList<>();
        for (int color : ColorTemplate.MATERIAL_COLORS){
            colors.add(color);
        }
        for (int color : ColorTemplate.VORDIPLOM_COLORS){
            colors.add(color);
        }
        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(colors);


        // set Pie Chart Style
        PieData data = new PieData(dataSet);
        data.setDrawValues(true);
        data.setValueFormatter(new PercentFormatter(pieChart));
        data.setValueTextSize(12f);
        data.setValueTextSize(Color.BLACK);

        // update Pie Chart
        pieChart.setData(data);
        pieChart.invalidate();

        // animate
        pieChart.animateY(2000, Easing.EaseInOutQuad);
    }

    private void setupPieChart(){
        pieChart.setDrawHoleEnabled(true);
        pieChart.setUsePercentValues(false);
        pieChart.setEntryLabelTextSize(12f);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setCenterText("總支出");
        pieChart.setCenterTextSize(20f);
        pieChart.getDescription().setEnabled(false);

        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(true);
    }

}