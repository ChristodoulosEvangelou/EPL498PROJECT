package com.example.project498;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.example.project498.databinding.FragmentApiCallBinding;
import com.squareup.picasso.Picasso;
import org.chromium.net.CronetEngine;
import org.chromium.net.CronetException;
import org.chromium.net.UrlRequest;
import org.chromium.net.UrlResponseInfo;
import org.json.JSONException;
import org.json.JSONObject;
import java.nio.ByteBuffer;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * This class is responsible for making an api call to open weather rest api and display the
 * temperature and condition on the screen depending on the city of Cyprus that the user has selected
 */
public class ApiCallFragment extends Fragment {

    private CronetEngine.Builder myBuilder;
    private CronetEngine cronetEngine;
    private Executor executor;
    private TextView weatherTxt;
    private TextView cityTxt;
    private ImageView weatherIcon;
    private ProgressBar progressBar;
    private Spinner citySpinner;
    private String selectedCity;

    private FragmentApiCallBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the binding for this fragment
        binding = FragmentApiCallBinding.inflate(inflater, container, false);
        weatherTxt = binding.weatherTxt;
        cityTxt = binding.cityTxt;
        weatherIcon = binding.imageView;
        progressBar = binding.progressBar;
        citySpinner = binding.dropdownCity;

        // Create a spinner with city options
        String[] cities = getResources().getStringArray(R.array.cities_array);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, cities);
        citySpinner.setAdapter(adapter);
        selectedCity = citySpinner.getItemAtPosition(0).toString();

        // when a new city is selected this method will be called
        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Update the selected city based on user choice
                selectedCity = parentView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle the case when nothing is selected
                // for now do nothing
            }
        });

        binding.buttonGetWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get weather
                makeWeatherApiRequest();
            }
        });

        // Make the weather API request
        makeWeatherApiRequest();
        return binding.getRoot();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myBuilder = new CronetEngine.Builder(getContext());
        cronetEngine = myBuilder.build();
        executor = Executors.newSingleThreadExecutor();
    }

    private void makeWeatherApiRequest() {
        // When making the request we want to hide the previous weather
        // icon and text and make the progress bar visible again
        progressBar.setVisibility(View.VISIBLE);
        cityTxt.setVisibility(View.GONE);
        weatherIcon.setVisibility(View.GONE);
        weatherTxt.setVisibility(View.GONE);

        // set where the api call would be made to
        UrlRequest.Builder requestBuilder = cronetEngine.newUrlRequestBuilder(
                "https://api.weatherapi.com/v1/current.json?key=263660a469914eb1a6d74143231310&q=" + selectedCity,
                new EPL498RequestCallback(),
                executor);

        UrlRequest request = requestBuilder.build();
        // here we make the actual api call
        request.start();
    }

    private void updateWeatherTextView(String weatherData) {
        /**
         * UI components can only be accessed and modified from the main thread.
         * Since API calls are asynchronous and may execute on background threads,
         * we use runOnUiThread to safely update the TextView with the weather data
         * on the main thread, ensuring a smooth user experience.
         */
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonObject = new JSONObject(weatherData);
                    JSONObject current = jsonObject.getJSONObject("current");

                    String temperatureC = current.getString("temp_c");
                    String conditionText = current.getJSONObject("condition").getString("text");
                    String iconUrl = current.getJSONObject("condition").getString("icon");

                    // Update the TextView with temperature and condition text
                    weatherTxt.setText("\n" + temperatureC + "°C\n" + conditionText);
                    cityTxt.setText(selectedCity);

                    // We use Picasso to get the icon that the api returned and display
                    // it into the weatherIcon
                    Picasso.get().load("https:" + iconUrl).resize(300, 300).into(weatherIcon);

                    // now let's make the progress bar invisible again
                    // and make visible the weather icon and condition
                    progressBar.setVisibility(View.GONE);
                    cityTxt.setVisibility(View.VISIBLE);
                    weatherIcon.setVisibility(View.VISIBLE);
                    weatherTxt.setVisibility(View.VISIBLE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * This inner class is responsible to extend the url request callback and override the
     * appropriate methods according to the implementation we want to setup
     */
    class EPL498RequestCallback extends UrlRequest.Callback {
        private static final String TAG = "EPL498RequestCallback";

        @Override
        public void onResponseStarted(UrlRequest request, UrlResponseInfo info) {
            Log.i(TAG, "onResponseStarted method called.");
            // You should call the request.read() method before the request can be
            // further processed. The following instruction provides a ByteBuffer object
            // with a capacity of 102400 bytes for the read() method. The same buffer
            // with data is passed to the onReadCompleted() method.
            request.read(ByteBuffer.allocateDirect(102400));
        }

        @Override
        public void onReadCompleted(UrlRequest request, UrlResponseInfo info, ByteBuffer byteBuffer) {
            Log.i(TAG, "onReadCompleted method called.");
            // You should keep reading the request until there's no more data.
            byteBuffer.flip();
            byte[] bytes = new byte[byteBuffer.remaining()];
            byteBuffer.get(bytes);
            String response = new String(bytes);

            // Let's see the json result in Logcat
            Log.i("Forecast", response);
            updateWeatherTextView(response);

            byteBuffer.clear();
            request.read(byteBuffer);
        }

        @Override
        public void onSucceeded(UrlRequest request, UrlResponseInfo info) {
            Log.i(TAG, "onSucceeded method called.");
        }

        @Override
        public void onFailed(UrlRequest request, UrlResponseInfo info, CronetException error) {
            Log.i(TAG, "onFailed method called.");
        }

        @Override
        public void onRedirectReceived(UrlRequest request, UrlResponseInfo info, String newLocationUrl) throws Exception {
            // do nothing for now
        }
    }
}