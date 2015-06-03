package com.wakaru.cucuo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


/**
 * A placeholder fragment containing a simple view.
 */
public class PrincipalFragment extends Fragment {

    Button simulCompra;
    EditText saldoMensual;
    TextView saldoDisponible;
    public static final String archivo = "MyPrefsFile";

    public PrincipalFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_principal, container, false);

        simulCompra = (Button) root.findViewById(R.id.simulCompra);
        saldoMensual = (EditText) root.findViewById(R.id.saldoMensual);
        saldoDisponible = (TextView) root.findViewById(R.id.textViewSaldoDisponible);


        SharedPreferences settings = this.getActivity().getSharedPreferences(archivo, 0);
        String textoSaldoMensual = settings.getString("saldoMensual", null);
        String textoSaldoDisponible = settings.getString("saldoDisponible", null);

        if (textoSaldoMensual == null) {
            saldoMensual.setText("$ " + 0);
            saldoDisponible.setText("$ " + 0);
        } else {
            saldoMensual.setText("$ " + textoSaldoMensual);
            saldoDisponible.setText("$ " + textoSaldoDisponible);
        }

        simulCompra.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent aSimular = new Intent(getActivity(), simularCompra.class);
                startActivity(aSimular);
            }
        });
        return root;
    }

    /**
     * Guarda el valor del saldo mensual disponible haciendo uso de Shared Preferences
     */
    @Override
    public void onStop() {
        super.onStop();

        SharedPreferences settings = this.getActivity().getSharedPreferences(archivo, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("saldoMensual", saldoMensual.getText().toString());
        editor.putString("saldoDisponible", saldoDisponible.getText().toString());

        editor.commit();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
