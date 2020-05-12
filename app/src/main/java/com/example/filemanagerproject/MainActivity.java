package com.example.filemanagerproject;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.button.MaterialButtonToggleGroup;

public class MainActivity extends AppCompatActivity implements AddFolderDialog.Callback {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.iv_main_addFolder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AddFolderDialog().show(getSupportFragmentManager(), null);
            }
        });

        if (StorageHelper.isExternalStorageReadable()) { listFiles(getExternalFilesDir(null).getPath(), false); }
        //        File externalFilesDir = getExternalFilesDir(null);
        //        getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);

        final EditText searchEt = findViewById(R.id.et_main_search);
        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame_main_fragmentContainer);
                if (fragment instanceof FileListFragment) {
                    ((FileListFragment) fragment).search(s.toString().trim());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        MaterialButtonToggleGroup toggleGroup = findViewById(R.id.toggleGroup_main);
        toggleGroup.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame_main_fragmentContainer);
                if (fragment instanceof FileListFragment) {
                    switch (checkedId) {
                        case R.id.btn_main_list:
                            if (isChecked) {
                                ((FileListFragment) fragment).setViewType(ViewType.ROW);
                            }
                            break;
                        case R.id.btn_main_grid:
                            if (isChecked) {
                                ((FileListFragment) fragment).setViewType(ViewType.GRID);
                            }
                            break;
                    }
                }
            }
        });
    }

    public void listFiles(String path, boolean addToBackStack) {
        FileListFragment fileListFragment = new FileListFragment();
        Bundle           bundle           = new Bundle();
        bundle.putString("path", path);
        fileListFragment.setArguments(bundle);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_main_fragmentContainer, fileListFragment);
        if (addToBackStack) { transaction.addToBackStack(null); }
        transaction.commit();
    }

    public void listFiles(String path) { this.listFiles(path, true); }

    @Override
    public void onCreateBtnClick(String folderName) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame_main_fragmentContainer);
        if (fragment instanceof FileListFragment) { ((FileListFragment) fragment).createNewFolder(folderName); }
    }
}
