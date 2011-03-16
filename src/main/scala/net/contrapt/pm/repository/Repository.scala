package net.contrapt.pm.repository

import java.io.File
import scala.collection.immutable.Map

import net.contrapt.pm.project.Project

/**
* The main reposistory for the project manager that manages access to the projects
* and related domain objects for the project manager
*/
class Repository {
  
	/** Maps a project name to its project for finding based on name */
	private var projects = Map[String, Project]()

	/** Maps a project location to its project for finding based on current buffer */
	private var projectsByLocation = Map[File, Project]()

	/**
	* Add a new project to the repository
	*/
	def addProject(project:Project) = {
		if ( projects contains project.name ) 
      throw new IllegalStateException("Project with name '"+project.name+"' exists already")
      if ( projectsByLocation contains project.location ) 
      throw new IllegalStateException("Project at location '"+project.location+"' exists already")
      projects += project.name -> project
      projectsByLocation += project.location -> project
   }

   /**
   * Remove the given project from the repository
   */
   def removeProject(project:Project) = {
   	if ( !(projects contains project.name) ) 
      throw new IllegalStateException("Project with name '"+project.name+"' does not exist")
      if ( !(projectsByLocation contains project.location) ) 
      throw new IllegalStateException("Project at location '"+project.location+"' does not exist")
      project.removeReferences
      projects -= project.name
      projectsByLocation -= project.location
   }

   /**
   * Does the repository contain the project with the given name
   */
   def containsProject(name:String):Boolean = projects contains name

   /**
   * Find the project with the given name.  If no such project exists, the
   * result will be null
   */
   def find(name:String):Project = {
   	projects(name)
   }

   /**
   * Find the project for the given file.  This recursively search the file's
   * parent directories until a project with a matching location is found.  If
   * none is found, then null is returned
   */
   def find(file:File):Project = {
   	if ( file == null ) null
   	else if ( file isDirectory ) {
   		if ( projectsByLocation contains file ) projectsByLocation(file)
   		else find(file.getParentFile)
   	}
   	else find(file.getParentFile)
   }

   /**
   * List root projects -- that is, projects without parents
   */
   def rootProjects:List[Project] = {
   	projects.values.filter(p => p.getParent == null).toList
   }

}
