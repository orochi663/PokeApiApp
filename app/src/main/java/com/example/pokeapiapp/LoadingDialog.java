package com.example.pokeapiapp;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

/**
 * Created by RTanweer on 11/2/2019.
 */
public class LoadingDialog extends DialogFragment {
// Simple modal wait dialog
    static String TAG = "LoadingDialog";

    private static LoadingDialog curInstance;

    static LoadingDialog getInstance() {
        if(curInstance == null) {
            curInstance = new LoadingDialog();
        }
        return curInstance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setCancelable(false);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.loading_layout, container, false);
    }
}
