package com.example.baseproject.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.baseproject.R;
import com.example.baseproject.databinding.ItemAppInforBinding;
import com.example.baseproject.model.AppInfor;

import java.util.ArrayList;
import java.util.List;

public class AppInforAdapter extends RecyclerView.Adapter<AppInforAdapter.ViewHolder> {
    private List<AppInfor> mListData;
    private final Context mContext;

    public void setListData(List<AppInfor> mListData) {
        this.mListData = mListData;
        notifyDataSetChanged();
    }


    public AppInforAdapter(Context context) {
        this.mContext = context;
        mListData = new ArrayList<>();
    }

    @Override
    public AppInforAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemAppInforBinding itemView =
                ItemAppInforBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(AppInforAdapter.ViewHolder holder, int position) {
        AppInfor appInfor = mListData.get(position);
        if (appInfor == null) return;

        holder.mBinding.tvPackageName.setText(appInfor.getmAppName());
        holder.mBinding.tvTime.setText(appInfor.getTimeUsage() + " ms");
        Glide.with(mContext).load(appInfor.getmIconApp()).into(holder.mBinding.imgIconApp);
    }

    @Override
    public int getItemCount() {
        return this.mListData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemAppInforBinding mBinding;

        public ViewHolder(@NonNull ItemAppInforBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }
    }

//    public interface CallBack {
//        void onClickContact(Contact contact);
//    }

}
