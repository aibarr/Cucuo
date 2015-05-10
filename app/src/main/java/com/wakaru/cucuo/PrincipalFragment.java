package com.wakaru.cucuo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;



/**
 * A placeholder fragment containing a simple view.
 */
public class PrincipalFragment extends Fragment {

    Button simulCompra;

    public PrincipalFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_principal, container, false);

        simulCompra = (Button)root.findViewById(R.id.simulCompra);

        simulCompra.setOnClickListener(new Button.OnClickListener(){


            @Override
            public void onClick(View v){
                Intent aSimular = new Intent(getActivity(), simularCompra.class);
                startActivity(aSimular);
            }

        });

        return root;
    }
}
