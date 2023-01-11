package com.wmt.setasringtones.viewModels.fragments.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wmt.setasringtones.models.CategoriesItem
import com.wmt.setasringtones.repository.CategoriesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class CategoriesViewModel : ViewModel() {
    private var mutableLiveData = MutableLiveData<Response<ArrayList<CategoriesItem>>>()
    val categoriesData: LiveData<Response<ArrayList<CategoriesItem>>>
    get() = mutableLiveData

    suspend fun observeCategoriesResponse() {
        viewModelScope.launch(Dispatchers.Main) {
            val repo = CategoriesRepository()
            mutableLiveData.value = repo.getCategories()
        }
    }
}