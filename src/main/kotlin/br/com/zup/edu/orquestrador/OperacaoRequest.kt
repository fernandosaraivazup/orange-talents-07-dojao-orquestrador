package br.com.zup.edu.orquestrador

import io.micronaut.core.annotation.Introspected
import java.math.BigDecimal
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Positive

@Introspected
data class OperacaoRequest(
    @field:NotBlank
    val numeroConta: String,
    @field:Positive
    @field:NotNull
    val valor: BigDecimal
) : Orquestravel
