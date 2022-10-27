package com.lmar.foodrecipeapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.lmar.foodrecipeapp.database.RecipeDatabase
import com.lmar.foodrecipeapp.entities.Category
import com.lmar.foodrecipeapp.entities.Meal
import com.lmar.foodrecipeapp.entities.MealsItems
import com.lmar.foodrecipeapp.interfaces.GetDataService
import com.lmar.foodrecipeapp.retrofit.RetrofitClientInstance
import kotlinx.coroutines.launch
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SplashScreen : BaseActivity(), EasyPermissions.PermissionCallbacks, EasyPermissions.RationaleCallbacks {
    private lateinit var btnGetStarted: Button
    private var READ_STORAGE_PERM = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        readStorageTask()

        btnGetStarted = findViewById(R.id.btnGetStarted)
        btnGetStarted.setOnClickListener {
            var intent = Intent(this@SplashScreen, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun getCategories() {
        val service = RetrofitClientInstance.instance!!.create(GetDataService::class.java)
        val call = service.getCategoryList()
        call.enqueue(object : Callback<Category> {
            override fun onFailure(call: Call<Category>, t: Throwable) {
                Toast.makeText(this@SplashScreen, "Something went wrong", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<Category>, response: Response<Category> ) {
                for (arr in response.body()!!.categorieitems!!) {
                    getMeal(arr.strcategory)
                }
                insertCategoryIntoRoomDb(response.body())
            }
        })
    }

    fun getMeal(categoryName: String) {
        val service = RetrofitClientInstance.instance!!.create(GetDataService::class.java)
        val call = service.getMealList(categoryName)
        call.enqueue(object : Callback<Meal> {
            override fun onFailure(call: Call<Meal>, t: Throwable) {
                Toast.makeText(this@SplashScreen, "Something went wrong", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<Meal>, response: Response<Meal>) {
                insertMealIntoRoomDb(categoryName, response.body())
            }
        })
    }

    private fun insertCategoryIntoRoomDb(category: Category?) {
        launch {
            this.let {
                for (arr in category!!.categorieitems!!) {
                    RecipeDatabase.getDatabase(this@SplashScreen)
                        .recipeDao().insertCategory(arr)
                }
            }
        }
    }

    private fun insertMealIntoRoomDb(categoryName: String, meal: Meal?) {
        launch {
            this.let {
                for (arr in meal!!.mealsItem!!) {
                    var mealItemModel = MealsItems(
                        arr.id,
                        arr.idMeal,
                        categoryName,
                        arr.strMeal,
                        arr.strMealThumb
                    )
                    RecipeDatabase.getDatabase(this@SplashScreen)
                        .recipeDao().insertMeal(mealItemModel)
                }
            }
        }
    }

    private fun clearDatabase() {
        launch {
            this.let {
                RecipeDatabase.getDatabase(this@SplashScreen).recipeDao().clearDb()
            }
        }
    }

    private fun hasReadStoragePermission(): Boolean {
        return EasyPermissions.hasPermissions(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    private fun readStorageTask() {
        if (hasReadStoragePermission()) {
            clearDatabase()
            getCategories()
        } else {
            EasyPermissions.requestPermissions(
                this,
                "This app needs access to your storage,",
                READ_STORAGE_PERM,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {

    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        }
    }

    override fun onRationaleAccepted(requestCode: Int) {

    }

    override fun onRationaleDenied(requestCode: Int) {

    }
}