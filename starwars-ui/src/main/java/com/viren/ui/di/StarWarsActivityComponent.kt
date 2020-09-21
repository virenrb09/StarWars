package com.viren.ui.di

import com.viren.ui.view.StarWarsActivity
import dagger.Component
import dagger.Module
import javax.inject.Scope

@Scope
annotation class ActivityScope

@ActivityScope
@Component(
    modules = [ActivityModule::class],
    dependencies = [StarWarsCommonDependencies::class]
)
interface StarWarsActivityComponent  {

    @Component.Builder
    interface Builder {

        fun dependencies(starWarsCommonDependencies: StarWarsCommonDependencies) : Builder

        fun build(): StarWarsActivityComponent
    }

    fun inject(activity: StarWarsActivity)
}

@Module
object ActivityModule {

}