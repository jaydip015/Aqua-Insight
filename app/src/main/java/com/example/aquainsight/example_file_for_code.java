package com.example.aquainsight;

public class example_file_for_code {
    /**
     * new Handler().postDelayed(new Runnable() {
     *             @Override
     *             public void run() {
     *                 googleMap.addMarker(new MarkerOptions().position(new LatLng(1,1)).title("one"));
     *                 googleMap.addMarker(new MarkerOptions().position(new LatLng(2,2)).title("two"));
     *                 googleMap.addMarker(new MarkerOptions().position(new LatLng(3,3)).title("three"));
     *                 googleMap.addMarker(new MarkerOptions().position(new LatLng(4,4)).title("four"));
     *                 googleMap.addMarker(new MarkerOptions().position(new LatLng(5,5)).title("five"));
     *                 googleMap.addMarker(new MarkerOptions().position(new LatLng(6,6)).title("six"));
     *                 googleMap.addMarker(new MarkerOptions().position(new LatLng(7,7)).title("seven"));
     *
     * //googleMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(5,5)));
     * //googleMap.animateCamera(CameraUpdateFactory.zoomIn());
     *             }
     *         },2500);
     */

//other function
/**
 * // Set the map coordinates to Kyoto Japan.
 *         LatLng kyoto = new LatLng(35.00116, 135.7681);
 *         // Set the map type to Hybrid.
 *         googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
 *         // Add a marker on the map coordinates.
 *         googleMap.addMarker(new MarkerOptions()
 *                 .position(kyoto)
 *                 .title("Kyoto"));
 *         // Move the camera to the map coordinates and zoom in closer.
 *         googleMap.moveCamera(CameraUpdateFactory.newLatLng(kyoto));
 *         googleMap.moveCamera(CameraUpdateFactory.zoomTo(15));
 *         // Display traffic.
 *         googleMap.setTrafficEnabled(true);
 */

//runtime
/**
 * GoogleMapOptions options = new GoogleMapOptions();
 * options.mapType(GoogleMap.MAP_TYPE_SATELLITE)
 *     .compassEnabled(false)
 *     .rotateGesturesEnabled(false)
 *     .tiltGesturesEnabled(false);
 *
 *     static
 *     map:cameraBearing="112.5"
 *   map:cameraTargetLat="-33.796923"
 *   map:cameraTargetLng="150.922433"
 *   map:cameraTilt="30"
 *   map:cameraZoom="13"
 *   map:mapType="normal"
 *   map:uiCompass="false"
 *   map:uiRotateGestures="true"
 *   map:uiScrollGestures="false"
 *   map:uiTiltGestures="true"
 *   map:uiZoomControls="false"
 *   map:uiZoomGestures="true"
 *
 *   plan for the app
 *   get location of the user
 *   or get the location of the user selected on the map
 *   search any place and get the lat lon
 *
 *    Address[addressLines=[0:"LALBHAI DALPATBHAI COLLEGE OF ENGINEERING, 120, Circular Road, University Area, Ahmedabad, Gujarat 380015, India"],
 *    feature=LALBHAI DALPATBHAI COLLEGE OF ENGINEERING,
 *    admin=Gujarat,
 *    sub-admin=null,
 *    locality=Ahmedabad,
 *    thoroughfare=Circular Road,
 *    postalCode=380015,
 *    countryCode=IN,
 *    countryName=India,
 *    hasLatitude=true,
 *    latitude=23.0338,
 *    hasLongitude=true,
 *    longitude=72.546584,
 *    phone=null,
 *    url=null,
 *    extras=null]
 */
}
