import org.example.steps.BookerApiSteps
import kotlin.test.Test

class BookerApiTest {

    val apiSteps = BookerApiSteps()



    @Test
    fun testHealthCheck(){
        apiSteps.checkPingHealthCheck()
    }

    @Test
    fun testCreateBook(){
        val bookingInstance = apiSteps.getBookingInstance()
        val response = apiSteps.createBooking(bookingInstance)

        val booking = apiSteps.getBookingById(response.bookingid)

        apiSteps.compareBooking(bookingInstance, booking)
    }

}