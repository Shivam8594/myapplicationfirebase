package com.example.myapplicationfirebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.example.myapplicationfirebase.models.CartItem

class CartListActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_list)
        setupActionbar()
    }
    private fun setupActionbar()
    {
        val actionbar = findViewById<Toolbar>(R.id.toolbar_cart_list_activity)
        setSupportActionBar(actionbar)
        val actionbar1 = supportActionBar
        if (actionbar1!= null)
        {
            actionbar1.setDisplayHomeAsUpEnabled(true)
            actionbar1.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24)
        }
        actionbar.setNavigationOnClickListener()
        {
            onBackPressed()
        }


    }

    fun successcartitemlist(cartList:ArrayList<CartItem>)
    {
          hideprogressdialog()
    }
}