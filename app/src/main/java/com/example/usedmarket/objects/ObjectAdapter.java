package com.example.usedmarket.objects;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.usedmarket.Config;
import com.example.usedmarket.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.usedmarket.Objects.integerList;

public class ObjectAdapter extends RecyclerView.Adapter<ObjectAdapter.ViewHolder> {
    private List<ObjectBean> objectBeanList;
    private ObjectBean objectBean;
    private Context context;

    public ObjectAdapter( Context context,List<ObjectBean> objectBeanList) {
        this.objectBeanList = objectBeanList;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.object_item, viewGroup, false);

        final ViewHolder holder=new ViewHolder(view);
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                final Integer integer= integerList.get(position);
                OkHttpClient client=new OkHttpClient();
                RequestBody requestBody = new FormBody.Builder()
                        .add("user_name",context.getSharedPreferences("zhigu",Context.MODE_PRIVATE).getString("account",""))
                        .add("goods_id", String.valueOf(integer))
                        .build();
                Request request=new Request.Builder()
                        .url(Config.host+"/user/login")
                        .post(requestBody)
                        .build();
                Call call=client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        final String error=e.getMessage();
                        Toast.makeText(context,error,Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseText=response.body().string();
                        try {
                            JSONObject jsonObject=new JSONObject(responseText);
                            if(jsonObject.getInt("code")==0){
                                Toast.makeText(context,"删除成功",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.goods_price.setText(objectBean.getGoods_price());
        viewHolder.goods_name.setText(objectBean.getGoods_name());
        Glide.with(context)
                .load(objectBean.getGoods_image())
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
        viewHolder.goods_detail.setText(objectBean.getGoods_detail());
        viewHolder.delete.setText("删除");
    }

    @Override
    public int getItemCount() {
        return objectBeanList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView goods_price;
        private TextView goods_name;
        private ImageView goods_image;
        private TextView goods_detail;
        private TextView delete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            goods_price = itemView.findViewById(R.id.goods_price);
            goods_name = itemView.findViewById(R.id.goods_name);
            goods_image = itemView.findViewById(R.id.goods_image);
            goods_detail = itemView.findViewById(R.id.goods_detail);
            delete=itemView.findViewById(R.id.delete);
        }
    }
}
