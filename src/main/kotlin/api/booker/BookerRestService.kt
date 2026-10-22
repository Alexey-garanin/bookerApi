package org.example.api.booker

import io.restassured.http.Method
import org.example.api.BaseRestService
import org.example.dto.request.AuthRequest
import org.example.dto.request.BookingPatchRequest
import org.example.dto.request.BookingRequest
import java.time.LocalDate

class BookerRestService: BaseRestService() {
    companion object {
        const val BOOKING_ID = "/booking/{id}"
        const val PING = "/ping"
        const val CREATE_BOOKING = "/booking"
        const val AUTH = "/auth"
    }

    fun getBookingIds(
        firstname: String? = null,
        lastname: String? = null,
        checkin: LocalDate? = null,
        checkout: LocalDate? = null,
    ) = request {
        method(Method.GET)
        path("/booking")
        if (firstname != null) queryParam("firstname", firstname)
        if (lastname != null) queryParam("lastname", lastname)

        if (checkin != null) queryParam("checkin", checkin.toString())
        if (checkout != null) queryParam("checkout", checkout.toString())
    }.execute { it }

    fun getBookingById(id: Long) = request {
        method(Method.GET)
        path(BOOKING_ID)
        pathParam("id", id)
    }.execute { it }

    fun pingBooking() = request {
        method(Method.GET)
        path(PING)
    }.execute { it }

    fun createBooking(request: Any) = request {
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
        path(BOOKING_ID)
        pathParam("id", id)
        cookie("token", token)
    }. execute{ it }

    fun updateBooking(id: Long, request: BookingRequest, token: String) = request {
        method(Method.PUT)
        path(BOOKING_ID)
        pathParam("id", id)
        cookie("token", token)
        body(request)
    }.execute { it }

    fun patchBooking(id: Long, request: BookingPatchRequest, token: String) = request {
        method(Method.PATCH)
        path(BOOKING_ID)
        pathParam("id", id)
        cookie("token", token)
        body(request)
    }. execute { it }

}
