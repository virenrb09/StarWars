package com.viren.ui.di

import androidx.fragment.app.FragmentActivity
import com.viren.ui.usecase.NavigationUseCase
import com.viren.starwars_api_bridge.repository.StarWarsRepository
import com.viren.ui.scheduler.SchedulerProvider
import com.viren.ui.view.CharacterSearchFragment
import com.viren.ui.viewmodel.CharacterSearchViewModel
import com.viren.ui.viewmodel.CharacterSearchViewModelImpl
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

interface StarWarsCommonDependencies {
    fun retrofit(): Retrofit
    fun navigationUseCase(): NavigationUseCase
    fun starWarsRepository(): StarWarsRepository
}

interface StarWarsDependenciesProvider {
    fun starWarsCommonDependencies(): StarWarsCommonDependencies
}

@Component(
    modules = [CharacterSearchModule::class],
    dependencies = [StarWarsCommonDependencies::class]
)
interface CharacterSearchComponent {
    // Using dagger factory pattern
    @Component.Factory
    interface Factory {
        fun create(
            starWarsCommonDependencies: StarWarsCommonDependencies,
            @BindsInstance activity: FragmentActivity
        ): CharacterSearchComponent
    }

    fun inject(fragment: CharacterSearchFragment)
}

@Module
object CharacterSearchModule {

    @Provides
    @JvmStatic
    fun characterSearchViewModel(
        navigationUseCase: NavigationUseCase,
        starWarsRepository: StarWarsRepository,
        activity: FragmentActivity,
        schedulerProvider: SchedulerProvider
    ): CharacterSearchViewModel = CharacterSearchViewModelImpl(
        activity,
        starWarsRepository,
        navigationUseCase,
        activity.supportFragmentManager,
        schedulerProvider
    )

    @Provides
    @JvmStatic
    fun schedularProvider(): SchedulerProvider = SchedulerProvider()
}