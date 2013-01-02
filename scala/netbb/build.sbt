organization := "com.github.bballant"

name := "netbb"

version := "0.0.1-SNAPSHOT"

libraryDependencies ++= Seq("io.netty" % "netty" % "3.5.11.Final")

resolvers ++= Seq("jboss repo" at "http://repository.jboss.org/nexus/content/groups/public-jboss/")
