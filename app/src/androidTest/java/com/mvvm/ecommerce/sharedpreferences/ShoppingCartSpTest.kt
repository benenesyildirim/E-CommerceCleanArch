package com.mvvm.ecommerce.sharedpreferences

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mvvm.ecommerce.App
import com.mvvm.ecommerce.domain.model.Product
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ShoppingCartSpTest {
    private lateinit var context: Context
    private lateinit var sharedPreferences: SharedPreferences
    private val product = Product(1001, "Duş Jeli", "3333", "TRY", "imageUrl", 22)

    @Before
    fun setup() {
        context = getApplicationContext<App>()
        sharedPreferences = context.getSharedPreferences(
            "prefs",
            MODE_PRIVATE
        )
    }

    @Test
    fun isProductSavedSuccessfully() {
        sharedPreferences.edit().putString("product", product.toString()).apply()

        assertEquals(sharedPreferences.getString("product",""),product.toString())
    }
}