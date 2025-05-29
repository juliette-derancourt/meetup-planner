package testing.extensions;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.ReflectionUtils;
import testing.dsl.RestApi;

import java.lang.reflect.Field;

public class RestApiExtension implements TestInstancePostProcessor {

    @Override
    public void postProcessTestInstance(Object testInstance, ExtensionContext context) {
        if (hasRestApiField(testInstance)) {
            RestApi restApi = createRestApi(context);
            setFieldInTestInstance(testInstance, restApi);
        }
    }

    private boolean hasRestApiField(Object testInstance) {
        Field field = ReflectionUtils.findField(testInstance.getClass(), null, RestApi.class);
        return field != null;
    }

    private static RestApi createRestApi(ExtensionContext context) {
        ApplicationContext applicationContext = SpringExtension.getApplicationContext(context);
        MockMvc mockMvc = applicationContext.getBean(MockMvc.class);
        ObjectMapper objectMapper = applicationContext.getBean(ObjectMapper.class);

        return new RestApi(mockMvc, objectMapper);
    }

    private static void setFieldInTestInstance(Object testInstance, RestApi restApi) {
        ReflectionTestUtils.setField(testInstance, null, restApi, RestApi.class);
    }

}
