import org.example.steps.BookerApiSteps

import org.junit.jupiter.api.Test
import io.qameta.allure.Epic
import io.qameta.allure.Feature
import io.qameta.allure.restassured.AllureRestAssured
import io.restassured.RestAssured
import org.assertj.core.api.Assertions.assertThat
import org.example.assertions.expectStatus
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName

@Epic("Restful-Booker")
@Feature("API Tests")
class BookerApiTest {

companion object {
        private val apiSteps = BookerApiSteps()

        @JvmStatic
        @BeforeAll
        fun setupAllureFilters() {
            RestAssured.filters(AllureRestAssured())
        }
    }

    @BeforeEach
    fun setup() {
        apiSteps.getToken()
    }

    @Test
    @DisplayName("Service availability check (Health Check)")
    fun testHealthCheck(){
        apiSteps.checkPingHealthCheck()
    }

    @Test
    @DisplayName("Successful authorization returns valid access token")
    fun successfulLoginReturnsToken(){
        val token = apiSteps.getToken()
        assertThat(token).isNotEmpty()
    }

    @Test
    @DisplayName("Delete booking")
    fun deleteBooking() {
        val response = apiSteps.createBooking(apiSteps.getBookingInstance())
        apiSteps.deleteBooking(response.bookingid)
        apiSteps.verifyBooking(response.bookingid)
    }

    @Test
    @DisplayName("Create booking")
    fun testCreateBooking(){
        val response = apiSteps.createBooking(apiSteps.getBookingInstance())
        try {
            assertThat(response.bookingid).isGreaterThan(0)
        } finally {
            runCatching { apiSteps.deleteBooking(response.bookingid) }
        }
    }

    @Test
    @DisplayName("Get booking by id")
    fun testGetBookingByID(){
        val bookingInstance = apiSteps.getBookingInstance()
        val response = apiSteps.createBooking(bookingInstance)
        try {
            val booking = apiSteps.getBookingById(response.bookingid)
            apiSteps.compareBooking(bookingInstance, booking)
        } finally {
            runCatching { apiSteps.deleteBooking(response.bookingid) }
        }
    }

    @Test
    @DisplayName("Update booking successfully changes all fields")
    fun updateBooking(){
        val response = apiSteps.createBooking(apiSteps.getBookingInstance())
        val updateBookingInstance = apiSteps.getBookingInstance()
        try {
            val booking = apiSteps.updateBooking(response.bookingid, updateBookingInstance)
            apiSteps.compareBooking(updateBookingInstance, booking)
        } finally {
            runCatching { apiSteps.deleteBooking(response.bookingid) }
        }
    }

    @Test
    @DisplayName("Patch booking successfully")
    fun patchBookingSuccessfully(){
        val response = apiSteps.createBooking(apiSteps.getBookingInstance())
        val patchBookingInstance = apiSteps.getPatchInstance()
        try {
            apiSteps.patchBooking(response.bookingid, patchBookingInstance)
            val booking = apiSteps.getBookingById(response.bookingid)
            apiSteps.comparePatchedBooking(response, patchBookingInstance,booking )
        } finally {
            runCatching { apiSteps.deleteBooking(response.bookingid) }
        }
    }

    @Test
    @DisplayName("Get all bookings")
    fun getAllBookingsIds(){
        apiSteps.getBookingIds().isNotEmpty()
    }

    @Test
    @DisplayName("Filter bookings by full name")
    fun filterBookingByFullName(){
        val bookingInstance = apiSteps.getBookingInstance()
        val response = apiSteps.createBooking(bookingInstance)
        try {
        val filteredBookings = apiSteps.getBookingIds(bookingInstance.firstname, bookingInstance.lastname)
        apiSteps.verifyBookingExistsInList(filteredBookings, response.bookingid)
        } finally {
            runCatching { apiSteps.deleteBooking(response.bookingid) }
        }
    }

    @Test
    @DisplayName("Authorization with wrong password returns 403 Forbidden")
    fun authWithWrongPasswordReturns403() {
        apiSteps.getWrongToken()
    }

    @Test
    @DisplayName("DELETE invalid token returns 403")
    fun deleteWithoutTokenReturns403() {
        val booking = apiSteps.createBooking(apiSteps.getBookingInstance())
        try {
            apiSteps.bookerService.deleteBooking(booking.bookingid, "invalid_token")
                .expectStatus(403, "Delete invalid token")
        } finally {
            runCatching { apiSteps.deleteBooking(booking.bookingid) }
        }
    }

    @Test
    @DisplayName("PUT invalid token returns 403")
    fun updateWithoutTokenReturns403() {
        val booking = apiSteps.createBooking(apiSteps.getBookingInstance())
        try {
            val updateRequest = apiSteps.getBookingInstance()
            apiSteps.bookerService.updateBooking(booking.bookingid, updateRequest, "invalid_token")
                .expectStatus(403, "Update invalid token")
        } finally {
            runCatching { apiSteps.deleteBooking(booking.bookingid) }
        }
    }

    @Test
    @DisplayName("PATCH invalid token returns 403")
    fun patchWithoutTokenReturns403() {
        val booking = apiSteps.createBooking(apiSteps.getBookingInstance())
        try {
            val patchRequest = apiSteps.getPatchInstance()
            apiSteps.bookerService.patchBooking(booking.bookingid, patchRequest, "invalid_token")
                .expectStatus(403, "Patch invalid token")
        } finally {
            runCatching { apiSteps.deleteBooking(booking.bookingid) }
        }
    }


}