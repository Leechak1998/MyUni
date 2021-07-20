package com.example.myuni.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.example.myuni.BR;
import com.example.myuni.R;
import com.example.myuni.model.Contacts;

import java.util.List;

public class ListviewAdapterTest extends BaseAdapter {

    private List<Contacts> data;

    private Context context;

    public ListviewAdapterTest(List<Contacts> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewDataBinding binding;
        //ItemListBinding
        if (convertView == null) {
            binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.fragment_contacts, parent, false);
            if (binding==null){
//                L.e("空的binding");
            }
            convertView = binding.getRoot();
            convertView.setTag(binding);
        } else {
            binding = (ViewDataBinding) convertView.getTag();
        }

        binding.setVariable(BR.adapter, data.get(position));

        return binding.getRoot();

    }
}
