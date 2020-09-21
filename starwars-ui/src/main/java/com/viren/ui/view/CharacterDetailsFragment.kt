package com.viren.ui.view

import android.content.Context
import android.os.Bundle
import android.view.View
import com.viren.starwars_api_bridge.model.*
import com.viren.starwars_ui.R
import com.viren.starwars_ui.databinding.FragmentCharacterDetailsBinding
import com.viren.ui.adapter.MoviesRecyclerAdapter
import com.viren.ui.adapter.SpeciesRecyclerAdapter
import com.viren.ui.di.StarWarsDependenciesProvider
import com.viren.ui.viewmodel.CharacterDetailsViewModel
import com.viren.ui.di.DaggerCharacterDetailsComponent
import com.viren.ui.utils.Response
import com.viren.ui.utils.ResponseTypes
import com.viren.ui.utils.showError
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject
import kotlin.math.roundToInt

private const val KEY_URL = "URL"
private const val KEY_NAME = "NAME"

class CharacterDetailsFragment : BaseFragment() {
    private val speciesList = ArrayList<SpeciesDetailsResponse>()
    private lateinit var speciesAdapter: SpeciesRecyclerAdapter
    private val filmsList = ArrayList<MoviesDetailsResponse>()
    private lateinit var filmsAdapter: MoviesRecyclerAdapter

    private lateinit var layoutBinding: FragmentCharacterDetailsBinding

    @Inject
    lateinit var viewModel: CharacterDetailsViewModel

    companion object {
        val TAG = CharacterDetailsFragment::class.java.toString()

        @JvmStatic
        fun createInstance(name: String, url: String) = CharacterDetailsFragment().apply {
            arguments = Bundle().apply {
                putString(KEY_URL, url)
                putString(KEY_NAME, name)
            }
        }
    }

    override fun setupDI(context: Context) {
        activity?.let {
            DaggerCharacterDetailsComponent.factory()
                    .create(
                            (it.application as StarWarsDependenciesProvider).starWarsCommonDependencies(),
                            it,
                            childFragmentManager
                    ).inject(this)
        }
    }

    override fun getLayout(): Int = R.layout.fragment_character_details

    override fun onCreateComplete(savedInstanceState: Bundle?) {
        binding?.let { layoutBinding = binding as FragmentCharacterDetailsBinding }
        arguments?.let {
            it.getString(KEY_URL)?.let { url ->
                viewModel.setupDetails(url)
            }
            it.getString(KEY_NAME)?.let { name ->
                layoutBinding.txtName.text = name
            }
        }
        setupView()
    }

    private fun setupView() {
        filmsAdapter = MoviesRecyclerAdapter(filmsList)
        layoutBinding.recyclerFilms.adapter = filmsAdapter

        speciesAdapter = SpeciesRecyclerAdapter(speciesList)
        layoutBinding.recyclerSpecies.adapter = speciesAdapter

        layoutBinding.imgBack.setOnClickListener {
            fragmentManager?.popBackStack()
        }

        addDisposible(viewModel.observeData().observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if (it is Response.Success) {
                        val value = it.data?.second
                        when (it.data?.first) {
                            ResponseTypes.CHARACTER_DETAILS -> showCharacterDetails(value as CharacterDetailsResponse)
                            ResponseTypes.PLANET_DETAILS -> showPlanetDetails(value as PlanetDetailsResponse)
                            ResponseTypes.SPECIE_DETAILS -> showSpecieDetails(value as SpeciesDetailsResponse)
                            ResponseTypes.MOVIE_DETAILS -> showMovieDetails(value as MoviesDetailsResponse)
                        }
                    } else if (it is Response.Error) {
                        view?.let {view ->
                            showError(view, resources.getString(R.string.api_error_msg)) {
                                // TODO : handle api retry
                            }
                        }
                    }
                })
    }

    private fun showCharacterDetails(characterDetails: CharacterDetailsResponse) {
        layoutBinding.txtName.text = characterDetails.name
        layoutBinding.txtBirthDate.text = characterDetails.birth_year
        //if (characterDetails.height.isDigitsOnly())
        layoutBinding.txtHeight.text = getString(
                R.string.character_height,
                characterDetails.height,
                getFeet(characterDetails.height),
                getInch(characterDetails.height)
        )
    }

    private fun showMovieDetails(moviesDetails: MoviesDetailsResponse) {
        layoutBinding.progressMovie.visibility = View.GONE
        filmsList.add(moviesDetails)
        filmsAdapter.notifyItemInserted(filmsList.lastIndex)
    }

    private fun showSpecieDetails(speciesDetails: SpeciesDetailsResponse) {
        layoutBinding.progressDetails.visibility = View.GONE
        speciesList.add(speciesDetails)
        speciesAdapter.notifyItemInserted(speciesList.lastIndex)
    }

    private fun showPlanetDetails(planetDetails: PlanetDetailsResponse) {
        layoutBinding.progressDetails.visibility = View.GONE
        layoutBinding.txtPlanet.text = getString(R.string.character_planet_name_is, planetDetails.name)
        layoutBinding.txtPopulation.text = getString(R.string.planet_population_is, planetDetails.population)
    }

    private fun getInch(height: String): String {
        val inch = height.toFloat() / 2.54
        return inch.roundToInt().toString()
    }

    private fun getFeet(height: String): String {
        val feet = height.toFloat() / 30.48
        return feet.roundToInt().toString()
    }
}