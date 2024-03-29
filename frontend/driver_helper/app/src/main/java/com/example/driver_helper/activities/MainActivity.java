package com.example.driver_helper.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.driver_helper.R;
import com.example.driver_helper.main.adapters.DataAdapter;
import com.example.driver_helper.main.adapters.ToolAdapter;
import com.example.driver_helper.main.adapters.VehicleAdapter;
import com.example.driver_helper.pojo.Gas;
import com.example.driver_helper.pojo.MaintenanceRecord;
import com.example.driver_helper.pojo.Record;
import com.example.driver_helper.pojo.RefuelingRecord;
import com.example.driver_helper.pojo.Tool;
import com.example.driver_helper.pojo.Vehicle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    Handler xmlHandler = new Handler(new GasXmlParser());
    Handler vehicleJsonHandler = new Handler(new VehicleJsonParser());
    Handler refuelingJsonHandler = new Handler(new RefuelingJsonParser());
    Handler maintenanceJsonHandler = new Handler(new MaintenanceJsonParser());

    String urlGetVehicle = "http://192.168.1.8:8080/vehicle/all";
    String urlGetRefueling = "http://192.168.1.8:8080/refueling/all";
    String urlGetMaintenance = "http://192.168.1.8:8080/maintenance/all";
    String urlGas = "https://vipmbr.cpc.com.tw/CPCSTN/ListPriceWebService.asmx/getCPCMainProdListPrice_XML";
    XmlPullParser pullParser = Xml.newPullParser();

    List<Gas> lstGas;
    Gas gas;
    List<Vehicle> lstVehicle;
    Map<Long,List<Record>> mapMaintenanceRecord;
    Map<Long,List<Record>> mapRefuelingRecord;
    public static int vehicleNumber;
    List<Tool> lstTool;
    List<Vehicle> lstVehicleForRV;

    LinearLayoutManager lmToolList;
    LinearLayoutManager lmVehicleList;
    LinearLayoutManager lmDataList;
    RecyclerView rvToolList;
    RecyclerView rvVehicleList;
    ViewPager2 vpDataList;
    public static DataAdapter dataAdapter;
    ToolAdapter toolAdapter;
    public static VehicleAdapter vehicleAdapter;

    String s1="";
    String s2="";
    String s3="";
    String jsonStr;
    String strResponse;

    public static Double gas_98, gas_95, gas_92, gas_diesel;


    Button btn ;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // move to onStart();
        // because we need to make sure UI will refresh
        // when we update the data in the database
    }

    @Override
    protected void onStart() {
        super.onStart();
        lstGas = new ArrayList<>();
        lstVehicle = new ArrayList<>();
        mapMaintenanceRecord = new HashMap<>();
        mapRefuelingRecord = new HashMap<>();
        lstTool = new ArrayList<>();
        lstVehicleForRV = new ArrayList<>();

        Thread threadGas = new Thread(new GasApiThread(urlGas));
        threadGas.start();

        // get all Vehicle data
        Thread threadVehicle = new Thread(new VehicleApiThread(urlGetVehicle));
        threadVehicle.start();

        try {
            // after threadVehicle finished
            // threadRefueling and threadMaintenance start
            threadGas.join();
            threadVehicle.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // get all Maintenance Records
        Thread threadMaintenance = new Thread(new MaintenanceApiThread(urlGetMaintenance));
        threadMaintenance.start();

        try {
            threadMaintenance.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // get all Refueling Records
        Thread threadRefueling = new Thread(new RefuelingApiThread(urlGetRefueling));
        threadRefueling.start();

        // ToolList Data initialize
        toolListData();
    }

    // Gas API Thread
    // call api to get data
    class GasApiThread implements Runnable{
        private String url;

        public GasApiThread(String url) {
            this.url = url;
        }

        @Override
        public void run() {
            String strResponse = getRequest(url);
            xmlHandler.obtainMessage(1, strResponse)
                    .sendToTarget();
        }
    }

    // Vehicle API Thread
    // call api to get data
    class VehicleApiThread implements Runnable{
        private String url;

        public VehicleApiThread(String url) {
            this.url = url;
        }

        @Override
        public void run() {
            strResponse = getRequest(url);
            vehicleJsonHandler.obtainMessage(1, strResponse)
                    .sendToTarget();

        }
    }

    // Maintenance API Thread
    // call api to get data
    class MaintenanceApiThread implements Runnable{
        private String url;

        public MaintenanceApiThread(String url) {
            this.url = url;
        }

        @Override
        public void run() {
            strResponse = getRequest(url);
            maintenanceJsonHandler.obtainMessage(1, strResponse)
                    .sendToTarget();

        }
    }

    // Refueling API Thread
    // call api to get data
    class RefuelingApiThread implements Runnable{
        private String url;

        public RefuelingApiThread(String url) {
            this.url = url;
        }

        @Override
        public void run() {
            strResponse = getRequest(url);
            refuelingJsonHandler.obtainMessage(1, strResponse)
                    .sendToTarget();

        }
    }



    // Xml Parser Handler
    // parsing xml data
    class GasXmlParser implements Handler.Callback{
        @Override
        public boolean handleMessage(@NonNull Message message) {
            String xmlStr = message.obj.toString();

//            TextView tv = findViewById(R.id.textView);
//            tv.setText(xmlStr);
            try {
                pullParser.setInput(new StringReader(xmlStr));
                int eventType = pullParser.getEventType();

                while (eventType != XmlPullParser.END_DOCUMENT){
                    switch (eventType) {
                        case XmlPullParser.START_DOCUMENT:
                            break;
                        case XmlPullParser.START_TAG:
                            if ("Table".equals(pullParser.getName())){
                                gas = new Gas();
                            }else if ("產品名稱".equals(pullParser.getName())){
                                eventType = pullParser.next();
                                gas.setType(pullParser.getText());
                            }else if ("參考牌價".equals(pullParser.getName())){
                                eventType = pullParser.next();
                                gas.setPrice(pullParser.getText());
                            }
                            break;
                        case XmlPullParser.END_TAG:
                            if ("Table".equals(pullParser.getName())){
                                lstGas.add(gas);
                                gas = null;
                            }
                            break;
                    }
                    eventType = pullParser.next();
                }
            } catch (XmlPullParserException | IOException e) {
                Log.w("ChiuXmlParser", e);
            }
            realtimeGasPrice();
            return true;
        }
    }

    // Vehicle Json Parser
    // parsing all Vehicle Data
    class VehicleJsonParser implements Handler.Callback{
        JSONObject jsonObject;

        @Override
        public boolean handleMessage(@NonNull Message message) {
            jsonStr = message.obj.toString();

            try {
                JSONArray jsonArray = new JSONArray(jsonStr);
                vehicleNumber = jsonArray.length();

                for (int i = 0; i < vehicleNumber; i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    Vehicle vehicle = new Vehicle(
                            jsonObject.getLong("id"),
                            jsonObject.getString("name"),
                            jsonObject.getString("mfd"),
                            jsonObject.getLong("mileage"),
                            jsonObject.getString("type"),
                            jsonObject.getString("brand"),
                            jsonObject.getString("model")
                    );

                    lstVehicle.add(vehicle);
                }
            } catch (JSONException e) {
                Log.w("ChiuVehicleJsonParser", e);
            }



            Log.w("ChiuVehicleJsonParserTest", String.valueOf(lstVehicle.size()));
            return true;
        }
    }


    // Maintenance Json Parser
    // parsing all Maintenance Data
    class MaintenanceJsonParser implements Handler.Callback{
        JSONObject jsonObject;

        @Override
        public boolean handleMessage(@NonNull Message message) {
            jsonStr = message.obj.toString();
            try {
                JSONArray jsonArray = new JSONArray(jsonStr);
                int maintenanceNumber = jsonArray.length();
                for (int i = 0; i < vehicleNumber; i++) {
                    mapMaintenanceRecord.put(lstVehicle.get(i).getId(), new ArrayList<>());
                }
                for (int i = 0; i < maintenanceNumber; i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    MaintenanceRecord maintenanceRecord = new MaintenanceRecord(
                            jsonObject.getLong("id"),
                            jsonObject.getString("date"),
                            jsonObject.getString("item"),
                            jsonObject.getLong("price"),
                            jsonObject.getString("notes"),
                            jsonObject.getJSONObject("vehicle").getLong("id")
                    );
                    mapMaintenanceRecord.get(maintenanceRecord.getVid()).add(maintenanceRecord);
                }
            } catch (JSONException e) {
                Log.w("ChiuMaintenanceJsonParser", e);
            }

//            for ( List<MaintenanceRecord> lst:
//                    mapMaintenanceRecord.values()) {
//                for (MaintenanceRecord r:
//                        lst) {
//                    s2 += r.toString()+"\n";
//                }
//                s2+="!!!!!!!!!!!!!!\n";
//            }
//            TextView tv2 = findViewById(R.id.textView2);
//            tv2.setText(s2);


            return true;
        }
    }

    /// Refueling Json Parser
    // parsing all Refueling Data
    class RefuelingJsonParser implements Handler.Callback{
        JSONObject jsonObject;

        @Override
        public boolean handleMessage(@NonNull Message message) {
            jsonStr = message.obj.toString();
            try {
                JSONArray jsonArray = new JSONArray(jsonStr);
                int refuelingNumber = jsonArray.length();

                for (int i = 0; i < vehicleNumber; i++) {
                    mapRefuelingRecord.put(lstVehicle.get(i).getId(), new ArrayList<>());
                }
                for (int i = 0; i < refuelingNumber; i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    RefuelingRecord refuelingRecord = new RefuelingRecord(
                            jsonObject.getLong("id"),
                            jsonObject.getString("date"),
                            jsonObject.getString("item"),
                            jsonObject.getDouble("liter"),
                            jsonObject.getLong("price"),
                            jsonObject.getString("notes"),
                            jsonObject.getJSONObject("vehicle").getLong("id")
                    );
                    mapRefuelingRecord.get(refuelingRecord.getVid()).add(refuelingRecord);
                }
            } catch (JSONException e) {
                Log.w("ChiuRefuelingJsonParser", e);
            }

            Log.w("chiuTesting1", String.valueOf(mapMaintenanceRecord.get(0L)));
            Log.w("chiuTesting1", String.valueOf(mapMaintenanceRecord.get(1L)));
            Log.w("chiuTesting1", String.valueOf(mapMaintenanceRecord.get(2L)));
            Log.w("chiuTesting1", String.valueOf(mapMaintenanceRecord.get(3L)));
            Log.w("chiuTesting1", String.valueOf(mapMaintenanceRecord.get(4L)));
            Log.w("chiuTesting1", String.valueOf(mapMaintenanceRecord.get(5L)));
            Log.w("chiuTesting2", String.valueOf(mapRefuelingRecord.toString()));
            Log.w("chiuTesting3", String.valueOf(lstVehicle.size()));

            // load data to Adapter
            toolListRecyclerView();
            vehicleListRecyclerView();
            dataListRecyclerView();

            return true;
        }
    }


    // getRequest
    // send Get request to Internet
    // convert data to String type
    private String getRequest(String strTxt){
        StringBuffer response;
        try {
            URL obj = new URL(strTxt);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setDoInput(true);
            con.setRequestMethod("GET");

            int responseCode = con.getResponseCode();
            Log.w("Chiu1", String.valueOf(responseCode));
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream())
            );
            response = new StringBuffer();
            String inputLine;
            while ((inputLine=in.readLine()) != null){
                response.append(inputLine);
            }

            in.close();

            return response.toString();
        } catch (MalformedURLException e) {
            Log.w("ChiuGetRequest", e);
        } catch (IOException e) {
            Log.w("ChiuGetRequest2", e);
            return null;
        }
        return strTxt;
    }



    private void realtimeGasPrice(){
        TextView tvGasName1 = findViewById(R.id.tvGasName1);
        TextView tvGasName2 = findViewById(R.id.tvGasName2);
        TextView tvGasName3 = findViewById(R.id.tvGasName3);
        TextView tvGasName4 = findViewById(R.id.tvGasName4);
        TextView tvGasPrice1 = findViewById(R.id.tvGasPrice1);
        TextView tvGasPrice2 = findViewById(R.id.tvGasPrice2);
        TextView tvGasPrice3 = findViewById(R.id.tvGasPrice3);
        TextView tvGasPrice4 = findViewById(R.id.tvGasPrice4);
        for (Gas gas:lstGas) {
            if ("92無鉛汽油".equals(gas.getType())){
                tvGasName1.setText("92無鉛");
                tvGasPrice1.setText(gas.getPrice());
                gas_92 = Double.valueOf(gas.getPrice());
            }else if ("95無鉛汽油".equals(gas.getType())){
                tvGasName2.setText("95無鉛");
                tvGasPrice2.setText(gas.getPrice());
                gas_95 = Double.valueOf(gas.getPrice());
            }else if ("98無鉛汽油".equals(gas.getType())){
                tvGasName3.setText("98無鉛");
                tvGasPrice3.setText(gas.getPrice());
                gas_98 = Double.valueOf(gas.getPrice());
            }else if ("超級柴油".equals(gas.getType())){
                tvGasName4.setText("超級柴油");
                tvGasPrice4.setText(gas.getPrice());
                gas_diesel = Double.valueOf(gas.getPrice());
            }
        }
    }

    private void toolListData(){
        Tool temp = new Tool("支出報表",R.drawable.report);
        lstTool.add(temp);
        temp = new Tool("新增加油紀錄",R.drawable.gas_pump);
        lstTool.add(temp);
        temp = new Tool("新增保養紀錄",R.drawable.wrench);
        lstTool.add(temp);
        temp = new Tool("設定",R.drawable.setting);
        lstTool.add(temp);
    }

    private void vehicleListRecyclerView(){
        rvVehicleList = findViewById(R.id.RecyclerViewVehicleList);
        lmVehicleList = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        vehicleAdapter = new VehicleAdapter(MainActivity.this, lstVehicle, mapMaintenanceRecord, mapRefuelingRecord);
        rvVehicleList.setLayoutManager(lmVehicleList);
        rvVehicleList.setAdapter(vehicleAdapter);
    }

    private void dataListRecyclerView(){
        vpDataList = findViewById(R.id.ViewPager2DataList);
        dataAdapter = new DataAdapter(MainActivity.this, lstVehicle, mapMaintenanceRecord, mapRefuelingRecord);
        vpDataList.setAdapter(dataAdapter);
    }

    private void toolListRecyclerView(){
        rvToolList = findViewById(R.id.recyclerViewToolList);
        lmToolList = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        toolAdapter = new ToolAdapter(MainActivity.this, lstTool, lstVehicle, mapMaintenanceRecord, mapRefuelingRecord, lstGas);
        rvToolList.setLayoutManager(lmToolList);
        rvToolList.setAdapter(toolAdapter);

    }

}