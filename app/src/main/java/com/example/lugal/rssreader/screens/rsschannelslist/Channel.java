package com.example.lugal.rssreader.screens.rsschannelslist;

import java.io.Serializable;

public final class Channel implements Serializable{
    private String channelId = "";
    private String channelName = "";
    private String channelUrl = "";
    private final String noName = "Не указано имя канала";
    private final String noUrl = "Не указан URL";
    private final String noId = "-1";

    public void setChannelId(final String channelId){
        if (channelId == null){
            this.channelId = noId;
        }
        else {
            this.channelId = channelId;
        }
    }
    public void setChannelName(final String channelName) {
        if (channelName==null){
            this.channelName = noName;
        }
        else {
            this.channelName = channelName;
        }
    }

    public void setChannelUrl(final String channelUrl) {
        if (channelUrl==null){
            this.channelUrl = noUrl;
        }
        else {
            this.channelUrl = channelUrl;
        }
    }

    public String getChannelId(){
        if ("".equals(channelId)){
            return noId;
        }
        else {
            return channelId;
        }
    }
    public String getChannelUrl() {
        if ("".equals(channelUrl)){
            return noUrl;
        }
        return channelUrl;
    }

    public String getChannelName() {
        if ("".equals(channelName)){
            return noName;
        }
        return channelName;
    }


}
