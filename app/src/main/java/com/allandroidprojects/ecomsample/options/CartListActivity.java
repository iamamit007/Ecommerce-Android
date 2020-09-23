package com.allandroidprojects.ecomsample.options;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.allandroidprojects.ecomsample.R;
import com.allandroidprojects.ecomsample.product.ItemDetailsActivity;
import com.allandroidprojects.ecomsample.startup.MainActivity;
import com.allandroidprojects.ecomsample.utility.ApiClient;
import com.allandroidprojects.ecomsample.utility.ApiInterface;
import com.allandroidprojects.ecomsample.utility.ImageUrlUtils;
import com.allandroidprojects.ecomsample.utility.NetworkCallBack;
import com.allandroidprojects.ecomsample.utility.NetworkResponse;
import com.allandroidprojects.ecomsample.utility.Order;
import com.allandroidprojects.ecomsample.utility.Product;
import com.facebook.drawee.view.SimpleDraweeView;
import com.velectico.rbm.network.callbacks.NetworkError;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

import static com.allandroidprojects.ecomsample.fragments.ImageListFragment.STRING_IMAGE_POSITION;
import static com.allandroidprojects.ecomsample.fragments.ImageListFragment.STRING_IMAGE_URI;
import static com.allandroidprojects.ecomsample.utility.ConstantAPIKt.product;

public class CartListActivity extends AppCompatActivity {
    private static Context mContext;

    public static String screenName;

