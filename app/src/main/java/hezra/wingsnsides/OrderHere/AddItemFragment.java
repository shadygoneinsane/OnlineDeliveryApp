package hezra.wingsnsides.OrderHere;

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

public class AddItemFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String title;
    private String price;

    private OnFragmentInteractionListener mListener;
    private View addOne,
            removeOne;
    private int countItem = 0;
    private TextView itemCount;
    private TextView price_tv, title_tv;
    private Button add_to_basket;

    public AddItemFragment() {
        // Required empty public constructor
    }

    public static AddItemFragment newInstance(String param1, String param2) {
        AddItemFragment fragment = new AddItemFragment();
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
            title = getArguments().getString(ARG_PARAM1);
            price = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_add_item, container, false);
        addOne = (View) parentView.findViewById(R.id.addOne);
        removeOne = (View) parentView.findViewById(R.id.removeOne);
        addOne.setOnClickListener(this);
        removeOne.setOnClickListener(this);
        itemCount = (TextView) parentView.findViewById(R.id.itemCount);
        itemCount.setText(String.valueOf(countItem));
        title_tv = (TextView) parentView.findViewById(R.id.title);
        title_tv.setText(title);
        price_tv = (TextView) parentView.findViewById(R.id.price);
        price_tv.setText(price);

        add_to_basket = (Button) parentView.findViewById(R.id.add_to_basket);
        add_to_basket.setOnClickListener(this);
        return parentView;
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
                break;

            case R.id.removeOne:
                countItem -= 1;
                itemCount.setText(String.valueOf(countItem));
                break;

            case R.id.add_to_basket:
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
}
