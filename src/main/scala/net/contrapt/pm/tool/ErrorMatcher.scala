package net.contrapt.pm.tool

import scala.util.matching.Regex
import scala.util.matching.Regex.Match

/**
* Encapsulate the definition of an error output pattern matcher which matches
* output against error and/or warning patterns returning the appropriate file, 
* line and message if any
*
* The indexes represent the index of the given item in the regular expression
* pattern
*/
class ErrorMatcher(val pattern:Regex, val fileIndex:Int, val lineIndex:Int, val columnIndex:Int, val messageIndex:Int) {

	def getMatch(line:String):ErrorRecord = {
		val m = pattern.findFirstMatchIn(line).orNull
		m match {
			case null => null
			case _ => createRecord(m)
		}
	}

	private def createRecord(m:Match) = {
		val file = m.group(fileIndex) 
		val line = m.group(lineIndex).toInt
		val column = if ( columnIndex > 0 ) m.group(columnIndex).toInt else 0 
		val msg = m.group(messageIndex)
		new ErrorRecord(file, line, column, msg)
	}
}
