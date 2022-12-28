package com.example.driver_helper.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.driver_helper.R;
import com.example.driver_helper.main.adapters.VehicleAdapter;
import com.example.driver_helper.pojo.Record;
import com.example.driver_helper.pojo.Vehicle;
import com.example.driver_helper.vehicle.adapters.RecordAdapter;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
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

    Intent intent;

    Dialog editDialog, deleteDialog;
    View viewDialogEdit, viewDialogDelete;

    EditText etDialogName, etDialogModel, etDialogDate, etDialogMileage, etDialogBrand, etDialogType;
    Button btnDialogConfirm, btnDialogCancel;
    String urlVehicle = "http://192.168.1.111:8080/vehicle/";
    String strResponse;

    @SuppressLint({"WrongViewCast", "CutPasteId", "MissingInflatedId"})
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


        // get Data From vehicleAdapter Intent
        intent = this.getIntent();


        vehicle = (Vehicle) intent.getSerializableExtra("Vehicle");

        // Avoid Create a new Car first
        if(intent.getSerializableExtra("MaintenanceList") != null) {
            lstMaintenance = (List<Record>) intent.getSerializableExtra("MaintenanceList");
        }else{
            lstMaintenance = new ArrayList<>();
        }

        if(intent.getSerializableExtra("RefuelingList") != null) {
            lstRefueling = (List<Record>) intent.getSerializableExtra("RefuelingList");
        }else{
            lstRefueling = new ArrayList<>();
        }

        // Show Data in Screen
        ivLogo.setImageResource(getLogoSrc(VehicleActivity.this, vehicle));
        tvName.setText(String.valueOf(vehicle.getName()));
        tvModel.setText(String.valueOf(vehicle.getModel()));
        tvDate.setText(String.valueOf(vehicle.getMfd()));
        tvBrand.setText(String.valueOf(vehicle.getBrand()));
        tvMileage.setText(String.valueOf(vehicle.getMileage()));
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



    // flat option menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // dialog
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        // Edit Dialog
        if (item.getItemId() == R.id.edit){
            //custom dialog
            editDialog = new Dialog(VehicleActivity.this);
            viewDialogEdit = getLayoutInflater().inflate(R.layout.dialog_edit_vehicle, null);

            etDialogName = viewDialogEdit.findViewById(R.id.edit_etName);
            etDialogModel = viewDialogEdit.findViewById(R.id.edit_etModel);
            etDialogDate = viewDialogEdit.findViewById(R.id.edit_etDate);
            etDialogMileage = viewDialogEdit.findViewById(R.id.edit_etMileage);
            etDialogBrand = viewDialogEdit.findViewById(R.id.edit_etBrand);
            etDialogType = viewDialogEdit.findViewById(R.id.edit_etType);

            btnDialogConfirm = viewDialogEdit.findViewById(R.id.btnConfirm);
            btnDialogCancel = viewDialogEdit.findViewById(R.id.btnCancel);

            etDialogName.setText(vehicle.getName());
            etDialogModel.setText(vehicle.getModel());
            etDialogDate.setText(vehicle.getMfd());
            etDialogMileage.setText(String.valueOf(vehicle.getMileage()));
            etDialogBrand.setText(vehicle.getBrand());
            etDialogType.setText(vehicle.getType());

            editDialog.setContentView(viewDialogEdit);
            editDialog.show();

            // Edit Dialog Confirm Button
            btnDialogConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Vehicle updatedVehicle = new Vehicle(vehicle.getId(), etDialogName.getText().toString()
                                , etDialogDate.getText().toString(), Long.valueOf(etDialogMileage.getText().toString()), etDialogType.getText().toString(),
                                etDialogBrand.getText().toString(), etDialogModel.getText().toString());

                        Boolean isBadValidation = VehicleAdapter.validateVehicleFormat(VehicleActivity.this, updatedVehicle);
                        // refresh VehicleActivity
                        if(!isBadValidation){
                            vehicle = updatedVehicle;
                            ivLogo.setImageResource(getLogoSrc(VehicleActivity.this, updatedVehicle));
                            tvName.setText(String.valueOf(updatedVehicle.getName()));
                            tvModel.setText(String.valueOf(updatedVehicle.getModel()));
                            tvDate.setText(String.valueOf(updatedVehicle.getMfd()));
                            tvBrand.setText(String.valueOf(updatedVehicle.getBrand()));
                            tvMileage.setText(String.valueOf(updatedVehicle.getMileage()));


                        // Put Request Thread Start
                        Log.w("ChiuThreadBug-1", "Testing");
                        Thread updateThread = new Thread(new VehicleApiThread(urlVehicle, updatedVehicle, "update"));
                        Log.w("ChiuThreadBug0", "Testing");
                        updateThread.start();

                        Log.w("ChiuThreadBug1", "Testing");
                        try {
                            Log.w("ChiuThreadBug2", "Testin");
                            updateThread.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Log.w("ChiuThreadBug3", "Testin");


                            editDialog.dismiss();
                        }
                    }catch (Exception e) {
                        Log.w("chiuVehicleData", "新增車輛資訊未完全");
                        Toast.makeText(VehicleActivity.this, "請完整輸入車輛資訊", Toast.LENGTH_LONG).show();
                    }
                }
            });

            btnDialogCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editDialog.dismiss();
                }
            });


        // Delete Dialog
        }else if (item.getItemId() == R.id.delete){
            deleteDialog = new Dialog(VehicleActivity.this);
            viewDialogDelete = getLayoutInflater().inflate(R.layout.dialog_delete_vehielce, null);

            btnDialogConfirm = viewDialogDelete.findViewById(R.id.btnConfirm);
            btnDialogCancel = viewDialogDelete.findViewById(R.id.btnCancel);

            deleteDialog.setContentView(viewDialogDelete);
            deleteDialog.show();

            btnDialogConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Thread deleteThread = new Thread(new VehicleApiThread(urlVehicle, vehicle ,  "delete"));
                    Log.w("ChiuThreadBug0", "Testing" );
                    deleteThread.start();

                    try {
                        deleteThread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // redirect to MainActivity
                    intent = new Intent(VehicleActivity.this,MainActivity.class);
                    VehicleActivity.this.startActivity(intent);

                    deleteDialog.dismiss();
                }
            });

            btnDialogCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteDialog.dismiss();
                }
            });

        }
        return super.onOptionsItemSelected(item);
    }

    public static int getLogoSrc(Context context, Vehicle vehicle){
        if ("Car".equals(vehicle.getType())) {
            return R.drawable.car;
        } else if ("Scooter".equals(vehicle.getType())){
            return R.drawable.scooter;
        }else if ("Motorcycle".equals(vehicle.getType())) {
            return R.drawable.motorcycle;
        }else if ("add".equals(vehicle.getType())){
            return R.drawable.add;
        }
        Toast.makeText(context, "請盡速將「"+vehicle.getName()+
                "」車輛種類更改為下列車輛格式：1. Car 2. Motorcycle 3. Scooter",
                Toast.LENGTH_LONG).show();
        return R.drawable.warning;
    }
    public static int getExpense(List<Record> lst){
        int cost = 0;
        if(lst != null && lst.size()!=0) {
            for (Record r : lst) {
                cost += r.getPrice();
            }
        }
        return cost;
    }


    // Vehicle API Thread
    // call api to get data
    class VehicleApiThread implements Runnable{
        private String url;
        private Vehicle vehicle;
        private String request;

        public VehicleApiThread(String url, Vehicle vehicle, String request) {
            this.url = url;
            this.vehicle = vehicle;
            this.request = request;
        }

        @Override
        public void run() {
            if ("update".equals(request)) {
                strResponse = putRequest(url, vehicle);
            } else if ("delete".equals(request)) {
                strResponse = deleteRequest(url, vehicle);
            }
        }
    }

    private String putRequest(String strTxt, Vehicle vehicle){
        try {
            URL obj = new URL(strTxt+vehicle.getId());
            Log.w("ChiuURL", strTxt+vehicle.getId() );
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setDoOutput(true);
            Log.w("ChiuDoOutput", "ChiuDoOutput");

            con.setRequestMethod("PUT");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");

            Log.w("ChiuPut", "ChiuPut");
            OutputStreamWriter out = new OutputStreamWriter(
                    con.getOutputStream());
            Log.w("ChiubeforeGson", "beforeGson" );

            // Vehicle convert to Json String
            Gson gson = new Gson();
            Log.w("ChiubeforeToJson", "beforeToJson" );
            String json = gson.toJson(vehicle);
            Log.w("ChiuJson", json );
            byte [] byteJson = json.getBytes("utf-8");
            out.write(json);
            out.close();

            int responseCode = con.getResponseCode();
            Log.w("Chiu1", String.valueOf(responseCode));
            return String.valueOf(responseCode);
        } catch (MalformedURLException e) {
            Log.w("ChiuPutRequest1", e);
        } catch (IOException e) {
            Log.w("ChiuGetRequest2", e);
            return null;
        }
        return strTxt;
    }

    private String deleteRequest(String strTxt, Vehicle vehicle){
        try {
            URL obj = new URL(strTxt + vehicle.getId());
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod("DELETE");

            OutputStreamWriter out = new OutputStreamWriter(
                    con.getOutputStream());

            int responseCode = con.getResponseCode();

            return String.valueOf(responseCode);
        } catch (MalformedURLException e) {
            Log.w("ChiuDeleteRequest", e);
        } catch (IOException e) {
            Log.w("ChiuDeleteRequest", e);
            return null;
        }
        return strTxt;
    }

}