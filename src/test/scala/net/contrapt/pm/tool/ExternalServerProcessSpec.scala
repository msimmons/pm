package net.contrapt.pm.tool

import java.io.File
import net.contrapt.pm.AbstractSpec

class ExternalServerProcessSpec extends AbstractSpec {

	"An ExternalServerProcess" should "know when it is started" in {
		val pomFile = new File(baseDir, "project1/pom.xml")
		val cmd = "mvn jetty:run -f "+pomFile
		val stopCmd = "mvn jetty:stop -f "+pomFile
		object t extends ExternalServerConfig("jetty", cmd, stopCmd, Map(), ".*Started Jetty Server".r, ".*Jetty server exiting".r)
		val s = t.getProcess("jetty", null)
		s.run
		var i=0;
		while (!s.isStarted && !s.finished) {
			Thread.sleep(100)
		}
		println("started:"+s.isStarted)
		println("finished:"+s.finished)
  }

}
