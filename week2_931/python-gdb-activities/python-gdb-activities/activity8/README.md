# Activity 8: Testing Account Subclasses

## 📌 Context
Activity 8 executes a specialized test suite `test_account_subclasses.py` verifying polymorphic behavior, interest accrual calculations, overdraft line utilization, and exception boundaries across `SavingsAccount` and `CurrentAccount`.

## 💻 Code

```python
# gdb/tests/test_account_subclasses.py

from gdb.domain.savings_account import SavingsAccount
from gdb.domain.current_account import CurrentAccount
from gdb.exceptions.account_exception import AccountException

def main() -> None:
    print("============================================================")
    print("  TESTING SAVINGS AND CURRENT ACCOUNT SUBCLASSES")
    print("============================================================")

    # 1. Creating Accounts
    sa = SavingsAccount(1001, "John Doe", 25, 1000.0)
    ca = CurrentAccount(1002, "Jane Smith", 30, 2000.0)

    # 2. Interest Accrual
    sa.apply_monthly_interest()

    # 3. Overdraft Repayment
    ca.set_pin(1234)
    ca.withdraw(1500.0, 1234)
    ca.repay_overdraft(500.0)

    # 4. Polymorphic Iteration
    portfolio = [sa, ca]
    for acc in portfolio:
        print(f"Account #{acc.account_number} | {acc.get_account_type()} | Balance: Rs. {acc.balance}")

if __name__ == "__main__":
    main()
```

## 🎯 Key Focus
- **Polymorphism**: Storing diverse subclass objects (`SavingsAccount`, `CurrentAccount`) inside a single `list[Account]` collection.
- **Interest Accrual**: Verifying monthly compound interest formulas (`balance += balance * (4.0 / 100) / 12`).
- **Overdraft Validation**: Testing withdrawal limits using the Rs. 5000.0 overdraft buffer.

## 👨‍🏫 Trainer Key Points to Explain

### 1. Polymorphic Collection Processing
- **Iterating Superclass Lists**: Point out `portfolio = [sa, ca]`. Show how caller code iterates through the list calling `acc.get_account_type()` without needing `if/else` type checks.

### 2. Precise Financial Math
- **Interest Formula Breakdown**: Explain `(4.0 / 100) / 12`. Show how monthly compounding interest increases balance accurately over time.

### 3. Overdraft Repayment Verification
- **Tracking Overdraft State**: Walk through Test 3 in `test_account_subclasses.py`. Show how withdrawing Rs. 1500 from a Rs. 1000 balance uses Rs. 500 overdraft, and depositing Rs. 500 restores overdraft to 0.

### 4. Polymorphic Summary Calculations
- **Portfolio Aggregation**: Highlight computing `total_balance = sum(acc.balance for acc in portfolio)`. Show how polymorphism enables uniform aggregation across subclass instances.

## ❓ Questions to Ask Students
1. *What is polymorphism, and how does `portfolio = [sa, ca]` demonstrate it?*
   - **Answer Hint**: Polymorphism allows treating different subclass objects through a shared superclass reference (`Account`), invoking specialized subclass behavior automatically.
2. *How is monthly interest calculated for a `SavingsAccount` with Rs. 1300.0 balance at 4.0% p.a.?*
   - **Answer Hint**: Interest = `1300.0 * (4.0 / 100) / 12` = Rs. 4.33, bringing balance to Rs. 1304.33.
3. *What exception is thrown if `SavingsAccount` withdrawal attempts to breach Rs. 500.0 minimum balance?*
   - **Answer Hint**: `InsufficientBalanceException` detailing minimum balance requirement violation.
4. *Why doesn't `portfolio` iteration require checking `isinstance(acc, SavingsAccount)`?*
   - **Answer Hint**: Polymorphism ensures calling virtual methods dynamically dispatches to the correct subclass implementation.

## ⚠️ Top Mistakes While Coding

### 1. Using Hardcoded Type Checks inside Processing Loops
- **Mistake**: Writing `if type(acc) == SavingsAccount:` inside processing loops.
- **Why it breaks**: Violates polymorphism and Open-Closed Principle (OCP).
- **Fix**: Rely on polymorphic method overriding declared on the superclass interface.

### 2. Applying Annual Interest Rate Directly without Dividing by 12 Months
- **Mistake**: Writing `self._balance += self._balance * (4.0 / 100)` inside `apply_monthly_interest()`.
- **Why it breaks**: Credits a full year's interest every month instead of 1/12th of annual interest.
- **Fix**: Divide annual interest percentage by 12 (`(rate / 100) / 12`).

### 3. Deducting Overdraft Repayment from Balance instead of Crediting Balance
- **Mistake**: Decreasing balance when repaying overdraft.
- **Why it breaks**: Penalizes customer by deducting funds twice.
- **Fix**: Decrease `_overdraft_used` and credit remaining deposit to `_balance`.

### 4. Hardcoding Static Formatting Multipliers in Display Helpers
- **Mistake**: Formatting balances with hardcoded fixed offsets.
- **Why it breaks**: Produces misaligned output across different numeric values.
- **Fix**: Format strings using dynamic specifiers like `{acc.balance:.2f}`.

## 🧪 Test Code & Execution

### 🪟 Windows (PowerShell)
```powershell
cd C:\Users\DELL\Downloads\gdb-Activities\python-gdb-activities\activity8
$env:PYTHONPATH="."
python -m gdb.tests.test_account_subclasses
```

### 🪟 Windows (Command Prompt - CMD)
```cmd
cd C:\Users\DELL\Downloads\gdb-Activities\python-gdb-activities\activity8
set PYTHONPATH=.
python -m gdb.tests.test_account_subclasses
```

### 🐧 Linux & 🍎 macOS (Bash / Zsh)
```bash
cd python-gdb-activities/activity8
PYTHONPATH=. python3 -m gdb.tests.test_account_subclasses
```
