package nl.stokpop.afterburner.client;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.*;

@Slf4j
public class RemoteCallUtil {

    private RemoteCallUtil() {}

    public static String createCompleteUrl(final String base, final String path) {
        if (base == null && path == null) return null;
        if (path == null) return base;
        if (base == null) return path;
        return String.join("/", base.trim(), removeFirstSlash(path.trim()));
    }

    public static String removeFirstSlash(final String path) {
        if (path == null) return null;
        return path.startsWith("/") ? path.substring(1) : path;
    }

    /**
     * get raw IP address for remote calls
     * @return IP address as String or null if failed
     */
    public static String getIpAddress(String remoteHostName, int remotePort) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(remoteHostName, remotePort), 200);
            InetAddress ipAddress = socket.getLocalAddress();
            return ipAddress.getHostAddress();
        } catch (IOException e) {
            log.warn("Cannot determine IP address for remote host: {}:{} : {}", remoteHostName, remotePort, e.getMessage());
            return null;
        }
    }

    /**
     * get raw IP address for remote calls
     * @return IP address as String or null if failed
     */
    public static String getIpAddress(String url) {
        URL parsedUrl;
        try {
            parsedUrl = new URL(url);
            String host = parsedUrl.getHost();
            if (host.equals("localhost") || host.equals("127.0.0.1")) {
                return "127.0.0.1";
            } else {
                return getIpAddress(host, parsedUrl.getPort() == -1 ? 80 : parsedUrl.getPort());
            }

        } catch (MalformedURLException e) {
            log.warn("Cannot determine IP address: {}", e.getMessage());
            return null;
        }
    }
}
