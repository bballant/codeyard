name := "docintel"

version := "1.0"

scalaVersion := "2.9.2"

libraryDependencies ++= Seq(
  // "org.apache.lucene" % "lucene-analyzers-common" % "4.1.0",
  // "nz.ac.waikato.cms.weka" % "weka-stable" % "3.6.9",
  // "org.jsefa" % "jsefa" % "0.9.3.RELEASE",
  "org.apache.mahout" % "mahout" % "0.7",
  "org.apache.lucene" % "lucene-analyzers-common" % "4.1.0",
  "org.apache.lucene" % "lucene-core" % "4.1.0",
  "org.scalanlp" %% "breeze" % "0.1"
)
