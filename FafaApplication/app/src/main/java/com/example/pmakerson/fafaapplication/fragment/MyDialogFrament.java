package com.example.pmakerson.fafaapplication.fragment;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.pmakerson.fafaapplication.R;
import com.example.pmakerson.fafaapplication.util.Const;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


/**
 * Created by P Makerson on 20/04/2016.
 */
public class MyDialogFrament extends DialogFragment {
    private static String ARG_TEXT_AND_TRANSLATION = "ARG_TEXT_AND_TRANSLATION";
    private static String ARG_DIALOG_TITLE = "ARG_DIALOG_TITLE";
    private String nameGiven;
    private EditText editText;

    private final int READ_BLOCK_SIZE = 100;

    private Button btnGenPDF;

    public static MyDialogFrament newInstance(String title, ArrayList<String> lyricsTextAndTranslation) {
        MyDialogFrament dialog = new MyDialogFrament();
        Bundle args = new Bundle();
        args.putString(ARG_DIALOG_TITLE, title);
        args.putStringArrayList(ARG_TEXT_AND_TRANSLATION, lyricsTextAndTranslation);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View dialogView = inflater.inflate(R.layout.fragment_dialog, container, false);

        editText = (EditText) dialogView.findViewById(R.id.text_file_name);
        btnGenPDF = (Button) dialogView.findViewById(R.id.button_print);

        nameGiven = editText.getText().toString();

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                enableBtnCreatePdf();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        if (getArguments() != null) {
            //get the dialog title
            getDialog().setTitle(getArguments().getString(ARG_DIALOG_TITLE));
        }


        return dialogView;
    }

    private void enableBtnCreatePdf() {
        int editSize = editText.getText().toString().length();

        if (editSize > 0) {
            nameGiven = editText.getText().toString();
            btnGenPDF.setEnabled(true);
            btnGenPDF.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    createPDF();
                    getDialog().dismiss();
                }
            });
        } else {
            btnGenPDF.setEnabled(false);
        }
    }


    public void createPDF() {
        Document doc = new Document();

        try {

            File dir = getContext().getDir(Const.FOLDER_NAME, getContext().MODE_PRIVATE);
            File file = new File(dir, nameGiven.trim() + ".pdf");
            FileOutputStream fOut = new FileOutputStream(file);
            PdfWriter.getInstance(doc, fOut);

            // open the document
            doc.open();
         /*   ByteArrayOutputStream stream = new ByteArrayOutputStream();
            Bitmap bitmap = BitmapFactory.decodeResource(getBaseContext()
                    .getResources(), R.drawable.androtuto);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            Image myImg = Image.getInstance(stream.toByteArray());
            myImg.setAlignment(Image.MIDDLE);

            // add image to document
            doc.add(myImg);
*/
            if (getArguments() != null) {
                //get the dialog title
                ArrayList<String> lyrisArray = getArguments().getStringArrayList(ARG_TEXT_AND_TRANSLATION);

                for (String text : lyrisArray) {

                    if (text != null) {

                        if (text.contains(Const.ORIGINAL_LYRIC_PREFIX)) {

                            String originalText = removeStringPrefix(text, Const.ORIGINAL_LYRIC_PREFIX);
                            Font paraFont = new Font(Font.TIMES_ROMAN, 14.0f, Font.BOLD, harmony.java.awt.Color.BLUE);
                            Paragraph p1 = new Paragraph(originalText, paraFont);

                            p1.setAlignment(Paragraph.ALIGN_CENTER);
                            //p1.setFont(paraFont);
                            // add paragraph to document
                            doc.add(p1);

                        } else {

                            String translationText = removeStringPrefix(text, Const.TRANSLATION_LYRIC_PREFIX);
                            Paragraph p2 = new Paragraph(translationText);
                            Font paraFont = new Font(Font.COURIER);
                            p2.setAlignment(Paragraph.ALIGN_CENTER);
                            p2.setFont(paraFont);
                            // add paragraph to document
                            doc.add(p2);
                        }
                    }

                }
            }


           /* Paragraph p2 = new Paragraph("Bonjour Android Tuto");
            Font paraFont2 = new Font(Font.COURIER, 14.0f, Color.GREEN);
            p2.setAlignment(Paragraph.ALIGN_CENTER);
            p2.setFont(paraFont2);

            doc.add(p2);*/

          /*  stream = new ByteArrayOutputStream();
            bitmap = BitmapFactory.decodeResource(getBaseContext()
                    .getResources(), R.drawable.android);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            myImg = Image.getInstance(stream.toByteArray());
            myImg.setAlignment(Image.MIDDLE);

            // add image to document
            doc.add(myImg);*/

            // set footer
            Phrase footerText = new Phrase("Pied de page ");
            HeaderFooter pdfFooter = new HeaderFooter(footerText, false);
            doc.setFooter(pdfFooter);

        } catch (DocumentException de) {
            Log.e("PDFCreator", "DocumentException:" + de);
        } catch (IOException e) {
            Log.e("PDFCreator", "ioException:" + e);
        } finally {
            doc.close();
        }

    }

    private String removeStringPrefix(String lyrics, String prefix) {
        String lyricsNoPrefix = lyrics.replace(prefix, "");
        return lyricsNoPrefix;
    }


}
