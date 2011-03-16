package net.contrapt.pm.tool

import net.contrapt.pm.AbstractSpec

class ExternalProcessSpec extends AbstractSpec {

  "An ExternalProcess" should "be able to split arguments into an array if asked" in {
    object t extends ExternalToolConfig("test", "ls", Map())
    val p = new ExternalToolProcess("ctx", t, null)
    p.splitArgs = true
    p.command2args("ls -aFl 'foo \"bar\"' \"baz is\" a file '' +3 -Dfoo").size should equal (11)
  }

  it should "create a runnable process" in {
    object t extends ExternalToolConfig("test", "dir", Map())
    val p = t.getProcess("ctx", null)
    p.run
    println(p)
  }

}
