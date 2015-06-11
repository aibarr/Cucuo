package com.wakaru.cucuo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;


/**
 * A placeholder fragment containing a simple view.
 */
public class simularCompraFragment extends Fragment {

    private EditText precioProducto;
    private TextView tarjeta1, tarjeta2, tarjeta3, tarjeta4, TexViewNumeroCuotas, TexViewPrecioDelProducto;
    private EditText Edit_Text_Cuotas;

    public simularCompraFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View root2 = inflater.inflate(R.layout.fragment_simular_compra, container, false);

        precioProducto = (EditText) root2.findViewById(R.id.editTextPrecioProducto);
        tarjeta1 = (TextView) root2.findViewById(R.id.textViewCuotaTarjeta1);
        tarjeta2 = (TextView) root2.findViewById(R.id.textViewCuotaTarjeta2);
        tarjeta3 = (TextView) root2.findViewById(R.id.textViewCuotaTarjeta3);
        tarjeta4 = (TextView) root2.findViewById(R.id.textViewCuotaTarjeta4);
        TexViewNumeroCuotas = (TextView) root2.findViewById(R.id.TexViewNumeroCuotas);
        TexViewPrecioDelProducto = (TextView) root2.findViewById(R.id.TexViewPrecioDelProducto);
        Edit_Text_Cuotas = (EditText) root2.findViewById(R.id.editTextNumeroCuotas);

        final String saldoDisponible = getActivity().getIntent().getExtras().getString("saldoDisponibleKey");

