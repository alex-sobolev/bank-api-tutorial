package com.alexsobolev.tutorials.springboot.alexsobolevtutorials

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BankApiTutorialApplication

fun main(args: Array<String>) {
    runApplication<BankApiTutorialApplication>(*args)
}
