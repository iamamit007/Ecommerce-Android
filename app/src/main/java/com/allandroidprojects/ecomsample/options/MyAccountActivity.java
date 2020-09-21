package com.allandroidprojects.ecomsample.options;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.allandroidprojects.ecomsample.R;
import com.allandroidprojects.ecomsample.login.CustomerDetailResponse;
import com.allandroidprojects.ecomsample.login.CustomerRegisterResponse;
import com.allandroidprojects.ecomsample.login.Shipping;
import com.allandroidprojects.ecomsample.registration.AddAddressActivity;
import com.allandroidprojects.ecomsample.utility.ApiClient;
import com.allandroidprojects.ecomsample.utility.ApiInterface;
import com.allandroidprojects.ecomsample.utility.Catagories;
import com.allandroidprojects.ecomsample.utility.NetworkCallBack;
import com.allandroidprojects.ecomsample.utility.NetworkResponse;
import com.velectico.rbm.network.callbacks.NetworkError;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import retrofit2.Call;

public class MyAccountActivity extends AppCompatActivity {
    TextView name;
    TextView email;
    TextView address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        name = (TextView) findViewById(R.id.fullname);
        address = (TextView) findViewById(R.id.addressdetail);
        email = (TextView) findViewById(R.id.email);
        SharedPreferences sh
                = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);

        String fname = sh.getString("firstName", "");
        String lname = sh.getString("lastName", "");
        String emailTxt = sh.getString("email", "");

        name.setText(fname + "" + lname);
        email.setText(emailTxt);

        callProfileApiList();
    }

    public void callProfileApiList(){

        ApiInterface apiInterface = ApiClient.getInstance().getClient().create(ApiInterface.class);
        Call<CustomerDetailResponse> responseCall = apiInterface.getCustomerProfile();
        responseCall.enqueue(callBack);

    }



    private NetworkCallBack callBack = new NetworkCallBack<CustomerDetailResponse>() {
        @Override
        public void onSuccessNetwork(@Nullable Object data, @NotNull NetworkResponse response) {
            Log.d("ZINGAKART",response.toString());
            Toast.makeText(MyAccountActivity.this,"Profile UP."+response.getData().toString(),Toast.LENGTH_SHORT).show();
            CustomerDetailResponse userDetail = (CustomerDetailResponse) response.getData();
//            List<Shipping> addr = userDetail.getShipping();
//            String add = addr.get(0).getAddress_1();
            address.setText(userDetail.toString());
        }

        @Override
        public void onFailureNetwork(@Nullable Object data, @NotNull NetworkError error) {

        }
    };
}