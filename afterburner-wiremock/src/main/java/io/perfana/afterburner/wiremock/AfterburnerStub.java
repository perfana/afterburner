package io.perfana.afterburner.wiremock;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;

import java.util.Scanner;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

public class AfterburnerStub {
    public static void main(String[] args) {
        WireMockServer wireMockServer = new WireMockServer(wireMockConfig().port(8060));
        wireMockServer.start();

        Scanner s = new Scanner(System.in);
        System.out.println("Press enter to continue.....");
        s.nextLine();

        WireMock.reset();
        wireMockServer.stop();
    }
}
