package com.example.tresversiones.ui.rutagps;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SlideshowViewModel extends ViewModel {

    private MutableLiveData<String> mText;



    public LiveData<String> getText() {
        return mText;
    }
}