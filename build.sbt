lazy val simulation = project
  .settings(
    scalaVersion := "3.3.1",
    libraryDependencies ++= Seq(
      "org.scalameta" %% "munit" % "0.7.29" % Test,
      "ca.umontreal.iro.simul" % "ssj" % "3.3.2"
    )
  )