        Edit_Text_Cuotas.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (Edit_Text_Cuotas.getText().toString().equals("")) {
                    TexViewNumeroCuotas.setVisibility(TexViewNumeroCuotas.INVISIBLE);
                } else {
                    TexViewNumeroCuotas.setVisibility(TexViewNumeroCuotas.VISIBLE);
                }
            }
        });

        precioProducto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (precioProducto.getText().toString().equals("")) {
                    TexViewPrecioDelProducto.setVisibility(TexViewNumeroCuotas.INVISIBLE);
                } else {
                    TexViewPrecioDelProducto.setVisibility(TexViewNumeroCuotas.VISIBLE);
                }
            }
        });

        Edit_Text_Cuotas.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    if (precioProducto.getText().toString().equals("")) {

                        Toast.makeText(getActivity(), "Precio invalido", Toast.LENGTH_SHORT).show();

                        precioProducto.setHintTextColor(getResources().getColor(R.color.Precio_No_Valido));
                        calcularCuotas(saldoDisponible, tarjeta1, tarjeta2, tarjeta3, tarjeta4);

                        return false;

                    } else {

                        if (Edit_Text_Cuotas.getText().toString().equals("")) {

                            Edit_Text_Cuotas.setHintTextColor(getResources().getColor(R.color.Cuota_No_Valido));

                            return true;
                        } else {

                            Edit_Text_Cuotas.setHintTextColor(getResources().getColor(R.color.Cuota_Valido));
                            if (Integer.parseInt(Edit_Text_Cuotas.getText().toString()) <= 1) {

                                Toast.makeText(getActivity(), "Mínimo 2 cuotas", Toast.LENGTH_SHORT).show();

                                Edit_Text_Cuotas.setText("2");
                                calcularCuotas(saldoDisponible, tarjeta1, tarjeta2, tarjeta3, tarjeta4);

                                return true;

                            } else {

                                calcularCuotas(saldoDisponible, tarjeta1, tarjeta2, tarjeta3, tarjeta4);

                                return true;
                            }
                        }
                    }
                } else {

                    return false;
                }
            }
        });

        precioProducto.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    if (precioProducto.getText().toString().equals("")) {

                        Toast.makeText(getActivity(), "Precio invalido", Toast.LENGTH_SHORT).show();

                        precioProducto.setHintTextColor(getResources().getColor(R.color.Precio_No_Valido));
                        calcularCuotas(saldoDisponible, tarjeta1, tarjeta2, tarjeta3, tarjeta4);

                        return false;

                    } else {

                        Toast.makeText(getActivity(), "Precio actualizado", Toast.LENGTH_SHORT).show();

                        precioProducto.setHintTextColor(getResources().getColor(R.color.Precio_Valido));
                        calcularCuotas(saldoDisponible, tarjeta1, tarjeta2, tarjeta3, tarjeta4);

                        return true;
                    }
                } else {
                    return false;
                }
            }
        });
        return root2;
    }

    public void calcularCuotas(String saldoDisponible, TextView tarjeta1, TextView tarjeta2, TextView tarjeta3, TextView tarjeta4) {

        DecimalFormatSymbols simbolo = new DecimalFormatSymbols();
        simbolo.setDecimalSeparator('.');
        simbolo.setGroupingSeparator(',');
        DecimalFormat formateador = new DecimalFormat("$ ###,###", simbolo);

        if (precioProducto.getText().toString().equals("")) {
            double valorCuota = 0;
            tarjeta1.setText(formateador.format(Double.parseDouble(new DecimalFormat("#").format(valorCuota))));
            tarjeta1.setTextColor(verificarSaldo(saldoDisponible, valorCuota));

            valorCuota = 0;
            tarjeta2.setText(formateador.format(Double.parseDouble(new DecimalFormat("#").format(valorCuota))));
            tarjeta2.setTextColor(verificarSaldo(saldoDisponible, valorCuota));

            valorCuota = 0;
            tarjeta3.setText(formateador.format(Double.parseDouble(new DecimalFormat("#").format(valorCuota))));
            tarjeta3.setTextColor(verificarSaldo(saldoDisponible, valorCuota));

            valorCuota = 0;
            tarjeta4.setText(formateador.format(Double.parseDouble(new DecimalFormat("#").format(valorCuota))));
            tarjeta4.setTextColor(verificarSaldo(saldoDisponible, valorCuota));
        } else {

            if (Edit_Text_Cuotas.getText().toString().equals("")) {
                Edit_Text_Cuotas.setText("2");
            }

            double valorCuota = metodoFrances(precioProducto, 0.04, Integer.parseInt(Edit_Text_Cuotas.getText().toString()));
            tarjeta1.setText(formateador.format(Double.parseDouble(new DecimalFormat("#").format(valorCuota))));
            tarjeta1.setTextColor(verificarSaldo(saldoDisponible, valorCuota));

            valorCuota = metodoFrances(precioProducto, 0.13, Integer.parseInt(Edit_Text_Cuotas.getText().toString()));
            tarjeta2.setText(formateador.format(Double.parseDouble(new DecimalFormat("#").format(valorCuota))));
            tarjeta2.setTextColor(verificarSaldo(saldoDisponible, valorCuota));

            valorCuota = metodoFrances(precioProducto, 0.09, Integer.parseInt(Edit_Text_Cuotas.getText().toString()));
            tarjeta3.setText(formateador.format(Double.parseDouble(new DecimalFormat("#").format(valorCuota))));
            tarjeta3.setTextColor(verificarSaldo(saldoDisponible, valorCuota));

            valorCuota = metodoFrances(precioProducto, 0.20, Integer.parseInt(Edit_Text_Cuotas.getText().toString()));
            tarjeta4.setText(formateador.format(Double.parseDouble(new DecimalFormat("#").format(valorCuota))));
            tarjeta4.setTextColor(verificarSaldo(saldoDisponible, valorCuota));
        }
    }

    /**
     * Función de prueba para mostrar ejemplo de funcionamiento del calculo del valor de la cuota haciendo uso del método Frances
     *
     * @param valorPrecioProducto Valor del producto (EditText)
     * @param tasaInteres         tasa de interés de la tarjeta (double, por ahora se entrega como decimal)
     * @param numeroCuotas        número de cuotas en que se comprará el producto (int)
     * @return Retorna el valor de la cuota mensual que se debe pagar
     */
    public double metodoFrances(EditText valorPrecioProducto, double tasaInteres, int numeroCuotas) {

        if (valorPrecioProducto.getText().toString().equals("")) {

            Toast.makeText(this.getActivity(), "Precio invalido", Toast.LENGTH_SHORT).show();
            precioProducto.setHintTextColor(getResources().getColor(R.color.Precio_No_Valido));

            return 0;
        } else {

            if (numeroCuotas == 0) {

                return 0;
            } else if (numeroCuotas == 1) {

                return Integer.parseInt(valorPrecioProducto.getText().toString());
            } else {

                double valorCuota = Integer.parseInt(valorPrecioProducto.getText().toString()) * ((tasaInteres * Math.pow(1 + tasaInteres, numeroCuotas)) / (Math.pow(1 + tasaInteres, numeroCuotas) - 1));
                precioProducto.setHintTextColor(getResources().getColor(R.color.Precio_Valido));
                return valorCuota;
            }
        }
    }

    /**
     * Determina el color del texto de la cuota, rojo si se pasa del saldo disponible, negro si no se pasa del saldo disponible
     * @param saldoDisponibleString Valor del saldo disponible, está tomando desde PrincipalFragment
     * @param valorCuotaDouble Valor que tiene la cuota, despues de calcular su valor
     * @return retorna un strint cn color rojo o negro
     */
    public int verificarSaldo(String saldoDisponibleString, double valorCuotaDouble) {
        if(Integer.parseInt(saldoDisponibleString) < valorCuotaDouble){
            //return "#EF5350";
            return getResources().getColor(R.color.Saldo_No_Disponible);
        }
        else{
            //int color = ;
            return getResources().getColor(R.color.Saldo_Disponible);
        }
    }
}
