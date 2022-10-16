package com.vg.openid;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.vg.openid.keycloak.KeycloakApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

@SpringBootApplication
@Slf4j
public class OIDCDemo implements CommandLineRunner {

    @Value("${oidc.apiKey}")
    private String apiKey;

    @Value("${oidc.apiSecret}")
    private String apiSecret;

    @Value("${oidc.callbackurl}")
    private String callback;

    @Value("${oidc.baseurl}")
    private String baseUrl;

    @Value("${oidc.realm}")
    private String realm;

    @SuppressWarnings("PMD.SystemPrintln")
    public static void main(String[] args)
        throws IOException, InterruptedException, ExecutionException {
        SpringApplication.run(OIDCDemo.class, args);
    }



    @Override
    public void run(String... args) throws Exception {

        final String protectedResourceUrl =
            baseUrl + "/realms/" + realm + "/protocol/openid-connect/userinfo";

        final OAuth20Service service =
            new ServiceBuilder(apiKey).apiSecret(apiSecret).defaultScope("openid")
                .callback(callback).build(KeycloakApi.instance(baseUrl, realm));
        final Scanner in = new Scanner(System.in);

        log.info("=== Keyloack's OAuth Workflow ===");

        // Obtain the Authorization URL
        log.info("Fetching the Authorization URL...");
        final String authorizationUrl = service.getAuthorizationUrl();
        log.info("Got the Authorization URL!");
        log.info("Now go and authorize ScribeJava here:");
        log.info(authorizationUrl);
        log.info("And paste the authorization code here");
        System.out.print(">>");
        final String code = in.nextLine();

        log.info("Trading the Authorization Code for an Access Token...");
        final OAuth2AccessToken accessToken = service.getAccessToken(code);
        log.info("Got the Access Token!");
        log.info("(The raw response looks like this: " + accessToken.getRawResponse() + "')");

        // Now let's go and ask for a protected resource!
        log.info("Now we're going to access a protected resource...");
        final OAuthRequest request = new OAuthRequest(Verb.GET, protectedResourceUrl);
        service.signRequest(accessToken, request);
        try (Response response = service.execute(request)) {
            log.info("Got it! Lets see what we found... response code -  {}, " + "body - {}",
                response.getCode(), response.getBody());
        }
        log.info("That's  !! Go and build something awesome with ScribeJava! :)");
    }
}
