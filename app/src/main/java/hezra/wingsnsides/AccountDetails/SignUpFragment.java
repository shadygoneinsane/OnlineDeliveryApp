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
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import hezra.wingsnsides.Main.MainNavigationActivity;
import hezra.wingsnsides.R;
import hezra.wingsnsides.Utils.Constants;
import hezra.wingsnsides.Utils.Preferences;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SignUpFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SignUpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUpFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private boolean flag = false;
    private OnFragmentInteractionListener mListener;
    private EditText fname, lname, email, password, mobile_end;
    private File SignUpFile;

    public SignUpFragment() {
        // Required empty public constructor
    }

    public static SignUpFragment newInstance(String param1, String param2) {
        SignUpFragment fragment = new SignUpFragment();
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
        View parent_view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        Button create_account_button = (Button) parent_view.findViewById(R.id.create_account_button);
        create_account_button.setOnClickListener(this);
        fname = (EditText) parent_view.findViewById(R.id.fname_et);
        lname = (EditText) parent_view.findViewById(R.id.lname_et);
        email = (EditText) parent_view.findViewById(R.id.email_et);
        password = (EditText) parent_view.findViewById(R.id.password_et);
        mobile_end = (EditText) parent_view.findViewById(R.id.mobile_number_end_et);
        return parent_view;
    }

    public void onButtonPressed() {
        /*if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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
            case R.id.create_account_button:
                register(fname.getText().toString(), lname.getText().toString(),
                        email.getText().toString(), password.getText().toString(),
                        mobile_end.getText().toString());
                break;

        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction();

        void onFragmentClosed(boolean result);
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

    private void register(String fname, String lname, String email, String password, String mobile) {

        String urlSuffix = "?fname=" + fname + "&" + "lname=" + lname
                + "&" + "email=" + email + "&" + "password=" + password
                + "&" + "mobile=" + mobile;
        class RegisterUser extends AsyncTask<String, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getActivity(), "Please Wait", null, true, true);
            }

            @Override
            protected String doInBackground(String... params) {
                String urlSuffix = params[0];
                BufferedReader bufferedReader;
                try {
                    URL url = new URL(Constants.BASEURLSIGNUP + urlSuffix);
                    Log.v("Url :", " " + url);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String result = bufferedReader.readLine();
                    Log.v("Response", result);
                    return result;
                } catch (Exception e) {
                    e.printStackTrace();
                    flag = true;
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String response) {
                super.onPostExecute(response);
                loading.dismiss();
                SIGNUP signup = new SIGNUP();
                try {
                    if (!flag && response != null) {
                        try {
                            //Parsing the SignUp json
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject jsonObject1 = jsonObject.getJSONObject("SIGNUP");
                            signup.setStatus(jsonObject1.getString("status"));
                            signup.setuID(jsonObject1.getString("U_ID"));
                            signup.setFNAME(jsonObject1.getString("FNAME"));
                            signup.setLNAME(jsonObject1.getString("LNAME"));
                            signup.setEMAIL(jsonObject1.getString("EMAIL"));
                            signup.setMOBILE(jsonObject1.getString("MOBILE"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        if (signup.getStatus().equalsIgnoreCase("SUCCESS")) {
                            Toast.makeText(getActivity().getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
                            Preferences.setStringPreference(getActivity(), Constants.EMAIL, signup.getEMAIL());
                            Preferences.setStringPreference(getActivity(), Constants.FNAME, signup.getFNAME());
                            Preferences.setStringPreference(getActivity(), Constants.LNAME, signup.getLNAME());
                            Preferences.setStringPreference(getActivity(), Constants.MOBILE, signup.getMOBILE());
                            Preferences.setStringPreference(getActivity(), Constants.UID, signup.getuID());
                            Preferences.setBooleanPreference(getActivity(), Constants.ISLOGGEDIN, true);

                            SignUpFile = new File(getActivity().getFilesDir(), Constants.SignUpJsonFile);
                            FileOutputStream outputStream;
                            outputStream = getActivity().openFileOutput(Constants.SignUpJsonFile, Context.MODE_PRIVATE);
                            outputStream.write(response.getBytes());
                            outputStream.close();

                            replaceFragment(MainNavigationActivity.fragment = new AccountFragment());

                            if (mListener != null) {
                                mListener.onFragmentInteraction();
                            }
                        } else if (signup.getStatus().equalsIgnoreCase("EXISTS")) {
                            Toast.makeText(getActivity().getApplicationContext(), "Email already exists", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity().getApplicationContext(), "Connection Error..", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "Connection Error.. \n Please try again !!", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Toast.makeText(getActivity().getApplicationContext(), "Connection Error.. \n Please try again !!", Toast.LENGTH_LONG).show();
                }
            }
        }
        RegisterUser ru = new RegisterUser();
        ru.execute(urlSuffix);
    }
}
