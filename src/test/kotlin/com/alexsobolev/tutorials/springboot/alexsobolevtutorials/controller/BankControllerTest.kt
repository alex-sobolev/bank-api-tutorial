package com.alexsobolev.tutorials.springboot.alexsobolevtutorials.controller

import com.alexsobolev.tutorials.springboot.alexsobolevtutorials.model.Bank
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jsonMapper
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.*

@SpringBootTest
@AutoConfigureMockMvc
internal class BankControllerTest(
    @Autowired val mockMvc: MockMvc,
    @Autowired val objectMapper: ObjectMapper
) {
    val baseUrl = "/api/banks"

    @Nested
    @DisplayName("GET /api/banks")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetBanks {
        @Test
        fun `should return all banks`() {
            mockMvc.get(baseUrl).andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$[0].accountNumber") { value("12345") }
            }

        }
    }

    @Nested
    @DisplayName("GET /api/bank/{accountNumber}")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetBank {
        @Test
        fun `should return a bank by account number`() {
            val accountNumber = 12345

            mockMvc.get("$baseUrl/$accountNumber").andDo { print() }.andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$.trust") { value("3.14") }
                jsonPath("$.transactionFee") { value("1") }
            }
        }

        @Test
        fun `should return Not Found if account number doesn't exist`() {
            val accountNumber = "does_not_exist"

            mockMvc.get("$baseUrl/$accountNumber")
                .andDo { print() }
                .andExpect {
                    status { isNotFound() }
                }
        }
    }

    @Nested
    @DisplayName("POST /api/banks")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class PostNewBank {
        @Test
        fun `should add a new bank`() {
            val accountNumber = "acc1234";
            val trust = 31.415
            val transactionFee = 2
            val newBank = Bank(accountNumber, trust, transactionFee)

            mockMvc.post(baseUrl) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(newBank)
            }
                .andDo { print() }
                .andExpect {
                    status { isCreated() }
                    jsonPath("$.accountNumber") { value(accountNumber) }
                    jsonPath("$.trust") { value(trust) }
                    jsonPath("$.transactionFee") { value(transactionFee) }
                }

            mockMvc.get("$baseUrl/${newBank.accountNumber}")
                .andExpect {
                    status { isOk() }
                    jsonMapper { objectMapper.writeValueAsString(newBank) }
                }
        }

        @Test
        fun `should return Bad Request if a bank with a given account number already exists`() {
            val invalidBank = Bank("12345", 1.0, 1)

            mockMvc.post(baseUrl) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(invalidBank)
            }
                .andDo { print() }
                .andExpect {
                    status { isBadRequest() }
                }
        }
    }

    @Nested
    @DisplayName("PATCH /api/banks")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class UpdateBank {
        @Test
        fun `should update an existing bank`() {
            val accountNumber = "12345"
            val trust = 3.14
            val transactionFee = 2
            val updatedBank = Bank(accountNumber, trust, transactionFee)

            mockMvc.patch(baseUrl) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(updatedBank)
            }
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    jsonMapper { objectMapper.writeValueAsString(updatedBank) }
                }

            mockMvc.get("$baseUrl/${updatedBank.accountNumber}")
                .andExpect {
                    status { isOk() }
                    jsonMapper { objectMapper.writeValueAsString(updatedBank) }
                }
        }

        @Test
        fun `should return Bad Request when trying to update a bank that doesn't exist`() {
            val invalidBank = Bank("does_not_exist", 3.0, 1)

            mockMvc.patch(baseUrl) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(invalidBank)
            }
                .andDo { print() }
                .andExpect {
                    status { isBadRequest() }
                }

        }
    }

    @Nested
    @DisplayName("DELETE /api/banks/{accountNumber}")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class DeleteBank {
        @Test
        fun `should delete a bank`() {
            val accountNumber = "12345"
            val url = "$baseUrl/$accountNumber"

            mockMvc.delete(url)
                .andDo { print() }
                .andExpect {
                    status { isNoContent() }
                }

            mockMvc.get(url)
                .andDo { print() }
                .andExpect {
                    status { isNotFound() }
                }
        }

        @Test
        fun `should return Bad Request when trying to delete a bank that doesn't exist`() {
            val invalidAccountNumber = "does_not_exist"

            mockMvc.delete("$baseUrl/$invalidAccountNumber")
                .andDo { print() }
                .andExpect {
                    status { isBadRequest() }
                }
        }
    }
}
