package net.contrapt.pm.build

import java.io.File

import net.contrapt.pm.AbstractSpec
import net.contrapt.pm.project.Project
import net.contrapt.pm.tool.Context


class MavenBuilderSpec extends AbstractSpec {

  "A MavenBuilder" should "run the default or given target on its build file" in {
    val p = new Project("test", baseDir)
    p.setProperty("m2.home", "c:\\apache-maven-2.2.1")
    object c extends Context(p, baseDir)
    val b = new MavenBuilder(new File(baseDir, "project2/pom.xml"), "package")
    b.build(c)
    b.build(c, "clean")
  }

  it should "allow definition of a custom target identified by name" in {
    val p = new Project("test", baseDir)
    p.setProperty("m2.home", "c:\\apache-maven-2.2.1")
    object c extends Context(p, baseDir)
    val b = new MavenBuilder(new File(baseDir, "project2/pom.xml"), "package")
    b.addTarget("custom", "clean package -Dtests=none")
    b.build(c, "custom")
  }

}
