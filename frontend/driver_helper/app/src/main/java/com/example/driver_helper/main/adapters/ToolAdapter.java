package com.example.driver_helper.main.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.driver_helper.R;
import com.example.driver_helper.activities.ExpenseStatementActivity;
import com.example.driver_helper.activities.MainActivity;
import com.example.driver_helper.activities.VehicleActivity;
import com.example.driver_helper.pojo.Gas;
import com.example.driver_helper.pojo.MaintenanceRecord;
import com.example.driver_helper.pojo.Record;
import com.example.driver_helper.pojo.RefuelingRecord;
import com.example.driver_helper.pojo.Tool;
import com.example.driver_helper.pojo.Vehicle;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ToolAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Tool> lstTool;
    private Context context;
    private Intent intent;
    private List<Gas> lstGas;

    String strResponse;
    String urlPostMaintenance = "http://192.168.1.111:8080/maintenance/vehicle/";
    String urlPostRefueling = "http://192.168.1.111:8080/refueling/vehicle/";

    List<Vehicle> lstVehicle;
    Map<Long,List<Record>> mapMaintenanceRecord;
    Map<Long,List<Record>> mapRefuelingRecord;
    List<String> lstVehicleName;
    String [] arrGasType = {"92無鉛汽油", "95無鉛汽油", "98無鉛汽油", "超級柴油"};

    Dialog addRefuelingDialog, addMaintenanceDialog;
    View viewDialogRefueling, viewDialogMaintenance;
    Button btnDialogConfirm, btnDialogCancel;
    Spinner spCarName, spGasType;
    EditText etDate, etPrice, etMileage, etNotes, etItem;
    public ToolAdapter(Context context, List<Tool> lstTool, List<Vehicle> lstVehicle, Map<Long, List<Record>> mapMaintenanceRecord, Map<Long, List<Record>> mapRefuelingRecord, List<Gas> lstGas) {
        this.lstTool = lstTool;
        this.context = context;
        this.lstVehicle = lstVehicle;
        this.mapMaintenanceRecord = mapMaintenanceRecord;
        this.mapRefuelingRecord = mapRefuelingRecord;
        this.lstGas = lstGas;
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tool, parent, false);
        lstVehicleName= new ArrayList<>();
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        Tool tool = lstTool.get(position);

        // create Vehicle Name List
        if (lstVehicle.size() != 0){

            for (Vehicle vehicle:lstVehicle) {
                lstVehicleName.add(vehicle.getName());
            }
        }

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
        }else if ("新增加油紀錄".equals(tool.getName())){
            itemViewHolder.ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addRefuelingDialog = new Dialog(context);
                    viewDialogRefueling =  LayoutInflater.from(context).inflate(R.layout.dialog_add_refueling, null);

                    spCarName = viewDialogRefueling.findViewById(R.id.add_RR_spCarName);
                    etDate = viewDialogRefueling.findViewById(R.id.add_RR_etDate);
                    spGasType = viewDialogRefueling.findViewById(R.id.add_RR_spGastype);
                    etMileage = viewDialogRefueling.findViewById(R.id.add_RR_etMileage);
                    etPrice = viewDialogRefueling.findViewById(R.id.add_RR_etPrice);
                    etNotes = viewDialogRefueling.findViewById(R.id.add_RR_Notes);

                    btnDialogConfirm = viewDialogRefueling.findViewById(R.id.btnConfirm);
                    btnDialogCancel = viewDialogRefueling.findViewById(R.id.btnCancel);

                    ArrayAdapter<String> carNameArrayAdapter= new ArrayAdapter<>(context, R.layout.item_spinner, lstVehicleName);
                    ArrayAdapter<String> gasTypeArrayAdapter= new ArrayAdapter<>(context, R.layout.item_spinner, arrGasType);

                    spCarName.setAdapter(carNameArrayAdapter);
                    spGasType.setAdapter(gasTypeArrayAdapter);

                    addRefuelingDialog.setContentView(viewDialogRefueling);
                    addRefuelingDialog.show();

                    btnDialogConfirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Vehicle vehicle = null;
                            int sizeRecordList=0;
                            for (Vehicle v:lstVehicle) {
                                if (v.getName().equals(spCarName.getSelectedItem().toString()))
                                     vehicle = v;
                            }
                            for (Long i = 0L; i < mapRefuelingRecord.size() ; i++) {
                                if (mapRefuelingRecord.get(i) != null) {
                                    sizeRecordList += mapRefuelingRecord.get(i).size();
                                    Log.i("ChiuMapCheck", String.valueOf(mapRefuelingRecord.get(i)));
                                }
                            }
                            Record refuelingRecord  = new RefuelingRecord(
                                    Long.parseLong(String.valueOf(sizeRecordList+1)),
                                    etDate.getText().toString(), spGasType.getSelectedItem().toString(),
                                    calLiter(Long.parseLong(etPrice.getText().toString()),
                                            spGasType.getSelectedItem().toString(), lstGas),
                                    Long.parseLong(etPrice.getText().toString()),
                                    etNotes.getText().toString(), vehicle.getId()
                                    );

                            // !!!!!
                            // 缺 call api post 上資料庫

                            Thread threadRecord = new Thread(new RecordApiThread(urlPostRefueling, refuelingRecord));
                            threadRecord.start();
                            try {
                                threadRecord.join();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            addRefuelingDialog.dismiss();
                        }
                    });

                    btnDialogCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            addRefuelingDialog.dismiss();
                        }
                    });


                }
            });
        }else if ("新增保養紀錄".equals(tool.getName())){
            itemViewHolder.ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addMaintenanceDialog = new Dialog(context);
                    viewDialogMaintenance =  LayoutInflater.from(context).inflate(R.layout.dialog_add_maintenance, null);

                    spCarName = viewDialogMaintenance.findViewById(R.id.add_MR_spCarName);
                    etDate = viewDialogMaintenance.findViewById(R.id.add_MR_etDate);
                    etItem = viewDialogMaintenance.findViewById(R.id.add_MR_etItem);
                    etMileage = viewDialogMaintenance.findViewById(R.id.add_MR_etMileage);
                    etPrice = viewDialogMaintenance.findViewById(R.id.add_MR_etPrice);
                    etNotes = viewDialogMaintenance.findViewById(R.id.add_MR_Notes);

                    btnDialogConfirm = viewDialogMaintenance.findViewById(R.id.btnConfirm);
                    btnDialogCancel = viewDialogMaintenance.findViewById(R.id.btnCancel);

                    ArrayAdapter<String> carNameArrayAdapter= new ArrayAdapter<>(context, R.layout.item_spinner, lstVehicleName);

                    spCarName.setAdapter(carNameArrayAdapter);

                    addMaintenanceDialog.setContentView(viewDialogMaintenance);
                    addMaintenanceDialog.show();

                    btnDialogConfirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Vehicle vehicle = null;
                            int sizeRecordList = 0;
                            for (Vehicle v:lstVehicle) {
                                if (v.getName().equals(spCarName.getSelectedItem().toString()))
                                    vehicle = v;
                            }
                            for (int i = 0; i < mapMaintenanceRecord.size() ; i++) {
                                if (mapMaintenanceRecord.get(i) != null)
                                    sizeRecordList += mapMaintenanceRecord.get(i).size();
                            }
                            Log.w("ChiuMapSize", mapMaintenanceRecord.toString() );
                            Record maintenanceRecord = new MaintenanceRecord(
                                    Long.parseLong(String.valueOf(sizeRecordList+1)),
                                    etDate.getText().toString(), etItem.getText().toString(),
                                    Long.parseLong(etPrice.getText().toString()),
                                    etNotes.getText().toString(), vehicle.getId()
                            );

                            // !!!!!
                            // 缺 call api post 上資料庫
                            Thread threadRecord = new Thread(new RecordApiThread(urlPostMaintenance, maintenanceRecord));
                            threadRecord.start();
                            try {
                                threadRecord.join();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            addMaintenanceDialog.dismiss();

                        }
                    });

                    btnDialogCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            addMaintenanceDialog.dismiss();
                        }
                    });
                }
            });
        }else if ("設定".equals(tool.getName())){

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

    public static Double calLiter(Long refuelPrice, String gasName, List<Gas> lstGas){
        for (Gas gas:lstGas) {
            if (gas.getType().equals(gasName))
                return refuelPrice / Double.parseDouble(gas.getPrice());
        }
        return 0d;
    }

    class RecordApiThread implements Runnable{
        private String url;
        private Record record;

        public RecordApiThread(String url, Record record) {
            this.url = url;
            this.record = record;
        }

        @Override
        public void run() {
            strResponse = postRequest(url, record);
            Log.w("ChiuPutRequest", strResponse);
        }
    }

    private String postRequest(String strTxt, Record record){
        try {
            URL obj = new URL(strTxt + record.getVid());
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
            Log.w("chiuGson", gson.toString());
            String json = gson.toJson(record);
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

