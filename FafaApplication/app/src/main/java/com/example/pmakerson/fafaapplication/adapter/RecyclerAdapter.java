package com.example.pmakerson.fafaapplication.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pmakerson.fafaapplication.R;
import com.example.pmakerson.fafaapplication.model.Track;
import com.squareup.picasso.Picasso;


import java.util.List;

/**
 * Created by P Makerson on 21/03/2016.
 * <p>
 * use to manage view on list
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private List<Track> list;

    private Context context;


    //ajouter un constructeur prenant en entrée une liste
    public RecyclerAdapter(Context context, List<Track> list) {
        this.context = context;
        this.list = list;
    }

    //cette fonction permet de créer les viewHolder
    //et par la même indiquer la vue à inflater (à partir des layout xml)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_nav_drawer, viewGroup, false);
        return new MyViewHolder(view);
    }

    //c'est ici que nous allons remplir notre cellule avec le texte/image de chaque myContent
    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int position) {
        Track myTrack = list.get(position);
        myViewHolder.bind(myTrack);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public Track getTract(int position) {
        return list.get(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView textTractTitle;
        private ImageView imageViewTarck;

        //itemView est la vue correspondante à 1 cellule
        public MyViewHolder(View itemView) {
            super(itemView);

            //c'est ici que l'on fait nos findView
            textTractTitle = (TextView) itemView.findViewById(R.id.track_title);
            imageViewTarck = (ImageView) itemView.findViewById(R.id.tract_logo);
        }

        //puis ajouter une fonction pour remplir la cellule en fonction d'un myContent
        public void bind(Track myTrack) {
            textTractTitle.setText(myTrack.getTrackName());
            Picasso.with(context).load(myTrack.getAlbumCoverUrl()).into(imageViewTarck);
        }

    }
}

