package com.wakaru.cucuo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;


/**
 * A placeholder fragment containing a simple view.
 */
public class simularCompraFragment extends Fragment {

    private SimpleAdapter mSchedule;
    private EditText nombreProducto;
    private EditText cuotas;
    private SeekBar barraCuotas;

    public simularCompraFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View root2 = inflater.inflate(R.layout.fragment_simular_compra, container, false);

        nombreProducto = (EditText) root2.findViewById(R.id.IdNombreProducto);


        cuotas = (EditText) root2.findViewById(R.id.editText2);
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
            }
        });

        return root2;
    }
}
