package com.wakaru.cucuo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;


/**
 * A placeholder fragment containing a simple view.
 */
public class PrincipalFragment extends Fragment {

    Button simulCompra;
    EditText saldoDisponible;
    EditText EditTextAgregarSaldo;
    EditText EditTextReducirSaldo;
    TextView TextViewTituloSaldo;
    int saldoDisponibleValor;
    Button ButtonAgregarSaldo;
    Button ButtonRestarSaldo;
    public static final String archivo = "MyPrefsFile";

    public PrincipalFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_principal, container, false);

        simulCompra = (Button) root.findViewById(R.id.simulCompra);
        ButtonAgregarSaldo = (Button) root.findViewById(R.id.ButtonAgregarSaldo);
        ButtonRestarSaldo = (Button) root.findViewById(R.id.ButtonRestarSaldo);
        saldoDisponible = (EditText) root.findViewById(R.id.textViewSaldoDisponible);
        EditTextAgregarSaldo = (EditText) root.findViewById(R.id.EditTextAgregarSaldo);
        EditTextReducirSaldo = (EditText) root.findViewById(R.id.EditTextReducirSaldo);
        TextViewTituloSaldo = (TextView) root.findViewById(R.id.TextViewTituloSaldo);

        SharedPreferences settings = this.getActivity().getSharedPreferences(archivo, 0);

        String textoSaldoDisponible = settings.getString("saldoDisponible", "");
        int intSaldoDisponible = settings.getInt("saldoDisponibleValor", -1);

        if (textoSaldoDisponible.equals("")) {
            saldoDisponible.setText(R.string.saldo_vacio);
        } else {
            saldoDisponible.setText(textoSaldoDisponible);
            TextViewTituloSaldo.setVisibility(TextViewTituloSaldo.VISIBLE);
        }

        /**
         * Cuando se apreta el boton, si no hay saldo ingresado, mantiene el titutlo del texto invisible, en caso que haber vuelve el titulo visible
         */
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

                    saldoDisponible.removeTextChangedListener(this);
                    saldoDisponible.setText(formatear(quitarFormato(saldoDisponible.getText().toString())));
                    saldoDisponible.setSelection(saldoDisponible.getText().length());
                    saldoDisponible.addTextChangedListener(this);

                    if (formatear(quitarFormato(saldoDisponible.getText().toString())).equals("")) {
                        TextViewTituloSaldo.setVisibility(TextViewTituloSaldo.INVISIBLE);
                    }
                }
            }
        });

        /**
         * Controla los eventos sobre el EditText de agregar saldo, aplica formato monetario mientras se edita
         */
        EditTextAgregarSaldo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (EditTextAgregarSaldo.getText().toString().equals("")) {

                } else {
                    EditTextAgregarSaldo.removeTextChangedListener(this);
                    EditTextAgregarSaldo.setText(formatear(quitarFormato(EditTextAgregarSaldo.getText().toString())));
                    EditTextAgregarSaldo.setSelection(EditTextAgregarSaldo.getText().length());
                    EditTextAgregarSaldo.addTextChangedListener(this);
                }
            }
        });

        /**
         * Controla los eventos sobre el EditText de reducir saldo, aplica formato monetario mientras se edita
         */
        EditTextReducirSaldo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (EditTextReducirSaldo.getText().toString().equals("")) {

                } else {
                    EditTextReducirSaldo.removeTextChangedListener(this);
                    EditTextReducirSaldo.setText(formatear(quitarFormato(EditTextReducirSaldo.getText().toString())));
                    EditTextReducirSaldo.setSelection(EditTextReducirSaldo.getText().length());
                    EditTextReducirSaldo.addTextChangedListener(this);
                }
            }
        });

        /**
         * Suma el saldo a algregar al saldo disponible
         */
        ButtonAgregarSaldo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (EditTextAgregarSaldo.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), R.string.no_hay_saldo, Toast.LENGTH_SHORT).show();
                } else {

                    if (saldoDisponible.getText().toString().equals("")) {
                        int nuevoDisponible = 0 + Integer.parseInt(quitarFormato(EditTextAgregarSaldo.getText().toString()));
                        saldoDisponible.setText(formatear(Integer.toString(nuevoDisponible)));
                        EditTextAgregarSaldo.setText("");
                    } else {
                        int nuevoDisponible = Integer.parseInt(quitarFormato(saldoDisponible.getText().toString())) + Integer.parseInt(quitarFormato(EditTextAgregarSaldo.getText().toString()));
                        saldoDisponible.setText(formatear(Integer.toString(nuevoDisponible)));
                        EditTextAgregarSaldo.setText("");
                    }
                    Toast.makeText(getActivity(), R.string.saldo_actualizado, Toast.LENGTH_SHORT).show();
                }

                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
            }
        });

        /**
         * Resta el saldo indicado al saldo disponible
         */
        ButtonRestarSaldo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (EditTextReducirSaldo.getText().toString().equals("") || saldoDisponible.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), R.string.no_hay_saldo, Toast.LENGTH_SHORT).show();
                } else {
                    int nuevoDisponible = Integer.parseInt(quitarFormato(saldoDisponible.getText().toString())) - Integer.parseInt(quitarFormato(EditTextReducirSaldo.getText().toString()));

                    if (nuevoDisponible < 0) {
                        Toast.makeText(getActivity(), R.string.saldo_insuficiente, Toast.LENGTH_SHORT).show();
                    } else {
                        saldoDisponible.setText(formatear(Integer.toString(nuevoDisponible)));
                        EditTextReducirSaldo.setText("");
                        Toast.makeText(getActivity(), R.string.saldo_actualizado, Toast.LENGTH_SHORT).show();
                    }
                }

                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
            }
        });

        /**
         * muestra con mensajes TOAST cuando se actualiza el saldo, o cuando esta vacio uina vez apretada la tecla ENTER
         */
        saldoDisponible.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    if (saldoDisponible.getText().toString().equals("")) {

                        Toast.makeText(getActivity(), R.string.saldo_actualizado, Toast.LENGTH_SHORT).show();

                    } else {

                        Toast.makeText(getActivity(), R.string.saldo_actualizado, Toast.LENGTH_SHORT).show();
                    }
                    return true;
                } else {

                    return false;
                }
            }
        });

        /**
         * Si el campo de saldo no esta vacio, crea un intent con el valor del saldo disponible y inicia el activity simular compra
         */
        simulCompra.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (saldoDisponible.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), R.string.saldo_invalido, Toast.LENGTH_SHORT).show();
                    saldoDisponible.setHintTextColor(getResources().getColor(R.color.Saldo_No_Disponible));
                } else {
                    String enviar;
                    saldoDisponible.setHintTextColor(getResources().getColor(R.color.Color_Texto_Hint));
                    Intent aSimular = new Intent(getActivity(), simularCompra.class);
                    enviar = quitarFormato(saldoDisponible.getText().toString());
                    aSimular.putExtra("saldoDisponibleKey", enviar);

                    InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);

                    startActivity(aSimular);
                }
            }
        });

        return root;
    }

    /**
     * Funcion que le quita el formato monetario al string, convirtiendolo en un string numerico sin formato
     *
     * @param saldoDisponible string numerico con formato monetario
     * @return retorna un string sin formato, solo numeros
     */
    public String quitarFormato(String saldoDisponible) {
        String resultado = "";

        if (saldoDisponible.equals("")) {

            resultado = "";
            return resultado;
        } else {

            if (saldoDisponible.contains("$") || saldoDisponible.contains(",") || saldoDisponible.contains(" ")) {
                String[] temporal = saldoDisponible.split("[,  $]");

                for (int i = 0; i < temporal.length; i++) {

                    if (temporal[i].equals("")) {
                        //nada
                    } else {
                        resultado = resultado + temporal[i];
                    }
                    System.out.println(temporal[i]);
                }

                return resultado;
            } else {
                return resultado = saldoDisponible;
            }
        }
    }

    /**
     * Funcion que da formato monetario en pesos al las cifras numeriocas
     *
     * @param saldo corresponde al valor numerico en STRING
     * @return retorna un string con el valor numerico en formato monetario
     */
    private static String formatear(String saldo) {

        if (saldo.equals("")) {
            return "";
        } else {
            String saldoDisponibleIngresado = saldo;
            double saldoDisponibleDouble = Double.parseDouble(saldoDisponibleIngresado);

            DecimalFormatSymbols simbolo = new DecimalFormatSymbols();
            simbolo.setDecimalSeparator('.');
            simbolo.setGroupingSeparator(',');
            DecimalFormat formateador = new DecimalFormat("$ ###,###", simbolo);

            return formateador.format(saldoDisponibleDouble);
        }
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
}
