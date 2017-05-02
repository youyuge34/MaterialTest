package com.example.yousheng.materialtest_guolin.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;
import com.bumptech.glide.Glide;
import com.example.yousheng.materialtest_guolin.R;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yousheng on 17/5/2.
 */

public class SquareListAdapter extends RecyclerView.Adapter<SquareListAdapter.SquareHolder> {
    private List<AVObject> mList = new ArrayList<>();
    private Context mContext;

    public SquareListAdapter(List<AVObject> mList) {
        this.mList = mList;
    }

    @Override
    public void onViewRecycled(SquareHolder holder) {
        
        super.onViewRecycled(holder);
    }

    @Override
    public SquareHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_square_list, parent, false);
        return new SquareHolder(view);
    }

    @Override
    public void onBindViewHolder(SquareHolder holder, final int position) {
        holder.spotName.setText(mList.get(position).get("name") == null ? "名字未知" : mList.get(position).get("name").toString());
        holder.spotOwner.setText(mList.get(position).getAVUser("owner") == null ? "unknown" : mList.get(position).getAVUser("owner").getUsername());
        Glide.with(mContext).load(mList.get(position).get("picUrl")).into(holder.sqareImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TastyToast.makeText(mContext,""+mList.get(position).getObjectId(),TastyToast.LENGTH_SHORT,TastyToast.SUCCESS);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class SquareHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.square_image)
        ImageView sqareImage;
        @BindView(R.id.spot_name)
        TextView spotName;
        @BindView(R.id.spot_owner)
        TextView spotOwner;


        public SquareHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
