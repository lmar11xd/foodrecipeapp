package com.lmar.foodrecipeapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lmar.foodrecipeapp.R
import com.lmar.foodrecipeapp.entities.CategoryItems

class MainCategoryAdapter: RecyclerView.Adapter<MainCategoryAdapter.RecipeViewHolder>() {

    private val resource = R.layout.item_rv_main_category
    var ctx: Context? = null
    var listener : OnItemClickListener? = null

    var arrMainCategory = ArrayList<CategoryItems>()

    class RecipeViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private var tvDishName : TextView
        private var ivDish : ImageView

        init {
            tvDishName = itemView.findViewById(R.id.tv_dish_name)
            ivDish = itemView.findViewById(R.id.img_dish)
        }

        fun bind(item: CategoryItems, listener: OnItemClickListener) {
            tvDishName.text = item.strcategory
            Glide.with(itemView.context!!).load(item.strcategorythumb).into(ivDish)
            itemView.rootView.setOnClickListener {
                listener!!.onClicked(item.strcategory)
            }
        }
    }

    fun setData(arrData: List<CategoryItems>) {
        arrMainCategory = arrData as ArrayList<CategoryItems>
    }

    fun setClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        ctx = parent.context
        return RecipeViewHolder(LayoutInflater.from(parent.context).inflate(resource,parent,false))
    }

    override fun getItemCount(): Int {
        return arrMainCategory.size
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        holder.bind(arrMainCategory[position], listener!!)
    }

    interface OnItemClickListener {
        fun onClicked(categoryName: String)
    }
}