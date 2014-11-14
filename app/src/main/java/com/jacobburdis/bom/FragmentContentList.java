package com.jacobburdis.bom;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageButton;

/**
 * Created by joshua on 5/1/14.
 */
public class FragmentContentList extends Fragment {

    ExpandableListView listView;
    private String[] favoriteList;
    private String[] parentList;
    private String[][] childList;
    private String[][] conditionsList;
    private String[][] blessingsList;
    private ImageButton imageButton;

    public FragmentContentList() {
        // Required empty public constructor
    }

    public FragmentContentList(String[] parent, String[][] child, String[][] conditions,
                               String[][] blessings, String[] favorite) {
        this.parentList = parent;
        this.childList = child;
        this.conditionsList = conditions;
        this.blessingsList = blessings;
        this.favoriteList = favorite;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_content, container, false);
        listView = (ExpandableListView) view.findViewById(R.id.fragmentAListView);
        listView.setAdapter(new contentListAdapter(getActivity(), parentList, childList,
                conditionsList, blessingsList, favoriteList));
        return view;
    }

}