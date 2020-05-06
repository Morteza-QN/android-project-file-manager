package com.example.filemanagerproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.Arrays;

public class FileListFragment extends Fragment implements FileAdapter.FileItemEventListener {
    private String path;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        path = getArguments().getString("path");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View         view         = inflater.inflate(R.layout.fragment_files, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.rv_files);
        TextView     pathTv       = view.findViewById(R.id.tv_files_path);
        pathTv.setText(path);

        File   currentFolder = new File(path);
        File[] files         = currentFolder.listFiles();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(new FileAdapter(Arrays.asList(files), this));

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
            ((MainActivity) getActivity()).listFiles(path + File.separator + file.getName());
        }
    }
}
