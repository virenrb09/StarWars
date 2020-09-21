package com.viren.ui.view


import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import com.viren.starwars_api_bridge.model.CharacterDetail
import com.viren.ui.viewmodel.CharacterSearchViewModel
import com.viren.starwars_ui.R
import com.viren.starwars_ui.databinding.FragmentCharacterSearchBinding
import com.viren.ui.adapter.CharacterSearchDiffUtilsCallback
import com.viren.ui.adapter.CharacterSearchRecyclerAdapter
import com.viren.ui.di.DaggerCharacterSearchComponent
import com.viren.ui.di.StarWarsDependenciesProvider
import com.viren.ui.utils.Response
import com.viren.ui.utils.dismissKeyboard
import com.viren.ui.utils.showError
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CharacterSearchFragment : BaseFragment() {

    @Inject
    lateinit var characterSearchViewModel: CharacterSearchViewModel
    private lateinit var layoutBinding: FragmentCharacterSearchBinding
    private var searchDisposable: Disposable? = null
    private lateinit var adapterCharacter: CharacterSearchRecyclerAdapter

    companion object {
        val TAG = CharacterSearchFragment::class.java.toString()

        fun createInstance(): CharacterSearchFragment {
            return CharacterSearchFragment()
        }
    }

    override fun getLayout(): Int = R.layout.fragment_character_search

    override fun setupDI(context: Context) {
        activity?.let {
            DaggerCharacterSearchComponent.factory()
                .create(
                    (it.application as StarWarsDependenciesProvider).starWarsCommonDependencies(),
                    it
                ).inject(this)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setupDI(context)
    }

    override fun onCreateComplete(savedInstanceState: Bundle?) {
        binding?.let {
            layoutBinding = binding as FragmentCharacterSearchBinding
            setupView()
            makeRecyclerAdapter()
        }
    }

    private fun setupView() {
        /**
         * Here i am using the KOTLIN extension to access the xml property directly - instead of doing view binding
         */
        layoutBinding.edtSearchName.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchCharacter(layoutBinding.edtSearchName.text.toString().trim())
            }
            true
        }

        layoutBinding.imgSearch.setOnClickListener {
            searchCharacter(layoutBinding.edtSearchName.text.toString().trim())
        }

        addDisposible(
            characterSearchViewModel.pageCount()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    layoutBinding.searchResult.text = it
                }
        )
        /**
         * Added disposable to fragment base to handle dispose on fragment out of view
         */
        addDisposible(
            characterSearchViewModel.characterSearchResult()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { it ->
                    when (it) {
                        is Response.Loading -> {
                            layoutBinding.loading.visibility = View.VISIBLE
                        }
                        is Response.Error -> {
                            layoutBinding.loading.visibility = View.INVISIBLE
                            view?.let {
                                showError(it, getString(R.string.api_error_msg)) {
                                    searchCharacter(
                                        layoutBinding.edtSearchName.text.toString().trim()
                                    )
                                }
                            }
                        }
                        is Response.Success -> {
                            layoutBinding.loading.visibility = View.INVISIBLE
                            showResultsInRecycler(it.data)
                        }
                    }
                }
        )
    }

    private fun searchCharacter(name: String) {
        // dismiss keyboard on search clicked
        layoutBinding.edtSearchName.dismissKeyboard()
        searchDisposable?.dispose()
        searchDisposable = characterSearchViewModel.searchCharacter(name)
        searchDisposable?.run { addDisposible(this) }
    }

    private fun makeRecyclerAdapter() {
        adapterCharacter =
            CharacterSearchRecyclerAdapter(
                CharacterSearchDiffUtilsCallback(),
                { character: CharacterDetail ->
                    characterSearchViewModel.showCharacterDetails(character)
                }) {
                // load more
                characterSearchViewModel.loadNextPage()
            }
        layoutBinding.recycler.adapter = adapterCharacter
    }

    private fun showResultsInRecycler(searchData: List<CharacterDetail>?) {
        adapterCharacter.submitList(searchData)
    }

}
