package org.example.api

import io.restassured.RestAssured
import io.restassured.http.ContentType
import io.restassured.http.Method
import io.restassured.response.Response
import io.restassured.specification.RequestSpecification
import org.slf4j.LoggerFactory


abstract class BaseRestService {

    protected val baseUrl: String = "https://restful-booker.herokuapp.com"

    protected fun request(block: RequestExecutor.() -> Unit): RequestExecutor {
        val spec = RestAssured.given()
            .baseUri(baseUrl)

        val executor = RequestExecutor(spec)
        executor.block()
        return executor
    }
}

class RequestExecutor(private val spec: RequestSpecification) {

    private val logger = LoggerFactory.getLogger(RequestExecutor::class.java)

    private var method: Method = Method.GET
    private var path: String = "/"

    fun method(method: Method) {
        this.method = method
    }

    fun path(path: String) {
        this.path = path
    }

    fun pathParam(name: String, value: Any) {
        spec.pathParam(name, value)
    }

    fun body(body: Any) {
        spec.contentType(ContentType.JSON)
        spec.body(body)
    }

    fun header(name: String, value: String) {
        spec.header(name, value)
    }

    fun execute(): Response {
        logger.info("→ REQUEST: {} {}", method, path)
        spec.log().all()

        val response = spec.`when`().request(method, path).thenReturn()


        logger.info("← RESPONSE: {}", response.statusCode)
        response.then().log().all()

        return response
    }

    fun <T> execute(block: (Response) -> T): T {
        val response = execute()
        return block(response)
    }
}