import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        BankAccount azaliya = new BankAccount(
                "Азалия",
                new Balance(1_000_000, Currency.RUB),
                new Balance(300_000, Currency.RUB));

        BankAccount emilia = new BankAccount(
                "Эмилия",
                new Balance(0, Currency.RUB),
                new Balance(100_000, Currency.RUB));

        System.out.println("Клиенты банка: Азалия и Эмилия");

        while (true) {
            System.out.println("\nМЕНЮ");
            System.out.println("1. Пополнить счет Азалии");
            System.out.println("2. Снять наличные Азалией");
            System.out.println("3. Перевести деньги от Азалии к Эмилии");
            System.out.println("4. Показать баланс обоих счетов");
            System.out.println("5. Показать журнал операций Азалии");
            System.out.println("0. Выход");
            System.out.print("Выберите действие: ");

            String choice = scanner.nextLine();

            if (choice.equals("0")) {
                System.out.println("До свидания!");
                break;
            } else if (choice.equals("1")) {
                System.out.print("Введите сумму в рублях для пополнения: ");
                long rubles = Long.parseLong(scanner.nextLine());

                Balance sum = new Balance(rubles * 100, Currency.RUB);

                try {
                    azaliya.deposit(sum);
                    System.out.println("Успешно! Новый баланс Азалии: " + azaliya.getCurrentBalance());
                } catch (IllegalArgumentException e) {
                    System.out.println("ОШИБКА: " + e.getMessage());
                }
            } else if (choice.equals("2")) {
                System.out.print("Введите сумму в рублях для снятия: ");
                long rubles = Long.parseLong(scanner.nextLine());
                Balance sum = new Balance(rubles * 100, Currency.RUB);

                try {
                    azaliya.cashOut(sum);
                    System.out.println("Успешно! Новый баланс Азалии: " + azaliya.getCurrentBalance());
                } catch (IllegalArgumentException e) {
                    System.out.println("ОШИБКА: " + e.getMessage());
                }
            } else if (choice.equals("3")) {
                System.out.print("Введите сумму в рублях для перевода Эмилии: ");
                long rubles = Long.parseLong(scanner.nextLine());
                Balance sum = new Balance(rubles * 100, Currency.RUB);

                try {

                    azaliya.transfer(emilia, sum);
                    System.out.println("Успешно переведено!");
                    System.out.println("Баланс Азалии:  " + azaliya.getCurrentBalance());
                    System.out.println("Баланс Эмилии:  " + emilia.getCurrentBalance());
                } catch (IllegalArgumentException e) {
                    System.out.println("ОШИБКА: " + e.getMessage());
                }
            } else if (choice.equals("4")) {
                System.out.println("Баланс Азалии:  " + azaliya.getCurrentBalance());
                System.out.println("Баланс Эмилии:  " + emilia.getCurrentBalance());
            } else if (choice.equals("5")) {
                System.out.println("Журнал Азалии");
                for (String entry : azaliya.getOperationLog()) {
                    System.out.println("  - " + entry);
                }
            } else {
                System.out.println("Неверный ввод. Попробуйте еще раз.");
            }
        }

        scanner.close();
    }
}