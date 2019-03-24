package nl.stokpop.afterburner.controller;

import nl.stokpop.afterburner.AfterburnerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

@RestController
public class TcpConnector {

    private final AfterburnerProperties props;

    @Autowired
    public TcpConnector(JdbcTemplate template, AfterburnerProperties props) {
        this.props = props;
    }

    @RequestMapping("/tcp/connect")
    public BurnerMessage tcpConnect(
            @RequestParam(value = "host", defaultValue = "localhost") String host,
            @RequestParam(value = "port", defaultValue = "8080") int port,
            @RequestParam(value = "timeout-ms", defaultValue = "5000") int timeoutInMillis) {
        long startTime = System.currentTimeMillis();

        Socket socket = null;
        try {
            socket = new Socket();
            SocketAddress address = new InetSocketAddress(host, port);
            long nanoStartTime = System.nanoTime();
            socket.connect(address, timeoutInMillis);
            long estimatedConnectTime = System.nanoTime() - nanoStartTime;

            nanoStartTime = System.nanoTime();
            socket.close();
            long estimatedCloseTime = System.nanoTime() - nanoStartTime;

            String message = String.format("{ 'tcp-connect':'success', 'connect-duration-nanos':%d, 'close-duration-nanos':%d, 'host':'%s', 'port':%d }",
                    estimatedConnectTime, estimatedCloseTime, host, port);

            long durationMillis = System.currentTimeMillis() - startTime;
            return new BurnerMessage(message, props.getAfterburnerName(), durationMillis);

        } catch (IOException e) {
            String message = String.format("{ 'tcp-connect':'failure', 'error':'%s', 'host':'%s', 'port':%d }",
                    e.getMessage(), host, port);
            long durationMillis = System.currentTimeMillis() - startTime;
            return new BurnerMessage(message, props.getAfterburnerName(), durationMillis);
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
