
package rs.ac.bg.etf.pp1;

import java.io.*;

import java_cup.runtime.Symbol;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import rs.ac.bg.etf.pp1.ast.Program;
import rs.ac.bg.etf.pp1.util.Log4JUtils;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;
import java.util.Timer;

public class Compiler {

	static {
		DOMConfigurator.configure(Log4JUtils.instance().findLoggerConfigFile());
		Log4JUtils.instance().prepareLogFile(Logger.getRootLogger());
	}

	
	public static void tsdump() {
		Tab.dump();
	}
	
	public static void main(String[] args) throws Exception {

		Logger log = Logger.getLogger(Compiler.class);

		Reader bufRd = null;
		long start = System.currentTimeMillis();
		
		try {
			String pathname = "test/test301.mj";
			File srcFile = new File(pathname);
			if(!srcFile.exists()) {
				log.error("Source file [" + srcFile.getAbsolutePath() + "] not found!");
				return;
			}
			log.info("Izabrani fajl: " + srcFile.getAbsolutePath());
			bufRd = new BufferedReader(new FileReader(srcFile));
			
			log.info("[[~~~=============================~~~MJ COMPILER STARTING~~~=============================~~~]]");

			
			Yylex lexer = new Yylex(bufRd);

			log.info("{{================================][   Leksicka analiza   ][================================}}");
			
			MJParser parser = new MJParser(lexer);
			Symbol symbol = null;
			
			symbol = parser.parse();

			log.info("{{================================][   Sintaksna analiza   ][===============================}}");
			Program program =null;
			try {
				program = (Program)(symbol.value);
			}catch(ClassCastException e) {
				log.fatal("Parser ne moze nastaviti sa radom zbog prethodnih gresaka. Ispravite program pa pokusajte opet.");
				return;
			};
			
			Tab.init();
			Tab.insert(Obj.Type, "bool", new Struct(Struct.Bool));
			Tab.insert(Obj.Type, "void", Tab.noType);
			
			log.info(program.toString(""));

			log.info("{{===============================][   Semanticka Analiza   ][===============================}}");
			SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer();
			program.traverseBottomUp(semanticAnalyzer);

			Tab.dump();

			log.info("{{======================================][   Kraj   ][======================================}}");
			if (parser.errorDetected) {
				log.fatal("Program nije uspesno kompajlovan! Ispravite greske i probajte ponovo");
			} else {
				String destPath = "test/program.obj";
			    File objFile = new File(destPath);

			    if (objFile.exists()) {
			        objFile.delete();
                }

			    CodeGenerator codeGen = new CodeGenerator();
			    program.traverseBottomUp(codeGen);
			    
			    
                Code.dataSize = semanticAnalyzer.gVarDeclarations;
                Code.dataSize += semanticAnalyzer.constDeclarations;
                Code.dataSize += semanticAnalyzer.lVarDeclarations;
                Code.mainPc = codeGen.getMainPc();
                
                Code.write(new FileOutputStream(objFile));
                log.info("Uspesno kompajlovan program. Mozete ga naci u "+destPath);
                
            }
		}
		finally {
			if (bufRd != null) try { bufRd.close(); } catch (IOException e1) { log.error(e1.getMessage(), e1); }
			long stop = System.currentTimeMillis();
			log.info("Izvrsavanje kompajlera je trajalo: "+(stop-start)+" ms.");
		}

	}


}
