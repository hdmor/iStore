package com.istore

import android.app.Application
import android.content.SharedPreferences
import android.os.Bundle
import androidx.room.Room
import com.facebook.drawee.backends.pipeline.Fresco
import com.istore.checkout.CheckoutViewModel
import com.istore.data.db.AppDatabase
import com.istore.data.repo.*
import com.istore.data.source.*
import com.istore.feature.auth.AuthViewModel
import com.istore.feature.cart.CartViewModel
import com.istore.feature.favorites.FavoriteProductsViewModel
import com.istore.feature.main.MainViewModel
import com.istore.feature.main.comment.CommentListViewModel
import com.istore.feature.main.home.HomeViewModel
import com.istore.feature.main.detail.ProductDetailViewModel
import com.istore.feature.main.common.ProductsListAdapter
import com.istore.feature.main.list.ProductListViewModel
import com.istore.feature.profile.ProfileViewModel
import com.istore.services.FrescoImageLoadingServiceImpl
import com.istore.services.ImageLoadingService
import com.istore.services.http.createApiServiceInstance
import com.istore.shipping.ShippingViewModel
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        Fresco.initialize(this)

        val appModule = module {
            // singleton services
            single { createApiServiceInstance() }
            single<ImageLoadingService> { FrescoImageLoadingServiceImpl() }
            // for saving user tokens in sharedPreferences
            single<SharedPreferences> {
                this@App.getSharedPreferences(
                    "app_settings",
                    MODE_PRIVATE
                )
            }
            single<UserRepository> {
                UserRepositoryImpl(
                    UserRemoteDataSource(get()),
                    UserLocalDataSource(get())
                )
            }
            // for using in IAuthenticator Class for saving token in sharedPreferences
            single { UserLocalDataSource(get()) }
            single<OrderRepository> { OrderRepositoryImpl(OrderRemoteDataSource(get())) }
            single { Room.databaseBuilder(this@App, AppDatabase::class.java, "db_istore").build() }

            // repository factories
            factory<BannerRepository> { BannerRepositoryImpl(BannerRemoteDataSource(get())) }
            // set room productDao fun to access ProductLocalDataSource as second parameter
            factory<ProductRepository> { ProductRepositoryImpl(ProductRemoteDataSource(get()), get<AppDatabase>().productDao()) }
            factory<CommentRepository> { CommentRepositoryImpl(CommentRemoteDataSource(get())) }
            factory<CartRepository> { CartRepositoryImpl(CartRemoteDataSource(get())) }

            // adapter factories [just for injecting imageLoadingService we us this factory]
            factory { (viewType: Int) -> ProductsListAdapter(viewType, get()) }

            // view models
            viewModel { HomeViewModel(get(), get()) }
            viewModel { (bundle: Bundle) -> ProductDetailViewModel(bundle, get(), get()) }
            viewModel { (productId: Int) -> CommentListViewModel(productId, get()) }
            viewModel { (sort: Int) -> ProductListViewModel(sort, get()) }
            viewModel { AuthViewModel(get()) }
            viewModel { CartViewModel(get()) }
            viewModel { MainViewModel(get()) }
            viewModel { ShippingViewModel(get()) }
            viewModel { (orderId: Int) -> CheckoutViewModel(orderId, get()) }
            viewModel { ProfileViewModel(get()) }
            viewModel { FavoriteProductsViewModel(get()) }
        }

        startKoin {
            androidContext(this@App)
            modules(appModule)
        }

        // loading auth token to TokenContainer when starting app
        val userRepository: UserRepository = get()
        userRepository.loadToken()

    }
}