package com.example.yousheng.materialtest_guolin;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by yousheng on 17/3/18.
 */

public class FruitAdapter extends RecyclerView.Adapter<FruitAdapter.ViewHolder> {
    private List<Fruit> mList;
    Context mContext;
    public FruitAdapter(List<Fruit> list) {
        mList=list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_spot_view,parent,false);
        mContext=parent.getContext();
        final ViewHolder holder=new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                Fruit fruit=mList.get(position);
                Intent intent=new Intent(mContext,FruitActivity.class);
                intent.putExtra(FruitActivity.FRUIT_NAME,fruit.getName());
                intent.putExtra(FruitActivity.FRUIT_IMAGE_ID,fruit.getImageId());
                mContext.startActivity(intent);
            }
        });


        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
Fruit fruit=mList.get(position);
        holder.textView.setText(fruit.getName());
        Glide.with(mContext).load(fruit.getImageId()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView imageView;
        TextView textView;
        public ViewHolder(View itemView) {
            super(itemView);
            cardView= (CardView) itemView;
//            imageView= (ImageView) itemView.findViewById(R.id.fruit_image);
//            textView= (TextView) itemView.findViewById(R.id.fruit_name);
        }
    }
}
