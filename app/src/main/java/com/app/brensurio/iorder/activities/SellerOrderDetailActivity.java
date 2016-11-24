package com.app.brensurio.iorder.activities;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.app.brensurio.iorder.R;
import com.app.brensurio.iorder.fragments.OrderDetailFragment;
import com.app.brensurio.iorder.models.Food;
import com.app.brensurio.iorder.models.Order;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SellerOrderDetailActivity extends AppCompatActivity {

    private String storeName;
    private Order order;

    private DatabaseReference mDatabase;

    // Bluetooth variables
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private OutputStream outputStream = null;
    public static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    public String address = "20:15:04:15:15:60";

    boolean proceed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_order_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
        getSupportActionBar().setTitle("Order details");

        btAdapter = BluetoothAdapter.getDefaultAdapter();
        checkBTState();

        Intent intent = getIntent();
        storeName = intent.getStringExtra("STORE_NAME");
        if (storeName.equalsIgnoreCase("store1"))
            address = "20:15:04:15:15:60"; // Scoops
        else if (storeName.equalsIgnoreCase("store2"))
            address = "20:15:04:16:10:78";
        else if (storeName.equalsIgnoreCase("store3"))
            address = "20:15:04:16:10:93"; // ovenmade

        mDatabase = FirebaseDatabase.getInstance().getReference();

        Bundle bundle = new Bundle();
        order = getIntent().getParcelableExtra("order");
        bundle.putParcelable("order", order);
        Fragment fragment = new OrderDetailFragment();
        fragment.setArguments(bundle);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.placeholder, fragment, "visible_fragment");
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    /**
     * Dispatch incoming result to the correct fragment.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            proceed = true;
        }
    }

   /* @Override
    public void onResume() {
        super.onResume();
        Log.d("onRESUME", "...onResume - try connect...");
        BluetoothDevice device = btAdapter.getRemoteDevice(address);
        try {
            btSocket = createBluetoothSocket(device);
        } catch (IOException e1) {
            errorExit("Fatal Error", "In onResume() and socket create failed: " + e1.getMessage() + ".");
        }
        btAdapter.cancelDiscovery();
        Log.d("CONNECTING:", "...Connecting...");
        try {
            btSocket.connect();
            Log.d("CONNECTION OK:", "...Connection ok...");
        }catch (IOException e) {
            try {
                btSocket.close();
            } catch (IOException e2) {
                errorExit("Fatal Error", "In onResume() and unable to close socket during connection failure" + e2.getMessage() + ".");
            }
        }

        Log.d("CREATING SOCKET:", "...Create Socket...");

        try{
            outputStream = btSocket.getOutputStream();
        }catch (IOException e) {
            errorExit("Fatal Error", "In onResume() and output stream creation failed:" + e.getMessage() + ".");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("ON PAUSE:", "...In onPause()...");
        if (outputStream != null) {
            try {
                outputStream.flush();
            } catch (IOException e) {
                errorExit("Fatal Error", "In onPause() and failed to flush output stream: " + e.getMessage() + ".");
            }
        }
        try     {
            btSocket.close();
        } catch (IOException e2) {
            errorExit("Fatal Error", "In onPause() and failed to close socket." + e2.getMessage() + ".");
        }
    }*/

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        if(Build.VERSION.SDK_INT >= 10){
            try {
                final Method m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord",
                        UUID.class);
                return (BluetoothSocket) m.invoke(device, MY_UUID);
            } catch (Exception e) {
                Log.e("ERROR", "Could not create Insecure RFComm Connection",e);
            }
        }
        return  device.createRfcommSocketToServiceRecord(MY_UUID);
    }

    private void checkBTState() {
        if(btAdapter == null){
            errorExit("Fatal Error", "Bluetooth not support");
        } else {
            if (btAdapter.isEnabled()) {
                Log.d("BT IS ON:", "...Bluetooth ON...");
            } else {
                //Prompt user to turn on Bluetooth
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }
    }

    private void errorExit(String title, String message){
        Toast.makeText(getBaseContext(), title + " - " + message, Toast.LENGTH_LONG).show();
    }

    private void transmitData(String data) {
        try {
            outputStream.write(data.getBytes());
        } catch (IOException e) {
            String msg = "In onResume() and an exception occurred during write: " + e.getMessage();
            if (address.equals("00:00:00:00:00:00"))
                msg = msg + ".\n\nUpdate your server address from 00:00:00:00:00:00 to the correct address on line 35 in the java code";
            msg = msg +  ".\n\nCheck that the SPP UUID: " + MY_UUID.toString() + " exists on server.\n\n";
            errorExit("Fatal Error", msg);
        }
    }

    private void print() {
        // total | date | refno | location | subtotal1 | foodname1 | quantity1 | subtotalN | foodnameN | quantityN
        String data;

        String[] name = order.getCustomerName().split("\\s+");
        int last = name.length - 1;
        String total = Double.toString(order.getAmount());
        String date = order.getDatetime().substring(0, 17);
        String refNo = order.getRefNo().substring(6, 12);
        String location = order.getLocation();

        data = name[0] + "|" + name[last] + "|" + total + "|" + date + "|" + refNo + "|" + location + "|";

        for (Food food : order.getItems()) {
            String subtotal;
            String price;
            String foodname;
            String quantity;

            subtotal = Double.toString(food.getPrice() * food.getAmount());
            price = Double.toString(food.getPrice());
            foodname = food.getName();
            quantity = Integer.toString(food.getAmount());

            data += subtotal + "|" + price + "|" + quantity + "|" + foodname + "|";
        }

        transmitData(data);

        DatabaseReference databaseReference =
                mDatabase.child("storeorders")
                        .child(order.getRefNo());
        Map<String, Object> statusUpdate = new HashMap<>();
        statusUpdate.put("status", "CONFIRMED");
        databaseReference.updateChildren(statusUpdate);
    }
}
