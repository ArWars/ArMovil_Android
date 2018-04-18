package com.example.ariie.armovil;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class Dispositivos extends ActionBarActivity {
    private BluetoothAdapter bluetooth = null;
    private Set<BluetoothDevice> emparejados;
    private Button btnEmparejamiento;
    private ListView listaDispositivos;
    public static String DISPOSITIVO_EXTRA = "DISPOSITIVO_EXTRA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispositivos);

        btnEmparejamiento = (Button)findViewById(R.id.btnEmparejar);
        listaDispositivos = (ListView)findViewById(R.id.lstDispositivos);

        conectarBTH();

        btnEmparejamiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                dispositivosEmparejados(); // Se llaman los dispostivos emparejados.
            }
        });
    }
    public void conectarBTH() {
        bluetooth = BluetoothAdapter.getDefaultAdapter();
        if(bluetooth == null)
        {
            // Muestra un mensaje diciendo que el dispositivo no tiene un adaptador blueooth.
            Toast.makeText(getApplicationContext(), "Este dispositivo no tiene un adaptador bluetooth", Toast.LENGTH_LONG).show();
            // Finaliza el APK
            finish();
        }
        else
        {
            if (bluetooth.isEnabled())
            { }
            else
            {
                // Pida al usuario que active el bluetooth
                Intent turnBTon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(turnBTon,1);
            }
        }
    }
    private void dispositivosEmparejados()
    {
        emparejados = bluetooth.getBondedDevices();
        ArrayList lista = new ArrayList();

        if (emparejados.size()>0)
        {
            for(BluetoothDevice bt : emparejados)
            {
                lista.add(bt.getName() + "\n" + bt.getAddress());
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(), "No hay ningun dispositivo bluetooth emparejado.", Toast.LENGTH_LONG).show();
        }

        final ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, lista);
        listaDispositivos.setAdapter(adapter);
        listaDispositivos.setOnItemClickListener(miLista); // Método llamado cuando se hace clic en el dispositivo de la lista
    }
    private AdapterView.OnItemClickListener miLista = new AdapterView.OnItemClickListener()
    {
        public void onItemClick (AdapterView<?> av, View v, int arg2, long arg3)
        {
            // Obtenga la dirección MAC del dispositivo, los últimos 17 caracteres en la Vista
            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);

            // Intenta comenzar la siguiente actividad.
            Intent i = new Intent( Dispositivos.this, Inicio.class);

            //Change the activity.
            i.putExtra(DISPOSITIVO_EXTRA, address); // Esto será recibido en NextActivity
            startActivity(i);
        }
    };
}
