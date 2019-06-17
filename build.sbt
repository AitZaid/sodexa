name := "Sodexo-Data"
organization := "fr.sodexo"

version := "0.1"

// Do not upgrade to Scala 2.12 ! Spark is only packaged with scala 2.11.x for now.
scalaVersion := "2.11.12"

// Choosing last version for starting project
val sparkVersion = "2.4.1"

resolvers ++= Seq(
  "apache-snapshots" at "http://repository.apache.org/snapshots/"
)

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion % Provided force(),
  "org.apache.spark" %% "spark-sql" % sparkVersion % Provided force(),
  "org.apache.spark" %% "spark-mllib" % sparkVersion % Provided force(),
  "com.holdenkarau" %% "spark-testing-base" % "2.3.0_0.10.0" % "test",
  "com.microsoft.azure" %% "azure-cosmosdb-spark_2.3.0" % "1.3.3"
)

// Change default JVM options to accomodate local spark cluster testing.
// Instructions on : https://github.com/holdenk/spark-testing-base
fork in Test := true
javaOptions ++= Seq("-Xms512M", "-Xmx2048M", "-XX:MaxPermSize=2048M", "-XX:+CMSClassUnloadingEnabled")
parallelExecution in Test := false


enablePlugins(JavaAppPackaging)

mainClass in assembly := Some("fr.sodexo.citybike.CityBikeClustering")
mappings in Universal <<= (mappings in Universal, assembly in Compile) map { (mappings, fatJar) =>
  val filtered = mappings filter { case (file, name) =>  ! name.endsWith(".jar") }
  filtered :+ (fatJar -> ("lib/" + fatJar.getName))
}
scriptClasspath := Seq( (jarName in assembly).value)

