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

public class SpotAdapter extends RecyclerView.Adapter<SpotAdapter.SpotHolder> {
    public class SpotHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.spot_image) ImageView spotImage;
        @BindView(R.id.spot_name) TextView spotName;

        public SpotHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

    private List<Spot> list = new ArrayList<>();
    Context context = null;
    private onRecyclerViewItemClicked mlistener;

    public SpotAdapter(List<Spot> list) {
        this.list = list;
    }

    //设置点击item回调的接口
    public void setOnRecyclerViewItemClickedListener(onRecyclerViewItemClicked listener){
        mlistener=listener;
    }

    @Override
    public SpotHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_spot_view, parent, false);
        return new SpotHolder(view);
    }

    @Override
    public void onBindViewHolder(final SpotHolder holder, int position) {
        Spot spot = list.get(position);
        Glide.with(context).load(spot.picUrl).fitCenter().into(holder.spotImage);
        holder.spotName.setText(spot.name);

        //设置点击item时候的事件
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mlistener!=null){
                    mlistener.onClicked(holder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
