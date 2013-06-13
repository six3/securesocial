import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

    val appName         = "ssdemo-java"
    val appVersion      = "1.0"

    val appDependencies = Seq(
<<<<<<< HEAD
        "securesocial" %% "securesocial" % "2.0.12"
    )
    val main = PlayProject(appName, appVersion, appDependencies, mainLang = JAVA).settings(
	resolvers += Resolver.url("sbt-plugin-releases", new URL("http://repo.scala-sbt.org/scalasbt/sbt-plugin-releases/"))(Resolver.ivyStylePatterns)
=======
	javaCore,
        "securesocial" %% "securesocial" % "master-SNAPSHOT"
    )

    val main = play.Project(appName, appVersion, appDependencies).settings(
	resolvers += Resolver.url("sbt-plugin-snapshots", new URL("http://repo.scala-sbt.org/scalasbt/sbt-plugin-snapshots/"))(Resolver.ivyStylePatterns)
>>>>>>> 121d022ebfce7f059788d09fca6bd9d9e666c9a1
    )

}
