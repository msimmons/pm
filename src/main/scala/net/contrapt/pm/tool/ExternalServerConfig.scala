package net.contrapt.pm.tool

import scala.collection.immutable.Map
import scala.util.matching.Regex

/**
* Configures an external server process; a server is distinct from a tool in that
* it is long running and provides a separate command to stop it
*/
class ExternalServerConfig(name:String, command:String, stopCommand:String, env:Map[String,String], started:Regex, stopped:Regex) extends ToolConfig(name, command, env) {

	val stopConfig = new ExternalToolConfig(name, stopCommand, env)

	def getProcess(name:String, handler:OutputHandler) = new ExternalServerProcess(name, this, handler, started, stopped)

	def getStopProcess(name:String, handler:OutputHandler) = new ExternalToolProcess(name, stopConfig, handler)

}