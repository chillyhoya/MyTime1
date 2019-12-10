package com.exmaple.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.ViewHolder> {
    private Context context;
    private List<Memorial> memorials;
    public NoteListAdapter(Context context, List<Memorial> memorials) {
        this.context = context;
        this.memorials = memorials;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_note, parent,false);
        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Memorial memorial = memorials.get(position);
        holder.mTvName.setText(memorial.getTitle());
        holder.mTvDes.setText(memorial.getContent());
        if (!TextUtils.isEmpty(memorial.getImg())) {
            Bitmap photo = BitmapFactory.decodeFile(memorial.getImg());
            holder.ivImg.setImageBitmap(photo);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener!=null){
                    listener.setOnItemClickListener(position);
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (listener!=null){
                    listener.setOnItemLongClickListener(position);
                }
                return false;
            }
        });
        holder.mtvTime.setText(memorial.getTime());
        holder.tv_day.setText(get(memorial.getTime())+"天");
    }

    @Override
    public int getItemCount() {
        return memorials.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView mTvName;
        TextView mTvDes;
        TextView mtvTime;
        ImageView ivImg;
        TextView tv_day;
        public ViewHolder(View itemView) {
            super(itemView);
            mTvName = itemView.findViewById(R.id.mTvName);
            mTvDes=itemView.findViewById(R.id.mTvDes);
            mtvTime = itemView.findViewById(R.id.mtvTime);
            ivImg = itemView.findViewById(R.id.iv_img);
            tv_day = itemView.findViewById(R.id.tv_day);
        }
    }
    private ItemClickListener listener;
    public void setOnItemClickListener(ItemClickListener listener ){
        this.listener = listener;
    }
    public interface ItemClickListener{
        void setOnItemClickListener(int position);
        void setOnItemLongClickListener(int position);
    }
    private long get(String time){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date d1 = df.parse(time);
            Date d2 = new Date();
            long diff = d1.getTime() - d2.getTime();
            long days = diff / (1000 * 60 * 60 * 24);
            return days;
        }
        catch (Exception e) {
        }
        return 0;
    }
}
