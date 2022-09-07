package io.perfana.afterburner.wiremock;

import com.github.tomakehurst.wiremock.WireMockServer;

import java.util.Scanner;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

public class AfterburnerStub {
    public static void main(String[] args) {
        int portNumber = 8060;
        WireMockServer wireMockServer = new WireMockServer(wireMockConfig().port(portNumber));
        wireMockServer.start();

        Scanner s = new Scanner(System.in);
        System.out.println("Listening on port: " + portNumber + " Press enter to stop Wiremock.....");
        s.nextLine();

        wireMockServer.stop();
    }
}
