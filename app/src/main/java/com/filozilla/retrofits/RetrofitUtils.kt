package com.filozilla.retrofits

import com.filozilla.interfaces.RetrofitDaoInterface

class RetrofitUtils {

    companion object {
        val BASE_URL = "https://filozilla.com/webservices/"

        fun getRetrofitDaoInterface(): RetrofitDaoInterface {
            return RetrofitBuilder.getClient(baseUrl = BASE_URL)
                .create(RetrofitDaoInterface::class.java)
        }
    }

}