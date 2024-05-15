package com.nurdev.weatherapp.utils

import com.nurdev.weatherapp.data.ApiInterface
import com.nurdev.weatherapp.data.local.City
import com.nurdev.weatherapp.data.models.CurrentWeather
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstanceTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var myApi: ApiInterface

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        myApi = retrofit.create(ApiInterface::class.java)
    }

    @Test
    suspend fun testApiRequest() {
        val response = MockResponse()
            .setResponseCode(200)
            .setBody(
                "{\n" +
                        "    \"coord\": {\n" +
                        "        \"lon\": 74.59,\n" +
                        "        \"lat\": 42.87\n" +
                        "    },\n" +
                        "    \"weather\": [\n" +
                        "        {\n" +
                        "            \"id\": 804,\n" +
                        "            \"main\": \"Clouds\",\n" +
                        "            \"description\": \"overcast clouds\",\n" +
                        "            \"icon\": \"04n\"\n" +
                        "        }\n" +
                        "    ],\n" +
                        "    \"base\": \"stations\",\n" +
                        "    \"main\": {\n" +
                        "        \"temp\": 7.11,\n" +
                        "        \"feels_like\": 4.69,\n" +
                        "        \"temp_min\": 7.11,\n" +
                        "        \"temp_max\": 7.11,\n" +
                        "        \"pressure\": 1024,\n" +
                        "        \"humidity\": 79,\n" +
                        "        \"sea_level\": 1024,\n" +
                        "        \"grnd_level\": 922\n" +
                        "    },\n" +
                        "    \"visibility\": 10000,\n" +
                        "    \"wind\": {\n" +
                        "        \"speed\": 3.57,\n" +
                        "        \"deg\": 349,\n" +
                        "        \"gust\": 4.6\n" +
                        "    },\n" +
                        "    \"clouds\": {\n" +
                        "        \"all\": 100\n" +
                        "    },\n" +
                        "    \"dt\": 1711643164,\n" +
                        "    \"sys\": {\n" +
                        "        \"type\": 1,\n" +
                        "        \"id\": 8871,\n" +
                        "        \"country\": \"KG\",\n" +
                        "        \"sunrise\": 1711587034,\n" +
                        "        \"sunset\": 1711632152\n" +
                        "    },\n" +
                        "    \"timezone\": 21600,\n" +
                        "    \"id\": 1528675,\n" +
                        "    \"name\": \"Bishkek\",\n" +
                        "    \"cod\": 200\n" +
                        "}"
            )
        mockWebServer.enqueue(response)

        val message = myApi.getCurrentWeather("Bishkek", "metric", Util.API_KEY)
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }
}