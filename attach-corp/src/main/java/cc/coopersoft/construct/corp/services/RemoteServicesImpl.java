package cc.coopersoft.construct.corp.services;

import cc.coopersoft.construct.corp.model.CorpEmployee;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.keycloak.representations.idm.UserRepresentation;

import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.Collections;

@Service
@Slf4j
@RefreshScope
public class RemoteServicesImpl implements RemoteServices {

    //https://keycloak.discourse.group/t/keycloak-admin-client-in-spring-boot/2547
    //https://gist.github.com/thomasdarimont/c4e739c5a319cf78a4cff3b87173a84b

    @Value("${trust.keycloak.url:}")
    private String serverUrl = "http://192.168.1.21:8901/auth";
    @Value("${trust.keycloak.realm:}")
    private String realm = "trust";
    // idm-client needs to allow "Direct Access Grants: Resource Owner Password Credentials Grant"
    @Value("${trust.keycloak.clientId:}")
    private String clientId = "corp-admin";
    @Value("${trust.keycloak.clientSecret:}")
    private String clientSecret = "c98309e2-0edc-4bbe-9a28-acb020d6cf7d";

    private final Source source;


    @Autowired
    public RemoteServicesImpl(Source source) {
        this.source = source;
    }

    @Override
    public void publishChangeMessage(long code){
        log.debug(" sending message corp {} change ", code);
        source.output().send(MessageBuilder.withPayload(code).build());
    }

    @Override
    public String addUser(long corp, CorpEmployee employee) {
        Keycloak keycloak = KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .realm(realm)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .resteasyClient(
                        new ResteasyClientBuilder()
                                .connectionPoolSize(10).build()
                ).build();

        // Get realm
        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();

        String username = employee.getUsername();

        int i = 0;
        while (usersResource.search(username).size() > 0){
            username = username + i;
        }

        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setUsername(username);
        user.setFirstName(employee.getName());
        //user.setLastName("Last");
        //user.setEmail();
        user.setAttributes(Collections.singletonMap("origin", Arrays.asList(String.valueOf(corp))));



        // Create user (requires manage-users role)
        Response response = usersResource.create(user);
        log.debug("Response: " + response.getStatus() + " " + response.getStatusInfo());
        log.debug("Response location: " + response.getLocation());
        String userId = CreatedResponseUtil.getCreatedId(response);
        log.debug("User created with userId: " + userId);

        // Define password credential
        CredentialRepresentation passwordCred = new CredentialRepresentation();
        passwordCred.setTemporary(true);
        passwordCred.setType(CredentialRepresentation.PASSWORD);
        passwordCred.setValue(username);

        UserResource userResource = usersResource.get(userId);

        // Set password credential
        userResource.resetPassword(passwordCred);

//        GroupsResource groupsResource = realmResource.groups();
//        groupsResource.groups()

//        // Get realm role "tester" (requires view-realm role)
//        RoleRepresentation testerRealmRole = realmResource.roles()//
//                .get("tester").toRepresentation();
//
//        // Assign realm role tester to user
//        userResource.roles().realmLevel() //
//                .add(Arrays.asList(testerRealmRole));
//
//        // Get client
//        ClientRepresentation app1Client = realmResource.clients() //
//                .findByClientId("app-frontend-springboot").get(0);
//
//        // Get client level role (requires view-clients role)
//        RoleRepresentation userClientRole = realmResource.clients().get(app1Client.getId()) //
//                .roles().get("user").toRepresentation();
//
//        // Assign client level role to user
//        userResource.roles() //
//                .clientLevel(app1Client.getId()).add(Arrays.asList(userClientRole));

        // Send password reset E-Mail
        // VERIFY_EMAIL, UPDATE_PROFILE, CONFIGURE_TOTP, UPDATE_PASSWORD, TERMS_AND_CONDITIONS
//        usersRessource.get(userId).executeActionsEmail(Arrays.asList("UPDATE_PASSWORD"));

        // Delete User
//        userResource.remove();


        return username;
    }

}
