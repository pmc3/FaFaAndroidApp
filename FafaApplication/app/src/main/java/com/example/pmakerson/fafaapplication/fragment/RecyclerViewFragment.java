package com.example.pmakerson.fafaapplication.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pmakerson.fafaapplication.R;
import com.example.pmakerson.fafaapplication.adapter.ViewRecyclerAdapter;
import com.example.pmakerson.fafaapplication.manager.FragmentActionManager;
import com.example.pmakerson.fafaapplication.manager.RecyclerItemClickListener;
import com.example.pmakerson.fafaapplication.model.Track;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by P Makerson on 12/04/2016.
 */
public class RecyclerViewFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private ViewRecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private FragmentActionManager fragmentActionManager;
    private ArrayList<Track> tracks;

    public RecyclerViewFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentActionManager = new FragmentActionManager(getActivity().getSupportFragmentManager());

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View listTrackView = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        return listTrackView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);


        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        tracks = new ArrayList<>();
        mAdapter = new ViewRecyclerAdapter(getContext(), tracks);
        mRecyclerView.setAdapter(mAdapter);
        new TutoAndroidFranceTask().execute();


        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                selectedLyricAtPosition(position);
            }
        }));
    }


    private void showLyrics(String lyricId) {
        LyricsFragment lyricsFragmentFragment = LyricsFragment.newInstance(lyricId);
        fragmentActionManager.addFragmentToContainer(lyricsFragmentFragment, R.id.place_holder);
    }

    public void selectedLyricAtPosition(int position) {
        ViewRecyclerAdapter recyclerAdapter = (ViewRecyclerAdapter) mRecyclerView.getAdapter();
        //get the first item in recycle view
        Track mTrack = recyclerAdapter.getTract(position);
        showLyrics(mTrack.getTrackLyricId());
    }

    class TutoAndroidFranceTask extends AsyncTask<Void, String, ArrayList<Track>> {

        @Override
        protected ArrayList<Track> doInBackground(Void... params) {

            Document document;
            try {
                // need http protocol
                document = Jsoup.connect("http://www.lacoccinelle.net/").get();

                ArrayList<Track> cl = new ArrayList<>();

                Track item;

                Elements mediaElements = document.getElementsByClass("media");

                for (int i = 0; i < 50; i++) {

                    Element e = mediaElements.get(i);

                    String linkHref = e.getElementsByTag("a").first().absUrl("href");
                    String linkText = e.getElementsByTag("a").attr("title");
                    String linkImg = e.getElementsByTag("img").first().absUrl("src");
                    item = new Track();
                    item.setTrackLyricId(linkHref);
                    item.setTrackName(linkText);
                    item.setAlbumCoverUrl(linkImg);

                    cl.add(item);
                }


                return cl;

            } catch (IOException e) {
                e.printStackTrace();
                Log.e("Error", e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Track> ts) {
            super.onPostExecute(ts);

            try {
                if (tracks == null) {
                    tracks = new ArrayList<>();
                }

                tracks.addAll(ts);
                mAdapter.notifyDataSetChanged();

            } catch (Exception e) {

            }

        }
    }
}
