resolvers ++= Seq(
  //"Sonatype snapshots"          at "http://oss.sonatype.org/content/repositories/snapshots/",
  //"Sonatype releases"           at "https://oss.sonatype.org/content/repositories/releases/",
  "Bintray sbt plugin releases" at "http://dl.bintray.com/sbt/sbt-plugin-releases/"
)

//addSbtPlugin("com.github.mpeltonen" % "sbt-idea" % "1.5.0-SNAPSHOT")

addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.5")