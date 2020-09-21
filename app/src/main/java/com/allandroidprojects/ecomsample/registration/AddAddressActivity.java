package com.allandroidprojects.ecomsample.registration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.allandroidprojects.ecomsample.R;
import com.allandroidprojects.ecomsample.login.CustomerAddressRequestParams;
import com.allandroidprojects.ecomsample.login.CustomerAddressResponse;
import com.allandroidprojects.ecomsample.login.CustomerRegisterRequestParams;
import com.allandroidprojects.ecomsample.login.CustomerRegisterResponse;
import com.allandroidprojects.ecomsample.login.Shipping;
import com.allandroidprojects.ecomsample.utility.ApiClient;
import com.allandroidprojects.ecomsample.utility.ApiInterface;
import com.allandroidprojects.ecomsample.utility.NetworkCallBack;
import com.allandroidprojects.ecomsample.utility.NetworkResponse;
import com.velectico.rbm.network.callbacks.NetworkError;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;

public class AddAddressActivity extends AppCompatActivity {

    EditText pin;
    EditText address;
    EditText city;
    EditText state;
    EditText country;
    EditText confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        pin = (EditText) findViewById(R.id.pin);
        address = (EditText) findViewById(R.id.address);
        city = (EditText) findViewById(R.id.city);
        state = (EditText) findViewById(R.id.state);
        country = (EditText) findViewById(R.id.country);
        Button save = (Button) findViewById(R.id.saveadd);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AddAddressActivity.this,"Sign UP.",Toast.LENGTH_SHORT).show();
                callAddAddressApi();
            }
        });
    }

    public void callAddAddressApi(){
        Shipping sdata = new Shipping(getIntent().getStringExtra("fname"),getIntent().getStringExtra("lname"),"",
                address.getText().toString(),"",city.getText().toString(),
                pin.getText().toString(),country.getText().toString(),
                state.getText().toString());
        CustomerAddressRequestParams param = new CustomerAddressRequestParams(Collections.singletonList(sdata));
        ApiInterface apiInterface = ApiClient.getInstance().getClient().create(ApiInterface.class);
        Call<CustomerAddressResponse> responseCall = apiInterface.addAddressDetails(param);
        responseCall.enqueue(callBack);

    }
    private NetworkCallBack callBack = new NetworkCallBack<CustomerAddressResponse>() {
        @Override
        public void onSuccessNetwork(@Nullable Object data, @NotNull NetworkResponse response) {
            Log.d("ZINGAKART Login",response.toString());
            Toast.makeText(AddAddressActivity.this,"Sign UP."+response.getData().toString(),Toast.LENGTH_SHORT).show();
            //Intent intent = new Intent(SignUpActivity.this, AddAddressActivity.class);
            //intent.putExtra("fname", firstName.getText().toString());
            //intent.putExtra("lname", lastName.getText().toString());
            //startActivity(intent);
        }

        @Override
        public void onFailureNetwork(@Nullable Object data, @NotNull NetworkError error) {

        }
    };
}