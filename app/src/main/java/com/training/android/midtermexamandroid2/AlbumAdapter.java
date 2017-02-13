package com.training.android.midtermexamandroid2;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.training.android.midtermexamandroid2.Model.Album;

import java.util.List;


public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {

    private Context context;
    private List<Album> data;
    private ViewHolder holder;


    public AlbumAdapter(List<Album> album) {
        this.data = album;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();

        final View itemLayoutView = LayoutInflater.from(context)
                .inflate(R.layout.card_layout, null);

        holder = new ViewHolder(itemLayoutView);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Album album = data.get(position);

        holder.mArtist.setText(album.getArtist());
        holder.mAlbum.setText(album.getAlbum());
        if (!album.getImgUrl().equals("")) {
            Picasso.with(context)
                    .load(album.getImgUrl())
                    .into(holder.mImgPic);
        }else {
            Picasso.with(context)
                    .load(R.mipmap.ic_launcher)
                    .into(holder.mImgPic);
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mAlbum;
        TextView mArtist;
        ImageView mImgPic;


        public ViewHolder(View itemView) {
            super(itemView);
            mAlbum = (TextView) itemView.findViewById(R.id.tvAlbum);
            mArtist = (TextView) itemView.findViewById(R.id.tvArtist);
            mImgPic = (ImageView) itemView.findViewById(R.id.imgPic);

        }


    }
}


