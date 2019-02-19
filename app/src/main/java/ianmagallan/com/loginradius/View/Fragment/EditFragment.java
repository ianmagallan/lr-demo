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
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import ianmagallan.com.loginradius.Constants.Constants;
import ianmagallan.com.loginradius.Contract.AuthenticationContract;
import ianmagallan.com.loginradius.R;

public class EditFragment extends Fragment {

    @BindView(R.id.profile_edit_et_username)
    EditText et_username;

    @BindView(R.id.profile_edit_et_name)
    EditText et_name;

    @BindView(R.id.profile_edit_et_surname)
    EditText et_surname;

    @BindView(R.id.profile_edit_et_phone)
    EditText et_phone;

    private Constants.EDIT_MODE mCurrentEditMode;

    private AuthenticationContract.ProfileEditCallback mCallback;

    public EditFragment() {

    }

    public static EditFragment newInstance(){
        return new EditFragment();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile_edit, container, false);
        if(v != null) {
            ButterKnife.bind(this, v);
        }
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().show();
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.setTitle(getResources().getString(R.string.edit_title));

        if(getArguments().getString(Constants.BUNDLE_USERNAME_TAG) != null){
            et_username.setText(getArguments().getString(Constants.BUNDLE_USERNAME_TAG));
        }
        if(getArguments().getString(Constants.BUNDLE_NAME_TAG) != null) {
            et_name.setText(getArguments().getString(Constants.BUNDLE_NAME_TAG));
        }
        if(getArguments().getString(Constants.BUNDLE_SURNAME_TAG) != null) {
            et_surname.setText(getArguments().getString(Constants.BUNDLE_SURNAME_TAG));
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.findItem(R.id.menu_save).setVisible(true);
        menu.findItem(R.id.menu_logout).setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_save:
                String userName = null;
                String firstName = null;
                String lastName = null;
                String userPhone = null;
                switch (mCurrentEditMode){
                    case USERNAME:
                        userName = et_username.getText().toString();
                        break;
                    case NAME:
                        firstName = et_name.getText().toString();
                        lastName = et_surname.getText().toString();
                        break;
                    case PHONE:
                        userPhone = et_phone.getText().toString();
                        break;
                }
                mCallback.onUpdateInfo(userName, firstName, lastName, userPhone);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof AuthenticationContract.ProfileEditCallback) {
            this.mCallback = (AuthenticationContract.ProfileEditCallback) context;
        }
    }

    @Override
    public void onDetach() {
        mCallback = null; //Avoid leaking.
        super.onDetach();
    }


    public void setupView(Constants.EDIT_MODE mode){
        mCurrentEditMode = mode;
        et_username.setEnabled(mode == Constants.EDIT_MODE.USERNAME);
        et_username.setVisibility(mode == Constants.EDIT_MODE.USERNAME ? View.VISIBLE : View.GONE);
        et_name.setEnabled(mode == Constants.EDIT_MODE.NAME);
        et_name.setVisibility(mode == Constants.EDIT_MODE.NAME ? View.VISIBLE : View.GONE);
        et_surname.setEnabled(mode == Constants.EDIT_MODE.NAME);
        et_surname.setVisibility(mode == Constants.EDIT_MODE.NAME ? View.VISIBLE : View.GONE);
        et_phone.setEnabled(mode == Constants.EDIT_MODE.PHONE);
        et_phone.setVisibility(mode == Constants.EDIT_MODE.PHONE ? View.VISIBLE : View.GONE);
    }

}
