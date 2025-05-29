package testing.extensions;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

/**
 * This extension waits for 0.5 seconds at the beginning of every test.
 * This is used to demonstrate the effect of parallel execution (configured
 * in {@code src/test/resources/junit-platform.properties}).
 * <p>
 * To register this extension globally, add
 * {@code junit.jupiter.extensions.autodetection.enabled=true}
 * in {@code src/test/resources/junit-platform.properties}.
 */
public class SlowDownExtension implements BeforeEachCallback {

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        Thread.sleep(500);
    }

}
