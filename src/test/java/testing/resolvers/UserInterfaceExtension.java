package testing.resolvers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;
import org.junit.jupiter.api.extension.support.TypeBasedParameterResolver;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import testing.dsl.UserInterface;

import static org.junit.jupiter.api.extension.ExtensionContext.Namespace;

public class UserInterfaceExtension extends TypeBasedParameterResolver<UserInterface> implements TestInstancePostProcessor {

    private final Namespace namespace = Namespace.create(UserInterfaceExtension.class);
    private final String key = "userInterface";

    @Override
    public void postProcessTestInstance(Object testInstance, ExtensionContext context) {
        UserInterface userInterface = buildUserInterface(context);
        setFieldInTest(testInstance, userInterface);

        context.getStore(namespace).put(key, userInterface);
    }

    @Override
    public UserInterface resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(namespace).get(key, UserInterface.class);
    }

    private static UserInterface buildUserInterface(ExtensionContext context) {
        ApplicationContext applicationContext = SpringExtension.getApplicationContext(context);
        MockMvc mockMvc = applicationContext.getBean(MockMvc.class);
        ObjectMapper objectMapper = applicationContext.getBean(ObjectMapper.class);

        return new UserInterface(mockMvc, objectMapper);
    }

    private static void setFieldInTest(Object testInstance, UserInterface userInterface) {
        try {
            ReflectionTestUtils.setField(testInstance, null, userInterface, UserInterface.class);
        } catch (Exception e) {
            // no field
        }
    }

}
