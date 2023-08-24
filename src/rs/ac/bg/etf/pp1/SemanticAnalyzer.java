package rs.ac.bg.etf.pp1;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.ast.*;
import rs.ac.bg.etf.pp1.ast.VisitorAdaptor;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;
import rs.etf.pp1.symboltable.Tab;

public class SemanticAnalyzer extends VisitorAdaptor{
	int gVarDeclarations = 0;
	int lVarDeclarations = 0;
	int constDeclarations = 0;
	int nVars;
	int paramCount=0;
	int printCount = 0;
	
	boolean errorDetected = false;
	boolean isGlobalScope = true;
	
	Struct boolType = new Struct(Struct.Bool);
	
	


	Logger log = Logger.getLogger(getClass());
	
	Struct currType = Tab.noType;
	Obj currMtd = null;
	private int readCallCount;
	private boolean hasReturn;
	Struct returnType = Tab.noType;
	
	public boolean passed(){
    	return !errorDetected;
	}
	
	public void report_error(String message, SyntaxNode info) {
		errorDetected = true;
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0: info.getLine();
		if (line != 0)
			msg.append (" na liniji ").append(line);
		log.error(msg.toString());
	}

	public void report_info(String message, SyntaxNode info) {
		StringBuilder msg = new StringBuilder(message); 
		int line = (info == null) ? 0: info.getLine();
		if (line != 0)
			msg.append (" na liniji ").append(line);
		log.info(msg.toString());
	}
	
	
	
	
	public void visit(Program program) {
		nVars = Tab.currentScope().getnVars();
		Tab.chainLocalSymbols(program.getProgName().obj);
		Tab.closeScope();
	}
	
	public void visit(ProgName progName) {
		progName.obj = Tab.insert(Obj.Prog, progName.getProgName(), Tab.noType);
		hasReturn = false;
		Tab.openScope();
	}
	
	public void visit(Type type) {
		Obj lookup =  Tab.find(type.getTypeName());
		if(lookup == Tab.noObj) {
			this.report_error("Ne postoji tip "+ type.getTypeName()+ " u tabeli simbola.", type);
			type.struct = Tab.noType;
			currType = Tab.noType;
			
		}else {
			if(lookup.getKind() != Obj.Type) {
				type.struct = Tab.noType;
				report_error("Nevazeci tip "+ type.getTypeName(), type);
				currType = Tab.noType;
			}else {
				type.struct = lookup.getType();
				currType = lookup.getType();
			}
		}
	}
	
	public void visit(VoidReturnType voidRet) {
		hasReturn = false;
	}
	public void visit(ConcreteReturnType retType) {
		hasReturn = true;
	}
	
	public void visit(VarListUnit varUnit) {
		if(Tab.currentScope().findSymbol(varUnit.getName()) == null) {
			if(currType != Tab.noType) {
				String varType;
				if(isGlobalScope) varType="globalna promenljiva"; else varType="lokalna promenljiva";
				if(varUnit.getArray() instanceof Matrix) {
					//matrix Table maybe works idfk 
					Tab.insert(Obj.Var,varUnit.getName(),new Struct(Struct.Array, new Struct(Struct.Array, currType)));
					report_info("Deklarisana "+varType+" - matrica "+varUnit.getName(), varUnit);
					
				}else if (varUnit.getArray() instanceof Arr) {
					Tab.insert(Obj.Var,varUnit.getName(),new Struct(Struct.Array, currType));
					report_info("Deklarisana "+varType+" - niz "+varUnit.getName(), varUnit);
				}else if(varUnit.getArray() instanceof NotArray){
					Tab.insert(Obj.Var, varUnit.getName(), currType);
					report_info("Deklarisana "+varType+" "+varUnit.getName(), varUnit);
				}
				if(isGlobalScope) gVarDeclarations++; else lVarDeclarations++;
				return;
			}
			report_error("Tip nemam pojma sta radi lmao "+ varUnit.getName(), varUnit);
			return;
		}
		report_error("Ime "+ varUnit.getName() + " je vec dekliarisano.", varUnit);
	}
	
