package br.com.zup.edu.orquestrador

import io.micronaut.validation.validator.Validator
import java.math.BigDecimal
import javax.validation.ConstraintViolationException

enum class Operacao {

    DEBITO {
        override fun getUrl(): String {
            return "http://localhost:8080/api/conta-digital/debita"
        }

        override fun castPayload(payload: HashMap<String, Any>, validator: Validator): Orquestravel {
            val operacao = OperacaoRequest(
                numeroConta = payload["numeroConta"].toString(),
                valor = BigDecimal(payload["valor"].toString())
            )
            return validar(validator, operacao)
        }

    },

    CREDITO {
        override fun getUrl(): String {
            return "http://localhost:8080/api/conta-digital/credita"
        }

        override fun castPayload(payload: HashMap<String, Any>, validator: Validator): Orquestravel {
            val operacao = OperacaoRequest(
                numeroConta = payload["numeroConta"].toString(),
                valor = BigDecimal(payload["valor"].toString())
            )
            return validar(validator, operacao)
        }
    },

    RECARGA_CELULAR {
        override fun getUrl(): String {
            return "http://localhost:8082/api/recarga-celular"
        }

        override fun castPayload(payload: HashMap<String, Any>, validator: Validator): Orquestravel {
            val recargaCelular = RecargaCelularRequest(
                operadora = payload["operadora"].toString(),
                numeroCelular = payload["numeroCelular"].toString(),
                contaADebitar = payload["contaADebitar"].toString(),
                valorRecarga = payload["valorRecarga"] as Int
            )
            return validar(validator, recargaCelular)
        }

    };

    abstract fun getUrl() : String
    abstract fun castPayload(payload: HashMap<String, Any>, validator: Validator) : Orquestravel

    fun validar(validator: Validator, orquestravel: Orquestravel) : Orquestravel {
        validator.validate(orquestravel).let {
            if (it.isNotEmpty()) {
                throw ConstraintViolationException(it)
            }
            return orquestravel
        }
    }

}