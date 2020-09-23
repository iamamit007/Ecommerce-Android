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
import com.allandroidprojects.ecomsample.login.Shipping;
import com.allandroidprojects.ecomsample.startup.MainActivity;
import com.allandroidprojects.ecomsample.utility.ApiClient;
import com.allandroidprojects.ecomsample.utility.ApiInterface;
import com.allandroidprojects.ecomsample.utility.NetworkCallBack;
import com.allandroidprojects.ecomsample.utility.NetworkResponse;
import com.velectico.rbm.network.callbacks.NetworkError;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;

import retrofit2.Call;

public class AddAddressActivity extends AppCompatActivity {

    EditText pintx;
    EditText address;
    EditText citytx;
    EditText statetx;
    EditText country;
    EditText confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        pintx = (EditText) findViewById(R.id.pin);
        address = (EditText) findViewById(R.id.address);
        citytx = (EditText) findViewById(R.id.city);
        statetx = (EditText) findViewById(R.id.state);
        country = (EditText) findViewById(R.id.country);
        Button save = (Button) findViewById(R.id.saveadd);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AddAddressActivity.this,"Sign UP.",Toast.LENGTH_SHORT).show();
                callAddAddressApi();
            }
        });
        String add = getIntent().getStringExtra("add");
        String pin = getIntent().getStringExtra("add3");
        String city = getIntent().getStringExtra("add1");
        String state = getIntent().getStringExtra("add2");
        String cntry = getIntent().getStringExtra("cntry");
        if (add != null){
            address.setText(add);
        }
        if (pin != null){
            pintx.setText(pin);
        }
        if (city != null){
            citytx.setText(city);
        }
        if (cntry != null){
            country.setText(cntry);
        }
        if (state != null){
            statetx.setText(state);
        }
    }

    public void callAddAddressApi(){
        Shipping sdata = new Shipping(getIntent().getStringExtra("fname"),getIntent().getStringExtra("lname"),"",
                address.getText().toString(),"",citytx.getText().toString(),
                pintx.getText().toString(),country.getText().toString(),
                statetx.getText().toString());
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
            Intent intent = new Intent(AddAddressActivity.this, MainActivity.class);
            intent.putExtra("fromAddress", "true");
            //intent.putExtra("lname", lastName.getText().toString());
            startActivity(intent);
        }

        @Override
        public void onFailureNetwork(@Nullable Object data, @NotNull NetworkError error) {

        }
    };
}