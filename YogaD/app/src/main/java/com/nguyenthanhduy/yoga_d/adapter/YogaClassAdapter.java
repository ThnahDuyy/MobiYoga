package com.nguyenthanhduy.yoga_d.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nguyenthanhduy.yoga_d.R;
import com.nguyenthanhduy.yoga_d.model.YogaClass;

import java.util.List;

public class YogaClassAdapter extends RecyclerView.Adapter<YogaClassAdapter.YogaViewHolder> implements IAdapter<YogaClass> {
    public List<YogaClass> yogaClasses;
    private IAdapterEvent<YogaClass> event;

    public YogaClassAdapter(List<YogaClass> yogaClasses) {
        this.yogaClasses = yogaClasses;
    }

    @NonNull
    @Override
    public YogaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_yoga_class, parent, false);
        return new YogaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull YogaViewHolder holder, int position) {
        YogaClass yoga = yogaClasses.get(position);
        try {
            holder.bindUI(yoga, event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return this.yogaClasses.size();
    }

    @Override
    public void add(YogaClass model) {
        yogaClasses.add(model);
        notifyItemInserted(yogaClasses.size() - 1);
    }

    @Override
    public void update(YogaClass model) {
        int locationUpdate = indexOfObject(model.yogaClassID);
        yogaClasses.set(locationUpdate, model);
        notifyItemChanged(locationUpdate);
    }

    @Override
    public void remove(String id) {
        int indexDelete = indexOfObject(id);
        this.yogaClasses.remove(indexDelete);
        notifyItemRemoved(indexDelete);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void updateDataList(List<YogaClass> modelList) {
        this.yogaClasses = modelList;
        notifyDataSetChanged();
    }

    @Override
    public int indexOfObject(String id) {
        for (int i = 0; i < yogaClasses.size(); i++) {
            if (yogaClasses.get(i).yogaClassID.equals(id)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void setAdapterEvent(IAdapterEvent<YogaClass> event) {
        this.event = event;
    }

    public static class YogaViewHolder extends RecyclerView.ViewHolder {
        private TextView lbName;
        private TextView lbPrice;
        private TextView lbTimeDay;
        private ImageButton btEdit;
        private ImageButton btDelete;

        public YogaViewHolder(@NonNull View itemView) {
            super(itemView);

            lbName = itemView.findViewById(R.id.txt_name);
            lbPrice = itemView.findViewById(R.id.txt_price);
            lbTimeDay = itemView.findViewById(R.id.txt_time_day);
            btEdit = itemView.findViewById(R.id.btn_edit);
            btDelete = itemView.findViewById(R.id.btn_delete);
        }

        @SuppressLint("SetTextI18n")
        public void bindUI(YogaClass yogaClass, IAdapterEvent<YogaClass> listener) throws Exception {
            lbName.setText(yogaClass.yogaName);
            lbPrice.setText(yogaClass.priceString());
            lbTimeDay.setText(yogaClass.getDayOfWeekString());

            btEdit.setOnClickListener(v -> {
                listener.onEditClicked(yogaClass);
            });

            btDelete.setOnClickListener(v -> {
                listener.onRemoveClicked(yogaClass.yogaClassID);
            });

            itemView.setOnClickListener(v -> {
                listener.onObjectClicked(yogaClass);
            });
        }
    }
}
