package com.saqino.picvideobanner.banner.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saqino.picvideobanner.banner.holder.IBaseViewHolder;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.util.BannerUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseBannerAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> implements IBaseViewHolder<T, VH> {

    protected List<T> mDatas = new ArrayList<>();
    private OnBannerListener mOnBannerListener;
    private VH mViewHolder;
    private int increaseCount = 2;

    public BaseBannerAdapter(List<T> datas) {
        setDatas(datas);
    }

    public void setDatas(List<T> datas) {
        if (datas == null) {
            datas = new ArrayList<>();
        }
        mDatas = datas;
    }

    public T getData(int position) {
        return mDatas.get(position);
    }

    @Override
    public final void onBindViewHolder(@NonNull VH holder, int position) {
        mViewHolder = holder;
        int real = getRealPosition(position);
        onBindView(holder, mDatas.get(real), real, getRealCount(), position);
        if (mOnBannerListener != null)
            holder.itemView.setOnClickListener(view -> mOnBannerListener.OnBannerClick(mDatas.get(real), real));
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return onCreateHolder(parent, viewType);
    }

        @Override
    public int getItemCount() {
        return getRealCount() > 1 ? getRealCount() + increaseCount : getRealCount();
    }

    public int getRealCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    public int getRealPosition(int position) {
        return BannerUtils.getRealPosition(increaseCount == 2, position, getRealCount());
    }

    public void setOnBannerListener(OnBannerListener listener) {
        this.mOnBannerListener = listener;
    }

    public VH getViewHolder() {
        return mViewHolder;
    }

    public void setIncreaseCount(int increaseCount) {
        this.increaseCount = increaseCount;
    }
}
