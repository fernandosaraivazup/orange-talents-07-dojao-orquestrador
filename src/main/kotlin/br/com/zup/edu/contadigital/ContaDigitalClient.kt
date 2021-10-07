package br.com.zup.edu.contadigital

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Post
import io.micronaut.http.client.annotation.Client

@Client("http://localhost:8080/api/conta-digital")
interface ContaDigitalClient {

    @Post("/credita")
    fun creditaNaConta(@Body request: ContaDigitalRequest) : HttpResponse<Any>

    @Post("/debita")
    fun debitaNaConta(@Body request: ContaDigitalRequest): HttpResponse<Any>
}