package org.example.api.booker

import io.restassured.http.Method
import org.example.api.BaseRestService
import org.example.dto.request.AuthRequest
import org.example.dto.request.BookingRequest

class BookerRestService: BaseRestService() {
    companion object {
        const val GET_BOOKING = "/booking/{id}"
        const val PING = "/ping"
        const val CREATE_BOOKING = "/booking"
        const val AUTH = "/auth"
    }

    fun getBookingById(id: Long) = request {
        method(Method.GET)
        path(GET_BOOKING)
        pathParam("id", id)
    }.execute { it }

    fun pingBooking() = request {
        method(Method.GET)
        path(PING)
    }.execute { it }

    fun createBooking(request: BookingRequest) = request {
        method(Method.POST)
        path(CREATE_BOOKING)
        body(request)
    }.execute { it }

    fun authBooking(request: AuthRequest) = request {
        method(Method.POST)
        path(AUTH)
        body(request)
    }.execute{ it }

    fun deleteBooking(id: Long, token: String) = request {
        method(Method.DELETE)
        path(GET_BOOKING)
        pathParam("id", id)
        cookie("token", token)
    }. execute()

}
