ThisBuild / tlBaseVersion := "0.23"
ThisBuild / startYear := Some(2018)
ThisBuild / licenses := Seq(License.Apache2)
ThisBuild / developers := List(
  tlGitHubDev("rossabaker", "Ross A. Baker"),
  tlGitHubDev("cquiroz", "Carlos Quiroz"),
)
ThisBuild / tlCiReleaseBranches := Seq.empty

val Scala213 = "2.13.18"
val Scala212 = "2.12.21"

/*
 * Scala 3.10 changed the binary representation of objects nested inside a package
 * object. In 0.23.11, package$ contained:
 *
 * public static final ...package$all$ all;
 * public static final ...package$booPickle$ booPickle;
 *
 * With Scala 3.10.1, those fields are no longer emitted.
 * MiMa is accurately reporting that binary-shape difference.
 * This affects both JVM and Scala.js, although rootJS happened to report it first.
 * Scala 3.10 introduced package-object changes through SIP-68.
 * Scala 3 change (https://github.com/scala/scala3/pull/24586)
 *
 * This project has not been supported since 2024, so this may be the final and only release.
 */
val Scala3 = "3.3.9"

ThisBuild / crossScalaVersions := Seq(Scala212, Scala213, Scala3)
ThisBuild / scalaVersion := Scala213

ThisBuild / githubWorkflowJavaVersions := Seq(
  JavaSpec.temurin("17"), // first entry is used by the publish job
  JavaSpec.temurin("8"),
  JavaSpec.temurin("11"),
  JavaSpec.temurin("21"),
)

ThisBuild / githubWorkflowBuildMatrixExclusions := Seq(
  // JVM: Scala 2.12 on Java 8
  MatrixExclude(
    Map("project" -> "rootJVM", "scala" -> "2.12", "java" -> "temurin@11")
  ),
  MatrixExclude(
    Map("project" -> "rootJVM", "scala" -> "2.12", "java" -> "temurin@17")
  ),
  MatrixExclude(
    Map("project" -> "rootJVM", "scala" -> "2.12", "java" -> "temurin@21")
  ),

  // JVM: Scala 2.13 on Java 8, 11, and 17
  MatrixExclude(
    Map("project" -> "rootJVM", "scala" -> "2.13", "java" -> "temurin@21")
  ),

  // JVM: Scala 3 on Java 17 and 21
  MatrixExclude(
    Map("project" -> "rootJVM", "scala" -> "3", "java" -> "temurin@8")
  ),
  MatrixExclude(
    Map("project" -> "rootJVM", "scala" -> "3", "java" -> "temurin@11")
  ),

  // Scala.js 1.23 requires Java 17+; test each Scala version once on 17
  MatrixExclude(Map("project" -> "rootJS", "java" -> "temurin@8")),
  MatrixExclude(Map("project" -> "rootJS", "java" -> "temurin@11")),
  MatrixExclude(Map("project" -> "rootJS", "java" -> "temurin@21")),
)

lazy val root = tlCrossRootProject.aggregate(boopickle)

val http4sVersion = "0.23.39"
val boopickleVersion = "1.5.0"

//This is the last munitVersion scalaCheck for 2.13
//So either stop upgrading this version, or stop supporting 2.13
val munitVersion = "1.3.1"
val munitCatsEffectVersion = "2.2.1"

ThisBuild / resolvers +=
  "s01 snapshots".at("https://s01.oss.sonatype.org/content/repositories/snapshots/")

lazy val boopickle = crossProject(JVMPlatform, JSPlatform)
  .crossType(CrossType.Pure)
  .in(file("boopickle"))
  .settings(
    startYear := Some(2018),
    licenses := Seq(License.Apache2),
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