	public void visit(SingleConstDecl constant) {
		Obj lookup = Tab.find(constant.getConstName());
		if(lookup== Tab.noObj) {
			if(constant.getConstVal().obj.getType() == currType) {
				constant.obj = Tab.insert(Obj.Con, constant.getConstName(), currType);
				constant.obj.setAdr(constant.getConstVal().obj.getAdr());
				report_info("Konstanta "+constant.getConstName()+" deklarisana", constant);
				constDeclarations++;
				return;
			}
			report_error("Tip konstante "+constant.getConstName()+" se ne poklapa sa vrednoscu.", constant);
			return;
		}
		report_error("Ime "+constant.getConstName()+" je vec deklarisano", constant);
		return;
	}
	
	public void visit(ConstBool constBool) {
		constBool.obj = new Obj(Obj.Con, "whatever", constBool.getBoolConst().struct);
		if(constBool.getBoolConst().getConstValue()) {
			constBool.obj.setAdr(1);
		}else {
			constBool.obj.setAdr(0);
		}
	}
	
	public void visit(ConstChar constChar) {
		constChar.obj = new Obj(Obj.Con, "whatever", constChar.getCharConst().struct);
		constChar.obj.setAdr(constChar.getCharConst().getConstValue());
		System.out.println(constChar.getCharConst().getConstValue());
		
	}
	
	public void visit(ConstInt numConst) {
		numConst.obj = new Obj(Obj.Con, "doesnt matter", numConst.getNumConst().struct);
		numConst.obj.setAdr(numConst.getNumConst().getConstValue());
	}
	
	
	
	public void visit(MethodDecl method) {
		Tab.chainLocalSymbols(currMtd);
		Tab.closeScope();
		isGlobalScope=true;
	}
	
	
	public void visit(MethName mtdName) {
		currMtd = Tab.insert(Obj.Meth, mtdName.getMethName(), currType);
		mtdName.obj = currMtd;
		returnType = currType;
		isGlobalScope=false;
		Tab.openScope();
	}
	
	//Designator
	
	public void visit(DataDesignator designator) {
		Obj desig = Tab.find(designator.getDesigName());
		if(desig!= Tab.noObj) {
			report_info("Objekat "+designator.getDesigName()+" je iskoriscen", designator);
			designator.obj = desig;
			return;
		}
		report_error("Ime "+designator.getDesigName()+ " nije deklarisano.",designator);
	}
	
	public void visit(ArrayDesignator designator) {
		Obj desig = Tab.find(designator.getArrayName().getArrayName());
		if(desig!= Tab.noObj) {
			if(desig.getType().getKind() != Struct.Array) {
				report_error(designator.getArrayName().getArrayName() + " nije niz.", designator);
				return;
			}
			if(designator.getExpr().struct != Tab.intType) {
				report_error("Niz nije indeksiran celobrojnim vrednostima", designator);
				return;
			}
			designator.obj = new Obj(Obj.Elem, desig.getName(), desig.getType().getElemType());
			return;
		}
		report_error("Ime "+designator.getArrayName().getArrayName()+ " nije deklarisano.",designator);
	}
	
	public void visit(MatrixDesignator designator) {
		Obj desig = Tab.find(designator.getArrayName().getArrayName());
		if(desig!=Tab.noObj) {
			if(desig.getType().getElemType().getKind() != Struct.Array) {
				report_error(designator.getArrayName().getArrayName() + " nije matrica.", designator);
				return;
			}
			if(designator.getExpr().struct != Tab.intType || designator.getExpr1().struct != Tab.intType) {
				report_error("Matrica nije indeksirana celobrojnim vrednostima", designator);
				return;
			}
			designator.obj = new Obj(Obj.Elem, desig.getName(), desig.getType().getElemType().getElemType());
			return;
		}
		report_error("Ime "+designator.getArrayName().getArrayName()+ " nije deklarisano.",designator);
	}
	

