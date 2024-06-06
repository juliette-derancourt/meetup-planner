package testing.resolvers;

import infrastructure.auth.SimpleAuthorizationProvider;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.support.TypeBasedParameterResolver;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import testing.dsl.TestDataFactory;

public class TestDataFactoryResolver extends TypeBasedParameterResolver<TestDataFactory> {

    @Override
    public TestDataFactory resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        ApplicationContext applicationContext = SpringExtension.getApplicationContext(extensionContext);
        SimpleAuthorizationProvider userAuthorizationProvider = applicationContext.getBean(SimpleAuthorizationProvider.class);
        return new TestDataFactory(userAuthorizationProvider);
    }

}
