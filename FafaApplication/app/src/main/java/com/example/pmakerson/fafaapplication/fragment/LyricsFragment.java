package com.example.pmakerson.fafaapplication.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pmakerson.fafaapplication.R;
import com.example.pmakerson.fafaapplication.util.Const;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by P Makerson on 04/04/2016.
 */
public class LyricsFragment extends Fragment {
    private String TAG = LyricsFragment.class.getSimpleName();
    private String trackLyricId;
    private LinearLayout containerLyricsLayout;
    private static String PARAM_TRACK_LYRIC_ID = "PARAM_TRACK_LYRIC_ID";
    private LayoutInflater inflater;

    private lyricsListener mLyricListener;

    public LyricsFragment() {
    }


    public static LyricsFragment newInstance(String trackLyricId) {
        LyricsFragment lyricsFragment = new LyricsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_TRACK_LYRIC_ID, trackLyricId);
        lyricsFragment.setArguments(bundle);
        return lyricsFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mLyricListener = (lyricsListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement LyricsListener");
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
        View lyricView = inflater.inflate(R.layout.fragment_lyrics, container, false);
        return lyricView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        containerLyricsLayout = (LinearLayout) view.findViewById(R.id.lyric_layout);

        if (getArguments() != null) {
            trackLyricId = getArguments().getString(PARAM_TRACK_LYRIC_ID);
            new lyricsTask().execute(trackLyricId);
        }
    }


    class lyricsTask extends AsyncTask<String, String, Void> {

        @Override
        protected Void doInBackground(String... params) {

            Document document;

            Elements lyricsOriginalTextElements = null;
            Elements lyricsTranslationTextElements = null;

            HashMap<Integer, String> mapOriginalText = new HashMap<>();
            HashMap<Integer, String> mapTranslationText = new HashMap<>();

            int countOriginalText = 0;
            int countTranslationText = 0;
            int countText = 0;
            int countP = 0;


            try {
                // need http protocol
                document = Jsoup.connect(params[0]).get();

                Elements lyricsElements = document.getElementsByTag("p");

                //si la liste des paragraphes n'est pas null
                if (lyricsElements != null) {

                    for (int indexP = 0; indexP < lyricsElements.size(); indexP++) {

                        lyricsOriginalTextElements = lyricsElements.get(indexP).getElementsByTag("strong");

                        if (lyricsOriginalTextElements != null) {

                            if (lyricsOriginalTextElements.size() > 0) {

                                lyricsTranslationTextElements = lyricsElements.get(indexP).getElementsByTag("em");

                                for (int indexLyric = 0; indexLyric < lyricsOriginalTextElements.size(); indexLyric++) {

                                    String lOriginal = lyricsOriginalTextElements.get(indexLyric).text();
                                    mapOriginalText.put(countText, Const.ORIGINAL_LYRIC_PREFIX + lOriginal);

                                    if (lyricsTranslationTextElements != null) {
                                        if (lyricsTranslationTextElements.size() > 0) {
                                            if (indexLyric < lyricsTranslationTextElements.size()) {

                                                String lTranslation = lyricsTranslationTextElements.get(indexLyric).text();

                                                mapTranslationText.put(countText, Const.TRANSLATION_LYRIC_PREFIX + lTranslation);
                                            }
                                        }
                                    }

                                    countText++;
                                }
                            }

                        }


                    }
                }
                /*for (Element e : lyricsElements) {

                    lyricsOriginalTextElements = e.getElementsByTag("strong");
                    if (lyricsOriginalTextElements != null) {
                        if (lyricsOriginalTextElements.size() > 0) {
                            for (Element originalLyrics : lyricsOriginalTextElements) {
                                String lOriginal = originalLyrics.text();
                                mapOriginalText.put(countOriginalText, lOriginal);
                                countOriginalText++;
                            }
                        }
                    }

                    lyricsTranslationTextElements = e.getElementsByTag("em");
                    if (lyricsTranslationTextElements != null) {
                        if (lyricsTranslationTextElements.size() > 0) {
                            for (Element translationLyrics : lyricsTranslationTextElements) {
                                String lTranslation = translationLyrics.text();
                                mapTranslationText.put(countTranslationText, lTranslation);
                                countTranslationText++;
                            }
                        }
                    }

                }*/

                if (mapOriginalText.size() > 0) {
                    for (int indexText = 0; indexText < mapOriginalText.size(); indexText++) {
                        String originalText = mapOriginalText.get(indexText);
                        String translationText = mapTranslationText.get(indexText);
                        publishProgress(originalText, translationText);
                    }
                }


                mLyricListener.songLyrics(mapOriginalText, mapTranslationText);

            } catch (IOException e) {
                e.printStackTrace();
                Log.e("Error", e.getMessage());
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

            String orginalText = values[0];
            String translationText = values[1];

            if (orginalText != null) {
                if (!orginalText.equals("")) {
                    containerLyricsLayout.addView(compileSongs(orginalText, translationText));
                }
            }
        }

    }


    private View compileSongs(String orginalText, String textTranslate) {
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.item_lyrics, null);

        String orginalTextNoPrefix = removeStringPrefix(orginalText, Const.ORIGINAL_LYRIC_PREFIX);
        TextView textViewlyric = (TextView) layout.findViewById(R.id.textViewLyric);
        textViewlyric.setText(orginalTextNoPrefix);

        if (textTranslate != null) {
            String textTranslateNoPrefix = removeStringPrefix(textTranslate, Const.TRANSLATION_LYRIC_PREFIX);
            TextView textViewlyricTranslate = (TextView) layout.findViewById(R.id.textViewLyricTranslate);
            textViewlyricTranslate.setText(textTranslateNoPrefix);
        }


        return layout;
    }

    private String removeStringPrefix(String lyrics, String prefix) {
        String lyricsNoPrefix = lyrics.replace(prefix, "");
        return lyricsNoPrefix;
    }


    public interface lyricsListener {
        void songLyrics(HashMap<Integer, String> mapOriginalText, HashMap<Integer, String> mapTranslationText);
    }

}

