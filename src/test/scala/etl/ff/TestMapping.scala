package etl.ff

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FunSpec
import com.shengli.etl.ff.vfs.PlainFile
import com.shengli.etl.ff.log.Logging
import scala.collection.mutable.HashMap
import com.shengli.etl.ff.mapping.LogRulesMapping

object ConsoleTest extends App {
}

class TestMapping extends FunSpec with ShouldMatchers with Logging {
  
    describe("test can get correct game log ? "){
      it("test get ff14_item_get_log rules......"){
        val game_name  = "ff14"
        val log_name = "[gsys]ItemGet,"
        val rules = LogRulesMapping.get(game_name,log_name)
        rules should be("ff14_item_get_log")
      }
      
       it("test get non-exists rules......"){
        val game_name  = "ff14"
        val log_name = ""
        val rules = LogRulesMapping.get(game_name,log_name)
        rules should be("ff14_leve_up")
      }
    }
}