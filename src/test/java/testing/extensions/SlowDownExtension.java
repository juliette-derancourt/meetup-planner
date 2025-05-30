package testing.extensions;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

/**
 * This extension waits for 1 seconds at the beginning of every test.
 */
public class SlowDownExtension implements BeforeEachCallback {

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        Thread.sleep(1000);
    }

}
