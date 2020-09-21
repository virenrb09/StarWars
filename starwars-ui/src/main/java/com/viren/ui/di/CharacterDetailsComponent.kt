package com.viren.ui.di

import android.app.Activity
import androidx.fragment.app.FragmentManager
import com.viren.starwars_api_bridge.repository.StarWarsRepository
import com.viren.ui.scheduler.SchedulerProvider
import com.viren.ui.view.CharacterDetailsFragment
import com.viren.ui.viewmodel.CharacterDetailsViewModel
import com.viren.ui.viewmodel.CharacterDetailsViewModelImpl
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides

@Component(
    modules = [CharacterDetailsModule::class],
    dependencies = [StarWarsCommonDependencies::class]
)
interface CharacterDetailsComponent {
    // Using dagger factory pattern
    @Component.Factory
    interface Factory {
        fun create(
            starWarsCommonDependencies: StarWarsCommonDependencies,
            @BindsInstance activity: Activity,
            @BindsInstance fragmentManager: FragmentManager
        ): CharacterDetailsComponent
    }

    fun inject(fragment: CharacterDetailsFragment)
}

@Module
object CharacterDetailsModule {

    @Provides
    @JvmStatic
    fun characterDetailsViewModel(
        starWarsRepository: StarWarsRepository,
        schedulerProvider: SchedulerProvider
    ): CharacterDetailsViewModel = CharacterDetailsViewModelImpl(
        starWarsRepository,
        schedulerProvider
    )

    @Provides
    @JvmStatic
    fun schedularProvider(): SchedulerProvider = SchedulerProvider()
}