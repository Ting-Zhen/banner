package com.saqino.picvideobanner;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import com.saqino.picvideobanner.banner.manager.BannerVideoManager;
import com.saqino.picvideobanner.banner.ChangeBanner;
import com.saqino.picvideobanner.banner.adapter.MediaVideoBannerAdapter;
import com.saqino.picvideobanner.bean.ResourceBean;
import com.youth.banner.config.IndicatorConfig;
import com.youth.banner.indicator.CircleIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.banner)
    ChangeBanner banner;
    @BindView(R.id.btn)
    Button btn;

    private List<ResourceBean> dataList;
    private MediaVideoBannerAdapter mAdapter;
    private BannerVideoManager mBannerVideoManager;

    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);
        initDataList();

        mAdapter = new MediaVideoBannerAdapter(this, dataList);
        banner.isAutoLoop(false);
        banner.setAdapter(mAdapter).
                setIndicator(new CircleIndicator(this))
                .setIndicatorGravity(IndicatorConfig.Direction.CENTER);
        mBannerVideoManager = new BannerVideoManager(this, banner, mAdapter, dataList);
        mBannerVideoManager.setPageChangeMillis(5000);
        mBannerVideoManager.setVideoPlayLoadWait(500);

        btn.setOnClickListener(v -> change());
    }


    private void change(){
        mBannerVideoManager.stopBeforeChangeResource();
        dataList.clear();
        ResourceBean bean = new ResourceBean();
        bean.setType(1);
        bean.setUrl("https://model-player.oss-cn-beijing.aliyuncs.com/bg_banner_pink.png");
        dataList.add(bean);
        bean = new ResourceBean();
        bean.setType(2);
        bean.setUrl("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
        dataList.add(bean);
        mAdapter.notifyDataSetChanged();
        mBannerVideoManager.startAlterChangeResource();
    }

    /**
     * 数据源请自行替换
     * MediaVideoBannerAdapter也需要修改数据类型
     */
    private void initDataList(){
        dataList = new ArrayList<>();
        ResourceBean bean = new ResourceBean();
        bean.setType(1);
        bean.setUrl("https://model-player.oss-cn-beijing.aliyuncs.com/bg_banner_blue.png");
        dataList.add(bean);

        bean = new ResourceBean();
        bean.setType(2);
        bean.setUrl("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
        dataList.add(bean);
        bean = new ResourceBean();
        bean.setType(1);
        bean.setUrl("https://model-player.oss-cn-beijing.aliyuncs.com/bg_banner_pink.png");
        dataList.add(bean);
        bean = new ResourceBean();
        bean.setType(2);
        bean.setUrl("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
        dataList.add(bean);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBannerVideoManager.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBannerVideoManager.onPause();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mBannerVideoManager.onDetachedFromWindow();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
