package `is`.hi.darts2.service

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class LocationService(private val context: Context) {

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    /**
     * Gets the last known location of the device.
     *
     * @param onResult a callback that returns the Location, or null if not available.
     */
    @SuppressLint("MissingPermission")
    fun getLocation(onResult: (Location?) -> Unit) {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                onResult(location)
            }
            .addOnFailureListener { exception ->
                // Log exception if needed, then return null
                onResult(null)
            }
    }
}
