package com.amiecart.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import com.amiecart.adapter.LatestAdapter;
import com.amiecart.util.API;
import com.amiecart.util.Constant;
import com.amiecart.lifedrop.MyApplication;
import com.example.placefinderapp.R;
import com.amiecart.lifedrop.SearchActivity;
import com.amiecart.item.ItemPlaceList;
import com.amiecart.util.JsonUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class LatestFragment extends Fragment {

    ScrollView mScrollView;
    ProgressBar mProgressBar;
    ArrayList<ItemPlaceList> mCategoryList;
    RecyclerView mCategoryView;
    LatestAdapter latestAdapter;
    MyApplication MyApp;
    private LinearLayout lyt_not_found;
    int j = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_category, container, false);

        MyApp = MyApplication.getAppInstance();
        mCategoryList = new ArrayList<>();

        mScrollView = rootView.findViewById(R.id.scrollView);
        mProgressBar = rootView.findViewById(R.id.progressBar);
        mCategoryView = rootView.findViewById(R.id.vertical_courses_list);
        lyt_not_found = rootView.findViewById(R.id.lyt_not_found);

        mCategoryView.setHasFixedSize(true);
        mCategoryView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        mCategoryView.setFocusable(false);
        mCategoryView.setNestedScrollingEnabled(false);

        JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API());
        jsObj.addProperty("method_name", "get_latest_places");
        jsObj.addProperty("user_id", MyApplication.getAppInstance().getUserId());
        if (JsonUtils.isNetworkAvailable(requireActivity())) {
            new getLatestData(API.toBase64(jsObj.toString())).execute(Constant.API_URL);
        }


        setHasOptionsMenu(true);
        return rootView;
    }

    @SuppressLint("StaticFieldLeak")
    private class getLatestData extends AsyncTask<String, Void, String> {

        String base64;

        private getLatestData(String base64) {
            this.base64 = base64;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress(true);
        }

        @Override
        protected String doInBackground(String... params) {
            return JsonUtils.getJSONString(params[0], base64);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            showProgress(false);
            if (null == result || result.length() == 0) {
                lyt_not_found.setVisibility(View.VISIBLE);
            } else {

                try {
                    JSONObject mainJson = new JSONObject(result);
                    JSONArray jsonArray = mainJson.getJSONArray(Constant.CATEGORY_ARRAY_NAME);
                    JSONObject objJson;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        objJson = jsonArray.getJSONObject(i);
                        if (objJson.has("status")) {
                            lyt_not_found.setVisibility(View.VISIBLE);
                        } else {
                            ItemPlaceList objItem = new ItemPlaceList();
                            objItem.setPlaceId(objJson.getString(Constant.LISTING_H_ID));
                            objItem.setPlaceCatId(objJson.getString(Constant.LISTING_H_CAT_ID));
                            objItem.setPlaceName(objJson.getString(Constant.LISTING_H_NAME));
                            objItem.setPlaceImage(objJson.getString(Constant.LISTING_H_IMAGE));
                            objItem.setPlaceVideo(objJson.getString(Constant.LISTING_H_VIDEO));
                            objItem.setPlaceDescription(objJson.getString(Constant.LISTING_H_DES));
                            objItem.setPlaceAddress(objJson.getString(Constant.LISTING_H_ADDRESS));
                            objItem.setPlaceEmail(objJson.getString(Constant.LISTING_H_EMAIL));
                            objItem.setPlacePhone(objJson.getString(Constant.LISTING_H_PHONE));
                            objItem.setPlaceWebsite(objJson.getString(Constant.LISTING_H_WEBSITE));
                            objItem.setPlaceLatitude(objJson.getString(Constant.LISTING_H_MAP_LATITUDE));
                            objItem.setPlaceLongitude(objJson.getString(Constant.LISTING_H_MAP_LONGITUDE));
                            objItem.setPlaceRateAvg(objJson.getString(Constant.LISTING_H_RATING_AVG));
                            objItem.setPlaceRateTotal(objJson.getString(Constant.LISTING_H_RATING_TOTAL));
                            objItem.setFavourite(objJson.getBoolean(Constant.LISTING_H_FAV));

                            if (Constant.SAVE_ADS_NATIVE_ON_OFF.equals("true")) {
                                if (j % Integer.parseInt(Constant.SAVE_NATIVE_CLICK_OTHER) == 0) {
                                    mCategoryList.add( null);
                                    j++;
                                }
                            }
                            mCategoryList.add(objItem);
                            j++;

                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                setResult();
            }
        }
    }

    private void setResult() {
        if (getActivity() != null) {
            latestAdapter = new LatestAdapter(getActivity(), mCategoryList);
            mCategoryView.setAdapter(latestAdapter);

            if (latestAdapter.getItemCount() == 0) {
                lyt_not_found.setVisibility(View.VISIBLE);
            } else {
                lyt_not_found.setVisibility(View.GONE);
            }

        }
    }

    private void showProgress(boolean show) {
        if (show) {
            mProgressBar.setVisibility(View.VISIBLE);
            mCategoryView.setVisibility(View.GONE);
            lyt_not_found.setVisibility(View.GONE);
        } else {
            mProgressBar.setVisibility(View.GONE);
            mCategoryView.setVisibility(View.VISIBLE);
        }
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_search, menu);

        final SearchView searchView = (SearchView) menu.findItem(R.id.search)
                .getActionView();

        final MenuItem searchMenuItem = menu.findItem(R.id.search);
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if (!hasFocus) {
                    searchMenuItem.collapseActionView();
                    searchView.setQuery("", false);
                }
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextChange(String newText) {
                // TODO Auto-generated method stub

                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                intent.putExtra("search", query);
                startActivity(intent);
                searchView.clearFocus();
                return false;
            }
        });

    }

}
