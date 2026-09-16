package org.example.steps

import org.example.api.booker.BookerRestService

class BookerApiSteps {

    val bookerService = BookerRestService()

    fun checkPingHealthCheck(){
        bookerService.pingBooking()
    }
}