package com.example.filemanagerproject;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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


        //        getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        //        File externalFilesDir = getExternalFilesDir(null);
        listFiles(getExternalFilesDir(null).getPath(), false);
    }

    public void listFiles(String path, boolean addToBackStack) {
        FileListFragment fileListFragment = new FileListFragment();
        Bundle           bundle           = new Bundle();
        bundle.putString("path", path);
        fileListFragment.setArguments(bundle);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_main_fragmentContainer, fileListFragment);
        if (addToBackStack)
            transaction.addToBackStack(null);
        transaction.commit();
    }

    public void listFiles(String path) { this.listFiles(path, true); }

    @Override
    public void onCreateBtnClick(String folderName) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame_main_fragmentContainer);
        if (fragment instanceof FileListFragment)
            ((FileListFragment) fragment).createNewFolder(folderName);
    }
}
