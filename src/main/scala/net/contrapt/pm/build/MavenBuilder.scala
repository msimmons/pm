package net.contrapt.pm.build

import java.io.File

import net.contrapt.pm.PropertyProvider
import net.contrapt.pm.tool.ExternalToolConfig
import net.contrapt.pm.tool.Context
import net.contrapt.pm.tool.EchoOutputHandler

/**
* A maven implementation of a builder
*/
class MavenBuilder(val pomFile : File, val defaultTarget:String) extends Builder with PropertyProvider {
  
	private val command = "${m2.home}/bin/mvn ${m2.flags} -f \"${m2.pom}\" ${m2.target}"
	private val env = Map[String,String]("MAVEN_OPTS" -> "${m2.opts}")
	private val toolConfig = new ExternalToolConfig("maven", command, env)

	private val targets = Set[String]("clean","install","test","package")

	override def build(context:Context) = buildInternal(context, defaultTarget)

	override def build(context:Context, target:String) = buildInternal(context, target)
	
	private def buildInternal(context:Context, target:String) = {
		val targetString = customTargets.keySet.contains(target) match {
			case true => customTargets(target)
			case _ => target
		}
		context.setProperty("m2.pom", pomFile.getAbsolutePath)
		context.setProperty("m2.target", targetString)
		context.addProvider(this)
		context.addTool(toolConfig, new EchoOutputHandler)
		context.run
	}

	override def listTargets = targets ++ customTargets.keySet

	override def getProperty(key:String) = null
}