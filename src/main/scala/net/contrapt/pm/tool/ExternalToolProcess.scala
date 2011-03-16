package net.contrapt.pm.tool

import java.io._

/**
* This class implements a tool running as an external process writing its output to 
* a temporary file
*/
class ExternalToolProcess(name:String, config:ToolConfig, handler:OutputHandler) extends ToolProcess(name, config, handler) {

	val outputFile = File.createTempFile(name+"."+config.name+".", null)
	private var output:BufferedWriter = null
	private var process:Process = null
	private var exception:Throwable = null

	var prefix = List("/bin/sh", "-c")

	/** true for Windows, false for Unix */
	var splitArgs = false

	/** Is the process finished? */
	var finished = false
  
	/**
	* Run the external process
	*/
	def run = {
		if ( openOutput ) {
			if ( startProcess ) monitorProcess
		}
	}

	/**
	* Get the exception encountered while running, if any
	*/
	def getException = exception

	/**
	* Open the temporary output file; return true if successful, false otherwise
	*/
	private def openOutput:Boolean = {
		try {
			outputFile.deleteOnExit
			output = new BufferedWriter(new FileWriter(outputFile))
			true
		}
		catch {
			case e:Throwable => exception = new IllegalStateException("Error opening output file", e)
			false
		}
	}

	/**
	* Attempt to start the external process; return true if successful, false otherwise
	*/
	private def startProcess = {
		val builder = new ProcessBuilder(command2args(command))
		builder.redirectErrorStream(true)
		//builder.directory(tool.directory)
		env.keySet.foreach(k => builder.environment.put(k, env(k)))
		try {
			process = builder.start
			true
		}
		catch {
			case e:Throwable => setException("Error starting process", e)
			false
		}
	}

	/**
	* Monitor the running process by processing each line of output
	*/
	private def monitorProcess = {
		val in = new BufferedReader(new InputStreamReader(process.getInputStream))
		try {
			if ( handler != null ) handler.initialize
			var line = in.readLine
			while ( line != null ) {
				output.write(line)
				output.newLine
				processLine(line)
				if ( handler != null ) handler.processLine(line)
				//if ( killed ) break;
				line = in.readLine
			}
			//processFinished(StateEnum.WAITING);
			val status = process.waitFor
		}
		catch {
			case e:Throwable => setException("Error processing command output", e)
			//bufferChanged = true;
		}
		finally {
			if ( handler != null ) handler.finish
			in.close
			output.close
			finished = true
			//Log.log(Log.DEBUG, this, "Finished running process:\n "+description);
			//processFinished(StateEnum.DONE);
		}
	}

	/**
	* Override this method to do any custom processing.  This will be called for 
	* each line read from output
	*/
	protected def processLine(line:String):Unit = Nil

	/**
	* Record an exception and append exception info to the output file
	*/
	private def setException(msg:String, t:Throwable) = {
		exception = t
		output.write(msg+": "+t)
		t.getStackTrace.foreach(el => {output.newLine; output.write("   "+el)})
	}

	/**
	* Split a command into a list of arguments 
	*/
	val ARGS_RE = "\"[^\"]*\"|'[^']*'|[^\"'\\s]+".r
	def command2args(command:String):java.util.List[String] = {
		val args = new java.util.ArrayList[String]()
		prefix.foreach(p => args.add(p))
		if ( splitArgs) ARGS_RE.findAllIn(command).foreach(m => args.add(m))
		else args.add(command)
		println(args)
		args
	}

	override def toString = getClass.getSimpleName+"["+command+","+env+","+outputFile+","+exception+"]"
 
}
