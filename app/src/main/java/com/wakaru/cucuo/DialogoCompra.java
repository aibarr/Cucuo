package com.wakaru.cucuo;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Joel on 16/06/2015.
 */
public class DialogoCompra extends DialogFragment implements View.OnClickListener {

    Button ButtonAceptarComprar;
    Button ButtonCancelarCompra;
    EditText EditTextNombreCompra;
    Comunicator comunicator;
    TextView TextViewMaxCaracteres;
    int maxCaracteres, actualCaracteres;

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
        TextViewMaxCaracteres = (TextView) view.findViewById(R.id.TextViewMaxCaracteres);
        maxCaracteres = 10;

        ButtonAceptarComprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (EditTextNombreCompra.getText().toString().equals("") || (actualCaracteres > maxCaracteres)) {

                    if (EditTextNombreCompra.getText().toString().equals("")){
                        Toast.makeText(getActivity().getApplicationContext(), "Ingresa nombre", Toast.LENGTH_SHORT).show();
                    }
                    if ((actualCaracteres > maxCaracteres)){
                        Toast.makeText(getActivity().getApplicationContext(), "Nombre muy largo", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    comunicator.onDialogMessage(EditTextNombreCompra.getText().toString());
                    dismiss();
                }
            }
        });

        EditTextNombreCompra.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                actualCaracteres = EditTextNombreCompra.length();
                TextViewMaxCaracteres.setText(String.valueOf(actualCaracteres) + " / " + String.valueOf(maxCaracteres));

                TextViewMaxCaracteres.setTextColor(revCaracteres(actualCaracteres, maxCaracteres));
            }
        });

        ButtonCancelarCompra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        setCancelable(false);
        return view;
    }

    /**
     * Funcion que retorna el color del texto de acuerdo a la cantidad de caracteres introducidas
     * @param actualCaracteres cantidad de caracteres escritos
     * @param maxCaracteres cantidad maxima de caracteres soportados
     * @return retorna el color (negro si no se pasa el maximo, rojo si se pasa)
     */
    private int revCaracteres(int actualCaracteres, int maxCaracteres) {

        if (actualCaracteres > maxCaracteres){
            return getResources().getColor(R.color.Saldo_No_Disponible);
        }
        else{
            return getResources().getColor(R.color.Saldo_Disponible);
        }
    }

    interface Comunicator {
        public void onDialogMessage(String message);
    }

    @Override
    public void onClick(View v) {

    }
}
