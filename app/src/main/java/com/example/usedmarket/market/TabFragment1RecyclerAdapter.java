package com.example.usedmarket.market;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.usedmarket.PersonHome;
import com.example.usedmarket.R;
import com.example.usedmarket.ShowActivity;

import java.util.List;

public class TabFragment1RecyclerAdapter extends RecyclerView.Adapter<TabFragment1RecyclerAdapter.ViewHolder> {

    private Context context;
    private List<FMBean> list;
    private FMBean fmBean;


    public TabFragment1RecyclerAdapter(Context context, List<FMBean> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public TabFragment1RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fm_item, viewGroup, false);

        final ViewHolder holder = new ViewHolder(view);
        holder.goodsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                fmBean = list.get(position);
                Intent intent = new Intent(v.getContext(), ShowActivity.class);
                intent.putExtra("user_name",fmBean.getUser_name());
                intent.putExtra("user_image",fmBean.getUser_image());
                intent.putExtra("goods_price",fmBean.getGoods_price());
                intent.putExtra("goods_name",fmBean.getGoods_name());
                intent.putExtra("goods_image",fmBean.getGoods_image());
                intent.putExtra("goods_detail",fmBean.getGoods_detail());

                v.getContext().startActivity(intent);
            }
        });
        holder.user_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                fmBean = list.get(position);
                Intent intent = new Intent(v.getContext(), PersonHome.class);
                v.getContext().startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TabFragment1RecyclerAdapter.ViewHolder viewHolder, int i) {
        fmBean = list.get(i);
        Glide.with(context)
                .load(fmBean.getUser_image()).override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(viewHolder.user_image);
        viewHolder.user_name.setText(fmBean.getUser_name());
        viewHolder.goods_price.setText(fmBean.getGoods_price());
        viewHolder.goods_name.setText(fmBean.getGoods_name());
        Glide.with(context)
                .load(fmBean.getGoods_image())
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(viewHolder.goods_image);
        viewHolder.goods_detail.setText(fmBean.getGoods_detail());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View goodsView;
        private ImageView user_image;
        private TextView user_name;
        private TextView goods_price;
        private TextView goods_name;
        private ImageView goods_image;
        private TextView goods_detail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            goodsView = itemView;
            user_image = itemView.findViewById(R.id.user_image);
            user_name = itemView.findViewById(R.id.user_name);
            goods_price = itemView.findViewById(R.id.goods_price);
            goods_name = itemView.findViewById(R.id.goods_name);
            goods_image = itemView.findViewById(R.id.goods_image);
            goods_detail = itemView.findViewById(R.id.goods_detail);
        }
    }
}
