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

    val apiSteps = BookerApiSteps()

    companion object {
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
    @DisplayName("Create booking")
    fun testCreateBook(){
        val bookingInstance = apiSteps.getBookingInstance()
        val response = apiSteps.createBooking(bookingInstance)
        val booking = apiSteps.getBookingById(response.bookingid)
        apiSteps.compareBooking(bookingInstance, booking)
        apiSteps.getToken()
        apiSteps.deleteBooking(response.bookingid)
    }


}