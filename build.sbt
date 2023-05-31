ThisBuild / tlBaseVersion := "0.23"
ThisBuild / developers := List(
  tlGitHubDev("rossabaker", "Ross A. Baker")
)

val Scala213 = "2.13.10"
ThisBuild / crossScalaVersions := Seq("2.12.17", Scala213, "3.3.0")
ThisBuild / scalaVersion := Scala213

lazy val root = tlCrossRootProject.aggregate(boopickle)

val http4sVersion = "0.23.18"
val boopickleVersion = "1.4.0"
val munitVersion = "1.0.0-M7"
val munitCatsEffectVersion = "2.0.0-M3"

lazy val boopickle = crossProject(JVMPlatform, JSPlatform)
  .crossType(CrossType.Pure)
  .in(file("boopickle"))
  .settings(
    name := "http4s-boopickle",
    description := "Provides Boopickle codecs for http4s",
    startYear := Some(2018),
    libraryDependencies ++= Seq(
      "org.http4s" %%% "http4s-core" % http4sVersion,
      "io.suzaku" %%% "boopickle" % boopickleVersion,
      "org.scalameta" %%% "munit-scalacheck" % munitVersion % Test,
      "org.typelevel" %%% "munit-cats-effect" % munitCatsEffectVersion % Test,
      "org.http4s" %%% "http4s-laws" % http4sVersion % Test,
    ),
  )
  .jvmSettings(
    tlMimaPreviousVersions ++= (0 to 11).map(y => s"0.23.$y").toSet
  )
  .jsSettings(
    tlMimaPreviousVersions ++= (5 to 11).map(y => s"0.23.$y").toSet
  )
