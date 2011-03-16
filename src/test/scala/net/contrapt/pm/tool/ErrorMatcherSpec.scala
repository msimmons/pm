package net.contrapt.pm.tool

import scala.util.matching.Regex
import scala.util.matching.Regex.Match

import net.contrapt.pm.AbstractSpec

class ErrorMatcherSpec extends AbstractSpec {
  
  "An ErrorMatcher" should "find the file, line, column and message matching the given regex" in {
    val file = "C:\\Documents and Settings\\msimmons\\scala\\pm\\src\\main\\scala\\net\\contrapt\\pm\\tool\\ErrorOutputHandler.scala"
    val line = 10
    val message = ":error: class ErrorOutputHandler needs to be abstract, since variable currentRecord is not defined"
    val m = new ErrorMatcher("\\[ERROR\\][\\s]+(.+?):(\\d+)(.+)".r, 1, 2, 0, 3)
    val er = m.getMatch("[ERROR] "+file+":"+line+message)
    er should not be null
    file should equal (er.file)
    line should equal (er.line)
    0 should equal (er.column)
    message should equal (er.message)
  }

}
