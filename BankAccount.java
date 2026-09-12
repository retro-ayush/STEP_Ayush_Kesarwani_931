import java.util.ArrayList;
import java.util.List;

/**
 * GreenLeaf Bank — Legacy BankAccount class
 *
 * This class is intentionally messy.
 * It mixes account state, validation, persistence, notification,
 * statement formatting, and interest calculation all in one place.
 * Refactor this across by implementing the lab tasks onward.
 */

/* =====================================================================
 * SRP AUDIT — EVERY DISTINCT "REASON TO CHANGE" IN THIS ONE FILE
 * =====================================================================
 *
 * The Single Responsibility Principle says a class should have exactly one
 * reason to change — i.e. exactly one actor/stakeholder who can force an edit.
 * Below is every independent reason this file would have to be reopened.
 * Each one has a different owner, a different release cadence, and a different
 * reason to break. Fourteen reasons = thirteen too many.
 *
 * --- R1: Changes if the ACCOUNT-OPENING / VALIDATION rules change ------
 *   Where: constructor, minimum-age check          (age < 18 -> force 18)
 *   Trigger: regulator or product lowers/raises the minimum age, or KYC adds
 *            new required fields.
 *   Owner: Compliance / Product.
 *
 * --- R2: Changes if the MINIMUM-BALANCE policy changes -----------------
 *   Where: constructor, and again inside withdraw()
 *   Trigger: Savings floor moves from 500 to 1000, a new "Zero-balance"
 *            account type is launched, or the floor becomes per-branch.
 *   Owner: Product.
 *   Extra smell: the rule is DUPLICATED in two places — a change must be made
 *   twice, and forgetting one place silently splits the policy.
 *
 * --- R3: Changes if the AUTO-CORRECTION policy changes -----------------
 *   Where: constructor — it silently rewrites bad input instead of rejecting.
 *   Trigger: the bank decides invalid input must throw / be rejected / be
 *            queued for manual review rather than quietly "corrected".
 *   Owner: Product / Compliance.
 *
 * --- R4: Changes if the DEPOSIT rules change --------------------------
 *   Where: deposit()
 *   Trigger: an upper deposit cap is added, cash deposits above a threshold
 *            need extra approval, or deposits are allowed on frozen accounts.
 *   Owner: Product.
 *
 * --- R5: Changes if the WITHDRAWAL rules change -----------------------
 *   Where: withdraw()
 *   Trigger: daily withdrawal limits, overdraft support for Current accounts,
 *            per-transaction ceilings, charges on excess withdrawals.
 *   Owner: Product.
 *
 * --- R6: Changes if the ACCOUNT-STATUS / lifecycle model changes -------
 *   Where: the status.equals("Active") guards in deposit(), withdraw(),
 *          plus closeAccount() and reopenAccount()
 *   Trigger: new states such as "Frozen", "Dormant", "Closed-Permanently",
 *            or rules like "dormant accounts can receive but not send".
 *   Owner: Operations / Compliance.
 *   Extra smell: status is a raw String, so a typo compiles fine.
 *
 * --- R7: Changes if the AUTHENTICATION / PIN rules change --------------
 *   Where: setPin(), verifyPin(), and the PIN check inside withdraw()
 *   Trigger: PIN length moves 4 -> 6, PINs must be hashed instead of stored
 *            in plain memory, OTP/biometric is added, lockout after 3 fails.
 *   Owner: Security.
 *
 * --- R8: Changes if the INTEREST RATES or the rate MODEL change --------
 *   Where: calculateInterest()
 *   Trigger: RBI moves the repo rate, a slab-based rate is introduced, or a
 *            new account type ("Fixed Deposit") needs its own rate.
 *   Owner: Treasury / Finance.
 *   Extra smell: an if/else chain on accountType — every new product type
 *   forces an edit here, violating Open/Closed as well.
 *
 * --- R9: Changes if the DATABASE / PERSISTENCE technology changes ------
 *   Where: saveToDatabase(), called from deposit(), withdraw(),
 *          closeAccount(), reopenAccount()
 *   Trigger: MySQL -> PostgreSQL, plain JDBC -> JPA/Hibernate, adding
 *            transactions/retries, or moving to an event-sourced store.
 *   Owner: Infrastructure / DBA.
 *
 * --- R10: Changes if the EMAIL / NOTIFICATION provider changes ---------
 *   Where: sendEmail(), called from deposit(), withdraw(),
 *          closeAccount(), reopenAccount()
 *   Trigger: SMTP -> SendGrid/SES, adding SMS or push notifications,
 *            templating the message body, or localising it.
 *   Owner: Infrastructure / Marketing.
 *
 * --- R11: Changes if the STATEMENT FORMAT changes ---------------------
 *   Where: printStatement()
 *   Trigger: marketing wants a new header/footer or branding, the statement
 *            must render as PDF/CSV/HTML instead of console text, date and
 *            currency columns are added, or it must be paginated by month.
 *   Owner: Marketing / Product.
 *   Extra smell: output is hard-wired to System.out — untestable and
 *   unusable from a web or mobile front end.
 *
 * --- R12: Changes if the TRANSACTION-LOG / audit format changes --------
 *   Where: the transactionLog field, and the string built in deposit()
 *          and withdraw()
 *   Trigger: audit needs a timestamp, a running balance, a transaction ID,
 *            a channel (ATM/UPI/branch), or structured JSON instead of a
 *            free-text String.
 *   Owner: Audit / Compliance.
 *
 * --- R13: Changes if the USER-FACING MESSAGES / error reporting change --
 *   Where: every System.out.println("...") error path and every
 *          "return false" in deposit(), withdraw(), closeAccount(),
 *          reopenAccount(), setPin()
 *   Trigger: messages must be localised (Hindi/regional), returned to a UI
 *            instead of printed, or replaced by typed exceptions / result
 *            objects so the caller can tell WHY an operation failed.
 *   Owner: UX / Localisation.
 *   Extra smell: a bare boolean loses the reason for failure entirely —
 *   the caller cannot distinguish "wrong PIN" from "amount too small".
 *
 * --- R14: Changes if the CURRENCY / money representation changes --------
 *   Where: the double balance field and every "Rs. " literal
 *   Trigger: multi-currency support, or the (real) fix of moving off double
 *            to BigDecimal so rounding errors stop eating paisa.
 *   Owner: Finance / Engineering.
 *
 * ---------------------------------------------------------------------
 * SUMMARY: fourteen independent reasons to change, owned by at least eight
 * different stakeholders — Compliance, Product, Security, Treasury,
 * Infrastructure, Marketing, Audit and UX. Any one of them editing this file
 * risks breaking the work of the others, and every change forces a full retest of
 * account behaviour. The refactor should pull each responsibility out behind
 * its own abstraction (e.g. InterestStrategy, AccountRepository,
 * NotificationService, StatementFormatter, TransactionLogger,
 * AccountValidator) so that each reason to change lands in exactly one class.
 * =====================================================================
 */

