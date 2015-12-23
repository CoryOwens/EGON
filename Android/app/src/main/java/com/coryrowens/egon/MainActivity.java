package com.coryrowens.egon;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by cory on 12/1/2015.
 */
public class MainActivity extends AppCompatActivity {
    private String[] testArray = {"One", "Two", "Three"};
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ExpandableListView mDrawerList;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private NameGenerator nameGen;
    private FloatingActionButton generateButton, copyButton;
    private TextView nameView;
    private Name name;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NameDataReader nameDataReader = new NameDataReader(getResources());
        nameGen = new NameGenerator(nameDataReader);
        nameView = (TextView) findViewById(R.id.name_view);
        initGenerateButton();
        initCopyButton();
        initActionBarDrawer();
    }

    private void initGenerateButton() {
        generateButton = (FloatingActionButton) findViewById(R.id.generate);
        generateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                name = nameGen.generate();
                String fullName = name.getFullName();
                if (fullName == null || "null".equals(fullName)) {
                    fullName = "";
                    CharSequence message = "Not enough options selected.";
                    Toast toast = Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT);
                    toast.show();
                }
                nameView.setText(fullName);
            }
        });
    }

    private void initCopyButton() {
        copyButton = (FloatingActionButton) findViewById(R.id.copy);
        copyButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String fullName = nameView.getText().toString();
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", fullName);
                clipboard.setPrimaryClip(clip);
                CharSequence message = "Copied to clipboard.";
                Toast toast = Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    private void initActionBarDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ExpandableListView) findViewById(R.id.left_drawer);
        // TODO: GET REAL DATA
        // Set the adapter for the list view
        mDrawerList.setAdapter(new SimpleExpandableListAdapter(
                        this,                                               // context

                        getGroupData(),                                     // groupData
                        R.layout.drawer_list_group,                         // groupLayout
                        new String[]{"ROOT_NAME"},                          // groupFrom
                        new int[]{android.R.id.text1},                      // groupTo

                        getChildData(),                                     // childData
                        R.layout.drawer_list_item,                          // childLayout
                        new String[]{"CHILD_NAME"},                         // childFrom
                        new int[]{android.R.id.text1}                       // childTo
                )
        );
        // Set the list's click listener
//        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerList.setOnChildClickListener(new DrawerItemClickListener());

        mTitle = mDrawerTitle = "EGON"; // TODO: Make resource
        getSupportActionBar().setTitle(mTitle);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private List<Map<String, String>> getGroupData() {
        List<Map<String, String>> groupData = new ArrayList<Map<String, String>>() {{
            add(new HashMap<String, String>() {{
                put("ROOT_NAME", "Time Periods");
            }});
            add(new HashMap<String, String>() {{
                put("ROOT_NAME", "Cultures");
            }});
            add(new HashMap<String, String>() {{
                put("ROOT_NAME", "Genders");
            }});
        }};
        return groupData;
    }

    // TODO: MOCK METHOD. Replace with something not dumb.
    private List<List<Map<String, String>>> getChildData() {
        List<List<Map<String, String>>> listOfChildGroups = new ArrayList<List<Map<String, String>>>();
        List<String> timePeriodOptions = new ArrayList<>();
        timePeriodOptions.addAll(nameGen.getTimePeriodOptions());
        Collections.sort(timePeriodOptions);
        List<Map<String, String>> timePeriodChildren = new ArrayList<Map<String, String>>();
        //TODO: Add clear/select all toggle
//        Map<String, String> clearTimePeriods = new HashMap<String, String>();
//        clearTimePeriods.put("CHILD_NAME", "Clear Time Periods");
//        timePeriodChildren.add(clearTimePeriods);
        for (String tp : timePeriodOptions) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("CHILD_NAME", tp);
            timePeriodChildren.add(map);
        }
        listOfChildGroups.add(timePeriodChildren);

        List<String> cultureOptions = new ArrayList<>();
        cultureOptions.addAll(nameGen.getCultureOptions());
        Collections.sort(cultureOptions);
        List<Map<String, String>> cultureChildren = new ArrayList<Map<String, String>>();
        //TODO: Add clear/select all toggle
