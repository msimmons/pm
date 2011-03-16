package net.contrapt.pm.tool

import scala.collection.immutable.HashMap

/**
* The specification of a tool including a name, a command line and environment
* map.  The command line and environment values may contain property placeholders
* that will be replaced dynamically at each execution depending on the specific
* context it is running in
* <p>
* An important feature of tools is that they are uniquely identified by a combination
* of <i>name</i> and <i>resourceType</i>.  This allows defining a set of tools
* which have the same name but apply to different types of resources, eg, a <i>compile</i>
* tool would invoke a different tool depending on the type of the current resource
*/
abstract class ToolConfig(val name:String, val command:String, val env:Map[String,String]) {
  
	/** The type of file this tool is applicable to; defaults to any type */
	var resourceType = "*"

	/**
	* Subclasses override this to provide appropriate process for the type of
	* tool
	*/
	def getProcess(basename:String, handler:OutputHandler):ToolProcess
  
	/**
	* Return the unique key of this tool (name and resourceType)
	*/
	def key = name+"."+resourceType

	override def toString = getClass.getSimpleName+"["+key+"]"

	override def equals(other:Any):Boolean = {
		other match {
			case other:ToolConfig => this.key.equals(other.key)
			case _ => false
		}
	}

	override def hashCode:Int = key.hashCode
}
