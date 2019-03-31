class TorProxySettings {
    static void configure(boolean flag) {
        if (flag) {
            System.getProperties().put("proxySet", "true");
            System.getProperties().put("socksProxyHost", "127.0.0.1");
            System.getProperties().put("socksProxyPort", "9050");
        }
    }
}
