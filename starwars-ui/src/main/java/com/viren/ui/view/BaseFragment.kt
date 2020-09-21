package com.viren.ui.view

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * A simple Fragment base class to perform the common chores of any fragment
 */
abstract class BaseFragment : Fragment() {

    internal var compositeDisposable: CompositeDisposable? = null
    var binding: ViewDataBinding? = null

    /**
     * abstract method to setup DI called on onAttach
     */
    abstract fun setupDI(context: Context)

    /**
     * get .xml resource of fragment layout
     */
    abstract fun getLayout(): Int

    /**
     * to notify BaseFragment impl. that they can start there work once the initial setup is done
     */
    abstract fun onCreateComplete(savedInstanceState: Bundle?)

    /**
     * this will safeguard the activity usage before attaching of fragment
     * as anyone using currentActivity before it will crash the app
     */
    private lateinit var currentActivity: Activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        compositeDisposable = CompositeDisposable()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(getLayout(), container, false)
        binding = DataBindingUtil.bind(view)
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Activity) {
            currentActivity = context
            setupDI(context)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        onCreateComplete(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        /**
         * clears all disposable once the fragment is destroyed
         * thus we bind the disposable call to lifecycle of fragment
         */
        compositeDisposable?.clear()
    }

    fun addDisposible(disposable: Disposable) {
        compositeDisposable?.add(disposable)
    }

}