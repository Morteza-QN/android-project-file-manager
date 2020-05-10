package com.example.filemanagerproject;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class AddFolderDialog extends DialogFragment {
    private TextInputEditText   folderNameEt;
    private TextInputLayout     folderNameEtl;
    private AlertDialog.Builder builder;
    private View                inflate;
    private View                createBtn;
    private Callback            callback;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        callback = ((Callback) context);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        inflate       = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_folder, null, false);
        builder       = new AlertDialog.Builder(getContext());
        folderNameEt  = inflate.findViewById(R.id.et_dialog_addFolder);
        folderNameEtl = inflate.findViewById(R.id.etl_dialog_addFolder);
        createBtn     = inflate.findViewById(R.id.btn_dialog_create);

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //callback to activity
                String nameFolder = folderNameEt.getText().toString().trim();
                if (nameFolder.isEmpty()) {
                    folderNameEtl.setError("folder name cannot be empty");
                }
                else {
                    callback.onCreateBtnClick(nameFolder);
                    dismiss();
                }
            }
        });
        return builder.setView(inflate).create();
    }

    public interface Callback {
        void onCreateBtnClick(String folderName);
    }
}
