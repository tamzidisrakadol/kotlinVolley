package com.example.datafetchfromphp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.datafetchfromphp.databinding.ProductItemLayoutBinding
import com.example.datafetchfromphp.model.FakeProductModel

class FakeProductAdapter(private val fakeProductList:List<FakeProductModel>):RecyclerView.Adapter<FakeProductAdapter.ViewHolder>() {

    class ViewHolder(val binding: ProductItemLayoutBinding):RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding=ProductItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return fakeProductList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val fakeProductModel = fakeProductList[position]
        holder.binding.fakeProductName.text=fakeProductModel.title
        holder.binding.fakeProductCategory.text=fakeProductModel.category
    }
}