public class BankAccount {

    private int accountNumber;
    private String name;
    private int age;
    private double balance;
    private String status;
    private Integer pin;
    private String accountType; // "Savings" or "Current"

    // Every deposit/withdrawal gets logged here as a plain string —
    // logging logic is mixed directly into deposit()/withdraw().
    private List<String> transactionLog = new ArrayList<>();

    public BankAccount(int accountNumber, String name, int age, double balance, String accountType) {

        // Validation logic mixed directly into the constructor
        if (age < 18) {
            System.out.println("Age was below 18, correcting to 18");
            age = 18;
        }

        double minimumBalance = accountType.equals("Savings") ? 500.0 : 1000.0;
        if (balance < minimumBalance) {
            System.out.println("Initial balance below minimum, correcting to " + minimumBalance);
            balance = minimumBalance;
        }

        this.accountNumber = accountNumber;
        this.name = name;
        this.age = age;
        this.balance = balance;
        this.accountType = accountType;
        this.status = "Active";
        this.pin = null;
    }

    // ----------------------------------------------------
    // Account operations, tangled with logging + notification
    // ----------------------------------------------------

    public boolean deposit(double amount) {

        if (!status.equals("Active")) {
            System.out.println("Account is not active");
            return false;
        }

        if (amount <= 0) {
            System.out.println("Invalid deposit amount");
            return false;
        }

        balance += amount;

        // Logging responsibility, baked directly into deposit()
        transactionLog.add("DEPOSIT: Rs. " + amount + " | New balance: " + balance);

        // Notification responsibility, baked directly into deposit()
        sendEmail(name, "Your deposit of Rs. " + amount + " was successful. New balance: " + balance);

        // Persistence responsibility, baked directly into deposit()
        saveToDatabase();

        return true;
    }

