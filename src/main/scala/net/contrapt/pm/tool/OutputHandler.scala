package net.contrapt.pm.tool

/**
* A class that processes output from a running process; this could be filtering for 
* error information or status information.
*/
abstract class OutputHandler {

   /**
   * Do any initalization 
   */
   def initialize:Unit = {}

   /**
   * Process a line of output
   *
   * @param line The line of output to process
   */
   def processLine(line:String):Unit

   /**
   * Called by <code>ProcessRunner</code> when all output to process
   * is finished
   */
   def finish:Unit = {}

   /**
   * Return an error message for the handler
   */
   def getErrorMessage:String = ""

   /**
   * Return a count of errors for this handler
   */
   def getErrorCount:Int = 0

}
