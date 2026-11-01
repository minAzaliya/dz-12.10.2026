import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class BankAccount {

    private final String accountNumber;
    private final String holderName;


    private final Balance operationLimit;

    private final List<String> operationLog = new ArrayList<>();

    private Balance currentBalance;

    public BankAccount(String holderName, Balance startBalance, Balance operationLimit) {
        if (holderName == null || holderName.isBlank()) {
            throw new IllegalArgumentException("Имя владельца не может быть пустым");
        }

        if (startBalance == null) {
            throw new IllegalArgumentException("Стартовый баланс не может быть null");
        }

        if (operationLimit == null) {
            throw new IllegalArgumentException("Лимит на операцию не может быть null");
        }

        if (startBalance.getCurrency() != operationLimit.getCurrency()) {
            throw new IllegalArgumentException("Валюта лимита должна совпадать с валютой счёта");
        }

        this.accountNumber = UUID.randomUUID().toString();
        this.holderName = holderName;
        this.currentBalance = startBalance;
        this.operationLimit = operationLimit;
    }

    public String getAccountNumber()   { return accountNumber; }
    public String getHolderName()      { return holderName; }
    public Balance getCurrentBalance() { return currentBalance; }


    public Balance getOperationLimit() { return operationLimit; }

    public List<String> getOperationLog() {
        return List.copyOf(operationLog);
    }

    public void deposit(Balance sum) {
        validateSum(sum);
        currentBalance = currentBalance.add(sum);
        operationLog.add("Пополнение на " + sum);
    }

    public void cashOut(Balance sum) {
        validateSum(sum);
        validateCashOut(sum);

        currentBalance = currentBalance.subtract(sum);
        operationLog.add("Снятие наличных " + sum);
    }

    public void transfer(BankAccount recipient, Balance sum) {
        if (recipient == null) {
            throw new IllegalArgumentException("Получатель не может быть null");
        }

        if (sum == null) {
            throw new IllegalArgumentException("Сумма не может быть null");
        }

        if (recipient == this) {
            throw new IllegalArgumentException("Нельзя перевести на тот же счёт");
        }

        if (recipient.currentBalance.getCurrency() != currentBalance.getCurrency()) {
            throw new IllegalArgumentException("Несовпадение валют между счетами");
        }

        validateSum(sum);
        validateCashOut(sum);

        Balance newSource = currentBalance.subtract(sum);
        Balance newTarget = recipient.currentBalance.add(sum);

        currentBalance = newSource;
        recipient.currentBalance = newTarget;

        operationLog.add("Перевод " + sum + " для " + recipient.holderName);
        recipient.operationLog.add("Получен перевод " + sum + " от " + holderName);
    }

    private void validateSum(Balance sum) {
        if (sum == null) {
            throw new IllegalArgumentException("Сумма не может быть null");
        }
        if (sum.getCurrency() != currentBalance.getCurrency()) {
            throw new IllegalArgumentException("Несовпадение валют");
        }
        if (sum.isZero()) {
            throw new IllegalArgumentException("Сумма должна быть положительной");
        }
    }

    private void validateCashOut(Balance sum) {
        if (sum.getKopecks() > currentBalance.getKopecks()) {
            throw new IllegalArgumentException(
                    "Недостаточно средств: баланс " + currentBalance
                            + ", запрошено " + sum);
        }

        if (sum.getKopecks() > operationLimit.getKopecks()) {
            throw new IllegalArgumentException("Сумма превышает лимит операции " + operationLimit);
        }
    }

    @Override
    public String toString() {
        return String.format("BankAccount[%s, balance=%s, limit=%s]",
                holderName, currentBalance, operationLimit);
    }
}