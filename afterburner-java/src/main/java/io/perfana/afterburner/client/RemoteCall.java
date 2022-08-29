package io.perfana.afterburner.client;

import java.io.IOException;

public interface RemoteCall {

    /**
     * Perform a call on the given url path.
     *
     * The prefix of the url should be provided via the constructor and via
     * application settings to avoid surprise calls to any domain.
     *
     * @param path the part of the url after [http://my.domain:8080/][path]
     * @return the result of the remote call (if any)
     */
    String call(String path) throws RemoteCallException, IOException;

}
