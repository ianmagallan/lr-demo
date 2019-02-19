package ianmagallan.com.loginradius.Contract;

import android.os.Bundle;

import ianmagallan.com.loginradius.Constants.Constants;

public interface AuthenticationContract {
    interface View {
        void onLoginSuccess(String accessToken);
        void onRecoverSuccess();
        void onRegisterSuccess();
        void onObtainedProfile(String name, String surname, String email, String username, String location);
        void onUpdateSuccess();
        void onFailure(String message, int code);
    }

    interface Presenter {
        void setView(AuthenticationContract.View mView);
        void login(String mEmail, String mPassword);
        void register(String mEmail, String mPassword, String mName, String mLastName);
        void recoverPassword(String email);
        void getProfile(String accessToken);
        void updateUsername(String mToken, String mUsername);
        void updatePersonalInfo(String mToken, String mName, String mLastName);
    }


    //Callbacks for Fragments.
    interface ForgotCallback {
        void onRecoverClicked(String email);
    }

    interface LoginCallback {
        void onLogin(String mEmail, String mPassword);
        void facebookLogin();
        void googleLogin();
        void twitterLogin();
        void goToRegister();
        void forgotPassword();
    }

    interface RegisterCallback {
        void onRegisterUser(String mEmail, String mPassword, String mName, String mLastName);
    }

    interface ProfileCallback {
        void goToEditScreen(Constants.EDIT_MODE mMode, Bundle mArgs);
        void onLogout();
    }

    interface ProfileEditCallback {
        void onUpdateInfo(String mUserName, String mName, String mLastName, String mPhone);
    }
}
