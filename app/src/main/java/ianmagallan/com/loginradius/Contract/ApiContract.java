package ianmagallan.com.loginradius.Contract;


import com.google.gson.JsonObject;
import com.loginradius.androidsdk.resource.QueryParams;
import com.loginradius.androidsdk.response.UpdateResponse;
import com.loginradius.androidsdk.response.login.LoginData;
import com.loginradius.androidsdk.response.password.ForgotPasswordResponse;
import com.loginradius.androidsdk.response.register.RegisterResponse;
import com.loginradius.androidsdk.response.register.RegistrationData;
import com.loginradius.androidsdk.response.userprofile.LoginRadiusUltimateUserProfile;

public interface ApiContract {

    interface Model {
        void login(QueryParams mParams, LoginCallback mLoginCallback);
        void recoverEmail(QueryParams mParams, RecoverCallback mRecoverCallback);
        void register(QueryParams mParams, RegistrationData mData, RegisterCallback mRegisterCallback);
        void obtainUser(QueryParams mParams, ProfileCallback mProfileCallback);
        void updateUsername(QueryParams mParams, EditProfileCallback mEditProfileCallback);
        void updateProfile(QueryParams mParams, JsonObject change, EditProfileCallback mEditProfileCallback);
    }

    interface LoginCallback extends BasicErrors{
        void onLoginSuccess(LoginData loginData);
    }

    interface RegisterCallback extends BasicErrors {
        void onRegisterSuccess(RegisterResponse response);
    }

    interface RecoverCallback extends BasicErrors {
        void onRecoverSuccess(ForgotPasswordResponse message);
    }

    interface ProfileCallback extends BasicErrors {
        void onObtainedProfileSuccess(LoginRadiusUltimateUserProfile data);
    }

    interface EditProfileCallback extends BasicErrors {
        void onUpdateUsernameSuccess(UpdateResponse data);
        void onUpdateProfileSuccess(RegisterResponse data);
    }

}
