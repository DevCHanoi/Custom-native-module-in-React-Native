package com.devcircle2;

import android.support.annotation.Nullable;
import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.modules.core.DeviceEventManagerModule;

/**
 * @author Anh Tuan Nguyen
 * @email tuan@agiletech.vn
 * @website agiletech.vn
 * @version 1.0.0
 * @since 2/25/2018
 */
public class MyCustomModule extends ReactContextBaseJavaModule {
    public MyCustomModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "MyCustomModule";
    }

    /**
     * hello method, return simple string back to js world using callback
     *
     * @param name String
     * @param callback Callback
     */
    @ReactMethod
    public void hello(String name, Callback callback) {
        String helloString = "Hi " + name + ", Welcome to native world";
        callback.invoke(helloString);
    }

    /**
     * personInfo method, return complex data back to js world using callback
     *
     * @param firstName String
     * @param lastName String
     * @param age Integer
     * @param salary Double
     * @param gender Boolean
     * @param options WritableArray
     * @param callback Callback
     */
    @ReactMethod
    public void personInfo(
            String firstName, String lastName, Integer age, Double salary,
            Boolean gender, WritableArray options, Callback callback) {

        WritableMap data= Arguments.createMap();
        data.putString("firstName", firstName);
        data.putString("lastName", lastName);
        data.putInt("age", age);
        data.putDouble("salary", salary);
        data.putArray("options", options);
        data.putBoolean("gender", gender);
        data.putString("genderStr", gender ? "male" : "female");
        callback.invoke(data);
    }

    /**
     * async await method
     *
     * @param signal Boolean
     * @param promise Promise
     */
    @ReactMethod
    public void pingPong(Boolean signal, Promise promise) {
        if(signal) {
            WritableMap data= Arguments.createMap();
            data.putString("signal", "Signal is true");
            promise.resolve(data);
        } else {
            promise.reject(new Exception("Signal is false"));
        }
    }

    /**
     * After register event
     * We trigger this function to do send event back to JavaScript
     * @param count
     * @param eventName
     * @param params
     */
    public void doSendEvent(Integer count, String eventName,@Nullable WritableMap params) {
        do{
            this.sendEvent(this.getReactApplicationContext(), eventName, params);
            count--;
        }while (count > 0);
    }

    /**
     * Send Events to Javascript
     * @param reactContext
     * @param eventName
     * @param params
     */
    private void sendEvent(ReactContext reactContext, String eventName, @Nullable WritableMap params) {
        if (reactContext.hasActiveCatalystInstance()) {
            Log.i("sendEvent", params.toString());
            reactContext
                    .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                    .emit(eventName, params);
        } else {
            Log.i("sendEvent", "Waiting for CatalystInstance...");
        }

    }
}