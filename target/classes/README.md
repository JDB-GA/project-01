# What are the goals?
- Design a file database for ACME Bank.
- Write the entire Java program using OOP principles.
- Include file handling, exception handling, unit testing, and more.

# Technical Requirements:
- Concrete classes
- Abstract classes
- Interfaces
- File handling
- Exception handling
- Lambda expressions and Optionals
- Unit testing

# Functional Requirements
- Login
- Recognize the role (Banker/ Customer)
- Add a new customer
- Assign accounts (checking, savings) to the customer.
- Customers should be able to reset their password.
- Account Transactions (requires login & customer must choose the account)
    - Withdraw Money
    - Deposit Money
    - Transfer Money (can be to own accounts or other bank accounts)
- Overdraft Protection (requires login)
    - Charge an ACME overdraft protection fee of $35 when overdrafting.
    - Prevent withdrawing more than $100 if the account balance is negative.
    - Deactivate the account after 2 overdrafts; reactivate if the customer resolves the negative balance and pays the overdraft fees.
    - Example 1: account balance 50, user withdraws 100, account now is -50 and counts -35 fees, overdraft count: 1
    - Example 2: account balance -85, user withdraws 20, account now is -115 and counts overdraft fees → -150, overdraft count: 2 & disable account.



# References
- [DBDiagram](https://dbdiagram.io/d/6a9f993e28e65f9ec245dd5e)
- [Trello](https://trello.com/b/JuK0qiqf/project-01-jdb)
- [Logger Used](https://www.slf4j.org/manual.html)
- [Bcrypt](https://mvnrepository.com/tags/bcrypt)