package com.example.datafetchfromphp.network

import com.example.datafetchfromphp.model.FakeProductModel
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET

interface ProductService {

    @GET("/products")
    fun getProduct():Observable<List<FakeProductModel>>
}