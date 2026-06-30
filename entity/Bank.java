package entity;

import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Bank {
    private List<Account> accounts;
    private String accountFile = "database/account.txt";
    private String transactionFile = "database/transactionhistory.txt";

    public Bank() {
        this.accounts = new ArrayList<>();
        loadAccounts();
    }

    private void loadAccounts() {
        File file = new File(accountFile);
        if (!file.exists()) {
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    accounts.add(Account.fromFileString(line));
                }
            }
        } catch (IOException e) {
            System.out.println("Loi khi doc file: " + e.getMessage());
        }
    }

    private void saveAccounts() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(accountFile))) {
            for (Account account : accounts) {
                bw.write(account.toFileString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Loi khi luu file: " + e.getMessage());
        }
    }

    private void saveTransaction(String message) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(transactionFile, true))) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
            bw.write("[" + timestamp + "] " + message);
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Loi khi luu lich su: " + e.getMessage());
        }
    }

    public String createBankAccount(Account account) {
        for (Account acc : accounts) {
            if (acc.getAccount().equals(account.getAccount())) {
                return "Tai khoan da ton tai";
            }
        }
        accounts.add(account);
        saveAccounts();
        saveTransaction("Tao tai khoan: " + account.getAccount() + " - " + account.getName());
        return "Tao tai khoan ngan hang thanh cong";
    }

    public Account login(String accountNumber, String password) {
        for (Account account : accounts) {
            if (account.getAccount().equals(accountNumber) && account.getPassword().equals(password)) {
                return account;
            }
        }
        return null;
    }

    public boolean deposit(Account account, double amount) {
        if (amount <= 0) {
            return false;
        }
        account.setBalance(account.getBalance() + amount);
        updateAccount(account);
        saveTransaction(
                "Nap tien: " + account.getAccount() + " - So tien: " + amount + " - So du: " + account.getBalance());
        return true;
    }

    public boolean withdraw(Account account, double amount) {
        if (amount <= 0 || account.getBalance() < amount) {
            return false;
        }
        account.setBalance(account.getBalance() - amount);
        updateAccount(account);
        saveTransaction(
                "Rut tien: " + account.getAccount() + " - So tien: " + amount + " - So du: " + account.getBalance());
        return true;
    }

    public boolean transfer(Account fromAccount, String toBankCode, double amount) {
        if (amount <= 0 || fromAccount.getBalance() < amount) {
            return false;
        }
        Account toAccount = findAccountByBankCode(toBankCode);
        if (toAccount == null) {
            return false;
        }
        fromAccount.setBalance(fromAccount.getBalance() - amount);
        toAccount.setBalance(toAccount.getBalance() + amount);
        updateAccount(fromAccount);
        updateAccount(toAccount);
        saveTransaction(
                "Chuyen tien: " + fromAccount.getAccount() + " -> " + toAccount.getAccount() + " - So tien: " + amount);
        return true;
    }

    private Account findAccountByBankCode(String bankCode) {
        for (Account account : accounts) {
            if (account.getBankCode().equals(bankCode)) {
                return account;
            }
        }
        return null;
    }

    private void updateAccount(Account account) {
        for (int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).getAccount().equals(account.getAccount())) {
                accounts.set(i, account);
                break;
            }
        }
        saveAccounts();
    }

    public String generateBankCode() {
        StringBuilder bankCode = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 8; i++) {
            bankCode.append(random.nextInt(0, 10));
        }
        return bankCode.toString();
    }
}