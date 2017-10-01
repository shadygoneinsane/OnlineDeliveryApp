package hezra.wingsnsides.OrderHere;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import hezra.wingsnsides.ChickenWings.ChickenFlavour;
import hezra.wingsnsides.R;
import hezra.wingsnsides.Utils.Constants;

import static android.content.ContentValues.TAG;
import static hezra.wingsnsides.Main.MainNavigationActivity.orderPackage;

public class OrderFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private File SignUpFile;
    private File CHICKENFLAVOURS;
    private OnFragmentInteractionListener mListener;
    private List<ChickenFlavour> ChickenFlavoursList;
    private List<FinalOrderPackage> finalOrderList;
    private RecyclerView recyclerView;
    private View parent_view;

    public OrderFragment() {
        // Required empty public constructor
    }


    public static OrderFragment newInstance(String param1, String param2) {
        OrderFragment fragment = new OrderFragment();
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

        finalOrderList = new ArrayList<>();

        String ChickenFlavoursJson = readChickenFileJsonAsString();
        if (ChickenFlavoursJson != null) {
            ChickenFlavoursList = new ArrayList<>();
            //fetching items from Chicken Flavour Json and creating an arrayList
            ChickenFlavoursList = parseChickenFlavourJsonToFlavourList(ChickenFlavoursJson);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parent_view = inflater.inflate(R.layout.fragment_order, container, false);
        recyclerView = (RecyclerView) parent_view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        ChickenFlavour chickenFlavour;
        FinalOrderPackage finalOrderPackage = new FinalOrderPackage();

        for (int i = 0; i < ChickenFlavoursList.size(); i++) {
            chickenFlavour = ChickenFlavoursList.get(i);

            for (int j = 0; j < orderPackage.size(); j++) {
                OrderPackage orderHolder = orderPackage.get(j);

                if (chickenFlavour.getPID().equalsIgnoreCase(orderHolder.getPID())) {
                    finalOrderPackage.setPID(orderHolder.getPID());
                    finalOrderPackage.setUID(orderHolder.getUID());
                    finalOrderPackage.setOQUANTITY(orderHolder.getOQUANTITY());
                    finalOrderPackage.setPID(orderHolder.getPID());
                    finalOrderPackage.setPID(orderHolder.getPID());
                    finalOrderPackage.setOAMOUNT(orderHolder.getOAMOUNT());
                    //Using Name from Chicken Package
                    finalOrderPackage.setONAME(chickenFlavour.getPNAME());
                    finalOrderList.add(finalOrderPackage);
                    break;
                }
            }
            recyclerView.setAdapter(new FinalOrderAdapter(finalOrderList));
        }

        return parent_view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private List<ChickenFlavour> parseChickenFlavourJsonToFlavourList(String response) {
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

                ChickenFlavoursList.add(listFlavours);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
        return ChickenFlavoursList;
    }

    public String readSignUpFileJsonAsString() {
        String result;
        SignUpFile = new File(getActivity().getFilesDir(), Constants.SignUpJsonFile);

        long length = CHICKENFLAVOURS.length();
        if (length < 1 || length > Integer.MAX_VALUE) {
            result = "";
            Log.w(TAG, "File is empty or huge: " + Constants.SignUpJsonFile);
        } else {
            try {
                FileReader in = new FileReader(CHICKENFLAVOURS);
                char[] content = new char[(int) length];

                int numRead = in.read(content);
                if (numRead != length) {
                    Log.e(TAG, "Incomplete read of " + Constants.SignUpJsonFile + ". Read chars " + numRead + " of " + length);
                }
                result = new String(content, 0, numRead);
            } catch (Exception ex) {
                Log.e(TAG, "Failure reading " + Constants.SignUpJsonFile, ex);
                result = "";
            }
        }
        Log.e(TAG, result);
        return result;
    }

    public String readChickenFileJsonAsString() {
        String result;
        CHICKENFLAVOURS = new File(getActivity().getFilesDir(), Constants.CHICKENFLAVOURS);

        long length = CHICKENFLAVOURS.length();
        if (length < 1 || length > Integer.MAX_VALUE) {
            result = "";
            Log.w(TAG, "File is empty or huge: " + Constants.CHICKENFLAVOURS);
        } else {
            try {
                FileReader in = new FileReader(CHICKENFLAVOURS);
                char[] content = new char[(int) length];

                int numRead = in.read(content);
                if (numRead != length) {
                    Log.e(TAG, "Incomplete read of " + Constants.CHICKENFLAVOURS + ". Read chars " + numRead + " of " + length);
                }
                result = new String(content, 0, numRead);
            } catch (Exception ex) {
                Log.e(TAG, "Failure reading " + Constants.CHICKENFLAVOURS, ex);
                result = "";
            }
        }
        Log.e(TAG, result);
        return result;
    }

}
