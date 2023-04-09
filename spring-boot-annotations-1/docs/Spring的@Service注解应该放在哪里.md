## 1. 概述

作为软件开发人员，我们需要不断地寻找使用给定技术或库的最佳实践。当然，你给出的观点并不一定是其他人认为正确的。

其中一个争论是关于Spring的@Service注解的放置。由于Spring提供了定义bean的替代方法，因此值得关注构造型注解的使用。

在本教程中，我们将介绍**@Service注解放置在接口、抽象类或具体类上的不同效果**。

## 2. @Service标注接口

某些开发人员可能倾向于将@Service放在接口上，因为他们希望：

+ 明确表明接口只能用于Service级别目的
+ 定义新的Service实现并在启动期间将它们自动检测为Spring bean

让我们看看如果我们标注一个接口它会是什么样子：

```java
@Service
public interface AuthenticationService {

    boolean authenticate(String username, String password);
}
```

正如我们所注意到的，AuthenticationService现在变得更具自我描述性。@Service注解的存在可以更明确的提示开发人员仅将其用于业务层，而不用于数据访问层或任何其他层。

通常，这很好，但有一个缺点。**通过将@Service放在接口上，我们创建了一个额外的依赖项并将我们的接口与外部库耦合**。

接下来，为了测试新服务bean的自动检测，让我们创建AuthenticationService的实现：

```java
public class InMemoryAuthenticationService implements AuthenticationService {

    @Override
    public boolean authenticate(String username, String password) {
        return false;
    }
}
```

我们应该注意，我们的新实现InMemoryAuthenticationService上没有@Service注解。我们只在接口AuthenticationService上添加了@Service。

因此，让我们在基本的Spring Boot设置的帮助下运行我们的Spring上下文：

```java
@SpringBootApplication
public class AuthApplication {

    @Autowired
    private AuthenticationService authService;

    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }
}
```

当我们运行应用程序时，我们**得到臭名昭著的NoSuchBeanDefinitionException**，并且Spring上下文无法启动：

```shell
Field inMemoryAuthService in cn.tuyucheng.taketoday.annotations.service.AuthApplication required a bean of type 
'cn.tuyucheng.taketoday.annotations.service.interfaces.AuthenticationService' that could not be found.
```

因此，**将@Service放在接口上不足以自动检测Spring组件**。

## 3. @Service标注抽象类

在抽象类上使用@Service注解并不常见。

让我们对其进行测试，看看它是否达到了让Spring自动检测实现类的目标。

我们从头开始定义一个抽象类，并在其上放置@Service注解：

```java
@Service
public abstract class AbstractAuthenticationService {

    public boolean authenticate(String username, String password) {
        return false;
    }
}
```

接下来，我们扩展AbstractAuthenticationService类**创建一个具体的实现，不使用@Service标注**：

```java
public class LdapAuthenticationService extends AbstractAuthenticationService {

    @Override
    public boolean authenticate(String username, String password) {
        return true;
    }
}
```

然后在AuthApplication中注入AbstractAuthenticationService：

```java
@SpringBootApplication
public class AuthApplication {

    @Autowired
    private AbstractAuthenticationService ldapAuthService;

    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }
}
```

我们应该注意到，我们并没有试图在这里直接注入抽象类，这是不可能的。**相反，我们注入具体类LdapAuthenticationService的实例，仅取决于抽象类型**。正如[里氏替换原则](https://www.baeldung.com/java-liskov-substitution-principle)所建议的那样，这是一个很好的做法。

因此，我们再次运行AuthApplication：

```shell
Field ldapAuthService in cn.tuyucheng.taketoday.annotations.service.AuthApplication required a bean of type 
'cn.tuyucheng.taketoday.annotations.service.abstracts.AbstractAuthenticationService' that could not be found.
```

正如我们所看到的，Spring上下文没有成功启动，它**以相同的NoSuchBeanDefinitionException异常结束**。

当然，**在抽象类上使用@Service注解在Spring中没有任何效果**。

## 4. @Service标注具体类

与我们在上面看到的相反，标注实现类而不是抽象类或接口是一种很常见的做法。

通过这种方式，我们的目标主要是告诉Spring这个类将是一个[@Component](https://www.baeldung.com/spring-component-repository-service)并用一个特殊的构造型构建标记它，在我们的例子中是@Service。

因此，Spring将从类路径中自动检测这些类，并自动将它们定义为托管bean。

```java
@Service
public class InMemoryAuthenticationService implements AuthenticationService {

    @Override
    public boolean authenticate(String username, String password) {
        //...
    }
}

@Service
public class LdapAuthenticationService extends AbstractAuthenticationService {

    @Override
    public boolean authenticate(String username, String password) {
        //...
    }
}
```

我们应该注意，AbstractAuthenticationService在这里并没有实现AuthenticationService。因此，我们可以独立测试它们：

```java
@SpringBootApplication
public class AuthApplication {

    @Autowired
    private AuthenticationService inMemoryAuthService;

    @Autowired
    private AbstractAuthenticationService ldapAuthService;

    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }
}
```

**我们的最终测试给了我们一个成功的结果**，并且Spring上下文启动时没有任何异常。这两个服务都自动注册为beans。

## 5. 结论

最终，我们看到唯一可行的方法是将@Service放在我们的实现类上，使它们能够被自动检测。**Spring的[组件扫描](https://www.baeldung.com/spring-component-scanning)不会扫描任何类作为Spring Bean，除非它们被Spring注解标注，即使它们是从另一个@Service注解的接口或抽象类派生的**。

此外，[Spring文档](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/stereotype/Service.html)还指出，在实现类上使用@Service可以让组件扫描自动检测到它们。

## 6. 总结

具体来说，将@Service注解放在接口或抽象类上没有任何效果，并且只有具体类在使用@Service注解时才会被自动组件扫描。