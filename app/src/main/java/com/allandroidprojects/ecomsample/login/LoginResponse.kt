package com.allandroidprojects.ecomsample.login

import com.google.gson.annotations.SerializedName
import java.io.Serializable



data class CustomerLoginResponse(
        val id : String?,
        val email: String?,
        val first_name:String?,
        val last_name:String?,
        val username:String?


): Serializable
data class  CustomerRegisterRequestParams(
        val email: String?,
        val first_name:String?,
        val last_name:String?,
        val username:String?,
        val password:String?

): BaseModel()
data class CustomerRegisterResponse(
        val id : String?,
        val email: String?,
        val first_name:String?,
        val last_name:String?,
        val username:String?,
        val avatar_url:String?


): Serializable

data class CustomerAddressResponse(
        val id : String?,
        val email: String?,
        val first_name:String?,
        val last_name:String?,
        val username:String?


): Serializable
data class  CustomerAddressRequestParams(
        val shipping: List<Shipping>

): BaseModel()
data class Shipping(
        var first_name: String? = null,
var last_name: String? = null,
var company: String? = null,
var address_1: String? = null,
var address_2: String? = null,
var city: String? = null,
var postcode: String? = null,
var country:String? = null,
var state: String? = null

):Serializable

data class Billing(
        var first_name: String? = null,
        var last_name: String? = null,
        var company: String? = null,
        var address_1: String? = null,
        var address_2: String? = null,
        var city: String? = null,
        var postcode: String? = null,
        var country:String? = null,
        var state: String? = null,
        var phone: String? = null

):Serializable

data class CustomerDetailResponse(
        val id : String?,
        val email: String?,
        val first_name:String?,
        val last_name:String?,
        val username:String?,
        val avatar_url:String?,
        val shipping: List<Shipping>,
        val billing: List<Billing>

): Serializable



open class BaseModel {

}
