package com.example.driver_helper.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.driver_helper.R;
import com.example.driver_helper.pojo.Tool;

import java.util.List;

public class ToolAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    int row_index;
    private List<Tool> lstTool;
    private Context context;

    public ToolAdapter(Context context, List<Tool> lstTool){
        this.context = context;
        this.lstTool = lstTool;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tool, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        itemViewHolder.tv.setText(lstTool.get(position).getName());
        itemViewHolder.iv.setImageResource(lstTool.get(position).getLogo());
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
}

