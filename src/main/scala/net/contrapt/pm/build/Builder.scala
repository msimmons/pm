package net.contrapt.pm.build

import scala.collection.immutable.HashMap

import net.contrapt.pm.tool.Context

/**
* A builder knows how to build a project.  A specific implementation knows 
* what targets are available.  In addition, user may add custom targets
* identifiable by name which may include multiple normal targets and or additional
* flags
*/
trait Builder {

	protected var customTargets = HashMap[String, String]() 
	
	/**
	* Build the default target
	*/
	def build(context:Context)
	
	/**
	* Build the given target
	*/
	def build(context:Context, target : String)

	/**
	* Return a list of available targets
	*/
	def listTargets:Set[String]

	/**
	* Add a custom target, which typically could be a composition of multiple
	* targets with special flags etc
	*/
	def addTarget(name:String, target:String) = {
	  assert( !(customTargets.keySet.contains(name)) )
	  customTargets += name -> target
	}
}