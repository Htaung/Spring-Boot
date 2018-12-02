Spring cook book page-135

Adding	custom	connector


build ==>
clean install

==>run spring boot == tomcat as embedded server
springboot:run


exlude tomcat server in maven ==>
go to pom.xml ==> go to dependencies tab then type tomcat in filter then click exlude



The	@ConditionalOnClass	annotation	tells	Spring	Boot	to	use	only	the	EmbeddedJetty
configuration	if	Jetty’s	classes,	namely	org.eclipse.jetty.server.Server	and
org.eclipse.jetty.util.Loader,	are	present	in	the	classpath.