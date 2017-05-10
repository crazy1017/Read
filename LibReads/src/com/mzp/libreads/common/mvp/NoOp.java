package com.mzp.libreads.common.mvp;

import static java.lang.reflect.Proxy.newProxyInstance;
import static com.mzp.libreads.common.mvp.Defaults.defaultValue;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

final class NoOp {

    private static final InvocationHandler DEFAULT_VALUE = new DefaultValueInvocationHandler();

    private NoOp() {
        // no instances
    }

    @SuppressWarnings("unchecked")
    public static <T> T of(Class<T> interfaceClass) {
        return (T) newProxyInstance(interfaceClass.getClassLoader(), new Class[]{interfaceClass},
                DEFAULT_VALUE);
    }

    private static class DefaultValueInvocationHandler implements InvocationHandler {

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            return defaultValue(method.getReturnType());
        }
    }
}