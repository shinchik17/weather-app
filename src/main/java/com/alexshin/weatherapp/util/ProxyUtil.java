package com.alexshin.weatherapp.util;


import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.ProxySelector;
import java.net.http.HttpClient;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ProxyUtil {

    private ProxyUtil() {
        throw new UnsupportedOperationException("ProxyUtil cannot be instantiated");
    }


    public static void setProxy(HttpClient.Builder httpClientBuilder) {
        String proxy = getProxyString();
        httpClientBuilder.authenticator(createAuthenticator(proxy))
                .proxy(createProxySelector(proxy));
    }

    private static ProxySelector createProxySelector(String proxy) {
        String host = getProxyHost(proxy);
        int port = getProxyPort(proxy);
        return ProxySelector.of(new InetSocketAddress(host, port));
    }


    private static Authenticator createAuthenticator(String proxy) {

        String user = getProxyUser(proxy);
        String pass = getProxyUserPassword(proxy);

        return new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, pass.toCharArray());
            }
        };
    }

    private static String getProxyUser(String proxy) {
        Pattern userP = Pattern.compile("//(\\w+):");
        Matcher userM = userP.matcher(proxy);
        if (userM.find()) {
            return userM.group(1);
        } else {
            throw new RuntimeException("Invalid HTTP proxy username");
        }

    }

    private static String getProxyUserPassword(String proxy) {
        Pattern passP = Pattern.compile("://(\\w+):(\\w+)");
        Matcher passM = passP.matcher(proxy);
        if (passM.find()) {
            return passM.group(2);
        } else {
            throw new RuntimeException("Invalid HTTP proxy user password");
        }
    }

    private static String getProxyHost(String proxy) {
        return proxy.substring(proxy.indexOf("@") + 1, proxy.lastIndexOf(":"));
    }

    private static int getProxyPort(String proxy) {
        return Integer.parseInt(proxy.substring(proxy.lastIndexOf(":") + 1));
    }


    public static String getProxyString() {
        String proxyName = PropertiesUtil.getProperty("http.proxy.var.name");
        return System.getenv(proxyName);
    }

    public static boolean isProxyPresent() {
        Optional<String> optProxyName = Optional.ofNullable(PropertiesUtil.getProperty("http.proxy.var.name"));
        return optProxyName.isPresent() && System.getenv(optProxyName.get()) != null;
    }


}