    public static void setScreenName(String screenName) {
        CartListActivity.screenName = screenName;
    }
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_list);
        mContext = CartListActivity.this;

        ImageUrlUtils imageUrlUtils = new ImageUrlUtils();
        ArrayList<String> cartlistImageUri =imageUrlUtils.getCartListImageUri();
        //Show cart layout based on items
        setCartLayout();

         recyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        RecyclerView.LayoutManager recylerViewLayoutManager = new LinearLayoutManager(mContext);

        recyclerView.setLayoutManager(recylerViewLayoutManager);

        callApiList();
    }


    public void setRecyclerView(List<Order> orderList){
        CartListActivity.SimpleStringRecyclerViewAdapter  adapter= new CartListActivity.SimpleStringRecyclerViewAdapter(recyclerView, orderList);
        recyclerView.setAdapter(adapter);
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    public void callApiList(){

        ApiInterface apiInterface = ApiClient.getInstance().getClient().create(ApiInterface.class);
        Call<List<Order>> responseCall = apiInterface.getMyOrder(14);
        responseCall.enqueue(callBack);

    }

    List<String> titles= new ArrayList<>();
    List<Product> responseData = new ArrayList<>();
    List<Order> orderData = new ArrayList<>();

    private NetworkCallBack callBack = new NetworkCallBack<List<Order>>() {
        @Override
        public void onSuccessNetwork(@Nullable Object data, @NotNull NetworkResponse response) {
            Log.d("Mita",response.getData().toString());
           orderData.addAll ((List<Order>)response.getData());
            setRecyclerView(orderData);
            for (Order o:orderData
                 ) {
                callOrderItems(o.getLine_items().get(0).getProduct_id());

            }

        }

        @Override
        public void onFailureNetwork(@Nullable Object data, @NotNull NetworkError error) {

        }
    };


    public void callOrderItems(int producId){

        ApiInterface apiInterface = ApiClient.getInstance().getClient().create(ApiInterface.class);
        Call<Product> responseCall = apiInterface.getProductDetails(product+"/"+producId);
        responseCall.enqueue(callBack2);

    }



    private NetworkCallBack callBack2 = new NetworkCallBack<Product>() {
        @Override
        public void onSuccessNetwork(@Nullable Object data, @NotNull NetworkResponse response) {
            responseData.add((Product)response.getData());
            CartListActivity.SimpleStringRecyclerViewAdapter  adapter =  (CartListActivity.SimpleStringRecyclerViewAdapter) recyclerView.getAdapter() ;
            if (adapter !=null){
                adapter.setProductList(responseData);
                adapter.notifyDataSetChanged();
            }

        }

        @Override
        public void onFailureNetwork(@Nullable Object data, @NotNull NetworkError error) {

        }
    };

    public static class SimpleStringRecyclerViewAdapter
            extends RecyclerView.Adapter<CartListActivity.SimpleStringRecyclerViewAdapter.ViewHolder> {

       // private ArrayList<String> mCartlistImageUri;
        private RecyclerView mRecyclerView;

        public List<Product> productList = new ArrayList<>();
        public List<Order> orderList = new ArrayList<>();

        public void setProductList(List<Product> productList) {
            this.productList = productList;
        }

        public Product getProductById(int Prodid){
            Product res = null;
            for (Product p:
                    productList
                 ) {
                if (p.getId() == Prodid){
                    res =  p;
                }

            }
            return res;
        }


        public static class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final SimpleDraweeView mImageView;
            public final LinearLayout mLayoutItem, mLayoutRemove , mLayoutEdit;
            public final TextView order_stat, address;
            public final TextView   name,quantity,price;



            public ViewHolder(View view) {
                super(view);
                mView = view;
                mImageView = (SimpleDraweeView) view.findViewById(R.id.image_cartlist);
                mLayoutItem = (LinearLayout) view.findViewById(R.id.layout_item_desc);
                mLayoutRemove = (LinearLayout) view.findViewById(R.id.layout_action1);
                mLayoutEdit = (LinearLayout) view.findViewById(R.id.layout_action2);
                order_stat = (TextView) view.findViewById(R.id.order_stat);
                address = (TextView) view.findViewById(R.id.address);
                name = (TextView) view.findViewById(R.id.name);
                quantity = (TextView) view.findViewById(R.id.quantity);
                price = (TextView) view.findViewById(R.id.price);
            }
        }

        public SimpleStringRecyclerViewAdapter(RecyclerView recyclerView, List<Order> orderList) {
            this.orderList = orderList;
            mRecyclerView = recyclerView;
        }

        @Override
        public CartListActivity.SimpleStringRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_cartlist_item, parent, false);
            return new CartListActivity.SimpleStringRecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onViewRecycled(CartListActivity.SimpleStringRecyclerViewAdapter.ViewHolder holder) {
            if (holder.mImageView.getController() != null) {
                holder.mImageView.getController().onDetach();
            }
            if (holder.mImageView.getTopLevelDrawable() != null) {
                holder.mImageView.getTopLevelDrawable().setCallback(null);
//                ((BitmapDrawable) holder.mImageView.getTopLevelDrawable()).getBitmap().recycle();
            }
        }

        @Override
        public void onBindViewHolder(final CartListActivity.SimpleStringRecyclerViewAdapter.ViewHolder holder, final int position) {
            final Uri uri = Uri.parse("https://static.pexels.com/photos/7093/coffee-desk-notes-workspace-medium.jpg");
            holder.mImageView.setImageURI(uri);
            Order order = orderList.get(position);
            Product p = getProductById(order.getLine_items().get(0).getProduct_id());
            try {
                if (p !=null){
                    holder.name.setText(p.getName());
                    holder.price.setText(p.getName());

                }
                holder.quantity.setText((order.getLine_items().get(0).getQuantity()));
                holder.order_stat.setText(order.getStatus());
                holder.order_stat.setText(order.getShipping().getAddress_1());
            } catch (Exception e) {
                e.printStackTrace();
            }
            holder.mLayoutItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent = new Intent(mContext, ItemDetailsActivity.class);
//                    intent.putExtra(STRING_IMAGE_URI,mCartlistImageUri.get(position));
//                    intent.putExtra(STRING_IMAGE_POSITION, position);
//                    mContext.startActivity(intent);
                }
            });

           //Set click action
            holder.mLayoutRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ImageUrlUtils imageUrlUtils = new ImageUrlUtils();
                    imageUrlUtils.removeCartListImageUri(position);
                    notifyDataSetChanged();
                    //Decrease notification count
                    MainActivity.notificationCountCart--;

                }
            });

            //Set click action
            holder.mLayoutEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            });
        }

        @Override
        public int getItemCount() {
            return orderList.size();
        }
    }

    protected void setCartLayout(){
        LinearLayout layoutCartItems = (LinearLayout) findViewById(R.id.layout_items);
        LinearLayout layoutCartPayments = (LinearLayout) findViewById(R.id.layout_payment);
        LinearLayout layoutCartNoItems = (LinearLayout) findViewById(R.id.layout_cart_empty);

//        if(MainActivity.notificationCountCart >0){
//            layoutCartNoItems.setVisibility(View.GONE);
//            layoutCartItems.setVisibility(View.VISIBLE);
//            layoutCartPayments.setVisibility(View.VISIBLE);
//        }else {
//            layoutCartNoItems.setVisibility(View.VISIBLE);
//            layoutCartItems.setVisibility(View.GONE);
//            layoutCartPayments.setVisibility(View.GONE);
//
//            Button bStartShopping = (Button) findViewById(R.id.bAddNew);
//            bStartShopping.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    finish();
//                }
//            });
//        }
    }
}
