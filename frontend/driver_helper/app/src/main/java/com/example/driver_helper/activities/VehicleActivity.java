package com.example.driver_helper.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.driver_helper.R;
import com.example.driver_helper.pojo.Record;
import com.example.driver_helper.pojo.Vehicle;
import com.example.driver_helper.vehicle.adapters.RecordAdapter;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
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

    Dialog editDialog;
    View viewDialog;

    EditText etDialogName, etDialogModel, etDialogDate, etDialogMileage, etDialogBrand, etDialogType;
    Button btnDialogConfirm, btnDialogCancel;
    String urlPutVehicle = "http://192.168.1.111:8080/vehicle/";
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
        lstMaintenance = (List<Record>) intent.getSerializableExtra("MaintenanceList");
        lstRefueling = (List<Record>) intent.getSerializableExtra("RefuelingList");

        // Show Data in Screen
        ivLogo.setImageResource(getLogoSrc(vehicle));
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

    // dilog
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.edit){

            //custom dialog
            editDialog = new Dialog(VehicleActivity.this);
            viewDialog = getLayoutInflater().inflate(R.layout.dialog_edit_vehicle, null);

            etDialogName = viewDialog.findViewById(R.id.edit_etName);
            etDialogModel = viewDialog.findViewById(R.id.edit_etModel);
            etDialogDate = viewDialog.findViewById(R.id.edit_etDate);
            etDialogMileage = viewDialog.findViewById(R.id.edit_etMileage);
            etDialogBrand = viewDialog.findViewById(R.id.edit_etBrand);
            etDialogType = viewDialog.findViewById(R.id.edit_etType);

            btnDialogConfirm = viewDialog.findViewById(R.id.btnConfirm);
            btnDialogCancel = viewDialog.findViewById(R.id.btnCancel);

            etDialogName.setText(vehicle.getName());
            etDialogModel.setText(vehicle.getModel());
            etDialogDate.setText(vehicle.getMfd());
            etDialogMileage.setText(String.valueOf(vehicle.getMileage()));
            etDialogBrand.setText(vehicle.getBrand());
            etDialogType.setText(vehicle.getType());

            editDialog.setContentView(viewDialog);
            editDialog.show();

            btnDialogConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Vehicle updatedVehicle = new Vehicle(vehicle.getId(),etDialogName.getText().toString()
                            , etDialogDate.getText().toString(),Long.valueOf(etDialogMileage.getText().toString()),etDialogType.getText().toString(),
                            etDialogBrand.getText().toString(), etDialogModel.getText().toString());

                    vehicle = updatedVehicle;
                    // refresh VehicleActivity
                    ivLogo.setImageResource(getLogoSrc(updatedVehicle));
                    tvName.setText(String.valueOf(updatedVehicle.getName()));
                    tvModel.setText(String.valueOf(updatedVehicle.getModel()));
                    tvDate.setText(String.valueOf(updatedVehicle.getMfd()));
                    tvBrand.setText(String.valueOf(updatedVehicle.getBrand()));
                    tvMileage.setText(String.valueOf(updatedVehicle.getMileage()));
                    Log.w("ChiuVehicle", updatedVehicle.toString() );

                    // Put Request Thread Start
                    Log.w("ChiuThreadBug-1", "Testing" );
                    Thread threadVehicle = new Thread(new VehicleApiThread(urlPutVehicle, updatedVehicle));
                    Log.w("ChiuThreadBug0", "Testing" );
                    threadVehicle.start();

                    Log.w("ChiuThreadBug1", "Testing" );
                    try {
                        Log.w("ChiuThreadBug2", "Testin" );
                        threadVehicle.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.w("ChiuThreadBug3", "Testin" );
                    editDialog.dismiss();
                }
            });

            btnDialogCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editDialog.dismiss();
                }
            });

        }
        return super.onOptionsItemSelected(item);
    }

    public static int getLogoSrc(Vehicle vehicle){
        if ("Car".equals(vehicle.getType())) {
            return R.drawable.car;
        } else if ("Scooter".equals(vehicle.getType())){
            return R.drawable.scooter;
        }else if ("Motorcycle".equals(vehicle.getType())) {
            return R.drawable.motorcycle;
        }
        return Integer.parseInt(null);
    }
    public static int getExpense(List<Record> lst){
        int cost = 0;
        for (Record r:lst) {
            cost += r.getPrice();
        }
        return cost;
    }


    // Vehicle API Thread
    // call api to get data
    class VehicleApiThread implements Runnable{
        private String url;
        private Vehicle vehicle;

        public VehicleApiThread(String url, Vehicle vehicle) {
            this.url = url;
            this.vehicle = vehicle;
        }

        @Override
        public void run() {
            strResponse = putRequest(url, vehicle);
            Log.w("ChiuPutRequest", strResponse);
        }
    }
    private String putRequest(String strTxt, Vehicle vehicle){
        try {
            URL obj = new URL(strTxt+vehicle.getId());
            Log.w("ChiuURL", strTxt+vehicle.getId() );
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setDoOutput(true);
            Log.w("ChiuDoOutput", "ChiuDoOutput");
//            con.setDoInput(true);
//            Log.w("ChiuDoInput", "ChiuDoInput");

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

}