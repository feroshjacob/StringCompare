name := "StringCompare"

version := "0.0.1"

organization := "cb.datascience"

scalaVersion := "2.10.4"

resolvers ++= Seq("snapshots"     at "https://oss.sonatype.org/content/repositories/snapshots",
                  "staging"       at "https://oss.sonatype.org/content/repositories/staging",
                  "releases"      at "https://oss.sonatype.org/content/repositories/releases"
                 )



scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature")

libraryDependencies ++= {
  val liftVersion = "2.6-M4"
  Seq(
     "org.apache.lucene" % "lucene-core" % "4.9.0",
     "org.apache.lucene" % "lucene-analyzers-common" % "4.9.0",
      "org.apache.lucene" % "lucene-queryparser" % "4.9.0",     
   "org.scalatest" %% "scalatest" % "1.9.1" % "test"
  )
} 


