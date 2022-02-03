package ru.barinov.notes.core.liveData

import androidx.lifecycle.*

fun <T1, T2, T3, R> combine(
    liveData1: LiveData<T1>,
    liveData2: LiveData<T2>,
    liveData3: LiveData<T3>,
    combineFn: (value1: T1, value2: T2, value3: T3) -> R
): LiveData<R> = MediatorLiveData<R>().apply {
    addSource(liveData1) {
        val liveData2Value = liveData2.value
        val liveData3Value = liveData3.value
        if (liveData2Value != null && liveData3Value != null) {
            value = combineFn(it, liveData2Value, liveData3Value)
        }
    }

    addSource(liveData2) {
        val liveData1Value = liveData1.value
        val liveData3Value = liveData3.value
        if (liveData1Value != null && liveData3Value != null) {
            value = combineFn(liveData1Value, it, liveData3Value)
        }
    }

    addSource(liveData3) {
        val liveData1Value = liveData1.value
        val liveData2Value = liveData2.value
        if (liveData1Value != null && liveData2Value != null) {
            value = combineFn(liveData1Value, liveData2Value, it)
        }
    }

}
