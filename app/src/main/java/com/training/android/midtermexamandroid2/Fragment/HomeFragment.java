package com.training.android.midtermexamandroid2.Fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.training.android.midtermexamandroid2.Model.Album;
import com.training.android.midtermexamandroid2.AlbumAdapter;
import com.training.android.midtermexamandroid2.Handler.HttpHandler;
import com.training.android.midtermexamandroid2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    String url;
    ArrayList<Album> data = new ArrayList<>();
    String ImageUrl;
    ProgressDialog pd;
    AlbumAdapter mAdapter;
    RecyclerView mRecycler;
    EditText mSearch;
    TextView mTvString;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mRecycler = (RecyclerView) view.findViewById(R.id.rvCards);
        mSearch = (EditText) view.findViewById(R.id.etSearch);
        mTvString = (TextView) view.findViewById(R.id.tvString);
        final WifiManager wifi = (WifiManager) getContext().getSystemService(Context.WIFI_SERVICE);

        setHasOptionsMenu(true);

        mSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    String search = mSearch.getText().toString();
                    if (!wifi.isWifiEnabled()) {
                        Toast.makeText(getContext(), "Connect to a network", Toast.LENGTH_SHORT).show();
                    } else {
                        if (search.isEmpty()) {
                            Toast.makeText(getContext(), "Invalid search", Toast.LENGTH_SHORT).show();
                        } else {
                            url = "http://ws.audioscrobbler.com/2.0/?method=album.search&album="
                                    + search + "&api_key=b6ff6fe3192f66735629799c2f4dd988&format=json";
                            new GetList().execute();
                        }
                    }
                    return true;
                }
                return false;
            }
        });


        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void clearAdapter() {
        int size = this.data.size();
        data.clear();
        mAdapter.notifyItemRangeRemoved(0, size);
        mRecycler.setAdapter(new AlbumAdapter(new ArrayList<Album>()));
        mSearch.setText("");
        mTvString.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_clear) {
            clearAdapter();
            Toast.makeText(getContext(), "Data Erased", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    private class GetList extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(getContext());
            pd.setMessage("Data loading...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            //Initialization of JSON variables
            data = new ArrayList<>();
            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeServiceCall(url);

            //Fetches data from JSON
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONObject results = jsonObj.getJSONObject("results");
                    JSONObject albumMatches = results.getJSONObject("albummatches");
                    JSONArray albumArray = albumMatches.getJSONArray("album");

                    for (int i = 0; i < albumArray.length(); i++) {
                        JSONObject li = albumArray.getJSONObject(i);

                        String name = li.getString("name");
                        String artist = li.getString("artist");

                        //FOR IMAGE
                        JSONArray imgArray = li.getJSONArray("image");
                        for (int j = 0; j < imgArray.length(); j++) {
                            JSONObject imgLi = imgArray.getJSONObject(2);
                            ImageUrl = imgLi.getString("#text");
                        }

                        Album current = new Album(name
                                , artist, ImageUrl);
                        data.add(current);
                    }
                } catch (final JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getContext(), "Connect to a network", Toast.LENGTH_SHORT).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (pd.isShowing())
                pd.dismiss();

            if (!data.isEmpty())
                mTvString.setVisibility(View.INVISIBLE);

            mAdapter = new AlbumAdapter(data);
            mRecycler.setAdapter(mAdapter);
            mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
    }
}
