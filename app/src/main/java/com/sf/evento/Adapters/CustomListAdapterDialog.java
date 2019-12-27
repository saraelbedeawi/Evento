package com.sf.evento.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sf.evento.R;

import java.util.List;

public class CustomListAdapterDialog extends BaseAdapter {

    private List<DocumentSnapshot> friends;
    FirebaseFirestore db;
    private LayoutInflater layoutInflater;

    public CustomListAdapterDialog(Context context,List<DocumentSnapshot> friends,FirebaseFirestore db) {
        this.friends = friends;
        layoutInflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return friends.size();
    }

    @Override
    public Object getItem(int position) {
        return friends.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

//    public View getView(int position, View convertView, ViewGroup parent) {
//        ViewHolder holder;
//        if (convertView == null) {
//            convertView = layoutInflater.inflate(R.layout.list_row_dialog, null);
//            holder = new ViewHolder();
//            holder.unitView = (TextView) convertView.findViewById(R.id.unit);
//            holder.quantityView = (TextView) convertView.findViewById(R.id.quantity);
//            convertView.setTag(holder);
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }
//
//        holder.unitView.setText(listData.get(position).getVariant().toString());
//        holder.quantityView.setText(listData.get(position).getUnit().toString());
//
//        return convertView;
//    }

    static class ViewHolder {
        TextView unitView;
        TextView quantityView;
    }

}