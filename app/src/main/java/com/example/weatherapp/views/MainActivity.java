package com.example.weatherapp.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.weatherapp.R;
import com.example.weatherapp.data.WeatherModel;
import com.example.weatherapp.databinding.ActivityMainBinding;
import com.example.weatherapp.logic.MainPresenter;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private MainPresenter presenter;
    private EditText editText;
    private ImageView changeLocationIcon,weatherIcon;
    private TextView currentCity,temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        initViews();
        initOnClick();
        initEditBox();

        //TODO :
        presenter = new MainPresenter(this);
        presenter.getWeatherForCurrentLocation();
    }

    private void initEditBox() {

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if((event!=null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) || (actionId == EditorInfo.IME_ACTION_DONE))){
                    Log.d("Clima","onEditorAction() true was called");
                    initOnClick();
                }
                Log.d("Clima","onEditorAction() FALSE was called");
                return false;
            }
        });

        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(!hasFocus){
                    editText.setText("");
                    Log.d("Clima","setOnFocusChangeListener() was called");
                }
            }
        });

    }



    private void initOnClick() {
        changeLocationIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String city = editText.getText().toString();
                presenter.setupRetrofitWithCity(city);
            }
        });
    }

    private void initViews() {
        editText = binding.editTextCity;
        editText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        currentCity = binding.textViewCurrentCity;
        changeLocationIcon = binding.imageViewChange;
        temp = binding.editTextTemp;
        weatherIcon=binding.imageViewWeather;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Clima","onResume() called");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        presenter.requestPermission(requestCode,grantResults);
    }

    public void updateUI(WeatherModel model) {
        currentCity.setText(model.getName());
        temp.setText(model.getTemp());
        int resourceId = getResources().getIdentifier(model.getWeatherIcon(),"drawable",getPackageName());
        weatherIcon.setImageResource(resourceId);
        editText.setHint(model.getName());
    }
}

