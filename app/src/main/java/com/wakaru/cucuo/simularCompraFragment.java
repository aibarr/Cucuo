package com.wakaru.cucuo;

import android.app.FragmentManager;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class simularCompraFragment extends Fragment {

    private EditText precioProducto;
    private TextView TexViewNumeroCuotas, TexViewPrecioDelProducto;
    private EditText Edit_Text_Cuotas;
    private ListView ListViewListaTarjetas;

    public simularCompraFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View root2 = inflater.inflate(R.layout.fragment_simular_compra, container, false);

        precioProducto = (EditText) root2.findViewById(R.id.editTextPrecioProducto);
        TexViewNumeroCuotas = (TextView) root2.findViewById(R.id.TexViewNumeroCuotas);
        TexViewPrecioDelProducto = (TextView) root2.findViewById(R.id.TexViewPrecioDelProducto);
        Edit_Text_Cuotas = (EditText) root2.findViewById(R.id.editTextNumeroCuotas);
        ListViewListaTarjetas = (ListView) root2.findViewById(R.id.ListViewListaTarjetas);

        final VivzAdapter adapter1 = new VivzAdapter(root2.getContext());
        ListViewListaTarjetas.setAdapter(adapter1);

        final String saldoDisponible = getActivity().getIntent().getExtras().getString("saldoDisponibleKey");

        ListViewListaTarjetas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                VivzAdapter adapter2 = (VivzAdapter) ListViewListaTarjetas.getAdapter();
                Toast.makeText(getActivity().getApplicationContext(), "Me clickeaste", Toast.LENGTH_SHORT).show();
                showDialog(view);
            }
        });

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
                    TexViewPrecioDelProducto.setVisibility(TexViewPrecioDelProducto.INVISIBLE);
                } else {
                    TexViewPrecioDelProducto.setVisibility(TexViewPrecioDelProducto.VISIBLE);
                    precioProducto.setHintTextColor(getResources().getColor(R.color.Color_Texto_Hint));

                    precioProducto.removeTextChangedListener(this);
                    precioProducto.setText(formatear(quitarFormato(precioProducto.getText().toString())));
                    precioProducto.setSelection(precioProducto.getText().length());
                    precioProducto.addTextChangedListener(this);

                    if (formatear(quitarFormato(precioProducto.getText().toString())).equals("")) {
                        TexViewPrecioDelProducto.setVisibility(TexViewPrecioDelProducto.INVISIBLE);
                    }
                }

                /*if (precioProducto.getText().toString().equals("")) {
                    TexViewPrecioDelProducto.setVisibility(TexViewNumeroCuotas.INVISIBLE);
                } else {
                    TexViewPrecioDelProducto.setVisibility(TexViewNumeroCuotas.VISIBLE);
                }*/
            }
        });

        Edit_Text_Cuotas.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    if (precioProducto.getText().toString().equals("")) {

                        Toast.makeText(getActivity(), "Precio invalido", Toast.LENGTH_SHORT).show();
                        precioProducto.setHintTextColor(getResources().getColor(R.color.Precio_No_Valido));
                        calcularCuotas(saldoDisponible, adapter1);

                        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);

                        return false;

                    } else {

                        if (Edit_Text_Cuotas.getText().toString().equals("")) {

                            Edit_Text_Cuotas.setHintTextColor(getResources().getColor(R.color.Cuota_No_Valido));

                            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);

                            return true;
                        } else {

                            Edit_Text_Cuotas.setHintTextColor(getResources().getColor(R.color.Cuota_Valido));
                            if (Integer.parseInt(Edit_Text_Cuotas.getText().toString()) <= 1) {

                                Toast.makeText(getActivity(), "Mínimo 2 cuotas", Toast.LENGTH_SHORT).show();

                                Edit_Text_Cuotas.setText("2");
                                calcularCuotas(saldoDisponible, adapter1);

                                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                                inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);

                                return true;

                            } else {

                                calcularCuotas(saldoDisponible, adapter1);

                                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                                inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);

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
                        calcularCuotas(saldoDisponible, adapter1);

                        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);

                        return false;

                    } else {

                        Toast.makeText(getActivity(), "Precio actualizado", Toast.LENGTH_SHORT).show();

                        precioProducto.setHintTextColor(getResources().getColor(R.color.Precio_Valido));
                        calcularCuotas(saldoDisponible, adapter1);

                        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);

                        return true;
                    }
                } else {
                    return false;
                }
            }
        });
        return root2;
    }

    public void showDialog(View v) {

        FragmentManager manager = getActivity().getFragmentManager();
        DialogoCompra myDialog = new DialogoCompra();
        myDialog.show(manager, "tag");
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
     * Procedimiento que asgina el valor de cada cuota
     *
     * @param saldoDisponible corresponde al valor del saldo disponible para comprar
     * @param adapter1
     */
    public void calcularCuotas(String saldoDisponible, VivzAdapter adapter1) {

        if (precioProducto.getText().toString().equals("")) {

            List<Integer> colores_valores = new ArrayList<Integer>();
            List<String> nombre_tarjetas = new ArrayList<String>();
            List<String> valores_cuotas = new ArrayList<String>();
            List<Integer> images_valores = new ArrayList<Integer>();

            double valorCuota = 0;


            colores_valores.add(verificarSaldo(saldoDisponible, valorCuota));
            nombre_tarjetas.add("CMR");
            valores_cuotas.add(formatear(String.valueOf(valorCuota)));
            images_valores.add(R.drawable.png_cmr);

            valorCuota = 0;


            colores_valores.add(verificarSaldo(saldoDisponible, valorCuota));
            nombre_tarjetas.add("LaPolar");
            valores_cuotas.add(formatear(String.valueOf(valorCuota)));
            images_valores.add(R.drawable.png_lapolar);

            valorCuota = 0;


            colores_valores.add(verificarSaldo(saldoDisponible, valorCuota));
            nombre_tarjetas.add("Ripley");
            valores_cuotas.add(formatear(String.valueOf(valorCuota)));
            images_valores.add(R.drawable.png_ripley);

            valorCuota = 0;


            colores_valores.add(verificarSaldo(saldoDisponible, valorCuota));
            nombre_tarjetas.add("BBVA");
            valores_cuotas.add(formatear(String.valueOf(valorCuota)));
            images_valores.add(R.drawable.png_bbva);

            adapter1 = new VivzAdapter(getActivity().getApplicationContext(), colores_valores, nombre_tarjetas, valores_cuotas, images_valores);
            ListViewListaTarjetas.setAdapter(adapter1);

        } else {

            if (Edit_Text_Cuotas.getText().toString().equals("")) {
                Edit_Text_Cuotas.setText("2");
            }

            List<Integer> colores_valores = new ArrayList<Integer>();
            List<String> nombre_tarjetas = new ArrayList<String>();
            List<String> valores_cuotas = new ArrayList<String>();
            List<Integer> images_valores = new ArrayList<Integer>();

            double valorCuota = metodoFrances(precioProducto, 0.04, Integer.parseInt(Edit_Text_Cuotas.getText().toString()));


            colores_valores.add(verificarSaldo(saldoDisponible, valorCuota));
            nombre_tarjetas.add("CMR");
            valores_cuotas.add(formatear(String.valueOf(valorCuota)));
            images_valores.add(R.drawable.png_cmr);

            valorCuota = metodoFrances(precioProducto, 0.13, Integer.parseInt(Edit_Text_Cuotas.getText().toString()));


            colores_valores.add(verificarSaldo(saldoDisponible, valorCuota));
            nombre_tarjetas.add("LaPolar");
            valores_cuotas.add(formatear(String.valueOf(valorCuota)));
            images_valores.add(R.drawable.png_lapolar);

            valorCuota = metodoFrances(precioProducto, 0.09, Integer.parseInt(Edit_Text_Cuotas.getText().toString()));


            colores_valores.add(verificarSaldo(saldoDisponible, valorCuota));
            nombre_tarjetas.add("Ripley");
            valores_cuotas.add(formatear(String.valueOf(valorCuota)));
            images_valores.add(R.drawable.png_ripley);

            valorCuota = metodoFrances(precioProducto, 0.20, Integer.parseInt(Edit_Text_Cuotas.getText().toString()));


            colores_valores.add(verificarSaldo(saldoDisponible, valorCuota));
            nombre_tarjetas.add("BBVA");
            valores_cuotas.add(formatear(String.valueOf(valorCuota)));
            images_valores.add(R.drawable.png_bbva);

            adapter1 = new VivzAdapter(getActivity().getApplicationContext(), colores_valores, nombre_tarjetas, valores_cuotas, images_valores);
            ListViewListaTarjetas.setAdapter(adapter1);
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

                double valorCuota = Integer.parseInt(quitarFormato(valorPrecioProducto.getText().toString())) * ((tasaInteres * Math.pow(1 + tasaInteres, numeroCuotas)) / (Math.pow(1 + tasaInteres, numeroCuotas) - 1));
                precioProducto.setHintTextColor(getResources().getColor(R.color.Precio_Valido));
                return valorCuota;
            }
        }
    }

    /**
     * Determina el color del texto de la cuota, rojo si se pasa del saldo disponible, negro si no se pasa del saldo disponible
     *
     * @param saldoDisponibleString Valor del saldo disponible, está tomando desde PrincipalFragment
     * @param valorCuotaDouble      Valor que tiene la cuota, despues de calcular su valor
     * @return retorna un strint cn color rojo o negro
     */
    public int verificarSaldo(String saldoDisponibleString, double valorCuotaDouble) {
        if (Integer.parseInt(saldoDisponibleString) < valorCuotaDouble) {
            //return "#EF5350";
            return getResources().getColor(R.color.Saldo_No_Disponible);
        } else {
            //int color = ;
            return getResources().getColor(R.color.Saldo_Disponible);
        }
    }
}

