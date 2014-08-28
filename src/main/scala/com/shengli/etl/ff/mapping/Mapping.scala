package com.shengli.etl.ff.mapping

import scala.collection.mutable.HashMap

/** 
* @ClassName: Mapping 
* @Description: Provide a mapping for user to use
* @author shengli.victor 盛利
* @date 2014年8月25日 下午4:38:04  
*/ 
abstract class Mapping {
    def get(key : String) : String
    def get(game_name : String, log_name : String) : String
}


/** 
* @ClassName: AppMapping 
* @Description: App Mapping is for mapping different logs from input lines, eg: [app_name,[log_name, log_rules]] -> [ff14,[level_up_log,"Level_up\\d+"]]
* @author shengli.victor 盛利
* @date 2014年8月25日 下午4:38:21  
*/ 
object LogRulesMapping extends Mapping {
  
   //mock mapping relations
   val mappings = new HashMap[String, HashMap[String, String]]()
   private val game1 = "ff14"
   private val ff14Mapping = new HashMap[String, String]()
   ff14Mapping += ("""[gsys]ItemGet,"""->"ff14_item_get_log")
   ff14Mapping += ("""[gsys]SetStack,"""->"ff14_stack_log")
   ff14Mapping += ("""[evnt]CraftLeveNpcTrade"""->"ff14_craft_leve_npc_log")
   
   mappings += (game1 -> ff14Mapping)
   
   
   def get(key : String) : String = {
     throw new RuntimeException("not supported yet.")
   }
   
   //get rules from the gieven game_name and log_name
   def get(game_name : String, rule_name : String) : String = {
	   val log_name = mappings.getOrElse(game_name, new HashMap()).getOrElse(rule_name, "")  
	   log_name.toString()
   }
}