package org.example.steps

import org.example.api.booker.BookerRestService
import org.example.dataprovider.booker.BookerDataProvider
import org.example.dto.request.BookingRequest
import org.example.dto.response.BookingResponse
import org.assertj.core.api.Assertions.assertThat
import io.qameta.allure.Step

class BookerApiSteps {

    val bookerService = BookerRestService()

    @Step("API functionality check (server ping)")
    fun checkPingHealthCheck(){
        bookerService.pingBooking()
    }

    fun getBookingInstance(): BookingRequest{
        return BookerDataProvider.createBookingInstance()
    }

    fun createBooking(request: BookingRequest): BookingResponse {
        return bookerService.createBooking(request)
    }

    fun getBookingById(id: Long): BookingRequest {
        return bookerService.getBookingById(id)
    }

    fun compareBooking(instance: BookingRequest, response: BookingRequest){
        assertThat(response)
           .describedAs("The booking body in the response does not match the request.")
           .isEqualTo(instance)
    }
}
