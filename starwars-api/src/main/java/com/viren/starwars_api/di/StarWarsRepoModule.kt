package com.viren.starwars_api.di

import com.viren.starwars_api.impl.StarWarsRepositoryImpl
import com.viren.starwars_api_bridge.api.StarWarsApi
import com.viren.starwars_api_bridge.repository.StarWarsRepository
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
object StarWarsRepoModule {

    @JvmStatic
    @Provides
    fun providesStarWarsApi(retrofit: Retrofit): StarWarsApi = retrofit.create(StarWarsApi::class.java)

    @Provides
    @JvmStatic
    fun provideStarWarsRepository(
        starWarsApi: StarWarsApi
    ): StarWarsRepository = StarWarsRepositoryImpl(
        starWarsApi
    )

}