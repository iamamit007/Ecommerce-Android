package com.allandroidprojects.ecomsample.options;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.allandroidprojects.ecomsample.R;
import com.allandroidprojects.ecomsample.login.CustomerDetailResponse;
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

        callProfileApiList();
    }

    public void callProfileApiList(){

        ApiInterface apiInterface = ApiClient.getInstance().getClient().create(ApiInterface.class);
        Call<List<CustomerDetailResponse>> responseCall = apiInterface.getCustomerProfile();
        responseCall.enqueue(callBack);

    }



    private NetworkCallBack callBack = new NetworkCallBack<List<CustomerDetailResponse>>() {
        @Override
        public void onSuccessNetwork(@Nullable Object data, @NotNull NetworkResponse response) {
            Log.d("ZINGAKART",response.toString());
            Toast.makeText(MyAccountActivity.this,"Profile UP."+response.getData().toString(),Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onFailureNetwork(@Nullable Object data, @NotNull NetworkError error) {

        }
    };
}