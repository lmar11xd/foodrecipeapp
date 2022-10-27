package com.lmar.foodrecipeapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lmar.foodrecipeapp.R
import com.lmar.foodrecipeapp.entities.MealsItems

class SubCategoryAdapter: RecyclerView.Adapter<SubCategoryAdapter.RecipeViewHolder>() {

    private val resource = R.layout.item_rv_sub_category
    var listener : OnItemClickListener? = null

    var arrSubCategory = ArrayList<MealsItems>()

    class RecipeViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private var tvDishName : TextView
        private var ivDish : ImageView
        
        init {
            tvDishName = itemView.findViewById(R.id.tv_dish_name)
            ivDish = itemView.findViewById(R.id.img_dish)
        }

        fun bind(item: MealsItems, listener: OnItemClickListener) {
            tvDishName.text = item.strMeal
            Glide.with(itemView.context!!).load(item.strMealThumb).into(ivDish)
            itemView.rootView.setOnClickListener {
                listener!!.onClicked(item.idMeal)
            }
        }
    }

    fun setData(arrData: List<MealsItems>) {
        arrSubCategory = arrData as ArrayList<MealsItems>
    }

    fun setClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        return RecipeViewHolder(LayoutInflater.from(parent.context).inflate(resource,parent,false))
    }

    override fun getItemCount(): Int {
        return arrSubCategory.size
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        holder.bind(arrSubCategory[position], listener!!)
    }

    interface OnItemClickListener {
        fun onClicked(id: String)
    }
}