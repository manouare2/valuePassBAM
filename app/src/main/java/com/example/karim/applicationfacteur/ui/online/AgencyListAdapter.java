package com.example.karim.applicationfacteur.ui.online;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.karim.applicationfacteur.R;
import com.example.karim.applicationfacteur.services.UIListener;
import com.example.karim.applicationfacteur.services.online.OnlineManager;
import com.example.karim.applicationfacteur.services.online.OnlineODataStoreException;
import com.example.karim.applicationfacteur.types.Agency;

import java.util.List;


public class AgencyListAdapter extends BaseAdapter implements UIListener {
    final private LayoutInflater inflater;

    final private AgencyListFragment fragment;
   UIListener ui= this;

    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;
    private Snackbar snackbar;
    private CoordinatorLayout coordinatorLayout;
    private boolean internetConnected=true;
    private  boolean a;
    private SQLiteHandler db;
    boolean insert= false;

    Agency agency;
     Context ctx;



    final private List<Agency> agencies;
    final private ListView myList;
    private Button agency_id;

    public AgencyListAdapter(AgencyListFragment fragment, ListView myList, LayoutInflater inflater, List<Agency> agencies,Context ctx) {
        this.inflater = inflater;
        this.fragment = fragment;
        this.myList = myList;
        this.agencies = agencies;
        this.ctx=ctx;
    }

	@Override
	public int getCount() {
		return (agencies!=null)?agencies.size():0;
	}

	@Override
	public Object getItem(int position) {
		return (agencies!=null)?agencies.get(position):null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
        db = new SQLiteHandler(ctx);
        if (view == null) {
            view = inflater.inflate(R.layout.agency_list_item, parent, false);
        }

        final Agency agency = agencies.get(position);

        ((TextView) view.findViewById(R.id.agency_name)).setText(agency.getCommande());
        //((TextView) view.findViewById(R.id.agency_id)).setText(agency.getAgencyId());


        view.setOnClickListener(new AgencyListClickListener(agencies.get(position)));
        view.setOnLongClickListener(new AgencyListLongClickListener(position));
        agency_id= (Button) view.findViewById(R.id.agency_id);
        final View finalView = view;
        agency_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                     OnlineManager.createAgency(ctx,agency,ui);
                    Toast.makeText(finalView.getContext(),"votre commande est bien validée",Toast.LENGTH_SHORT).show();


                } catch (OnlineODataStoreException e) {
                    e.printStackTrace();
                }

            }
        });


        return view;
	}


    /**
     * Delete an item from the list.
     *
     * @param i the index of the item to delete
     */
    public void deleteItem(int i) {
        myList.setItemChecked(i, false);  // un-select the item that is being deleted
        agencies.remove(i);
        notifyDataSetChanged();
    }

    @Override
    public void onRequestError(int operation, Exception e) {

    }

    @Override
    public void onRequestSuccess(int operation, String key) {

    }

    @Override
    public void onODataRequestError(Exception e) {

    }

    @Override
    public void onODataRequestSuccess(String info) {

    }

    /**
     * This listener handles clicks on a travel agency in the list.
     */
    private class AgencyListClickListener implements View.OnClickListener {

        final private Agency item;

        private AgencyListClickListener(Agency item) {
            this.item = item;
        }

        @Override
        public void onClick(final View view) {
            fragment.onAgencySelected(item);

        }
    }
    
    
    /**
     * This listener handles long clicks on a travel agency in the list. Long clicking
     * on an agency makes it available for deleting.
     */
    private class AgencyListLongClickListener implements View.OnLongClickListener {

        final private int i;

        private AgencyListLongClickListener(int i) {
            this.i = i;
        }

        @Override
        public boolean onLongClick(View view) {
            myList.setItemChecked(i, true);
            return true;
        }
    }


}
