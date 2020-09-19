package com.allandroidprojects.ecomsample.utility

import com.google.gson.annotations.SerializedName
import java.io.Serializable



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

