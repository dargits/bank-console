package entity;

public class Account {
    private String account;
    private String password;
    private String bankCode;
    private double balance;
    private String name;

    public Account(String account, String password, String bankCode, double balance, String name) {
        this.account = account;
        this.password = password;
        this.bankCode = bankCode;
        this.balance = balance;
        this.name = name;
    }

    public String getAccount() {
        return account;
    }

    public String getPassword() {
        return password;
    }

    public String getBankCode() {
        return bankCode;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public String toFileString() {
        return account + "|" + password + "|" + bankCode + "|" + balance + "|" + name;
    }

    public static Account fromFileString(String line) {
        String[] parts = line.split("\\|");
        return new Account(parts[0], parts[1], parts[2], Double.parseDouble(parts[3]), parts[4]);
    }
}
