# SDS (Service Downgrade System)

## 1. 背景
移动互联网时代的来临，给业务增长插上翅膀的同时，也给服务端应用带来了巨大的挑战，大流量、高并发和海量数据似乎成为了服务端应用的标配。

为了对端上请求进行统一处理、鉴权和负载均衡等，服务端一般会有个网关层，网关层拦截所有的流量，进行统一的限流、鉴权和流量转发管理，对外屏蔽内部的处理细节，网关层的下游，是一个由某个服务治理框架来管理多个应用的大型服务网络，虽然我们能使用微服务架构来对应用进行拆分，让多个应用之间职责、边界更清晰，做到独立横向扩展，并能匹配团队来进行快速迭代开发和维护。微服务架构，会导致团队需要维护的应用数量成倍的增加，错综复杂的服务依赖，可能架构师都说不清楚各应用的依赖关系，一旦某个应用或下游依赖出问题，就可能导致整个核心服务瘫痪，而大多数服务治理框架的自动容错、隔离和恢复能力都比较简单，缺乏专业的界面来进行策略调整和维护，所以急需一个专业的系统来做专业的事情，于是我们就开发了SDS。

## 2. SDS简介
SDS（即 Service Downgrade System）是一个轻量级、简单、易用的限流、熔断、降级系统，能让Java应用做到自动限流、熔断和快速恢复，提升应用整体的“弹性”。现在服务端通过采用流行的微服务架构来应对错综复杂的大流量场景，并能在业务高速发展时仍然能做到较强的快速迭代能力和可扩展性。微服务架构并不是将整个系统变得更简单，相反，微服务架构的管理难度高于普通的集中式架构，所以，如何保证系统的每个节点在错综复杂的环境下能稳定提供服务，需要借助工具来让服务节点能抵挡流量冲击、熔断依赖坏点。

