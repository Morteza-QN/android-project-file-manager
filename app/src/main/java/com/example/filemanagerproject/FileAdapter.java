package com.example.filemanagerproject;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.FileViewHolder> {
    private List<File>            files;
    private FileItemEventListener eventListener;

    public FileAdapter(List<File> files, FileItemEventListener eventListener) {
        this.files         = files;
        this.eventListener = eventListener;
    }

    @NonNull
    @Override
    public FileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FileViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_files, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FileViewHolder holder, int position) { holder.bindFile(files.get(position)); }

    @Override
    public int getItemCount() { return files.size(); }

    public interface FileItemEventListener {
        void onItemClick(File file);
    }

    class FileViewHolder extends RecyclerView.ViewHolder {
        private TextView  fileNameTv;
        private ImageView fileIcon;

        FileViewHolder(@NonNull View itemView) {

            super(itemView);
            fileNameTv = itemView.findViewById(R.id.tv_item_nameFile);
            fileIcon   = itemView.findViewById(R.id.iv_item_folder);
        }

        void bindFile(final File file) {
            if (file.isDirectory())
                fileIcon.setImageResource(R.drawable.ic_folder_black_32dp);
            else
                fileIcon.setImageResource(R.drawable.ic_file_black_32dp);
            fileNameTv.setText(file.getName());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { eventListener.onItemClick(file); }
            });
        }

    }
}
