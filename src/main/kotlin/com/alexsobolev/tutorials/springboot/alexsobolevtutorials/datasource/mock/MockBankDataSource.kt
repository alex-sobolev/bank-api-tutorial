package com.alexsobolev.tutorials.springboot.alexsobolevtutorials.datasource.mock

import com.alexsobolev.tutorials.springboot.alexsobolevtutorials.datasource.BankDataSource
import com.alexsobolev.tutorials.springboot.alexsobolevtutorials.model.Bank
import org.springframework.stereotype.Repository

@Repository
class MockBankDataSource : BankDataSource {
    val banks = mutableListOf(
        Bank(accountNumber = "12345", trust = 3.14, transactionFee = 1),
        Bank(accountNumber = "10101", trust = 17.0, transactionFee = 2),
        Bank(accountNumber = "123456", trust = 11.0, transactionFee = 3),
    )

    override fun retrieveBanks(): Collection<Bank> = banks

    override fun retrieveBank(accountNumber: String): Bank =
        banks.firstOrNull { bank: Bank -> bank.accountNumber == accountNumber }
            ?: throw NoSuchElementException("No banks found with account: $accountNumber")

    override fun createBank(bank: Bank): Bank {
        if (banks.any { it.accountNumber == bank.accountNumber }) {
            throw IllegalArgumentException("Bank with account number ${bank.accountNumber} already exists")
        }

        banks.add(bank)

        return bank
    }

    override fun patchBank(updatedBank: Bank): Bank {
        val bankToUpdate = banks.firstOrNull { it.accountNumber == updatedBank.accountNumber }
            ?: throw IllegalArgumentException("Bank with account number ${updatedBank.accountNumber} doesn't exist")

        banks.remove(bankToUpdate)
        banks.add(updatedBank)

        return updatedBank
    }

    override fun deleteBank(accountNumber: String) {
        val bankToDelete = banks.firstOrNull { it.accountNumber == accountNumber }
            ?: throw IllegalArgumentException("Bank with account number $accountNumber doesn't exist")

        banks.remove(bankToDelete)
    }
}
