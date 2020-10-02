package com.zingakart.android.options;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.zingakart.android.R;
import com.zingakart.android.login.CustomerDetailResponse;
import com.zingakart.android.login.Shipping;
import com.zingakart.android.registration.AddAddressActivity;
import com.zingakart.android.utility.ApiClient;
import com.zingakart.android.utility.ApiInterface;
import com.zingakart.android.utility.NetworkCallBack;
import com.zingakart.android.utility.NetworkResponse;
import com.velectico.rbm.network.callbacks.NetworkError;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import retrofit2.Call;

public class MyAccountActivity extends AppCompatActivity {
    TextView name;
    TextView email;
    TextView address;
    Shipping addr;
    String add;
    String add1;
    String add2;
    String add3;
    String cntry;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        name = (TextView) findViewById(R.id.fullname);
        address = (TextView) findViewById(R.id.addressdetail);
        email = (TextView) findViewById(R.id.email);
        ImageView iv = (ImageView)findViewById(R.id.imageView2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences sh
                = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);

        String fname = sh.getString("firstName", "");
        String lname = sh.getString("lastName", "");
        String emailTxt = sh.getString("email", "");
        String image = sh.getString("image", "");
        name.setText(fname + "" + lname);
        email.setText(emailTxt);
        Uri uri = Uri.parse(image);
        iv.setImageURI(uri);

        callProfileApiList();

        ImageView edit = (ImageView)findViewById(R.id.imageedit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyAccountActivity.this, AddAddressActivity.class);
                intent.putExtra("add", add);
                intent.putExtra("add1", add1);
                intent.putExtra("add2", add2);
                intent.putExtra("add3", add3);
                intent.putExtra("cntry", cntry);
                intent.putExtra("action", "add_address");
                startActivity(intent);
            }
        });
    }

    KProgressHUD hud  = null;
    void   showHud(){
        hud =  KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();
    }

    void hide(){
        hud.dismiss();
    }
    public void callProfileApiList(){

        showHud();
        ApiInterface apiInterface = ApiClient.getInstance().getClient().create(ApiInterface.class);
        Call<CustomerDetailResponse> responseCall = apiInterface.getCustomerProfile();
        responseCall.enqueue(callBack);

    }



    private NetworkCallBack callBack = new NetworkCallBack<CustomerDetailResponse>() {
        @Override
        public void onSuccessNetwork(@Nullable Object data, @NotNull NetworkResponse response) {
            hide();
            Log.d("ZINGAKART",response.toString());
            CustomerDetailResponse userDetail = (CustomerDetailResponse) response.getData();
            addr = userDetail.getShipping();
             add = addr.getAddress_1();
             add1 = addr.getCity();
             add2 = addr.getState();
             add3 = addr.getPostcode();
             cntry = addr.getCountry();
            address.setText(add+", "+ add1+", "+add2+" - "+add3);
        }

        @Override
        public void onFailureNetwork(@Nullable Object data, @NotNull NetworkError error) {
            hide();
        }
    };
}