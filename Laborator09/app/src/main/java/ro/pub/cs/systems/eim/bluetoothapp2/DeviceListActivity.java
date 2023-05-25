package ro.pub.cs.systems.eim.bluetoothapp2;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.Set;

public class DeviceListActivity extends AppCompatActivity {
    private ProgressBar progressScanDevices;
    private ArrayAdapter<String> adapterPairedDevices, adapterAvailableDevices;
    private BluetoothAdapter bluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);
        init();
    }

    private void init() {
        ListView listPairedDevices = findViewById(R.id.list_paired_devices);
        ListView listAvailableDevices = findViewById(R.id.list_available_devices);
        progressScanDevices = findViewById(R.id.progress_scan_devices);

        adapterPairedDevices = new ArrayAdapter<>(this, R.layout.device_list_item);
        adapterAvailableDevices = new ArrayAdapter<>(this, R.layout.device_list_item);

        listPairedDevices.setAdapter(adapterPairedDevices);
        listAvailableDevices.setAdapter(adapterAvailableDevices);

        listAvailableDevices.setOnItemClickListener(this::onDeviceClick);
        listPairedDevices.setOnItemClickListener(this::onDeviceClick);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        populatePairedDevices();

        registerReceiver(bluetoothDeviceListener, new IntentFilter(BluetoothDevice.ACTION_FOUND));
        registerReceiver(bluetoothDeviceListener, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED));
    }

    private void onDeviceClick(AdapterView<?> adapterView, View view, int i, long l) {
        //TODO
    }

    private void populatePairedDevices() {
        // TODO
    }

    private final BroadcastReceiver bluetoothDeviceListener = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO
        }
    };

    private void onDiscoveryFinished() {
        // TODO
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_device_list, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_scan_devices) {
            scanDevices();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void scanDevices() {
        // TODO
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(bluetoothDeviceListener);
    }
}