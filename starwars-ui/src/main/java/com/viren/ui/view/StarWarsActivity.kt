package com.viren.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.viren.ui.di.DaggerStarWarsActivityComponent
import com.viren.starwars_ui.R
import com.viren.ui.di.StarWarsDependenciesProvider
import com.viren.ui.usecase.NavigationUseCase
import javax.inject.Inject

class StarWarsActivity : AppCompatActivity() {

    @Inject
    lateinit var navigationUseCase: NavigationUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_starwars)

        /**
         * Perform activity inject
         */
        DaggerStarWarsActivityComponent.builder()
            .dependencies((application as StarWarsDependenciesProvider).starWarsCommonDependencies())
            .build()
            .inject(this)

       navigationUseCase.displayCharacterSearchFragment(supportFragmentManager, R.id.container)
    }

}