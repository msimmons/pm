package net.contrapt.pm.tool

/**
* A record returned by an {@link ErrorMatcher}.  Specifies the matched filename,
* line number, column and error message
*/
class ErrorRecord(val file:String, val line:Int, val column:Int, val message:String) {
  
	var extra:List[String] = List[String]()
	
	override def toString = file+"["+line+","+column+"] "+message
}
