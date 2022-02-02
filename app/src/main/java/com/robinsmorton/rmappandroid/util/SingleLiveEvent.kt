package com.robinsmorton.rmappandroid.util

import android.util.Log
import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

open class SingleLiveEvent<T> : MutableLiveData<T>(){

    companion object{
        const val TAG = "SingleLiveEvent"
    }
    private val mPending = AtomicBoolean(false)

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        if(hasActiveObservers()){
            Log.d(TAG, "Multiple observers registered but only one will be notified of changes")
        }

        super.observe(owner, { t ->
            if (mPending.compareAndSet(true, false)){
                observer.onChanged(t)
            }
        })
    }

    @MainThread
    override fun setValue(value: T?) {
        mPending.set(true)
        super.setValue(value)
    }

    /*
    Used for cases where T is void, to make calls cleaner.
    */
    @MainThread
    fun call() {
        setValue(null)
    }
}