## 1. 概述

在本文中，我们将介绍[Ehcache](http://www.ehcache.org/)，这是一种广泛使用的基于Java的开源缓存。它具有内存和磁盘存储、监听器、缓存加载器、Restful和SOAP API以及其他非常有用的功能。

为了展示缓存如何优化我们的应用程序，我们将创建一个简单的方法来计算所提供数字的平方值。在每次调用时，该方法将调用calculateSquareOfNumber(int number)方法并将信息打印到控制台。

通过这个简单的例子，我们想演示的是平方值的计算只执行一次，并且具有相同输入值的所有其他调用都从缓存中返回结果。

重要的是要注意，我们完全专注于Ehcache本身(不使用Spring)；如果你想了解Ehcache如何与Spring配合使用，请阅读[这篇](Spring缓存指南.md)文章。

## 2. Maven依赖

为了使用Ehcache，我们需要添加以下Maven依赖项：

```xml
<dependency>
    <groupId>org.ehcache</groupId>
    <artifactId>ehcache</artifactId>
    <version>3.5.2</version>
</dependency>
```

最新版本的Ehcache工件可以在[这里](https://central.sonatype.com/artifact/org.ehcache/ehcache/3.10.8)找到。

## 3. 缓存配置

Ehcache可以通过两种方式进行配置：

+ 第一种方式是通过Java POJO，其中所有配置参数都通过Ehcache API进行配置
+ 第二种方式是通过XML文件进行配置，我们可以根据提供的[模式定义](http://www.ehcache.org/documentation/3.1/xsds.html#core)配置Ehcache

在本文中，我们将展示这两种方法–Java和XML配置。

### 3.1 Java配置

本小节将展示使用POJO配置Ehcache。此外，我们将创建一个工具类，以便于缓存配置和可用性：

```java
public class CacheHelper {

    private final CacheManager cacheManager;

    private final Cache<Integer, Integer> squareNumberCache;

    public CacheHelper() {
        cacheManager = CacheManagerBuilder.newCacheManagerBuilder().build();
        cacheManager.init();
        squareNumberCache = cacheManager.createCache("squareNumberCache",
              CacheConfigurationBuilder.newCacheConfigurationBuilder(Integer.class, Integer.class, ResourcePoolsBuilder.heap(10)));
    }

    public Cache<Integer, Integer> getSquareNumberCache() {
        return squareNumberCache;
    }

    public Cache<Integer, Integer> getSquareNumberCacheFromCacheManager() {
        return cacheManager.getCache("squareNumberCache", Integer.class, Integer.class);
    }
}
```

要初始化我们的缓存，首先，我们需要定义Ehcache的CacheManager对象。在这个例子中，我们使用newCacheManagerBuilder() API创建一个默认的缓存squaredNumber。

缓存简单地将整数键映射到整数值。

请注意，在开始使用定义的缓存之前，我们需要使用init()方法初始化CacheManager对象。

最后，要获取我们的缓存，我们只需使用getCache() API以及提供的缓存名称、键和值类型。

通过这几行代码，我们创建了第一个缓存，现在可供我们的应用程序使用。

### 3.2 XML配置

3.1小节中的配置对象，等同于使用此XML配置：

```xml
<cache-template name="squaredNumber">
    <key-type>java.lang.Integer</key-type>
    <value-type>java.lang.Integer</value-type>
    <heap unit="entries">10</heap>
</cache-template>
```

要在Java应用程序中包含这个缓存，我们需要在Java中读取XML配置文件：

```java
URL myUrl = getClass().getResource(xmlFile); 
XmlConfiguration xmlConfig = new XmlConfiguration(myUrl); 
CacheManager myCacheManager = CacheManagerBuilder.newCacheManager(xmlConfig);
```

## 4. Ehcache测试

在第3节中，我们展示了如何为你的目的定义简单缓存。为了证明缓存确实有效，我们将创建SquaredCalculator类，该类将计算所提供输入的平方值，并将计算出的值存储在缓存中。

当然，如果缓存中已经包含计算值，我们将返回缓存值以避免不必要的计算：

```java
public class SquaredCalculator {
    private static final Logger LOGGER = LoggerFactory.getLogger(SquaredCalculator.class);

    private CacheHelper cache;

    public void setCache(CacheHelper cache) {
        this.cache = cache;
    }

    public int getSquareValueOfNumber(int input) {
        if (cache.getSquareNumberCache().containsKey(input))
            return cache.getSquareNumberCache().get(input);
        LOGGER.info("Calculating square value of {} and caching result.", input);
        int squaredValue = (int) Math.pow(input, 2);
        cache.getSquareNumberCache().put(input, squaredValue);
        return squaredValue;
    }
}
```

为了完成我们的测试场景，我们还需要计算平方值的代码：

```java
class SquareCalculatorUnitTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(SquareCalculatorUnitTest.class);
    private final SquaredCalculator squaredCalculator = new SquaredCalculator();
    private final CacheHelper cacheHelper = new CacheHelper();

    @BeforeEach
    void setup() {
        squaredCalculator.setCache(cacheHelper);
    }

    @Test
    @DisplayName("whenCalculatingSquareValueAgain_thenCacheHasAllValues")
    void whenCalculatingSquareValueAgain_thenCacheHasAllValues() {
        for (int i = 10; i < 15; i++) {
            assertFalse(cacheHelper.getSquareNumberCache().containsKey(i));
            LOGGER.info("{}的平方值为: {}", i, squaredCalculator.getSquareValueOfNumber(i));
        }

        for (int i = 10; i < 15; i++) {
            assertTrue(cacheHelper.getSquareNumberCache().containsKey(i));
            LOGGER.info("{}的平方值为: {}", i, squaredCalculator.getSquareValueOfNumber(i) + "\n");
        }
    }
}
```

如果我们运行测试，可以在控制台中看到如下日志输出：

```shell
20:10:09.715 [main] INFO [c.t.t.e.calculator.SquaredCalculator] >>> 计算10的平方值并缓存结果. 
20:10:09.715 [main] INFO [c.t.t.e.SquareCalculatorUnitTest] >>> 10的平方值为: 100 
20:10:09.715 [main] INFO [c.t.t.e.calculator.SquaredCalculator] >>> 计算11的平方值并缓存结果. 
20:10:09.715 [main] INFO [c.t.t.e.SquareCalculatorUnitTest] >>> 11的平方值为: 121 
20:10:09.715 [main] INFO [c.t.t.e.calculator.SquaredCalculator] >>> 计算12的平方值并缓存结果. 
20:10:09.715 [main] INFO [c.t.t.e.SquareCalculatorUnitTest] >>> 12的平方值为: 144 
20:10:09.715 [main] INFO [c.t.t.e.calculator.SquaredCalculator] >>> 计算13的平方值并缓存结果. 
20:10:09.715 [main] INFO [c.t.t.e.SquareCalculatorUnitTest] >>> 13的平方值为: 169 
20:10:09.715 [main] INFO [c.t.t.e.calculator.SquaredCalculator] >>> 计算14的平方值并缓存结果. 
20:10:09.715 [main] INFO [c.t.t.e.SquareCalculatorUnitTest] >>> 14的平方值为: 196 

20:10:09.715 [main] INFO [c.t.t.e.SquareCalculatorUnitTest] >>> 10的平方值为: 100
20:10:09.715 [main] INFO [c.t.t.e.SquareCalculatorUnitTest] >>> 11的平方值为: 121
20:10:09.715 [main] INFO [c.t.t.e.SquareCalculatorUnitTest] >>> 12的平方值为: 144
20:10:09.715 [main] INFO [c.t.t.e.SquareCalculatorUnitTest] >>> 13的平方值为: 169
20:10:09.715 [main] INFO [c.t.t.e.SquareCalculatorUnitTest] >>> 14的平方值为: 196
```

可以看到，getSquareValueOfNumber()方法仅在第一次调用时进行计算。在第二次调用中，所有值都在缓存中找到并返回。

## 5. 其他Ehcache配置选项

对于我们上面创建的缓存，它是一个没有任何特殊配置的简单缓存。本节将展示在缓存创建中一些有用的其他选项。

### 5.1 磁盘持久性

如果有太多值要存储到缓存中，我们可以将其中一些值存储在硬盘上。

```java
PersistentCacheManager persistentCacheManager = CacheManagerBuilder.newCacheManagerBuilder()
        .with(CacheManagerBuilder.persistence(getStoragePath()
            + File.separator 
            + "squaredValue")) 
        .withCache("persistent-cache", CacheConfigurationBuilder
            .newCacheConfigurationBuilder(Integer.class, Integer.class,
                ResourcePoolsBuilder.newResourcePoolsBuilder()
                    .heap(10, EntryUnit.ENTRIES)
                    .disk(10, MemoryUnit.MB, true)) 
            )
    .build(true);

persistentCacheManager.close();
```

这里我们使用PersistentCacheManager代替默认的CacheManager，它将持久化所有无法保存到内存中的值。

从配置中，我们可以看到缓存会将10个元素保存到内存中，并在硬盘上分配10MB用于持久化。

### 5.2 数据过期

如果我们缓存了大量数据，通常会在隔一段时间后将缓存数据过期，这样可以避免大量的内存使用。

Ehcache通过Expiry接口控制数据的过期时效：

```java
CacheConfiguration<Integer, Integer> cacheConfiguration = CacheConfigurationBuilder
    .newCacheConfigurationBuilder(Integer.class, Integer.class, ResourcePoolsBuilder.heap(100)) 
    .withExpiry(Expirations.timeToLiveExpiration(Duration.of(60, TimeUnit.SECONDS))).build();
```

在这个缓存中，所有数据将存活60秒，并在该时间段后从内存中删除。

## 6. 总结

在本文中，我们展示了如何在Java应用程序中使用简单的Ehcache缓存。

在我们的示例中，我们看到即使是简单配置的缓存也可以节省大量不必要的操作。此外，我们还演示了通过POJO和XML配置缓存，并且Ehcache具有一些不错的功能-例如持久性和数据过期。