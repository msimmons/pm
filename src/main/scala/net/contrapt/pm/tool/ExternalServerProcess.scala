package net.contrapt.pm.tool

import scala.util.matching.Regex

/**
* This class implements a server running as an external process writing its output to 
* a temporary file.  As opposed to a tool, a server is expected to run for a long time. 
* There should be a way to discover when/whether it has started sucessfully and a 
* way to stop it normally and find out when it has stopped
*/
class ExternalServerProcess(name:String, config:ToolConfig, handler:OutputHandler, started:Regex, stopped:Regex) extends ExternalToolProcess(name, config, handler) {
	
	object Status extends Enumeration {
		val Starting = Value("starting")
		val Started = Value("started")
		val Stopped = Value("stopped")
	}

	private var status = Status.Starting

	def isStarted = status == Status.Started

	def isStopped = status == Status.Stopped

	override protected def processLine(line:String) = {
		super.processLine(line)
		println(line)
		status match {
			case Status.Starting => checkStarted(line)
			case Status.Started => checkStopped(line)
			case Status.Stopped => Nil
		}
	}

	private def checkStarted(line:String) = {
		started.findFirstIn(line) match {
			case Some(_) => status = Status.Started
			case None => Nil
		}
	}

	private def checkStopped(line:String) = {
		stopped.findFirstIn(line) match {
			case Some(_) => status = Status.Stopped
			case None => Nil
		}
	}

	override def toString = super.toString+"["+status+"]"

}
