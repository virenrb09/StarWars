package com.viren.starwars.core

import android.app.Application
import android.content.Context
import com.viren.starwars.di.AppComponent
import com.viren.starwars.di.DaggerAppComponent
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.viren.starwars_api_bridge.BASE_URL
import com.viren.ui.di.StarWarsCommonDependencies
import com.viren.ui.di.StarWarsDependenciesProvider
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class StarWarsApp : Application(), StarWarsDependenciesProvider {
    // retrofit instance
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

    private lateinit var component: AppComponent

    override fun onCreate() {
        super.onCreate()
        setupDi()
    }

    private fun setupDi() {
        component = DaggerAppComponent.builder()
            .application(this)
            .retrofit(retrofit)
            .context(this)
            .build()
        component.inject(this)
    }

    // the dependencies provide from App level
    override fun starWarsCommonDependencies(): StarWarsCommonDependencies = component
}

// extension for app context
fun Context.app(): StarWarsApp = applicationContext as StarWarsApp