    public boolean withdraw(double amount, Integer enteredPin) {

        if (!status.equals("Active")) {
            System.out.println("Account is not active");
            return false;
        }

        if (pin != null) {
            if (enteredPin == null || !enteredPin.equals(pin)) {
                System.out.println("Incorrect PIN");
                return false;
            }
        }

        if (amount <= 0) {
            System.out.println("Invalid withdrawal amount");
            return false;
        }

        double minimumBalance = accountType.equals("Savings") ? 500.0 : 1000.0;
        if (balance - amount < minimumBalance) {
            System.out.println("Withdrawal would breach minimum balance");
            return false;
        }

        balance -= amount;

        transactionLog.add("WITHDRAW: Rs. " + amount + " | New balance: " + balance);

        sendEmail(name, "Your withdrawal of Rs. " + amount + " was successful. New balance: " + balance);

        saveToDatabase();

        return true;
    }

    public boolean closeAccount() {
        if (status.equals("Inactive")) return false;
        status = "Inactive";
        sendEmail(name, "Your account has been closed.");
        saveToDatabase();
        return true;
    }

    public boolean reopenAccount() {
        if (status.equals("Active")) return false;
        status = "Active";
        sendEmail(name, "Your account has been reopened.");
        saveToDatabase();
        return true;
    }

    public boolean setPin(int newPin) {
        if (newPin >= 1000 && newPin <= 9999) {
            this.pin = newPin;
            return true;
        }
        return false;
    }

    public boolean verifyPin(int enteredPin) {
        return pin != null && pin.equals(enteredPin);
    }

    // ----------------------------------------------------
    // Interest calculation — an if/else chain baked into the account itself
    // ----------------------------------------------------

    public double calculateInterest() {
        if (accountType.equals("Savings")) {
            return balance * 0.04;
        } else if (accountType.equals("Current")) {
            return balance * 0.01;
        } else {
            return 0.0;
        }
    }

    // ----------------------------------------------------
    // "Persistence" — pretend database logic living inside the account
    // ----------------------------------------------------

    private void saveToDatabase() {
        // Pretend this talks to MySQL. In reality just prints.
        System.out.println("[DB] Saving account " + accountNumber + " to MySQL...");
    }

    // ----------------------------------------------------
    // "Notification" — pretend email logic living inside the account
    // ----------------------------------------------------

    private void sendEmail(String recipient, String message) {
        // Pretend this talks to an SMTP server. In reality just prints.
        System.out.println("[EMAIL] To: " + recipient + " | " + message);
    }

    // ----------------------------------------------------
    // "Statement generation" — formatting logic living inside the account
    // ----------------------------------------------------

    public void printStatement() {
        System.out.println("---- Statement for Account #" + accountNumber + " (" + name + ") ----");
        for (String entry : transactionLog) {
            System.out.println(entry);
        }
        System.out.println("Current Balance: Rs. " + balance);
        System.out.println("-----------------------------------------------------");
    }

    // ----------------------------------------------------
    // Getters
    // ----------------------------------------------------

    public int getAccountNumber() { return accountNumber; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public double getBalance() { return balance; }
    public String getStatus() { return status; }
    public String getAccountType() { return accountType; }
    public boolean hasPin() { return pin != null; }
}
