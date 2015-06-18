package com.wakaru.cucuo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class Historial extends ActionBarActivity {

    private ListView ListViewListaHistorial;
    private List<String> nombres_compras, valores_totales, valores_cuotas, fechas, images;
    private String archivoHistorial = "MyPrefsFile";
    private TextView EditTextSinCompras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);

        EditTextSinCompras = (TextView) findViewById(R.id.EditTextSinCompras);
        ListViewListaHistorial = (ListView) findViewById(R.id.ListViewListaHistorial);
        nombres_compras = new ArrayList<String>();
        valores_totales = new ArrayList<String>();
        valores_cuotas = new ArrayList<String>();
        fechas = new ArrayList<String>();
        images = new ArrayList<String>();

        SharedPreferences settingsHistorial = this.getSharedPreferences(archivoHistorial, 0);

        String datos_nombres_compras = settingsHistorial.getString("nombres_compras", "");
        String datos_valores_totales = settingsHistorial.getString("valores_totales", "");
        String datos_valores_cuotas = settingsHistorial.getString("valores_cuotas", "");
        String datos_fechas = settingsHistorial.getString("fechas", "");
        String datos_images = settingsHistorial.getString("images", "");

        if (datos_nombres_compras.equals("")) {

            EditTextSinCompras.setVisibility(EditTextSinCompras.VISIBLE);

        } else {

            EditTextSinCompras.setVisibility(EditTextSinCompras.INVISIBLE);
            String[] nombres_compras_string = datos_nombres_compras.split("/");
            String[] valores_totales_string = datos_valores_totales.split("/");
            String[] valores_cuotas_string = datos_valores_cuotas.split("/");
            String[] fechas_string = datos_fechas.split("/");
            String[] images_string = datos_images.split("/");

            VivzAdapterHistorial adapter = new VivzAdapterHistorial(this, nombres_compras_string, valores_totales_string, valores_cuotas_string, fechas_string, images_string);
            ListViewListaHistorial.setAdapter(adapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_historial, menu);
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
}

class SingleRowHistorial {
    String nombre_compra;
    String valor_total;
    String valor_cuota;
    String fecha;
    String image;

    SingleRowHistorial(String nombre_compra, String valor_total, String valor_cuota, String fecha, String image) {

        this.nombre_compra = nombre_compra;
        this.valor_total = valor_total;
        this.valor_cuota = valor_cuota;
        this.fecha = fecha;
        this.image = image;
    }
}

class VivzAdapterHistorial extends BaseAdapter {

    ArrayList<SingleRowHistorial> list;
    Context context;

    VivzAdapterHistorial(Context c, String[] nombres_compras, String[] valores_totales, String[] valores_cuotas, String[] fechas, String[] images) {
        context = c;
        list = new ArrayList<SingleRowHistorial>();
        for (int i = 0; i < nombres_compras.length; i++) {
            list.add(new SingleRowHistorial(nombres_compras[i], valores_totales[i], valores_cuotas[i], fechas[i], images[i]));
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
        View row = inflater.inflate(R.layout.row_historial, parent, false);

        TextView HistorialNombre = (TextView) row.findViewById(R.id.TextViewHistorialNombre);
        TextView FechaCompra = (TextView) row.findViewById(R.id.TextViewFechaCompra);
        TextView CostoTotal = (TextView) row.findViewById(R.id.TextViewValorTotal);
        TextView CuotasValorCuota = (TextView) row.findViewById(R.id.TextViewValorCuota);
        ImageView ImageViewFotoHistorial = (ImageView) row.findViewById(R.id.ImageViewFotoHistorial);

        SingleRowHistorial temp = list.get(position);

        HistorialNombre.setText(temp.nombre_compra);
        CostoTotal.setText(temp.valor_total);
        CuotasValorCuota.setText(temp.valor_cuota);
        FechaCompra.setText(temp.fecha);
        ImageViewFotoHistorial.setImageResource(Integer.parseInt(temp.image));

        return row;
    }
}
