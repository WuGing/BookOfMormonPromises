package com.jacobburdis.bom;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 *
 */
public class FragmentCategoryList extends ListFragment {

    private String fragmentIdentifier;
    private int fragmentIdentifierHelper;
    private SimpleCursorAdapter mListAdapter;
    DataBaseHelper myDB;

    public FragmentCategoryList()
    {
        // Required empty public constructor
    }

    public FragmentCategoryList(String identifier, int identifierHelper)
    {
        this.fragmentIdentifier = identifier;
        this.fragmentIdentifierHelper = identifierHelper;
        myDB = new DataBaseHelper(getActivity());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        myDB.openDataBase();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        String [] fromFieldNames;

        switch ( fragmentIdentifierHelper )
        {
            // favorite
            case 0:
                fromFieldNames = new String[] { DataBaseHelper.KEY_BOOK };
                break;
            // condition
            case 1:
                fromFieldNames = new String[] { DataBaseHelper.KEY_CONDITION };
                break;
            // blessing
            case 2:
                fromFieldNames = new String[] { DataBaseHelper.KEY_BLESSING };
                break;
            // chronological
            case 3:
                fromFieldNames = new String[] { DataBaseHelper.KEY_BOOK };
                break;
            default:
                fromFieldNames = null;
                break;
        }

        int[] toViewIDs = new int []{
                R.id.categoryName
        };


        mListAdapter = new SimpleCursorAdapter(
                getActivity(),
                R.layout.list_item,
                myDB.getAllRows(fragmentIdentifierHelper),
                fromFieldNames,
                toViewIDs
        );

        setListAdapter(mListAdapter);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onListItemClick(ListView lv, View view, int position,
                                long id) {
        super.onListItemClick(lv, view, position, id);

        Cursor b = ((SimpleCursorAdapter) lv.getAdapter()).getCursor();
        b.moveToPosition(position);

        String numberHelper = b.getString(b.getColumnIndex("_id"));
        int number = Integer.parseInt(numberHelper);

        Cursor c = myDB.getRow(number, fragmentIdentifier);

        String content;
        Cursor parentCursor;
        Cursor childCursor;
        Cursor conditionsCursor;
        Cursor blessingsCursor;
        Cursor favoriteCursor;

        switch ( fragmentIdentifierHelper )
        {
            // favorite
            case 0:
                content = c.getString(c.getColumnIndex(DataBaseHelper.KEY_BOOK));
                parentCursor = myDB.getRowsFavorites(content);
                childCursor = myDB.getRowsFavorites(content);
                conditionsCursor = myDB.getRowsFavorites(content);
                blessingsCursor = myDB.getRowsFavorites(content);
                favoriteCursor = myDB.getRowsFavorites(content);
                break;

            // condition
            case 1:
                content = c.getString(c.getColumnIndex(DataBaseHelper.KEY_CONDITION));
                parentCursor = myDB.getRowsCondition(content);
                childCursor = myDB.getRowsCondition(content);
                conditionsCursor = myDB.getRowsCondition(content);
                blessingsCursor = myDB.getRowsCondition(content);
                favoriteCursor = myDB.getRowsCondition(content);
                break;

            // blessing
            case 2:
                content = c.getString(c.getColumnIndex(DataBaseHelper.KEY_BLESSING));
                parentCursor = myDB.getRowsBlessing(content);
                childCursor = myDB.getRowsBlessing(content);
                conditionsCursor = myDB.getRowsBlessing(content);
                blessingsCursor = myDB.getRowsBlessing(content);
                favoriteCursor = myDB.getRowsBlessing(content);
                break;

            // chronological
            case 3:
                content = c.getString(c.getColumnIndex(DataBaseHelper.KEY_BOOK));
                parentCursor = myDB.getRowsBook(content);
                childCursor = myDB.getRowsBook(content);
                conditionsCursor = myDB.getRowsBook(content);
                blessingsCursor = myDB.getRowsBook(content);
                favoriteCursor = myDB.getRowsBook(content);
                break;

            default:
                content = null;
                parentCursor = null;
                childCursor = null;
                conditionsCursor = null;
                blessingsCursor = null;
                favoriteCursor = null;
                break;
        }

        c = null;

        Fragment contentList = new FragmentContentList(parentListGetter(parentCursor),
                childListGetter(childCursor), conditionsListGetter(conditionsCursor),
                blessingsListGetter(blessingsCursor), favoriteGetter(favoriteCursor));
        getFragmentManager().beginTransaction()
                .addToBackStack(null)
                .replace(android.R.id.content, contentList)
                .commit();
    }

    public String[] parentListGetter(Cursor c)
    {

        List<String> parent = new ArrayList<String>();
        while (!c.isAfterLast()){
            parent.add(recursiveParent(c));
            c.moveToNext();
        }

        String[] parentValues = parent.toArray(new String[parent.size()]);

        return parentValues;
    }

    public String recursiveParent(Cursor c)
    {
        String temp =
                c.getString(c.getColumnIndex(DataBaseHelper.KEY_REFERENCE));
        return temp;
    }

    public String[][] childListGetter(Cursor c)
    {

        List<String> child = new ArrayList<String>();
        while(!c.isAfterLast())
        {
            child.add(recursiveChild(c));
            c.moveToNext();
        }

        String[][] childValues = new String[child.size()][child.size()];
        for (int i=0; i < child.size(); i++)
        {
            childValues[i][0] = child.get(i);
        }

        return childValues;
    }

    public String recursiveChild(Cursor c)
    {
        String temp =
                c.getString(c.getColumnIndex(DataBaseHelper.KEY_CONTENT));
        return temp;
    }

    public String[][] conditionsListGetter(Cursor c)
    {
        List<String> conditions = new ArrayList<String>();
        while(!c.isAfterLast())
        {
            conditions.add(recursiveCondition(c));
            c.moveToNext();
        }

        String[][] conditionValues = new String[conditions.size()][conditions.size()];
        for (int i=0; i < conditions.size(); i++)
        {
            conditionValues[i][0] = conditions.get(i);
        }
        return conditionValues;
    }

    public String recursiveCondition(Cursor c)
    {
        String temp =
                c.getString(c.getColumnIndex(DataBaseHelper.KEY_CONDITIONS));
        return temp;
    }

    public String[][] blessingsListGetter(Cursor c)
    {
        List<String> blessings = new ArrayList<String>();
        while(!c.isAfterLast())
        {
            blessings.add(recursiveBlessing(c));
            c.moveToNext();
        }

        String[][] blessingValues = new String[blessings.size()][blessings.size()];
        for ( int i = 0; i < blessings.size(); i++ )
        {
            blessingValues[i][0] = blessings.get(i);
        }
        return blessingValues;
    }

    public String recursiveBlessing(Cursor c)
    {
        String temp =
                c.getString(c.getColumnIndex(DataBaseHelper.KEY_BLESSINGS));
        return temp;
    }

    public String[] favoriteGetter(Cursor c)
    {
        List<String> favorite = new ArrayList<String>();
        while(!c.isAfterLast())
        {
            favorite.add(recursiveFavorite(c));
            c.moveToNext();
        }

        String[] favoriteValues = favorite.toArray(new String[favorite.size()]);

        return favoriteValues;
    }

    public String recursiveFavorite(Cursor c)
    {
        String temp =
                c.getString(c.getColumnIndex(DataBaseHelper.KEY_FAVORITE));
        return temp;
    }

}
