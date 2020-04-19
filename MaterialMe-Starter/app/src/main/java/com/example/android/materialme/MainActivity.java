/*
 * Copyright (C) 2018 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.materialme;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/***
 * Main Activity for the Material Me app, a mock sports news application
 * with poor design choices.
 */
public class MainActivity extends AppCompatActivity {

    // Member variables.
    private RecyclerView mRecyclerView;
    private ArrayList<Sport> mSportsData;
    private SportsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the RecyclerView.
        mRecyclerView = findViewById(R.id.recyclerView);

        // Set the Layout Manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the ArrayList that will contain the data.
        mSportsData = new ArrayList<>();

        // Initialize the adapter and set it to the RecyclerView.
        mAdapter = new SportsAdapter(this, mSportsData);
        mRecyclerView.setAdapter(mAdapter);

        // Get the data.
        initializeData(savedInstanceState);
        // Item touch helper to delete items
        helper.attachToRecyclerView(mRecyclerView);
    }

    /**
     * Item touch helper
     */
    ItemTouchHelper helper=new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP|
            ItemTouchHelper.DOWN|ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                              RecyclerView.ViewHolder target) {
            int from=viewHolder.getAdapterPosition();
            int to=target.getAdapterPosition();
            Collections.swap(mSportsData,from,to);
            mAdapter.notifyItemMoved(from,to);
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            mSportsData.remove(viewHolder.getAdapterPosition());
            mAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
        }
    });
    /**
     * Initialize the sports data from resources.
     * @param savedInstanceState
     */
    private void initializeData(Bundle savedInstanceState) {
        if(savedInstanceState!=null && savedInstanceState.containsKey("sports")){
            mSportsData.clear();
            ArrayList<Sport>tempData= (ArrayList<Sport>) savedInstanceState.getSerializable("sports");
            mSportsData.addAll(tempData);
        }else{
            // Get the resources from the XML file.
            String[] sportsList = getResources().getStringArray(R.array.sports_titles);
            String[] sportsInfo = getResources().getStringArray(R.array.sports_info);
            TypedArray sportsImagesResource=getResources().obtainTypedArray(R.array.sports_images);

            // Clear the existing data (to avoid duplication).
            mSportsData.clear();

            // Create the ArrayList of Sports objects with titles and
            // information about each sport.
            for(int i=0;i<sportsList.length;i++){
                mSportsData.add(new Sport(sportsList[i],sportsInfo[i],sportsImagesResource.getResourceId(i,0)));
            }
            sportsImagesResource.recycle();
        }
        // Notify the adapter of the change.
        mAdapter.notifyDataSetChanged();
    }

    public void resetSports(View view) {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("sports",mSportsData);
    }
}
