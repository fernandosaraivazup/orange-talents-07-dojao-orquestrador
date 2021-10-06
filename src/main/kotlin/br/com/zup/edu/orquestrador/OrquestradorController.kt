package br.com.zup.edu.orquestrador

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.validation.validator.Validator

@Controller("/api")
class OrquestradorController(
    val client: HttpClient,
    val validator: Validator
) {

    @Post
    fun orquestra(@Body request: OrquestradorRequest): HttpResponse<Any> {

        try {

            val httpRequest = request.generateHttpRequest(validator)

            return client.toBlocking().exchange(httpRequest, Any::class.java)
        }
        catch (e: HttpClientResponseException) {

            println(e.cause)
            return HttpResponse.status<Any?>(e.status).body(e.response)

        }

    }

}