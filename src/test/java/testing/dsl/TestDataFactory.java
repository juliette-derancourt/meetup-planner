package testing.dsl;

import infrastructure.auth.SimpleAuthorizationProvider;

import java.util.UUID;

public class TestDataFactory {

    private final SimpleAuthorizationProvider authorizationProvider;

    public TestDataFactory(SimpleAuthorizationProvider authorizationProvider) {
        this.authorizationProvider = authorizationProvider;
    }

    public UUID givenAnOrganizer(String uuid) {
        UUID organizerId = UUID.fromString(uuid);
        authorizationProvider.addOrganizer(organizerId);
        return organizerId;
    }

}
