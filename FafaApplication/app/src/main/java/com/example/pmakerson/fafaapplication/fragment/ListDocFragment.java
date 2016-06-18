package com.example.pmakerson.fafaapplication.fragment;


import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.pmakerson.fafaapplication.R;
import com.example.pmakerson.fafaapplication.util.Const;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by P Makerson on 26/04/2016.
 */
public class ListDocFragment extends Fragment {

    // List view for showing pdf files
    ListView pdfList;

    //Adapter to list view
    ArrayAdapter<String> adapter;
    // array of pdf files
    File filelist;

    String openedPdfFileName;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewList = inflater.inflate(R.layout.fragment_list_view, container, false);
        return viewList;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pdfList = (ListView) view.findViewById(R.id.my_list);
        pdfList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //On Clicking list item, Render Pdf file corresponding to filePath
                openedPdfFileName = adapter.getItem(position);
                openRenderer(openedPdfFileName);
            }
        });

        getListFile();

    }


    private ArrayAdapter<String> getListFile() {
        File mydir = getContext().getDir(Const.FOLDER_NAME, Context.MODE_PRIVATE);
        filelist = mydir.getAbsoluteFile();
        ArrayList<String> fileNameList = new ArrayList<>();

        if (filelist != null && filelist.length() > 0) {

            for (String list : filelist.list()) {
                fileNameList.add(list);
            }

            adapter = new ArrayAdapter<>(getContext(),
                    R.layout.item_list, fileNameList);

            pdfList.setAdapter(adapter);

        } else {
            Toast.makeText(getContext(),
                    "No pdf file found, Please create new Pdf file",
                    Toast.LENGTH_LONG).show();
        }
        return adapter;
    }


    /**
     * API for initializing file descriptor and pdf renderer after selecting pdf from list
     */
    private void openRenderer(String fileName) {
        Uri internalUri = Uri.parse("content://com.sample.provider/" + fileName);
        viewPdf(internalUri);

    }


    private void viewPdf(Uri internalUri) {
        Intent intent;
        intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(internalUri, "application/pdf");

        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("No Application Found");
            builder.setMessage("Download one from Android Market?");
            builder.setPositiveButton("Yes, Please",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent marketIntent = new Intent(Intent.ACTION_VIEW);
                            marketIntent.setData(Uri.parse("market://details?id=com.adobe.reader"));
                            startActivity(marketIntent);
                        }
                    });
            builder.setNegativeButton("No, Thanks", null);
            builder.create().show();
            Log.v("", "Exception : " + e);
        }


    }
}