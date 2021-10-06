package br.com.zup.edu.orquestrador

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Positive

data class RecargaCelularRequest(
    @field:NotBlank val operadora: String,
    @field:NotBlank val numeroCelular: String,
    @field:NotBlank val contaADebitar: String,
    @field:NotNull @field:Positive val valorRecarga: Int
) : Orquestravel