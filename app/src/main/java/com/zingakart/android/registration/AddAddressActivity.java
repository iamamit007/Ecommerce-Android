package com.zingakart.android.registration;

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

import com.zingakart.android.R;
import com.zingakart.android.login.CustomerAddressRequestParams;
import com.zingakart.android.login.CustomerAddressResponse;
import com.zingakart.android.login.CustomerDetailResponse;
import com.zingakart.android.login.Shipping;
import com.zingakart.android.startup.MainActivity;
import com.zingakart.android.utility.ApiClient;
import com.zingakart.android.utility.ApiInterface;
import com.zingakart.android.utility.CreateOrderRequest;
import com.zingakart.android.utility.NetworkCallBack;
import com.zingakart.android.utility.NetworkResponse;
import com.zingakart.android.utility.Order;
import com.zingakart.android.utility.OrderLines;
import com.zingakart.android.utility.PaymentActivity;
import com.zingakart.android.utility.Product;
import com.zingakart.android.utility.UpdatePaymentOrderRequest;
import com.velectico.rbm.network.callbacks.NetworkError;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;

import static com.zingakart.android.utility.ConstantAPIKt.customer_order_retrieve;

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
            Order order = (Order)response.getData();
            if (isCod){
                updateOrder(order);
            }else {
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


    public void updateOrder(Order order){



        UpdatePaymentOrderRequest request = new UpdatePaymentOrderRequest(14,"COD",false,"");
        try {
            ApiInterface apiInterface = ApiClient.getInstance().getClient().create(ApiInterface.class);
            Call<Order> responseCall = apiInterface.updateOrder(customer_order_retrieve+"/"+order.getId() ,request);
            responseCall.enqueue(updatecallBack2);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private NetworkCallBack updatecallBack2 = new NetworkCallBack<Order>() {
        @Override
        public void onSuccessNetwork(@Nullable Object data, @NotNull NetworkResponse response) {
            Log.d("Order Places",response.getData().toString());
            Toast.makeText(AddAddressActivity.this, "Order Placed Successfully", Toast.LENGTH_SHORT).show();
            finish();

        }

        @Override
        public void onFailureNetwork(@Nullable Object data, @NotNull NetworkError error) {

        }
    };
}