package com.bluefletch.spectralink;

import android.content.Context;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BarcodeScanner {
    
    protected static Object stateLock = new Object();
    protected static boolean hasInitialized = false;

    protected static String TAG= BarcodeScanner.class.getSimpleName();

    protected Context context;
    protected Object barcodeManager;
    protected Object barcodeListener;

    protected Method registerMethod;
    protected Method unregisterMethod;
    
    protected ScanCallback<String> scanCallback;
    public void setScanCallback(ScanCallback<String> callback) {
        scanCallback = callback;
    }

    public BarcodeScanner(Context context) {
        try {
            this.context = context;

            Field serviceField = Context.class.getDeclaredField("BARCODE_SERVICE");
            barcodeManager = context.getSystemService((String)serviceField.get(null));
            for(Method m : barcodeManager.getClass().getMethods()) {
                if (m.getName().equals("registerBarcodeListener")) {
                    registerMethod = m;
                } else if (m.getName().equals("unregisterBarcodeListener")){
                    unregisterMethod = m;
                }
            }

            Class listenInterface = Class.forName("android.hardware.BarcodeManager$BarcodeListener");

            barcodeListener =  listenInterface.cast(BarcodeListenerProxy.createInstance(listenInterface, this));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

    }

    /**
     * Register this scanner
     */
    public void register() {
        Log.i(TAG, "Open called");
        if (hasInitialized) {
            return;
        }
        synchronized (stateLock) {
            if (hasInitialized) {
                return;
            }
            hasInitialized = true;
            try {
                registerMethod.invoke(barcodeManager, barcodeListener);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Stop listening for scans
     */
    public void unregister() {
        if (!hasInitialized) {
            return;
        }
        synchronized (stateLock) {
            if (!hasInitialized) {
                return;
            }
            hasInitialized = false;
            Log.i(TAG, "Running close plugin intent");
            try {
                unregisterMethod.invoke(barcodeManager, barcodeListener);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Called by the barcode listener proxy
     * @param value
     */
    public void onBarcodeAvailable(String value){ 
        Log.d(TAG, "Barcode scan received: " + value);
        if (scanCallback != null) {
            scanCallback.execute(value);
        }
    }

}
