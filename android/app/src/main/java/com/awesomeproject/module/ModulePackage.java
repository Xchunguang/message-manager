package com.awesomeproject.module;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;

import java.util.ArrayList;
import java.util.List;

public class ModulePackage implements ReactPackage{


    private MailModule mailModule;

    @Override
    public List<NativeModule> createNativeModules(ReactApplicationContext reactContext) {
        List<NativeModule> modules = new ArrayList<>();
        MailModule curMailModule = new MailModule(reactContext);
        modules.add(curMailModule);
        mailModule = curMailModule;
        return modules;
    }

    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
        return new ArrayList<>();
    }

    public MailModule getMailModule(){
        return mailModule;
    }
}