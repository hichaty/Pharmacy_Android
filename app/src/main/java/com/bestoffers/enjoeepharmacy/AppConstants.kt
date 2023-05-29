package com.bestoffers.enjoeepharmacy

class AppConstants {
    companion object {
        val PREF_USER_ID = "user_id"
        val PREF_IS_LOGGED_IN = "isLoggedIn"
        val PREF_FULL_NAME = "full_name"
        val PREF_EMAIL = "email"
        val PREF_IMAGE = "image"
        val PREF_MOB = "mob"
        val PREF_COUNTRY_CODE = "country_code"
        val PREF_TOKEN = "token"
        val PREF_USER_TYPE = "user_type"
        val PREF_USER_DATA = "user_data"
        val PREF_AUTH = "auth"
        val PREF_ACCESS_TOKEN = "access_token"
        val PREF_TOKEN_TYPE = "token_type"


        const val AUTHORIZATION = "Z2VvdGFndmFsaWR1c2VyOmdlb3RhZ3ZhbGlkdXNlckAxMjMhMTIz"
//        var BASE_DOMAIN = "https://appone.biz/enjoee-pharmacy"
        var BASE_DOMAIN = "https://apponedemo.top/enjoee-pharmacy"
        var BASE_URL = BASE_DOMAIN+ "/api/"

        val URL_SIGNUP = BASE_URL+"register"; //done
        val URL_LOGIN = BASE_URL+"login"; // done
        val URL_OTP_VERIFY = BASE_URL+"otp-verify";
        val URL_RESEND_VERIFY_OTP = BASE_URL+"resend-verify-otp";
        val URL_GET_PROFILE = BASE_URL+"profile"; // done
        val URL_FORGOT_PASSWORD = BASE_URL+"forgot-password";
        val URL_FORGOT_PASSWORD_VERIFY = BASE_URL+"forgot-password-verify";
        val URL_SET_PASSWORD = BASE_URL+"set-password"; // done
        val URL_UPDATE_PROFILE = BASE_URL+"update-profile"; // done
        val URL_CHANGE_PASSWORD = BASE_URL+"change-password"; //done
        val URL_GET_CATEGORIES = BASE_URL+"category";//done
        val URL_GET_PRODUCTS = BASE_URL+"product"; // done
//        val URL_GET_PRODUCTS_DETAILS = BASE_URL+"product/";
        val URL_RELATED_ID_PRODUCT = BASE_URL+"related-id-product";//done
        val URL_NEAREST_VENDOR = BASE_URL+"nearest-vendor"; // done
        val URL_VENDOR_PRODUCT = BASE_URL+"vendor-product";//done
        val URL_ADD_TO_CART = BASE_URL+"add-to-cart";
        val URL_UPDATE_CART = BASE_URL+"update-cart";
        val URL_GET_CART = BASE_URL+"get-cart";
        val URL_CART_REMOVE_PRODUCT = BASE_URL+"cart-remove-product";
        val URL_REMOVE_CART = BASE_URL+"remove-cart";
        val URL_ORDER = BASE_URL+"order";
        val URL_UPDATE_ORDER = BASE_URL+"update-order";
        val URL_GET_ORDER = BASE_URL+"get-order";

    }

}