package ianmagallan.com.loginradius.View;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.FadingCircle;
import com.loginradius.androidsdk.helper.LoginRadiusSDK;
import com.loginradius.androidsdk.resource.SocialProviderConstant;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import ianmagallan.com.loginradius.App;
import ianmagallan.com.loginradius.Constants.Constants;
import ianmagallan.com.loginradius.Contract.AuthenticationContract;
import ianmagallan.com.loginradius.Presenter.AuthenticationPresenter;
import ianmagallan.com.loginradius.R;
import ianmagallan.com.loginradius.View.Fragment.EditFragment;
import ianmagallan.com.loginradius.View.Fragment.ForgotFragment;
import ianmagallan.com.loginradius.View.Fragment.LoginFragment;
import ianmagallan.com.loginradius.View.Fragment.ProfileFragment;
import ianmagallan.com.loginradius.View.Fragment.RegisterFragment;

public class AuthenticationActivity extends AppCompatActivity implements
        AuthenticationContract.View,
        AuthenticationContract.ForgotCallback,
        AuthenticationContract.LoginCallback,
        AuthenticationContract.RegisterCallback,
        AuthenticationContract.ProfileCallback,
        AuthenticationContract.ProfileEditCallback {


    private enum FRAGMENT_SCREEN {
        LOGIN, REGISTER, FORGOT, PROFILE, PROFILE_EDIT
    }

    @Inject
    AuthenticationPresenter mPresenter;

    @Inject
    SharedPreferences mPrefs;

    @Inject
    SharedPreferences.Editor mPrefsEditor;

    @BindView(R.id.spin_kit)
    SpinKitView spin_kit;

    @BindView(R.id.main_fragment_container)
    RelativeLayout main_container;

    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Dependency injection initialization.
        ((App) getApplication()).getAppComponent().inject(this);
        mPresenter.setView(this);

        //Butterknife initialization.
        ButterKnife.bind(this);

        //Init fragment manager
        mFragmentManager = getSupportFragmentManager();

        //Check if user has token.
        if(mPrefs.getString(Constants.TOKEN_TAG, "").length() > 0){
            loadFragment(FRAGMENT_SCREEN.PROFILE, null);
            getProfile();
        } else {
            loadFragment(FRAGMENT_SCREEN.LOGIN, null);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==2) {
            if (data != null){
                Log.i("Access Token",data.getStringExtra(Constants.LR_ACCESS_TOKEN_TAG));
                Log.i("Provider",data.getStringExtra(Constants.LR_PROVIDER_TAG));
                mPrefsEditor.putString(Constants.TOKEN_TAG, data.getStringExtra(Constants.LR_ACCESS_TOKEN_TAG));
                mPrefsEditor.commit();
                mFragmentManager.popBackStack();
                loadFragment(FRAGMENT_SCREEN.PROFILE, null);
                getProfile();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        //If there is only one fragment in the stack, we want to close the app.
        if(mFragmentManager.getBackStackEntryCount() == 1) {
            finish();
        } else {
            super.onBackPressed();
        }
    }

    //Function called from the Fragment to let us know that they want to call login from the presenter.
    @Override
    public void onLogin(String mEmail, String mPassword) {
        setProgress(true);
        mPresenter.login(mEmail, mPassword);
    }

    //Function called by a Fragment to let us know that we should show the "Forgot Screen".
    @Override
    public void forgotPassword() {
        loadFragment(FRAGMENT_SCREEN.FORGOT, null);

    }

    //Social login
    @Override
    public void facebookLogin() {
        LoginRadiusSDK.NativeLogin nat = new LoginRadiusSDK.NativeLogin();
        nat.startFacebookNativeLogin(AuthenticationActivity.this, 2);
    }

    @Override
    public void googleLogin() {
        LoginRadiusSDK.NativeLogin nat = new LoginRadiusSDK.NativeLogin();
        nat.startGoogleNativeLogin(AuthenticationActivity.this, 2);
    }

    @Override
    public void twitterLogin() {
        LoginRadiusSDK.WebLogin webLogin = new LoginRadiusSDK.WebLogin();
        webLogin.setProvider(SocialProviderConstant.TWITTER);
        webLogin.startWebLogin(AuthenticationActivity.this,2);
    }

    //Show the register screen.
    @Override
    public void goToRegister() {
        loadFragment(FRAGMENT_SCREEN.REGISTER, null);
    }

    //Called when the recover button is pressed.
    @Override
    public void onRecoverClicked(String email) {
        mPresenter.recoverPassword(email);
    }

    //Callback function from the presenter when we are authenticated.
    @Override
    public void onLoginSuccess(String accessToken) {
        setProgress(false);
        Log.i("Main", accessToken);

        //Store accessToken.
        mPrefsEditor.putString(Constants.TOKEN_TAG, accessToken);
        mPrefsEditor.commit();

        //Pop the current fragment(login). We don't need it anymore.
        mFragmentManager.popBackStack();

        loadFragment(FRAGMENT_SCREEN.PROFILE, null);
        getProfile();
    }

    //Generic function when there is an error from the server.
    @Override
    public void onFailure(String message, int code) {
        setProgress(false);
        if(code == 906){ //Expired token, remove it.
            mPrefsEditor.remove(Constants.TOKEN_TAG);
            mPrefsEditor.commit();
            mFragmentManager.popBackStack();
            loadFragment(FRAGMENT_SCREEN.LOGIN, null);
            return;
        }
        MaterialDialog.Builder dialog = new MaterialDialog.Builder(this);
        dialog.title(getResources().getString(R.string.general_error))
                .content(message)
                .positiveText(getResources().getString(R.string.general_ok))
                .positiveColor(getResources().getColor(R.color.colorPrimary));
        dialog.show();
        Log.e("Main", message);
    }

    @Override
    public void onRecoverSuccess() {
        setProgress(false);
        Log.i("Main", "Success when recovering");
        MaterialDialog.Builder dialog = new MaterialDialog.Builder(this);
        dialog.title(getResources().getString(R.string.recover_title))
                .content(getResources().getString(R.string.recover_message))
                .positiveText(getResources().getString(R.string.general_ok))
                .onPositive((dialog1, which) ->
                    getSupportFragmentManager().popBackStack()
                )
                .positiveColor(getResources().getColor(R.color.colorPrimary));

        dialog.show();
    }

    @Override
    public void onRegisterSuccess() {
        setProgress(false);
        Log.i("Main", "Registered successfully");
        MaterialDialog.Builder dialog = new MaterialDialog.Builder(this);
        dialog.title(getResources().getString(R.string.register_title))
                .content(getResources().getString(R.string.register_message))
                .positiveText(getResources().getString(R.string.general_ok))
                .onPositive((dialog1, which) ->
                        getSupportFragmentManager().popBackStack()
                )
                .positiveColor(getResources().getColor(R.color.colorPrimary));

        dialog.show();
    }

    @Override
    public void onObtainedProfile(String name, String surname, String email, String username, String location) {
        setProgress(false);
        Fragment fragment = mFragmentManager.findFragmentByTag(Constants.PROFILE_TAG);
        if(fragment instanceof ProfileFragment) {
            ((ProfileFragment) fragment).fillProfileInfo(name, surname, email, username, location);
        }
    }


    @Override
    public void onRegisterUser(String mEmail, String mPassword, String mName, String mLastName) {
        setProgress(true);
        mPresenter.register(mEmail, mPassword, mName, mLastName);
    }

    @Override
    public void goToEditScreen(Constants.EDIT_MODE mMode, Bundle mBundle) {
        loadFragment(FRAGMENT_SCREEN.PROFILE_EDIT, mBundle);
        Fragment currentFragment = mFragmentManager.findFragmentByTag(Constants.EDIT_PROFILE_TAG);
        if(currentFragment instanceof EditFragment){
            ((EditFragment) currentFragment).setupView(mMode);
        }
    }

    @Override
    public void onLogout() {
        MaterialDialog.Builder dialog = new MaterialDialog.Builder(this);
        dialog.title(getResources().getString(R.string.logout_title))
                .content(getResources().getString(R.string.logout_message))
                .positiveText(getResources().getString(R.string.general_ok))
                .negativeText(getResources().getString(R.string.general_cancel))
                .onPositive((dialog1, which) -> {
                    mPrefsEditor.remove(Constants.TOKEN_TAG);
                    mPrefsEditor.commit();
                    mFragmentManager.popBackStack();
                    loadFragment(FRAGMENT_SCREEN.LOGIN, null);
                })
                .positiveColor(getResources().getColor(R.color.colorPrimary))
                .negativeColor(getResources().getColor(R.color.colorPrimary));

        dialog.show();
    }

    @Override
    public void onUpdateInfo(String mUserName, String mName, String mLastName, String mPhone) {
        setProgress(true);
        final String mToken = mPrefs.getString(Constants.TOKEN_TAG, "");
        if(mUserName != null) {
            mPresenter.updateUsername(mToken, mUserName);
        } else if(mName != null) {
            mPresenter.updatePersonalInfo(mToken, mName, mLastName);
        }
    }

    @Override
    public void onUpdateSuccess() {
        setProgress(false);
        MaterialDialog.Builder dialog = new MaterialDialog.Builder(this);
        dialog.title(getResources().getString(R.string.update_title))
                .content(getResources().getString(R.string.update_message_successful))
                .positiveText(getResources().getString(R.string.general_ok))
                .onPositive((dialog1, which) -> {
                    onBackPressed();
                    getProfile();
                })
                .positiveColor(getResources().getColor(R.color.colorPrimary));
        dialog.show();
    }


    public void getProfile(){
        setProgress(true);
        String mToken = mPrefs.getString(Constants.TOKEN_TAG, "");
        mPresenter.getProfile(mToken);
    }

    public void loadFragment(FRAGMENT_SCREEN screen, Bundle bundle){
        Fragment fragment = null;
        String tag = "";
        switch (screen){
            case LOGIN:
                fragment = LoginFragment.newInstance();
                tag = null;
                break;
            case REGISTER:
                fragment = RegisterFragment.newInstance();
                tag = Constants.REGISTER_TAG;
                break;
            case FORGOT:
                fragment = ForgotFragment.newInstance();
                tag = Constants.RECOVER_TAG;
                break;
            case PROFILE:
                fragment = ProfileFragment.newInstance();
                tag = Constants.PROFILE_TAG;
                break;
            case PROFILE_EDIT:
                fragment = EditFragment.newInstance();
                tag = Constants.EDIT_PROFILE_TAG;
                break;
        }
        fragment.setArguments(bundle);
        //forgotFragment.setEnterTransition(new Slide(Gravity.RIGHT));
        //forgotFragment.setExitTransition(new Slide(Gravity.LEFT));
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        ft.replace(R.id.main_fragment_container, fragment, tag);
        //ft.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left);
        ft.addToBackStack(null);
        ft.commit();
        getSupportFragmentManager().executePendingTransactions();
    }

    void setProgress(boolean show){
        main_container.setVisibility(!show ? View.VISIBLE : View.GONE);
        spin_kit.setVisibility(show ? View.VISIBLE : View.GONE);
        Sprite fadingCircle = new FadingCircle();
        spin_kit.setIndeterminateDrawable(fadingCircle);
    }
}
