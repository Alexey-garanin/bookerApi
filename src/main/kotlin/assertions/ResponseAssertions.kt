package org.example.assertions

import io.restassured.response.Response
import org.assertj.core.api.Assertions.assertThat

fun Response.expectStatus(expected: Int, description: String = ""): Response {
    val desc = if (description.isNotBlank()) "$description: " else ""
    assertThat(this.statusCode)
        .describedAs("${desc}expected $expected, but got ${this.statusCode}")
        .isEqualTo(expected)
    return this
}

inline fun <reified T> Response.expectStatusAndParse(expectedStatus: Int, description: String = ""): T {
    this.expectStatus(expectedStatus, description)
    return this.`as`(T::class.java)
}