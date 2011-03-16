package net.contrapt.pm.project

import scala.collection.immutable.HashMap
import scala.collection.immutable.HashSet
import scala.util.matching.Regex

import java.io.File

import net.contrapt.pm.scm.Managed
import net.contrapt.pm.build.Buildable
import net.contrapt.pm.tool.ToolConfig
import net.contrapt.pm.PropertyResolver

/**
 * A project manages file system resources relative to a base directory allowing
 * easy access to the resources, filtering, excluding
 * <p>
 * A project also manages properties that can be used as substitution placeholders
 * in various contexts especially for running tools
 * <p>
 * A project can define tools that are used to perform operations on files
 * <p>
 * Projects can inherit and override properties and tools from parent projects
 * <p>
 * A project also maintains information about 
 * <li> How it is built (ant, maven, make)
 * <li> What SCM system it is using
 * <li> Other?
 * <p>
 * A project can have a single parent and multiple children.  Standard practice
 * would be to define a global default project that defines tools, properties and
 * exclusions which should be available to all child projects
 *
 */
class Project(val name:String, val location:File) extends PropertyResolver with Buildable with Managed {
	
	/** The project's parent, if any */
	private var parent:Project = null

	/** The project's children, if any */
	private var children = HashSet[Project]()

	/** The last time files were cached */
	private var lastCached : Long = 0

	/** The set of all directories in the project */
	private var directories = HashSet[File](location)

	/** Map of all files in the project to a unique name */
	private var files = HashMap[String, File]()

	/** A set of source directory name patterns to exclude from the file cache */
	private var exclusions = HashSet[Regex]()

	/** A set of tools defined by this project */
	private var tools = HashSet[ToolConfig]()

	/**
	* Set the given project as this project's parent, returning the previous
	* parent if any
	*/
	def setParent(newParent:Project): Project = {
		if ( newParent == this ) throw new IllegalStateException("You cannot set project's parent to itself")
		val oldParent = parent
		if ( newParent != null ) newParent.children += this
		if ( oldParent != null ) oldParent.children -= this
		setParentResolver(newParent)
		parent = newParent
		oldParent
	}

	/**
	* Get this project's parent
	*/
	def getParent:Project = parent

	/**
	* Get this project's children
	*/
	def getChildren:Set[Project] = children

	/**
	* Add a directory exclusion in the form of a regular expression.  Directories
	* matching the expression will not be included in the list of files for this
	* project.  Exclusions are inherited from the parent project as well
	*/
	def addExclusion(exclusion:String):Unit = {
		exclusions += exclusion.r
		directories.foreach(dir => if ( isExcluded(dir) ) directories -= dir)
		// TODO How about files?
	}

	/**
	* Return all the directories managed by this project
	*/
	def getDirectories : Set[File] = {
		directories.foreach(cacheDirectory)
		lastCached = System.currentTimeMillis
		directories
	}

	/**
	* Return all the files managed by this project
	*/
	def getFiles : Map[String, File] = {
		getDirectories
		files
	}

	/**
	* Remove all child and parent references from this project
	*/
	def removeReferences = {
		setParent(null)
		children.foreach(_.setParent(null))
		children = children.empty
	}

	private def cacheDirectory(dir:File): Unit = {
		if ( isExcluded(dir) ) None
		else if ( isChildDir(dir) ) None
		else {
			if ( directories.contains(dir) ) None
			else directories += dir
			if (dir.lastModified < lastCached) None
			else cacheFiles(dir)
		}
	}

	/**
	* A directory is excluded if its name matches an exlusion pattern from this
	* project or any parent project
	*/
	private def isExcluded(dir:File): Boolean = {
		exclusions.find(_.pattern.matcher(dir.getName).matches) != None ||
		(parent != null && parent.isExcluded(dir))
	}

	/**
	* A directory is a child if it is the location of a child project
	*/
	private def isChildDir(dir:File):Boolean = {
		children.find(_.location.equals(dir)) != None
	}

	private def cacheFiles(dir:File): Unit = {
		val fileList = dir.listFiles
		if ( fileList == null ) None
		else fileList.foreach(cacheFile)
	}

	private def cacheFile(file:File): Unit = {
		file.isDirectory match {
			case true => cacheDirectory(file)
			case _ => files += createFileKey(file)->file
		}
	}

	/**
	* Return a key that will uniquely identify the file within this project
	*/
	def createFileKey(file:File) : String = file.getName+" ("+file.getParent.replace(location.getPath, "")+")";

	override def toString = getClass.getSimpleName + "[" + name + "]"

	override def equals(other:Any): Boolean = {
		other match {
			case that:Project => this.name equals that.name
			case _ => false
		}
	}

	override def hashCode:Int = name.hashCode
}