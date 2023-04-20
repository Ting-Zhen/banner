package com.saqino.picvideobanner.banner.manager;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import com.saqino.picvideobanner.banner.ChangeBanner;
import com.saqino.picvideobanner.banner.adapter.MediaVideoBannerAdapter;
import com.saqino.picvideobanner.banner.holder.VideoHolder;
import com.saqino.picvideobanner.bean.ResourceBean;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.youth.banner.listener.OnPageChangeListener;

import java.util.List;

public class BannerVideoManager {

    private Context context;
    private ChangeBanner banner;
    private MediaVideoBannerAdapter adapter;
    //数据源，请自行替换
    private List<ResourceBean> list;

    private long mPageChangeMillis = 5000;

    private long mVideoPlayLoadWait = 500;

    public BannerVideoManager(Context context, ChangeBanner banner,
                              MediaVideoBannerAdapter adapter, List<ResourceBean> list){
        this.context = context;
        this.banner = banner;
        this.adapter = adapter;
        this.list = list;
        banner.addOnPageChangeListener(new BannerPageChange());
    }

    //设置切换间隔时间，单位毫秒
    public void setPageChangeMillis(long mills){
        this.mPageChangeMillis = mills;
    }

    /**
     * 设置视频播放前置等待时间，单位毫秒
     *
     * 设置为无限轮播时，适配器中会多出两个ViewHolder
     * 需要给banner自动切换ViewHolder一点时间
     * 保证获取到的ViewHolder是当前展示的那一个
     *
     * @param mills
     */
    public void setVideoPlayLoadWait(long mills){
        this.mVideoPlayLoadWait = mills;
    }

    /**
     * 视频+图片轮播处理代码
     */
    private VideoHolder currentVideoHolder = null;
    private class BannerPageChange implements OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            mPosition = position;
            if (adapter != null){
                currentVideoHolder = null;
                //当前页不是视频类型
                if (list.get(position).getType() != 2) {
                    adapter.stopVideo();
                    if (videoPlayRunnable != null) mHandler.removeCallbacks(videoPlayRunnable);
                    startScroll(mPageChangeMillis);
                }else {
                    //视频类型，且仅有一个视频，自动重播
                    if (list.size() == 1)
                        playVideo(0, position);
                    else {
                        playVideo(mVideoPlayLoadWait, position+1);
                    }
                    stopScroll();
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    //当前position（显示出来的position，非真实position
    private int mPosition = 0;

    private Handler mHandler = new Handler();
    //轮播切换的runnable
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            mHandler.removeCallbacks(runnable);
            banner.setCurrentItem(banner.getCurrentItem() + 1, true);
        }
    };

    //控制视频播放
    private Runnable videoPlayRunnable;
    private void playVideo(long millis, int position){
        if (videoPlayRunnable != null) mHandler.removeCallbacks(videoPlayRunnable);
        videoPlayRunnable = new Runnable(){
            @Override
            public void run() {
                if (position == 1 && list.size() > 1 && banner.getCurrentItem() != 1){
                    banner.setCurrentItem(1, false);
                    return;
                }
                VideoHolder videoHolder = adapter.getVideoHolder(position);
                if (videoHolder == null) {
                    showToast("获取视频播放控件出错");
                    return;
                }
                videoHolder.player.onVideoReset();
                videoHolder.player.startPlayLogic();
                videoHolder.player.setVideoAllCallBack(new GSYSampleCallBack(){
                    @Override
                    public void onAutoComplete(String url, Object... objects) {
                        super.onAutoComplete(url, objects);
                        if (position == 0) videoHolder.player.startPlayLogic();
                        else startScroll(0);
                    }
                });
                currentVideoHolder = videoHolder;
            }
        };
        mHandler.postDelayed(videoPlayRunnable, millis);
    }

    //停止轮播，并等待切换数据源
    public void stopBeforeChangeResource(){
        onPause();
        currentVideoHolder = null;
    }

    //切换数据源，并开始轮播
    public void startAlterChangeResource(){
        banner.setCurrentItem(1, false);
        mPosition = 0;
        banner.getIndicator().onPageSelected(mPosition);
        onResume();
    }

    //开始切换
    private void startScroll(long millis){
        mHandler.postDelayed(runnable, millis);
    }

    //停止切换
    private void stopScroll(){
        mHandler.removeCallbacks(runnable);
    }

    private void showToast(String str){
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }

    //Activity生命周期控制
    public void onPause(){
        stopScroll();
        if (currentVideoHolder != null) currentVideoHolder.player.onVideoPause();
        mHandler.removeCallbacks(videoPlayRunnable);
    }

    //Activity生命周期控制
    public void onResume(){
        if (list == null) return;
        if (list.size() == 0) return;
        if (list.get(mPosition).getType() == 2) {
            if (currentVideoHolder != null) currentVideoHolder.player.onVideoResume();
            else {
                if (list.size() == 1)
                    playVideo(mVideoPlayLoadWait, mPosition);
                else playVideo(mVideoPlayLoadWait, mPosition+1);
            }
        }else {
            if (list.size() == 1)
                banner.setCurrentItem(mPosition, false);
            else  banner.setCurrentItem(mPosition + 1, false);
            //banner.getIndicator().onPageSelected(mPosition);
            startScroll(mPageChangeMillis);
        }
    }

    //Activity生命周期控制
    public void onDetachedFromWindow(){
        stopScroll();
    }
}
