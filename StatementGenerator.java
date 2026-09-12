public class StatementGenerator {
    public String generate(BankAccount account) {
        StringBuilder sb = new StringBuilder();
        sb.append("Statement for Account")
          .append(account.getAccountNumber())
          .append(" (").append(account.getName()).append(") ----\n");
        for (String entry : account.getTransactionLog()) {
            sb.append(entry).append("\n");
        }
        sb.append("Current Balance: Rs. ").append(account.getBalance()).append("\n");
        return sb.toString();
    }
}
