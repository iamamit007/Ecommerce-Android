package com.allandroidprojects.ecomsample.options;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.allandroidprojects.ecomsample.R;
import com.allandroidprojects.ecomsample.fragments.ViewPagerActivity;
import com.allandroidprojects.ecomsample.utility.Order;
import com.allandroidprojects.ecomsample.utility.Product;

public class OrderDetailActivity extends AppCompatActivity {
    public static Order order;
    public static void setOrder(Order order) {
        OrderDetailActivity.order = order;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        TextView orderId = (TextView)findViewById(R.id.orderId);
        TextView orderName = (TextView)findViewById(R.id.name);
        TextView orderPrice = (TextView)findViewById(R.id.price);
        TextView orderQty = (TextView)findViewById(R.id.quantity);
        TextView orderStat = (TextView)findViewById(R.id.order_stat);
        TextView ordershipName = (TextView)findViewById(R.id.shipname);
        TextView orderadd1 = (TextView)findViewById(R.id.shipadd);
        TextView orderadd2 = (TextView)findViewById(R.id.shipadd1);
        TextView orderadd3 = (TextView)findViewById(R.id.shipadd2);
        TextView orderadd4 = (TextView)findViewById(R.id.shipadd3);
        TextView mobile = (TextView)findViewById(R.id.mobileTxt);
        TextView subtotalPrice = (TextView)findViewById(R.id.subt);
        TextView shipPrice = (TextView)findViewById(R.id.shipPrice);
        TextView totalPrice = (TextView)findViewById(R.id.totalAmt);
        orderId.setText("OrderID - #"+order.getNumber());
        orderName.setText(order.getLine_items().get(0).getName());
        orderPrice.setText("₹ "+order.getTotal());
        orderQty.setText(("Qty: "+order.getLine_items().get(0).getQuantity()));
        orderStat.setText(order.getStatus());
        ordershipName.setText(order.getShipping().getFirst_name()+" "+order.getShipping().getLast_name());
        subtotalPrice.setText("₹"+order.getLine_items().get(0).getTotal());
        totalPrice.setText("₹"+order.getTotal());
        double ship2 = Double.parseDouble(order.getTotal())-Double.parseDouble(order.getLine_items().get(0).getTotal());
        orderadd1.setText(order.getShipping().getCompany());
        orderadd2.setText(order.getShipping().getAddress_1());
        orderadd3.setText(order.getShipping().getAddress_2() +""+order.getShipping().getCity()
                +", "+order.getShipping().getState()+" "+order.getShipping().getPostcode());
        orderadd4.setText(order.getShipping().getCountry());
        shipPrice.setText("+ ₹"+ship2+"0");

    }
}