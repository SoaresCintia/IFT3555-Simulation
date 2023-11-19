lazy val simulation = project
    .settings(
        scalaVersion:="3.3.1",
        libraryDependencies +="org.scalameta" %% "munit" % "0.7.29" % Test)