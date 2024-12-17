package com.example.cqqch.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cqqch.R;

import java.util.List;

public class ProfileImageAdapter extends RecyclerView.Adapter<ProfileImageAdapter.ProfileImageViewHolder> {

    private List<Integer> imageList;
    private OnImageClickListener onImageClickListener;

    public ProfileImageAdapter(List<Integer> imageList, OnImageClickListener onImageClickListener) {
        this.imageList = imageList;
        this.onImageClickListener = onImageClickListener;
    }

    @NonNull
    @Override
    public ProfileImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_perfil, parent, false);
        return new ProfileImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileImageViewHolder holder, int position) {
        int imageResource = imageList.get(position);
        holder.profileImage.setImageResource(imageResource);

        // Configurar clic
        holder.itemView.setOnClickListener(v -> onImageClickListener.onImageClick(imageResource));
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public static class ProfileImageViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImage;

        public ProfileImageViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.profile_image_option);
        }
    }

    public interface OnImageClickListener {
        void onImageClick(int imageResource);
    }
}
