package com.example.ariie.armovil;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Set;


public class Inicio extends AppCompatActivity {
    private BluetoothAdapter bluetooth = null;
    private Set<BluetoothDevice> emparejados;
    private ToggleButton btnBluetooth;
    private ListView listaDispositivos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        btnBluetooth = (ToggleButton)findViewById(R.id.btnBluetooth);
        listaDispositivos = (ListView)findViewById(R.id.lstDispositivos);

        conectarBTH();

        btnBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                dispositivosEmparejados(); //method that will be called
            }
        });
    }

    public void conectarBTH() {
        bluetooth = BluetoothAdapter.getDefaultAdapter();
        if(bluetooth == null)
        {
            //Muestra un mensaje diciendo que el dispositivo no tiene un adaptador blueooth.
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
                //Pida al usuario que active el bluetooth
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
        listaDispositivos.setOnItemClickListener(miLista); //Method called when the device from the list is clicked
    }
    private AdapterView.OnItemClickListener miLista = new AdapterView.OnItemClickListener()
    {
        public void onItemClick (AdapterView<?> av, View v, int arg2, long arg3)
        {
            // Get the device MAC address, the last 17 chars in the View
            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);

            // Make an intent to start next activity.
            Intent i = new Intent( Inicio.this, Inicio.class);

            //Change the activity.
            i.putExtra("", address); //this will be received at NextActivity
            startActivity(i);
        }
    };

}
