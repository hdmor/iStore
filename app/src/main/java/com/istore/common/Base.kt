package com.istore.common

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.snackbar.Snackbar
import com.istore.R
import com.istore.common.exception.IException
import com.istore.feature.auth.AuthActivity
import io.reactivex.disposables.CompositeDisposable
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

abstract class IFragment : Fragment(), IView {

    override val rootView: CoordinatorLayout?
        get() = view as CoordinatorLayout

    override val viewContext: Context?
        get() = context

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        EventBus.getDefault().unregister(this)
        super.onStop()
    }
}

abstract class IActivity : AppCompatActivity(), IView {

    override val rootView: CoordinatorLayout?
        get() {
            val viewGroup = window.decorView.findViewById(android.R.id.content) as ViewGroup
            if (viewGroup !is CoordinatorLayout) {
                viewGroup.children.forEach {
                    if (it is CoordinatorLayout) return it
                }
                throw IllegalStateException("root view must be instance of coordinator layout.")
            } else return viewGroup
        }
    override val viewContext: Context?
        get() = this

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        EventBus.getDefault().unregister(this)
        super.onStop()
    }
}

interface IView {
    val rootView: CoordinatorLayout?
    val viewContext: Context?
    fun setProgressIndicator(flag: Boolean) {
        rootView?.let {
            viewContext?.let { context ->
                var loadingView = it.findViewById<View>(R.id.loading_fr)
                if (loadingView == null && flag) {
                    loadingView =
                        LayoutInflater.from(context).inflate(R.layout.view_loading, it, false)
                    it.addView(loadingView)
                }
                loadingView?.visibility = if (flag) View.VISIBLE else View.GONE
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun errorHandler(iException: IException) {
        viewContext?.let {
            when (iException.type) {
                IException.Type.SIMPLE -> showSnackBar(
                    iException.serverMessage ?: it.getString(
                        iException.userFriendlyMessage
                    )
                )
                IException.Type.AUTH -> {
                    if (it !is AuthActivity)
                        it.startActivity(Intent(it, AuthActivity::class.java))
                    Toast.makeText(it, iException.serverMessage, Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Log.e(TAG, "errorHandler: ${iException.message}")
                }
            }
        }
    }

    fun showSnackBar(message: String, duration: Int = Snackbar.LENGTH_SHORT) {
        rootView?.let { Snackbar.make(it, message, duration).show() }
    }

    fun showEmptyState(layoutResId: Int): View? {
        rootView?.let {
            viewContext?.let { context ->
                var emptyState = it.findViewById<View>(R.id.fl_empty_state_root_view)
                if (emptyState == null) {
                    emptyState = LayoutInflater.from(context).inflate(layoutResId, it, false)
                    it.addView(emptyState)
                }
                emptyState.visibility = View.VISIBLE
                return emptyState
            }
        }
        return null
    }
}

abstract class IViewModel : ViewModel() {
    val compositeDisposable = CompositeDisposable()
    val progressBarLiveData = MutableLiveData<Boolean>()

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}
