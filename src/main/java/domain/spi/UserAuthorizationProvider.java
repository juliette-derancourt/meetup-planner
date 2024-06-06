package domain.spi;

import java.util.UUID;

public interface UserAuthorizationProvider {

    boolean isOrganizer(UUID userId);

}
