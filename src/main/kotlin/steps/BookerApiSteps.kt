package org.example.steps

import org.example.api.booker.BookerRestService
import org.example.dataprovider.booker.BookerDataProvider
import org.example.dto.request.BookingRequest
import org.example.dto.response.BookingResponse
import org.assertj.core.api.Assertions.assertThat
import io.qameta.allure.Step
import org.example.dto.response.AuthResponse

class BookerApiSteps: BaseApiSteps() {

    val bookerService = BookerRestService()
    private var currentToken: String? = null

    fun getBookingInstance(): BookingRequest{
        return BookerDataProvider.createBookingInstance()
    }

    @Step("Check API ping")
    fun checkPingHealthCheck(){
        bookerService.pingBooking().expectStatus(201, "Ping")
    }

    @Step("Create booking")
    fun createBooking(request: BookingRequest): BookingResponse {
        return bookerService.createBooking(request).expectStatusAndParse(200, "Create booking")
    }

    @Step("Get auth token")
    fun getToken(): String {
        if (currentToken != null) {
            return currentToken!!
        }

        val request = BookerDataProvider.createAuthRequest()
        val response = bookerService.authBooking(request).expectStatusAndParse<AuthResponse>(200, "Get token")

        currentToken = response.token
        return response.token
    }

    @Step("Delete booking")
    fun deleteBooking(id: Long) {
        val token = requireNotNull(currentToken) {
            "Token is not set"
        }
        bookerService.deleteBooking(id, token).expectStatus(201, "Delete booking")
    }


    @Step("Get booking by id")
    fun getBookingById(id: Long): BookingRequest {
        return bookerService.getBookingById(id).expectStatusAndParse(200, "Get booking")
    }

    @Step("Compare booking data")
    fun compareBooking(instance: BookingRequest, response: BookingRequest){
        assertThat(response)
           .describedAs("The booking body in the response does not match the request.")
           .isEqualTo(instance)
    }


}
