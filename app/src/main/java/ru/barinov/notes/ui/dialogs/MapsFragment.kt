package ru.barinov.notes.ui.dialogs

import android.app.AlertDialog
import android.app.Dialog


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.google.android.gms.maps.CameraUpdateFactory


import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

import ru.barinov.R
import ru.barinov.notes.ui.noteViewFragment.NoteView


class MapsFragment : DialogFragment() {
    var lat = 0.0
    var lng = 0.0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val data: Bundle? = arguments
        lat= data!!.getDouble(NoteView.latitude)
        lng= data.getDouble(NoteView.longitude)
        Log.d("@@@2", "$lat $lng")
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }


    private var callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        val loc = LatLng(lat, lng)
        googleMap.addMarker(MarkerOptions().position(loc).title("Note created here"))
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 10.0f))

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        super.onViewCreated(view, savedInstanceState)

    }


    companion object {
        fun getInstance(val1: Double, val2: Double): MapsFragment {
            val mapsFragment = MapsFragment()
            val data= Bundle()
            data.putDouble(NoteView.latitude, val1)
            data.putDouble(NoteView.longitude, val2)
            mapsFragment.arguments=data
            return mapsFragment

        }
    }

}