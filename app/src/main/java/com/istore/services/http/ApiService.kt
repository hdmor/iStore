package com.istore.services.http

import com.google.gson.JsonObject
import com.istore.data.*
import io.reactivex.Single
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @GET("banner/slider")
    fun getBanners(): Single<List<Banner>>

    @GET("product/list")
    fun getProducts(@Query("sort") sort: String): Single<List<Product>>

    @GET("comment/list")
    // @Query means QueryParameters
    fun getComments(@Query("product_id") productId: Int): Single<List<Comment>>

    @POST("cart/add")
    // @Body means BodyRequest
    fun addToCart(@Body jsonObject: JsonObject): Single<AddToCartResponse>

    @POST("cart/remove")
    fun removeItemFromCart(@Body jsonObject: JsonObject): Single<MessageResponse>

    @GET("cart/list")
    fun getCart(): Single<CartResponse>

    @POST("cart/changeCount")
    fun changeCartItemsCount(@Body jsonObject: JsonObject): Single<AddToCartResponse>

    @GET("cart/count")
    fun getCartItemsCount(): Single<CartItemsCount>

    @POST("auth/token")
    fun login(@Body jsonObject: JsonObject): Single<TokenResponse>

    @POST("user/register")
    fun signup(@Body jsonObject: JsonObject): Single<MessageResponse>

    /**
     * for refreshToken we use Call retrofit
     * because IAuthenticator Class must be handle that
     */
    @POST("auth/token")
    fun refreshToken(@Body jsonObject: JsonObject): Call<TokenResponse>

    @POST("order/submit")
    fun submitOrder(@Body jsonObject: JsonObject): Single<SubmitOrderResult>

    @GET("order/checkout")
    fun checkout(@Query("order_id") orderId: Int): Single<CheckoutResult>
}

fun createApiServiceInstance(): ApiService {

    /**
     * if you want to sent always header by any request you can use addInterceptor by below example
     * if you not, you can add [@Header annotation] to top of each request
     */
    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor {
            val oldRequest = it.request()
            val newRequestBuilder = oldRequest.newBuilder()
            if (TokenContainer.token != null)
                newRequestBuilder.addHeader("Authorization", "Bearer ${TokenContainer.token}")
            newRequestBuilder.addHeader("Accept", "application/json")
            newRequestBuilder.method(oldRequest.method(), oldRequest.body())
            return@addInterceptor it.proceed(newRequestBuilder.build())
        }.build()

    val retrofit = Retrofit.Builder()
        .baseUrl("http://expertdevelopers.ir/api/v1/")
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()
    return retrofit.create(ApiService::class.java)
}