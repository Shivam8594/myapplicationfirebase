package com.example.myapplicationfirebase

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.provider.SyncStateContract
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import com.example.bottomnavigation.fragments.DashboardFragment
import com.example.bottomnavigation.fragments.ProductsFragment
import com.example.myapplicationfirebase.models.CartItem
import com.example.myapplicationfirebase.models.Product
import com.example.myapplicationfirebase.models.User
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Class
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.currentCoroutineContext

class FirestoreWork {

    private val mfirestore = FirebaseFirestore.getInstance()

    fun registerUser(activity: RegisterActivity, userInfo: User) {
        mfirestore.collection("users")
            .document(userInfo.id)
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {

                activity.ShowErrorSnackbar(
                    "Your Details are valid Registration Successfull",
                    false
                )
                activity.hideprogressdialog()
            }
            .addOnFailureListener { e ->
                activity.hideprogressdialog()

            }
    }

    fun getCurrentUserId(): String {
        val currentuser = FirebaseAuth.getInstance().currentUser
        var currentuserId = ""
        if (currentuser != null) {
            currentuserId = currentuser.uid
        }
        return currentuserId

    }

    fun getUserDetails(activity: Activity) {
        mfirestore.collection(Constants.USERS)
            .document(getCurrentUserId())
            .get()
            .addOnSuccessListener { document ->
                Log.i(activity.javaClass.simpleName, document.toString())

                val user = document.toObject<User>(User::class.java)!!
                val sharedPrefrences = activity.getSharedPreferences(
                    Constants.MYSHOPPAL,
                    Context.MODE_PRIVATE
                )

                val editor: SharedPreferences.Editor = sharedPrefrences.edit()
                editor.putString(Constants.LOGGED_IN_USERNAME, "${user.firstname} ${user.lastname}")

                editor.apply()

                when (activity) {
                    is LoginActivity -> {
                        activity.userLoggedInSuccess(user)
                    }
                    is SettingActivity ->
                    {
                        activity.userdetailssuccess(user)
                    }
                }

            }
            .addOnFailureListener { e ->
                when (activity) {
                    is LoginActivity -> {
                        activity.hideprogressdialog()
                     }
                    is SettingActivity ->
                    {
                        activity.hideprogressdialog()
                    }
                }
            }
    }

    fun updateuserprofiledata(activity: Activity, userHashMap: HashMap<String, Any>) {
        mfirestore.collection(Constants.USERS)
            .document(getCurrentUserId())
            .update(userHashMap)
            .addOnSuccessListener {

                when (activity) {
                    is UserProfileActivity -> {
                        activity.onsuccessupdate()
                    }

                }


            }
            .addOnFailureListener { e ->
                when (activity) {
                    is UserProfileActivity -> {
                        activity.hideprogressdialog()
                    }

                }
                Log.e(activity.javaClass.simpleName, "Error While updating the user details", e)


            }
    }

