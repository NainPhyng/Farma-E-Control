package com.example.farmae_control.ui.membresia;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MembresiaViewModel extends ViewModel {
    private final MutableLiveData<String> mText;

    public MembresiaViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Aquí van las membresias");
    }

    public LiveData<String> getText() {
        return mText;
    }
}