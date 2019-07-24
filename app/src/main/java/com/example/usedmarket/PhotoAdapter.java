package com.example.usedmarket;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;



import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> {

    private PhotoBean photoBean;
    private List<PhotoBean> photoBeanList;

    public PhotoAdapter(List<PhotoBean> photoBeanList) {

        this.photoBeanList = photoBeanList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.photo_item,viewGroup,false);
        ViewHolder holder=new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        photoBean=photoBeanList.get(i);
        viewHolder.photo.setImageBitmap(photoBean.getPhoto()); ;
    }

    @Override
    public int getItemCount() {
        return photoBeanList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView photo;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            photo=itemView.findViewById(R.id.photo);
        }
    }
}
