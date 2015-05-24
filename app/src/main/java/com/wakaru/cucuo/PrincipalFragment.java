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


/**
 * A placeholder fragment containing a simple view.
 */
public class PrincipalFragment extends Fragment {

    Button simulCompra;
    EditText saldoMensual;
    public static final String archivo = "MyPrefsFile";

    public PrincipalFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_principal, container, false);

        simulCompra = (Button) root.findViewById(R.id.simulCompra);
        saldoMensual = (EditText) root.findViewById(R.id.saldoMensual);

        SharedPreferences settings = this.getActivity().getSharedPreferences(archivo, 0);
        String texto = settings.getString("saldoMensual", null);
        saldoMensual.setText(texto);

        simulCompra.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent aSimular = new Intent(getActivity(), simularCompra.class);
                startActivity(aSimular);
            }
        });
        return root;
    }

    @Override
    public void onStop() {
        super.onStop();

        SharedPreferences settings = this.getActivity().getSharedPreferences(archivo, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("saldoMensual", saldoMensual.getText().toString());

        editor.commit();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
