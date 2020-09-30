package com.zingakart.android.registration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zingakart.android.R;
import com.zingakart.android.login.CustomerRegisterRequestParams;
import com.zingakart.android.login.CustomerRegisterResponse;
import com.zingakart.android.utility.ApiClient;
import com.zingakart.android.utility.ApiInterface;
import com.zingakart.android.utility.NetworkCallBack;
import com.zingakart.android.utility.NetworkResponse;
import com.velectico.rbm.network.callbacks.NetworkError;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import retrofit2.Call;

public class SignUpActivity extends AppCompatActivity {
    EditText firstName;
    EditText lastName;
    EditText username;
    EditText email;
    EditText password;
    EditText confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Button buttonSignup = (Button) findViewById(R.id.register);
         firstName = (EditText) findViewById(R.id.fname);
         lastName = (EditText) findViewById(R.id.lname);
         email = (EditText) findViewById(R.id.email);
         password = (EditText) findViewById(R.id.password);
         confirmPassword = (EditText) findViewById(R.id.confirmpassword);
        username = (EditText) findViewById(R.id.usrname);


        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SignUpActivity.this,"Sign UP.",Toast.LENGTH_SHORT).show();
                callRegisterApi();
            }
        });
    }
    public void callRegisterApi(){

        CustomerRegisterRequestParams param = new CustomerRegisterRequestParams(email.getText().toString(),
                firstName.getText().toString(),
                lastName.getText().toString(),
                username.getText().toString(),
                password.getText().toString());
        ApiInterface apiInterface = ApiClient.getInstance().getClient().create(ApiInterface.class);
        Call<CustomerRegisterResponse> responseCall = apiInterface.getregisterDetails(param);
        responseCall.enqueue(callBack);

    }
    private NetworkCallBack callBack = new NetworkCallBack<CustomerRegisterResponse>() {
        @Override
        public void onSuccessNetwork(@Nullable Object data, @NotNull NetworkResponse response) {
            Log.d("ZINGAKART Login",response.toString());
            Toast.makeText(SignUpActivity.this,"Sign UP."+response.getData().toString(),Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SignUpActivity.this, AddAddressActivity.class);
            intent.putExtra("fname", firstName.getText().toString());
            intent.putExtra("lname", lastName.getText().toString());
            startActivity(intent);

        }

        @Override
        public void onFailureNetwork(@Nullable Object data, @NotNull NetworkError error) {

        }
    };


}