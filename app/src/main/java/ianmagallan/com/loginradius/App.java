package ianmagallan.com.loginradius;

import android.app.Application;
import android.content.Context;
import com.loginradius.androidsdk.helper.LoginRadiusSDK;
import ianmagallan.com.loginradius.DependencyInjection.AppComponent;
import ianmagallan.com.loginradius.DependencyInjection.AppModule;
import ianmagallan.com.loginradius.DependencyInjection.DaggerAppComponent;
import ianmagallan.com.loginradius.DependencyInjection.NetModule;

public class App extends Application {

    private AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .netModule(new NetModule())
                .build();

        LoginRadiusSDK.Initialize init = new LoginRadiusSDK.Initialize();
        init.setApiKey(getResources().getString(R.string.api_key));
        init.setSiteName(getResources().getString(R.string.site_name));
        init.setSott(getResources().getString(R.string.sott_key));

 }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    public AppComponent getAppComponent(){
        return mAppComponent;
    }
}