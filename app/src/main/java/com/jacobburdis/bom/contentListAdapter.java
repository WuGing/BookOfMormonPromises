package com.jacobburdis.bom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by joshua on 4/30/14.
 * going to need different adapters for each view. Likely. Might have
 * to put some more thought into it. But will need to have an adapter
 * to make the list of labels, and then will need an adapter that
 * lists the references and the content of those references.
 */
public class contentListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private String[][] blessingsList;
    private String[][] conditionsList;
    private String[][] childList;
    private String[] parentList;
    private String[] favoriteList;
    private String[] favoriteHelperList;
    public LayoutInflater mInflater;
    private DataBaseHelper myDB;
    private ArrayList<Boolean> itemChecked = new ArrayList<Boolean>();

    public contentListAdapter(Context context) {
        this.context=context;
    }

    public contentListAdapter(Context context, String[] parent, String[][] child,
                              String[][] conditions, String[][] blessings, String[]favorite,
                              String[] favoriteHelper){
        this.context = context;
        this.parentList = parent;
        this.childList = child;
        this.conditionsList = conditions;
        this.blessingsList = blessings;
        this.favoriteList = favorite;
        this.favoriteHelperList = favoriteHelper;
        mInflater = LayoutInflater.from(context);
        myDB = new DataBaseHelper(context);

        // populate the favorite list boolean
        for (int i = 0; i < this.getGroupCount(); i++){
            if (favoriteList[i].equals("0")) {
                itemChecked.add(i, false);
            }
            else if (favoriteList[i].equals("1")) {
                itemChecked.add(i, true);
            }
        }
        myDB.close();
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
    public View getGroupView(final int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        // test
        convertView = mInflater.inflate(R.layout.group_view, parent, false);
        TextView tv = (TextView) convertView.findViewById(R.id.textTitle);
        tv.setText(parentList[groupPosition]);

        final CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.favorite);

        checkBox.setFocusable(false);

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CheckBox cb = (CheckBox) view.findViewById(R.id.favorite);

                if (cb.isChecked()) {
                    // Log.v("checkBox", "checked, setting to unChecked");
                    myDB.updateFavorite(favoriteHelperList[groupPosition], "1");
                    itemChecked.set(groupPosition, true);
                    notifyDataSetChanged();

                } else if (!cb.isChecked()) {
                    // Log.v("checkBox", "unChecked, setting to Checked");
                    myDB.updateFavorite(favoriteHelperList[groupPosition], "0");
                    itemChecked.set(groupPosition, false);
                    notifyDataSetChanged();
                }
            }

        });
        checkBox.setChecked(itemChecked.get(groupPosition));
        return convertView;
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

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
