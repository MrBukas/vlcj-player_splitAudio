package uk.co.caprica.vlcjplayer.server;

public class ServerUtils {

    public static boolean isServer() {
        return System.getenv("isMain").equals("1");
    }

    public static boolean isListener() {
        return System.getenv("isMain").equals("0");
    }
}
