package org.example.api.booker

import io.restassured.http.Method
import org.example.api.BaseRestService
import org.example.dto.request.BookingRequest
import org.example.dto.response.BookingResponse

class BookerRestService: BaseRestService() {
    companion object {
        const val GET_BOOKING = "/booking/{id}"
        const val PING = "/ping"
        const val CREATE_BOOKING = "/booking"
    }


    fun getBookingById(id: Long): BookingRequest = request {
        method(Method.GET)
        path(GET_BOOKING)
        pathParam("id", id)
    }.execute { it.jsonPath().getObject("", BookingRequest::class.java) }

    fun pingBooking() = request {
        method(Method.GET)
        path(PING)
    }.execute { response ->
        response.then().statusCode(201)
    }

    fun createBooking(request: BookingRequest): BookingResponse = request {
        method(Method.POST)
        path(CREATE_BOOKING)
        body(request)
    }.execute { response ->
        check(response.statusCode == 200) {
            "Expected status code 200, but got ${response.statusCode}"
        }
        response.jsonPath().getObject("", BookingResponse::class.java)
    }
}
