package net.contrapt.pm.repository

import java.io.File

import net.contrapt.pm.AbstractSpec
import net.contrapt.pm.project.Project

class RepositorySpec extends AbstractSpec {
  
  "A Repository" should "find projects by name" in {
    val repo = new Repository
    val p1 = new Project("p1", new File(baseDir, "project1"))
    repo.addProject(p1)
    repo find "p1" should equal (p1)
  }

  it should "find the correct project for a file in that project" in {
    val repo = new Repository
    val p1 = new Project("p1", new File(baseDir, "project1"))
    repo.addProject(p1)
    repo find new File(baseDir, "project1/src/main/scala/Class1.scala") should equal (p1)
  }

  it should "return null if it doesn't find a project" in {
    val repo = new Repository
    val p1 = new Project("p1", new File(baseDir, "project1"))
    repo.addProject(p1)
    repo find baseDir should equal (null)
    repo find new File(baseDir, "foo/bar/baz/oz/notexist") should equal (null)
  }

  it should "not allow projects with duplicate names" in { 
    val repo = new Repository
    val p1 = new Project("p1", new File(baseDir, "project1"))
    repo.addProject(p1)
    val p2 = new Project("p1", new File(baseDir, "project2"))
    evaluating { repo addProject p2 } should produce [IllegalStateException]
  }

  it should "not allow projects with duplicate base directories" in {
    val repo = new Repository
    val p1 = new Project("p1", new File(baseDir, "project1"))
    repo.addProject(p1)
    val p2 = new Project("p2", new File(baseDir, "project1"))
    evaluating { repo addProject p2 } should produce [IllegalStateException] 
  }

  it should "complain about removing a non-existent project" in {
    val repo = new Repository
    val p1 = new Project("p1", new File(baseDir, "project1"))
    evaluating { repo removeProject p1 } should produce [IllegalStateException]
  }

  it should "allow finding all projects without parents (root projects" in { 
    val repo = new Repository
    val p1 = new Project("p1", new File(baseDir, "project1"))
    val p2 = new Project("p2", new File(baseDir, "project1/src/main"))
    val p3 = new Project("p3", new File(baseDir, "project1/src/main/scala"))
    repo addProject p1
    repo addProject p2
    repo addProject p3
    repo.rootProjects.size should equal (3)
    p3 setParent p2
    repo.rootProjects.size should equal (2)
    p2 setParent p1
    repo.rootProjects.size should equal (1)
  }

  it should "remove an existing project when asked" in {
    val repo = new Repository
    val p1 = new Project("p1", new File(baseDir, "project1"))
    repo addProject p1
    repo containsProject p1.name should equal (true)
    repo removeProject p1
    repo containsProject p1.name should equal (false)
    repo find new File(baseDir, "project1/pom.xml") should equal(null)
  }

  it should "update references when removing a child project" in {
    val repo = new Repository
    val p1 = new Project("p1", new File(baseDir, "project1"))
    val p2 = new Project("p2", new File(baseDir, "project2"))
    repo addProject p1
    repo addProject p2
    p2 setParent p1
    p1.getChildren.size should equal (1)
    p2.getParent should equal (p1)
    repo removeProject p2
    p1.getChildren.size should equal (0)
    p2.getParent should equal(null)
  }

  it should "update references when removing a parent project" in {
    val repo = new Repository
    val p1 = new Project("p1", new File(baseDir, "project1"))
    val p2 = new Project("p2", new File(baseDir, "project2"))
    repo addProject p1
    repo addProject p2
    p2 setParent p1
    p1.getChildren.size should equal (1)
    p2.getParent should equal (p1)
    repo removeProject p1
    p1.getChildren.size should equal (0)
    p2.getParent should equal(null)
  }

}
