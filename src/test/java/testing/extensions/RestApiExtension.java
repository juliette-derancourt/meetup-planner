package testing.extensions;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import testing.dsl.RestApi;

public class RestApiExtension implements TestInstancePostProcessor {

    @Override
    public void postProcessTestInstance(Object testInstance, ExtensionContext context) {
        RestApi restApi = buildRestApi(context);
        setFieldInTestClass(testInstance, restApi);
    }

    private static RestApi buildRestApi(ExtensionContext context) {
        ApplicationContext applicationContext = SpringExtension.getApplicationContext(context);
        MockMvc mockMvc = applicationContext.getBean(MockMvc.class);
        ObjectMapper objectMapper = applicationContext.getBean(ObjectMapper.class);

        return new RestApi(mockMvc, objectMapper);
    }

    private static void setFieldInTestClass(Object testInstance, RestApi restApi) {
        try {
            ReflectionTestUtils.setField(testInstance, null, restApi, RestApi.class);
        } catch (Exception e) {
            // no field to set
        }
    }

}
