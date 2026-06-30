import entity.Account;
import entity.Bank;
import util.FileChecker;
import java.util.Scanner;

public class App {
    private static Bank bank;
    private static Scanner scanner;
    private static Account currentAccount;

    public static void main(String[] args) {
        FileChecker.checkAndCreateFile("database/account.txt");
        FileChecker.checkAndCreateFile("database/transactionhistory.txt");

        bank = new Bank();
        scanner = new Scanner(System.in);

        mainMenu();
        scanner.close();
    }

    private static void mainMenu() {
        while (true) {
            System.out.println("\n========== NGAN HANG ==========");
            System.out.println("1. Dang ky tai khoan");
            System.out.println("2. Dang nhap");
            System.out.println("3. Thoat");
            System.out.print("Chon: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    registerAccount();
                    break;
                case "2":
                    login();
                    break;
                case "3":
                    System.out.println("Cam on ban da su dung dich vu!");
                    return;
                default:
                    System.out.println("Lua chon khong hop le!");
            }
        }
    }

    private static void registerAccount() {
        System.out.println("\n----- DANG KY TAI KHOAN -----");
        System.out.print("Nhap ten dang nhap: ");
        String account = scanner.nextLine();

        System.out.print("Nhap mat khau: ");
        String password = scanner.nextLine();

        System.out.print("Nhap ten cua ban: ");
        String name = scanner.nextLine();

        String bankCode = bank.generateBankCode();
        Account newAccount = new Account(account, password, bankCode, 0, name);

        String result = bank.createBankAccount(newAccount);
        System.out.println(result);
        if (result.contains("thanh cong")) {
            System.out.println("Ma ngan hang cua ban: " + bankCode);
        }
    }

    private static void login() {
        System.out.println("\n----- DANG NHAP -----");
        System.out.print("Nhap ten dang nhap: ");
        String account = scanner.nextLine();

        System.out.print("Nhap mat khau: ");
        String password = scanner.nextLine();

        currentAccount = bank.login(account, password);

        if (currentAccount == null) {
            System.out.println("Tai khoan hoac mat khau khong dung!");
            return;
        }

        System.out.println("Dang nhap thanh cong! Xin chao " + currentAccount.getName());
        accountMenu();
    }

    private static void accountMenu() {
        while (true) {
            System.out.println("\n========== MENU TAI KHOAN ==========");
            System.out.println("So du: " + currentAccount.getBalance() + " VND");
            System.out.println("Ma ngan hang: " + currentAccount.getBankCode());
            System.out.println("1. Nap tien");
            System.out.println("2. Rut tien");
            System.out.println("3. Chuyen tien");
            System.out.println("4. Dang xuat");
            System.out.print("Chon: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    deposit();
                    break;
                case "2":
                    withdraw();
                    break;
                case "3":
                    transfer();
                    break;
                case "4":
                    System.out.println("Dang xuat thanh cong!");
                    currentAccount = null;
                    return;
                default:
                    System.out.println("Lua chon khong hop le!");
            }
        }
    }

    private static void deposit() {
        System.out.println("\n----- NAP TIEN -----");
        System.out.print("Nhap so tien: ");
        try {
            double amount = Double.parseDouble(scanner.nextLine());
            if (bank.deposit(currentAccount, amount)) {
                System.out.println("Nap tien thanh cong! So du moi: " + currentAccount.getBalance() + " VND");
            } else {
                System.out.println("Nap tien that bai! So tien phai lon hon 0.");
            }
        } catch (NumberFormatException e) {
            System.out.println("So tien khong hop le!");
        }
    }

    private static void withdraw() {
        System.out.println("\n----- RUT TIEN -----");
        System.out.print("Nhap so tien: ");
        try {
            double amount = Double.parseDouble(scanner.nextLine());
            if (bank.withdraw(currentAccount, amount)) {
                System.out.println("Rut tien thanh cong! So du moi: " + currentAccount.getBalance() + " VND");
            } else {
                System.out.println("Rut tien that bai! So du khong du hoac so tien khong hop le.");
            }
        } catch (NumberFormatException e) {
            System.out.println("So tien khong hop le!");
        }
    }

    private static void transfer() {
        System.out.println("\n----- CHUYEN TIEN -----");
        System.out.print("Nhap ma ngan hang nguoi nhan: ");
        String toBankCode = scanner.nextLine();

        System.out.print("Nhap so tien: ");
        try {
            double amount = Double.parseDouble(scanner.nextLine());
            if (bank.transfer(currentAccount, toBankCode, amount)) {
                System.out.println("Chuyen tien thanh cong! So du moi: " + currentAccount.getBalance() + " VND");
            } else {
                System.out.println(
                        "Chuyen tien that bai! So du khong du, ma ngan hang khong ton tai hoac so tien khong hop le.");
            }
        } catch (NumberFormatException e) {
            System.out.println("So tien khong hop le!");
        }
    }
}