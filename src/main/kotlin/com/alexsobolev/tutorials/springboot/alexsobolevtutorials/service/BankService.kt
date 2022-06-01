package com.alexsobolev.tutorials.springboot.alexsobolevtutorials.service

import com.alexsobolev.tutorials.springboot.alexsobolevtutorials.datasource.BankDataSource
import com.alexsobolev.tutorials.springboot.alexsobolevtutorials.model.Bank
import org.springframework.stereotype.Service

@Service
class BankService(private val dataSource: BankDataSource) {
    fun getBanks(): Collection<Bank> = dataSource.retrieveBanks()
    fun getBank(accountNumber: String): Bank = dataSource.retrieveBank(accountNumber)
    fun addBank(bank: Bank): Bank = dataSource.createBank(bank)
    fun updateBank(bank: Bank): Bank = dataSource.patchBank(bank)
}
