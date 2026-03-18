package eu.pb4.common.economy.api;


import net.minecraft.network.chat.Component;

import java.math.BigInteger;

@SuppressWarnings({"unused"})
public interface EconomyTransaction {
    boolean isSuccessful();

    default boolean isFailure() {
        return !this.isSuccessful();
    }

    /**
     * Player-facing message in case of a failure/success of operation
     */
    Component message();

    /**
     * Final balance for transaction. Equal to previousBalance in case of failure
     */
    BigInteger finalBalance();

    BigInteger previousBalance();

    /**
     * Amount of money in transaction. Should be negative if money is removed
     */
    BigInteger transactionAmount();

    EconomyAccount account();

    record Simple(boolean isSuccessful, Component message, BigInteger finalBalance, BigInteger previousBalance, BigInteger transactionAmount, EconomyAccount account) implements EconomyTransaction {
        public EconomyTransaction failure(Component message, BigInteger balance, BigInteger transactionAmount, EconomyAccount account) {
            return new Simple(false, message, balance, balance, transactionAmount, account);
        }

        public EconomyTransaction success(Component message, BigInteger previousBalance, BigInteger transactionAmount, EconomyAccount account) {
            return success(message, previousBalance.add(transactionAmount), previousBalance, transactionAmount, account);
        }
        public EconomyTransaction success(Component message, BigInteger finalBalance, BigInteger previousBalance, BigInteger transactionAmount, EconomyAccount account) {
            return new Simple(true, message, finalBalance, previousBalance, transactionAmount, account);
        }
    }
}


