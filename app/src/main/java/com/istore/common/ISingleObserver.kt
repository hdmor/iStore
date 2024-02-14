package com.istore.common

import com.istore.common.exception.IExceptionMapper
import io.reactivex.SingleObserver
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.greenrobot.eventbus.EventBus

abstract class ISingleObserver<T>(private val compositeDisposable: CompositeDisposable) :
    SingleObserver<T> {

    override fun onSubscribe(d: Disposable) {
        compositeDisposable.add(d)
    }

    override fun onError(e: Throwable) {
        EventBus.getDefault().post(IExceptionMapper.map(e))
    }

}