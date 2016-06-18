package com.example.pmakerson.fafaapplication.activity;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.pmakerson.fafaapplication.R;
import com.example.pmakerson.fafaapplication.fragment.FragmentDrawer;
import com.example.pmakerson.fafaapplication.fragment.ListDocFragment;
import com.example.pmakerson.fafaapplication.fragment.LyricsFragment;
import com.example.pmakerson.fafaapplication.fragment.MyDialogFrament;
import com.example.pmakerson.fafaapplication.fragment.RecyclerViewFragment;
import com.example.pmakerson.fafaapplication.fragment.VideoFragment;
import com.example.pmakerson.fafaapplication.manager.FragmentActionManager;
import com.example.pmakerson.fafaapplication.model.Track;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class FafaActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener, LyricsFragment.lyricsListener {

    private FragmentActionManager fragmentActionManager;

    private FragmentDrawer drawerFragment;

    private Toolbar toolbar;

    private MenuItem mSearchAction;
    private boolean isSearchOpened = false;
    private EditText edtSeach;

    private final String HTTPS = "https://";
    private final String HTTP = "http://";

    private ImageButton btnOpenInBrowser;
    private ImageButton btnFileDownloaded;
    private ImageButton btnFolderOpen;

    private ArrayList<String> arrayTextAndTranslation;


    private String TAG = FafaActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fafa);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false); //hide the title

        fragmentActionManager = new FragmentActionManager(getSupportFragmentManager());


        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.nav_view);
        drawerFragment.setUp(R.id.nav_view, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
        drawerFragment.setDrawerListener(this);


        btnOpenInBrowser = (ImageButton) findViewById(R.id.button_open_in_browser);
        btnOpenInBrowser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBrowser("google.com");
            }
        });


        btnFileDownloaded = (ImageButton) findViewById(R.id.button_file_downloaded);
        btnFileDownloaded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogAndLoadText();
            }
        });

        btnFolderOpen = (ImageButton) findViewById(R.id.button_folder_open);
        btnFolderOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showListFile();
            }
        });


        showVideoview();
    }

    private void showListFile() {
        ListDocFragment docFragment = new ListDocFragment();
        fragmentActionManager.addFragmentToContainer(docFragment, R.id.place_holder);
    }

    private void showDialogAndLoadText() {
        MyDialogFrament myDialogFrament = MyDialogFrament.newInstance("Enter name", arrayTextAndTranslation);
        myDialogFrament.show(getSupportFragmentManager(), "Dialog Fragment");

    }

    private void showTopMusics() {
        RecyclerViewFragment topListTrack = new RecyclerViewFragment();
        fragmentActionManager.addFragmentToContainer(topListTrack, R.id.place_holder);
    }


    private void showVideoview() {
        VideoFragment videoFragment = new VideoFragment();
        fragmentActionManager.addFragmentToContainer(videoFragment, R.id.place_holder);
    }

    private void showLyrics(String lyricPageId) {
        LyricsFragment lyricsFragmentFragment = LyricsFragment.newInstance(lyricPageId);
        fragmentActionManager.addFragmentToContainer(lyricsFragmentFragment, R.id.place_holder);
    }


    private void openBrowser(String url) {

        if (!url.startsWith(HTTP) && !url.startsWith(HTTPS)) {
            url = HTTP + url;
        }

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(Intent.createChooser(intent, "Chose browser"));

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (isSearchOpened) {
            handleMenuSearch();
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        mSearchAction = menu.findItem(R.id.action_search);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_search) {
            handleMenuSearch();
        }


        return super.onOptionsItemSelected(item);
    }

    private void handleMenuSearch() {

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        if (isSearchOpened) { //test if the search is open

            imm.hideSoftInputFromWindow(edtSeach.getWindowToken(), 0);
            //add the search icon in the action bar
            mSearchAction.setIcon(R.drawable.ic_search_black_48dp);

            getSupportActionBar().setDisplayShowCustomEnabled(false);  //disable a custom view inside the actionbar

            isSearchOpened = false;
        } else { //open the search entry

            // custom view in the action bar.
            getSupportActionBar().setCustomView(R.layout.item_search_bar);//add the custom view
            edtSeach = (EditText) getSupportActionBar().getCustomView().findViewById(R.id.edtSearch); //the text editor

            getSupportActionBar().setDisplayShowCustomEnabled(true); //enable the custom view to display

            //this is a listener to do a search when the user clicks on search button
            edtSeach.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        doSearch();
                        return true;
                    }
                    return false;
                }
            });


            edtSeach.requestFocus();

            //open the keyboard focused in the edtSearch
            imm.showSoftInput(edtSeach, InputMethodManager.SHOW_IMPLICIT);

            //add the close icon
            mSearchAction.setIcon(R.drawable.ic_close_black_48dp);

            isSearchOpened = true;
        }
    }


    private void doSearch() {
        //
    }


    @Override
    public void onDrawerItemSelected(Track mtrack) {
        showLyrics(mtrack.getTrackLyricId());
    }


    @Override
    public void songLyrics(HashMap<Integer, String> mapOriginalText, HashMap<Integer, String> mapTranslationText) {

        if (mapOriginalText != null) {
            arrayTextAndTranslation = new ArrayList<>();
            if (mapOriginalText.size() > 0) {
                for (int indexText = 0; indexText < mapOriginalText.size(); indexText++) {
                    String lOriginal = mapOriginalText.get(indexText);
                    String lTranslation = mapTranslationText.get(indexText);
                    arrayTextAndTranslation.add(lOriginal);
                    arrayTextAndTranslation.add(lTranslation);
                }
            }
        }


    }
}
