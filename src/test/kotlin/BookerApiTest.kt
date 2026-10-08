import org.example.steps.BookerApiSteps

import org.junit.jupiter.api.Test
import io.qameta.allure.Epic
import io.qameta.allure.Feature
import io.qameta.allure.restassured.AllureRestAssured
import io.restassured.RestAssured
import org.junit.jupiter.api.BeforeAll
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

    @Test
    @DisplayName("Service availability check (Health Check)")
    fun testHealthCheck(){
        apiSteps.checkPingHealthCheck()
    }

    @Test
    @DisplayName("Successful authorization returns valid access token")
    fun successfulLoginReturnsToken(){
        apiSteps.getToken()
    }

    @Test
    @DisplayName("Delete booking")
    fun deleteBooking() {
        val bookingInstance = apiSteps.getBookingInstance()
        val booking = apiSteps.createBooking(bookingInstance)
        apiSteps.getToken()
        apiSteps.deleteBooking(booking.bookingid)
        apiSteps.verifyBooking(booking.bookingid)
    }

    @Test
    @DisplayName("Create booking")
    fun testCreateBook(){
        val bookingInstance = apiSteps.getBookingInstance()
        val response = apiSteps.createBooking(bookingInstance)
        apiSteps.getToken()
        apiSteps.deleteBooking(response.bookingid)
    }

    @Test
    @DisplayName("Get booking by id")
    fun testGetBookingByID(){
        val bookingInstance = apiSteps.getBookingInstance()
        val response = apiSteps.createBooking(bookingInstance)
        val booking = apiSteps.getBookingById(response.bookingid)
        apiSteps.compareBooking(bookingInstance, booking)
        apiSteps.getToken()
        apiSteps.deleteBooking(response.bookingid)
    }

}