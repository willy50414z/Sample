package com.willy.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.willy.myapplication.R;
import com.willy.myapplication.listener.indexTracker.DeleteTrackItemListener;
import com.willy.myapplication.po.TrackListPO;

import java.util.List;

public class TrackingItemAdapter extends BaseAdapter {
    private Context context;
    private List<TrackListPO> btnLvDataList;
    private int id;

    public TrackingItemAdapter(Context context, List<TrackListPO> btnLvDataList) {
        super();
        this.context = context;
        this.btnLvDataList = btnLvDataList;
    }

    public int getCount() {
        // return the number of records
        return btnLvDataList.size();
    }

    // getView method is called for each item of ListView
    public View getView(int position, View view, ViewGroup parent) {
        // inflate the layout for each item of listView
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.track_item_row, parent, false);


        // get the reference of textView and button
        TextView txtTrackTarget = (TextView) view.findViewById(R.id.txtTrackTarget);
        TextView trackDetail = (TextView) view.findViewById(R.id.trackDetail);
        Button btnAction = (Button) view.findViewById(R.id.btnAction);
        TextView trackId = (TextView) view.findViewById(R.id.trackId);

        // Set the title and button name
        trackId.setText(String.valueOf(btnLvDataList.get(position).getId()));
        txtTrackTarget.setText(btnLvDataList.get(position).getTrackTarget(context).getTargetName());
        trackDetail.setText(btnLvDataList.get(position).getDnLimit()+" - "+btnLvDataList.get(position).getUpLimit()+"   AMT: "+btnLvDataList.get(position).getAmt());
        btnAction.setText("Delete");
        btnAction.setId(btnLvDataList.get(position).getId());

        // Click listener of button
        btnAction.setOnClickListener(new DeleteTrackItemListener(context));

        return view;
    }

    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
}