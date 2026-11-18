package com.minibank.account;

import jakarta.persistence.*;
import java.math.BigDecimal;
import  java.time.Instant;

@Entity // links this class to db/table
@Table (name = "accounts") // the table name it defaults to class name if we dont add it
public class Account {

    @Id // marks what below as the prim key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // increment method for the key
    private long id;

    // @column can be used to link fields if names below dosnt match column names
    // unless its the camel case format vs the underscore format in db 
    // for ex.. original table name is account_number but here we are using accountNumber
    // only in this case of fromat mismatch springboot autmoatically match them
    private String accountNumber;
    private String ownerUsername;
    private String currency;
    private BigDecimal balance;


    // store the enums as they are insted if the default JPA 
    // which stores them as index like orders numbers by defult 
    // starting from 0 as the first value of the enum
    @Enumerated(EnumType.STRING)
    private AccountStatus status; 

    // optimistic locking, Hibernate manages this
    // prevents race of conditions
    @Version 
    private Long version; 

    private Instant createdAt;

    // from what i understood this is used for fetching and reading data
    // JPA dosnt know whats in db so it works as it goes when reading/fetchin hence the blank
    protected Account() {
         // JPA needs a no-args constructor; protected so our code can't use it
    }

    public Account(String accountNumber, String ownerUsername, String currency) {
        this.accountNumber = accountNumber;
        this.ownerUsername = ownerUsername;
        this.currency = currency;
        this.balance = BigDecimal.ZERO;
        this.status = AccountStatus.ACTIVE;
        this.createdAt = Instant.now();
    }

    public void deposit(BigDecimal amount) {
        requireActive();
        if (amount.signum() <= 0) { // signum() returns -1, 0 or 1, which is a clean way to check for a positive amount.
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
        balance = balance.add(amount);
    }

    public void withdraw(BigDecimal amount) {
        requireActive();
        if (amount.signum() <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }
        if (balance.compareTo(amount) < 0) {
            throw new IllegalStateException("Insufficient funds");
        }
        balance = balance.subtract(amount);
    }

    private void requireActive() {
        if (status != AccountStatus.ACTIVE) {
            throw new IllegalStateException("Account is " + status);
        }
    }


    public Long getId() { return id; }
    public String getAccountNumber() { return accountNumber; }
    public String getOwnerUsername() { return ownerUsername; }
    public String getCurrency() { return currency; }
    public BigDecimal getBalance() { return balance; }
    public AccountStatus getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
}