    fun storeimagetocloudstorage(activity: Activity, imageFileuri: Uri?,imagetype:String) {
        val sref: StorageReference = FirebaseStorage.getInstance().reference.child(
             imagetype + System.currentTimeMillis() + '.' +
                    Constants.getfileextension(activity, imageFileuri!!)
        )
        sref.putFile(imageFileuri!!).addOnSuccessListener { taskSnapshot ->
            Log.e("Firebase Image Url", taskSnapshot.metadata!!.reference!!.downloadUrl.toString())

            taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                uri->
                Log.e("Downloadable image Url",uri.toString())
                 when(activity)
                 {
                     is UserProfileActivity ->
                     {
                         activity.imageUploadSuccess(uri)

                     }
                     is AddProductActivity->
                     {
                         activity.imageUploadSuccess(uri)
                     }
                 }


            }

        }
            .addOnFailureListener { exception->
                when(activity)
                {
                    is UserProfileActivity ->
                    {
                        activity.hideprogressdialog()

                    }
                    is AddProductActivity ->
                    {
                        activity.hideprogressdialog()

                    }
                }
                Log.e(activity.javaClass.simpleName,exception.message, exception)

            }
    }
    fun uploadproductdetails(activity:AddProductActivity, productInfo: Product) {
        mfirestore.collection("products")
            .document()
            .set(productInfo, SetOptions.merge())
            .addOnSuccessListener {

               activity.productuploadsuccess()
             }
            .addOnFailureListener { e ->
                activity.hideprogressdialog()
                activity.productuploadfailed()

            }
    }


     fun getProductsList(fragment: Fragment)
         {
         mfirestore.collection(Constants.PRODUCT)
             .whereEqualTo(Constants.USER_ID,getCurrentUserId())
             .get()
             .addOnSuccessListener { document->
               val productsList:ArrayList<Product> = ArrayList()
                 for(i in document.documents)
                 {
                     var product = i.toObject(Product::class.java)
                    product!!.product_id =i.id
                     productsList.add(product!!)
                 }

                 when(fragment)
                 {
                     is ProductsFragment ->
                     {
                              fragment.successProductListFromFirestore(productsList)
                     }
                 }
             }

                  }

    fun getDashboardItemList(fragment: DashboardFragment)
    {
        mfirestore.collection(Constants.PRODUCT)
            .get()
            .addOnSuccessListener { document->
                 val productsList:ArrayList<Product> = ArrayList()
                for(i in document.documents)
                {
                    var product = i.toObject(Product::class.java)
                    product!!.product_id =i.id
                    productsList.add(product!!)
                }
                fragment.successDashboardItemsList(productsList)
            }
            .addOnFailureListener {
                e->
                fragment.hideprogressdialog()
            }
    }
    fun deleteproduct(fragment:ProductsFragment, productId:String)
    {
        mfirestore.collection(Constants.PRODUCT)
            .document(productId)
            .delete()
            .addOnSuccessListener {
                fragment.productDeleteSuccess()
            }
            .addOnFailureListener {
                fragment.hideprogressdialog()
            }
    }

    fun getproductdetails(activity:ProductDetailsActivity, productid:String)
    {
        mfirestore.collection(Constants.PRODUCT)
            .document(productid)
            .get()
            .addOnSuccessListener {
                document->
                val product = document.toObject(Product::class.java)
                activity.ProductDetailsSuccess(product!!)

            }
            .addOnFailureListener {
                e->
                activity.hideprogressdialog()
            }
    }

    fun addtoCartItems(activity:ProductDetailsActivity, addToCart: CartItem)
    {
        mfirestore.collection(Constants.CART_ITEMS)
            .document()
            .set(addToCart, SetOptions.merge())
            .addOnSuccessListener {
                activity.addToCartSucess()
            }
            .addOnFailureListener {
                activity.hideprogressdialog()
            }
    }

    fun checkIfItemexitIncart(activity:ProductDetailsActivity, productId:String)
    {
        mfirestore.collection(Constants.CART_ITEMS)
            .whereEqualTo(Constants.USER_ID,getCurrentUserId())
            .whereEqualTo(Constants.PRODUCT_ID,productId)
            .get()
                .addOnSuccessListener { document->
                      if(document.documents.size>0)
                      {
                          activity.productExitIncart()
                      } else
                      {
                          activity.hideprogressdialog()
                      }
            }
            .addOnFailureListener {
                activity.hideprogressdialog()
            }
    }

         fun getcartlist(activity:Activity)

         {
             mfirestore.collection(Constants.CART_ITEMS)
                 .whereEqualTo(Constants.USER_ID,getCurrentUserId())
                 .get()
                 .addOnSuccessListener { documents->
                     val list:ArrayList<CartItem> = ArrayList()
                     for(i in documents)
                     {
                         val cartItem = i.toObject(CartItem::class.java)
                         cartItem.id= i.id
                         list.add(cartItem)
                     }
                 }
         }


}