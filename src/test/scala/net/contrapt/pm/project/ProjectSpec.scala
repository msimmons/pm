package net.contrapt.pm.project

import java.io.File

import net.contrapt.pm.AbstractSpec

class ProjectSpec extends AbstractSpec {

  "A project" should "cache all directories under its baseDir" in {
    val project = new Project("project1", new File(baseDir, "project1"))
    project.getDirectories.size should equal (6)
  }

  it should "cache all files under its baseDir" in {
    val project = new Project("project1", new File(baseDir, "project1"))
    project.getFiles.size should equal(3)
  }

  it should "exclude directories subject to an exclusion filter" in {
    val project = new Project("project1", new File(baseDir, "project1"))
    project addExclusion "target"
    project.getDirectories.size should equal(4)
    project.getFiles.size should equal(2)
  }

  it should "update directory cache when an exclusion is added" is pending
  it should "update file cache when an exclusion is added" is pending
  it should "support multiple exclusion filters" is pending

  it should "not allow setting the parent to itself" in {
    val parent = new Project("project1", new File(baseDir, "project1"))
    evaluating { parent setParent parent } should produce [IllegalStateException]
  }

  it should "support setting and clearing a parent project" in {
    val parent = new Project("project1", new File(baseDir, "project1"))
    val child = new Project("project2", new File(baseDir, "project2"))
    child setParent parent
    parent.getChildren.size should equal (1)
    child.getParent should equal (parent)
    child setParent null
    parent.getChildren.size should equal (0)
    child.getParent should equal (null)
  }

  it should "resolve properties and inherit from a parent" in {
    val child = new Project("child", new File(baseDir, "project1"))
    child.setProperty("foo", "foo.child")
    child.setProperty("bar", "bar.child")
    child.getProperty("foo") should equal ("foo.child")
    child.getProperty("bar") should equal ("bar.child")
    child.getProperty("baz") should equal ("")
    val parent = new Project("parent", new File(baseDir, "project2"))
    parent.setProperty("bar", "bar.parent")
    parent.setProperty("baz", "baz.parent")
    child setParent parent
    child.getProperty("foo") should equal ("foo.child")
    child.getProperty("bar") should equal ("bar.child")
    child.getProperty("baz") should equal ("baz.parent")
    child.getProperty("buz") should equal ("")
  }

  it should "support equality comparisons correctly" in {
    val p1 = new Project("foo", new File(baseDir, "project1"))
    val p2 = new Project("foo", new File(baseDir, "project1"))
    val p3 = new Project("bar", new File(baseDir, "project1"))
    p1 should not equal (null)
    p1 should not equal ("foo")
    p1 should not equal (p3)
    p1 should equal (p2)
    p1.hashCode should equal (p2.hashCode)
  }

  it should "inherit exclusions from parent projects" in {
    val p1 = new Project("p1", new File(baseDir, "project1"))
    val p2 = new Project("p2", new File(baseDir, "project1/src"))
    val p3 = new Project("p3", new File(baseDir, "project1/src/main"))
    p3 setParent p2
    p2 setParent p1
    p1.getDirectories.size should equal (3)
    p2.getDirectories.size should equal (1)
    p3.getDirectories.size should equal (2)
  }

  it should "update references when a parent is cleared" in {
    val p1 = new Project("p1", new File(baseDir, "project1"))
    val p2 = new Project("p2", new File(baseDir, "project1/src"))
    val p3 = new Project("p3", new File(baseDir, "project1/src/main"))
    p3 setParent p1
    p1.getChildren.size should equal (1)
    p3 setParent p2
    p1.getChildren.size should equal (0)
    p2.getChildren.size should equal (1)
    p3 setParent null
    p1.getChildren.size should equal (0)
    p2.getChildren.size should equal (0)
  }

}
