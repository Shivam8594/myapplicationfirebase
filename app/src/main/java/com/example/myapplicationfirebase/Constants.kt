package com.example.myapplicationfirebase

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap

object Constants {
    const val USERS = "users"
    const val PRODUCT:String ="products"
    const val MYSHOPPAL :String = "MyShopPalPrefs"
    const val LOGGED_IN_USERNAME:String ="logged_in_username"
    const val EXTRA_LOGIN_DETAILS:String ="extra_user_details"
    const val READ_STORAGE_PERMISSIONS_CODE:Int =2
    const val IMAGE_REQUEST_CODE = 1
    const val USER_PROFILE_IMAGE_File ="User_Profile_Image"
    const val USER_PROFILE_IMAGE  ="image"
    const val PRODUCT_IMAGE :String = "Product_Image"

    const val USER_ID ="user_id"
    const val EXTRA_PRODUCT_OWNER_ID:String = "extra_product_owner_id"
    const val DEFAULT_CART_QUANTITY:String ="1"
    const val CART_ITEMS ="cart_items"
    const val PRODUCT_ID ="product_id"


    fun showImagechooser(activity: Activity)
    {
        val galleryIntent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        activity.startActivityForResult(galleryIntent, IMAGE_REQUEST_CODE)
    }


    const val  MALE ="male"
    const val FEMALE ="female"
    const val FIRST_NAME ="firstname"
    const val LAST_NAME ="lastname"
    const val MOBILE ="mobile"
    const val GENDER ="gender"
    const val PROFILE_COMPLETED ="Profilecompleted"
    const val EXTRA_PRODUCT_ID:String="extra_product_id"
    fun getfileextension(activity:Activity,URI:Uri):String?
    {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(activity.contentResolver.getType(URI))
    }

}