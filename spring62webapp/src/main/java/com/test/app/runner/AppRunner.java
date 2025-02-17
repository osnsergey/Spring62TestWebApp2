package com.test.app.runner;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.core.StandardHost;
import org.apache.catalina.loader.WebappLoader;
import org.apache.catalina.startup.ContextConfig;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.StandardRoot;
import org.apache.coyote.AbstractProtocol;
import org.apache.tomcat.JarScanner;
import org.apache.tomcat.util.scan.StandardJarScanFilter;
import org.apache.tomcat.util.scan.StandardJarScanner;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * Class responsible for running the Embedded Tomcat Server
 */
public class AppRunner {
    private static boolean createDirIfAbsent(File dir) {
        boolean created = true;
        if (!dir.exists()) {
            created = dir.mkdirs();
        }
        return created;
    }

    public void run(String[] args) throws IOException, URISyntaxException, LifecycleException {
        Tomcat tomcat = new Tomcat();
        configure(tomcat);

        tomcat.start();
        tomcat.getServer().await();
    }

    private static void tryToSetDir(String dirName, Runnable setDir) throws URISyntaxException {
        File dir = new File(Objects.requireNonNull(AppRunner.class.getResource("/")).toURI().getPath() + "/" + dirName);
        if (!createDirIfAbsent(dir)) {
            throw new IllegalArgumentException("Could not create directory [" + dir.getAbsolutePath() + "].");
        }
        setDir.run();
    }

    private static WebappLoader createWebappLoader(StandardContext context) {
        WebappLoader loader = new WebappLoader();
        loader.setContext(context);
        loader.setDelegate(true);

        return loader;
    }

    private static JarScanner createJarScanner() {
        JarScanner scanner = new StandardJarScanner();
        StandardJarScanFilter filter = new StandardJarScanFilter();
        filter.setDefaultPluggabilityScan(false);
        filter.setDefaultTldScan(false);
        scanner.setJarScanFilter(filter);
        return scanner;
    }

    private static StandardRoot createStandardRoot(StandardContext context) {
        final URL webResourceSetUrl = JarLauncher.class.getProtectionDomain().getCodeSource().getLocation();

        StandardRoot standardRoot = new StandardRoot(context);
        standardRoot.setCachingAllowed(false);
        standardRoot.setAllowLinking(false);

        standardRoot.createWebResourceSet(WebResourceRoot.ResourceSetType.RESOURCE_JAR, "/WEB-INF/applicationContext.xml",
                webResourceSetUrl, "/WEB-INF/applicationContext.xml");
        standardRoot.createWebResourceSet(WebResourceRoot.ResourceSetType.RESOURCE_JAR, "/WEB-INF/web.xml",
                webResourceSetUrl, "/WEB-INF/web.xml");

        return standardRoot;
    }

    private static StandardContext createStandardContext() throws URISyntaxException {
        StandardContext context = new StandardContext();

        String docBase = "tomcat-docbase";
        String workDir = "tomcat-work";
        tryToSetDir(docBase, () -> context.setDocBase(docBase));
        tryToSetDir(workDir, () -> context.setWorkDir(workDir));

        context.setSessionTimeout(60);
        context.setUseRelativeRedirects(false);
        context.setParentClassLoader(Thread.currentThread().getContextClassLoader());
        context.setLoader(createWebappLoader(context));
        context.addLifecycleListener(new ContextConfig());
        context.setJarScanner(createJarScanner());
        context.setResources(createStandardRoot(context));

        return context;
    }

    private void configure(Tomcat tomcat) throws UnknownHostException, URISyntaxException {
        tomcat.setBaseDir(".");
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");

        connector.setPort(80);
        connector.setProperty("bindOnInit", String.valueOf(false));
        connector.setProperty("server", "Apache");
        connector.setURIEncoding(StandardCharsets.UTF_8.name());

        AbstractProtocol<?> protocolHandler = (AbstractProtocol<?>) connector.getProtocolHandler();
        protocolHandler.setConnectionTimeout(60000);
        protocolHandler.setAddress(InetAddress.getByName("0.0.0.0"));

        tomcat.setConnector(connector);

        StandardHost host = (StandardHost) tomcat.getHost();
        host.setAutoDeploy(false);
        host.setDeployOnStartup(false);
        host.setDeployXML(false);
        host.setCreateDirs(false);
        host.setAppBase(".");

        tomcat.getHost().addChild(createStandardContext());
    }

    public static void main(String[] args) throws IOException, LifecycleException, URISyntaxException {
        new AppRunner().run(args);
    }
}