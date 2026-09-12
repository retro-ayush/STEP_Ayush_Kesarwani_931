public class Main {
    public static void main(String[] args) {
        AccountRepository repo = new AccountRepository();
        NotificationService notifier = new NotificationService();
        StatementGenerator statements = new StatementGenerator();
        BankAccount account = new BankAccount(931, "Ayush", 20, 5000.0, "Savings");
        if (account.deposit(1500)) {
            notifier.send("Your deposit of Rs. 1500.0 was successful. New balance: " + account.getBalance());
            repo.save(account);
        }
        if (account.deposit(800)) {
            notifier.send("Your deposit of Rs. 800.0 was successful. New balance: " + account.getBalance());
            repo.save(account);
        }
        if (account.withdraw(2000)) {
            notifier.send("Your withdrawal of Rs. 2000.0 was successful. New balance: " + account.getBalance());
            repo.save(account);
        }
        System.out.println(statements.generate(account));
    }
}
