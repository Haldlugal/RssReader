package com.example.lugal.rssreader.screens.articlelist.articlesDelivery;

import android.content.Context;
import android.content.Intent;

import com.example.lugal.rssreader.errors.ErrorsReceiver;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import com.example.lugal.rssreader.R;

public final class RssTextDownloader {
    private final Context context;
    public RssTextDownloader(Context context) {
        this.context = context;
    }

    public StringReader generateParserInput(final String urlString) {
        HttpURLConnection connection = null;
        final StringBuilder output = new StringBuilder();
        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10 * 1000);
            connection.setConnectTimeout(10 * 1000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            final int bufferSize = 1024;
            final char[] buffer = new char[bufferSize];
            Reader in = new InputStreamReader(inputStream);
            for (; ; ) {
                int rsz = in.read(buffer, 0, buffer.length);
                if (rsz < 0)
                    break;
                output.append(buffer, 0, rsz);
            }
            return new StringReader(output.toString());
        }
        catch (final MalformedURLException e) {
            final String errorText = context.getString(R.string.malformedUrlError) + urlString;
            sendError(errorText);
        }
        catch (final UnknownHostException e){
            final String errorText = context.getString(R.string.unknownHostError) + urlString;
            sendError(errorText);
        }
        catch (final IOException e) {
            final String errorText = context.getString(R.string.errorWhileReceivingRss) + urlString;
            sendError(errorText);
        }
        finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return new StringReader(output.toString());
    }
    private void sendError(final String message){
        Intent errorBroadcast = ErrorsReceiver.getErrorReceiver(message);
        context.sendBroadcast(errorBroadcast);
    }
}
