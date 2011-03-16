package net.contrapt.pm.tool

import scala.util.matching.Regex
import scala.util.matching.Regex.Match

/**
* Scan each line of output looking for error or warning info matching the given
* patterns.  Typical use is to find compilation errors from the output of a build
* tool.  Supports an error pattern, a warning pattern and an <i>extra</i> pattern
* which adds additional matched text lines to an error or warning
*/
class ErrorOutputHandler(error:ErrorMatcher, warning:ErrorMatcher, extra:Regex) extends OutputHandler {

	private var currentRecord:ErrorRecord = null
	var records = List[ErrorRecord]()
  
	override def processLine(line:String) = {
		currentRecord match {
			case null => newRecord(line)
			case _ => existingRecord(line)
		}
	}

	override def finish = {
		currentRecord match {
			case null => Nil
			case _ => finishRecord(null)
		}
	}

	/**
	* See if there is an error or warning; if not, current record remains
	* null
	*/
	private def newRecord(line:String) = {
		currentRecord = error.getMatch(line)
		if ( currentRecord == null ) currentRecord = warning.getMatch(line)
	}

	/**
	* See if there is a new error or warning; if not, see if there is extra info
	* to append to the existing record.  If yes, finish the existing record and
	* start on the new
	*/
	private def existingRecord(line:String) = {
		var record = error.getMatch(line)
		if ( record == null ) record = warning.getMatch(line)
		record match {
			case null => addExtra(extra.findFirstMatchIn(line).orNull)
			case _ => finishRecord(record)
		}
	}

	/**
	* Add extra info to the current error record if the line matches the
	* extra expression
	*/
	private def addExtra(extraMatch:Match) = {
		extraMatch match {
			case null => finishRecord(null)
			case _ => currentRecord.extra = currentRecord.extra :+ extraMatch.matched
		}
	}

	/**
	* Insert record into list and clear the current record
	*/
	private def finishRecord(record:ErrorRecord) {
		records = records :+ currentRecord
		currentRecord = record
	}

}
