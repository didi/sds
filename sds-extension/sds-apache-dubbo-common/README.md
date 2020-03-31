# sds apache dubbo extension
** Note: sds-apache-dubbo currently only supports Apache Dubbo 2.7.3、2.7.5、2.7.6. below are the reasons
-  according to [2.7.6-release-note](https://github.com/apache/dubbo/releases/tag/dubbo-2.7.6) and [Filter refactor, keep all callback methods inside Filter.Listener #5731](https://github.com/apache/dubbo/pull/5731) Filter.Listener in 2.7.4 and 2.7.5 is not compatible with 2.7.3 and 2.7.6. 
- but in 2.7.3 the default onResponse method is not removed so we cannot directly implement Filter.Listener and Filter interface in the same time

** For leagcy `com.alibaba:dubbo` 2.6.x, please use `sds-dubbo` instead.

## how to use
To use sds apache dubbo, if you are using maven, add the following dependency to you `pom.xml`

```xml
<dependency>
    <groupId>com.didiglobal.sds</groupId>
    <artifactId>sds-apache-dubbo-2.7.6</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

## how to disable
The SdsProviderFilter and SdsConsumerFilter are enabled by default once you add this dependency in you `pom.xml`

you can disable it by configure xml 
```
<dubbo:provider filter="-SdsProviderFilter"/>
<dubbo:consumer filter="-SdsConsumerFilter"/>
```

if you are using dubbo spring boot, you can disable it through application.yml like this
```
dubbo:
  provider:
    filter: -SdsProviderFilter
  consumer:
    filter: -SdsConsumerFilter  
```

## tips on choose apache dubbo version
according to [Dubbo 各版本总结与升级建议 #5669](https://github.com/apache/dubbo/issues/5669)
use 2.7.3 or 2.7.6
