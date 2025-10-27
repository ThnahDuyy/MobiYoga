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
import com.nguyenthanhduy.yoga_d.model.ClassInstance;

import java.util.List;

public class ClassInstanceAdapter extends RecyclerView.Adapter<ClassInstanceAdapter.ClassInstanceViewHolder> implements IAdapter<ClassInstance> {
    private List<ClassInstance> classInstances;
    private IAdapterEvent<ClassInstance> listener;

    public ClassInstanceAdapter(List<ClassInstance> classInstances) {
        this.classInstances = classInstances;
    }

    @NonNull
    @Override
    public ClassInstanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_class_instance, parent, false);
        return new ClassInstanceAdapter.ClassInstanceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassInstanceViewHolder holder, int position) {
        ClassInstance classInstance = classInstances.get(position);
        holder.bindView(classInstance, listener);
    }

    @Override
    public int getItemCount() {
        return classInstances.size();
    }

    @Override
    public void add(ClassInstance model) {
        classInstances.add(model);
        notifyItemInserted(classInstances.size() - 1);
    }

    @Override
    public void update(ClassInstance model) {
        int locationUpdate = indexOfObject(model.classInstanceID);
        classInstances.set(locationUpdate, model);
        notifyItemChanged(locationUpdate);
    }

    @Override
    public void remove(String id) {
        int locationDelete = indexOfObject(id);
        classInstances.remove(locationDelete);
        notifyItemRemoved(locationDelete);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void updateDataList(List<ClassInstance> modelList) {
        classInstances = modelList;
        notifyDataSetChanged();
    }

    @Override
    public int indexOfObject(String id) {
        for (int i = 0; i < classInstances.size(); i++) {
            if (classInstances.get(i).classInstanceID.equals(id)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void setAdapterEvent(IAdapterEvent<ClassInstance> event) {
        this.listener = event;
    }


    public static class ClassInstanceViewHolder extends RecyclerView.ViewHolder {
        private TextView lbTeacher;
        private TextView lbDate;
        private TextView lbDes;
        private ImageButton btEdit;
        private ImageButton btDelete;

        public ClassInstanceViewHolder(@NonNull View itemView) {
            super(itemView);
            lbTeacher = itemView.findViewById(R.id.txt_teacher);
            lbDate = itemView.findViewById(R.id.txt_date);
            lbDes = itemView.findViewById(R.id.txt_des);
            btEdit = itemView.findViewById(R.id.btn_edit);
            btDelete = itemView.findViewById(R.id.btn_delete);
        }

        public void bindView(ClassInstance classInstance, IAdapterEvent<ClassInstance> listener) {
            lbTeacher.setText(classInstance.teacher);
            lbDate.setText(classInstance.date);
            lbDes.setText(classInstance.comment);
            btEdit.setOnClickListener(v -> {
                listener.onEditClicked(classInstance);
            });

            btDelete.setOnClickListener(v -> {
                listener.onRemoveClicked(classInstance.classInstanceID);
            });
        }
    }
}
