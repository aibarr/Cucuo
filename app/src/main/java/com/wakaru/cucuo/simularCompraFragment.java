package com.wakaru.cucuo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

        return root2;
    }

}
