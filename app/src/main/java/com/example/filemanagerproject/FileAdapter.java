package com.example.filemanagerproject;


import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.FileViewHolder> {
    private List<File>            files;
    private List<File>            filterFiles;
    private FileItemEventListener eventListener;
    private ViewType              viewType;

    public FileAdapter(List<File> files, FileItemEventListener eventListener) {
        this.files         = new ArrayList<>(files);
        this.eventListener = eventListener;
        this.filterFiles   = this.files;
        if (viewType != null) {
            viewType = ViewType.ROW;
        }
    }

    @NonNull
    @Override
    public FileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FileViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                viewType == ViewType.ROW.getValue() ? R.layout.item_files : R.layout.item_file_grid, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FileViewHolder holder, int position) {
        holder.bindFile(filterFiles.get(position));
    }

    @Override
    public int getItemViewType(int position) { return viewType.getValue(); }

    @Override
    public int getItemCount() { return filterFiles.size(); }

    public void setViewType(ViewType viewType) {
        this.viewType = viewType;
        notifyDataSetChanged();
    }

    public void addItem(File file) {
        files.add(0, file);
        notifyItemInserted(0);
    }

    public void deleteItem(File file) {
        int index = files.indexOf(file);
        if (index > -1) {
            files.remove(file);
            notifyItemRemoved(index);
        }
    }

    public void searchItem(String query) {
        if (!query.isEmpty()) {
            List<File> result = new ArrayList<>();
            for (File file : files) {
                if (file.getName().toLowerCase().contains(query.toLowerCase())) { result.add(file); }
            }
            filterFiles = result;
            notifyDataSetChanged();
        }
        else {
            filterFiles = files;
            notifyDataSetChanged();
        }

    }

    public interface FileItemEventListener {
        void onItemClick(File file);

        void onItemDeleteClick(File file);

        void onItemCopyClick(File file);

        void onItemMoveClick(File file);
    }

    class FileViewHolder extends RecyclerView.ViewHolder {
        private TextView  fileNameTv;
        private ImageView fileIcon;
        private View      moreIv;

        FileViewHolder(@NonNull View itemView) {
            super(itemView);
            fileNameTv = itemView.findViewById(R.id.tv_item_nameFile);
            fileIcon   = itemView.findViewById(R.id.iv_item_folder);
            moreIv     = itemView.findViewById(R.id.iv_item_more);
        }


        void bindFile(final File file) {
            if (file.isDirectory()) { fileIcon.setImageResource(R.drawable.ic_folder_black_32dp); }
            else { fileIcon.setImageResource(R.drawable.ic_file_black_32dp); }
            fileNameTv.setText(file.getName());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { eventListener.onItemClick(file); }
            });
            moreIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
                    popupMenu.getMenuInflater().inflate(R.menu.menu_file_item, popupMenu.getMenu());
                    popupMenu.show();
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.menu_item_delete:
                                    eventListener.onItemDeleteClick(file);
                                    break;
                                case R.id.menu_item_copy:
                                    eventListener.onItemCopyClick(file);
                                    break;
                                case R.id.menu_item_move:
                                    eventListener.onItemMoveClick(file);
                                    break;
                            }
                            return false;
                        }
                    });
                }
            });
        }
    }
}
