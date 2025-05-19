package com.shop_sphere.customer_service.service;

import com.shop_sphere.customer_service.config.KeycloakPropsConfig;
import com.shop_sphere.customer_service.exception.CustomerNotFoundException;
import com.shop_sphere.customer_service.exception.DuplicatedException;
import com.shop_sphere.customer_service.viewmodel.customer.CustomerListVm;
import com.shop_sphere.customer_service.viewmodel.customer.CustomerPostVm;
import com.shop_sphere.customer_service.viewmodel.customer.CustomerUpdateVm;
import com.shop_sphere.customer_service.viewmodel.customer.CustomerVm;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class CustomerService {

    private static final int USER_PER_PAGE = 10;
    private final Keycloak keycloak;
    private final KeycloakPropsConfig keycloakPropsConfig;

    public CustomerService(Keycloak keycloak,
                        KeycloakPropsConfig keycloakPropsConfig) {
        this.keycloak = keycloak;
        this.keycloakPropsConfig = keycloakPropsConfig;
    }

    private CredentialRepresentation passwordCredential(String password) {
        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setTemporary(false);
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setValue(password);
        return credentialRepresentation;
    }

    private boolean checkUsernameExists(RealmResource realmResource, String username) {
         List<UserRepresentation> userRepresentations = realmResource.users().search(username, true);
         return !userRepresentations.isEmpty();
    }

    private boolean checkEmailExists(RealmResource realmResource, String email) {
        List<UserRepresentation> userRepresentations = realmResource.users().searchByEmail(email, true);
        return !userRepresentations.isEmpty();
    }



    public CustomerListVm getCustomers(int pageNo) {

        // Search for users whose username or email matches the value provided by search.
        List<UserRepresentation> userRepresentations =
                keycloak.realm(keycloakPropsConfig.getRealm())
                        .users()
                        .search(null, pageNo * USER_PER_PAGE, USER_PER_PAGE);
        // Skip previous (pageNo * USER_PER_PAGE), get next (USER_PER_PAGE)
        List<CustomerVm> customers = userRepresentations.stream()
                .filter(UserRepresentation::isEnabled)
                .map(CustomerVm::fromUserRepresentation)
                .toList();

        int size = userRepresentations.size();
        int totalPage = (size + USER_PER_PAGE - 1) / USER_PER_PAGE;

        return new CustomerListVm(size, customers, totalPage);
    }

    public CustomerVm getCustomerProfile(String username) {
        UserRepresentation userRepresentation = keycloak.realm(keycloakPropsConfig.getRealm())
                                                        .users()
                                                        .get(username)
                                                        .toRepresentation();

        if(userRepresentation == null)
            throw new CustomerNotFoundException("USERNAME", username);

        return CustomerVm.fromUserRepresentation(userRepresentation);
    }

    public CustomerVm getCustomerById(String userId) {
        try {
            UserRepresentation userRepresentation = keycloak.realm(keycloakPropsConfig.getRealm())
                                                            .users()
                                                            .get(userId)
                                                            .toRepresentation();
            return CustomerVm.fromUserRepresentation(userRepresentation);
        } catch (NotFoundException exception) {
            throw new CustomerNotFoundException("ID", userId);
        }
    }

    public CustomerVm getCustomerByEmail(String email) {
        try {
            List<UserRepresentation> userRepresentation = keycloak.realm(keycloakPropsConfig.getRealm())
                    .users()
                    .searchByEmail(email, true);
            return CustomerVm.fromUserRepresentation(userRepresentation.getFirst());
        } catch (NotFoundException exception) {
            throw new CustomerNotFoundException("EMAIL", email);
        }
    }

    public CustomerVm create(CustomerPostVm customerPostVm) {
        // Get realms
        RealmResource realmResource = keycloak.realm(keycloakPropsConfig.getRealm());

        if (checkUsernameExists(realmResource, customerPostVm.username()))
            throw new DuplicatedException(customerPostVm.username());

        if (checkEmailExists(realmResource, customerPostVm.email()))
            throw new DuplicatedException(customerPostVm.email());

        // Define credential representation
        CredentialRepresentation credentialRepresentation = passwordCredential(customerPostVm.password());

        // Define new user
        UserRepresentation user = new UserRepresentation();
        user.setUsername(customerPostVm.username());
        user.setCredentials(Collections.singletonList(credentialRepresentation));
        user.setEmail(customerPostVm.email());
        user.setFirstName(customerPostVm.firstName());
        user.setLastName(customerPostVm.lastName());
        user.setEnabled(true);
        Response response = realmResource.users().create(user);

        // Get new user
        String userId = CreatedResponseUtil.getCreatedId(response);
        UserResource userResource = realmResource.users().get(userId);

        // Assign role to new user
        RoleRepresentation roleRepresentation = realmResource.roles().get(customerPostVm.role()).toRepresentation();
        userResource.roles().realmLevel().add(Collections.singletonList(roleRepresentation));

        return CustomerVm.fromUserRepresentation(user);
    }

    public void updateCustomerById(String userId, CustomerUpdateVm customerUpdateVm) {
        RealmResource realmResource = keycloak.realm(keycloakPropsConfig.getRealm());
        UserResource userResource = realmResource.users().get(userId);
        UserRepresentation userRepresentation = userResource.toRepresentation();
        if(userRepresentation == null)
            throw new CustomerNotFoundException("ID", userId);

        // Patch user profile
        if(customerUpdateVm.email() != null)
            userRepresentation.setEmail(customerUpdateVm.email());
        if(customerUpdateVm.firstName() != null)
            userRepresentation.setFirstName(customerUpdateVm.firstName());
        if(customerUpdateVm.lastName() != null)
            userRepresentation.setLastName(customerUpdateVm.lastName());

        // Update user in Keycloak
        userResource.update(userRepresentation);
    }

    public void deleteCustomerById(String userId) {
        RealmResource realmResource = keycloak.realm(keycloakPropsConfig.getRealm());
        UserResource userResource = realmResource.users().get(userId);
        UserRepresentation userRepresentation = userResource.toRepresentation();

        if(userRepresentation == null)
            throw new CustomerNotFoundException("ID", userId);

        // inactive user
        userRepresentation.setEnabled(false);

        // Update user in Keycloak
        userResource.update(userRepresentation);
    }
}
