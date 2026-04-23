package com.example.farmae_control.ui.sucursales;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SucursalesViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public SucursalesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Aquí van las sucursales");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
