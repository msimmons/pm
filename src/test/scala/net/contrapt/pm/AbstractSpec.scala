package net.contrapt.pm

import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import java.io.File

@RunWith(classOf[JUnitRunner])
abstract class AbstractSpec extends FlatSpec with ShouldMatchers {

  val BASE_DIR_PROPERTY = "test.classes.dir"
  
  val baseDir = {
    System.getProperty(BASE_DIR_PROPERTY) match {
      case null => throw new IllegalStateException("You must provide the property: "+BASE_DIR_PROPERTY) 
      case dir => new File(dir)
    }
  }

}
