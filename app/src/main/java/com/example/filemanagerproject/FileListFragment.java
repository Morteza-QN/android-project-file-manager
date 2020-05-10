package com.example.filemanagerproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class FileListFragment extends Fragment implements FileAdapter.FileItemEventListener {
    private static final String       KEY_path = "path";
    private              String       path;
    private              FileAdapter  adapter;
    private              View         view;
    private              RecyclerView recyclerView;
    private              TextView     pathTv;

    private static void copy(File src, File dest) throws IOException {
        //Check if sourceFolder is a directory or file
        //If sourceFolder is file; then copy the file directly to new location
        if (src.isDirectory()) {
            //Verify if destinationFolder is already present; If not then create it
            if (!dest.exists()) { dest.mkdir(); }

            //Get all files from source directory
            String[] files = src.list();

            //Iterate over all files and copy them to destinationFolder one by one
            if (files != null) {
                for (String file : files) {
                    File srcFile  = new File(src, file);
                    File destFile = new File(dest, file);

                    //Recursive function call
                    copy(srcFile, destFile);
                }
            }
        }
        else {
            //Copy the file content from one place to another
            FileInputStream  inputStream  = new FileInputStream(src);
            FileOutputStream outputStream = new FileOutputStream(dest);
            byte[]           buffer       = new byte[1024];
            int              len;
            while ((len = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, len);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        path = getArguments().getString(KEY_path);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view         = inflater.inflate(R.layout.fragment_files, container, false);
        recyclerView = view.findViewById(R.id.rv_files);
        pathTv       = view.findViewById(R.id.tv_files_path);

        File   currentFolder = new File(path);
        File[] files         = currentFolder.listFiles();

        pathTv.setText(currentFolder.getName().equalsIgnoreCase("files") ? "External Storage" : currentFolder.getName());

        adapter = new FileAdapter(Arrays.asList(files), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        view.findViewById(R.id.im_files_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { getActivity().onBackPressed(); }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onItemClick(File file) {
        if (file.isDirectory()) {
            ((MainActivity) getActivity()).listFiles(file.getPath());
        }
    }

    @Override
    public void onItemDeleteClick(File file) {
        if (file.delete()) {
            adapter.deleteItem(file);
        }
    }

    @Override
    public void onItemCopyClick(File file) {
        try {
            copy(file, getDestinationFile(file.getName()));
            Toast.makeText(getContext(), "file is copied", Toast.LENGTH_LONG).show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemMoveClick(File file) {
        try {
            copy(file, getDestinationFile(file.getName()));
            onItemDeleteClick(file);
            Toast.makeText(getContext(), "file is moved", Toast.LENGTH_LONG).show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File getDestinationFile(String fileName) {
        return new File(getContext().getExternalFilesDir(null).getPath() + File.separator + "Destination" + File.separator +
                        fileName);
    }

    public void createNewFolder(String nameFolder) {
        File newFolder = new File(path + File.separator + nameFolder);
        if (!newFolder.exists()) {
            if (newFolder.mkdir()) {
                adapter.addItem(newFolder);
                recyclerView.scrollToPosition(0);
            }
        }
    }
}
