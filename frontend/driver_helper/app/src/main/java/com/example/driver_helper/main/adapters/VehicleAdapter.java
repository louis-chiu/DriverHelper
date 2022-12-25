package com.example.driver_helper.main.adapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.driver_helper.R;
import com.example.driver_helper.activities.MainActivity;
import com.example.driver_helper.activities.VehicleActivity;
import com.example.driver_helper.pojo.MaintenanceRecord;
import com.example.driver_helper.pojo.Record;
import com.example.driver_helper.pojo.RefuelingRecord;
import com.example.driver_helper.pojo.Vehicle;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class VehicleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    int row_index;
    private List<Vehicle> lstVehicle;
    private Map<Long,List<Record>> mapMaintenanceRecord;
    private Map<Long,List<Record>> mapRefuelingRecord;
    private Context context;

    Dialog addDialog;
    View viewDialog;
    String urlPostVehicle = "http://192.168.1.111:8080/vehicle";
    String strResponse;

    private Intent intent;

    EditText etDialogName, etDialogModel, etDialogDate, etDialogMileage, etDialogBrand, etDialogType;
    Button btnDialogConfirm, btnDialogCancel;

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
        }else if (position == lstVehicle.size()){
            // popup menu ?
            itemViewHolder.ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addDialog = new Dialog(context);
                    viewDialog = LayoutInflater.from(context).inflate(R.layout.dialog_add_vehicle, null);

                    etDialogName = viewDialog.findViewById(R.id.add_etName);
                    etDialogModel = viewDialog.findViewById(R.id.add_etModel);
                    etDialogDate = viewDialog.findViewById(R.id.add_etDate);
                    etDialogMileage = viewDialog.findViewById(R.id.add_etMileage);
                    etDialogBrand = viewDialog.findViewById(R.id.add_etBrand);
                    etDialogType = viewDialog.findViewById(R.id.add_etType);

                    btnDialogConfirm = viewDialog.findViewById(R.id.btnConfirm);
                    btnDialogCancel = viewDialog.findViewById(R.id.btnCancel);

                    addDialog.setContentView(viewDialog);
                    addDialog.show();

                    btnDialogConfirm.setOnClickListener(new View.OnClickListener() {
                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        public void onClick(View view) {
                            Vehicle newVehicle = new Vehicle(lstVehicle.get(lstVehicle.size()-1).getId()+1,etDialogName.getText().toString()
                                    , etDialogDate.getText().toString(),Long.valueOf(etDialogMileage.getText().toString()),etDialogType.getText().toString(),
                                    etDialogBrand.getText().toString(), etDialogModel.getText().toString());


                            Log.w("ChiuVehicle", newVehicle.toString() );
                            // Put Request Thread Start
                            Log.w("ChiuThreadBug-1", "Testing" );
                            Thread threadVehicle = new Thread(new VehicleApiThread(urlPostVehicle, newVehicle));
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

                            lstVehicle.add(newVehicle);
                            notifyItemInserted(position);
                            notifyItemRangeChanged(position, lstVehicle.size());
                            notifyDataSetChanged();
                            addDialog.dismiss();
                        }
                    });

                    btnDialogCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            addDialog.dismiss();
                        }
                    });

                    Log.w("ChiuAddCar", "Nothing");
                }
            });


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
            strResponse = postRequest(url, vehicle);
            Log.w("ChiuPutRequest", strResponse);
        }
    }
    private String postRequest(String strTxt, Vehicle vehicle){
        try {
            URL obj = new URL(strTxt);
            Log.w("ChiuURL", strTxt);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setDoOutput(true);
            Log.w("ChiuDoOutput", "ChiuDoOutput");

            // set the Request Header
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");

            OutputStreamWriter out = new OutputStreamWriter(
                    con.getOutputStream());

            // Vehicle convert to Json String
            Gson gson = new Gson();
            String json = gson.toJson(vehicle);
            out.write(json);
            out.close();

            int responseCode = con.getResponseCode();
            Log.w("Chiu1", String.valueOf(responseCode));
            return String.valueOf(responseCode);
        } catch (MalformedURLException e) {
            Log.w("ChiuPostRequest1", e);
        } catch (IOException e) {
            Log.w("ChiuPostRequest2", e);
            return null;
        }
        return strTxt;
    }


}
