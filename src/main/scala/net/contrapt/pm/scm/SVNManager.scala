package net.contrapt.pm.scm

import net.contrapt.pm.tool.{Context, ExternalToolConfig, EchoOutputHandler}

/**
* Subversion source code management interface
*/
class SVNManager extends Manager {

	private val command = "${svn.home}/bin/svn ${svn.flags} {svn.command} \"${svn.file}\""
	private val env = Map[String,String]()
	private val toolConfig = new ExternalToolConfig("svn", command, env)

	def add(context:Context) = {
		context.setProperty("svn.command", "add")
		context.setProperty("svn.file", context.file.getAbsolutePath)
		context.addTool(toolConfig, new EchoOutputHandler)
		context.run
	}

	def delete(context:Context) = {
	}
	
	def checkout(context:Context) = {
	}

	def commit(context:Context) = {
	}

	def diff(context:Context) = {
	}

	def diff(context:Context, revision:String) = {
	}

	def revert(context:Context) = {
	}

	def status(context:Context) = {
	}

}
