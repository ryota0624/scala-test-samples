name := "scala-test-samples"

version := "0.1"

scalaVersion := "2.13.4"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.2.2" % Test,
  "org.scalatestplus" %% "scalacheck-1-14" % "3.2.2.0" % Test,
  "com.dimafeng" %% "testcontainers-scala-scalatest" % "0.39.1" % Test
)
