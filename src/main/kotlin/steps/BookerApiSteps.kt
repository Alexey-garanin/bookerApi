package org.example.steps

import org.example.api.booker.BookerRestService
import org.example.dataprovider.booker.BookerDataProvider
import org.example.dto.request.BookingRequest
import org.example.dto.response.BookingResponse
import org.assertj.core.api.Assertions.assertThat
import io.qameta.allure.Step
import io.restassured.response.Response
import org.example.assertions.expectStatus
import org.example.assertions.expectStatusAndParse
import org.example.assertions.expectStatusAndParseList
import org.example.dto.request.BookingPatchRequest
import org.example.dto.response.AuthResponse
import org.example.dto.response.BookingIdResponse
import java.time.LocalDate
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

class BookerApiSteps {

    val bookerService = BookerRestService()
    private var currentToken: String? = null

    fun getBookingInstance(): BookingRequest{
        return BookerDataProvider.createBookingInstance()
    }

    fun getPatchInstance(): BookingPatchRequest{
        return BookerDataProvider.createPatchRequest()
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

    @Step("Delete booking {id}")
    fun deleteBooking(id: Long) {
        val token = requireNotNull(currentToken) {
            "Token is not set"
        }
        bookerService.deleteBooking(id, token).expectStatus(201, "Delete booking")
    }

    @Step("Update booking {id}")
    fun updateBooking(id: Long, request: BookingRequest): BookingRequest {
        val token = requireNotNull(currentToken) {
            "Token is not set"
        }
        return bookerService.updateBooking(id, request, token).expectStatusAndParse(200, "Update booking")
    }

    @Step("Patch booking {id}")
    fun patchBooking(id: Long, request: BookingPatchRequest): BookingRequest {
        val token = requireNotNull(currentToken) {
            "Token is not set"
        }
        return bookerService.patchBooking(id, request, token).expectStatusAndParse(200, "Patch booking")
    }

    @Step("Get booking {id}")
    fun getBookingById(id: Long): BookingRequest {
        return bookerService.getBookingById(id).expectStatusAndParse(200, "Get booking")
    }

    @Step("Try to get booking {id}")
    fun verifyBooking(id: Long){
        bookerService.getBookingById(id).expectStatus(404, "Verify booking is not found")
    }

    @Step("Compare booking data")
    fun compareBooking(instance: BookingRequest, response: BookingRequest){
        assertThat(response)
           .describedAs("The booking body in the response does not match the request.")
           .isEqualTo(instance)
    }

    @Step("Compare booking data (partial update)")
    fun comparePatchedBooking(
        original: BookingResponse,
        patchRequest: BookingPatchRequest,
        response: BookingRequest
    ) {
        patchRequest.firstname?.let {
            assertThat(response.firstname)
                .describedAs("Firstname was not updated")
                .isEqualTo(it)
        }
        patchRequest.lastname?.let {
            assertThat(response.lastname)
                .describedAs("Lastname was not updated")
                .isEqualTo(it)
        }

        assertThat(response.totalprice)
            .describedAs("Totalprice should not change after PATCH")
            .isEqualTo(original.booking.totalprice)
        assertThat(response.depositpaid)
            .describedAs("Depositpaid should not change after PATCH")
            .isEqualTo(original.booking.depositpaid)
        assertThat(response.bookingdates)
            .describedAs("Bookingdates should not change after PATCH")
            .isEqualTo(original.booking.bookingdates)
        assertThat(response.additionalneeds)
            .describedAs("Additionalneeds should not change after PATCH")
            .isEqualTo(original.booking.additionalneeds)
    }

    @Step("Get booking IDs")
    fun getBookingIds(firstname: String? = null, lastname: String? = null, checkin: LocalDate? = null, checkout: LocalDate? = null): List<BookingIdResponse> {
        return bookerService.getBookingIds(firstname, lastname, checkin, checkout)
            .expectStatusAndParseList<List<BookingIdResponse>>(200, "Get booking IDs")
    }

    @Step("Verify booking {bookingId} exists in filtered list")
    fun verifyBookingExistsInList(bookings: List<BookingIdResponse>, bookingId: Long){
        assertThat(bookings).describedAs("Filtered list should contain booking with ID $bookingId")
            .contains(BookingIdResponse(bookingId))
    }


    @Step("Try to get auth token with wrong credentials")
    fun getWrongToken() {
        val response = bookerService.authBooking(BookerDataProvider.createAuthRequest(password = "wrong_password"))
        assertThat(response.jsonPath().getString("reason"))
            .describedAs("Error message for wrong password")
            .isEqualTo("Bad credentials")
    }

    @Step("Create booking with invalid empty body")
    fun createInvalidEmptyBooking(): Response {
        return bookerService.createBooking("{}").expectStatus(500, "Create booking with empty body")
    }

    @Step("Create booking without field: {fieldName}")
    fun createBookingWithoutField(fieldName: String): Response {
        val objectMapper = jacksonObjectMapper()
        val validInstance = getBookingInstance()
        val bodyMap = objectMapper.convertValue(validInstance, MutableMap::class.java)
        bodyMap.remove(fieldName)
        return bookerService.createBooking(bodyMap).expectStatus(500, "Create booking with invalid body")
    }
}
