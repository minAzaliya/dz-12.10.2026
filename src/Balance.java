import java.util.Objects;

public final class Balance {
    private final long kopecks;
    private final Currency currency;

    public Balance(long kopecks, Currency currency) {
        if (currency == null) {
            throw new IllegalArgumentException("Currency не может быть пустым");
        }
        if (kopecks < 0) {
            throw new IllegalArgumentException("Сумма не может быть отрицательной!");
        }
        this.currency = currency;
        this.kopecks = kopecks;
    }

    public long getKopecks() {
        return kopecks;
    }

    public Currency getCurrency() {
        return currency;
    }

    public boolean isZero() {
        return kopecks == 0;
    }

    public Balance add(Balance toAdd) {
        checkCurrency(toAdd);
        return new Balance(kopecks + toAdd.kopecks, currency);
    }

    public Balance subtract(Balance toSubtract) {
        checkCurrency(toSubtract);
        if (toSubtract.kopecks > kopecks) {
            throw new IllegalArgumentException(
                    "Недостаточно средств: есть " + kopecks
                            + ", нужно " + toSubtract.kopecks);
        }

        return new Balance(kopecks - toSubtract.kopecks, currency);
    }

    public Balance convertTo(Currency newCurrency) {
        if (newCurrency == null) {
            throw new IllegalArgumentException("Новая валюта не может быть null");
        }
        if (newCurrency == currency) {
            return this;
        }
        long inRubles = kopecks * currency.getPriceInRublesKopecks();
        long converted = inRubles / newCurrency.getPriceInRublesKopecks();
        return new Balance(converted, newCurrency);
    }

    private void checkCurrency(Balance balance) {
        if (balance == null) {
            throw new IllegalArgumentException("Другой баланс не может быть null");
        }
        if (balance.currency != currency) {
            throw new IllegalArgumentException("Несовпадение валют: " + currency + " и " + balance.currency);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Balance balance)) return false;
        return kopecks == balance.kopecks && currency == balance.currency;
    }

    @Override
    public int hashCode() {
        return Objects.hash(kopecks, currency);
    }

    @Override
    public String toString() {
        return String.format("%d.%02d %s",
                kopecks / 100, kopecks % 100, currency.getSymbol());
    }
}

