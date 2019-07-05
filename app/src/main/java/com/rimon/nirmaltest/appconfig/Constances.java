package com.rimon.nirmaltest.appconfig;

public class Constances {


    //Change the url depending on the name of your web hosting
   // public static String BASE_URL = "https://droideve.com/smartgeostore";
    public static String BASE_URL = AppConfig.BASE_URL;
    public static String API_KEY = AppConfig.API_KEY;
    public static String API_FORECAST_COUNT = AppConfig.API_KEY;

    //CHAT CONFIG SERVER
    public static String SERVER_ADDRESS_IP;
    public static String SOCKET_SERVER_VERSION;


    //Set the link to the app store account
    //public static String PLAY_STORE_URL = AppConfig.PLAY_STORE_URL;
    //Your AdMob Banner Unit ID
    //public static final String BANNER_UNIT_ID = Config.BANNER_UNIT_ID;
    //public static final String ADS_UNIT_ID = Config.ADS_UNIT_ID;
    //Your maps api key
    //public static final String MAPS_API_ID = Config.MAPS_API_ID;
    //You Can download other fonts , place it in the app and change the variables
    public static class Fonts{
        public final static String BOLD = "fonts/OpenSans-Bold.ttf";
        public final static String REGULAR = "fonts/OpenSans-Regular.ttf";
    }

    //WARNING :  DO NOT EDIT THIS
    public static class API{

        private static String API_VERSION = "1.0";
        //store API's
        public static final String API_GET_WEATHER = "forecast.json";

    }




}
