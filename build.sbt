import AssemblyKeys._
assemblySettings

name := "akka_try"

version := "1.0"

scalaVersion := "2.10.3"

EclipseKeys.createSrc := EclipseCreateSrc.Default + EclipseCreateSrc.Resource

libraryDependencies ++= Seq(
  "com.typesafe.akka" % "akka-actor_2.10" % "2.3.4" withSources () ,
  "org.scalatest" % "scalatest_2.10" % "2.2.0",
   "log4j" % "log4j" % "1.2.17",
   "org.slf4j" % "slf4j-log4j12" % "1.7.7",
   "org.json4s" %% "json4s-native" % "3.2.10",  
   "org.json4s" %% "json4s-jackson" % "3.2.10",
   "commons-vfs" % "commons-vfs" % "1.0",
   "com.esotericsoftware.reflectasm" % "reflectasm" % "1.07",
    "com.esotericsoftware.kryo" % "kryo" % "2.21"
)


resolvers ++= Seq(
      // HTTPS is unavailable for Maven Central
      "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
      "Maven Repository"     at "http://repo.maven.apache.org/maven2",
      "Apache Repository"    at "https://repository.apache.org/content/repositories/releases",
      "JBoss Repository"     at "https://repository.jboss.org/nexus/content/repositories/releases/",
      "MQTT Repository"      at "https://repo.eclipse.org/content/repositories/paho-releases/",
      "Cloudera Repository"  at "http://repository.cloudera.com/artifactory/cloudera-repos/",
	  "cloudera"             at "https://repository.cloudera.com/content/repositories/releases",
      // For Sonatype publishing
      // "sonatype-snapshots"   at "https://oss.sonatype.org/content/repositories/snapshots",
      // "sonatype-staging"     at "https://oss.sonatype.org/service/local/staging/deploy/maven2/",
      // also check the local Maven repository ~/.m2
      Resolver.mavenLocal,
	  Resolver.sonatypeRepo("releases"),
      Resolver.sonatypeRepo("snaspshots")
)