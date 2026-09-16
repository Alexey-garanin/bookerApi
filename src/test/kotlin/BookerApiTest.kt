import org.example.api.booker.BookerRestService
import org.example.steps.BookerApiSteps
import kotlin.test.Test

class BookerApiTest {

    val apiSteps = BookerApiSteps()



    @Test
    fun testHealthCheck(){
        apiSteps.checkPingHealthCheck()
    }

}