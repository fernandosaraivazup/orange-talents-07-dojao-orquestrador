package br.com.zup.edu.recargacelular

import br.com.zup.edu.contadigital.ContaDigitalClient
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.http.client.exceptions.HttpClientException
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.validation.Validated
import org.slf4j.LoggerFactory
import javax.validation.Valid

@Controller
@Validated
class RecargaCelularController(
    val contaClient: ContaDigitalClient,
    val recargaClient: RecargaCelularClient
) {

    val logger = LoggerFactory.getLogger(this.javaClass)

    var debitou = false
    var creditou = false

    @Post("/api/recargaCelular")
    fun recarrega(@Valid request: SolicitacaoRecargaRequest): HttpResponse<Any> {
        val contaRequest = request.toContaDigitalRequest()
        try {
            val possivelDebito = contaClient.debitaNaConta(contaRequest)

            if (possivelDebito.status != HttpStatus.OK) {
                when (possivelDebito.status) {
                    HttpStatus.NOT_FOUND -> return HttpResponse.notFound("Não foi possível encontrar a conta")
                    HttpStatus.LOCKED ->
                        return HttpResponse.status<Any?>(HttpStatus.LOCKED).body("Este recurso já foi alterado.")
                    else ->
                        return HttpResponse.badRequest()
                }
            }
            logger.info("Conta encontrada com sucesso")
            debitou = true

            val possivelRecarga = recargaClient.recarrega(request.toRecargaRequest())

            logger.info("Recarga realizada com sucesso")

            return HttpResponse.ok()


        } catch (e: HttpClientResponseException) {
            logger.error("Saldo insuficiente")
            return HttpResponse.status<Any?>(HttpStatus.UNPROCESSABLE_ENTITY).body("Saldo insuficiente.")
        } catch (e: HttpClientException) {
            var sistemaOffline = ""
            val msg = "está fora do ar"
            when {
                e.message!!.contains("8080") -> {
                    sistemaOffline = "Sistema de conta digital"
                    logger.error("$sistemaOffline $msg")
                }
                e.message!!.contains("8083") -> {
                    logger.warn("Debitou? $debitou")
                    sistemaOffline = "Sistema de recarga"
                    logger.error("$sistemaOffline $msg")
                    contaClient.creditaNaConta(contaRequest)
                    creditou = true
                    logger.warn("Creditou? $creditou")
                }
            }

            return HttpResponse.status<Any?>(HttpStatus.SERVICE_UNAVAILABLE)
                .body("$sistemaOffline $msg")
        }

    }

}