package com.bluefletch.spectralink.plugin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;


import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import com.bluefletch.spectralink.BarcodeScanner;
import com.bluefletch.spectralink.ScanCallback;

public class SpectralinkCordovaPlugin extends CordovaPlugin {
    
    private BarcodeScanner scanner;
    protected static String TAG = "SpectralinkCordovaPlugin";

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView)
    {
        super.initialize(cordova, webView);
        scanner = new BarcodeScanner(cordova.getActivity());
    }
    @Override
    public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {
        if ("register".equals(action)) {
            scanner.setScanCallback(new ScanCallback<String>() {
                @Override
                public void execute(String scan) {
                    Log.i(TAG, "Scan result [" + scan + "].");
                    
                    PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, scan);
                    pluginResult.setKeepCallback(true);
                    callbackContext.sendPluginResult(pluginResult);
                }
            });
            scanner.register();
        }
        else if ("unregister".equals(action)) {
            scanner.setScanCallback(null);
            scanner.unregister();
        }

        return true;
    }
    /**
    * Always close the current intent reader
    */
    @Override
    public void onPause(boolean multitasking)
    {
        super.onPause(multitasking);
        scanner.unregister();
    }


    /**
    * Always resume the current activity
    */
    @Override
    public void onResume(boolean multitasking)
    {
        super.onResume(multitasking);
        scanner.register();
    }
}
