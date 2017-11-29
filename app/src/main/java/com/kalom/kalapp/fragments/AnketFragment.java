package com.kalom.kalapp.fragments;


import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.kalom.kalapp.MainActivity;
import com.kalom.kalapp.R;
import com.kalom.kalapp.classes.Anket;
import com.kalom.kalapp.classes.AnketAdapter;
import com.kalom.kalapp.classes.Config;
import com.kalom.kalapp.classes.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class AnketFragment extends Fragment {

    private AnketAdapter adapter=null;
    private AnketInfo us;
    private ListView listemiz;
    private View list_footer_view;
    private CoordinatorLayout coordinatorLayout;
    private SwipeRefreshLayout swip;

    private int str=0;
    private int fnsh=Config.anket_load_one_time;
    public boolean refreshed=false;

    final List<Anket> anketler = new ArrayList<>();

    public static AnketFragment newInstance() {
        return new AnketFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

         final View rootView = inflater.inflate(R.layout.anketfragment_layout,
                container, false);

          listemiz=rootView.findViewById(R.id.listView1);
          list_footer_view = ((LayoutInflater) getContext().getSystemService(MainActivity.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.list_footer, null, false);

            swip=rootView.findViewById(R.id.swiperefresh);

        listemiz.setSmoothScrollbarEnabled(true);

        adapter=new AnketAdapter(getActivity(), anketler,getContext());
        listemiz.setAdapter(adapter);


        us=new AnketInfo();
        us.execute();




        swip.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        refresh();
                        swip.setRefreshing(false);

                    }
                }
        );




        listemiz.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {


                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                        && (listemiz.getLastVisiblePosition() - listemiz.getHeaderViewsCount() -
                        listemiz.getFooterViewsCount()) >= (adapter.getCount()-1)) {

                    us=new AnketInfo();
                    us.execute();

                }

                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                        &&listemiz.getFirstVisiblePosition() == 0 && refreshed) {

                    refresh();
                    listemiz.setSelection(0);
                    refreshed=false;
                }


            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });


        return rootView;

    }

    protected void showloader(){
        listemiz.addFooterView(list_footer_view);
        listemiz.setSelection(listemiz.getLastVisiblePosition());
        listemiz.setEnabled(false);
        swip.setEnabled(false);



    }

    protected void hideloader(){
        listemiz.removeFooterView(list_footer_view);
        listemiz.setEnabled(true);
        swip.setEnabled(true);


    }

    public void scrolltoTop(){

        listemiz.smoothScrollToPosition(0);

    }

    public void refresh(){
        anketler.clear();
        adapter.notifyDataSetChanged();
        us=new AnketInfo();
        str=0;
        fnsh=Config.anket_load_one_time;
        us.execute();
    }



    @SuppressLint("StaticFieldLeak")
    private class AnketInfo extends AsyncTask<Void, String,String> {

        @Override
        protected void onPreExecute(){

            showloader();

        }

        @Override
        protected String doInBackground(Void... params) {


            // Simulate network access.
            JSONParser js=new JSONParser();

            try{

                String api_call= Config.api_server+"include/duyuru.php?s="+str +"&f="+fnsh;
                str=fnsh;
                fnsh+=Config.anket_load_one_time;
                return js.JsonString(api_call);



            }catch(IOException | JSONException e){
                e.getMessage();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result){
           hideloader();
            if(result==null){

                Toast.makeText(getContext(),"INTERNET YOK",Toast.LENGTH_LONG).show();

                return;
            }

            try {

                JSONArray ar = new JSONArray(result);


                for(int i = 0; i< ar.length(); ++i){
                    try {
                        JSONObject obj = (JSONObject) ar.get(i);
                        anketler.add(new Anket(
                                obj.get("baslik").toString(),
                                obj.get("prebaslik").toString(),
                                obj.get("content").toString(),
                                "http://10.0.2.2/logo.jpg",
                                1
                        ));


                    } catch (JSONException e) {
                        e.printStackTrace();

                        return;
                    }

                }

                adapter.notifyDataSetChanged();


            } catch (JSONException e) {
                e.printStackTrace();
            }



        }






    }

}