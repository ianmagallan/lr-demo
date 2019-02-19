package ianmagallan.com.loginradius.View.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.shaishavgandhi.loginbuttons.FacebookButton;
import com.shaishavgandhi.loginbuttons.GoogleButton;
import com.shaishavgandhi.loginbuttons.TwitterButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import ianmagallan.com.loginradius.Contract.AuthenticationContract;
import ianmagallan.com.loginradius.R;

public class LoginFragment extends Fragment {

    @BindView(R.id.login_et_email)
    EditText et_email;

    @BindView(R.id.login_et_password)
    TextInputEditText et_password;

    @BindView(R.id.login_bt_signin)
    Button bt_login;

    @BindView(R.id.login_tv_forgot)
    TextView tv_forgot;

    @BindView(R.id.login_facebook)
    FacebookButton bt_facebook;

    @BindView(R.id.login_google)
    GoogleButton bt_google;

    @BindView(R.id.login_twitter)
    TwitterButton bt_twitter;

    @BindView(R.id.login_tv_register)
    TextView tv_register;

    private AuthenticationContract.LoginCallback mCallback;

    public LoginFragment() {

    }

    public static LoginFragment newInstance(){
        return new LoginFragment();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        if(v != null) {
            ButterKnife.bind(this, v);
        }
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().hide();

        bt_login.setOnClickListener(view -> mCallback.onLogin(et_email.getText().toString(), et_password.getText().toString()));
        bt_facebook.setOnClickListener(view -> mCallback.facebookLogin());
        bt_google.setOnClickListener(view -> mCallback.googleLogin());
        tv_forgot.setOnClickListener(view -> mCallback.forgotPassword());
        tv_register.setOnClickListener(view -> mCallback.goToRegister());
        bt_twitter.setOnClickListener(view -> mCallback.twitterLogin());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof AuthenticationContract.LoginCallback) {
            this.mCallback = (AuthenticationContract.LoginCallback) context;
        }
    }

    @Override
    public void onDetach() {
        mCallback = null; //Avoid leaking.
        super.onDetach();
    }
}
