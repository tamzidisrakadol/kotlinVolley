package com.example.datafetchfromphp.views

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.datafetchfromphp.R
import com.example.datafetchfromphp.databinding.ActivityReactiveBinding
import com.jakewharton.rxbinding4.view.clicks
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import java.util.concurrent.TimeUnit

class ReactiveActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReactiveBinding


    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityReactiveBinding.inflate(layoutInflater)
        setContentView(binding.root)
        simpleObserver()
       binding.button.clicks().throttleFirst(2000,TimeUnit.MILLISECONDS).subscribe{
           Log.d("click","Clicked Sub")
       }

    }



    private fun simpleObserver(){
        val fakeList = listOf<String>("A","B","C")
        val observable = Observable.fromIterable(fakeList)  //create observable with fromIterable

        //create observer to observe the observable
        observable.subscribe(object : Observer<String>{
            override fun onSubscribe(d: Disposable) {
                Log.d("rTag","onSubscribe is called")
            }

            override fun onError(e: Throwable) {
                Log.d("rTag","error $e")
            }

            override fun onComplete() {
                Log.d("rTag","onCompleted is called")
            }

            override fun onNext(t: String) {
                Log.d("rTag","next= $t")
            }

        })
    }
}