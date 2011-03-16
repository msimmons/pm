package net.contrapt.pm

import java.io.File

class PropertyResolverSpec extends AbstractSpec {

  object child extends PropertyResolver

  "A property resolver" should "resolve properties" in {
    child.setProperty("foo", "foo.child")
    child.setProperty("bar", "bar.child")
    child.getProperty("foo") should equal ("foo.child")
    child.getProperty("bar") should equal ("bar.child")
    child.getProperty("baz") should equal ("")
  }

  it should "inherit properties from a parent resolver" in {
    object parent extends PropertyResolver
    parent.setProperty("bar", "bar.parent")
    parent.setProperty("baz", "baz.parent")
    child setParentResolver parent
    child.getProperty("foo") should equal ("foo.child")
    child.getProperty("bar") should equal ("bar.child")
    child.getProperty("baz") should equal ("baz.parent")
    child.getProperty("buz") should equal ("")
  }

}
