package com.example.driver_helper.vehicle.adapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.driver_helper.R;

import com.example.driver_helper.activities.MainActivity;
import com.example.driver_helper.activities.RecordActivity;
import com.example.driver_helper.activities.VehicleActivity;
import com.example.driver_helper.main.adapters.ToolAdapter;
import com.example.driver_helper.pojo.MaintenanceRecord;
import com.example.driver_helper.pojo.Record;
import com.example.driver_helper.pojo.RefuelingRecord;
import com.example.driver_helper.pojo.Vehicle;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Statement;
import java.util.List;

public class RecordAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<Record>lstRecord;
    private Vehicle vehicle;
    private Context context;
    private int typeHeader;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private boolean isStatement = false;
    public static final int TYPE_STATEMENT = 1;
    public static final int TYPE_SIMPLE = 0;
    public static final int TYPE_COMPLEX = 2;

    private int clickedPosition;

    Dialog deleteDialog, editDialog;
    View viewDialogDelete, viewDialogEdit;
    Button  btnDialogConfirm, btnDialogCancel;
    Record record;

    String urlMaintenance = "http://192.168.1.111:8080/maintenance/";
    String urlRefueling = "http://192.168.1.111:8080/refueling/";
    String strResponse, urlRecord;
    TextView tvCarName;
    EditText etDate, etPrice, etMileage, etNotes, etItem;
    Spinner spGasType;
    String [] arrGasType = {"92無鉛汽油", "95無鉛汽油", "98無鉛汽油", "超級柴油"};
    ArrayAdapter<String> gasTypeArrayAdapter;

    public RecordAdapter(Context context, List<Record> lstRecord){
        this.context = context;
        this.lstRecord = lstRecord;
    }

    public RecordAdapter(Context context, List<Record> lstRecord,Vehicle vehicle, int typeHeader) {
        this.lstRecord = lstRecord;
        this.context = context;
        this.vehicle = vehicle;
        this.typeHeader = typeHeader;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        gasTypeArrayAdapter = new ArrayAdapter<>(context, R.layout.item_spinner, arrGasType);
        if (viewType == TYPE_ITEM) {
            // Here Inflating your recyclerview item layout
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expense, parent, false);
            return new ItemViewHolder(itemView);
        } else if (viewType == TYPE_HEADER && typeHeader == TYPE_SIMPLE) {
            // Here Inflating your header view
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_total_expense, parent, false);
            return new HeaderViewHolder(itemView);
        }else if (typeHeader == TYPE_STATEMENT || typeHeader ==TYPE_COMPLEX) {
            // Here Inflating your header view
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_total_expense_statement, parent, false);
            return new StatementHeaderViewHolder(itemView);
        }
        else return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (holder instanceof ItemViewHolder){
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            itemViewHolder.tvDate.setText(lstRecord.get(position-1).getDate());
            itemViewHolder.tvItem.setText(lstRecord.get(position-1).getItem());
            itemViewHolder.tvPrice.setText("$"+lstRecord.get(position - 1).getPrice());
            if (typeHeader == TYPE_COMPLEX){

                itemViewHolder.ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        record = lstRecord.get(position - 1);
                        editDialog = new Dialog(context);

                        if(record instanceof RefuelingRecord){
                            viewDialogEdit = LayoutInflater.from(context).inflate(R.layout.dialog_edit_refueling, null);
                            urlRecord = urlRefueling;

                            tvCarName = viewDialogEdit.findViewById(R.id.edit_RR_tvCarName);
                            etDate = viewDialogEdit.findViewById(R.id.edit_RR_etDate);
                            etPrice = viewDialogEdit.findViewById(R.id.edit_RR_etPrice);
                            etMileage = viewDialogEdit.findViewById(R.id.edit_RR_etMileage);
                            etNotes = viewDialogEdit.findViewById(R.id.edit_RR_etNotes);
                            spGasType = viewDialogEdit.findViewById(R.id.edit_RR_spGastype);

                            spGasType.setAdapter(gasTypeArrayAdapter);

                            tvCarName.setText(vehicle.getName());
                            etDate.setText(String.valueOf(record.getDate()));
                            etPrice.setText(String.valueOf(record.getPrice()));
                            etMileage.setText(String.valueOf(vehicle.getMileage()));
                            etNotes.setText(record.getNotes());
                            int spinnerPosition = gasTypeArrayAdapter.getPosition(record.getItem());
                            spGasType.setSelection(spinnerPosition);

                            urlRecord = urlRefueling;
                        }else if(record instanceof MaintenanceRecord){
                            viewDialogEdit = LayoutInflater.from(context).inflate(R.layout.dialog_edit_maintenance, null);
                            tvCarName = viewDialogEdit.findViewById(R.id.edit_MR_tvCarName);
                            etDate = viewDialogEdit.findViewById(R.id.edit_MR_etDate);
                            etPrice = viewDialogEdit.findViewById(R.id.edit_MR_etPrice);
                            etMileage = viewDialogEdit.findViewById(R.id.edit_MR_etMileage);
                            etNotes = viewDialogEdit.findViewById(R.id.edit_MR_Notes);
                            etItem = viewDialogEdit.findViewById(R.id.edit_MR_etItem);

                            tvCarName.setText(vehicle.getName());
                            etDate.setText(String.valueOf(record.getDate()));
                            etPrice.setText(String.valueOf(record.getPrice()));
                            etMileage.setText(String.valueOf(vehicle.getMileage()));
                            etNotes.setText(record.getNotes());
                            etItem.setText(record.getItem());

                            urlRecord = urlMaintenance;
                        }



                        btnDialogConfirm = viewDialogEdit.findViewById(R.id.btnConfirm);
                        btnDialogCancel = viewDialogEdit.findViewById(R.id.btnCancel);

                        editDialog.setContentView(viewDialogEdit);
                        editDialog.show();

                        btnDialogConfirm.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (record instanceof RefuelingRecord) {
                                    record = new RefuelingRecord(
                                            record.getId(),
                                            etDate.getText().toString(), spGasType.getSelectedItem().toString(),
                                            Double.parseDouble(etPrice.getText().toString())/getGasPrice(spGasType.getSelectedItem().toString()),
                                            Long.parseLong(etPrice.getText().toString()),
                                            etNotes.getText().toString(),
                                            vehicle.getId()
                                    );

                                }else if (record instanceof MaintenanceRecord){
                                    record = new MaintenanceRecord(
                                            record.getId(),
                                            etDate.getText().toString(),
                                            etItem.getText().toString(),
                                            Long.parseLong(etPrice.getText().toString()),
                                            etNotes.getText().toString(), vehicle.getId()
                                    );
                                }

                                Thread editThread = new Thread(new RecordApiThread(urlRecord, record,"update"));
                                Log.w("ChiuThreadBug0", "Testing" );
                                editThread.start();

                                try {
                                    editThread.join();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                lstRecord.set(position-1, record);
                                notifyDataSetChanged();
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
                });
                itemViewHolder.ll.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        record = lstRecord.get(position - 1);

                        deleteDialog = new Dialog(context);
                        viewDialogDelete = LayoutInflater.from(context).inflate(R.layout.dialog_delete_record, null);

                        if(record instanceof RefuelingRecord){
                            urlRecord = urlRefueling;
                        }else if(lstRecord.get(0) instanceof MaintenanceRecord){
                            urlRecord = urlMaintenance;
                        }


                        btnDialogConfirm = viewDialogDelete.findViewById(R.id.btnConfirm);
                        btnDialogCancel = viewDialogDelete.findViewById(R.id.btnCancel);

                        deleteDialog.setContentView(viewDialogDelete);
                        deleteDialog.show();

                        btnDialogConfirm.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Thread deleteThread = new Thread(new RecordApiThread(urlRecord, record ,  "delete"));
                                Log.w("ChiuThreadBug0", "Testing" );
                                deleteThread.start();

                                try {
                                    deleteThread.join();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                lstRecord.remove(position-1);
                                notifyDataSetChanged();
                                deleteDialog.dismiss();
                            }
                        });

                        btnDialogCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                deleteDialog.dismiss();
                            }
                        });
                        return false;
                    }
                });
            }
        }else if (holder instanceof HeaderViewHolder){
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            headerViewHolder.tvTotalExpense.setText(VehicleActivity.getExpense(lstRecord)+ " 元");
        }else if (holder instanceof StatementHeaderViewHolder){
            StatementHeaderViewHolder headerViewHolder = (StatementHeaderViewHolder) holder;

            headerViewHolder.tvHeader.setText(" "+vehicle.getName());

            Drawable leftDrawable = ContextCompat.getDrawable(context, VehicleActivity.getLogoSrc(context, vehicle));
            leftDrawable.setBounds(0, 0, 120, 120);
            headerViewHolder.tvHeader.setCompoundDrawables(leftDrawable, null, null, null);

            headerViewHolder.tvTotalExpense.setText(VehicleActivity.getExpense(lstRecord)+ " 元");

        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }
    @Override
    public int getItemCount() {
        return lstRecord.size()+1;
    }

    private class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView tvTotalExpense;

        public HeaderViewHolder(View v) {
            super(v);
            tvTotalExpense =  v.findViewById(R.id.headerExpense_tvTotalExpense);
        }
    }

    private class StatementHeaderViewHolder extends RecyclerView.ViewHolder {
        TextView tvTotalExpense, tvHeader;

        public StatementHeaderViewHolder(View v) {
            super(v);
            tvHeader = v.findViewById(R.id.headerExpenseStatement_tvHeader);
            tvTotalExpense =  v.findViewById(R.id.headerExpenseStatement_tvTotalExpense);
        }
    }
    public class ItemViewHolder extends RecyclerView.ViewHolder{
        TextView tvPrice, tvDate, tvItem, tvNotes;
        LinearLayout ll;

        public ItemViewHolder(View v){
            super(v);
            tvDate = v.findViewById(R.id.itemExpense_tvDate);
            tvItem = v.findViewById(R.id.itemExpense_tvItem);
            tvPrice = v.findViewById(R.id.itemExpense_tvPrice);
            ll = v.findViewById(R.id.itemExpense_lineaerLayout);

        }
    }

    // Vehicle API Thread
    // call api to get data
    class RecordApiThread implements Runnable{
        private String url;
        private Record record;
        private String request;

        public RecordApiThread(String url, Record record, String request) {
            this.url = url;
            this.record = record;
            this.request = request;
        }

        @Override
        public void run() {
            if ("update".equals(request)) {
                strResponse = putRequest(url, record);
            } else if ("delete".equals(request)) {
                strResponse = deleteRequest(url, record);
            }
        }
    }

    private String putRequest(String strTxt, Record record){
        try {
            URL obj = new URL(strTxt+record.getId());
            Log.w("ChiuURL", strTxt+record.getId() );
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

            // Record convert to Json String
            Gson gson = new Gson();
            Log.w("ChiubeforeToJson", "beforeToJson" );
            String json = gson.toJson(record);
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

    private String deleteRequest(String strTxt, Record record){
        try {
            URL obj = new URL(strTxt + record.getId());
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod("DELETE");

            OutputStreamWriter out = new OutputStreamWriter(
                    con.getOutputStream());
            int responseCode = con.getResponseCode();

            Log.w("chiu", String.valueOf(responseCode));
            return String.valueOf(responseCode);
        } catch (MalformedURLException e) {
            Log.w("ChiuDeleteRequest", e);
        } catch (IOException e) {
            Log.w("ChiuDeleteRequest", e);
            return null;
        }
        return strTxt;
    }

    private Double getGasPrice(String gasType){
        if ("92無鉛汽油".equals(gasType)){
            return  MainActivity.gas_92;
        }else if ("95無鉛汽油".equals(gasType)){
            return  MainActivity.gas_95;
        }else if ("98無鉛汽油".equals(gasType)){
            return  MainActivity.gas_98;
        }else if ("超級柴油".equals(gasType)){
            return  MainActivity.gas_diesel;
        }
        return 1D;
    }
}
