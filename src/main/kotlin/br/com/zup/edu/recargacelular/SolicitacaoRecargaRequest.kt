package br.com.zup.edu.recargacelular

import br.com.zup.edu.contadigital.ContaDigitalRequest
import io.micronaut.core.annotation.Introspected
import java.math.BigDecimal
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Positive

@Introspected
data class SolicitacaoRecargaRequest(
    @field:NotBlank val numeroConta: String,
    @field:NotNull val operadora: Operadora,
    @field:NotBlank @field:Pattern(regexp = "[(]?[1-9][0-9][)]?9[0-9]{4}[-]?[0-9]{4}") val numeroTelefone: String,
    @field:NotNull @field:Positive val valor: BigDecimal
) {

    fun toContaDigitalRequest() : ContaDigitalRequest {
        return ContaDigitalRequest(numeroConta, valor)
    }

    fun toRecargaRequest() : RecargaCelularRequest {
        return RecargaCelularRequest(operadora, numeroTelefone, valor)
    }

}