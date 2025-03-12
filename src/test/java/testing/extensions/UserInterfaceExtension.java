package testing.extensions;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import testing.dsl.UserInterface;

public class UserInterfaceExtension implements TestInstancePostProcessor {

    @Override
    public void postProcessTestInstance(Object testInstance, ExtensionContext context) {
        UserInterface userInterface = buildUserInterface(context);
        setFieldInTestClass(testInstance, userInterface);
    }

    private static UserInterface buildUserInterface(ExtensionContext context) {
        ApplicationContext applicationContext = SpringExtension.getApplicationContext(context);
        MockMvc mockMvc = applicationContext.getBean(MockMvc.class);
        ObjectMapper objectMapper = applicationContext.getBean(ObjectMapper.class);

        return new UserInterface(mockMvc, objectMapper);
    }

    private static void setFieldInTestClass(Object testInstance, UserInterface userInterface) {
        try {
            ReflectionTestUtils.setField(testInstance, null, userInterface, UserInterface.class);
        } catch (Exception e) {
            // no field to set
        }
    }

}
