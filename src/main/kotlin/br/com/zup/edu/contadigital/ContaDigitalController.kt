package br.com.zup.edu.contadigital

import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.http.client.exceptions.HttpClientException
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.validation.Validated
import javax.validation.Valid

@Validated
@Controller("/api/conta-digital")
class ContaDigitalController(
    val contaDigitalClient: ContaDigitalClient,
) {

    @Post("/credita")
    fun credita(@Valid @Body request: ContaDigitalRequest): HttpResponse<Any> {
        try {
            val resposta = contaDigitalClient.creditaNaConta(request)

            when(resposta.status) {
                HttpStatus.NOT_FOUND ->
                    return HttpResponse.notFound("Não foi possível encontrar a conta")
                HttpStatus.LOCKED ->
                    return HttpResponse.status<Any?>(HttpStatus.LOCKED)
                        .body("Este recurso já foi alterado.")
                else ->
                    return HttpResponse.ok()
            }
        } catch (e: HttpClientException) {
            e.printStackTrace()
            return HttpResponse.status<Any?>(HttpStatus.SERVICE_UNAVAILABLE)
                        .body("O sistema está fora do ar")
        }
    }

    @Post("/debita")
    fun debita(@Valid @Body request: ContaDigitalRequest): HttpResponse<Any> {
        try {
            val resposta = contaDigitalClient.debitaNaConta(request)

            when(resposta.status) {
                HttpStatus.NOT_FOUND ->
                    return HttpResponse.notFound("Não foi possível encontrar a conta")
                HttpStatus.LOCKED ->
                    return HttpResponse.status<Any?>(HttpStatus.LOCKED)
                        .body("Este recurso já foi alterado.")
                else ->
                    return HttpResponse.ok()
            }
        } catch (e: HttpClientResponseException) {
            return HttpResponse.status<Any?>(HttpStatus.UNPROCESSABLE_ENTITY)
                        .body("Saldo insuficiente.")
        }
        catch (e: HttpClientException) {
            e.printStackTrace()
            return HttpResponse.status<Any?>(HttpStatus.SERVICE_UNAVAILABLE)
                        .body("O sistema está fora do ar")
        }
    }

}