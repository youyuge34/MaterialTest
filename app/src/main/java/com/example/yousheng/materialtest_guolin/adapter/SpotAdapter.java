package com.example.yousheng.materialtest_guolin.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.yousheng.materialtest_guolin.R;
import com.example.yousheng.materialtest_guolin.bean.Spot;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yousheng on 17/4/12.
 */

public class SpotAdapter extends RecyclerView.Adapter<SpotAdapter.SpotHolder> {
    public class SpotHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.spot_image) ImageView spotImage;
        @BindView(R.id.spot_name) TextView spotName;
        public SpotHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    private List<Spot> list=new ArrayList<>();
    Context context=null;

    public SpotAdapter(List<Spot> list) {
        this.list = list;
    }

    @Override
    public SpotHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context=parent.getContext();
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_spot_view,parent,false);
        return new SpotHolder(view);
    }

    @Override
    public void onBindViewHolder(SpotHolder holder, int position) {
        Spot spot=list.get(position);
        Glide.with(context).load(spot.picUrl).into(holder.spotImage);
        holder.spotName.setText(spot.name);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
