package ianmagallan.com.loginradius.View.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import ianmagallan.com.loginradius.Constants.Constants;
import ianmagallan.com.loginradius.Contract.AuthenticationContract;
import ianmagallan.com.loginradius.R;

public class RegisterFragment extends Fragment {

    @BindView(R.id.register_et_email)
    EditText et_email;

    @BindView(R.id.register_et_password)
    EditText et_password;

    @BindView(R.id.register_et_password_confirm)
    EditText et_password_confirm;

    @BindView(R.id.register_et_name)
    EditText et_name;

    @BindView(R.id.register_et_surname)
    EditText et_surname;

    @BindView(R.id.register_bt_signup)
    Button bt_signup;

    private AuthenticationContract.RegisterCallback mCallback;

    public RegisterFragment() {

    }

    public static RegisterFragment newInstance(){
        return new RegisterFragment();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_register, container, false);
        if(v != null) {
            ButterKnife.bind(this, v);
        }
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().show();
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.setTitle(getResources().getString(R.string.register_register));

        bt_signup.setOnClickListener(view -> {
            final boolean emailValid = Patterns.EMAIL_ADDRESS.matcher(et_email.getText().toString()).matches();
            final boolean passwordMatch = et_password.getText().toString().equals(et_password_confirm.getText().toString());
            final boolean lengthRequirements = et_password.getText().toString().length() >= Constants.PASSWORD_MIN_LENGTH;

            if(!emailValid){
                et_email.setError(getResources().getString(R.string.warning_email_valid));
            } else if(!passwordMatch) {
                et_password.setError(getResources().getString(R.string.warning_equal_passwords));
            }else if(passwordMatch && !lengthRequirements) {
                et_password.setError(getResources().getString(R.string.warning_password_min));
            } else {
                mCallback.onRegisterUser(et_email.getText().toString(), et_password.getText().toString(), et_name.getText().toString(), et_surname.getText().toString());
            }

        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof AuthenticationContract.RegisterCallback) {
            this.mCallback = (AuthenticationContract.RegisterCallback) context;
        }
    }

    @Override
    public void onDetach() {
        mCallback = null; //Avoid leaking.
        super.onDetach();
    }
}