	public void visit(ArrayName arrName) {
		arrName.obj = Tab.find(arrName.getArrayName());
	}
	
	//Print
	
	public void visit(PrintStmt print) {
		Struct dataType = print.getExpr().struct;
		if(dataType != Tab.intType && dataType != Tab.charType && dataType != boolType) {
			report_error("Nedozvoljen podatak za print. Ocekivan int, char ili bool.", print);
			return;
		}
		printCount++;
	}
	
	public void visit(PrintNumStmt print) {
		Struct dataType = print.getExpr().struct;
		if(dataType != Tab.intType && dataType != Tab.charType && dataType != boolType) {
			report_error("Nedozvoljen podatak za print. Ocekivan int, char ili bool.", print);
			return;
		}
		printCount++;
	}
	
	
	public void visit(IncrDeSt incrStmt) {
		if(incrStmt.getDesignator().obj == null) {
			report_error("Podatak koji se inkrementira nije promenljiva ili je nedefinisan", incrStmt);
			return;
		}
		if(incrStmt.getDesignator().obj.getType() != Tab.intType) {
			report_error("Podatak koji se inkrementira mora biti int.", incrStmt);
		}
	}
	
	public void visit(DecrDeSt decrStmt) {
		if(decrStmt.getDesignator().obj == null) {
			report_error("Podatak koji se dekrementira nije promenljiva ili je nedefinisan", decrStmt);
			return;
		}
		if(decrStmt.getDesignator().obj.getType() != Tab.intType) {
			report_error("Podatak koji se dekrementira mora biti int.", decrStmt);
		}
	}
	
	//Values
	
	public void visit(NumConst number) {
		number.struct = Tab.intType;
	}
	
	public void visit(CharConst character) {
		character.struct = Tab.charType;
	}
	
	public void visit(BoolConst bool) {
		bool.struct = boolType;
	}
	
	
	//Factor
	
	public void visit(NumFactor factor) {
		factor.struct = factor.getNumConst().struct;
	}
	
	public void visit (CharFactor factor) {
		factor.struct = factor.getCharConst().struct;
	}
	
	public void visit(BoolFactor factor) {
		factor.struct = factor.getBoolConst().struct;
	}
	
	public void visit(ExprFactor factor) {
		factor.struct = factor.getExpr().struct;
	}
	
	public void visit(NewArray newArray) {
		if(newArray.getExpr().struct != Tab.intType) {
			report_error("Velicina niza mora biti tipa int.", newArray);
		}
		newArray.struct=new Struct(Struct.Array, currType);
	}
	
	public void visit(NewMatrix newMatr) {
		if(newMatr.getExpr1().struct != Tab.intType || newMatr.getExpr().struct != Tab.intType) {
			report_error("Velicina niza mora biti tipa int.", newMatr);
		}
		newMatr.struct=new Struct(Struct.Array, new Struct(Struct.Array, currType));
	}
	
	public void visit(DesignatorFactor factor) {
		if(factor.getDesignator().obj == null) {
			report_error("Zadato ime nije validno kao promenljiva", factor);
			return;
		}
		factor.struct = factor.getDesignator().obj.getType();
	}
	
	//Term
	
	public void visit(FactorTerm factor) {
		factor.struct = factor.getFactor().struct;
		
	}
	
	public void visit(TermList terms) {
		Struct f1 = terms.getFactor().struct;
		Struct f2 = terms.getTerm().struct;
		if(f1!=Tab.intType || f2 != Tab.intType) {
			report_error("Matematicki izraz mora koristiti tipove int", terms);
		}else {
			terms.struct=terms.getFactor().struct;
		}
	}
	
	//Expr
	
	public void visit(TermExpr expr) {
		expr.struct=expr.getTerm().struct;
	}
	
