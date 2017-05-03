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
import com.example.yousheng.materialtest_guolin.view.SquareDetailActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yousheng on 17/5/2.
 */

public class SquareListAdapter extends RecyclerView.Adapter<SquareListAdapter.SquareHolder> {
    private List<AVObject> mList = new ArrayList<>();
    private Context mContext;

    public SquareListAdapter(List<AVObject> mList,Context context) {
        this.mList = mList;
        mContext=context;
    }

    @Override
    public SquareHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_square_list, parent, false);
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
                SquareDetailActivity.newInstance(mContext,mList.get(position).getObjectId().toString());
//                TastyToast.makeText(mContext,""+mList.get(position).getObjectId(),TastyToast.LENGTH_SHORT,TastyToast.SUCCESS);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class SquareHolder extends RecyclerView.ViewHolder {
        ImageView sqareImage;
        TextView spotName;
        TextView spotOwner;

        View itemView;

        public SquareHolder(View itemView) {
            super(itemView);
            this.itemView=itemView;
            sqareImage= (ImageView) itemView.findViewById(R.id.square_image);
            spotName= (TextView) itemView.findViewById(R.id.spot_name);
            spotOwner= (TextView) itemView.findViewById(R.id.spot_owner);
        }
    }
}
