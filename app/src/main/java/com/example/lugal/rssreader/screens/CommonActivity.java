package com.example.lugal.rssreader.screens;

import java.util.ArrayList;

public abstract class CommonActivity extends OptionsActivity {

    private final ArrayList<CommonActivityController> observers = new ArrayList<>();

    @Override
    protected void onResume() {
        super.onResume();
        notifyOnResume();
    }

    @Override
    protected void onStart() {
        notifyOnStart();
        super.onStart();
    }

    private void notifyOnStart(){
        for (int i = 0, size = observers.size(); i < size; i++) {
            final CommonActivityController observer = observers.get(i);
            observer.onStart();
        }
    }

    protected void notifyOnCreate(){
        for (int i = 0, size = observers.size(); i < size; i++) {
            final CommonActivityController observer = observers.get(i);
            observer.onCreate();
        }
    }
    private void notifyOnResume(){
        for (int i = 0, size = observers.size(); i < size; i++) {
            final CommonActivityController observer = observers.get(i);
            observer.onResume();
        }
    }
    protected void notifyOnPause(){
        for (int i = 0, size = observers.size(); i < size; i++) {
            final CommonActivityController observer = observers.get(i);
            observer.onPause();
        }
    }


    protected void addObserver(final CommonActivityController observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    protected void removeObserver(final CommonActivityController observer) {
        if (!observers.isEmpty()) {
            observers.remove(observer);
        }
    }

}
