package hezra.wingsnsides.AccountDetails;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import hezra.wingsnsides.Main.MainFragment;
import hezra.wingsnsides.Main.MainNavigationActivity;
import hezra.wingsnsides.R;
import hezra.wingsnsides.Utils.Constants;
import hezra.wingsnsides.Utils.Preferences;

public class AccountFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private TextView fname, lname, email, mobile, updateAccount;
    private Button logOutLink, updateMobile;

    private OnFragmentInteractionListener mListener;

    public AccountFragment() {
        // Required empty public constructor
    }

    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_account, container, false);
        fname = (TextView) parentView.findViewById(R.id.fname_tv);
        lname = (TextView) parentView.findViewById(R.id.lname_tv);
        email = (TextView) parentView.findViewById(R.id.email_tv);
        updateAccount = (TextView) parentView.findViewById(R.id.updateAccount);
        mobile = (TextView) parentView.findViewById(R.id.mobileNumber);
        updateMobile = (Button) parentView.findViewById(R.id.updateMobile);

        fname.setText(Preferences.getStringPreference(getActivity(), Constants.FNAME));
        lname.setText(Preferences.getStringPreference(getActivity(), Constants.LNAME));
        email.setText(Preferences.getStringPreference(getActivity(), Constants.EMAIL));
        mobile.setText(Preferences.getStringPreference(getActivity(), Constants.MOBILE));

        updateAccount.setOnClickListener(this);
        updateMobile.setOnClickListener(this);

        logOutLink = (Button) parentView.findViewById(R.id.logOutLink);
        logOutLink.setOnClickListener(this);


        return parentView;
    }

    public void onButtonPressed() {
        /*if (mListener != null) {
            mListener.onFragmentInteraction();
        }*/
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.logOutLink:
                Preferences.setBooleanPreference(getActivity(), Constants.ISLOGGEDIN, false);
                Preferences.setStringPreference(getActivity(), Constants.FNAME, "");
                Preferences.setStringPreference(getActivity(), Constants.LNAME, "");
                Preferences.setStringPreference(getActivity(), Constants.MOBILE, "");

                replaceFragment(MainNavigationActivity.fragment = new MainFragment());

                if (mListener != null) {
                    mListener.onFragmentInteraction();
                }
                break;

            case R.id.updateAccount:
                replaceFragment(MainNavigationActivity.fragment = new AccountFragmentUpdatePassword());

                if (mListener != null) {
                    mListener.onFragmentInteraction();
                }
                break;

            case R.id.updateMobile:
                replaceFragment(MainNavigationActivity.fragment = new AccountFragmentUpdateMobile());

                if (mListener != null) {
                    mListener.onFragmentInteraction();
                }
                break;
        }
    }

    protected void setFragment(Fragment fragment) {
        try {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, fragment).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void removeFragment(Fragment fragment) {
        try {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            //fragmentTransaction.replace(R.id.content_frame, fragment).commit();
            fragmentTransaction.remove(fragment);
            fragmentTransaction.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected void replaceFragment(Fragment fragment) {
        try {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, fragment).commit();
            fragmentTransaction.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction();
    }
}
