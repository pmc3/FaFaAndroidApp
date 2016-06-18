package com.example.pmakerson.fafaapplication.fragment;

import android.content.Context;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.example.pmakerson.fafaapplication.R;
import com.example.pmakerson.fafaapplication.adapter.RecyclerAdapter;
import com.example.pmakerson.fafaapplication.model.Track;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by greg on 04/02/2016.
 */
public class FragmentDrawer extends Fragment {

    private String TAG = FragmentDrawer.class.getSimpleName();

    private RecyclerView recyclerView;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private RecyclerAdapter adapter;
    private View containerView;
    private FragmentDrawerListener drawerListener;
    private List<Track> tracks;

    public FragmentDrawer() {

    }


    public void setDrawerListener(FragmentDrawerListener listener) {
        this.drawerListener = listener;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tracks = new ArrayList<>();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_navigation_drawer, container, false);

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        /*SETUP RECYCLEVIEW*/
        recyclerView = (RecyclerView) view.findViewById(R.id.drawerList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new RecyclerAdapter(getContext(), tracks);
        recyclerView.setAdapter(adapter);

        new lyricsTask().execute();

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                selectedLyricAtPosition(position);
                mDrawerLayout.closeDrawer(containerView);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


    }


    public void selectedLyricAtPosition(int position) {
        Track mTrack = adapter.getTract(position);
        drawerListener.onDrawerItemSelected(mTrack);
    }


    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {
        containerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;


        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                toolbar.setAlpha(1 - slideOffset / 2);
            }

        };

        mDrawerLayout.addDrawerListener(mDrawerToggle);
        // Defer code dependent on restoration of previous instance state.
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });


    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    class lyricsTask extends AsyncTask<Void, String, ArrayList<Track>> {

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
                //showItemInDrawer(tracks);
                adapter.notifyDataSetChanged();

                //selectedLyricAtPosition(0);

            } catch (Exception e) {

            }

        }
    }


    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildAdapterPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }


    }

    public interface FragmentDrawerListener {
        void onDrawerItemSelected(Track mtrack);
    }


}
