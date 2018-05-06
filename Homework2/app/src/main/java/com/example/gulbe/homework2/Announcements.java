package com.example.gulbe.homework2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gulbe on 26.04.2018.
 */

public class Announcements extends Fragment {
    public ListView listView;
    ArrayList<String> announcements;
    ArrayList<String> urls;
    public ProgressDialog dialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.announcements, container, false);
        listView =(ListView) view.findViewById(R.id.lv);
        announcements=new ArrayList<>();
        urls=new ArrayList<>();

        new Announcement().execute();
        return view;
    }

    private class Announcement extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog=new ProgressDialog(getContext());
            dialog.setMessage("Loading");
            dialog.show();

        }


        @Override
        protected Void doInBackground(Void... voids) {

            String url= "http://www.ybu.edu.tr/muhendislik/bilgisayar/";
            try {

                Document doc= Jsoup.connect(url).get();
                Element element=doc.select("div.caContent").first();
                Elements rows=element.select("div.cncItem");

                for(int i=0;i<rows.size();i++){
                    Element row = rows.get(i);
                    Elements y=row.select("a");

                    announcements.add(y.get(0).text());
                    String x=rows.select("a").attr("href");
                    urls.add(x);


                }
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            dialog.dismiss();

            ArrayAdapter adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, announcements);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {


                    String[] a = new String[urls.size()];
                    for(int i=0;i<urls.size();i++){
                        a[i]="http://www.ybu.edu.tr/muhendislik/bilgisayar/"+urls.get(i).toString();
                    }

                    Uri uri = Uri.parse(a[position]);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);






                }
            });


        }
    }

    @Override public void onStop() {
        super.onStop();
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
        //announcements.clear();
    }

}