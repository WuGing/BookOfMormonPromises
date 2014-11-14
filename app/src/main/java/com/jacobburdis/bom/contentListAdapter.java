package com.jacobburdis.bom;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by joshua on 4/30/14.
 * going to need different adapters for each view. Likely. Might have
 * to put some more thought into it. But will need to have an adapter
 * to make the list of labels, and then will need an adapter that
 * lists the references and the content of those references.
 */
public class contentListAdapter extends BaseExpandableListAdapter implements View.OnClickListener {
    private Context context;
    private String[][] blessingsList;
    private String[][] conditionsList;
    private String[][] childList;
    private String[] parentList;
    private String[] favoriteList;
    private String favorite;
    public LayoutInflater mInflater;

    public contentListAdapter(Context context) {
        this.context=context;
    }

    public contentListAdapter(Context context, String[] parent, String[][] child,
                              String[][] conditions, String[][] blessings, String[]favorite){
        this.context = context;
        this.parentList = parent;
        this.childList = child;
        this.conditionsList = conditions;
        this.blessingsList = blessings;
        this.favoriteList = favorite;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getGroupCount() {
        return parentList.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupPosition;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        // test
        convertView = mInflater.inflate(R.layout.group_view, parent, false);
        TextView tv = (TextView) convertView.findViewById(R.id.textTitle);
        tv.setText(parentList[groupPosition]);
        ImageButton imageButton = (ImageButton) convertView.findViewById(R.id.favorite);
        imageButton.setFocusable(false);
        imageButton.setOnClickListener(this);
        favorite = favoriteList[groupPosition].toString();
        if (favorite == "0")
        {
            imageButton.setBackgroundResource(R.drawable.ic_launcher);
        }

//        TextView tv = new TextView(context);
//        tv.setText(parentList[groupPosition]);
//        tv.setGravity(Gravity.CENTER_VERTICAL);
//        tv.setHeight(120);
//        tv.setTextSize(20);
//        tv.setPadding(90, 0, 0, 0);
        return convertView;
    }

    @Override
    public void onClick(View v)
    {
        Log.v("Adapter", "Row button clicked");
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        convertView = mInflater.inflate(R.layout.child_view, parent, false);
        TextView content = (TextView) convertView.findViewById(R.id.textContent);
        content.setText(childList[groupPosition][childPosition]);
        TextView conditions = (TextView) convertView.findViewById(R.id.textConditions);
        conditions.setText("Condition(s): " + conditionsList[groupPosition][childPosition]);
        TextView blessings = (TextView) convertView.findViewById(R.id.textBlessings);
        blessings.setText("Blessing(s): " + blessingsList[groupPosition][childPosition]);

//        TextView tv = new TextView(context);
//        tv.setText(childList[groupPosition][childPosition]);
//        tv.setTextSize(15);
//        tv.setPadding(90, 0, 0, 0);
//        TextView tv1 = new TextView(context);
//        tv1.setText(childList[groupPosition][childPosition]);
//        tv1.setTextSize(15);
//        tv1.setPadding(90, 5, 0, 0);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
