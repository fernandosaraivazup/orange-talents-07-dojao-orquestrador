package br.com.zup.edu.orquestrador

import io.micronaut.core.annotation.Introspected
import io.micronaut.http.HttpRequest
import io.micronaut.validation.validator.Validator

@Introspected
data class OrquestradorRequest(
    val operacao: Operacao,
    val payload: HashMap<String, Any>
) {
    fun generateHttpRequest(validator: Validator): HttpRequest<Any>? {

        return HttpRequest.POST(operacao.getUrl(), operacao.castPayload(payload, validator))

    }
}