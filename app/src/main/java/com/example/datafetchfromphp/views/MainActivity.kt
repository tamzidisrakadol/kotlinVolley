package com.example.datafetchfromphp.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.datafetchfromphp.adapter.CustomerAdapter
import com.example.datafetchfromphp.databinding.ActivityMainBinding
import com.example.datafetchfromphp.model.CustomerModel
import com.example.datafetchfromphp.utility.Constraints
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONException

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var customerList = mutableListOf<CustomerModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //start effect + loading data
        showShimmerEffect()

        //recyclerView layout manager
        binding.recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL,false)



        //swipe Refresh layout
        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.recyclerView.visibility = View.INVISIBLE
            binding.shimmerFrameLayout.visibility = View.VISIBLE
            showShimmerEffect()
            binding.swipeRefreshLayout.isRefreshing = false
        }


    }

    override fun onResume() {
        super.onResume()
        fetchCustomerList()
    }

    //collect data from mySql
    private fun fetchCustomerList(){
        val stringRequest = StringRequest(Request.Method.POST,Constraints.customerURl, {
            try {
                val jsonArray = JSONArray(it)
                customerList.clear()
                for (i in 0 until jsonArray.length()){
                    val jsonObject = jsonArray.getJSONObject(i)
                    val customerModel = CustomerModel()
                    customerModel.customerName = jsonObject.getString("CustomerName")
                    customerModel.customerAddress = jsonObject.getString("CustomerAddress")
                    customerModel.orderQuantity = jsonObject.getInt("orderQuantity")
                    customerList.add(customerModel)
                    Log.d("Stag","list : $customerList")
                }
                val customerAdapter = CustomerAdapter(customerList)
                binding.recyclerView.adapter = customerAdapter
                customerAdapter.notifyDataSetChanged()

            }catch (e:JSONException){
               Log.d("eTag",e.toString())
            }
        }, {
            Log.d("eTag",it.toString())
        })
        val requestQueue: RequestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)
    }


    private fun showShimmerEffect(){
        lifecycleScope.launch {
            binding.shimmerFrameLayout.startShimmer()
            delay(3000)
            binding.shimmerFrameLayout.stopShimmer()
            binding.shimmerFrameLayout.visibility = View.INVISIBLE
            withContext(Dispatchers.Main){
                fetchCustomerList()
                binding.recyclerView.visibility= View.VISIBLE
            }

        }
    }

}