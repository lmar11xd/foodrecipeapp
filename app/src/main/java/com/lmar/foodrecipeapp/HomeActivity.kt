package com.lmar.foodrecipeapp

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lmar.foodrecipeapp.adapter.MainCategoryAdapter
import com.lmar.foodrecipeapp.adapter.SubCategoryAdapter
import com.lmar.foodrecipeapp.database.RecipeDatabase
import com.lmar.foodrecipeapp.entities.CategoryItems
import com.lmar.foodrecipeapp.entities.MealsItems
import kotlinx.coroutines.launch

class HomeActivity : BaseActivity() {
    private lateinit var rv_main_category: RecyclerView
    private lateinit var rv_sub_category: RecyclerView
    private lateinit var tvCategory: TextView

    var arrMainCategory = ArrayList<CategoryItems>()
    var arrSubCategory = ArrayList<MealsItems>()

    var mainCategoryAdapter = MainCategoryAdapter()
    var subCategoryAdapter = SubCategoryAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        rv_main_category = findViewById(R.id.rv_main_category)
        rv_sub_category = findViewById(R.id.rv_sub_category)
        tvCategory = findViewById(R.id.tvCategory)

        getDataFromDb()
        mainCategoryAdapter.setClickListener(onClicked)
        subCategoryAdapter.setClickListener(onClickedSubItem)
    }

    private val onClicked = object : MainCategoryAdapter.OnItemClickListener {
        override fun onClicked(categoryName: String) {
            getMealDataFromDb(categoryName)
        }
    }

    private val onClickedSubItem = object : SubCategoryAdapter.OnItemClickListener {
        override fun onClicked(id: String) {
            var intent = Intent(this@HomeActivity, DetailActivity::class.java)
            intent.putExtra("id", id)
            startActivity(intent)
        }
    }

    private fun getDataFromDb() {
        launch {
           this.let {
               var cat = RecipeDatabase.getDatabase(this@HomeActivity).recipeDao().getAllCategory()
               arrMainCategory = cat as ArrayList<CategoryItems>
               arrMainCategory.reverse()

               getMealDataFromDb(arrMainCategory[0].strcategory)
               mainCategoryAdapter.setData(arrMainCategory)
               rv_main_category.layoutManager = LinearLayoutManager(this@HomeActivity, LinearLayoutManager.HORIZONTAL, false)
               rv_main_category.adapter = mainCategoryAdapter
           }
        }
    }

    private fun getMealDataFromDb(categoryName: String) {
        tvCategory.text = "$categoryName Category"
        launch {
            this.let {
                var cat = RecipeDatabase.getDatabase(this@HomeActivity).recipeDao().getSpecificMealList(categoryName)
                arrSubCategory = cat as ArrayList<MealsItems>
                subCategoryAdapter.setData(arrSubCategory)
                rv_sub_category.layoutManager = LinearLayoutManager(this@HomeActivity, LinearLayoutManager.HORIZONTAL, false)
                rv_sub_category.adapter = subCategoryAdapter
            }
        }
    }
}