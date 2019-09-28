package com.vidhey.viewmodelapicall.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vidhey.viewmodelapicall.Model.ProductClass
import com.vidhey.vmac.VMac
import io.reactivex.disposables.CompositeDisposable

class MainViewModel : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val _data = MutableLiveData<ArrayList<ProductClass>>()

    val data: LiveData<ArrayList<ProductClass>>
        get() {
            return _data
        }


    init {

        val request = ""

        val methodBase = ""
        val methodName = ""
        val url = ""

        compositeDisposable.add(
            VMac()
                .url(url)
                .methodName(methodName)
                .methodBase(methodBase)
                .request(request)
                .parentTags("a:Catalogue")
                .childTags(
                    "a:TotalCash",
                    "a:CatalogueId",
                    "a:PointsRequired",
                    "a:ProductImage",
                    "a:ProductName",
                    "a:IsPlanner",
                    "a:CatogoryName",
                    "a:MerchantName",
                    "a:DeliveryType",
                    "a:ProductCode",
                    "a:ProductDesc",
                    "a:TermsCondition",
                    "a:VendorName",
                    "a:VendorId",
                    "a:RedemptionDate"
                )
                .execute<ProductClass> {

                    Log.d("Result", "List_Data : $it")
                }
        )

    }

    override fun onCleared() {
        super.onCleared()

        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
            Log.d("compositeDisposable", "Disposed Successful")
        }
    }
}