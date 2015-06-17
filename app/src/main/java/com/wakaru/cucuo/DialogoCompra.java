package com.wakaru.cucuo;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Joel on 16/06/2015.
 */
public class DialogoCompra extends DialogFragment implements View.OnClickListener {

    Button ButtonAceptarComprar;
    Button ButtonCancelarCompra;
    EditText EditTextNombreCompra;
    Comunicator comunicator;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        comunicator = (Comunicator) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialogo_compra, null);

        ButtonAceptarComprar = (Button) view.findViewById(R.id.ButtonAceptarComprar);
        ButtonCancelarCompra = (Button) view.findViewById(R.id.ButtonCancelarCompra);
        EditTextNombreCompra = (EditText) view.findViewById(R.id.EditTextNombreCompra);

        ButtonAceptarComprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comunicator.onDialogMessage("Compra aceptada");
                dismiss();
            }
        });

        ButtonCancelarCompra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                comunicator.onDialogMessage("Compra cancelada");
            }
        });

        setCancelable(false);
        return view;
    }

    interface Comunicator{
        public void onDialogMessage(String message);
    }

    @Override
    public void onClick(View v) {

    }
}
