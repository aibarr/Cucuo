package com.wakaru.cucuo;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class simularCompra extends ActionBarActivity implements DialogoCompra.Comunicator {

    private EditText precioProducto;
    private TextView TexViewNumeroCuotas, TexViewPrecioDelProducto;
    private EditText Edit_Text_Cuotas;
    private ListView ListViewListaTarjetas;
    private String saldoDisponible;
    private String id_image;
    private String costo_total;
    private String archivo = "MyPrefsFile";
    private String archivoHistorial = "MyPrefsFile";
    private VivzAdapter adapter1;
    private VivzAdapter adapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_simular_compra);

        precioProducto = (EditText) findViewById(R.id.editTextPrecioProducto);
        TexViewNumeroCuotas = (TextView) findViewById(R.id.TexViewNumeroCuotas);
        TexViewPrecioDelProducto = (TextView) findViewById(R.id.TexViewPrecioDelProducto);
        Edit_Text_Cuotas = (EditText) findViewById(R.id.editTextNumeroCuotas);
        ListViewListaTarjetas = (ListView) findViewById(R.id.ListViewListaTarjetas);

        adapter1 = new VivzAdapter(this);
        ListViewListaTarjetas.setAdapter(adapter1);

        saldoDisponible = getIntent().getExtras().getString("saldoDisponibleKey");

        /**
         * Maneja la seleccion de una fila en el ListView, cuando es valida la compra llama al dialogo para realizar la compra
         */
        ListViewListaTarjetas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                adapter2 = (VivzAdapter) ListViewListaTarjetas.getAdapter();
                costo_total = adapter2.list.get(position).valor_cuota;
                id_image = String.valueOf(adapter2.list.get(position).image);

                if (costo_total.equals("$ 0") || precioProducto.getText().toString().equals("") || Edit_Text_Cuotas.getText().toString().equals("")) {

                    if (precioProducto.getText().toString().equals("")) {

                        Toast.makeText(getApplicationContext(), "Precio invalido", Toast.LENGTH_SHORT).show();
                    }

                    if (Edit_Text_Cuotas.getText().toString().equals("")) {

                        Toast.makeText(getApplicationContext(), "Cuota invalida", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        if (Integer.parseInt(Edit_Text_Cuotas.getText().toString()) == 1) {

                            Toast.makeText(getApplicationContext(), "2 cuotas min", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {

                    if (Integer.parseInt(quitarFormato(costo_total)) > Integer.parseInt(saldoDisponible)) {

                        Toast.makeText(getApplicationContext(), "Saldo insuficiente", Toast.LENGTH_SHORT).show();

                    } else {

                        showDialog(view);
                    }
                }
            }
        });

        /**
         * Maneja el Hint del EditText de las cuotas, si hay un valor escrito hace visible el Hint superior, en caso contrario lo oculta
         */
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
                    Edit_Text_Cuotas.removeTextChangedListener(this);

                    if (Integer.parseInt(Edit_Text_Cuotas.getText().toString()) < 1) {

                        //Edit_Text_Cuotas.removeTextChangedListener(this);
                        Edit_Text_Cuotas.setText("2");
                        Edit_Text_Cuotas.setSelection(Edit_Text_Cuotas.getText().length());

                        calcularCuotas(saldoDisponible, adapter1);

                        //Edit_Text_Cuotas.addTextChangedListener(this);

                    } else {
                        Edit_Text_Cuotas.setHintTextColor(getResources().getColor(R.color.Cuota_Valido));

                        calcularCuotas(saldoDisponible, adapter1);

                    }
                    Edit_Text_Cuotas.addTextChangedListener(this);
                }
            }
        });

        /**
         * Se encarga de aplicar el formato monetario sobre el precio del producto a medida que se va cambiando el texto
         */
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
                    calcularCuotas(saldoDisponible, adapter1);

                } else {

                    TexViewPrecioDelProducto.setVisibility(TexViewPrecioDelProducto.VISIBLE);
                    precioProducto.setHintTextColor(getResources().getColor(R.color.Color_Texto_Hint));

                    precioProducto.removeTextChangedListener(this);
                    precioProducto.setText(formatear(quitarFormato(precioProducto.getText().toString())));
                    precioProducto.setSelection(precioProducto.getText().length());
                    precioProducto.addTextChangedListener(this);

                    if (formatear(quitarFormato(precioProducto.getText().toString())).equals("")) {

                        TexViewPrecioDelProducto.setVisibility(TexViewPrecioDelProducto.INVISIBLE);
                        calcularCuotas(saldoDisponible, adapter1);

                    } else {
                        precioProducto.setHintTextColor(getResources().getColor(R.color.Precio_Valido));

                        calcularCuotas(saldoDisponible, adapter1);
                    }
                }
            }
        });

        /**
         * Maneja el ingreso de cuotas, ademas realiza el calculo del valor de las cuotas a traves de la funcion "calcularCuotas" cuando se apreta la tecla ENTER
         */
        Edit_Text_Cuotas.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    if (precioProducto.getText().toString().equals("")) {

                        Toast.makeText(getApplicationContext(), "Precio invalido", Toast.LENGTH_SHORT).show();
                        precioProducto.setHintTextColor(getResources().getColor(R.color.Precio_No_Valido));
                        calcularCuotas(saldoDisponible, adapter1);

                        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                        return false;

                    } else {

                        if (Edit_Text_Cuotas.getText().toString().equals("")) {

                            Edit_Text_Cuotas.setHintTextColor(getResources().getColor(R.color.Cuota_No_Valido));

                            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                            return true;

                        } else {

                            Edit_Text_Cuotas.setHintTextColor(getResources().getColor(R.color.Cuota_Valido));

                            if (Integer.parseInt(Edit_Text_Cuotas.getText().toString()) <= 1) {

                                Toast.makeText(getApplicationContext(), "Minimo 2 cuotas", Toast.LENGTH_SHORT).show();

                                Edit_Text_Cuotas.setText("2");
                                calcularCuotas(saldoDisponible, adapter1);

                                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                                return true;

                            } else {

                                calcularCuotas(saldoDisponible, adapter1);

                                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                                return true;
                            }
                        }
                    }
                } else {

                    return false;
                }
            }
        });

        /**
         * Se calcula el valor de las cuotas a traves del procedimiento "calcularCuotas" cuando se apreta la tecla ENTER al momento de escribir el valor del precio del producto.
         */
        precioProducto.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    if (precioProducto.getText().toString().equals("")) {

                        Toast.makeText(getApplicationContext(), "Precio invalido", Toast.LENGTH_SHORT).show();

                        precioProducto.setHintTextColor(getResources().getColor(R.color.Precio_No_Valido));
                        calcularCuotas(saldoDisponible, adapter1);

                        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                        return false;

                    } else {

                        precioProducto.setHintTextColor(getResources().getColor(R.color.Precio_Valido));
                        calcularCuotas(saldoDisponible, adapter1);

                        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                        return true;
                    }

                } else {

                    return false;
                }
            }
        });
    }

    /**
     * Procedimiento encargado de recibir el mensaje del Dialogo de compra
     *
     * @param v
     */
    public void showDialog(View v) {

        FragmentManager manager = getFragmentManager();
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
     * @param adapter1        adaptador que sera seteado en la ListView
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

            adapter1 = new VivzAdapter(getApplicationContext(), colores_valores, nombre_tarjetas, valores_cuotas, images_valores);
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

            adapter1 = new VivzAdapter(getApplicationContext(), colores_valores, nombre_tarjetas, valores_cuotas, images_valores);
            ListViewListaTarjetas.setAdapter(adapter1);
        }
    }

    /**
     * Funcion de prueba para mostrar ejemplo de funcionamiento del calculo del valor de la cuota haciendo uso del metodo Frances
     *
     * @param valorPrecioProducto Valor del producto (EditText)
     * @param tasaInteres         tasa de interes de la tarjeta (double, por ahora se entrega como decimal)
     * @param numeroCuotas        numero de cuotas en que se comprara el producto (int)
     * @return Retorna el valor de la cuota mensual que se debe pagar
     */
    public double metodoFrances(EditText valorPrecioProducto, double tasaInteres, int numeroCuotas) {

        if (valorPrecioProducto.getText().toString().equals("")) {

            Toast.makeText(getApplicationContext(), "Precio invalido", Toast.LENGTH_SHORT).show();
            precioProducto.setHintTextColor(getResources().getColor(R.color.Precio_No_Valido));

            return 0;

        } else {

            if (numeroCuotas == 0) {

                return 0;

            } else if (numeroCuotas == 1) {

                return Integer.parseInt(quitarFormato(valorPrecioProducto.getText().toString()));

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
     * @param saldoDisponibleString Valor del saldo disponible, esta tomando desde PrincipalFragment
     * @param valorCuotaDouble      Valor que tiene la cuota, despues de calcular su valor
     * @return retorna un strint cn color rojo o verde
     */
    public int verificarSaldo(String saldoDisponibleString, double valorCuotaDouble) {

        if (Integer.parseInt(saldoDisponibleString) < valorCuotaDouble) {

            return getResources().getColor(R.color.Saldo_No_Disponible);

        } else {

            return getResources().getColor(R.color.Saldo_Disponible);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.menu_simular_compra, menu);
        //getActionBar().setDisplayHomeAsUpEnabled(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDialogMessage(String message) {

        if (!message.equals("")) {
            Toast.makeText(this, "Compra registrada", Toast.LENGTH_SHORT).show();

            String saldoDiponibleFinal = String.valueOf(Integer.parseInt(saldoDisponible) - Integer.parseInt(quitarFormato(costo_total)));
            SharedPreferences settings = this.getSharedPreferences(archivo, 0);
            SharedPreferences.Editor editor = settings.edit();

            editor.putString("saldoDisponible", formatear(saldoDiponibleFinal));
            editor.commit();

            Calendar c = Calendar.getInstance();
            System.out.println("Current time => " + c.getTime());

            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
            String formattedDate = df.format(c.getTime());

            SharedPreferences settingsHistorial = this.getSharedPreferences(archivoHistorial, 0);

            String datos_nombres_compras = settingsHistorial.getString("nombres_compras", "");
            String datos_valores_totales = settingsHistorial.getString("valores_totales", "");
            String datos_valores_cuotas = settingsHistorial.getString("valores_cuotas", "");
            String datos_fechas = settingsHistorial.getString("fechas", "");
            String datos_images = settingsHistorial.getString("images", "");

            if (datos_nombres_compras.equals("")) {

                SharedPreferences.Editor editorHistorial = settingsHistorial.edit();

                editorHistorial.putString("nombres_compras", message);
                editorHistorial.putString("valores_totales", "Total: " + precioProducto.getText().toString());
                editorHistorial.putString("valores_cuotas", Edit_Text_Cuotas.getText().toString() + " " + "Cuotas de " + costo_total);
                editorHistorial.putString("fechas", formattedDate);
                editorHistorial.putString("images", id_image);

                editorHistorial.commit();

            } else {

                SharedPreferences.Editor editorHistorial = settingsHistorial.edit();

                editorHistorial.putString("nombres_compras", datos_nombres_compras + "/" + message);
                editorHistorial.putString("valores_totales", datos_valores_totales + "/" + "Total: " + precioProducto.getText().toString());
                editorHistorial.putString("valores_cuotas", datos_valores_cuotas + "/" + Edit_Text_Cuotas.getText().toString() + " " + "Cuotas de " + costo_total);
                editorHistorial.putString("fechas", datos_fechas + "/" + formattedDate);
                editorHistorial.putString("images", datos_images + "/" + id_image);

                editorHistorial.commit();
            }

            Intent aSimular = new Intent(getApplicationContext(), Principal.class);
            startActivity(aSimular);

        } else {

        }
    }
}

/**
 * Clase utilizada para manejar los elementos del ListView
 */
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

/**
 * Adaptador definido para el archivo dialogo_compra.XML
 */
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