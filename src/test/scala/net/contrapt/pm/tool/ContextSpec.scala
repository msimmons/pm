package net.contrapt.pm.tool

import scala.util.matching.Regex
import scala.util.matching.Regex.Match

import net.contrapt.pm.AbstractSpec
import net.contrapt.pm.project.Project

class ContextSpec extends AbstractSpec {

  "A Context" should "run a tool with properties replaced" in {
    val p = new Project("test", baseDir)
    object c extends Context(p, baseDir)
    c.setProperty("p1", "p1v")
    c.setProperty("basedir", "../")
    object t extends ExternalToolConfig("test", "ls -aFl ${basedir}", Map("EDITOR"->"${p1}"))
    object h extends OutputHandler {
      override def processLine(line:String) = println(line)
    }
    c addTool (t, h)
    c.run
  }

  it should "know how to replace ${} properties" in {
    val p = new Project("test", baseDir)
    object c extends Context(p, baseDir)
    c.setProperty("p1", "p1v")
    c.setProperty("p2", "p2v")
    c.setProperty("p3", "p3v")
    c.replaceProperties("") should equal ("")
    c.replaceProperties("hello") should equal ("hello")
    c.replaceProperties("${hello}") should equal ("")
    c.replaceProperties("${}") should equal ("${}")
    c.replaceProperties("${!a}") should equal  ("${!a}")
    c.replaceProperties("hello, ${p1} is ${p2} or is it ${p3}? $${f") should equal ("hello, p1v is p2v or is it p3v? $${f")
  }

  it should "treat its project as a parent resolver" in {
    val p = new Project("test", baseDir)
    p.setProperty("m2.home", "c:\\apache-maven-2.2.1")
    object c extends Context(p, baseDir)
    c.replaceProperties("${m2.home}") should equal ("c:\\apache-maven-2.2.1")
  }

}
