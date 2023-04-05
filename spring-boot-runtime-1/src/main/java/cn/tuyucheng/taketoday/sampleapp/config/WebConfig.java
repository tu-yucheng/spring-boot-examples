package cn.tuyucheng.taketoday.sampleapp.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/*
 * Please note that main web configuration is in src/main/webapp/WEB-INF/api-servlet.xml
 */
@Configuration
@EnableWebMvc
@ComponentScan({"cn.tuyucheng.taketoday.sampleapp.web"})
public class WebConfig implements WebMvcConfigurer {

	public WebConfig() {
		super();
	}

    /*
    @Override
    public void configureMessageConverters(final List<HttpMessageConverter<?>> messageConverters) {
        final Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        builder.indentOutput(true)
            .dateFormat(new SimpleDateFormat("dd-MM-yyyy hh:mm"));
        messageConverters.add(new MappingJackson2HttpMessageConverter(builder.build()));
        // messageConverters.add(new MappingJackson2XmlHttpMessageConverter(builder.createXmlMapper(true).build()));

        // messageConverters.add(new MappingJackson2HttpMessageConverter());

        // messageConverters.add(new ProtobufHttpMessageConverter());

    }

    */
}
