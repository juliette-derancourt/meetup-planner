package infrastructure.auth;

import domain.spi.UserAuthorizationProvider;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class SimpleAuthorizationProvider implements UserAuthorizationProvider {

    private final List<UUID> organizers = new ArrayList<>();

    @Override
    public boolean isOrganizer(UUID userId) {
        return organizers.contains(userId);
    }

    public void addOrganizer(UUID userId) {
        organizers.add(userId);
    }

}
