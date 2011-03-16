package net.contrapt.pm.tool

/**
* An output handler that simply echos the output to standard out
*/
class EchoOutputHandler extends OutputHandler {

	override def processLine(line:String) = println(line)

}
