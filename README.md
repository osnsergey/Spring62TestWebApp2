# Issue description
Component Scan can't find annotated beans in the Web application if beans are located in the embedded .jar file in the Web application after the migration from Spring 6.1 to Spring 6.2.

The exception during startup:
```
21:42:59.048 [main] DEBUG org.springframework.beans.factory.support.DefaultListableBeanFactory -- Creating shared instance of singleton bean 'localizationController'
21:42:59.070 [main] WARN org.springframework.web.context.support.XmlWebApplicationContext -- Exception encountered during context initialization - cancelling refresh attempt: org.springframework.beans.factory.UnsatisfiedDependencyException: Error creating bean with name 'localizationController': Unsatisfied dependency expressed through field 'langPackageLoader': No qualifying bean of type 'com.my.app.localization.LangPackageLoader' available: expected at least 1 bean which qualifies as autowire candidate. Dependency annotations: {@org.springframework.beans.factory.annotation.Autowired(required=true)}
21:42:59.076 [main] ERROR org.springframework.web.context.ContextLoader -- Context initialization failed
org.springframework.beans.factory.UnsatisfiedDependencyException: Error creating bean with name 'localizationController': Unsatisfied dependency expressed through field 'langPackageLoader': No qualifying bean of type 'com.my.app.localization.LangPackageLoader' available: expected at least 1 bean which qualifies as autowire candidate. Dependency annotations: {@org.springframework.beans.factory.annotation.Autowired(required=true)}
        at org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor$AutowiredFieldElement.resolveFieldValue(AutowiredAnnotationBeanPostProcessor.java:788)
        at org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor$AutowiredFieldElement.inject(AutowiredAnnotationBeanPostProcessor.java:768)
        at org.springframework.beans.factory.annotation.InjectionMetadata.inject(InjectionMetadata.java:146)
        at org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor.postProcessProperties(AutowiredAnnotationBeanPostProcessor.java:509)
        at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.populateBean(AbstractAutowireCapableBeanFactory.java:1445)
        at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:600)
        at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:523)
        at org.springframework.beans.factory.support.AbstractBeanFactory.lambda$doGetBean$0(AbstractBeanFactory.java:339)
        at org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:346)
        at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:337)
        at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:202)
        at org.springframework.beans.factory.support.DefaultListableBeanFactory.instantiateSingleton(DefaultListableBeanFactory.java:1155)
        at org.springframework.beans.factory.support.DefaultListableBeanFactory.preInstantiateSingleton(DefaultListableBeanFactory.java:1121)
        at org.springframework.beans.factory.support.DefaultListableBeanFactory.preInstantiateSingletons(DefaultListableBeanFactory.java:1056)
        at org.springframework.context.support.AbstractApplicationContext.finishBeanFactoryInitialization(AbstractApplicationContext.java:987)
        at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:627)
        at org.springframework.web.context.ContextLoader.configureAndRefreshWebApplicationContext(ContextLoader.java:394)
        at org.springframework.web.context.ContextLoader.initWebApplicationContext(ContextLoader.java:274)
        at org.springframework.web.context.ContextLoaderListener.contextInitialized(ContextLoaderListener.java:126)
        at org.apache.catalina.core.StandardContext.listenerStart(StandardContext.java:4008)
        at org.apache.catalina.core.StandardContext.startInternal(StandardContext.java:4436)
        at org.apache.catalina.util.LifecycleBase.start(LifecycleBase.java:164)
        at org.apache.catalina.core.ContainerBase$StartChild.call(ContainerBase.java:1203)
        at org.apache.catalina.core.ContainerBase$StartChild.call(ContainerBase.java:1193)
        at java.base/java.util.concurrent.FutureTask.run(FutureTask.java:264)
        at org.apache.tomcat.util.threads.InlineExecutorService.execute(InlineExecutorService.java:75)
        at java.base/java.util.concurrent.AbstractExecutorService.submit(AbstractExecutorService.java:145)
        at org.apache.catalina.core.ContainerBase.startInternal(ContainerBase.java:749)
        at org.apache.catalina.core.StandardHost.startInternal(StandardHost.java:772)
        at org.apache.catalina.util.LifecycleBase.start(LifecycleBase.java:164)
        at org.apache.catalina.core.ContainerBase$StartChild.call(ContainerBase.java:1203)
        at org.apache.catalina.core.ContainerBase$StartChild.call(ContainerBase.java:1193)
        at java.base/java.util.concurrent.FutureTask.run(FutureTask.java:264)
        at org.apache.tomcat.util.threads.InlineExecutorService.execute(InlineExecutorService.java:75)
        at java.base/java.util.concurrent.AbstractExecutorService.submit(AbstractExecutorService.java:145)
        at org.apache.catalina.core.ContainerBase.startInternal(ContainerBase.java:749)
        at org.apache.catalina.core.StandardEngine.startInternal(StandardEngine.java:203)
        at org.apache.catalina.util.LifecycleBase.start(LifecycleBase.java:164)
        at org.apache.catalina.core.StandardService.startInternal(StandardService.java:415)
        at org.apache.catalina.util.LifecycleBase.start(LifecycleBase.java:164)
        at org.apache.catalina.core.StandardServer.startInternal(StandardServer.java:870)
        at org.apache.catalina.util.LifecycleBase.start(LifecycleBase.java:164)
        at org.apache.catalina.startup.Tomcat.start(Tomcat.java:437)
        at com.test.app.runner.AppRunner.run(AppRunner.java:42)
        at com.test.app.runner.AppRunner.main(AppRunner.java:131)
        at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
        at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:77)
        at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
        at java.base/java.lang.reflect.Method.invoke(Method.java:568)
        at org.springframework.boot.loader.MainMethodRunner.run(MainMethodRunner.java:49)
        at org.springframework.boot.loader.Launcher.launch(Launcher.java:95)
        at org.springframework.boot.loader.Launcher.launch(Launcher.java:58)
        at com.test.app.runner.JarLauncher.main(JarLauncher.java:38)
Caused by: org.springframework.beans.factory.NoSuchBeanDefinitionException: No qualifying bean of type 'com.my.app.localization.LangPackageLoader' available: expected at least 1 bean which qualifies as autowire candidate. Dependency annotations: {@org.springframework.beans.factory.annotation.Autowired(required=true)}

        at org.springframework.beans.factory.support.DefaultListableBeanFactory.raiseNoMatchingBeanFound(DefaultListableBeanFactory.java:2177)
        at org.springframework.beans.factory.support.DefaultListableBeanFactory.doResolveDependency(DefaultListableBeanFactory.java:1627)
        at org.springframework.beans.factory.support.DefaultListableBeanFactory.resolveDependency(DefaultListableBeanFactory.java:1552)
        at org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor$AutowiredFieldElement.resolveFieldValue(AutowiredAnnotationBeanPostProcessor.java:785)
        ... 52 common frames omitted
```

Bean definitions can't be found in the following jar:
```
spring62webapp.jar!\WEB-INF\lib\localization.jar!\
```

# Build sample application
Use JDK 17.
```
./gradlew spring62webapp:release
```
# Run sample application
Enter the release directory:
```
cd ./spring62webapp/result/release 
```
Run the jar:
```
java -jar spring62webapp.jar
```
