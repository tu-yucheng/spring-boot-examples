## 1. 概述

在本教程中，我们将探索org.springframework.web.bind.annotation包中的Spring Web注解。

## 2. @RequestMapping

简单地说，**[@RequestMapping](https://www.baeldung.com/spring-requestmapping)标记@Controller类内部的请求处理程序方法**；可以使用以下方式进行配置：

+ path或其别名name和value：方法映射到哪个URL
+ method：兼容的HTTP方法
+ params：根据HTTP参数的存在、不存在或值过滤请求
+ headers：根据HTTP标头的存在、不存在或值过滤请求
+ consumes：该方法可以在HTTP请求正文中使用哪些媒体类型
+ produces：该方法可以在HTTP响应正文中生成哪些媒体类型

这是一个简单的示例：

```java
@Controller
class VehicleController {

    @RequestMapping(value = "/vehicles/home", method = RequestMethod.GET)
    String home() {
        return "home";
    }
}
```

**如果我们在类级别应用此注解，相当于为@Controller类中的所有处理程序方法提供默认设置**。**唯一的例外是，Spring不会用方法级设置覆盖该URL，而是合并两个路径部分**。

例如，下面的配置和上面的效果是一样的：

```java
@Controller
@RequestMapping(value = "/vehicles", method = RequestMethod.GET)
class VehicleController {

    @RequestMapping("/home")
    String home() {
        return "home";
    }
}
```

此外，@GetMapping、@PostMapping、@PutMapping、@DeleteMapping和@PatchMapping是@RequestMapping的不同变体，其HTTP方法已分别设置为GET、POST、PUT、DELETE和PATCH。

这些从Spring 4.3版本开始可用。

## 3. @RequestBody

让我们继续讨论[@RequestBody](https://www.baeldung.com/spring-request-response-body)-它**将HTTP请求的主体映射到一个对象**：

```java
@PostMapping("/save")
void saveVehicle(@RequestBody Vehicle vehicle) {
    // ...
}
```

反序列化是自动的，取决于请求的内容类型。

## 4. @PathVariable

**此注解指示方法参数绑定到URI模板变量**。我们可以使用@RequestMapping注解指定URI模板，并使用@PathVariable将方法参数绑定到模板部分之一。

我们可以使用name或其别名value参数来实现这一点：

```java
@RequestMapping("/{id}")
Vehicle getVehicle(@PathVariable("id") long id) {
    // ...
}
```

如果模板中变量的名称与方法参数的名称匹配，则不必在注解中指定它：

```java
@RequestMapping("/{id}")
Vehicle getVehicle(@PathVariable long id) {
    // ...
}
```

此外，我们可以通过将require参数设置为false来将路径变量标记为可选：

```java
@RequestMapping("/{id}")
Vehicle getVehicle(@PathVariable(required = false) long id) {
    // ...
}
```

## 5. @RequestParam

我们使用@RequestParam来**访问HTTP请求参数**：

```java
@RequestMapping
Vehicle getVehicleByParam(@RequestParam("id") long id) {
    // ...
}
```

它具有与@PathVariable注解相同的配置选项。

除了这些设置之外，当Spring在请求中发现没有值或值为空时，我们可以使用@RequestParam指定一个要注入的值。为此，我们必须设置defaultValue参数。

提供默认值隐式地将required属性设置为false：

```java
@RequestMapping("/buy")
Car buyCar(@RequestParam(defaultValue = "5") int seatCount) {
    // ...
}
```

除了参数之外，我们还可以访问其他HTTP请求部分：cookie和header。**我们可以分别使用注解@CookieValue和@RequestHeader实现相同的效果**。

并且可以像@RequestParam一样配置它们。

## 6. 响应处理注解

在接下来的部分中，我们将看到在Spring MVC中操作HTTP响应的最常见注解。

### 6.1 @ResponseBody

如果我们**用[@ResponseBody](https://www.baeldung.com/spring-request-response-body)标记请求处理程序方法，Spring会将方法的结果视为响应体本身**：

```java
@ResponseBody
@RequestMapping("/hello")
String hello() {
    return "Hello World!";
}
```

如果我们用这个注解来标注一个@Controller类，所有的请求处理方法都会应用该注解。

### 6.2 @ExceptionHandler

**通过这个注解，我们可以声明一个自定义的异常处理方法，当请求处理程序方法抛出任何指定的异常时，Spring调用此方法**。

捕获的异常可以作为参数传递给该异常处理方法：

```java
@ExceptionHandler(IllegalArgumentException.class)
void onIllegalArgumentException(IllegalArgumentException exception) {
    // ...
}
```

### 6.3 @ResponseStatus

**如果我们使用此注解标注请求处理程序方法，则可以指定响应的HTTP状态码**。我们可以使用code参数或其别名value参数来声明状态码。

此外，我们可以使用reason参数提供原因。

我们也可以将它与@ExceptionHandler一起使用：

```java
@ExceptionHandler(IllegalArgumentException.class)
@ResponseStatus(HttpStatus.BAD_REQUEST)
void onIllegalArgumentException(IllegalArgumentException exception) {
    // ...
}
```

有关HTTP响应状态码的更多信息，请阅读[本文](https://www.baeldung.com/spring-mvc-controller-custom-http-status-code)。

## 7. 其他Web注解

有些注解不直接管理HTTP请求或响应。在接下来的部分中，我们将介绍最常见的。

### 7.1 @Controller

我们可以使用@Controller定义一个Spring MVC控制器。有关更多信息，请阅读关于[Spring Bean注解](https://www.baeldung.com/spring-bean-annotations)的文章。

### 7.2 @RestController

**@RestController结合了@Controller和@ResponseBody**。

因此，以下声明是等价的：

```java
@Controller
@ResponseBody
class VehicleRestController {
    // ...
}
```

```java
@RestController
class VehicleRestController {
    // ...
}
```

### 7.3 @ModelAttribute

使用这个注解，我们可以通过**提供Model的key来访问已经存在于MVC @Controller模型中的元素**：

```java
@PostMapping("/assemble")
void assembleVehicle(@ModelAttribute("vehicle") Vehicle vehicleInModel) {
    // ...
}
```

与@PathVariable和@RequestParam一样，如果参数具有相同的名称，则不必指定Model的key：

```java
@PostMapping("/assemble")
void assembleVehicle(@ModelAttribute Vehicle vehicle) {
    // ...
}
```

此外，@ModelAttribute还有另一个用途：**如果我们用它标注一个方法，Spring会自动将该方法的返回值添加到Model中**：

```java
@ModelAttribute("vehicle")
Vehicle getVehicle() {
    // ...
}
```

和之前一样，我们不必指定Model的key，Spring默认使用方法的名称：

```java
@ModelAttribute
Vehicle vehicle() {
    // ...
}
```

在Spring调用请求处理程序方法之前，它会调用类中所有带有@ModelAttribute注解的方法。

有关@ModelAttribute的更多信息可以在[本文](https://www.baeldung.com/spring-mvc-and-the-modelattribute-annotation)中找到。

### 7.4 @CrossOrigin

**@CrossOrigin为带有该注解的请求处理程序方法启用跨域通信**：

```java
@CrossOrigin
@RequestMapping("/hello")
String hello() {
    return "Hello World!";
}
```

如果我们用它标记一个类，则作用于类中的所有请求处理程序方法。

我们可以使用此注解的参数微调CORS行为。

有关更多详细信息，请阅读[本文](https://www.baeldung.com/spring-cors)。

## 8. 总结

在本文中，我们了解了如何使用Spring MVC处理HTTP请求和响应。