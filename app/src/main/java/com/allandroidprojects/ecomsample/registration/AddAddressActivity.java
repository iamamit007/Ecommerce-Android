package com.allandroidprojects.ecomsample.registration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.allandroidprojects.ecomsample.R;
import com.allandroidprojects.ecomsample.login.CustomerAddressRequestParams;
import com.allandroidprojects.ecomsample.login.CustomerAddressResponse;
import com.allandroidprojects.ecomsample.login.CustomerDetailResponse;
import com.allandroidprojects.ecomsample.login.Shipping;
import com.allandroidprojects.ecomsample.options.MyAccountActivity;
import com.allandroidprojects.ecomsample.startup.MainActivity;
import com.allandroidprojects.ecomsample.utility.ApiClient;
import com.allandroidprojects.ecomsample.utility.ApiInterface;
import com.allandroidprojects.ecomsample.utility.CreateOrderRequest;
import com.allandroidprojects.ecomsample.utility.LineItem;
import com.allandroidprojects.ecomsample.utility.NetworkCallBack;
import com.allandroidprojects.ecomsample.utility.NetworkResponse;
import com.allandroidprojects.ecomsample.utility.Order;
import com.allandroidprojects.ecomsample.utility.OrderLines;
import com.allandroidprojects.ecomsample.utility.PaymentActivity;
import com.allandroidprojects.ecomsample.utility.Product;
import com.velectico.rbm.network.callbacks.NetworkError;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;

public class AddAddressActivity extends AppCompatActivity {

    EditText pintx;
    EditText address;
    EditText citytx;
    EditText statetx;
    EditText country;
    EditText confirmPassword,landmrk;
    EditText first_name,lastg_name;
    TextView title;
    LinearLayout paybox;
    Button cod,checkout;
    Boolean isCod = true;

    public static Product product;

    public static void setProduct(Product product) {
        AddAddressActivity.product = product;
    }

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
         title = (TextView) findViewById(R.id.title);
         first_name = (EditText) findViewById(R.id.first_name);
         lastg_name = (EditText) findViewById(R.id.last_name);
        landmrk = (EditText) findViewById(R.id.landmrk);
        cod = (Button) findViewById(R.id.cod);
        checkout = (Button) findViewById(R.id.checkout);
        paybox = (LinearLayout) findViewById(R.id.paybox);
        callProfileApiList();
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AddAddressActivity.this,"Sign UP.",Toast.LENGTH_SHORT).show();
                if (getIntent().hasExtra("action")){
                    if (getIntent().getStringExtra("action").equalsIgnoreCase("billing")){
                        callCreateOrder();
                    }else {
                        try {

                            callAddAddressApi();
                        }catch (Exception e){
                            Log.d("FFF",e.getLocalizedMessage());
                        }

                    }
                }


            }
        });
        cod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    isCod = true;
                    callCreateOrder();
                }catch (Exception e){
                    Log.d("FFF",e.getLocalizedMessage());
                }


            }
        });
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    isCod = false;
                    callCreateOrder();
                }catch (Exception e){
                    Log.d("FFF",e.getLocalizedMessage());
                }


            }
        });
        String add = getIntent().getStringExtra("add");
        String pin = getIntent().getStringExtra("add3");
        String city = getIntent().getStringExtra("add1");
        String state = getIntent().getStringExtra("add2");
        String cntry = getIntent().getStringExtra("cntry");

        if (getIntent().hasExtra("action")){
            if (getIntent().getStringExtra("action").equalsIgnoreCase("billing")){
                title.setText("Add Billing Address");
                title.setText("Place order");
                save.setVisibility(View.GONE);
                paybox.setVisibility(View.VISIBLE);
             }else {
                first_name.setVisibility(View.GONE);
                lastg_name.setVisibility(View.GONE);
                title.setText("Add address");
                title.setText("save");
                save.setVisibility(View.VISIBLE);
                paybox.setVisibility(View.GONE);
            }
        }


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


    public void callCreateOrder(){

        Shipping shipping = new Shipping(
                first_name.getText().toString(),
                lastg_name.getText().toString(),
                "",
                address.getText().toString(),
                landmrk.getText().toString(),
                citytx.getText().toString(),
                statetx.getText().toString(),
                pintx.getText().toString(),
                country.getText().toString()
                );
        List<OrderLines> items =  new ArrayList<>();
        items.add(lineItem);
        CreateOrderRequest request = new CreateOrderRequest(14,"cash","bank",true,shipping,items

                );
        try {
            ApiInterface apiInterface = ApiClient.getInstance().getClient().create(ApiInterface.class);
            Call<Order> responseCall = apiInterface.createMyOrder(request);
            responseCall.enqueue(callBack2);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private NetworkCallBack callBack2 = new NetworkCallBack<Order>() {
        @Override
        public void onSuccessNetwork(@Nullable Object data, @NotNull NetworkResponse response) {
            Log.d("ZINGAKART Login",response.toString());
            if (isCod){
                Toast.makeText(AddAddressActivity.this,"Order has been placed scuccesfully",Toast.LENGTH_SHORT).show();
                finish();
            }else {Order order = (Order)response.getData();
                PaymentActivity.setOrder(order);
                Intent intent = new Intent(AddAddressActivity.this, PaymentActivity.class);
                intent.putExtra("action", "add_address");
                startActivity(intent);
            }

        }

        @Override
        public void onFailureNetwork(@Nullable Object data, @NotNull NetworkError error) {

        }
    };


    OrderLines lineItem = null;

    public void callProfileApiList(){

        ApiInterface apiInterface = ApiClient.getInstance().getClient().create(ApiInterface.class);
        Call<CustomerDetailResponse> responseCall = apiInterface.getCustomerProfile();
        responseCall.enqueue(myprofile);

    }



    private NetworkCallBack myprofile = new NetworkCallBack<CustomerDetailResponse>() {
        @Override
        public void onSuccessNetwork(@Nullable Object data, @NotNull NetworkResponse response) {
            Log.d("ZINGAKART",response.toString());
            CustomerDetailResponse customerDetailResponse = (CustomerDetailResponse)response.getData();
            try {
                lineItem = new OrderLines(

                       product.getId(),
                        1,
                        12
                );
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onFailureNetwork(@Nullable Object data, @NotNull NetworkError error) {

        }
    };
}