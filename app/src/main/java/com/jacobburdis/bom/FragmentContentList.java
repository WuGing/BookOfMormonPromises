package com.jacobburdis.bom;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ExpandableListView;
import android.widget.TextView;

/**
 * Created by joshua on 5/1/14.
 */
public class FragmentContentList extends ActionBarActivity {

    ExpandableListView listView;
    TextView textView;
    private int titleHelper;
    private String header;
    private String[] favoriteHelper;
    private String[] favoriteList;
    private String[] parentList;
    private String[][] childList;
    private String[][] conditionsList;
    private String[][] blessingsList;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_content);

        UnpackageBundle();

        setActionBarTitle();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textView = (TextView) findViewById(R.id.list_header);
        textView.setText(header);

        listView = (ExpandableListView) findViewById(R.id.fragmentAListView);
        listView.setAdapter(new contentListAdapter(this, parentList, childList,
                conditionsList, blessingsList, favoriteList, favoriteHelper));
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    void UnpackageBundle(){

        Bundle arguments = getIntent().getExtras();

        this.parentList = arguments.getStringArray("parentList");
        this.childList = ArrayReceived(arguments, "childList");
        this.conditionsList = ArrayReceived(arguments, "conditionsList");
        this.blessingsList = ArrayReceived(arguments, "blessingsList");
        this.favoriteList = arguments.getStringArray("favoriteList");
        this.favoriteHelper = arguments.getStringArray("favoriteHelper");
        this.header = arguments.getString("header");
        this.titleHelper = arguments.getInt("titleHelper");

    }

    public String[][] ArrayReceived(Bundle receivedArguments, String whichList) {

        String[] objects = receivedArguments.getStringArray(whichList);

        String[][] receivedArray = new String[objects.length][objects.length];

        for (int i=0; i < objects.length; i++)
        {
            receivedArray[i][0] = "" + objects[i];
        }

        return receivedArray;

    }

    void setActionBarTitle(){
        switch (titleHelper){
            case 0:
                setTitle("Favorites");
                break;
            case 1:
                setTitle("What I Promise to Do");
                break;
            case 2:
                setTitle("What God Promises Me");
                break;
            case 3:
                setTitle("Promises by Book in Order");
                break;
        }
    }

}