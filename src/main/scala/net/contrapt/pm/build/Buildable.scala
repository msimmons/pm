package net.contrapt.pm.build

import net.contrapt.pm.tool.Context

/**
 * Something that is buildable responds to a build command with
 * an optional target
 *
 */
trait Buildable {
	
	var builder:Builder = null 
	
	def build(context:Context) = builder.build(context)
	
	def build(context:Context, target : String) = builder.build(context, target)

}