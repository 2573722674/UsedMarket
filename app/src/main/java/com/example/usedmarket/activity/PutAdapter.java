package com.example.usedmarket.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.usedmarket.R;
import com.example.usedmarket.Show1Activity;
import com.example.usedmarket.ShowActivity;

import java.util.List;

public class PutAdapter extends RecyclerView.Adapter<PutAdapter.ViewHolder> {
    private Context context;
    private PutBean putBean;
    private List<PutBean> putBeanList;

    public PutAdapter(Context context,List<PutBean> putBeanList) {
        this.context=context;
        this.putBeanList = putBeanList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.put_item,viewGroup,false);
        final ViewHolder holder=new ViewHolder(view);
        holder.activityView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                putBean=putBeanList.get(position);
                Intent intent = new Intent(v.getContext(), Show1Activity.class);
                intent.putExtra("activityName",putBean.getActivityName());
                intent.putExtra("userName",putBean.getUserName());
                intent.putExtra("startTime",putBean.getStartTime());
                intent.putExtra("endTime",putBean.getEndTime());
                intent.putExtra("activityDetail",putBean.getActivityDetail());

                v.getContext().startActivity(intent);

            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        putBean=putBeanList.get(i);
        viewHolder.activityName.setText(putBean.getActivityName());
        viewHolder.userName.setText(putBean.getUserName());
        viewHolder.startTime.setText(putBean.getStartTime());
        viewHolder.endTime.setText(putBean.getEndTime());
        viewHolder.activityDetail.setText(putBean.getActivityDetail());

    }

    @Override
    public int getItemCount() {
        return putBeanList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView activityName,userName,startTime,endTime,activityDetail;
        private View activityView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            activityView=itemView;
            activityName=itemView.findViewById(R.id.activityName);
            userName=itemView.findViewById(R.id.userName);
            startTime=itemView.findViewById(R.id.startTime);
            endTime=itemView.findViewById(R.id.endTime);
            activityDetail=itemView.findViewById(R.id.activityDetail);

        }
    }
}
