package ianmagallan.com.loginradius.DependencyInjection;

import javax.inject.Singleton;

import dagger.Component;
import ianmagallan.com.loginradius.View.AuthenticationActivity;

@Singleton
@Component(modules = {AppModule.class, NetModule.class})
public interface AppComponent {

    void inject(AuthenticationActivity authenticationActivity);
}
