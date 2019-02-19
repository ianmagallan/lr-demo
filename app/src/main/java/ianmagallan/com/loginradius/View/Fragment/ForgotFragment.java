package ianmagallan.com.loginradius.View.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import ianmagallan.com.loginradius.Contract.AuthenticationContract;
import ianmagallan.com.loginradius.R;

public class ForgotFragment extends Fragment {

    @BindView(R.id.login_et_recover)
    EditText et_recover;

    @BindView(R.id.login_bt_recover)
    Button bt_recover;

    private AuthenticationContract.ForgotCallback mCallback;

    public ForgotFragment() {

    }

    public static ForgotFragment newInstance(){
        return new ForgotFragment();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_forgot, container, false);
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
        activity.setTitle(getResources().getString(R.string.recover_title));
        bt_recover.setOnClickListener(view -> mCallback.onRecoverClicked(et_recover.getText().toString()));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof AuthenticationContract.ForgotCallback) {
            this.mCallback = (AuthenticationContract.ForgotCallback) context;
        }
    }

    @Override
    public void onDetach() {
        mCallback = null; //Avoid leaking.
        super.onDetach();
    }

}
