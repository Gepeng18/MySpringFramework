package demo.pattern.proxy;

import demo.pattern.proxy.cglib.AlipayMethodInterceptor;
import demo.pattern.proxy.cglib.CglibUtil;
import demo.pattern.proxy.impl.*;
import demo.pattern.proxy.jdkproxy.AlipayInvocationHandler;
import demo.pattern.proxy.jdkproxy.JdkDynamicProxyUtil;
import net.sf.cglib.proxy.MethodInterceptor;

import java.lang.reflect.InvocationHandler;

public class ProxyDemo {
    public static void main(String[] args) {
        proxyPattern();

        trueJdkProxy();

        falseJdkProxy();

        trueCglibProxy();
    }

    private static void proxyPattern() {
        ToCPayment toCProxy = new AlipayToC(new ToCPaymentImpl());
        toCProxy.pay();
        ToBPayment toBProxy = new AlipayToB(new ToBPaymentImpl());
        toBProxy.pay();
    }

    private static void trueJdkProxy() {
        ToCPayment toCPayment = new ToCPaymentImpl();
        InvocationHandler handler = new AlipayInvocationHandler(toCPayment);
        ToCPayment toCProxy = JdkDynamicProxyUtil.newProxyInstance(toCPayment,handler);
        toCProxy.pay();
        ToBPayment toBPayment = new ToBPaymentImpl();
        InvocationHandler handlerToB = new AlipayInvocationHandler(toBPayment);
        ToBPayment toBProxy = JdkDynamicProxyUtil.newProxyInstance(toBPayment, handlerToB);
        toBProxy.pay();
    }

    private static void falseJdkProxy() {
        CommonPayment commonPayment = new CommonPayment();
        AlipayInvocationHandler invocationHandler = new AlipayInvocationHandler(commonPayment);
        CommonPayment commonPaymentProxy = JdkDynamicProxyUtil.newProxyInstance(commonPayment, invocationHandler);
        commonPaymentProxy.pay();
    }


    private static void trueCglibProxy() {
        CommonPayment commonPayment = new CommonPayment();
        MethodInterceptor methodInterceptor = new AlipayMethodInterceptor();
        CommonPayment commonPaymentProxy = CglibUtil.createProxy(commonPayment, methodInterceptor);
        commonPaymentProxy.pay();

        ToCPayment toCPayment = new ToCPaymentImpl();
        ToCPayment toCProxy = CglibUtil.createProxy(toCPayment, methodInterceptor);
        toCProxy.pay();
    }

}
