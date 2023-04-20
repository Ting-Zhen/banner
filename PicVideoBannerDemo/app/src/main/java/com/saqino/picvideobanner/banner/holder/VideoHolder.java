package com.saqino.picvideobanner.banner.holder;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saqino.picvideobanner.R;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

public class VideoHolder extends RecyclerView.ViewHolder  {
    public StandardGSYVideoPlayer player;

    public VideoHolder(@NonNull View view) {
        super(view);
        player = view.findViewById(R.id.player);
    }
}
