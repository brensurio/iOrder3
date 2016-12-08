package com.app.brensurio.iorder.activities;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.app.brensurio.iorder.R;
import com.app.brensurio.iorder.adapters.CustomerOrderAdapter;
import com.app.brensurio.iorder.models.Food;
import com.app.brensurio.iorder.models.Order;
import com.app.brensurio.iorder.ui.CustomRecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class TransactionHistoryActivity extends AppCompatActivity {

    private String storeName;
    private List<Order> transactionList;
    private DatabaseReference mDatabase;

    // Bluetooth variables
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private OutputStream outputStream = null;
    public static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private String address = "20:15:04:15:15:60";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
        getSupportActionBar().setTitle("Today's Transaction");

        storeName = getIntent().getStringExtra("storeName");
        transactionList = new ArrayList<>();

        btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (storeName.equalsIgnoreCase("store1"))
            address = "20:15:04:15:15:60"; // Scoops
        else if (storeName.equalsIgnoreCase("store2"))
            address = "20:15:04:16:10:78";
        else if (storeName.equalsIgnoreCase("store3"))
            address = "20:15:04:16:10:93"; // ovenmade

        mDatabase = FirebaseDatabase.getInstance().getReference();
        Query query = mDatabase.child("storeorders").orderByChild("store")
                .equalTo(storeName);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    Order order = singleSnapshot.getValue(Order.class);
                    if (order.getStatus().equalsIgnoreCase("CONFIRMED")) {
                        DateFormat df = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
                        Date orderDate;
                        Date todayDate;
                        try {
                            orderDate = df.parse(order.getDatetime());
                            todayDate = df.parse(DateFormat.getDateTimeInstance().format(new Date()));
                            if (orderDate.equals(todayDate))
                                transactionList.add(order);

                        } catch (ParseException e) {
                            //Handle exception here, most of the time you will just log it.
                            e.printStackTrace();
                        }
                    }
                }

                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.custom_recycler_view);
                CustomerOrderAdapter customerOrderAdapter =
                        new CustomerOrderAdapter(getBaseContext(), transactionList);
                LinearLayoutManager linearLayoutManager =
                        new LinearLayoutManager(getBaseContext());
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(customerOrderAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });

        Button printbutton = (Button) findViewById(R.id.print_button);
        printbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                print();
            }
        });
    }

    @Override
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
        } catch (IOException e) {
            try {
                btSocket.close();
            } catch (IOException e2) {
                errorExit("Fatal Error", "In onResume() and unable to close socket during connection failure" + e2.getMessage() + ".");
            }
        }
        Log.d("CREATING SOCKET:", "...Create Socket...");
        try {
            outputStream = btSocket.getOutputStream();
        } catch (IOException e) {
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
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        if(Build.VERSION.SDK_INT >= 10){
            try {
                final Method m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord",
                        new Class[] { UUID.class });
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
                print();
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

    private void print() {
        // total | date | refno | location | subtotal1 | foodname1 | quantity1 | subtotalN | foodnameN | quantityN
        for (Order order : transactionList) {
            String data = "";

            String[] name = order.getCustomerName().split("\\s+");
            int last = name.length - 1;
            String total = Double.toString(order.getAmount());
            String date = order.getDatetime().substring(0, 17);
            String refNo = order.getRefNo().substring(6, 12).toString();
            String location = order.getLocation();

            data = name[0] + "|" + name[last] + "|" + total + "|" + date + "|" + refNo + "|" + location + "|";

            for (Food food : order.getItems()) {
                String subtotal, price, foodname, quantity;
                subtotal = Double.toString(food.getPrice() * food.getAmount());
                price = Double.toString(food.getPrice());
                foodname = food.getName();
                quantity = Integer.toString(food.getAmount());
                data += subtotal + "|" + price + "|" + quantity + "|" + foodname + "|";
            }

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
    }
}
