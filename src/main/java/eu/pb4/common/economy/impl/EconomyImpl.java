package eu.pb4.common.economy.impl;

import com.mojang.authlib.GameProfile;
import eu.pb4.common.economy.api.EconomyAccount;
import eu.pb4.common.economy.api.EconomyCurrency;
import eu.pb4.common.economy.api.EconomyProvider;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;

import java.util.*;


@ApiStatus.Internal
public final class EconomyImpl {
    private static final Map<String, EconomyProvider> PROVIDERS_BY_ID = new HashMap<>();
    private static final Map<EconomyProvider, String> ID_BY_PROVIDERS = new HashMap<>();
    private static final Set<EconomyProvider> PROVIDERS = new HashSet<>();

    public static final Style WHITE_NON_ITALIC_STYLE = Style.EMPTY.withFormatting(Formatting.WHITE).withItalic(false);

    public static String getId(EconomyProvider provider) {
        return ID_BY_PROVIDERS.get(provider);
    }

    public static EconomyAccount get(MinecraftServer server, GameProfile profile, Identifier account) {
        EconomyProvider provider = getProvider(account.getNamespace());

        if (provider != null) {
            return provider.getAccount(server, profile, account.getPath());
        }

        return null;
    }

    public static Collection<EconomyAccount> getAll(MinecraftServer server, GameProfile profile) {
        ArrayList<EconomyAccount> list = new ArrayList<>();

        for (EconomyProvider provider : PROVIDERS) {
            list.addAll(provider.getAccounts(server, profile));
        }

        return list;
    }

    public static Collection<EconomyAccount> getAll(MinecraftServer server, GameProfile profile, EconomyCurrency currency) {
        ArrayList<EconomyAccount> list = new ArrayList<>();

        for (EconomyProvider provider : PROVIDERS) {
            list.addAll(provider.getAccounts(server, profile, currency));
        }

        return list;
    }

    public static EconomyCurrency getCurrency(MinecraftServer server, Identifier account) {
        EconomyProvider provider = getProvider(account.getNamespace());

        if (provider != null) {
            return provider.getCurrency(server, account.getPath());
        }

        return null;
    }

    public static Collection<EconomyCurrency> getCurrencies(MinecraftServer server) {
        var list = new ArrayList<EconomyCurrency>();

        for (var provider : PROVIDERS) {
            list.addAll(provider.getCurrencies(server));
        }

        return list;
    }

    public static EconomyProvider getProvider(String id) {
        return PROVIDERS_BY_ID.get(id);
    }

    public static EconomyProvider register(String providerId, EconomyProvider provider) {
        if (PROVIDERS_BY_ID.containsKey(providerId)) {
            throw new IllegalArgumentException("Provider '" + providerId + "' already exists!");
        }

        for(int i = 0; i < providerId.length(); ++i) {
            if (!isNamespaceCharacterValid(providerId.charAt(i))) {
                throw new IllegalArgumentException("Provider id '" + providerId + "' contains invalid characters!");
            }
        }

        PROVIDERS_BY_ID.put(providerId, provider);
        ID_BY_PROVIDERS.put(provider, providerId);
        PROVIDERS.add(provider);
        return provider;
    }

    public static Collection<EconomyProvider> providers() {
        return Collections.unmodifiableSet(PROVIDERS);
    }



    private static boolean isNamespaceCharacterValid(char character) {
        return character == '_' || character == '-' || character >= 'a' && character <= 'z' || character >= '0' && character <= '9' || character == '.';
    }
}
