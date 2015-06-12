package com.wakaru.cucuo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A placeholder fragment containing a simple view.
 */
public class PrincipalFragment extends Fragment {

    Button simulCompra;
    EditText saldoDisponible;
    TextView TextViewTituloSaldo;
    int saldoDisponibleValor;
    ImageButton ImageButtonAumentar;
    public static final String archivo = "MyPrefsFile";

    public PrincipalFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_principal, container, false);

        simulCompra = (Button) root.findViewById(R.id.simulCompra);
        saldoDisponible = (EditText) root.findViewById(R.id.textViewSaldoDisponible);
        TextViewTituloSaldo = (TextView) root.findViewById(R.id.TextViewTituloSaldo);
        ImageButtonAumentar = (ImageButton) root.findViewById(R.id.ImageButtonAumentar);

        SharedPreferences settings = this.getActivity().getSharedPreferences(archivo, 0);

        String textoSaldoDisponible = settings.getString("saldoDisponible", null);
        int intSaldoDisponible = settings.getInt("saldoDisponibleValor",-1);

        if (intSaldoDisponible == -1){

            intSaldoDisponible = -1;
            saldoDisponible.setText("");
        }
        else {
            //disponible editado

            saldoDisponible.setText(textoSaldoDisponible);
        }



        saldoDisponible.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (saldoDisponible.getText().toString().equals("")) {
                    TextViewTituloSaldo.setVisibility(TextViewTituloSaldo.INVISIBLE);
                } else {
                    TextViewTituloSaldo.setVisibility(TextViewTituloSaldo.VISIBLE);
                    saldoDisponible.setHintTextColor(getResources().getColor(R.color.Color_Texto_Hint));
                }
            }
        });

        saldoDisponible.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    if (saldoDisponible.getText().equals("")) {

                        Toast.makeText(getActivity(), "Saldo actualizados vacio", Toast.LENGTH_SHORT).show();

                    } else {

                        Toast.makeText(getActivity(), "Saldo actualizado", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                } else {

                    return false;
                }
            }
        });

        simulCompra.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (saldoDisponible.getText().toString().equals("")){
                    Toast.makeText(getActivity(), "Saldo no valido", Toast.LENGTH_SHORT).show();
                    saldoDisponible.setHintTextColor(getResources().getColor(R.color.Saldo_No_Disponible));
                }

                else {
                    saldoDisponible.setHintTextColor(getResources().getColor(R.color.Color_Texto_Hint));
                    Intent aSimular = new Intent(getActivity(), simularCompra.class);
                    aSimular.putExtra("saldoDisponibleKey", saldoDisponible.getText().toString());

                    startActivity(aSimular);
                }
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
        editor.putString("saldoDisponible", saldoDisponible.getText().toString());
        editor.putInt("saldoDisponibleValor", saldoDisponibleValor);

        editor.commit();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void guardarDatos(){

    }
}
