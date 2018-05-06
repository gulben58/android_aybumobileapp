package com.example.gulbe.homework2;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by gulbe on 26.04.2018.
 */
public class FoodList extends Fragment {
    public TextView foodList;
    public ImageView image;
    ArrayList<String> list;
    String img;
    public ProgressDialog dialog;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.food_list, container, false);
        foodList=(TextView) view.findViewById(R.id.food);
        image= (ImageView) view.findViewById(R.id.image);
        list=new ArrayList<>();
        new Food().execute();
        return view;
    }

    private class Food extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog=new ProgressDialog(getContext());
            dialog.setMessage("Loading");
            dialog.show();

        }


        @Override
        protected Void doInBackground(Void... voids) {

            try {
                URL url=new URL("http://ybu.edu.tr/sks");
                Document doc= Jsoup.parse(url,70000);
                Element table=doc.select("table").get(0);
                Elements rows=table.select("tr");

                Elements imgsrc=rows.select("img[src]");
               img="http://ybu.edu.tr"+imgsrc.attr("src");


                for(int i=1;i<rows.size();i++){

                    Element row = rows.get(i);
                    Elements tds = row.select("td");

                    list.add(tds.get(0).text());


                }



            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
           dialog.dismiss();


            String food="";
            for(int i=0;i<list.size();i++){
                food=food+"\n"+list.get(i).toString();
            }
            foodList.setText(food);
            Picasso.with(getContext()).load(img).into(image);

        }
    }
    @Override public void onStop() {
        super.onStop();
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
       // list.clear();
    }

}
