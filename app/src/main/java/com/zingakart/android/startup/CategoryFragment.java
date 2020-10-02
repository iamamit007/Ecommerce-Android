package com.zingakart.android.startup;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zingakart.android.R;
import com.zingakart.android.fragments.ImageListFragment;
import com.zingakart.android.utility.ApiClient;
import com.zingakart.android.utility.ApiInterface;
import com.zingakart.android.utility.Catagories;
import com.zingakart.android.utility.NetworkCallBack;
import com.zingakart.android.utility.NetworkResponse;
import com.facebook.drawee.view.SimpleDraweeView;
import com.velectico.rbm.network.callbacks.NetworkError;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;


public class CategoryFragment extends Fragment {

    public static final String STRING_IMAGE_URI = "ImageUri";
    public static final String STRING_IMAGE_POSITION = "ImagePosition";
    private static MainActivity mActivity;
    private static int cataGoryId = 0;

    RecyclerView rv;

    public static void setCataGoryId(int cataGoryId) {
        CategoryFragment.cataGoryId = cataGoryId;
    }
    // TODO: Rename and change types and number of parameters


    @Override
    public void onResume() {
        super.onResume();
        callApiList();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rv  = (RecyclerView) inflater.inflate(R.layout.layout_recylerview_list, container, false);
        return rv;
    }
    public void callApiList(){
        ApiInterface apiInterface = ApiClient.getInstance().getClient().create(ApiInterface.class);
        Call<List<Catagories>> responseCall = apiInterface.getCatagories(30,true,cataGoryId);
        responseCall.enqueue(callBack2);
    }

    List<Catagories> wishListData = new ArrayList<>();
    private NetworkCallBack callBack2 = new NetworkCallBack<List<Catagories>>() {
        @Override
        public void onSuccessNetwork(@Nullable Object data, @NotNull NetworkResponse response) {
            List<Catagories>  child = (List<Catagories>) response.getData();
            Log.d("yyyyyyyyyyyyyyyyyyy",child.toString());
            wishListData.addAll ((List<Catagories>)response.getData());
            StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            rv.setLayoutManager(layoutManager);
            rv.setAdapter(new SimpleStringRecyclerViewAdapter(rv, wishListData));

        }

        @Override
        public void onFailureNetwork(@Nullable Object data, @NotNull NetworkError error) {

        }
    };

    public static class SimpleStringRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleStringRecyclerViewAdapter.ViewHolder> {

        private List<Catagories> mWishlistImageUri;
        private RecyclerView mRecyclerView;

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final SimpleDraweeView mImageView;
            public TextView product_name;
            public  TextView product_des;
            public  TextView product_price;
            public final LinearLayout mLayoutItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mImageView = (SimpleDraweeView) view.findViewById(R.id.image1);
                product_name = (TextView) view.findViewById(R.id.product_name);
                product_des = (TextView) view.findViewById(R.id.product_des);
                product_price = (TextView) view.findViewById(R.id.product_price);
                mLayoutItem = (LinearLayout) view.findViewById(R.id.layout_item);

            }
        }

        public SimpleStringRecyclerViewAdapter(RecyclerView recyclerView, List<Catagories> wishlistImageUri) {
            mWishlistImageUri = wishlistImageUri;
            mRecyclerView = recyclerView;
        }

        @Override
        public SimpleStringRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
            return new SimpleStringRecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onViewRecycled(SimpleStringRecyclerViewAdapter.ViewHolder holder) {
            if (holder.mImageView.getController() != null) {
                holder.mImageView.getController().onDetach();
            }
            if (holder.mImageView.getTopLevelDrawable() != null) {
                holder.mImageView.getTopLevelDrawable().setCallback(null);
//                ((BitmapDrawable) holder.mImageView.getTopLevelDrawable()).getBitmap().recycle();
            }
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            final Catagories product = mWishlistImageUri.get(position);
            holder.product_name.setText(product.getName().replace("&amp;", "&"));
            holder.product_des.setVisibility(View.GONE);
            holder.product_price.setVisibility(View.GONE);
            if (product.getImage()!= null) {

                final Uri uri = Uri.parse(product.getImage().getSrc());
                holder.mImageView.setImageURI(uri);
            }
            else{
                holder.mImageView.setImageURI(Uri.parse("https://cdn.fcglcdn.com/brainbees/images/products/438x531/3569032a.jpg"));
            }
            holder.mLayoutItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mActivity!=null){

                        ImageListFragment.setCataGoryId(product.getId());
                        mActivity.cnageFragment(new ImageListFragment());

                    }

                }
            });

        }

        @Override
        public int getItemCount() {
            return mWishlistImageUri.size() ;
        }
    }
}