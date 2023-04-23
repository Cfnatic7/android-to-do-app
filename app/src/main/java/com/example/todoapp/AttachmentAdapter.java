package com.example.todoapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class AttachmentAdapter extends RecyclerView.Adapter<AttachmentAdapter.AttachmentViewHolder> {
    private List<String> attachments;
    private Context context;

    public AttachmentAdapter(List<String> attachments, Context context) {
        this.attachments = attachments;
        this.context = context;
    }

    public static class AttachmentViewHolder extends RecyclerView.ViewHolder {
        ImageView attachmentImageView;

        public AttachmentViewHolder(View itemView) {
            super(itemView);
            attachmentImageView = itemView.findViewById(R.id.attachment_image_view);
        }

        public void bind(final String attachment) {
            // Odczytanie obrazu z pamięci zewnętrznej i ustawienie go jako źródła ImageView
            Bitmap bitmap = BitmapFactory.decodeFile(attachment);
            attachmentImageView.setImageBitmap(bitmap);
        }
    }

    @NonNull
    @Override
    public AttachmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.attachment_item, parent, false);
        return new AttachmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AttachmentViewHolder holder, int position) {
        holder.bind(attachments.get(position));
    }

    @Override
    public int getItemCount() {
        return attachments.size();
    }
}
