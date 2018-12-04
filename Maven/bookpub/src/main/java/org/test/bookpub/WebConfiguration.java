package org.test.bookpub;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.catalina.connector.Connector;
import org.apache.catalina.filters.RemoteIpFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.test.bookpub.repository.BookRepository;



@Configuration
@PropertySource("classpath:/tomcat.https.properties")
@EnableConfigurationProperties(WebConfiguration.TomcatSslConnectorProperties.class)
public class WebConfiguration extends WebMvcConfigurerAdapter {
	
	@ConfigurationProperties(prefix = "custom.tomcat.https")
	public static class TomcatSslConnectorProperties {
		private Integer port;
		private Boolean ssl = true;
		private Boolean secure = true;
		private String scheme = "https";
		private File keystore;
		private String keystorePassword;

		// Skipping getters and setters to save space, but we do need them
		public void configureConnector(Connector connector) {
			if (port != null)
				connector.setPort(port);
			if (secure != null)
				connector.setSecure(secure);
			if (scheme != null)
				connector.setScheme(scheme);
			if (ssl != null)
				connector.setProperty("SSLEnabled", ssl.toString());
			System.out.println("ssl ==>" + ssl.toString());
			System.out.println("keystore.getAbsolutePath()" + keystore.getAbsolutePath());
			if (keystore != null && keystore.exists()) {
				connector.setProperty("keystoreFile", keystore.getAbsolutePath());
				connector.setProperty("keystorePassword", keystorePassword);
			}
		}

		public Integer getPort() {
			return port;
		}

		public void setPort(Integer port) {
			this.port = port;
		}

		public Boolean getSsl() {
			return ssl;
		}

		public void setSsl(Boolean ssl) {
			this.ssl = ssl;
		}

		public Boolean getSecure() {
			return secure;
		}

		public void setSecure(Boolean secure) {
			this.secure = secure;
		}

		public String getScheme() {
			return scheme;
		}

		public void setScheme(String scheme) {
			this.scheme = scheme;
		}

		public File getKeystore() {
			return keystore;
		}

		public void setKeystore(File keystore) {
			this.keystore = keystore;
		}

		public String getKeystorePassword() {
			return keystorePassword;
		}

		public void setKeystorePassword(String keystorePassword) {
			this.keystorePassword = keystorePassword;
		}
		
		
	}

	// HttpMessageConverter as @Bean is the quickest and simplest way of adding
	// a custom converter to the application.
	/*
	 * @Bean public ByteArrayHttpMessageConverter
	 * byteArrayHttpMessageConverter() { return new
	 * ByteArrayHttpMessageConverter(); }
	 */

	/**
	 * When the application needs to dictate the extension of
	 * WebMvcConfigurerAdapter to configure other things such as interceptors,
	 * then it would be more consistent to override the
	 * configureMessageConverters method and add our converter to the list. As
	 * there can be multiple instances of WebMvcConfigurers, which could be
	 * either added by us or via the autoconfiguration settings from various
	 * Spring Boot Starters, there is no guarantee that our method can get
	 * called in any particular order
	 */
	/*
	 * @Override public void
	 * configureMessageConverters(List<HttpMessageConverter<?>> converters) {
	 * converters.add(new ByteArrayHttpMessageConverter()); }
	 */

	/**
	 * Best Way This method gets invoked after all the WebMvcConfigurers get
	 * called for configureMessageConverters and the list of converters is fully
	 * populated. Of course, it is entirely possible that some other instance of
	 * WebMvcConfigurer could override the extendMessageConverters as well
	 */
	@Override
	public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
		// converters.clear();
		converters.add(new ByteArrayHttpMessageConverter());
	}

	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		return new LocaleChangeInterceptor();
	}

	@Bean
	public EmbeddedServletContainerFactory servletContainer(TomcatSslConnectorProperties properties) {
		TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory();

		tomcat.addAdditionalTomcatConnectors(createSslConnector(properties));
		return tomcat;
	}

	private Connector createSslConnector(TomcatSslConnectorProperties properties) {
		Connector connector = new Connector();
		properties.configureConnector(connector);
		return connector;
	}

	@Bean
	public RemoteIpFilter remoteIpFilter() {
		return new RemoteIpFilter();
	}

	@Override
	public void configurePathMatch(PathMatchConfigurer configurer) {
		configurer.setUseSuffixPatternMatch(false).setUseTrailingSlashMatch(true);
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(localeChangeInterceptor());
	}

	@Bean
	public EmbeddedServletContainerCustomizer embeddedServletContainerCustomizer() {
		return (ConfigurableEmbeddedServletContainer container) -> {
			container.setSessionTimeout(1, TimeUnit.MINUTES);
		};
		// return new EmbeddedServletContainerCustomizer() {
		// @Override
		// public void customize(ConfigurableEmbeddedServletContainer container)
		// {
		// container.setSessionTimeout(1, TimeUnit.MINUTES);
		// }
		// };
	}

	@Autowired
	private BookRepository bookRepository;

	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addFormatter(new BookFormatter(bookRepository));
	}
	
	@Override
	public	void	addResourceHandlers(ResourceHandlerRegistry	registry)	{
			registry.addResourceHandler("/internal/**").addResourceLocations("classpath:/");
	}
}
