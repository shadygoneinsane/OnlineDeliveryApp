package hezra.wingsnsides.ChickenWings;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import hezra.wingsnsides.Main.MainFragment;
import hezra.wingsnsides.Main.MainNavigationActivity;
import hezra.wingsnsides.OrderHere.OrderPackage;
import hezra.wingsnsides.R;
import hezra.wingsnsides.Utils.Constants;
import hezra.wingsnsides.Utils.Preferences;

import static android.content.ContentValues.TAG;
import static hezra.wingsnsides.Main.MainNavigationActivity.orderPackage;

public class AddChickenItemFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private static final String P_ID = "P_ID";
    private static final String P_NAME = "P_NAME";
    private static final String P_DETAIL = "P_DETAIL";
    private static final String U_ID = "U_ID";

    private String title, detail, pid;

    private OnFragmentInteractionListener mListener;
    private View addOne, removeOne;
    private int countItem = 1, countPcs = 6;
    private TextView itemCount;
    private TextView title_tv, detail_tv, total_tv;
    private Button add_to_basket;
    private CheckBox pcs6_cb, pcs12_cb, pcs18_cb, pcs25_cb, pcs30_cb;
    private int pcsCost, total_with_count;
    private File CHICKENFLAVOURS;
    private List<ChickenFlavour> packageList;

    private String filename = Constants.CHICKENFLAVOURS;

    public AddChickenItemFragment() {
        // Required empty public constructor
    }

    public static AddChickenItemFragment newInstance(ChickenFlavour packageItem) {
        AddChickenItemFragment fragment = new AddChickenItemFragment();
        Bundle args = new Bundle();
        packageItem.getPID();
        packageItem.getPNAME();
        packageItem.getPDETAIL();
        packageItem.getCID();

        args.putString(P_ID, packageItem.getPID());
        args.putString(P_NAME, packageItem.getPNAME());
        args.putString(P_DETAIL, packageItem.getPDETAIL());

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pid = getArguments().getString(P_ID);
            title = getArguments().getString(P_NAME);
            detail = getArguments().getString(P_DETAIL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_add_chicken_item, container, false);
        addOne = (View) parentView.findViewById(R.id.addOne);
        removeOne = (View) parentView.findViewById(R.id.removeOne);
        addOne.setOnClickListener(this);
        removeOne.setOnClickListener(this);
        itemCount = (TextView) parentView.findViewById(R.id.itemCount);
        itemCount.setText(String.valueOf(countItem));
        title_tv = (TextView) parentView.findViewById(R.id.title);
        title_tv.setText(title);

        detail_tv = (TextView) parentView.findViewById(R.id.detail);
        detail_tv.setText(detail);

        add_to_basket = (Button) parentView.findViewById(R.id.add_to_basket);
        add_to_basket.setOnClickListener(this);

        total_tv = (TextView) parentView.findViewById(R.id.total_tv);
        //setting default order data
        pcsCost = 2000;
        countPcs = 6;
        total_tv.setText(String.valueOf(countItem * pcsCost));

        pcs6_cb = (CheckBox) parentView.findViewById(R.id.pcs6_cb);
        pcs6_cb.setChecked(true);
        pcs6_cb.setOnCheckedChangeListener(this);
        pcs12_cb = (CheckBox) parentView.findViewById(R.id.pcs12_cb);
        pcs12_cb.setOnCheckedChangeListener(this);
        pcs18_cb = (CheckBox) parentView.findViewById(R.id.pcs18_cb);
        pcs18_cb.setOnCheckedChangeListener(this);
        pcs25_cb = (CheckBox) parentView.findViewById(R.id.pcs25_cb);
        pcs25_cb.setOnCheckedChangeListener(this);
        pcs30_cb = (CheckBox) parentView.findViewById(R.id.pcs30_cb);
        pcs30_cb.setOnCheckedChangeListener(this);

        packageList = new ArrayList<>();

        return parentView;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (compoundButton.getId() == R.id.pcs6_cb) {
            pcs12_cb.setOnCheckedChangeListener(null);
            pcs12_cb.setChecked(false);
            pcs18_cb.setOnCheckedChangeListener(null);
            pcs18_cb.setChecked(false);
            pcs25_cb.setOnCheckedChangeListener(null);
            pcs25_cb.setChecked(false);
            pcs30_cb.setOnCheckedChangeListener(null);
            pcs30_cb.setChecked(false);

            pcs12_cb.setOnCheckedChangeListener(this);
            pcs18_cb.setOnCheckedChangeListener(this);
            pcs25_cb.setOnCheckedChangeListener(this);
            pcs30_cb.setOnCheckedChangeListener(this);

            pcsCost = 2000;
            countPcs = 6;
            total_tv.setText(String.valueOf(countItem * pcsCost));
        } else if (compoundButton.getId() == R.id.pcs12_cb) {
            pcs6_cb.setOnCheckedChangeListener(null);
            pcs6_cb.setChecked(false);
            pcs18_cb.setOnCheckedChangeListener(null);
            pcs18_cb.setChecked(false);
            pcs25_cb.setOnCheckedChangeListener(null);
            pcs25_cb.setChecked(false);
            pcs30_cb.setOnCheckedChangeListener(null);
            pcs30_cb.setChecked(false);

            pcs6_cb.setOnCheckedChangeListener(this);
            pcs18_cb.setOnCheckedChangeListener(this);
            pcs25_cb.setOnCheckedChangeListener(this);
            pcs30_cb.setOnCheckedChangeListener(this);

            pcsCost = 3200;
            countPcs = 12;
            total_tv.setText(String.valueOf(countItem * pcsCost));
        } else if (compoundButton.getId() == R.id.pcs18_cb) {
            pcs6_cb.setOnCheckedChangeListener(null);
            pcs6_cb.setChecked(false);
            pcs12_cb.setOnCheckedChangeListener(null);
            pcs12_cb.setChecked(false);
            pcs25_cb.setOnCheckedChangeListener(null);
            pcs25_cb.setChecked(false);
            pcs30_cb.setOnCheckedChangeListener(null);
            pcs30_cb.setChecked(false);

            pcs6_cb.setOnCheckedChangeListener(this);
            pcs12_cb.setOnCheckedChangeListener(this);
            pcs25_cb.setOnCheckedChangeListener(this);
            pcs30_cb.setOnCheckedChangeListener(this);

            pcsCost = 4200;
            countPcs = 18;
            total_tv.setText(String.valueOf(countItem * pcsCost));
        } else if (compoundButton.getId() == R.id.pcs25_cb) {
            pcs6_cb.setOnCheckedChangeListener(null);
            pcs6_cb.setChecked(false);
            pcs12_cb.setOnCheckedChangeListener(null);
            pcs12_cb.setChecked(false);
            pcs18_cb.setOnCheckedChangeListener(null);
            pcs18_cb.setChecked(false);
            pcs30_cb.setOnCheckedChangeListener(null);
            pcs30_cb.setChecked(false);

            pcs6_cb.setOnCheckedChangeListener(this);
            pcs12_cb.setOnCheckedChangeListener(this);
            pcs18_cb.setOnCheckedChangeListener(this);
            pcs30_cb.setOnCheckedChangeListener(this);

            pcsCost = 5500;
            countPcs = 25;
            total_tv.setText(String.valueOf(countItem * pcsCost));
        } else if (compoundButton.getId() == R.id.pcs30_cb) {
            pcs6_cb.setOnCheckedChangeListener(null);
            pcs6_cb.setChecked(false);
            pcs12_cb.setOnCheckedChangeListener(null);
            pcs12_cb.setChecked(false);
            pcs18_cb.setOnCheckedChangeListener(null);
            pcs18_cb.setChecked(false);
            pcs25_cb.setOnCheckedChangeListener(null);
            pcs25_cb.setChecked(false);

            pcs6_cb.setOnCheckedChangeListener(this);
            pcs12_cb.setOnCheckedChangeListener(this);
            pcs18_cb.setOnCheckedChangeListener(this);
            pcs25_cb.setOnCheckedChangeListener(this);

            pcsCost = 6800;
            countPcs = 30;
            total_tv.setText(String.valueOf(countItem * pcsCost));
        }
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
            case R.id.addOne:
                countItem += 1;
                itemCount.setText(String.valueOf(countItem));
                total_tv.setText(String.valueOf(countItem * pcsCost));
                break;

            case R.id.removeOne:
                countItem -= 1;
                itemCount.setText(String.valueOf(countItem));
                total_tv.setText(String.valueOf(countItem * pcsCost));
                break;

            case R.id.add_to_basket:
                OrderPackage orderHolder = new OrderPackage();
                orderHolder.setOAMOUNT(String.valueOf(countItem * pcsCost));
                orderHolder.setPID(pid);
                orderHolder.setOQUANTITY(String.valueOf(countItem));
                orderHolder.setUID(Preferences.getStringPreference(getActivity(), Constants.UID));

                orderPackage.add(orderHolder);

                replaceFragment(MainNavigationActivity.fragment = new MainFragment());
                if (mListener != null) {
                    MainNavigationActivity.cartCount = String.valueOf(itemCount.getText().toString());
                    mListener.onFragmentInteraction();
                }
                break;
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

    private void setAdapter(String response) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(response);
            Log.v("Logs", jsonObject.toString());
            JSONArray jsonArray = jsonObject.getJSONArray("CHICKENWINGSFLAVOURS");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                ChickenFlavour listFlavours = new ChickenFlavour();
                listFlavours.setPID(jsonObject1.getString("P_ID"));
                listFlavours.setPNAME(jsonObject1.getString("P_NAME"));
                listFlavours.setPDETAIL(jsonObject1.getString("P_DETAIL"));
                listFlavours.setCID(jsonObject1.getString("C_ID"));

                packageList.add(listFlavours);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }

    public String readFileJsonAsString() {
        String result;
        CHICKENFLAVOURS = new File(getActivity().getFilesDir(), filename);

        long length = CHICKENFLAVOURS.length();
        if (length < 1 || length > Integer.MAX_VALUE) {
            result = "";
            Log.w(TAG, "File is empty or huge: " + filename);
        } else {
            try {
                FileReader in = new FileReader(CHICKENFLAVOURS);
                char[] content = new char[(int) length];

                int numRead = in.read(content);
                if (numRead != length) {
                    Log.e(TAG, "Incomplete read of " + filename + ". Read chars " + numRead + " of " + length);
                }
                result = new String(content, 0, numRead);
            } catch (Exception ex) {
                Log.e(TAG, "Failure reading " + this.filename, ex);
                result = "";
            }
        }
        Log.e(TAG, result);
        return result;
    }
}
