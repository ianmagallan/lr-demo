package ianmagallan.com.loginradius.DependencyInjection;

import com.loginradius.androidsdk.api.AuthenticationAPI;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;

@Module
public class NetModule {

    public NetModule() {

    }

    @Provides
    @Singleton
    AuthenticationAPI provideAuthenticationApi() {
        return new AuthenticationAPI();
    }
}
