package com.example.datafetchfromphp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.datafetchfromphp.databinding.CustomeritemLayoutBinding
import com.example.datafetchfromphp.model.CustomerModel

class CustomerAdapter(private val customerList:List<CustomerModel>) : RecyclerView.Adapter<CustomerAdapter.ViewHolder>() {

    class ViewHolder(var binding:CustomeritemLayoutBinding ):RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CustomeritemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return customerList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val customerModel = customerList[position]
        holder.binding.customerName.text = customerModel.customerName
        holder.binding.customerAddress.text = customerModel.customerAddress
        holder.binding.orderQuantity.text= customerModel.orderQuantity.toString()
    }


}