package com.example.datafetchfromphp.views


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.datafetchfromphp.adapter.FakeProductAdapter
import com.example.datafetchfromphp.databinding.ActivityReactiveBinding
import com.example.datafetchfromphp.model.FakeProductModel
import com.example.datafetchfromphp.network.ProductService
import com.example.datafetchfromphp.utility.Constraints
import com.jakewharton.rxbinding4.view.clicks
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class ReactiveActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReactiveBinding
    private lateinit var adapter: FakeProductAdapter
    private var fakeProductList = mutableListOf<FakeProductModel>()



    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReactiveBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        implementNetworkCall()
        search()

//        binding.button.clicks()
//            .throttleFirst(2000, TimeUnit.MILLISECONDS)  // for delay
//            .subscribe {
//                Log.d("click", "Clicked Sub")
//            }
        binding.button.setOnClickListener {
            startActivity(Intent(this@ReactiveActivity,LeakActivity::class.java))
        }

    }


    private fun simpleObserver() {
        val fakeList = listOf<String>("A", "B", "C")
        val observable = Observable.fromIterable(fakeList)  //create observable with fromIterable

        //create observer to observe the observable
        observable.subscribe(object : Observer<String> {
            override fun onSubscribe(d: Disposable) {
                Log.d("rTag", "onSubscribe is called")
            }

            override fun onError(e: Throwable) {
                Log.d("rTag", "error $e")
            }

            override fun onComplete() {
                Log.d("rTag", "onCompleted is called")
            }

            override fun onNext(t: String) {
                Log.d("rTag", "next= $t")
            }

        })
    }



    @SuppressLint("CheckResult")
    private fun implementNetworkCall() {

        val retrofit = Retrofit.Builder()
            .baseUrl(Constraints.storeUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()

        val productService = retrofit.create(ProductService::class.java)

        productService.getProduct()
            .subscribeOn(Schedulers.io())  // upstream + threadpool run on another thread
            .observeOn(AndroidSchedulers.mainThread())  //downStream run on main thread
            .subscribe() {
                fakeProductList.addAll(it)
                adapter = FakeProductAdapter(fakeProductList)
                binding.recyclerView.adapter = adapter
                adapter.notifyDataSetChanged()

                Log.d("searchTag", "size = ${fakeProductList.size}")
            }
    }


    //adding rxJava with searchbar for asynchronous
    private fun search() {

        //creating observable
        Observable.create<String> {

            binding.rSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (!it.isDisposed) {
                        newText?.let { it1 ->
                            it.onNext(it1)
                            filterList(it1)
                        }
                    }

                    return false
                }
            })
        }.debounce(1000, TimeUnit.MILLISECONDS)   //for avoiding too much network call
            .distinctUntilChanged()  //avoid duplicate network call
            .filter { t ->   //  filtering the string is empty or not
                if (t.isEmpty()) {
                    Log.d("searchTag", "is empty")
                    false
                } else {
                    true
                }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<String> {
                override fun onSubscribe(d: Disposable) {
                    Log.d("searchTag", "$d")
                }

                override fun onError(e: Throwable) {
                    Log.d("searchTag", "$e")
                }

                override fun onComplete() {
                    Log.d("searchTag", "onComplete")
                }

                override fun onNext(t: String) {
                    Log.d("searchTag", t)
                }

            })
    }

    //filtering list from fakeProductList
    private fun filterList(query: String) {
        val filterList = mutableListOf<FakeProductModel>()
        for (i in fakeProductList) {
            if (i.title.contains(query)) {
                filterList.add(i)
            }
        }
        if (filterList.isEmpty()) {
            Log.d("searchTag", "No Data Found")
        } else {
            adapter.setFilterList(filterList)
        }
    }


    companion object{
        lateinit var context: Context
    }

}