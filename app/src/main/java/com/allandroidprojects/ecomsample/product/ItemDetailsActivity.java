package com.allandroidprojects.ecomsample.product;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.allandroidprojects.ecomsample.BuildConfig;
import com.allandroidprojects.ecomsample.R;
import com.allandroidprojects.ecomsample.fragments.ImageListFragment;
import com.allandroidprojects.ecomsample.fragments.ViewPagerActivity;
import com.allandroidprojects.ecomsample.login.CustomerRegisterRequestParams;
import com.allandroidprojects.ecomsample.login.LoginPopup;
import com.allandroidprojects.ecomsample.notification.NotificationCountSetClass;
import com.allandroidprojects.ecomsample.options.CartListActivity;
import com.allandroidprojects.ecomsample.options.WishlistActivity;
import com.allandroidprojects.ecomsample.registration.AddAddressActivity;
import com.allandroidprojects.ecomsample.startup.MainActivity;
import com.allandroidprojects.ecomsample.utility.ApiClient;
import com.allandroidprojects.ecomsample.utility.ApiInterface;
import com.allandroidprojects.ecomsample.utility.ImageUrlUtils;
import com.allandroidprojects.ecomsample.utility.Images;
import com.allandroidprojects.ecomsample.utility.NetworkCallBack;
import com.allandroidprojects.ecomsample.utility.NetworkResponse;
import com.allandroidprojects.ecomsample.utility.Product;
import com.allandroidprojects.ecomsample.utility.WishList;
import com.allandroidprojects.ecomsample.utility.attributes;
import com.allandroidprojects.ecomsample.utility.createWishlistRequestParams;
import com.allandroidprojects.ecomsample.utility.createWishlistResponse;
import com.facebook.drawee.view.SimpleDraweeView;
import com.velectico.rbm.network.callbacks.NetworkError;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

import static com.allandroidprojects.ecomsample.utility.ConstantAPIKt.customer_wishList_retrieveById;
import static com.allandroidprojects.ecomsample.utility.ConstantAPIKt.getwishListProductByKey;

public class ItemDetailsActivity extends AppCompatActivity {
    int imagePosition;
    String stringImageUri;
    public static Product product;
    String userId = "";
    String wishsharekey = "";
    List<WishList> wishListData = new ArrayList<>();
    public static void setProduct(Product product) {
        ItemDetailsActivity.product = product;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_item_details);

        SharedPreferences sh
                = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        userId = sh.getString("id", "true");
        SimpleDraweeView mImageView = (SimpleDraweeView)findViewById(R.id.image1);
        TextView textViewAddToCart = (TextView)findViewById(R.id.text_action_bottom1);
        TextView textViewBuyNow = (TextView)findViewById(R.id.text_action_bottom2);
        TextView prodName = (TextView)findViewById(R.id.prodName);
        TextView prodPrice = (TextView)findViewById(R.id.prodPrice);
        TextView descTxt = (TextView)findViewById(R.id.descTxt);
        Spinner sizeSpinner = (Spinner)findViewById(R.id.sizeList);
        if (product!=null && product.getImages()!=null){
            List<Images> imgArr = product.getImages();
            final String prodImage = imgArr.get(0).getSrc();
            prodName.setText(product.getName());
            Uri uri = Uri.parse(prodImage);
            mImageView.setImageURI(uri);
            prodPrice.setText(Html.fromHtml(product.getPrice_html(), Html.FROM_HTML_MODE_COMPACT));
            descTxt.setText(Html.fromHtml(product.getShort_description(), Html.FROM_HTML_MODE_COMPACT));

            //Getting image uri from previous screen
            if (getIntent() != null) {
                stringImageUri = getIntent().getStringExtra(ImageListFragment.STRING_IMAGE_URI);
                imagePosition = getIntent().getIntExtra(ImageListFragment.STRING_IMAGE_URI,0);
            }
            String x = product.getAverage_rating();
        }
        callApiList();

//        Array op = attrArr.get(1).getOptions();
//        Toast.makeText(ItemDetailsActivity.this,"Size"+op,Toast.LENGTH_SHORT).show();






        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewPagerActivity.setProduct1(product);
                    Intent intent = new Intent(ItemDetailsActivity.this, ViewPagerActivity.class);
                    intent.putExtra("position", imagePosition);
                    startActivity(intent);

            }
        });

        textViewAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageUrlUtils imageUrlUtils = new ImageUrlUtils();
                imageUrlUtils.addCartListImageUri(stringImageUri);
                Toast.makeText(ItemDetailsActivity.this,"Item added to cart.",Toast.LENGTH_SHORT).show();
                MainActivity.notificationCountCart++;
                NotificationCountSetClass.setNotifyCount(MainActivity.notificationCountCart);
            }
        });

        textViewBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userId == "true") {
                    LoginPopup popUpClass = new LoginPopup();
                    popUpClass.showPopupWindow(view);
                }
                else{
                    Intent intent = new Intent(ItemDetailsActivity.this, AddAddressActivity.class);
                    AddAddressActivity.setProduct(product);
                    intent.putExtra("fromScreen", "frompoductDetail");
                    intent.putExtra("action", "billing");
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(),
                            "Buy now",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        TextView share = (TextView)findViewById(R.id.text_action1);
        TextView wishlist = (TextView)findViewById(R.id.text_action3);
        share.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        product.getPermalink());
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });
        wishlist.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
               callCreateWishApiList();
            }
        });

    }
    public void callCreateWishApiList(){
        createWishlistRequestParams param = new createWishlistRequestParams(product.getId(),userId);
        ApiInterface apiInterface = ApiClient.getInstance().getClient().create(ApiInterface.class);
        Call<List<createWishlistResponse>> responseCall = apiInterface.createWishList(getwishListProductByKey+"/"+wishsharekey+"/add_products",param);
        responseCall.enqueue(callBack);

    }

    private NetworkCallBack callBack = new NetworkCallBack<List<createWishlistResponse>>() {
        @Override
        public void onSuccessNetwork(@Nullable Object data, @NotNull NetworkResponse response) {
            Log.d("ytuytuytu",response.getData().toString());
            Toast.makeText(getApplicationContext(),
                    "Wishlist now"+response.getData().toString(),
                    Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onFailureNetwork(@Nullable Object data, @NotNull NetworkError error) {

        }
    };

    public void callApiList(){

        ApiInterface apiInterface = ApiClient.getInstance().getClient().create(ApiInterface.class);
        Call<List<WishList>>responseCall = apiInterface.getMyWishlist(customer_wishList_retrieveById+"/"+userId);
        responseCall.enqueue(callBack2);

    }

    private NetworkCallBack callBack2 = new NetworkCallBack<List<WishList>>() {
        @Override
        public void onSuccessNetwork(@Nullable Object data, @NotNull NetworkResponse response) {
            Log.d("ytuytuytu",response.getData().toString());
            wishListData.addAll ((List<WishList>)response.getData());
            wishsharekey = wishListData.get(0).getShare_key();
        }

        @Override
        public void onFailureNetwork(@Nullable Object data, @NotNull NetworkError error) {

        }
    };
}