class SingleRow {
    String nombre_tarjeta;
    String valor_cuota;
    int image;
    int color;

    SingleRow(String nombre_tarjeta, String valor_cuota, int image, int color) {

        this.nombre_tarjeta = nombre_tarjeta;
        this.valor_cuota = valor_cuota;
        this.image = image;
        this.color = color;
    }
}

class VivzAdapter extends BaseAdapter {

    ArrayList<SingleRow> list;
    Context context;

    VivzAdapter(Context c) {

        context = c;
        list = new ArrayList<SingleRow>();
        Resources res = c.getResources();
        String[] titulos_tarjetas = res.getStringArray(R.array.titulos_tarjetas);
        String[] valor_cuotas = res.getStringArray(R.array.valor_cuotas_iniciales);
        int[] images = {R.drawable.png_cmr, R.drawable.png_lapolar, R.drawable.png_ripley, R.drawable.png_bbva};
        int[] color = {c.getResources().getColor(R.color.Color_Texto), c.getResources().getColor(R.color.Color_Texto), c.getResources().getColor(R.color.Color_Texto), c.getResources().getColor(R.color.Color_Texto)};

        for (int i = 0; i < 4; i++) {
            list.add(new SingleRow(titulos_tarjetas[i], valor_cuotas[i], images[i], color[i]));
        }
    }

    VivzAdapter(Context c, List<Integer> colors, List<String> nombre_tarjetas, List<String> valor_cuotas, List<Integer> images) {
        context = c;
        list = new ArrayList<SingleRow>();
        for (int i = 0; i < 4; i++) {
            list.add(new SingleRow(nombre_tarjetas.get(i), valor_cuotas.get(i), images.get(i), colors.get(i)));
        }
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.row_tarjetas, parent, false);

        TextView titulo_tarjeta = (TextView) row.findViewById(R.id.TextViewTituloTarjeta);
        TextView valor_cuota = (TextView) row.findViewById(R.id.TextViewValorCuota);
        ImageView image = (ImageView) row.findViewById(R.id.ImageViewImageViewTarjeta);

        SingleRow temp = list.get(position);

        titulo_tarjeta.setText(temp.nombre_tarjeta);
        valor_cuota.setText(temp.valor_cuota);
        valor_cuota.setTextColor(temp.color);
        image.setImageResource(temp.image);

        return row;
    }
}
