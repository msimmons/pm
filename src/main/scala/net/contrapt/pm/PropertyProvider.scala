package net.contrapt.pm

import scala.collection.immutable.HashMap

/**
* A read-only object that provides properties as String->String key/value 
* mappings
*/
trait PropertyProvider {

	/**
	* Return the property associated with the given key
	*/
  def getProperty(key:String):String

}
