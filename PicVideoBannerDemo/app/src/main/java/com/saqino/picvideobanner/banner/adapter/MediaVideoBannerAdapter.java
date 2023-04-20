package com.saqino.picvideobanner.banner.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.saqino.picvideobanner.R;
import com.saqino.picvideobanner.banner.holder.ImageHolder;
import com.saqino.picvideobanner.banner.holder.VideoHolder;
import com.saqino.picvideobanner.bean.ResourceBean;
import com.youth.banner.util.BannerUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MediaVideoBannerAdapter extends BaseBannerAdapter<ResourceBean, RecyclerView.ViewHolder> {
    private Context context;

    private Map<Integer, VideoHolder> videoHolderMap;

    public interface OnBannerVideoPlayListener{
        void onVideoPlayStart();
        void onVideoPlayComplete();
    }

    public MediaVideoBannerAdapter(Context context, List<ResourceBean> mDatas) {
        super(mDatas);
        this.context = context;
        videoHolderMap = new HashMap<Integer, VideoHolder>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 1:
                return new ImageHolder(BannerUtils.getView(parent, R.layout.banner_image));
            case 2:
                return new VideoHolder(BannerUtils.getView(parent, R.layout.banner_video));
            /*case 3:
                return new TitleHolder(BannerUtils.getView(parent, R.layout.banner_title));*/
        }
        return new ImageHolder(BannerUtils.getView(parent, R.layout.banner_image));
    }

    @Override
    public int getItemViewType(int position) {
        return getData(getRealPosition(position)).getType();
    }

    @Override
    public void onBindView(RecyclerView.ViewHolder holder, ResourceBean data, int position, int size, int adapterPosition) {
        int viewType = holder.getItemViewType();
        switch (viewType) {
            case 1:
                ImageHolder imageHolder = (ImageHolder) holder;
                Glide.with(imageHolder.itemView)
                        .load(data.getUrl())
                        .into(imageHolder.imageView);
                break;
            case 2:
                VideoHolder videoHolder = (VideoHolder) holder;
                videoHolder.player.setUp(data.getUrl(), true, null);
                videoHolder.player.getBackButton().setVisibility(View.GONE);
                //增加封面
                ImageView imageView = new ImageView(context);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                //imageView.setImageResource(R.drawable.image4);
                videoHolder.player.setThumbImageView(imageView);
                videoHolderMap.put(adapterPosition, videoHolder);
                break;
        }
    }

    public VideoHolder getVideoHolder(Integer position){
        return videoHolderMap.get(position);
    }


    public void stopVideo(){
        for (Integer key : videoHolderMap.keySet()){
            try {
                if (getVideoHolder(key) != null)
                    getVideoHolder(key).player.onVideoReset();
            }catch (Throwable e){
                Log.e("VideoBannerError", "停止视频播放出错 "+e.getLocalizedMessage());
            }
        }
    }
}
