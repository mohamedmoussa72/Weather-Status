package com.orcas.dailyforecast.ui.viewmodel

import android.content.Context
import android.util.Log
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.*
import com.orcas.dailyforecast.utile.SingleEvent
import com.orcas.dailyforecast.utile.wrapEspressoIdlingResource
import com.orcas.data.model.WeatherDataDB
import com.orcas.data.model.city.City
import com.orcas.data.utile.DataResult
import com.orcas.domain.usecase.city.GetCitiesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CityViewModel @Inject constructor(private val getCitiesUseCase: GetCitiesUseCase):ViewModel(){

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val citiesLiveDataPrivate = MutableLiveData<DataResult<List<City>>>()
    val citiesLiveData: LiveData<DataResult<List<City>>> get() = citiesLiveDataPrivate

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    private val showToastPrivate = MutableLiveData<SingleEvent<Any>>()
    val showToast: LiveData<SingleEvent<Any>> get() = showToastPrivate



    fun getCities(context: Context) {

      viewModelScope.launch {
//            wrapEspressoIdlingResource {
                getCitiesUseCase.execute(context)?.collect {
                    citiesLiveDataPrivate.value = it
                    Log.e("TagData", it.status.toString())
                }
//            }
        }
    }

    fun showToastMessage(description:String) {
        Log.e("TagDataVIEW", description)

        showToastPrivate.value = SingleEvent(description)
    }
}