package com.allandroidprojects.ecomsample.options;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.allandroidprojects.ecomsample.R;
import com.allandroidprojects.ecomsample.product.ItemDetailsActivity;
import com.allandroidprojects.ecomsample.utility.ApiClient;
import com.allandroidprojects.ecomsample.utility.ApiInterface;
import com.allandroidprojects.ecomsample.utility.ImageUrlUtils;
import com.allandroidprojects.ecomsample.utility.NetworkCallBack;
import com.allandroidprojects.ecomsample.utility.NetworkResponse;
import com.allandroidprojects.ecomsample.utility.Order;
import com.allandroidprojects.ecomsample.utility.WishList;
import com.facebook.drawee.view.SimpleDraweeView;
import com.velectico.rbm.network.callbacks.NetworkError;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

import static com.allandroidprojects.ecomsample.fragments.ImageListFragment.STRING_IMAGE_POSITION;
import static com.allandroidprojects.ecomsample.fragments.ImageListFragment.STRING_IMAGE_URI;
import static com.allandroidprojects.ecomsample.utility.ConstantAPIKt.customer_wishList_retrieveById;

public class WishlistActivity extends AppCompatActivity {
    private static Context mContext;
    RecyclerView recyclerView;
    List<WishList> wishListData = new ArrayList<>();
    String usrId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_recylerview_list);
        mContext = WishlistActivity.this;
        usrId = getIntent().getStringExtra("id");

        ImageUrlUtils imageUrlUtils = new ImageUrlUtils();
         recyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        callApiList();

    }

    public void callApiList(){

        ApiInterface apiInterface = ApiClient.getInstance().getClient().create(ApiInterface.class);
        Call<List<WishList>>responseCall = apiInterface.getMyWishlist(customer_wishList_retrieveById+"/"+usrId);
        responseCall.enqueue(callBack);

    }

    private NetworkCallBack callBack = new NetworkCallBack<List<WishList>>() {
        @Override
        public void onSuccessNetwork(@Nullable Object data, @NotNull NetworkResponse response) {
            Log.d("ytuytuytu",response.getData().toString());
            Toast.makeText(getApplicationContext(),
                    "Wishlist now"+response.getData().toString(),
                    Toast.LENGTH_SHORT).show();
            wishListData.addAll ((List<WishList>)response.getData());
            RecyclerView.LayoutManager recylerViewLayoutManager = new LinearLayoutManager(mContext);
            recyclerView.setLayoutManager(recylerViewLayoutManager);
            recyclerView.setAdapter(new SimpleStringRecyclerViewAdapter(recyclerView, wishListData));

        }

        @Override
        public void onFailureNetwork(@Nullable Object data, @NotNull NetworkError error) {

        }
    };

    public static class SimpleStringRecyclerViewAdapter
            extends RecyclerView.Adapter<WishlistActivity.SimpleStringRecyclerViewAdapter.ViewHolder> {

        private List<WishList> mWishlistImageUri;
        private RecyclerView mRecyclerView;

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final SimpleDraweeView mImageView;
            public final LinearLayout mLayoutItem;
            public final ImageView mImageViewWishlist;
            public final TextView wishTitle;
            public final TextView wishKey;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mImageView = (SimpleDraweeView) view.findViewById(R.id.image_wishlist);
                mLayoutItem = (LinearLayout) view.findViewById(R.id.layout_item_desc);
                mImageViewWishlist = (ImageView) view.findViewById(R.id.ic_wishlist);
                wishTitle = (TextView) view.findViewById(R.id.wishtitle);
                wishKey = (TextView) view.findViewById(R.id.wishKey);

            }
        }

        public SimpleStringRecyclerViewAdapter(RecyclerView recyclerView, List<WishList> wishlistImageUri) {
            mWishlistImageUri = wishlistImageUri;
            mRecyclerView = recyclerView;
        }

        @Override
        public WishlistActivity.SimpleStringRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_wishlist_item, parent, false);
            return new WishlistActivity.SimpleStringRecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onViewRecycled(ViewHolder holder) {
            if (holder.mImageView.getController() != null) {
                holder.mImageView.getController().onDetach();
            }
            if (holder.mImageView.getTopLevelDrawable() != null) {
                holder.mImageView.getTopLevelDrawable().setCallback(null);
//                ((BitmapDrawable) holder.mImageView.getTopLevelDrawable()).getBitmap().recycle();
            }
        }

        @Override
        public void onBindViewHolder(final WishlistActivity.SimpleStringRecyclerViewAdapter.ViewHolder holder, final int position) {

            if (position == 0){
                holder.wishTitle.setText("Shopping List"+mWishlistImageUri.get(position).getShare_key());
            }
            else {
                holder.wishTitle.setText("Shopping List " + position + mWishlistImageUri.get(position).getShare_key());
            }
            holder.wishKey.setText(mWishlistImageUri.get(position).getTitle());
            holder.mLayoutItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, MyWishListActivity.class);
                    intent.putExtra("shareKey",mWishlistImageUri.get(position).getShare_key());
                    //intent.putExtra(STRING_IMAGE_POSITION, position);
                    mContext.startActivity(intent);
                }
            });
//
//            //Set click action for wishlist
//            holder.mImageViewWishlist.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    ImageUrlUtils imageUrlUtils = new ImageUrlUtils();
//                    imageUrlUtils.removeWishlistImageUri(position);
//                    notifyDataSetChanged();
//                }
//            });
        }

        @Override
        public int getItemCount() {
            return mWishlistImageUri.size() ;
        }
    }
}
