package com.allandroidprojects.ecomsample.login

import com.google.gson.annotations.SerializedName
import java.io.Serializable



data class CustomerRegisterResponse(
        val email: String,
        val first_name:String,
        val last_name:String,
        val username:String


): Serializable





open class BaseModel {

}
