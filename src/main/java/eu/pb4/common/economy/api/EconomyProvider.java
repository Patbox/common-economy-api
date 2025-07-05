package eu.pb4.common.economy.api;

import com.mojang.authlib.GameProfile;
import eu.pb4.common.economy.impl.EconomyImpl;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SuppressWarnings({"unused"})
public interface EconomyProvider {
    /**
     * Providers name
     */
    Text name();

    /**
     * Gets account controlled by player following id
     *
     * @param player
     * @param account account's id
     * @return EconomyAccount or null
     */
    @Nullable
    default EconomyAccount getAccount(ServerPlayerEntity player, String account) {
        return this.getAccount(player.getServer(), player.getGameProfile(), account);
    }

    /**
     * Gets account controlled by player following id
     *
     * @param server current server
     * @param profile GameProfile of player having access/owning account
     * @param accountId accounts id
     * @return EconomyAccount or null
     */
    @Nullable
    EconomyAccount getAccount(MinecraftServer server, GameProfile profile, String accountId);

    /**
     * Gets all accounts controlled by player
     *
     * @param player
     * @return Collection of accounts
     */
    default Collection<EconomyAccount> getAccounts(ServerPlayerEntity player) {
        return this.getAccounts(player.getServer(), player.getGameProfile());
    }

    /**
     * Gets all accounts accessible by player (provided as gameprofile)
     * @param server current server
     * @param profile GameProfile of player having access/owning account
     * @return Collection of accounts
     */
    Collection<EconomyAccount> getAccounts(MinecraftServer server, GameProfile profile);

    /**
     * Gets all accounts controlled by player under provided currency
     *
     * @param player
     * @param currency Accounts currency
     * @return Collection of accounts
     */
    default Collection<EconomyAccount> getAccounts(ServerPlayerEntity player, EconomyCurrency currency) {
        return this.getAccounts(player.getServer(), player.getGameProfile(), currency);
    }

    /**
     * Gets all accounts controlled by player under provided currency
     *
     * @param server current server
     * @param profile GameProfile of player having access/owning account
     * @param currency Accounts currency
     * @return Collection of accounts
     */
    default Collection<EconomyAccount> getAccounts(MinecraftServer server, GameProfile profile, EconomyCurrency currency) {
        List<EconomyAccount> list = new ArrayList<>();
        for (var x : getAccounts(server, profile)) {
            if (x.currency() == currency) {
                list.add(x);
            }
        }
        return list;
    }

    /**
     * Gets currency from id
     *
     * @param server
     * @param currencyId
     * @return EconomyCurrency or null
     */
    @Nullable
    EconomyCurrency getCurrency(MinecraftServer server, String currencyId);

    /**
     * Gets all currencies handled by this provider
     *
     * @return Collection of currencies
     * @param server
     */
    Collection<EconomyCurrency> getCurrencies(MinecraftServer server);

    /**
     * Gets default account id of player
     * @return id of default account
     */
    @Nullable
    default String defaultAccount(ServerPlayerEntity player, EconomyCurrency currency) {
        return defaultAccount(player.getServer(), player.getGameProfile(), currency);
    }

    /**
     * Gets default account id of player
     * @return id of default account
     */
    @Nullable
    String defaultAccount(MinecraftServer server, GameProfile profile, EconomyCurrency currency);

    /**
     * Gets default account of player
     * @return id of default account
     */
    @Nullable
    default EconomyAccount getDefaultAccount(ServerPlayerEntity player, EconomyCurrency currency) {
        var id = defaultAccount(player, currency);
        return  id != null ? this.getAccount(player, id) : null;
    }

    /**
     * Gets default account of player
     * @return id of default account
     */
    @Nullable
    default EconomyAccount getDefaultAccount(MinecraftServer server, GameProfile profile, EconomyCurrency currency) {
        var id = defaultAccount(server, profile, currency);
        return  id != null ? this.getAccount(server, profile, id) : null;
    }

    /**
     * Icon for other mods to use
     * @return
     */
    default ItemStack icon() {
        return Items.SUNFLOWER.getDefaultStack();
    }

    /**
     * Provider's id
     */
    default String id() {
        return EconomyImpl.getId(this);
    }
}
