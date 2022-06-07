package nl.stokpop.afterburner.client;

import feign.Client;
import feign.Feign;
import feign.Retryer;
import feign.httpclient.ApacheHttpClient;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.binder.httpcomponents.PoolingHttpClientConnectionManagerMetricsBinder;
import nl.stokpop.afterburner.AfterburnerException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;

@Configuration
public class AfterburnerClientConfig {

    @Value("${afterburner.feign.client-type:DEFAULT}")
    private FeignClientType feignClientType;

    public enum FeignClientType {
        DEFAULT, HC4
    }

    @Bean
    public CloseableHttpClient feignHttpClient() {

        RequestConfig config = RequestConfig.custom()
                .setConnectionRequestTimeout(250)
                .setConnectTimeout(100)
                .setSocketTimeout(100)
                .build();

        SSLContext sslContext = setupSSLContextForMutualTLS();

        SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContext,  new DefaultHostnameVerifier());

        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("https", socketFactory)
                .build();

        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
        connectionManager.setDefaultMaxPerRoute(10);
        connectionManager.setMaxTotal(10);

        PoolingHttpClientConnectionManagerMetricsBinder metrics =
            new PoolingHttpClientConnectionManagerMetricsBinder(connectionManager, "afterburner-http-client");
        metrics.bindTo(Metrics.globalRegistry);

        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create()
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(config)
                .disableConnectionState();

        return httpClientBuilder.build();
    }

    @NotNull
    private SSLContext setupSSLContextForMutualTLS() {
        try {
            String password = "changeit";
            KeyStore trustStore = loadKeyStore("/keystore/client-truststore.p12", password);
            KeyStore keyStore = loadKeyStore("/keystore/client.p12", password);

            return SSLContexts.custom()
                    .loadKeyMaterial(keyStore, password.toCharArray())
                    .loadTrustMaterial(trustStore, new TrustSelfSignedStrategy())
                    .build();

        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException | UnrecoverableKeyException e) {
            throw new AfterburnerException("Failed to load keystores for mutual TLS.", e);
        }
    }

    @NotNull
    private KeyStore loadKeyStore(String keyStorePath, String password) {
        try {
            KeyStore trustStore = KeyStore.getInstance("pkcs12");
            ClassPathResource resource = new ClassPathResource(keyStorePath);
            loadKeyStoreFromClassPathResource(password, trustStore, resource);
            return trustStore;
        } catch (KeyStoreException e) {
            throw new AfterburnerException("Failed to load keystore: " + keyStorePath, e);
        }
    }

    private void loadKeyStoreFromClassPathResource(String password, KeyStore trustStore, ClassPathResource resource) {
        try (InputStream fis = resource.getInputStream()) {
            trustStore.load(fis, password.toCharArray());
        } catch (IOException | CertificateException | NoSuchAlgorithmException e) {
            throw new AfterburnerException("Failed to load keystore: " + resource, e);
        }
    }

    @Bean
    public Feign.Builder feignBuilder(CloseableHttpClient client) {
        Client feignClient = feignClientType == FeignClientType.DEFAULT
                ? new Client.Default(setupSSLContextForMutualTLS().getSocketFactory(), new DefaultHostnameVerifier())
                : new ApacheHttpClient(client);
        return Feign.builder()
                .retryer(Retryer.NEVER_RETRY)
                .client(feignClient);
    }
}
