package com.wakaru.cucuo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;


/**
 * A placeholder fragment containing a simple view.
 */
public class simularCompraFragment extends Fragment {

    private EditText precioProducto;
    private TextView cuotas, tarjeta1, tarjeta2, tarjeta3, tarjeta4, tarjeta5;
    private SeekBar barraCuotas;

    public simularCompraFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View root2 = inflater.inflate(R.layout.fragment_simular_compra, container, false);

        precioProducto = (EditText) root2.findViewById(R.id.editTextPrecioProducto);
        cuotas = (TextView) root2.findViewById(R.id.textViewCuotasSeleccionadas);
        tarjeta1 = (TextView) root2.findViewById(R.id.textViewCuotaTarjeta1);
        tarjeta2 = (TextView) root2.findViewById(R.id.textViewCuotaTarjeta2);
        tarjeta3 = (TextView) root2.findViewById(R.id.textViewCuotaTarjeta3);
        tarjeta4 = (TextView) root2.findViewById(R.id.textViewCuotaTarjeta4);
        tarjeta5 = (TextView) root2.findViewById(R.id.textViewCuotaTarjeta5);
        barraCuotas = (SeekBar) root2.findViewById(R.id.seekBar);
        barraCuotas.setMax(48);

        cuotas.setText(barraCuotas.getProgress() + "/" + barraCuotas.getMax());

        /**
         * Actualiza el valor de las cuotas, de acuerdo al valor de la barra deslizante
         */
        barraCuotas.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int numeroBarra;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                numeroBarra = progress;
                cuotas.setText(progress + "/" + barraCuotas.getMax());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                cuotas.setText(numeroBarra + "/" + barraCuotas.getMax());
                double valorCuota = metodoFrances(precioProducto, 0.04, numeroBarra);
                tarjeta1.setText(new DecimalFormat("#.##").format(valorCuota));
                valorCuota = metodoFrances(precioProducto, 0.13, numeroBarra);
                tarjeta2.setText(new DecimalFormat("#.##").format(valorCuota));
                valorCuota = metodoFrances(precioProducto, 0.09, numeroBarra);
                tarjeta3.setText(new DecimalFormat("#.##").format(valorCuota));
                valorCuota = metodoFrances(precioProducto, 0.20, numeroBarra);
                tarjeta4.setText(new DecimalFormat("#.##").format(valorCuota));
                valorCuota = metodoFrances(precioProducto, 0.15, numeroBarra);
                tarjeta5.setText(new DecimalFormat("#.##").format(valorCuota));
            }
        });

        precioProducto.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    if (precioProducto.getText().toString().equals("")) {
                        Toast.makeText(getActivity(), "Precio invalido", Toast.LENGTH_SHORT).show();
                        return false;
                    } else {
                        Toast.makeText(getActivity(), "Precio actualizado", Toast.LENGTH_SHORT).show();

                        cuotas.setText(barraCuotas.getProgress() + "/" + barraCuotas.getMax());
                        double valorCuota = metodoFrances(precioProducto, 0.04, barraCuotas.getProgress());
                        tarjeta1.setText(new DecimalFormat("#.##").format(valorCuota));
                        valorCuota = metodoFrances(precioProducto, 0.13, barraCuotas.getProgress());
                        tarjeta2.setText(new DecimalFormat("#.##").format(valorCuota));
                        valorCuota = metodoFrances(precioProducto, 0.09, barraCuotas.getProgress());
                        tarjeta3.setText(new DecimalFormat("#.##").format(valorCuota));
                        valorCuota = metodoFrances(precioProducto, 0.20, barraCuotas.getProgress());
                        tarjeta4.setText(new DecimalFormat("#.##").format(valorCuota));
                        valorCuota = metodoFrances(precioProducto, 0.15, barraCuotas.getProgress());
                        tarjeta5.setText(new DecimalFormat("#.##").format(valorCuota));

                        return true;
                    }
                } else {
                    return false;
                }
            }
        });
        return root2;
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
            precioProducto.setHintTextColor(Color.parseColor("#F44336"));
            return 0;
        } else {
            if (numeroCuotas == 0) {
                return 0;
            } else if (numeroCuotas == 1) {
                return Integer.parseInt(valorPrecioProducto.getText().toString());
            } else {
                double valorCuota = Integer.parseInt(valorPrecioProducto.getText().toString()) * ((tasaInteres * Math.pow(1 + tasaInteres, numeroCuotas)) / (Math.pow(1 + tasaInteres, numeroCuotas) - 1));
                precioProducto.setHintTextColor(Color.parseColor("#000000"));
                return valorCuota;
            }
        }
    }
}
