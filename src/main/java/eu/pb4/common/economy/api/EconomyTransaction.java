package eu.pb4.common.economy.api;

import net.minecraft.text.Text;

@SuppressWarnings({"unused"})
public interface EconomyTransaction {
    boolean isSuccessful();

    default boolean isFailure() {
        return !this.isSuccessful();
    }

    /**
     * Player-facing message in case of a failure/success of operation
     */
    Text message();

    /**
     * Final balance for transaction. Equal to previousBalance in case of failure
     */
    long finalBalance();

    long previousBalance();

    /**
     * Amount of money in transaction. Should be negative if money is removed
     */
    long transactionAmount();

    EconomyAccount account();

    record Simple(boolean isSuccessful, Text message, long finalBalance, long previousBalance, long transactionAmount, EconomyAccount account) implements EconomyTransaction {
        public EconomyTransaction failure(Text message, long balance, long transactionAmount, EconomyAccount account) {
            return new Simple(false, message, balance, balance, transactionAmount, account);
        }

        public EconomyTransaction success(Text message, long previousBalance, long transactionAmount, EconomyAccount account) {
            return success(message, previousBalance + transactionAmount, previousBalance, transactionAmount, account);
        }
        public EconomyTransaction success(Text message, long finalBalance, long previousBalance, long transactionAmount, EconomyAccount account) {
            return new Simple(true, message, finalBalance, previousBalance, transactionAmount, account);
        }
    }
}


