package net.contrapt.pm.tool

abstract class ToolProcess(val name:String, val config:ToolConfig, val handler:OutputHandler) {

	var command = config.command

	var env = config.env

	def run:Unit

}
