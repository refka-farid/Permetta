package com.bravedroid.permetta.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.bravedroid.permetta.R
import com.bravedroid.permetta.base.LogHelper.logLifeCycle
import com.bravedroid.permetta.permission.oldapi.PermissionActivity

open class BaseFragment : Fragment(), LifecycleObserver {

    /**

    private class PermissionActivityObserver(private var context: Context?) : LifecycleObserver {
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun activityHasBeenCreated() {
    LogHelper.logInformation("activityHasBeenCreated from observer")
    (context as? PermissionActivity)?.lifecycle?.removeObserver(this) ?: return
    context = null
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun activityHasBeenStarted() {
    LogHelper.logInformation("activityHasBeenStarted")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun activityHasBeenResumed() {
    LogHelper.logInformation("activityHasBeenResumed")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun activityHasBeenPaused() {
    LogHelper.logInformation("activityHasBeenPaused")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun activityHasBeenStopped() {
    LogHelper.logInformation("activityHasBeenStopped")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun activityHasBeenDestroyed() {
    LogHelper.logInformation("activityHasBeenDestroyed")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    fun activityHasBeenANY() {
    LogHelper.logInformation("activityHasBeenAny")
    }
    }

    override fun onAttach(context: Context) {
    super.onAttach(context)
    (context as AppCompatActivity).lifecycle.addObserver(PermissionActivityObserver(context))
    }
     **/
    override fun onAttach(context: Context) {
        super.onAttach(context)
        logLifeCycle("onAttach")
        val hashcode = requireActivity().hashCode().toString()
        LogHelper.logInformation("#onAttach the hashcode of my activity is $hashcode")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logLifeCycle("onCreate")
        val hashcode = requireActivity().hashCode().toString()
        LogHelper.logInformation("#onCreate the hashcode of my activity is $hashcode")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        logLifeCycle("onCreateView")
        val hashcode = requireActivity().hashCode().toString()
        LogHelper.logInformation("#onCreateView the hashcode of my activity is $hashcode")
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_base, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logLifeCycle("onViewCreated")
        val hashcode = requireActivity().hashCode().toString()
        LogHelper.logInformation("#onViewCreated the hashcode of my activity is $hashcode")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    protected fun activityHasBeenCreated() {
        LogHelper.logInformation("activityHasBeenCreated from fragment")
        (context as PermissionActivity).lifecycle.removeObserver(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        logLifeCycle("onActivityCreated")
        val hashcode = requireActivity().hashCode().toString()
        LogHelper.logInformation("#onActivityCreated the hashcode of my activity is $hashcode")
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        logLifeCycle("onViewStateRestored")
    }

    override fun onStart() {
        super.onStart()
        logLifeCycle("onStart")
    }

    override fun onResume() {
        super.onResume()
        logLifeCycle("onResume")
    }

    override fun onPause() {
        super.onPause()
        logLifeCycle("onPause")
    }

    override fun onStop() {
        super.onStop()
        logLifeCycle("onStop")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        logLifeCycle("onSaveInstanceState")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        logLifeCycle("onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        logLifeCycle("onDestroy")
    }

    override fun onDetach() {
        super.onDetach()
        logLifeCycle("onDetach")
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) = BaseFragment()
    }
}
