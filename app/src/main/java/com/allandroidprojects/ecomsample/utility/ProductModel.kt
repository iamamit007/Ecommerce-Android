package com.allandroidprojects.ecomsample.utility

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.lang.reflect.Array


data class Catagories(
        val id: Int,
        val name:String,
        val slug: String?,
        val parent: Int?,
        val description: String?,
        val image: String?,
        val menu_order: Int?,
        val count: Int?
):Serializable

data class Images(
        val id: Int?,
        val src: String?
):Serializable

data class Product(
        val id: Int,
        val name:String,
        val slug: String?,
        val permalink: String?,
        val date_created: String?,
        val date_modified: String?,
        val type: String?,
        val status: String?,
        val catalog_visibility: String?,
        val featured: Boolean?,
        val parent: Int?,
        val description: String?,
        val short_description: String?,
        val price: String?,
        val regular_price: String?,
        val sku: String?,
        val image: String?,
        val menu_order: Int?,
        val count: Int?,
        val sale_price: String?,
        val date_on_sale_from: String?,
        val date_on_sale_to: String?,
        val price_html: String?,
        val on_sale: Boolean?,
        val purchasable: Boolean?,
        val total_sales: Int?,
        val virtual: Boolean?,
        val downloadable: Boolean?,
        val backorders_allowed: Boolean?,
        val backordered: Boolean?,
        val stock_status: String?,
        val reviews_allowed: Boolean?,
        val average_rating: String?,
        val images: List<Images>?
        //val attributes: List<attributes>?


//
//"stock_quantity": null,
//"stock_status": "instock",
//"backorders": "no",
//"backorders_allowed": false,
//"backordered": false,
//"sold_individually": true,
//"weight": "",
//"dimensions": {
//    "length": "",
//    "width": "",
//    "height": ""
//},
//"shipping_required": false,
//"shipping_taxable": false,
//"shipping_class": "",
//"shipping_class_id": 0,
//"reviews_allowed": false,
//"average_rating": "0.00",
//"rating_count": 0,
//"related_ids": [],
//"upsell_ids": [],
//"cross_sell_ids": [],
//"parent_id": 0,
//"purchase_note": "",
//"categories": [],
//"tags": [],
//"images": [],
//"attributes": [],
//"default_attributes": [],
//"variations": [],
//"grouped_products": [],
//"menu_order": 0,
//"meta_data": [],
//"_links": {
//    "self": [{
//        "href": "https://www.zingakart.com/wp-json/wc/v3/products/34785"
//    }],
//    "collection": [{
//        "href": "https://www.zingakart.com/wp-json/wc/v3/products"
//    }]
//}
):Serializable

data class attributes(
        val id: Int?,
        val name: String?,
        val position: String?,
        val visible: Boolean?,
        val variation: Boolean?,
        val options :Array?
):Serializable



data class Order(
        val id: Int?,
        val parent_id: Int?,
        val number: String?,
        val created_via: String?,
        val status: String?,
        val currency: String?,
        val date_created: String?,
        val total: String?,
        val shipping: shipping?,
        val line_items: List<LineItem>?

):Serializable

data class LineItem(
        val id: Int?,
        val name: String?,
        val product_id: Int?,
        val variation_id: Int?,
        val quantity: Int?,
        val tax_class: String?,
        val total: String?,
        val subtotal: Int?,
        val subtotal_tax: Int?
):Serializable

data class shipping(
        val first_name: String?,
        val last_name: String?,
        val company: String?,
        val address_1: String?,
        val address_2: String?,
        val city: String?,
        val state: String?,
        val postcode: String?,
        val country: String?


):Serializable
