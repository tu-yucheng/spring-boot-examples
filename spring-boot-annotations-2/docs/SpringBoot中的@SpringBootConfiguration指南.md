## 1. 概述

在本教程中，我们将简要讨论[@SpringBootConfiguration](https://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/SpringBootConfiguration.html)注解以及它的用法。

## 2. Spring Boot应用程序配置

**@SpringBootConfiguration是一个类级注解**，它是Spring Boot框架的一部分。它**表示一个类提供应用程序配置**。

Spring Boot倾向于基于Java的配置。因此，@SpringBootConfiguration注解是应用程序中配置的主要来源。通常，定义main()方法的类是此注解的良好候选者。


### 2.1 @SpringBootConfiguration

大多数Spring Boot程序通过[@SpringBootApplication](https://www.baeldung.com/spring-boot-annotations)自动应用@SpringBootConfiguration，这是一个继承自它的注解。如果应用程序使用了@SpringBootApplication，则也应用了@SpringBootConfiguration。

让我们看看@SpringBootConfiguration在应用程序中的用法。

首先，我们创建一个包含配置的应用程序类：

```java
@SpringBootConfiguration
public class AnnotationApplication {

    public static void main(String[] args) {
        SpringApplication.run(AnnotationApplication.class, args);
    }

    @Bean
    public PersonService personService() {
        return new PersonServiceImpl();
    }
}
```

@SpringBootConfiguration注解标注了AnnotationApplication类。这向Spring容器**表明该类具有@Bean定义方法**。换句话说，它包含实例化和配置依赖bean的方法。

例如，AnnotationApplication类包含PersonService bean的bean定义方法。

此外，容器处理配置类。反过来，这会为应用程序生成bean。因此，我们现在可以使用@Autowired或@Inject之类的[依赖注入注解](https://www.baeldung.com/inversion-control-and-dependency-injection-in-spring)。

### 2.2 @SpringBootConfiguration与@Configuration

@SpringBootConfiguration是[@Configuration](https://www.baeldung.com/spring-bean-annotations)注解的替代方法。主要区别在于@SpringBootConfiguration允许自动定位配置，这对于单元测试或集成测试特别有用。

**建议你的应用程序只有一个@SpringBootConfiguration或@SpringBootApplication**，大多数情况下我们都是使用@SpringBootApplication。

## 3. 总结

在本文中，我们快速介绍了@SpringBootConfiguration注解。此外，我们说明了@SpringBootConfiguration在Spring Boot应用程序中的用法。