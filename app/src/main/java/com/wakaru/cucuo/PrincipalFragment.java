package com.wakaru.cucuo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A placeholder fragment containing a simple view.
 */
public class PrincipalFragment extends Fragment {

    Button simulCompra;
    EditText saldoMensual;
    TextView saldoDisponible;
    TextView tituloSaldoDiponible;
    public static final String archivo = "MyPrefsFile";

    public PrincipalFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_principal, container, false);

        simulCompra = (Button) root.findViewById(R.id.simulCompra);
        saldoMensual = (EditText) root.findViewById(R.id.saldoMensual);
        saldoDisponible = (TextView) root.findViewById(R.id.textViewSaldoDisponible);
        tituloSaldoDiponible = (TextView) root.findViewById(R.id.showLimit);


        SharedPreferences settings = this.getActivity().getSharedPreferences(archivo, 0);
        String textoSaldoMensual = settings.getString("saldoMensual", null);
        String textoSaldoDisponible = settings.getString("saldoDisponible", null);

        if ((textoSaldoMensual == null) || (textoSaldoMensual.equals("0"))) {
            saldoMensual.setText("0");
            saldoDisponible.setText("0");
        } else {
            saldoMensual.setText(textoSaldoMensual);
            if (textoSaldoDisponible.equals("0")) {
                saldoDisponible.setText("0");
            } else {
                saldoDisponible.setText(textoSaldoDisponible);
            }
        }

        //saldoMensual.addTextChangedListener(this);

        simulCompra.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent aSimular = new Intent(getActivity(), simularCompra.class);
                aSimular.putExtra("saldoDisponibleKey", saldoDisponible.getText().toString());

                startActivity(aSimular);
            }
        });

        /**
         * Guarda el valor del saldo mensual. Actualiza el disponible si es 0 (Usa app por primera vez)
         */
        saldoMensual.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    EditText saldoMensualViejo = saldoMensual;
                    if (saldoDisponible.getText().equals("0")) {
                        saldoDisponible.setText(saldoMensual.getText());
                        Toast.makeText(getActivity(), "Saldo mensual y disponible actualizados", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Saldo mensual actualizado", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                } else {
                    return false;
                }
            }
        });

        saldoMensual.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                tituloSaldoDiponible.setTextColor(Color.parseColor("#F44336"));
                return false;
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
