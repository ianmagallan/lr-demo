package ianmagallan.com.loginradius.DependencyInjection;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.loginradius.androidsdk.api.AuthenticationAPI;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ianmagallan.com.loginradius.Constants.Constants;
import ianmagallan.com.loginradius.Presenter.AuthenticationPresenter;

/**
 * Class that provides the application context and presenters.
 */

@Module
public class AppModule {
    private Application mApplication;

    public AppModule(Application mApplication){
        this.mApplication = mApplication;
    }

    @Provides
    @Singleton
    Application getApplication(){
        return this.mApplication;
    }

    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences(Application mApplication){
        return mApplication.getSharedPreferences(Constants.PREFS_TAG, Context.MODE_PRIVATE);
    }

    @Provides
    @Singleton
    SharedPreferences.Editor provideSharedPreferencesEditor(Application mApplication){
        return mApplication.getSharedPreferences(Constants.PREFS_TAG, Context.MODE_PRIVATE).edit();
    }


    @Provides
    @Singleton
    AuthenticationPresenter provideLoginPresenter(AuthenticationAPI mAuthenticationApi, Application mApplication){
        return new AuthenticationPresenter(mAuthenticationApi, mApplication);
    }

}