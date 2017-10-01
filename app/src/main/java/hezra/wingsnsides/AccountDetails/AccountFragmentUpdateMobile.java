package hezra.wingsnsides.AccountDetails;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import hezra.wingsnsides.Main.MainNavigationActivity;
import hezra.wingsnsides.R;
import hezra.wingsnsides.Utils.Constants;
import hezra.wingsnsides.Utils.Login;
import hezra.wingsnsides.Utils.Preferences;

public class AccountFragmentUpdateMobile extends Fragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private TextView fname, lname;
    private EditText o_password, mobile;
    private Button Submit;
    private boolean flag = false;
    private OnFragmentInteractionListener mListener;

    public AccountFragmentUpdateMobile() {
        // Required empty public constructor
    }

    public static AccountFragmentUpdateMobile newInstance(String param1, String param2) {
        AccountFragmentUpdateMobile fragment = new AccountFragmentUpdateMobile();
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
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_account_update_mobile, container, false);
        fname = (TextView) parentView.findViewById(R.id.fname_et);
        lname = (TextView) parentView.findViewById(R.id.lname_et);
        mobile = (EditText) parentView.findViewById(R.id.mobileNumber_et);
        o_password = (EditText) parentView.findViewById(R.id.old_password_et);

        fname.setText(Preferences.getStringPreference(getActivity(), Constants.FNAME));
        lname.setText(Preferences.getStringPreference(getActivity(), Constants.LNAME));
        mobile.setText(Preferences.getStringPreference(getActivity(), Constants.MOBILE));

        Submit = (Button) parentView.findViewById(R.id.Submit_Changes);
        Submit.setOnClickListener(this);


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
            case R.id.Submit_Changes:

                updateUser(Preferences.getStringPreference(getActivity(), Constants.EMAIL), o_password.getText().toString(), mobile.getText().toString());
                break;
        }
    }

    private void updateUser(final String email, String opassword, String mobile) {

        String urlSuffix = "?email=" + email + "&" + "password=" + opassword + "&" + "mobile=" + mobile;
        class RegisterUser extends AsyncTask<String, Void, String> {
            private ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getActivity(), "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String response) {
                super.onPostExecute(response);
                loading.dismiss();
                try {
                    if (!flag) {
                        Login log = new Login();

                        JSONObject LoginObject;
                        try {
                            LoginObject = new JSONObject(response);
                            Log.v("Logs", LoginObject.toString());
                            JSONObject login = LoginObject.getJSONObject("LOGIN");

                            log.setStatus(login.getString("status"));
                            log.setEmail(login.getString("EMAIL"));
                            log.setFname(login.getString("FNAME"));
                            log.setLname(login.getString("LNAME"));
                            log.setMobile(login.getString("MOBILE"));

                            if (log.getStatus().equalsIgnoreCase("UPDATED")) {
                                Toast.makeText(getActivity().getApplicationContext(), "Successfully updated password", Toast.LENGTH_LONG).show();
                                Preferences.setStringPreference(getActivity(), Constants.EMAIL, log.getEmail());
                                Preferences.setStringPreference(getActivity(), Constants.FNAME, log.getFname());
                                Preferences.setStringPreference(getActivity(), Constants.LNAME, log.getLname());
                                Preferences.setStringPreference(getActivity(), Constants.MOBILE, log.getMobile());
                                Preferences.setBooleanPreference(getActivity(), Constants.ISLOGGEDIN, true);

                                replaceFragment(MainNavigationActivity.fragment = new AccountFragment());

                                if (mListener != null) {
                                    mListener.onFragmentInteraction();
                                }
                            } else {
                                Toast.makeText(getActivity().getApplicationContext(), "Failure", Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity().getApplicationContext(), "Failure", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "Connection Error.. \n Please try again !!", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Toast.makeText(getActivity().getApplicationContext(), "Connection Error.. \n Please try again !!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected String doInBackground(String... params) {
                String urlSuffix = params[0];
                BufferedReader bufferedReader;
                try {
                    URL url = new URL(Constants.BASEURLUPDATEUSERMOBILE + urlSuffix);
                    Log.v("Url :", " " + url);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String result = bufferedReader.readLine();
                    Log.v("Result", result);
                    return result;
                } catch (Exception e) {
                    e.printStackTrace();
                    flag = true;
                    return null;
                }
            }
        }
        RegisterUser ru = new RegisterUser();
        ru.execute(urlSuffix);
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
