package com.wakaru.cucuo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;


/**
 * A placeholder fragment containing a simple view.
 */
public class simularCompraFragment extends Fragment {

    private SimpleAdapter mSchedule;

    public simularCompraFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root2 = inflater.inflate(R.layout.fragment_simular_compra, container, false);

        String[] mytems = {"CMR","BCI","BBVA","Falabella","Tarjeta4","Tarjeta5","Tarjeta6"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(root2.getContext(), android.R.layout.simple_list_item_1, mytems);
        ListView list = (ListView) root2.findViewById(R.id.listView1);
        list.setAdapter(adapter);

        return root2;
    }

}
