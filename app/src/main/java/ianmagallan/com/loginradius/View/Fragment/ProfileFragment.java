package ianmagallan.com.loginradius.View.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ianmagallan.com.loginradius.Constants.Constants;
import ianmagallan.com.loginradius.Contract.AuthenticationContract;
import ianmagallan.com.loginradius.R;

public class ProfileFragment extends Fragment implements View.OnTouchListener {

    @BindView(R.id.profile_tv_name)
    TextView tv_fullName;

    @BindView(R.id.profile_tv_email)
    TextView tv_email;

    @BindView(R.id.profile_et_username)
    EditText et_username;

    @BindView(R.id.profile_et_name)
    EditText et_name;

    @BindView(R.id.profile_et_surname)
    EditText et_surname;

    @BindView(R.id.profile_tv_last_login)
    TextView tv_last_login;

    private View mSavedInstance = null;

    private AuthenticationContract.ProfileCallback mCallback;

    public ProfileFragment() {

    }

    public static ProfileFragment newInstance(){
        return new ProfileFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof AuthenticationContract.ProfileCallback) {
            this.mCallback = (AuthenticationContract.ProfileCallback) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(mSavedInstance == null){
            mSavedInstance = inflater.inflate(R.layout.fragment_profile, container, false);
            if(mSavedInstance != null) {
                ButterKnife.bind(this, mSavedInstance);
            }
        }
        return mSavedInstance;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().show();
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        activity.setTitle(getResources().getString(R.string.profile_title));
        et_username.setOnTouchListener(this);
        et_name.setOnTouchListener(this);
        et_surname.setOnTouchListener(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.findItem(R.id.menu_save).setVisible(false);
        menu.findItem(R.id.menu_logout).setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_logout:
                mCallback.onLogout();
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDetach() {
        mCallback = null; //Avoid leaking.
        super.onDetach();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
            Bundle bundle = new Bundle();
            final int viewId = view.getId();
            if(viewId == et_username.getId()){
                bundle.putString(Constants.BUNDLE_USERNAME_TAG, et_username.getText().toString());
                mCallback.goToEditScreen(Constants.EDIT_MODE.USERNAME, bundle);
            } else if(viewId == et_name.getId() || viewId == et_surname.getId()){
                bundle.putString(Constants.BUNDLE_NAME_TAG, et_name.getText().toString());
                bundle.putString(Constants.BUNDLE_SURNAME_TAG, et_surname.getText().toString());
                mCallback.goToEditScreen(Constants.EDIT_MODE.NAME, bundle);
            }
        }
        return false;
    }

    public void fillProfileInfo(String mName, String mSurname, String mEmail, String mUserName, String mLocation){
        final String fullName = mName + " " + mSurname;
        tv_fullName.setText(fullName);
        tv_email.setText(mEmail);
        tv_last_login.setText(!mLocation.isEmpty() ? mLocation : getResources().getString(R.string.profile_unknown));
        et_username.setText(mUserName);
        et_name.setText(mName);
        et_surname.setText(mSurname);
    }
}
