package ianmagallan.com.loginradius.Model;

import android.content.Context;

import com.google.gson.JsonObject;
import com.loginradius.androidsdk.api.AuthenticationAPI;
import com.loginradius.androidsdk.handler.AsyncHandler;
import com.loginradius.androidsdk.resource.QueryParams;
import com.loginradius.androidsdk.response.UpdateResponse;
import com.loginradius.androidsdk.response.login.LoginData;
import com.loginradius.androidsdk.response.password.ForgotPasswordResponse;
import com.loginradius.androidsdk.response.register.RegisterResponse;
import com.loginradius.androidsdk.response.register.RegistrationData;
import com.loginradius.androidsdk.response.userprofile.LoginRadiusUltimateUserProfile;

import ianmagallan.com.loginradius.Contract.ApiContract;

public class ApiModel implements ApiContract.Model {
    private AuthenticationAPI mAuthenticationApi;
    private Context mContext;

    public ApiModel(AuthenticationAPI mAuthenticationApi, Context mContext){
        this.mAuthenticationApi = mAuthenticationApi;
        this.mContext = mContext;
    }

    @Override
    public void login(QueryParams mParams, ApiContract.LoginCallback mLoginCallback) {
        mAuthenticationApi.login(mContext, mParams, new AsyncHandler<LoginData>() {
            @Override
            public void onSuccess(LoginData data) {
                mLoginCallback.onLoginSuccess(data);
            }

            @Override
            public void onFailure(Throwable error, String errorcode) {
                mLoginCallback.onFailure(error, errorcode);
            }
        });
    }

    @Override
    public void recoverEmail(QueryParams mParams, ApiContract.RecoverCallback mRecoverCallback) {
        mAuthenticationApi.forgotPasswordByEmail(mParams, new AsyncHandler<ForgotPasswordResponse>() {
            @Override
            public void onSuccess(ForgotPasswordResponse data) {
                mRecoverCallback.onRecoverSuccess(data);
            }

            @Override
            public void onFailure(Throwable error, String errorcode) {
                mRecoverCallback.onFailure(error, errorcode);
            }
        });
    }

    @Override
    public void register(QueryParams mParams, RegistrationData mData, ApiContract.RegisterCallback mRegisterCallback) {
        mAuthenticationApi.register(mParams, mData, new AsyncHandler<RegisterResponse>() {
            @Override
            public void onSuccess(RegisterResponse data) {
                mRegisterCallback.onRegisterSuccess(data);
            }

            @Override
            public void onFailure(Throwable error, String errorcode) {
                mRegisterCallback.onFailure(error, errorcode);
            }
        });
    }

    @Override
    public void obtainUser(QueryParams mParams, ApiContract.ProfileCallback mProfileCallback) {
        mAuthenticationApi.readAllUserProfile(mParams, new AsyncHandler<LoginRadiusUltimateUserProfile>() {
            @Override
            public void onSuccess(LoginRadiusUltimateUserProfile data) {
                mProfileCallback.onObtainedProfileSuccess(data);
            }

            @Override
            public void onFailure(Throwable error, String errorcode) {
                mProfileCallback.onFailure(error, errorcode);
            }
        });
    }

    @Override
    public void updateUsername(QueryParams mParams, ApiContract.EditProfileCallback mEditProfileCallback) {
        mAuthenticationApi.updateUsername(mParams, new AsyncHandler<UpdateResponse>() {
            @Override
            public void onSuccess(UpdateResponse data) {
                mEditProfileCallback.onUpdateUsernameSuccess(data);
            }

            @Override
            public void onFailure(Throwable error, String errorcode) {
                mEditProfileCallback.onFailure(error, errorcode);
            }
        });
    }

    @Override
    public void updateProfile(QueryParams mParams, JsonObject change, ApiContract.EditProfileCallback mEditProfileCallback) {
        mAuthenticationApi.updateProfile(mParams, change, new AsyncHandler <RegisterResponse> () {
            @Override
            public void onSuccess(RegisterResponse registerResponse) {
                mEditProfileCallback.onUpdateProfileSuccess(registerResponse);
            }
            @Override
            public void onFailure(Throwable error, String errorcode) {
                mEditProfileCallback.onFailure(error, errorcode);
            }
        });
    }


}
