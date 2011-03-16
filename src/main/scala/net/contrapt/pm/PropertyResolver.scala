package net.contrapt.pm

import scala.collection.immutable.HashMap

trait PropertyResolver extends PropertyProvider {

	/** Map of project properties */
	private var properties = HashMap[String, String]()

	/** A parent resolver if appropriate */
	private var parent:PropertyResolver = null

	/** An optional list of additional providers, possibly provided by external sources */
	private var providers = List[PropertyProvider]()

	/**
	* Set a property key->value association
	*/
	def setProperty(key:String, value:String) = {
	  key match {
	    case null => throw new IllegalArgumentException("Key must not be null") 
	    case _ => properties += key-> (if (value == null) "" else value)
	  }
	}

	/**
	* Return the property associated with the given key; optionally delegate to
	* additional providers or a parent resolver
	*/
  def getProperty(key:String):String = {
  	  var value:Option[String] = properties.get(key) match {
  	  	  case v:Some[String] => v
  	  	  case None => getProvidedProperty(key)
  	  }
  	  value match {
  	  	  case None => if ( parent == null ) "" else parent.getProperty(key)
  	  	  case Some(v) => v.replace("\\", "\\\\")
  	  }
  }

  /**
  * Look for the given key in all the providers of this resolver (including this one)
  */
  private def getProvidedProperty(key:String):Option[String] = {
  	  var value:String = null
  	  providers.iterator.find(p => {value = p.getProperty(key); value != null})
  	  if ( value == null ) None else Some(value)
  }
  
  protected[pm] def setParentResolver(parent:PropertyResolver) = { this.parent = parent }
  protected[pm] def addProvider(provider:PropertyProvider) = providers :+ provider

}
