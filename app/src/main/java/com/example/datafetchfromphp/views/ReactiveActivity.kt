package com.example.datafetchfromphp.views


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.datafetchfromphp.apiService.ProductService
import com.example.datafetchfromphp.databinding.ActivityReactiveBinding
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



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReactiveBinding.inflate(layoutInflater)
        setContentView(binding.root)
        simpleObserver()
        implementNetworkCall()


        binding.button.clicks()
            .throttleFirst(2000, TimeUnit.MILLISECONDS)  // for delay
            .subscribe {
            Log.d("click", "Clicked Sub")
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


    private fun implementNetworkCall(){

        val retrofit = Retrofit.Builder()
            .baseUrl("https://fakestoreapi.com")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()

        val productService = retrofit.create(ProductService::class.java)

        productService.getProduct()
            .subscribeOn(Schedulers.io())  // upstream + threadpool run on another thread
            .observeOn(AndroidSchedulers.mainThread())  //downStream run on main thread
            .subscribe(){
                Log.d("product",it.toString())
            }
    }

}