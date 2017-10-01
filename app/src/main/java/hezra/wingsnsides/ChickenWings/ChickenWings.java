package hezra.wingsnsides.ChickenWings;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import hezra.wingsnsides.R;
import hezra.wingsnsides.Utils.Constants;

import static hezra.wingsnsides.Utils.Constants.CHICKENFLAVOURURL;

public class ChickenWings extends Fragment implements View.OnClickListener, ViewPager.OnPageChangeListener,
        OnPackageClickListener, AddChickenItemFragment.OnFragmentInteractionListener {

    private OnFragmentInteractionListener mListener;

    private ViewPager intro_images;
    private LinearLayout pager_indicator;
    private int dotsCount, currentPage = 0;
    private ImageView[] dots;
    private ViewPagerAdapter mAdapter;
    private Handler handler;
    private Runnable Update;
    private List<ChickenFlavour> packageList;
    private RecyclerView recyclerView;
    private File CHICKENFLAVOURS;
    //private String filename = Constants.CHICKENFLAVOURS;
    private String TAG = "Hezra";

    private int[] mImageResources = {
            R.drawable.hezra_chicken_wings,
            R.drawable.classic_buffalo_wings,
    };

    public ChickenWings() {
        // Required empty public constructor
    }

    public static ChickenWings newInstance(String param1, String param2) {
        ChickenWings fragment = new ChickenWings();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View parent_view = inflater.inflate(R.layout.fragment_chicken_wings, container, false);

        intro_images = (ViewPager) parent_view.findViewById(R.id.pager_introduction);
        pager_indicator = (LinearLayout) parent_view.findViewById(R.id.viewPagerCountDots);
        mAdapter = new ViewPagerAdapter(getActivity(), mImageResources);
        intro_images.setAdapter(mAdapter);
        intro_images.setCurrentItem(0);
        intro_images.setOnPageChangeListener(this);
        setUiPageViewController();

        recyclerView = (RecyclerView) parent_view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //recyclerView.setAdapter(new ChickenFlavourAdapter(packageList, this));
        packageList = new ArrayList<>();

        String jsonString = readFileJsonAsString();
        setAdapter(jsonString);

        FetchJson fetchJson = new FetchJson(getActivity());
        fetchJson.execute(CHICKENFLAVOURURL);

        return parent_view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
        handler = new Handler();
        Update = new Runnable() {
            public void run() {
                if (currentPage == dotsCount) {
                    currentPage = 0;
                }
                intro_images.setCurrentItem(currentPage++, true);
                handler.postDelayed(Update, 2500);
            }
        };
        handler.postDelayed(Update, 2500);
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
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    private void setUiPageViewController() {

        dotsCount = mAdapter.getCount();
        dots = new ImageView[dotsCount];

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(getActivity());
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(4, 0, 4, 0);
            pager_indicator.addView(dots[i], params);
        }
        dots[0].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));

        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
        handler = new Handler();
        Update = new Runnable() {
            public void run() {
                if (currentPage == dotsCount) {
                    currentPage = 0;
                }
                intro_images.setCurrentItem(currentPage++, true);
                handler.postDelayed(Update, 2500);
            }
        };
        handler.postDelayed(Update, 2500);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < dotsCount; i++) {
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));
        }

        dots[position].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
        currentPage = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onStop() {
        handler.removeCallbacksAndMessages(null);
        super.onStop();
    }

    @Override
    public void onClick(ChickenFlavour packageItem) {
        packageItem.getPID();
        packageItem.getPNAME();
        packageItem.getPDETAIL();
        packageItem.getCID();

        AddChickenItemFragment addItemFragment;
        addItemFragment = AddChickenItemFragment.newInstance(packageItem);
        replaceFragment(addItemFragment);
    }

    @Override
    public void onFragmentInteraction() {

    }

    private class ViewPagerAdapter extends PagerAdapter {

        private Context mContext;
        private int[] mResources;

        ViewPagerAdapter(Context mContext, int[] mResources) {
            this.mContext = mContext;
            this.mResources = mResources;
        }

        @Override
        public int getCount() {
            return mResources.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((LinearLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View itemView = LayoutInflater.from(mContext).inflate(R.layout.pager_item, container, false);

            ImageView imageView = (ImageView) itemView.findViewById(R.id.img_pager_item);
            imageView.setImageResource(mResources[position]);

            container.addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        /*AddChickenItemFragment addItemFragment;
        switch (view.getId()) {
            case R.id.card_view_special_chicken_wings:
                addItemFragment = AddChickenItemFragment.newInstance(title1.getText().toString(), price1.getText().toString());
                replaceFragment(addItemFragment);
                break;

            case R.id.card_view2:
                addItemFragment = AddChickenItemFragment.newInstance(title2.getText().toString(), price2.getText().toString());
                replaceFragment(addItemFragment);
                break;
        }*/
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
        void onFragmentInteraction(Uri uri);
    }

    private class FetchJson extends AsyncTask<String, Integer, String> {
        private Context context;

        private FetchJson(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... sUrl) {
            InputStream input = null;
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            StringBuffer buffer = null;
            String line = "";
            FileOutputStream outputStream;

            try {
                URL url = new URL(sUrl[0]);
                Log.v("ToBeDownloaded", url.toString());
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP " + connection.getResponseCode() + " " + connection.getResponseMessage();
                }

                packageList = new ArrayList<>();
                CHICKENFLAVOURS = new File(context.getFilesDir(), Constants.CHICKENFLAVOURS);
                CHICKENFLAVOURS.delete();

                input = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(input));

                buffer = new StringBuffer();

                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                    Log.d("Response ", line);
                }

                outputStream = getActivity().openFileOutput(Constants.CHICKENFLAVOURS, Context.MODE_PRIVATE);
                outputStream.write(buffer.toString().getBytes());
                outputStream.close();

                return buffer.toString();
            } catch (Exception e) {
                return e.toString();
            } finally {
                try {
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }
                if (connection != null)
                    connection.disconnect();
            }
        }

        @Override
        protected void onPostExecute(String response) {
            setAdapter(response);
        }
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

            recyclerView.setAdapter(new ChickenFlavourAdapter(packageList, ChickenWings.this));
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }

    public String readFileJsonAsString() {
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
