package com.shengli.etl.ff.fight

import com.shengli.etl.ff.vfs.PlainFile
import com.shengli.etl.ff.mapping.LogRulesMapping
import com.shengli.etl.ff.vfs.PlainFileWriter

object BeginFight extends App{
	val file =  new PlainFile()
	val lines = file.readLines("""E:\akka_try\item_log.txt""")
	synchronized {
		for ( (game,mp) <- LogRulesMapping.mappings ) {
		      for( (log_name, rule) <- mp ){
		         new Thread(new PlainFileWriter(log_name,"E:\\"+log_name+".txt")).start()
		      }
		}
	}
	
}