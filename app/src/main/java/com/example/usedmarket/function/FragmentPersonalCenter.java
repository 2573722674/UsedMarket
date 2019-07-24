package com.example.usedmarket.function;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.usedmarket.Collect;
import com.example.usedmarket.Config;
import com.example.usedmarket.FeedBack;
import com.example.usedmarket.Objects;
import com.example.usedmarket.Played;
import com.example.usedmarket.R;
import com.example.usedmarket.PersonHome;
import com.example.usedmarket.Setting;
import com.example.usedmarket.util.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FragmentPersonalCenter extends Fragment {

    private LinearLayout person,objects,played,sold,putactivity,setting;
    private ImageView user_image;
    private TextView user_name;
    String userimage;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_personal_center,container,false);
        person=view.findViewById(R.id.person);
        sold=view.findViewById(R.id.sold);
        putactivity=view.findViewById(R.id.putactivity);
        setting=view.findViewById(R.id.setting);
        objects=view.findViewById(R.id.objects);
        played=view.findViewById(R.id.played);
        user_image=view.findViewById(R.id.user_image);
        user_name=view.findViewById(R.id.user_name);
        String user=getContext().getSharedPreferences("zhigu",Context.MODE_PRIVATE).getString("account","");
        user_name.setText(user);
        HttpUtil.sendOkHttpRequest(Config.host + "/user/image?user_name="+user,new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                final String error=e.getMessage();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(),error,Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText=response.body().string();
                try {
                    JSONObject jsonObject=new JSONObject(responseText);
                    final  String image=jsonObject.getString("message");

                    userimage=image;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Glide.with(getContext()).load(image).into(user_image);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        setListeners();
        return view;
    }

    private void setListeners(){
        OnClick onClick=new OnClick();
        person.setOnClickListener(onClick);
        sold.setOnClickListener(onClick);
        setting.setOnClickListener(onClick);
        putactivity.setOnClickListener(onClick);
        objects.setOnClickListener(onClick);
        played.setOnClickListener(onClick);
    }

    private class OnClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent intent=null;
            switch (v.getId()){
                case R.id.putactivity:
                    intent=new Intent(getActivity(), FeedBack.class);
                    break;
                case R.id.person:
                    intent=new Intent(getActivity(),PersonHome.class);
                    intent.putExtra("user_image",userimage);
                    break;
                case R.id.objects:
                    intent=new Intent(getActivity(), Objects.class);
                    break;
                case R.id.played:
                    intent=new Intent(getActivity(), Played.class);
                    break;
                case R.id.sold:
                    intent=new Intent(getActivity(), Collect.class);
                    break;
                case R.id.setting:
                    intent=new Intent(getActivity(), Setting.class);
                    break;
            }
            startActivity(intent);
        }
    }
}