## 3. SDS架构设计
SDS采用C/S架构，只要Java应用依赖并使用了sds-client包，那么它就是一个SDS的客户端，sds-client中包含了限流、熔断和数据统计等功能。sds-admin作为Server端主要是为了配置降级策略、提供丰富的仪表盘并且保存客户端上传的统计数据、并应答最新的降级策略(快速体验sds-admin：https://sds.chpengzh.com/ )，如下图：
<div align="center">    
  <img src="https://pt-starimg.didistatic.com/static/starimg/img/fxnHAtPgtS1575906494316.png" alt="SDS架构图" align="center" />
</div>

那么sds-client.jar是如何和sds-admin进行交互的？SDS客户端每10秒钟向SDS服务端发送一次心跳，用于上传SDS客户端在最近一个完整周期（10秒）内的统计和降级数据，并从服务端拉取最新的降级点策略信息。值得注意的是，sds-client.jar的依赖比较少，只靠内存来统计数据，各客户端的数据在服务端才进行聚合展现，服务端借助客户端的心跳来分发最新的降级点策略。

## 4. sds-client工作原理
从SDS的架构来看，服务端sds-admin的工作职责在于数据保存、策略配置和数据展示，而客户端Jar包sds-client的职责是应用策略、统计数据、执行策略和上报数据等。sds-client是限流和熔断功能的客户端，主要有如下功能：

**访问量限流**，*10s的时间窗口内请求量超过指定阈值就进行限流。*

**并发限流**，*可以限制某个方法的并发调用数不能超过多少。*

**异常限流**，*可以指定某个方法在10s的时间窗口内达到多少错误量或者达到某个错误比率就进行限流。*

**超时限流**，*可以指定某个方法在10s的时间窗口内超过指定时间阈值的请求量超过超时量阈值就进行限流。*

**令牌桶**，*通过每秒生成多少个令牌和桶的最大容量来实现令牌桶的效果。*

需要注意的是，上述所有限流方式的阈值和相关参数在sds-admin都是可配置的，运行时都可以动态调整，并且，这些阈值和参数都是针对单台服务器的，不是针对总的集群的。

### 4.1 访问量限流
根据访问量限流应该是最常用的一种限流策略，一般来说有如下两种做法：

方案一：我们统计该方法在单位时间内的调用量，如果达到某个阈值，那么在该单位时间剩下的时间里，该方法的调用会被限流。当到达下一个单位时间时，该计数器会被清零并重新计数。我这里把它称作基于自然时间的限流方案。

方案二：仍然是基于单位时间内的调用量来进行限流，但该单位时间不像方案一中是固定的自然单位时间，这里的单位时间会随着时间的变化而不断向前推进，只是时间宽度不变，类似于TCP的滑动窗口，我这里把它称作基于滑动窗口的限流方案。

方案1的一个缺点是在切换单位时间时，由于计数器需要清理，会导致限流跳变，因为一切要“从头开始”，所以会导致，当到达某个单位时间的90%时，超过了阈值触发了限流，那么限流只在剩下的10%的单位时间内生效，等切换到下一个单位时间，又需要重新统计和判断。而方案二的出现，就是为了规避方案一的这个问题，统计的时间周期不是选择自然固定的时间段，而是随着时间移动的一个固定时间宽度的窗口。类似于TCP的滑动窗口，或者一致性哈希的效果。

SDS2.0采用的就是方案二的方式，时间窗口采用的是10s，滑动步长选择的是1s。开源框架Hystrix中，虽然没有访问量限流，但里面的异常量限流的统计方式也是用滑动窗口的方式来统计的，SDS2.0参考了其设计方案，但Hystrix的时间窗口宽度和步长（桶）的大小是可以调整的，SDS2.0为了实现的简洁性，设计成不可调整。当然，访问量的限流阈值和限流比例在sds-admin的界面上都是可以配置的。

那么，SDS2.0如何实现滑动窗口的？答案就是通过数组，即AtomicLongArray，如下图：
<div align="center">    
  <img src="https://pt-starimg.didistatic.com/static/starimg/img/Ctcbf6rzM91576037384470.jpeg" alt="SDS滑动窗口" align="center" />
</div>

上图每个小方格代表一个步长时间，即1s，步长时间是滑动窗口每次移动的时间长度，而滑动窗口本身的时间长度是固定的，即10s。为了能循环利用空间，我们在设计时把AtomicLongArray做成了“环”状，这样就做到了滑动窗口在有限空间内无限次的滑动。细心的朋友会发现，上面总共有30个小方格，如果像前面说的，做到在有限空间内无限滑动的话，那么11个小方格做成"环"状就可以了，为啥要30个小方格？

sds-client除了需要统计滑动窗口的数据，还有两个任务，统计数据上传和从sds-admin拉去最新的降级策略，而统计的数据上传并不是基于滑动窗口来的，而是根据自然时间窗口而来的，这个自然时间窗口的长度也是10s（之所以自然时间窗口的长度和滑动窗口的长度保持一致，是因为我们配置降级策略的一些阈值会根据仪表盘显示的统计数据来，如果两者窗口长度不一致，会增加我们配置阈值的难度），所以sds-client上传的统计数据是类似 [12:10:00~12:10:10)、[12:10:10~12:10:20)、[12:10:20~12:10:30) 这样的自然时间窗口，就是如上图所示的红色、黄色和蓝色时间区域的统计数据。举个例子，假如当前时间是12:10:11，那么该时刻的统计数据会落到上图从左开始的第11个格子中（也就是黄色区域的一个格子），依次类推，当时间到达12:10:12时，会将当前的统计数据落在低12个格子中。当时间到达12:10:15时，sds-client这时候会将上一个自然时间窗口（也就是12:10:00至12:10:10，即上图的红色区域）的统计数据，上传给sds-admin，而sds-admin会将最新的降级策略应答给sds-client。此时，sds-client还会将下一自然时间窗口（也就是上图蓝色区域）的数据给清空，便于后面使用。该流程操作会循环操作，这也是为什么用了30个小方格。访问量限流的阈值在sds-admin上是可以动态调整的。

### 4.2 并发限流
并发限流有两种常见的实现方式，一种是通过线程池来隔离，通过线程池的线程数量来限制并发调用，另一种是使用Semaphore来做并发限制。著名的限流熔断工具Hystrix采用的就是这两种方法。这两种方法各有优缺点，线程池的方案开销比较大，如果使用不当反而会增加系统的不稳定性，但可以做到超时提前返回，因为执行是异步的。Semaphore的优点也很明显，开销小，性能高，风险也比线程池的方案小，但无法做到超时返回。SDS2.0采用的是Semaphore的方式。和访问量限流一样，并发限流的阈值也是可以在sds-admin来动态调整的。

### 4.3 异常限流
有时候我们很难评估一个接口方法的服务能力，特别是在没有历史数据的情况下，这将导致访问量限流和并发限流的阈值很难设置成一个合适的值，所以这时候，我们可以利用该接口方法抛出的异常数量或比例来作为该接口是否已经达服务提供的最大能力的信号，所以就有了异常量限流和异常率限流。异常量限流，顾名思义，将以滑动窗口来统计异常数量，当这个异常数量达到我们设置的阈值，那么就按照我们在sds-admin配置的降级比例来进行限流。注意，这里提到和下面提到的滑动窗口和上面访问量限流的滑动窗口是同一个。有了异常量限流，就会有异常率限流的需求，异常率限流指的是异常率达到一定的比例，比如40%，就开始进行限流，注意，这里的 异常率 = 滑动窗口异常数量 / (滑动窗口访问量 - 滑动窗口降级量)。细心的朋友一定会想到，在流量很小时，比如每秒才几笔请求进来，这时候产生的异常数量将导致异常率的统计有较大变化，比如4笔请求有2笔抛出了异常，那么异常比例就达到了50%！这显然不符合我们预期，所以当我们采用异常率限流方式时，除了异常率阈值需要设置以外，还需要设置一个异常率计算的起始访问量值。

### 4.4 超时限流
某些服务，当流量上涨后，随着系统负载的增加，平均耗时通常会有所提高，当流量继续上涨，系统负载继续增高，这时候通常就会出现超时等错误。SDS2.0提供了超时限流方案，在滑动窗口内统计耗时超过指定超时时间阈值（单位毫秒）的请求数量，如果该数量超过指定的超时数量阈值，那么将进行限流，当然，超时时间阈值和超时数量阈值在sds-admin端可配。

### 4.5 降级比例
降级比例是一个[0-100]的整数，代表如果判断为降级，那么真正要降级的比例，例如，配置为50，表示如果策略执行器判定该流量需要降级，那么最终被降级的概率是50%，所以，如果配置成100，那么策略执行器判断为降级后，最终肯定会被降级，相反，如果配置成0，那么永远不会被降级。

降级比例可以用作策略执行的灰度发布方案！

## 5. 使用方式
详见：https://github.com/didi/sds/wiki/SDS%E7%9A%84%E4%BD%BF%E7%94%A8%E6%8C%87%E5%8D%97
### 5.1 添加依赖
我们已经知道，所有的降级逻辑将由sds-client.jar来完成，为了让某个应用能成为SDS的一个客户端，应用需要依赖如下Jar：

```xml
<dependency>
   <groupId>com.didiglobal.sds</groupId>
   <artifactId>sds-easy</artifactId>
   <version>1.0.1-SNAPSHOT</version>
</dependency>
```
sds-easy内部依赖了sds-client，sds-easy的出现是为了让我们使用sds-client更便捷。

如果我们使用了dubbo框架，那么还可以依赖sds-dubbo，里面内置了Dubbo Filter可以供我们轻松切入Dubbo框架：

```xml
<dependency>
   <groupId>com.didiglobal.sds</groupId>
   <artifactId>sds-dubbo</artifactId>
   <version>1.0.1-SNAPSHOT</version>
</dependency>
```

### 5.2 实例化一个SdsClient对象
注意，该对象应该使用单例模式，可以直接通过SdsClientFactory工厂来创建：

```java
// SDS控制台地址
private static final String SERVER_URL = "http://127.0.0.1:8887";
// 通过工厂方法来创建SdsClient实例
private static final SdsClient sdsClient = SdsClientFactory.getOrCreateSdsClient("BikeBusinessDepartment", "order", SERVER_URL);
```

我们可以在Spring配置文件如下初始化：
```xml
<bean id="sdsClient" class="com.didiglobal.sds.client.SdsClientFactory" factory-method="getOrCreateSdsClient">
    <constructor-arg type="java.lang.String" value="BikeBusinessDepartment" />
    <constructor-arg type="java.lang.String" value="order" />
    <constructor-arg type="java.lang.String" value="http://127.0.0.1:8887" />
</bean>
```

### 5.3 使用SdsClient对象来进行降级（不推荐，使用起来比较麻烦）
首先，当我们需要对某个方法做降级保护时，我们需要给该方法做一个降级标记，这样我们才能在服务端给该方法的降级策略做配置，我们把这个降级标记称作【降级点】，实际上是一个字符串，为了美观，我们采用Java变量的命名风格：驼峰法来命名。

例如，我们需要对businessMethod方法进行降级保护，我们可以把该降级点记做【businessMethodPoint】（一个应用通常会有很多降级点，可以把这些降级点放在一个常量文件里面，便于统一管理），确定了降级点后，就可以直接编码了，如下：

```java
/**
 * 模拟业务方法
 * 如果执行了业务逻辑，则返回true，降级了则返回false
 *
 * @return
 */
protected boolean businessMethod() {
    try {
        /**
         * 1. 降级判断，true表示需要进行降级
         */
        if (sdsClient.shouldDowngrade("somePoint")) {
            // 降级后根据业务返回默认值或者抛异常
            return false;
        }
 
        // 下面是正常的业务逻辑
        // TODO
        return true;
 
    } catch (Exception e) {
        /**
         * 2. 这里用于统计异常量
         */
        sdsClient.exceptionSign("somePoint", e);
 
        // 记得捕获完还得抛出去，偷偷吃掉麻烦就大了
        throw e;
 
    } finally {
        /**
         *  3. 回收资源，一般在finally代码中调用
         */
        sdsClient.downgradeFinally("somePoint");
    }
}
```

可以看出，上面为了对一个方法进行降级，我们共调用了sdsClient的三个方法：

**shouldDowngrade**：根据当前信息和降级策略，判断该降级点是否应该被降级，如果应该被降级，则返回true，否则返回false。

**exceptionSign**：异常量记录，记录该笔请求是否有抛出异常，如果有，那么异常量数据加1。

**downgradeFinally**：标记降级生命周期已经结束。

注意：一定要保证downgradeFinally方法被调用（放在finally块中），否则某些资源无法释放！！

## 6. 一种更简便的使用方式（推荐）
### 6.1 上面的方式太复杂，其实我们可以直接使用SdsEasyUtil来进行降级（效果和上面一样）
前面提到过，sds-easy中提供了一种更简便的使用方式，即使用SdsEasyUtil类，例如：

```java
protected static final String SERVER_URL = "http://127.0.0.1:8887";
 
static {
    // 可以找个安静的地方初始化SdsClient
    SdsClientFactory.getOrCreateSdsClient("BikeBusinessDepartment", "order", SERVER_URL);
}
 
// 这里假装是业务Service
private ThreadLocal<String> traceIdService = ThreadLocal.withInitial(() -> "古墓丽影");
 
@Test
public void invokerMethodTest() {
 
    // 某个局部变量（路人甲）
    Date date = new Date();
 
    /**
     * 包含降级判断的业务逻辑执行
     *
     * 注意：SdsEasyUtil类是简化神器
     */
    String result = SdsEasyUtil.invokerMethod("somePoint", "我是降级后的默认值", () -> {
 
        // 这里可以添加一些奇怪的业务逻辑
        System.out.println(date);
        return traceIdService.get();
 
    });
 
    Assert.assertEquals("古墓丽影", result);
}
```

当然，上图使用了Lambda的写法，如果是JDK8以下的版本，可以使用如下写法：
```java
String result = SdsEasyUtil.invokerMethod("somePoint", "我是降级后的默认值", new BizFunction<String>() {
    @Override
    public String invokeBizMethod() {
 
        // 这里可以添加一些奇怪的业务逻辑
        System.out.println(date);
        return traceIdService.get();
 
    }
});
```

## 7. 注解支持（推荐）
SDS也支持使用注解接入，我们只需要在方法上使用 **@SdsDowngradeMethod** 即可。注解的接入方式有两种，一种是使用**Java 
Agent**的能力来在类加载时将SDS代码植入（类似于Pinpoint的做法），另一种是利用**Aspectj**的能力来动态植入。
### 7.1 通过Java Agent的方式使用注解
第一步：通过maven依赖sds-bootstrap.jar（或者将sds-bootstrap.jar放到某个绝对路径下，例如/home/sds/lib/sds-bootstrap.jar）
```xml
    <dependency>
        <groupId>com.didiglobal.sds</groupId>
        <artifactId>sds-bootstrap</artifactId>
        <version>1.0.1-SNAPSHOT</version>
    </dependency>
```
第二步：在JVM启动时通过javaagent来引入sds-bootstrap.jar，所以我们需要修改启动脚本，例如：
```jvm
# 通过相对路径引入sds-bootstrap.jar (这里假设项目打完包后sds-bootstrap.jar在启动脚本当前目录的lib目录下)
java -javaagent:lib/sds-bootstrap.jar MyApplication

# 当然也可以通过绝对路径引入sds-bootstrap.jar
java -javaagent:/home/sds/lib/sds-bootstrap.jar MyApplication
```
第三步：这样就可以直接在类方法中使用@SdsDowngradeMethod了。
> 注意：该方式对类中的任何方法，不管是private还是public都有效。

### 7.2 通过Spring AOP(Aspectj)的方式使用注解
第一步：项目需要依赖sds-aspectj.jar，例如：
```xml
    <dependency>
        <groupId>com.didiglobal.sds</groupId>
        <artifactId>sds-aspectj</artifactId>
        <version>1.0.1-SNAPSHOT</version>
    </dependency>
```
第二步：如果使用了Spring(当然也包含Spring Boot)，那么需要创建一个SdsPointAspect Bean：
```java
@Configuration
public class SdsConfiguration {
    @Bean
    public SdsPointAspect createSdsPointAspect() {
        return new SdsPointAspect();
    }
}
```
第三步：这样就可以直接在类方法中使用@SdsDowngradeMethod了。
> 注意：该方式无法对类的private方法生效。

> 提醒：Java Agent方式和Spring AOP方式请不要同时使用！

## 8. 降级露出
我只能从sds-admin的仪表盘来感知到被降级了吗？其实客户端也可以感知降级，通过如下方式注册一个监听器：
```java
static {
    SdsDowngradeActionNotify.addDowngradeActionListener(new DowngradeActionListener() {
    
        @Override
        public void downgradeAction(String point, DowngradeActionType downgradeActionType, Date date) {
                System.out.println(point + "被降级了，降级方式：" + downgradeActionType);
                System.out.println("降级点监听器一般用来输出日志！");
            }
        });
    
    }
}
```
注意，监听器不宜注册太多，一个足矣，比如我们可以输出一些降级日志等。需要注意的是，只要有请求被降级，该监听器就会被触发，虽然是异步执行，但最好不要在里面做高耗时的操作，更不能在里面进行短信或电话报警（被降级的量有可能很高）。


## 9. 常用扩展
为了能进一步减少接入的难度，我们对常用框架的接入进行了封装，让接入的成本更低。

### 9.1 对Dubbo的支持
大多数外部工具，对Dubbo的切入点都在Filter上，Filter它是由Dubbo通过SPI来初始化的，SdsClient实例应该作为静态单例来使用。

我们为此提供了sds-dubbo.jar（注意，如果是maven方式构建，可以业务系统可以只依赖sds-dubbo.jar，因为sds-dubbo.jar内依赖了sds-client.jar），里面有一个SdsProviderFilter 和 SdsConsumerFilter，这两个filter都提供了可覆盖的接口，用于业务系统根据自己的特殊性，来提供降级后的返回值或行为：
```java
    /**
     * 降级后返回默认值
     *
     * @return 返回值不应该是Throwable的子类
     */
    protected static Object returnDefaultValueAfterDowngrade() {
        return null;
    }
    
    /**
     * 降级后直接抛异常
     *
     * @return
     */
    protected static Throwable throwThrowableAfterDowngrade() {
        return null;
    }
```

## 10. SDS优势
SDS的目标是打造一个简单、易用、可靠的限流、熔断和降级系统。让我们回退到2015年年底，当时代驾急需一个能自动限流、熔断和恢复的工具，但经过市场调研，著名的Hystrix并不是我们想要的，Hystrix因为它依赖了RxJava，所以对我们来说太重了，而且Hystrix的主要两种限流方式是信号量和线程池，不满足我们对固定时间窗口访问量的限流方式（当时代驾使用的监控系统的时间力度是1分钟，所以我们希望也能在1分钟内进行流控操作），而且Hystrix基于Command模式来设计，侵入性较强。既然我们目标明确，于是SDS1.0版本在2016年初，功能简单，但API设计得不太合理，易用性比较差，而且整个控制台界面也不人性化，现在看来有些惨不忍睹。于是在17年底开始着手进行重构，在2018年SDS2.0诞生，SDS2.0在功能和易用性方面相比SDS1.0有质的飞越，目前在两轮车中使用（代驾、安全等部门使用的还是SDS1.0），为了回馈社会，希望把SDS2.0开源出来。 相比著名的Hystrix，SDS的优势主要体现在支持的限流功能更丰富，有访问量、并发量、错误量、超时量、令牌桶等限流方式（没有Hystrix的线程池限流），并且控制台仪表盘比Hystrix更强大。 相比18年7月阿里开源Sentinel，SDS的优势主要体现更简单，上手更容易，并且SDS支持现成的一键降级方案，能更方便的制定紧急预案。相比Sentinel，SDS的弱势主要体现在没有集群统一限流功能，并且只支持Java。

## 11. 联系或加入我们
**微信群**：我们有微信群（SDS开发者交流群），但群二维码7天有效，所以请加微信号 **sugarmq**、**devil_chpengzh**、**lansedemeng-2010**、**huangyiminghappy**、**BU_DONG_XIAO_BIN** 为好友（备注下sds），我们会拉你入群。

**钉钉群**：我们也有钉钉群（SDS开发者交流群），请用钉钉扫码加入：

![avatar](https://pt-starimg.didistatic.com/static/starimg/img/3AAuscs7Rj1582955653942.png)

## 12. 协议

<img alt="Apache-2.0 license" src="https://lucene.apache.org/images/mantle-power.png" width="128">
SDS 基于 Apache-2.0 协议进行分发和使用，更多信息参见 [协议文件](LICENSE)。
