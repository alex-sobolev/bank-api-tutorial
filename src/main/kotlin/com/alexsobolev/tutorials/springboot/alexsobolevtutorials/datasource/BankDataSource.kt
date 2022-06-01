package com.alexsobolev.tutorials.springboot.alexsobolevtutorials.datasource

import com.alexsobolev.tutorials.springboot.alexsobolevtutorials.model.Bank

interface BankDataSource {
    fun retrieveBanks(): Collection<Bank>

    fun retrieveBank(accountNumber: String): Bank

    fun createBank(bank: Bank): Bank

    fun patchBank(updatedBank: Bank): Bank
}
