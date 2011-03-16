package net.contrapt.pm.scm

import net.contrapt.pm.tool.Context

/**
 * A source code management system that supports standard operations on managed
 * resources
 */
trait Manager {

	/**
	* Add a file to the SCM system
	*/
	def add(context:Context)

	/**
	* Remove the file from SCM
	*/
	def delete(context:Context)
	
	/**
	* Checkout a file
	*/
	def checkout(context:Context)

	/**
	* Commit changes to a file?
	*/
	def commit(context:Context)

	/**
	* Diff file with the head revision
	*/
	def diff(context:Context)

	/**
	* Diff file with the given revision
	*/
	def diff(context:Context, revision:String)

	/**
	* Revert changes to the file
	*/
	def revert(context:Context)

	/**
	* Return the status of the file
	*/
	def status(context:Context)
}