	public void visit(NegationExpr expr) {
		if(expr.getTerm().struct.getKind()!=Struct.Int) {
			report_error("Negacija ocekuje jedino tip int",expr);
			return;
		}
		expr.struct=expr.getTerm().struct;
	}
	
	public void visit(ExprList terms) {
		Struct term1 = terms.getTerm().struct;
		Struct term2 =terms.getExpr().struct;
		if(term1!=Tab.intType || term2 != Tab.intType) {
			report_error("Izraz mora koristiti tipove int.",terms);
		}
		terms.struct=terms.getTerm().struct;
	}
	
	//Designator Statements
	
	public void visit(AssignDeSt assignment) {
		if(assignment.getDesignator().obj == null) {
			report_error("Zadato ime mije validno kao promenljiva za dodelu", assignment);
			return;
		}if(assignment.getExpr().struct == null) {
			report_error("Izraz koji se dodeljuje nije validan", assignment);
			return;
		}
		
		Struct desigType = assignment.getDesignator().obj.getType();
		
		if(assignment.getExpr().struct.assignableTo(desigType)) {
			//all good :ok_emoji:
		}else {
			report_error("Ne poklapaju se tipovi za dodelu", assignment);
		}
	}
	
	public void visit(RetStmt emptyReturn) {
		if(hasReturn==true) {
			report_error("Funkcija mora imati povratnu vrednost.", emptyReturn);
		}
	}
	
	public void visit(RetExprStmt retVal) {
		if(retVal.getExpr().struct != returnType) {
			report_error("Ne poklapa se povratna vrednost return iskaza sa povratnom vrednoscu funkcije", retVal);
		}
	}
	
	public void visit(ReadStmt readStmt) {
		if(readStmt.getDesignator().obj == null) {
			report_error("Podatak koji se inkrementira ili nije promenljiva ili je nedefinisan", readStmt);
			return;
		}
		if(readStmt.getDesignator().obj == Tab.noObj) {
			report_error(readStmt.getDesignator().obj.getName()+" nije prethodno deklarisan", readStmt);
			return;
		}
		int desigKind = readStmt.getDesignator().obj.getKind();
		if(desigKind == Obj.Con || desigKind == Obj.Meth || desigKind == Obj.Prog || desigKind == Obj.Type) {
			report_error(readStmt.getDesignator().obj.getName()+" nije promenljiva vrednost", readStmt);
			return;
		}
		Struct desigType = readStmt.getDesignator().obj.getType();
		if(desigType!= Tab.charType && desigType != Tab.intType && desigType != boolType) {
			report_error(readStmt.getDesignator().obj.getName()+" nije odgovarajuci tip", readStmt);
			return;
		}
	}
	
	public void visit(FormParamUnit parameter) {
		if(Tab.currentScope().findSymbol(parameter.getFormParamName()) == null) {
			if(currType != Tab.noType) {
				if(parameter.getArray() instanceof Matrix) {
					//matrix Table maybe works idfk 
					Tab.insert(Obj.Var,parameter.getFormParamName(),new Struct(Struct.Array, new Struct(Struct.Array, currType)));
					report_info("Deklarisan parametar - matrica "+parameter.getFormParamName(), parameter);
					
				}else if (parameter.getArray() instanceof Arr) {
					Tab.insert(Obj.Var,parameter.getFormParamName(),new Struct(Struct.Array, currType));
					report_info("Deklarisan formalni parametar - niz "+parameter.getFormParamName(), parameter);
				}else if(parameter.getArray() instanceof NotArray){
					Tab.insert(Obj.Var, parameter.getFormParamName(), currType);
					report_info("Deklarisan parametar "+parameter.getFormParamName(), parameter);
				}
				paramCount++;
				return;
			}
			report_error("Tip nemam pojma sta radi lmao "+ parameter.getFormParamName(), parameter);
			return;
		}
		report_error("Ime "+ parameter.getFormParamName() + " je vec dekliarisano.", parameter);
	}
	
	
	
}
