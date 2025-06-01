package testing.extensions;

import infrastructure.auth.SimpleAuthorizationProvider;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;
import org.junit.jupiter.api.extension.support.TypeBasedParameterResolver;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import testing.dsl.TestDataFactory;

public class TestDataFactoryExtension
        extends TypeBasedParameterResolver<TestDataFactory>
        implements TestInstancePostProcessor {

    private final ExtensionContext.Namespace namespace = ExtensionContext.Namespace.create(TestDataFactoryExtension.class);
    private final String key = "testDataFactory";

    @Override
    public void postProcessTestInstance(Object testInstance, ExtensionContext context) {
        TestDataFactory testDataFactory = buildTestDataFactory(context);
        setFieldInTestClass(testInstance, testDataFactory);

        context.getStore(namespace).put(key, testDataFactory);
    }

    @Override
    public TestDataFactory resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(namespace).get(key, TestDataFactory.class);
    }

    private static TestDataFactory buildTestDataFactory(ExtensionContext extensionContext) {
        ApplicationContext applicationContext = SpringExtension.getApplicationContext(extensionContext);
        SimpleAuthorizationProvider userAuthorizationProvider = applicationContext.getBean(SimpleAuthorizationProvider.class);

        return new TestDataFactory(userAuthorizationProvider);
    }

    private static void setFieldInTestClass(Object testInstance, TestDataFactory testDataFactory) {
        try {
            ReflectionTestUtils.setField(testInstance, null, testDataFactory, TestDataFactory.class);
        } catch (Exception e) {
            // no field to set
        }
    }

}
