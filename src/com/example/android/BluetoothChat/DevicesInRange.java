package com.example.android.BluetoothChat;

import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class DevicesInRange extends Activity {

	
	 	private static final String TAG = "DevicesInRange";
	    private static final boolean D = true;

	    // Return Intent extra
	    public static String EXTRA_DEVICE_ADDRESS = "device_address";
	    private BluetoothAdapter mBtAdapter;
	    
	    private Bundle AllDevicesBundle;
	    //private ArrayAdapter<String> mPairedDevicesArrayAdapter;
	    //private ArrayAdapter<String> mNewDevicesArrayAdapter;
	    
	    int count=0;
	    
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        
	        //requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
	        //setContentView(R.layout.main);
	        
	        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
	        System.out.println("Bluetooth adapter : "+ mBtAdapter);
	        
	        if(D) Log.d(TAG, "onCreateDiscovery");
	        doDiscovery();
	        
	        // Register for broadcasts when a device is discovered
	        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
	        this.registerReceiver(mReceiver, filter);

	        // Register for broadcasts when discovery has finished
	        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
	        this.registerReceiver(mReceiver, filter);
	        
	        // Get the local Bluetooth adapter
	        
	    }
	    
	    /*public void OnStart() {
	        super.onStart();
	        
	        if(D) Log.d(TAG, "onCreateDiscovery");
	        doDiscovery();
	        // Register for broadcasts when a device is discovered
	        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
	        this.registerReceiver(mReceiver, filter);

	        // Register for broadcasts when discovery has finished
	        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
	        this.registerReceiver(mReceiver, filter);
	        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
	    }*/
	    
	    @Override
	    protected void onDestroy() {
	        super.onDestroy();

	        // Make sure we're not doing discovery anymore
	        if (mBtAdapter != null) {
	            mBtAdapter.cancelDiscovery();
	        }

	        // Unregister broadcast listeners
	        this.unregisterReceiver(mReceiver);
	    }
	    
	    /**
	     * Start device discover with the BluetoothAdapter
	     */
	    private void doDiscovery() {
	        if (D) Log.d(TAG, "doDiscovery()");

	        // Indicate scanning in the title
	        //setProgressBarIndeterminateVisibility(true);
	        //setTitle(R.string.scanning);

	        // Turn on sub-title for new devices
	       // findViewById(R.id.title_new_devices).setVisibility(View.VISIBLE);

	        // If we're already discovering, stop it
	        if( mBtAdapter != null){
	        if (mBtAdapter.isDiscovering()) {
	            mBtAdapter.cancelDiscovery();
	        }
	        }
	        // Request discover from BluetoothAdapter
	        mBtAdapter.startDiscovery();
	        if (D) Log.d(TAG, "startedDiscovery");
	    }
	    
	    // The BroadcastReceiver that listens for discovered devices and
	    // changes the title when discovery is finished
	    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
	    	 @Override
	         public void onReceive(Context context, Intent intent) {
	    		 String action = intent.getAction();
	    		 if (D) Log.d(TAG, "Listeningfordevices");
	             // When discovery finds a device
	             if (BluetoothDevice.ACTION_FOUND.equals(action)) {
	                 // Get the BluetoothDevice object from the Intent
	            	 if (D) Log.d(TAG, "Hooray!!! Found a device");
	                 BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
	                 //Add to the list of all available devices
	                 System.out.println(device.getAddress());
	                 AllDevicesBundle.putString("Address"+count, device.getAddress());
	                 count++;
	                 
	                 // If it's already paired, skip it, because it's been listed already
	                 /*if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
	                     mNewDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
	                 }*/
	                 
	             
	             } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
	            	 if (D) Log.d(TAG, "Finished Discovery");
	             		finishDiscovery(count);
	             	
	             		
	                 /*setProgressBarIndeterminateVisibility(false);
	                 setTitle(R.string.select_device);
	                 if (mNewDevicesArrayAdapter.getCount() == 0) {
	                     String noDevices = getResources().getText(R.string.none_found).toString();
	                     mNewDevicesArrayAdapter.add(noDevices);
	                 }*/
	             }
	    		 
	    	 }
	    	
	    	
	    };
	    
	    public void finishDiscovery(int count)
	    {
	    	Intent intent = new Intent();
	        intent.putExtra("AddressBundle", AllDevicesBundle);
	        intent.putExtra("count", count);
	        setResult(Activity.RESULT_OK, intent);
	        finish();
	    }
	    
}
