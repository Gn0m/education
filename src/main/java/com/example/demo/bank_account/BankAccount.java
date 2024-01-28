package com.example.demo.bank_account;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class BankAccount {

    private int balance = 0;

    public synchronized void deposit(int i) {
        balance += i;
    }

    public synchronized void withdraw(int i) {
        if (balance < i) {
            log.info("Недостаточно баланса");
        } else {
            balance -= i;
        }

    }
}
