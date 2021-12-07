package com.example.karim.applicationfacteur.ui.main;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.karim.applicationfacteur.R;
import com.example.karim.applicationfacteur.types.Agency3;
import com.example.karim.applicationfacteur.types.Envoi;

import java.util.ArrayList;

public class QRcollecelist extends ActionBarActivity {

    private ListView mListView1, mListView2;
    TextView txt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testt);


        txt = (TextView) findViewById(R.id.li);
        final ArrayList<Envoi> myList = (ArrayList<Envoi>) getIntent().getSerializableExtra("myList");
        final ArrayList<String> myListt = new ArrayList<>();
        final ArrayList<String> myList2 = new ArrayList<>();
        final ArrayList<String> myList1 = new ArrayList<>();
        final ArrayList<String> myList3 = new ArrayList<>();

        for (int i = 0; i < myList.size(); i++) {
            myListt.add(myList.get(i).getCode_envoi());
        }
        final String str[] = (String[]) getIntent().getSerializableExtra("str");

        for (int i = 0; i < str.length; i++) {
            myList2.add(str[i]);

        }


        for (int i = 0; i < myListt.size(); i++) {
            myList2.add(myListt.get(i));

        }





        for (int i = 0; i < myList2.size(); i++) {
            boolean isFound = false;
            for (int j = i+1; j < myList2.size(); j++) {
                if (myList2.get(i).equals(myList2.get(j))) {
                    isFound = true;
                    break;
                }
            }
            if (isFound) myList3.add(myList2.get(i));
        }







       /* if (myList3.size() == myListt.size() && myList3.size() == myList2.size()) {

            txt.setText("code correcte");


        } else {
            txt.setText("code erroné");

        }
*/








        mListView1 = (ListView) findViewById(R.id.listView1);
        mListView2 = (ListView) findViewById(R.id.listView2);

        //  mListView1.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,myListt)


        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,myListt) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // Get the current item from ListView
                View view = super.getView(position, convertView, parent);
                for (int j = 0 ; j<myList3.size();j++) {
                    //  Toast.makeText(getApplicationContext(),myList3.get(j)+"res",Toast.LENGTH_SHORT).show();
                    if(this.getItem(position).equalsIgnoreCase(myList3.get(j)))
                    {
                        view.setBackgroundColor(Color.parseColor("#ffdd00"));
                    }


                }
             /*   if (position ==1) {
                    // Set a background color for ListView regular row/item
                    view.setBackgroundColor(Color.parseColor("#FFB6B546"));
                } else {
                    // Set the background color for alternate row/item
                    view.setBackgroundColor(Color.parseColor("#FFCCCB4C"));
                }*/
                return view;
            }
        };

        // DataBind ListView with items from ArrayAdapter
        mListView1.setAdapter(arrayAdapter);

        //  mListView2.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, str));


        final ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,str) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // Get the current item from ListView
                View view = super.getView(position, convertView, parent);
                for (int j = 0 ; j<myList3.size();j++) {
                    //  Toast.makeText(getApplicationContext(),myList3.get(j)+"res",Toast.LENGTH_SHORT).show();
                    if(this.getItem(position).equalsIgnoreCase(myList3.get(j)))
                    {
                        view.setBackgroundColor(Color.parseColor("#ffdd00"));
                    }
                }
             /*   if (position ==1) {
                    // Set a background color for ListView regular row/item
                    view.setBackgroundColor(Color.parseColor("#FFB6B546"));
                } else {
                    // Set the background color for alternate row/item
                    view.setBackgroundColor(Color.parseColor("#FFCCCB4C"));
                }*/
                return view;
            }
        };

        mListView2.setAdapter(arrayAdapter1);

        ListUtils.setDynamicHeight(mListView1);
        ListUtils.setDynamicHeight(mListView2);



    }







    public static class ListUtils {
        public static void setDynamicHeight(ListView mListView) {
            ListAdapter mListAdapter = mListView.getAdapter();
            if (mListAdapter == null) {
                // when adapter is null
                return;
            }
            int height = 0;
            int desiredWidth = View.MeasureSpec.makeMeasureSpec(mListView.getWidth(), View.MeasureSpec.UNSPECIFIED);
            for (int i = 0; i < mListAdapter.getCount(); i++) {
                View listItem = mListAdapter.getView(i, null, mListView);

                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                height += listItem.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = mListView.getLayoutParams();
            params.height = height + (mListView.getDividerHeight() * (mListAdapter.getCount() - 1));
            mListView.setLayoutParams(params);
            mListView.requestLayout();
        }
    }
}