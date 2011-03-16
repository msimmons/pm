package net.contrapt.pm.tool

import scala.collection.immutable.Map

/**
* Configuration for running an external tool; a command string plus some environment variables if necessary
*/
class ExternalToolConfig(name:String, command:String, env:Map[String,String]) extends ToolConfig(name, command, env) {

	def getProcess(name:String, handler:OutputHandler):ToolProcess = new ExternalToolProcess(name, this, handler)

}