package eu.pb4.common.economy.api;


import net.minecraft.network.chat.Component;

import java.math.BigInteger;

/// A change in an [EconomyAccount]'s balance. A transaction MAY NOT be partially successful.
@SuppressWarnings({"unused"})
public interface EconomyTransaction {
    boolean isSuccessful();

    default boolean isFailure() {
        return !this.isSuccessful();
    }

    /// Player-facing message describing the failure or success of the transaction.
    ///
    /// For example, "Transaction failed!", "Failed: Account does not have enough currency", "Success!", or
    /// "Successfully added $amount to $account!"
    Component message();

    /// Account balance after this transaction. In case of failure, [#finalBalance] MUST equal [#previousBalance].
    BigInteger finalBalance();

    /// Account balance before this transaction.
    BigInteger previousBalance();

    /// Value of transaction. MUST be negative if transaction represents value being removed.
    ///
    /// MUST be the same regardless of success.
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


