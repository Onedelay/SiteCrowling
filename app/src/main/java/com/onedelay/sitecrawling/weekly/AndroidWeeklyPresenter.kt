package com.onedelay.sitecrawling.weekly

import com.onedelay.sitecrawling.data.source.RetrofitService
import com.onedelay.sitecrawling.data.model.entity.WeeklyItem
import com.onedelay.sitecrawling.util.addTo
import com.onedelay.sitecrawling.util.onMainThread
import com.onedelay.sitecrawling.util.onNetwork
import io.reactivex.disposables.CompositeDisposable


internal class AndroidWeeklyPresenter constructor(
        private val view: AndroidWeeklyContract.View,
        private val retrofitService: RetrofitService = RetrofitService.create(),
        private val compositeDisposable: CompositeDisposable = CompositeDisposable()
) : AndroidWeeklyContract.UserAction {

    override fun requestAndroidWeekly() {
        view.showProgress()

        retrofitService.getAndroidWeekly()
                .onNetwork()
                .onMainThread()
                .subscribe({
                    view.receiveList(it)
                    view.hideProgress()
                }, {
                    view.showError(it.message ?: "")
                    view.hideProgress()
                }).addTo(compositeDisposable)
    }

    override fun clickWeeklyItem(item: WeeklyItem) {
        view.openNewBrowser(item.url)
    }

    override fun onDestroy() {
        compositeDisposable.clear()
    }

}