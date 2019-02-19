package ianmagallan.com.loginradius.Presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.loginradius.androidsdk.api.AuthenticationAPI;
import com.loginradius.androidsdk.resource.QueryParams;
import com.loginradius.androidsdk.response.UpdateResponse;
import com.loginradius.androidsdk.response.login.LoginData;
import com.loginradius.androidsdk.response.password.ForgotPasswordResponse;
import com.loginradius.androidsdk.response.register.Email;
import com.loginradius.androidsdk.response.register.RegisterResponse;
import com.loginradius.androidsdk.response.register.RegistrationData;
import com.loginradius.androidsdk.response.userprofile.LoginRadiusUltimateUserProfile;
import java.util.ArrayList;
import java.util.Arrays;

import ianmagallan.com.loginradius.Contract.ApiContract;
import ianmagallan.com.loginradius.Contract.AuthenticationContract;
import ianmagallan.com.loginradius.Model.ApiModel;
import ianmagallan.com.loginradius.Model.ServerError;

public class AuthenticationPresenter implements
        AuthenticationContract.Presenter,
        ApiContract.LoginCallback,
        ApiContract.RecoverCallback,
        ApiContract.RegisterCallback,
        ApiContract.ProfileCallback,
        ApiContract.EditProfileCallback {

    private AuthenticationContract.View mView;
    private ApiModel mApiModel;

    public AuthenticationPresenter(AuthenticationAPI mApi, Context mContext){
        this.mApiModel = new ApiModel(mApi, mContext);
    }


    /**
     *
     * PRESENTER METHODS
     *
     */

    @Override
    public void setView(AuthenticationContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void login(String mEmail, String mPassword) {
        QueryParams params = new QueryParams();
        params.setEmail(mEmail);
        params.setPassword(mPassword);
        //params.setEmailTemplate("<template>");
        mApiModel.login(params, this);
    }

    @Override
    public void recoverPassword(String email) {
        QueryParams params = new QueryParams();
        params.setEmail(email);
        mApiModel.recoverEmail(params, this);
    }

    @Override
    public void register(String mEmail, String mPassword, String mName, String mLastName) {
        QueryParams params = new QueryParams();
        //params.setPreventEmailVerification(true);
        final RegistrationData data = new RegistrationData();
        data.setFirstName(mName);
        data.setLastName(mLastName);
        data.setPassword(mPassword);

        Email emailObj = new Email();
        emailObj.setType("Primary");
        emailObj.setValue(mEmail);
        data.setEmail(new ArrayList<Email>(Arrays.asList(emailObj)));

        mApiModel.register(params, data, this);
    }

    @Override
    public void getProfile(String accessToken) {
        QueryParams params = new QueryParams();
        params.setAccess_token(accessToken);
        mApiModel.obtainUser(params, this);
    }

    @Override
    public void updateUsername(String mToken, String mUsername) {
        QueryParams params = new QueryParams();
        params.setAccess_token(mToken);
        params.setUsername(mUsername);
        mApiModel.updateUsername(params, this);    }

    @Override
    public void updatePersonalInfo(String mToken, String mName, String mLastName) {
        QueryParams params = new QueryParams();
        params.setAccess_token(mToken);
        JsonObject change = new JsonObject();
        change.addProperty("FirstName", mName);
        change.addProperty("LastName", mLastName);
        mApiModel.updateProfile(params, change, this);
    }


    /**
     *
     * Callback methods
     *
     */

    @Override
    public void onLoginSuccess(LoginData loginData) {
        mView.onLoginSuccess(loginData.getAccessToken());
    }

    @Override
    public void onRecoverSuccess(ForgotPasswordResponse message) {
        if(message.getIsPosted()){
            mView.onRecoverSuccess();
        }
    }

    @Override
    public void onRegisterSuccess(RegisterResponse response) {
        if(response.getIsPosted()){
            mView.onRegisterSuccess();
        }
    }


    @Override
    public void onObtainedProfileSuccess(LoginRadiusUltimateUserProfile data) {
        String email = data.Email != null ? data.Email.get(0).Value : "";
        String nickName = data.getUserName() != null ? data.getUserName().toString() : "";
        String lastLocation = data.getLastLoginLocation() != null ? data.getLastLoginLocation().toString() : "";
        mView.onObtainedProfile(data.FirstName, data.LastName, email, nickName, lastLocation);
    }


    @Override
    public void onUpdateUsernameSuccess(UpdateResponse data) {
        mView.onUpdateSuccess();
    }

    @Override
    public void onUpdateProfileSuccess(RegisterResponse data) {
        mView.onUpdateSuccess();
    }

    @Override
    public void onFailure(Throwable error, String errorCode) {
        try{
            Gson gson = new Gson();
            ServerError serverError = gson.fromJson(error.getMessage(), ServerError.class);
            mView.onFailure(serverError.mMessage, serverError.mErrorCode);
            return;
        } catch(Exception ex){
            System.err.print("Something went wrong when parsing response" + ex.getMessage());
        }
        mView.onFailure(error.getMessage(), 0);
    }
}
