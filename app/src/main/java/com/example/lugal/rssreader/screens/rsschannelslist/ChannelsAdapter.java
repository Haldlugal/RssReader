package com.example.lugal.rssreader.screens.rsschannelslist;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.lugal.rssreader.R;
import com.example.lugal.rssreader.database.ChannelDBWorker;
import com.example.lugal.rssreader.screens.articlelist.ArticleListActivity;

import java.util.ArrayList;

public final class ChannelsAdapter extends RecyclerView.Adapter<ChannelsAdapter.ChannelHolder> {

    private final ArrayList<Channel> channels;
    private final Context activity;

    ChannelsAdapter(final Context context, final ArrayList<Channel> channels) {
        this.channels = channels;
        this.activity = context;
    }

    @NonNull
    @Override
    public ChannelHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.channel_item, parent,false);
        return new ChannelHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ChannelHolder channelHolder, final int position) {
        channelHolder.channelId.setText(channels.get(position).getChannelId());
        channelHolder.channelName.setText(channels.get(position).getChannelName());
        channelHolder.channelUrl.setText(channels.get(position).getChannelUrl());
        final ButtonClickListener buttonClickListener = new ButtonClickListener(position);
        channelHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                channelButtonClicked(channelHolder.getAdapterPosition());
            }
        });
        channelHolder.deleteButton.setOnClickListener(buttonClickListener);

    }

    @Override
    public long getItemId(final int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return channels.size();
    }

    private void deleteChannelButtonClicked(final int position){
        final Intent intent = ChannelDBWorker.getDbWorkerIntentForDeletingChannel(activity, channels.get(position).getChannelId());
        activity.startService(intent);
    }

    private void channelButtonClicked(final int position){
        Channel channel = channels.get(position);
        ArrayList<Channel> channelList = new ArrayList<>();
        channelList.add(channel);
        Intent articleListIntent = ArticleListActivity.getArticleListIntent(activity, channelList);
        activity.startActivity(articleListIntent);
    }



    private final class ButtonClickListener implements View.OnClickListener{
        final private int position;
        ButtonClickListener(final int position) {
            this.position = position;
        }

        @Override
        public void onClick(final View view) {
            switch (view.getId()){
                case R.id.deleteChannelButton:{
                    deleteChannelButtonClicked(position);
                    break;
                }
                default:{
                    break;
                }
            }
        }
    }

    final class ChannelHolder extends RecyclerView.ViewHolder{
        private final TextView channelId;
        private final TextView channelName;
        private final TextView channelUrl;
        private final Button deleteButton;


        ChannelHolder(@NonNull final View itemView) {
            super(itemView);
            channelId = itemView.findViewById(R.id.channelItemId);
            channelName = itemView.findViewById(R.id.channelItemName);
            channelUrl = itemView.findViewById(R.id.channelItemUrl);
            deleteButton = itemView.findViewById(R.id.deleteChannelButton);
        }
    }
}
