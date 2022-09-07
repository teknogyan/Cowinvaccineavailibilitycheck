package com.example.cowinvaccineavailibilitycheck;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private ArrayList<DistrictData> districtDataArrayList = new ArrayList<>();

    public RecyclerAdapter(ArrayList<DistrictData> arrayList){
        this.districtDataArrayList = arrayList;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        private final TextView centreName;
        private final TextView tv_dose1;
        private final TextView tv_dose2;

        public TextView getCentreName() {
            return centreName;
        }

        public TextView getTv_dose1() {
            return tv_dose1;
        }

        public TextView getTv_dose2() {
            return tv_dose2;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            centreName = itemView.findViewById(R.id.tv_centerName);
            tv_dose1 = itemView.findViewById(R.id.tv_dose1);
            tv_dose2 = itemView.findViewById(R.id.tv_dose2);

        }
     }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_view,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.getCentreName().setText( (districtDataArrayList.get(position).getVaccineName() )+ " : "
                +( districtDataArrayList.get(position).getCentreName())); //fix it
        holder.getTv_dose1().setText(districtDataArrayList.get(position).getDose1());
        holder.getTv_dose2().setText(districtDataArrayList.get(position).getDose2());
    }

    @Override
    public int getItemCount() {
        return districtDataArrayList.size();
    }


}
