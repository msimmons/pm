package net.contrapt.pm.tool

import scala.util.matching.Regex
import scala.util.matching.Regex.Match
import java.io.File

import net.contrapt.pm.PropertyResolver
import net.contrapt.pm.project.Project

/**
 * A context encapsulates the environment in which a tool or list of tools
 * is run.  A context is always based on a project and a file within that project.
 *
 * The context will also store results of each tool run for potential use by the application
 * or downstream tools
 */
class Context(val project:Project, val file:File) extends PropertyResolver {

	setParentResolver(project)
  
	private var tools = List[ToolProcess]()
  
	/**
	* Add a tool; tools will be run in the order added
	*/
	def addTool(config:ToolConfig, handler:OutputHandler):Unit = {
		tools = tools :+ config.getProcess(project.name, handler)
	}

	/**
	* Return the collection of tools that have been added
	*/
	def getTools = tools

	/**
	* Run the tools in succession in the order they were added
	*/
	def run = {
		tools.foreach(prepare)
		runTools
		tools.foreach(println)
	}

	private def prepare(process:ToolProcess): Unit = {
		//promptForProperties(spec.tool)
		process.command = replaceProperties(process.config.command)
		process.env = replaceEnvironment(process.config.env)
	}

	/**
	* Default implementation runs each tool in the current thread.  Override to
	* do more complicated processing such as running in a separate thread or taking
	* action between tools
	*/
	protected def runTools:Unit = {
		tools.foreach(_.run)
	}

	/** RE to find property placeholders defined as ${..} */
	private val PROPERTY_RE = "\\$\\{([a-zA-Z0-9\\.]+)\\}".r

	/**
	* Replace all ${} style properties in the given string with appropriate project or buildfile
	* properties; do this recursively until no replacements are made
	*
	* @param arg The string containing properties to replace
	* @return A string with all substitutions made
	*/
	final def replaceProperties(arg:String):String =  {
		PROPERTY_RE.replaceAllIn(arg, m => getProperty(m.group(1)))
	}

	/**
	* Replace all ${} style properties in the given string with appropriate project or buildfile
	* properties; do this recursively until no replacements are made
	*
	* @param env An environment map with placeholders in the values
	* @return A map with all substitutions made
	*/
	final def replaceEnvironment(env:Map[String,String]):Map[String,String] =  {
		env.mapValues(replaceProperties)
	}

}
