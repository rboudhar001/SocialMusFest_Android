package com.example.rachid.myapplication;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Rachid on 27/04/2016.
 */
public class TabPublished extends Fragment {

    private static final String TAG = "TabPublished";
    public static TabPublished tabPublished;

    private ListView mListView;
    private EventsAdapter adapter;
    private ArrayList<Event> listViewValues;

    private TextView mNoEventsView;
    //private ProgressDialog mProgressDialog;
    private ProgressBar mProgressBar;

    private MyNetwork myNetwork;

    private Handler handler;
    private Runnable runnable;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.tab_published ,container, false);

        tabPublished = this;

        Log.i(TAG, "ENTRO A TabPublished:onCreateView: CREATE_NEW_INSTANCE");

        // ----------------------------------------------------------------------------------------
        mProgressBar = (ProgressBar) view.findViewById(R.id.tabPublished_progress);
        mNoEventsView = (TextView) view.findViewById(R.id.tabPublished_text_no_events);
        mListView = (ListView) view.findViewById(R.id.tabPublished_listView_events);

        final Resources res = getResources();

        //TODO: Recoger de la DB del servidor de Junguitu los eventos.
        // TEMPORAL
        // ----------------------------------------------------------------------------------------
        /*
        Event event_1 = new Event();
        event_1.setName("Evento numero 1");
        event_1.setPlace("Vitoria");
        event_1.setFirstDay("01/01/1990");
        event_1.setLastDay("31/12/2016");

        listViewValues.add(event_1);
        */
        // ----------------------------------------------------------------------------------------

        // Connect and Get data from Server
        // ----------------------------------------------------------------------------------------
        showProgressDialog();
        myNetwork = new MyNetwork(TAG, tabPublished);
        myNetwork.Connect();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if ( myNetwork.isConnected() ) {

                    if ( myNetwork.isLoggedIn() ) {

                        listViewValues = myNetwork.getPublishedEvents(MyState.getUser().getID());
                        Log.i(TAG, "ENTRO A TabPublished:onCreateView: GET_EVENTS_SUCCESFULL");

                        myNetwork.Disconnect();
                        Log.i(TAG, "ENTRO A TabPublished:onCreateView: DISCONNECT");

                        hideProgressDialog();
                        // ----------------------------------------------------------------------------------------

                        if (listViewValues == null) {
                            Log.i(TAG, "ENTRO A TabPublished:onCreateView:listViewValues: NULL");
                            listViewValues = new ArrayList<>();
                        }

                        if (!listViewValues.isEmpty()) {

                            Log.i(TAG, "ENTRO A TabPublished:onCreateView:listViewValues: NO_EMPTY");

                            mNoEventsView.setVisibility(View.INVISIBLE);
                            mListView.setVisibility(View.VISIBLE);
                        } else {

                            Log.i(TAG, "ENTRO A TabPublished:onCreateView:listViewValues: EMPTY");

                            mNoEventsView.setVisibility(View.VISIBLE);
                            mListView.setVisibility(View.INVISIBLE);
                        }

                        adapter = new EventsAdapter(EventsActivity.activity, listViewValues, res);
                        mListView.setAdapter(adapter);

                    }
                    else {
                        Log.i(TAG, "ENTRO A TabPublished:onCreateView: NO_LOGGIN_IN");
                        Toast.makeText(tabPublished.getContext(), getString(R.string.error_could_not_view_events), Toast.LENGTH_SHORT).show();
                        hideProgressDialog();
                    }

                } else {
                    hideProgressDialog();
                    Log.i(TAG, "ENTRO A TabPublished:onCreateView: NO_CONNECT");
                    Toast.makeText(tabPublished.getContext(), getString(R.string.error_could_not_view_events), Toast.LENGTH_SHORT).show();
                    hideProgressDialog();
                }

            }
        }, 3000);
        // ----------------------------------------------------------------------------------------

        /*
        startTime(10); //10 seg
        while ( (!myNetwork.isConnected()) || (!myNetwork.isLoggedIn()) ) { // durante 10 seg
            SystemClock.sleep(1000);
        }
        endTime();

        listViewValues = myNetwork.getPublishedEvents(MyState.getUser().getID());
        myNetwork.Disconnect();
        hideProgressDialog();
        // ----------------------------------------------------------------------------------------

        if (listViewValues == null) {
            listViewValues = new ArrayList<>();
        }

        TextView mNoEventsView = (TextView) view.findViewById(R.id.tabPublished_text_no_events);
        if (!listViewValues.isEmpty()) {
            mNoEventsView.setVisibility(View.INVISIBLE);
            mListView.setVisibility(View.VISIBLE);
        }
        else {
            mNoEventsView.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.INVISIBLE);
        }

        adapter = new EventsAdapter(EventsActivity.activity, listViewValues, res);
        mListView.setAdapter(adapter);
        */

        return view;
    }

    /*****************  This function used by adapter ****************/
    public void onItemClick(int mPosition) {
        Event tempValues = listViewValues.get(mPosition);
        //Toast.makeText(EventsActivity.activity, "Event Name: " + tempValues.getName(), Toast.LENGTH_LONG).show();
        //Toast.makeText(EventsActivity.activity, "Event ID: " + tempValues.getID(), Toast.LENGTH_LONG).show();

        // TODO: Al clickear un evento, mostrarlo en la ventana de ShowEventActivity
        Intent intent = new Intent(tabPublished.getContext(), ShowEventActivity.class);
        intent.putExtra("event", tempValues); // tempValues es el evento seleccionado por el usuario
        startActivity(intent);
    }


    // *****************
    // *** FUNCIONES ***
    // *****************
    // --------------------------------------------------------------------------------------------
    //
    private void startTime(int seconds) {
        // ---------------------------------------------------------------------------------------
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                endTime();
                //Toast.makeText(activity, "Impossible connect to server", Toast.LENGTH_SHORT).show();
                // ... Aqui lo que quieres ejecutar una vez pasados los 10 segundos ...
            }
        };

        handler.postDelayed(runnable, seconds); // 10 seg
        // ---------------------------------------------------------------------------------------
    }

    //
    private void endTime() {
        handler.removeCallbacks(runnable);
    }

    //
    private void showProgressDialog() {
        mProgressBar.setVisibility(View.VISIBLE);
        mNoEventsView.setVisibility(View.INVISIBLE);
        mListView.setVisibility(View.INVISIBLE);
    }

    //
    private void hideProgressDialog() {
        mProgressBar.setVisibility(View.INVISIBLE);
    }
    // --------------------------------------------------------------------------------------------
}