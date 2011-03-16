package net.contrapt.pm.tool

import java.io.FileReader
import java.io.BufferedReader
import java.io.File

import net.contrapt.pm.AbstractSpec

class ErrorOutputHandlerSpec extends AbstractSpec {
  
	"An ErrorOutputHandler" should "find errors warnings and extra lines in a stream of output" in {
		val error = new ErrorMatcher("\\[ERROR\\][\\s]+(.+?):(\\d+)(.+)".r, 1, 2, 0, 3)
		val warning = new ErrorMatcher("\\[WARNING\\][\\s]+(.+?):(\\d+)(.+)".r, 1, 2, 0, 3)
		val extra = "\\[ERROR\\][\\s]*(.*)".r
		var handler = new ErrorOutputHandler(error, warning, extra)
		val in = new BufferedReader(new FileReader(baseDir+File.separator+"erroroutput.txt"))
		var line = in.readLine
		while ( line != null ) {
			handler.processLine(line)
			line = in.readLine
		}
		handler.records.size should equal (2)
		handler.records(0).extra.size should equal (1)
	}

}
