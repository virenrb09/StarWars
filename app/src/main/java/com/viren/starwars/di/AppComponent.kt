package com.viren.starwars.di

import android.content.Context
import com.viren.starwars_api.di.StarWarsRepoModule
import com.viren.ui.usecase.NavigationUseCase
import com.viren.starwars.core.StarWarsApp
import com.viren.ui.di.StarWarsCommonDependencies
import com.viren.ui.usecase.NavigationUseCaseImpl
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class AppScope

@AppScope
@Component(
    modules = [AppModule::class]
)
interface AppComponent : StarWarsCommonDependencies {

    // Using dagger build pattern
    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: StarWarsApp): Builder

        @BindsInstance
        fun retrofit(retrofit: Retrofit): Builder

        @BindsInstance
        fun context(context: Context): Builder

        fun build(): AppComponent
    }

    fun inject(app: StarWarsApp)
}

@Module(
    includes = [
        StarWarsRepoModule::class
    ]
)
object AppModule {

    @Provides
    @JvmStatic
    @AppScope
    fun navigationUseCase(): NavigationUseCase = NavigationUseCaseImpl()

}