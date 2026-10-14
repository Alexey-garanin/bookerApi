import org.example.steps.BookerApiSteps

import org.junit.jupiter.api.Test
import io.qameta.allure.Epic
import io.qameta.allure.Feature
import io.qameta.allure.restassured.AllureRestAssured
import io.restassured.RestAssured
import org.assertj.core.api.Assertions.assertThat
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

}