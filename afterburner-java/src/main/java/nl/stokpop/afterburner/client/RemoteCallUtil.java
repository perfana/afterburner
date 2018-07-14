package nl.stokpop.afterburner.client;

public class RemoteCallUtil {

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
}
