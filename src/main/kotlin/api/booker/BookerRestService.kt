package org.example.api.booker

import io.restassured.http.Method
import org.example.api.BaseRestService

class BookerRestService: BaseRestService() {
    companion object {
        const val GET_BOOKING = "/booking/{id}"
        const val PING = "/ping"
    }


    fun getBookingById(id: Long): Any = request {
        method(Method.GET)
        path(GET_BOOKING)
        pathParam("id", id)
    }.execute { }

    fun pingBooking() = request {
        method(Method.GET)
        path(PING)
    }.execute { response ->
        response.then().statusCode(201)
    }

}