//        Map<String, String> clearCultures = new HashMap<String, String>();
//        clearCultures.put("CHILD_NAME", "Clear Cultures");
//        cultureChildren.add(clearCultures);
        for (String c : cultureOptions) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("CHILD_NAME", c);
            cultureChildren.add(map);
        }
        listOfChildGroups.add(cultureChildren);

        List<Map<String, String>> genderChildren = new ArrayList<Map<String, String>>();
        Map<String, String> map = new HashMap<String, String>();
        map.put("CHILD_NAME", "Female");
        genderChildren.add(map);
        map = new HashMap<String, String>();
        map.put("CHILD_NAME", "Male");
        genderChildren.add(map);
        listOfChildGroups.add(genderChildren);

        return listOfChildGroups;
    }

    private class DrawerItemClickListener implements ExpandableListView.OnChildClickListener {
//        @Override
//        public void onItemClick(AdapterView parent, View view, int position, long id) {
//            Log.e("TAG", "Item Clicked: " + position + " (Parent: " + parent + ")");
//            selectItem(position);
//        }

        @Override
        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

            Log.d("DRAWER_ONCLICK", "Item Clicked: " + childPosition + "(Group: " + groupPosition + ")");
            long groupPackedPosition = parent.getPackedPositionForGroup(groupPosition);
            int groupFlatPosition = parent.getFlatListPosition(groupPackedPosition);
            TextView groupView = (TextView) parent.getChildAt(groupFlatPosition);
            String groupLabel = groupView.getText().toString();
            Log.d("DRAWER_ONCLICK", "groupLabel: " + groupLabel);
            long childPackedPosition = parent.getPackedPositionForChild(groupPosition, childPosition);
            int childFlatPosition = parent.getFlatListPosition(childPackedPosition);
            TextView itemView = (TextView) parent.getChildAt(childFlatPosition);
            String itemLabel = itemView.getText().toString();
            Log.d("DRAWER_ONCLICK", "itemLabel: " + itemLabel);
            int selected = ContextCompat.getColor(parent.getContext(), R.color.drawerSelected);
            int unselected = ContextCompat.getColor(parent.getContext(), R.color.drawerUnselected);
            switch (groupLabel) {
                case "Time Periods":
                    if (nameGen.toggleTimePeriod(itemLabel)) {

                        itemView.setBackgroundColor(selected);
                        Log.d("DRAWER_ONCLICK", "Toggled " + itemLabel + " On.");
                    } else {
                        itemView.setBackgroundColor(unselected);
                        Log.d("DRAWER_ONCLICK", "Toggled " + itemLabel + " Off.");
                    }
                    break;
                case "Cultures":
                    if (nameGen.toggleCulture(itemLabel)) {
                        itemView.setBackgroundColor(selected);
                        Log.d("DRAWER_ONCLICK", "Toggled " + itemLabel + " On.");
                    } else {
                        itemView.setBackgroundColor(unselected);
                        Log.d("DRAWER_ONCLICK", "Toggled " + itemLabel + " Off.");
                    }
                    break;
                case "Genders":
                    Gender gender;
                    if (Gender.MALE.equalsName(itemLabel)) {
                        gender = Gender.MALE;
                    } else if (Gender.FEMALE.equalsName(itemLabel)) {
                        gender = Gender.FEMALE;
                    } else {
                        return false;
                    }
                    if (nameGen.toggleGender(gender)) {
                        itemView.setBackgroundColor(selected);
                        Log.d("DRAWER_ONCLICK", "Toggled " + itemLabel + " On.");
                    } else {
                        itemView.setBackgroundColor(unselected);
                        Log.d("DRAWER_ONCLICK", "Toggled " + itemLabel + " Off.");
                    }
                    break;
                default:
                    return false;
            }
            return true;
        }
    }

    /**
     * Swaps fragments in the main content view
     */
    private void selectItem(int position) {
        // Create a new fragment and specify the planet to show based on position
//        Fragment fragment = new MenuFragment();
//        Bundle args = new Bundle();
//        args.putInt(MenuFragment.ARG_MENU_NUMBER, position);
//        fragment.setArguments(args);
//
//        // Insert the fragment by replacing any existing fragment
//        FragmentManager fragmentManager = getFragmentManager();
//        fragmentManager.beginTransaction()
//                .replace(R.id.main_content_view, fragment)
//                .commit();
//
//        // Highlight the selected item, update the title, and close the drawer
//        mDrawerList.setItemChecked(position, true);
//        setTitle(testArray[position]);
//        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return false;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
}