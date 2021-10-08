package br.com.zup.edu.recargacelular

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Post
import io.micronaut.http.client.annotation.Client

@Client("http://localhost:8083/api/recargaCelular")
interface RecargaCelularClient {

    @Post
    fun recarrega(@Body request: RecargaCelularRequest) : HttpResponse<Any>

}