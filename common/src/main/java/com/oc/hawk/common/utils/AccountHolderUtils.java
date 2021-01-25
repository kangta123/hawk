package com.oc.hawk.common.utils;


import com.oc.hawk.api.constant.AccountHolder;

import java.util.Objects;

public class AccountHolderUtils {
    private static final ThreadLocal<AccountHolder> ACCOUNT_THREAD_LOCAL = new ThreadLocal<>();

    public static AccountHolder getAccountHolder() {
        AccountHolder currentThreadAccount = ACCOUNT_THREAD_LOCAL.get();
        if (Objects.nonNull(currentThreadAccount)) {
            return currentThreadAccount;
        } else {
            return null;
        }
    }

    public static void setCurrent(AccountHolder accountHolder) {
        ACCOUNT_THREAD_LOCAL.set(accountHolder);
    }

    public static void removeAccountHolder() {
        ACCOUNT_THREAD_LOCAL.set(null);
        ACCOUNT_THREAD_LOCAL.remove();
    }
}

