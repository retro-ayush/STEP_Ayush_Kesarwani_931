"""Test Driver for Activity 8."""

from gdb.domain.account import Account
from gdb.domain.savings_account import SavingsAccount
from gdb.domain.current_account import CurrentAccount
from gdb.exceptions.account_exception import AccountException

def print_account(acc: Account) -> None:
    print(f"Account #{acc.account_number} | {acc.account_holder_name} ({acc.age} yrs) | {acc.get_account_type()} | Rs. {acc.balance:.2f} | {acc.status} | PIN: {'Yes' if acc.has_pin() else 'No'}")

def print_exception(e: Exception) -> None:
    print(f"EXCEPTION: {e}")

def main() -> None:
    print("============================================================")
    print("  TESTING SAVINGS AND CURRENT ACCOUNT SUBCLASSES")
    print("============================================================")

    try:
        print("\n>>> 1. Creating Accounts")
        sa = SavingsAccount(1001, "John Doe", 25, 1000.0)
        ca = CurrentAccount(1002, "Jane Smith", 30, 2000.0)

        print_account(sa)
        print_account(ca)

        print("\n>>> 2. Savings Account Operations")
        sa.set_pin(1234)
        sa.deposit(500.0)
        print(f"Deposited Rs. 500.0 into Savings. Balance: Rs. {sa.balance}")
        sa.withdraw(200.0, 1234)
        print(f"Withdrew Rs. 200.0 from Savings. Balance: Rs. {sa.balance}")
        sa.apply_monthly_interest()
        print(f"Applied monthly interest (4.0% p.a.). New Balance: Rs. {sa.balance:.2f}")

        print("\n>>> 3. Current Account Overdraft Operations")
        ca.set_pin(1234)
        print(f"Current Account Balance: Rs. {ca.balance} | Available Overdraft: Rs. {ca.available_overdraft}")
        ca.withdraw(1500.0, 1234)
        print(f"Withdrew Rs. 1500.0 (used overdraft). Balance: Rs. {ca.balance} | Overdraft Used: Rs. {ca.overdraft_used}")
        ca.repay_overdraft(500.0)
        print(f"Repaid Rs. 500.0 overdraft. Balance: Rs. {ca.balance} | Overdraft Used: Rs. {ca.overdraft_used}")

        print("\n>>> 4. Polymorphic Iteration")
        accounts: list[Account] = [sa, ca]
        total_balance = 0.0
        for acc in accounts:
            print_account(acc)
            total_balance += acc.balance
        print(f"Total Portfolio Balance: Rs. {total_balance:.2f}")

        print("\n>>> 5. Testing Exceptions")
        print("Attempting Savings withdrawal below minimum balance:")
        try: sa.withdraw(1000.0, 1234)
        except AccountException as e: print_exception(e)

        print("Attempting Current withdrawal exceeding overdraft limit:")
        try: ca.withdraw(10000.0, 1234)
        except AccountException as e: print_exception(e)

    except AccountException as e:
        print_exception(e)

    print("\n============================================================")
    print("  SUBCLASSES TEST COMPLETED!")
    print("============================================================")

if __name__ == "__main__":
    main()
