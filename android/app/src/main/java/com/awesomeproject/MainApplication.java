package com.awesomeproject;

import android.app.Application;
import android.content.Context;

import com.awesomeproject.module.ModulePackage;
import com.facebook.react.ReactApplication;
import com.oblador.vectoricons.VectorIconsPackage;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.shell.MainReactPackage;
import com.facebook.soloader.SoLoader;

import java.util.Arrays;
import java.util.List;


public class MainApplication extends Application implements ReactApplication {

  private static Context context;

  private static ModulePackage curModlePackage;

  private final ReactNativeHost mReactNativeHost = new ReactNativeHost(this) {
    @Override
    public boolean getUseDeveloperSupport() {
      return BuildConfig.DEBUG;
    }

    @Override
    protected List<ReactPackage> getPackages() {
      ModulePackage modlePackage = new ModulePackage();
      curModlePackage = modlePackage;
      return Arrays.<ReactPackage>asList(
          new MainReactPackage(),
            new VectorIconsPackage(),
              modlePackage
      );
    }

    @Override
    protected String getJSMainModuleName() {
      return "index";
    }
  };

  @Override
  public ReactNativeHost getReactNativeHost() {
    return mReactNativeHost;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    MainApplication.context = getApplicationContext();
    SoLoader.init(this, /* native exopackage */ false);
  }

  public static Context getAppContext() {
    return MainApplication.context;
  }

  public static ModulePackage getModule(){
    return curModlePackage;
  }
}
