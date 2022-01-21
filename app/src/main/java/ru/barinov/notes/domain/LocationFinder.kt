package ru.barinov.notes.domain

import android.content.Context
import android.location.Geocoder
import android.location.LocationManager

class LocationFinder(locationManager: LocationManager, context: Context) {
    val locationManager: LocationManager= locationManager
    val geocoder: Geocoder = Geocoder(context)
}