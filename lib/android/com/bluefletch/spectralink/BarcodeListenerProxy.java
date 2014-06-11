package com.bluefletch.spectralink;

import java.lang.reflect.*;

public class BarcodeListenerProxy implements java.lang.reflect.InvocationHandler {

    public static Object createInstance(Class clazz, BarcodeScanner invoker) {
        BarcodeListenerProxy handler = new BarcodeListenerProxy(invoker);

        return Proxy.newProxyInstance(null, new Class[]{clazz}, handler);
    }

    private BarcodeScanner mInvoker;
    public BarcodeListenerProxy(BarcodeScanner invoker){
        mInvoker = invoker;
    }

    public Object invoke(Object proxy, Method m, Object[] args) throws Throwable {
        try {
            if (m.getName().equals("getActivity")){
                return mInvoker.context;
            }
            else if (m.getName().equals("onBarcodeAvailable")) {
                mInvoker.onBarcodeAvailable((String)args[0]);
            } else {
                throw new RuntimeException("Method not implemented for interface: " + m.getName());

            }
        } catch (Exception e) {
            throw new RuntimeException("unexpected invocation exception: " + e.getMessage());
        }
        return null;
    }
}