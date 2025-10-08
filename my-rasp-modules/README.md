# Module Development Guidelines

## 1. The module must extend the AbstractModule abstract class

```java
@ModuleInfo(desc = "module desc")
public class MyModule extends AbstractModule {

}
```
The module class @ModuleInfo annotation includes a `desc` field to facilitate displaying the module's function in the management console.

## 2. The module must implement an `init` method

```java
    @Override
    public void init() {
        new ClassWatchBuilder(moduleEventWatcher)...
    }
```
Implement the `init` method and add target class and method matching rules.

## 3. Module Package Name
The package name must begin with `com.okrasp.module.`. For example, the package name of the login module is `com.okrasp.module.login.hook`.

> Warning ⚠️: By default, only classes under `com.okrasp.module.` are encrypted.

## 4. Target Class Matching

Starting with `login-hook-module` For example, the module's target class is `com.example.springdemoexample.Controller`.
When matching the target class, the class name is converted to the Java internal representation `com/example/springdemoexample/Controller`.

## 5. Target Method Matching

The target method `String login(String username,String password)` is converted to the following method signature:

```text
login(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
```

Fuzzy matching of method names is also supported. If there is only one login method in the Controller class, you can simply specify the method name `login` and omit the aspect description.

```java
.onClass(new ClassMatcher("com/example/springdemoexample/Controller")
                        .onMethod("login", new MyLoginHookListener()))
```
The above matching rule indicates: Match methods named `login` in the target class. If a match occurs, execute the aspect method of MyLoginHookListener.

#### Methods for Obtaining Method Signatures

##### Method 1: Unzip the JAR file containing the class and decompile it

Unzip the class file: `jar -xvf application.jar`

Decompile the class: `javap -v com/example/springdemoexample/Controller.class`

Obtain the internal description of the method `login(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;`

##### Method 2: Install the jclasslib IDEA plugin (recommended)

## 6. Implementing AOP Logic
Using the login-hook-module module as an example, MyLoginHookListener needs to extend the abstract class RaspHookListener
```java
 class MyLoginHookListener extends RaspHookListener {
        @Override
        public void atEnter(Object[] args, Object thiss) throws Throwable {
            if (args != null && args.length == 2) {
                String username = (String) args[0];
                String password = (String) args[1];
                logger.info("username: " + username + ", password: " + password);
            }
        }
    }
```

+ Get method parameters to implement the `atEnter` method;
+ Get return value to implement `atExit`;
+ Get exception thrown by the method to implement `atExceptionExit`

## 7. Output logs in the module

The module has a built-in `logger` object. The implementation principle is: the jrasp framework is responsible for injecting the actual log object instance into the corresponding module (similar to the dependency injection mechanism of Spring)

Output attack logs

```java
logger.attack(AttackInfo attackInfo);
```

Output module logs:

```java
logger.info(String message);
logger.warn(String message);
```