package com.allandroidprojects.ecomsample.product;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.allandroidprojects.ecomsample.BuildConfig;
import com.allandroidprojects.ecomsample.R;
import com.allandroidprojects.ecomsample.fragments.ImageListFragment;
import com.allandroidprojects.ecomsample.fragments.ViewPagerActivity;
import com.allandroidprojects.ecomsample.login.LoginPopup;
import com.allandroidprojects.ecomsample.notification.NotificationCountSetClass;
import com.allandroidprojects.ecomsample.options.CartListActivity;
import com.allandroidprojects.ecomsample.registration.AddAddressActivity;
import com.allandroidprojects.ecomsample.startup.MainActivity;
import com.allandroidprojects.ecomsample.utility.ImageUrlUtils;
import com.allandroidprojects.ecomsample.utility.Images;
import com.allandroidprojects.ecomsample.utility.Product;
import com.allandroidprojects.ecomsample.utility.attributes;
import com.facebook.drawee.view.SimpleDraweeView;

import java.lang.reflect.Array;
import java.util.List;

public class ItemDetailsActivity extends AppCompatActivity {
    int imagePosition;
    String stringImageUri;
    public static Product product;
    String userId = "";
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
        List<Images> imgArr = product.getImages();
        String prodImage = imgArr.get(0).getSrc();
//        Array op = attrArr.get(1).getOptions();
//        Toast.makeText(ItemDetailsActivity.this,"Size"+op,Toast.LENGTH_SHORT).show();


        prodName.setText(product.getName());
        prodPrice.setText(Html.fromHtml(product.getPrice_html(), Html.FROM_HTML_MODE_COMPACT));
        descTxt.setText(Html.fromHtml(product.getShort_description(), Html.FROM_HTML_MODE_COMPACT));

        //Getting image uri from previous screen
        if (getIntent() != null) {
            stringImageUri = getIntent().getStringExtra(ImageListFragment.STRING_IMAGE_URI);
            imagePosition = getIntent().getIntExtra(ImageListFragment.STRING_IMAGE_URI,0);
        }
        String x = product.getAverage_rating();
        Uri uri = Uri.parse(prodImage);
        mImageView.setImageURI(uri);

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
                    intent.putExtra("fromScreen", "frompoductDetail");
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
    }
}
