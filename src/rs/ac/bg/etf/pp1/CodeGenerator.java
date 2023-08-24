package rs.ac.bg.etf.pp1;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.DefaultThrowableRenderer;

import rs.ac.bg.etf.pp1.CounterVisitor.FormParamCounter;
import rs.ac.bg.etf.pp1.CounterVisitor.VarCounter;
import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

public class CodeGenerator extends VisitorAdaptor {
	
	
	private int mainPc;
	Struct boolType = new Struct(Struct.Bool);
	
	private boolean newMatrixDeclaration;
	private Struct newMatrDataType;
	
	public int getMainPc() {
		return mainPc;
	}
	
	public void visit(Program prog) {
		Code.put(Code.exit);
		Code.put(Code.return_);
		return;
	}
	
	public void visit(RetStmt ret) {
		Code.put(Code.exit);
		Code.put(Code.return_);
	}
	
	public void visit(RetExprStmt retEx) {
		Code.put(Code.exit);
		Code.put(Code.return_);
	}
	
	public void visit(MethodDecl meth) {
		Code.put(Code.exit);
		Code.put(Code.return_);
		//4 puta za redom, sigurno je ok :)
	}
	
	public void visit() {
		
	}
	
	public void visit(PrintStmt print) {
		if(print.getExpr().struct != Tab.charType) {
			Code.loadConst(5);
			Code.put(Code.print);
		}else {
			Code.loadConst(1);
			Code.put(Code.bprint);
		}
	}
	
	public void visit(PrintNumStmt print) {
		if(print.getExpr().struct != Tab.charType) {
			Code.put(Code.print);
		}else {
			Code.put(Code.bprint);
		}
		
	}
	
	
	public void visit(NegationExpr neg) {
		Code.put(Code.neg);
	}
	
	public void visit (NumConst number) {
		Obj obj = Tab.insert(Obj.Con, "$", number.struct);
		obj.setAdr(number.getConstValue());
		obj.setLevel(0);
		if (number.getParent() instanceof NumFactor) Code.load(obj);
	}
	
	public void visit(CharConst character) {
		Obj obj = Tab.insert(Obj.Con, "$", character.struct);
		obj.setAdr(character.getConstValue());
		obj.setLevel(0);
		if(character.getParent() instanceof CharFactor)Code.load(obj);
	}
	
	public void visit(BoolConst bool) {
		Obj obj = Tab.insert(Obj.Con, "$", boolType);
		if(bool.getConstValue()) {
			obj.setAdr(1);
		}else {
			obj.setAdr(0);
		}
		obj.setLevel(0);
		if(bool.getParent() instanceof BoolFactor) Code.load(obj);
	}
	
	
	
	public void visit(TermList expr) {
		if(expr.getMulop() instanceof ModOp) {
			Code.put(Code.rem);
		}else if(expr.getMulop() instanceof MultOp) {
			Code.put(Code.mul);
		}else if(expr.getMulop() instanceof DivOp) {
			Code.put(Code.div);
		}
	}
	
	public void visit(ExprList expr) {
		if(expr.getAddop() instanceof MinusOp) {
			Code.put(Code.sub);
		}else if(expr.getAddop() instanceof PlusOp) {
			Code.put(Code.add);
		}
	}
	
	public void visit(DataDesignator designator) {
		if(!(designator.getParent() instanceof AssignDeSt || designator.getParent() instanceof ReadStmt)) {
			Code.load(designator.obj);
		}
	}
	
	public void visit(ArrayDesignator designator) {
		//pocetno stanje na ExprStack: e1
		//ciljno stanje: adr,e1 za aload; adr,e1,adr,e1 za aload i astore;
		if(designator.getArrayName().obj == Tab.noObj) {
			System.out.println("wtf");
			//Compiler.tsdump();
		}
		
		Code.load(designator.getArrayName().obj);
		Code.put(Code.dup_x1);
		Code.put(Code.pop);
		//adr,e1
		if(designator.getParent() instanceof IncrDeSt || designator.getParent() instanceof DecrDeSt) {
			Code.put(Code.dup2);
			//adr,e1,adr,e1
		}else if(designator.getParent() instanceof AssignDeSt || designator.getParent() instanceof ReadStmt) return;
		Struct dataType = designator.obj.getType();
		if(dataType != Tab.charType) {
			Code.put(Code.aload);
		}else {
			Code.put(Code.baload);
		}
		
	}
	
	public void visit(ReadStmt read) {
		if(read.getDesignator().obj.getType()== Tab.charType) {
			Code.put(Code.bread);
		}else {
			Code.put(Code.read);
		}
		if(read.getDesignator() instanceof DataDesignator) {
			Code.store(read.getDesignator().obj);
		}else {
			if(read.getDesignator().obj.getType()== Tab.charType) {
				Code.put(Code.bastore);
			}else {
				Code.put(Code.astore);
			}
		}
	}
	
	public void visit(MatrixDesignator designator) {
		//pocetno stanje na ExprStack: e1,e2
		//ciljno stanje: arrayAdr,e2,mAdr,e1 za dvostruki aload.
		//za astore arrayadr,e2,arrayadr,e2,mAdr,e1;
		Code.put(Code.dup_x1);
		Code.put(Code.pop);
		//e2,e1
		Code.load(designator.getArrayName().obj);
		Code.put(Code.dup_x1);
		Code.put(Code.pop);
		//e2,mAdr,e1
		Code.put(Code.aload);
		Code.put(Code.dup_x1);
		Code.put(Code.pop);
		//nista lepse nego kad jednu te istu stvar sedam puta stavljam
		//arrayAdr,e2;
		if( designator.getParent() instanceof IncrDeSt || designator.getParent() instanceof DecrDeSt) {
			Code.put(Code.dup2);
			//arrayadr,e2,arrayadr,e2
		}else if (designator.getParent() instanceof AssignDeSt || designator.getParent() instanceof ReadStmt) return;
		Struct dataType = designator.obj.getType();
		if(dataType != Tab.charType) {
			Code.put(Code.aload);
		}else {
			Code.put(Code.baload);
		}
	}
	
	
	public void visit(AssignDeSt assignment) {
		if(newMatrixDeclaration) {
			newMatrixDeclaration = false;
			//e1,e2
			Code.put(Code.dup_x1);
			Code.put(Code.pop);
			Code.put(Code.dup_x1);
			//e1,e2,e1
			Code.put(Code.newarray);
			Code.put(0);
			
			//e1,e2,mAdr
			//treba mi e1 kao brojac i e2 za svaki new array
			//ili pak arraylength
			Code.store(assignment.getDesignator().obj);
			//e1,e2
			Code.loadConst(0);
			//e1,e2,0
			int top = Code.pc;
			Code.put(Code.dup_x1);
			Code.put(Code.dup_x1);
			//e1,0,0,e2,0;
			Code.put(Code.pop);
			//e1,0,0,e2;
			Code.put(Code.dup_x2);
			//e1,e2,0,0,e2;
			
			Code.put(Code.newarray);
			if(newMatrDataType == Tab.charType) {
				Code.put(0);
			}else {
				Code.put(1);
			}
			//e1,e2,0,0,arrAdr
			Code.load(assignment.getDesignator().obj);
			Code.put(Code.dup_x2);
			Code.put(Code.pop);
			//e1,e2,0,madr,0,arradr
			Code.put(Code.astore);
			//e1,e2,0
			Code.loadConst(1);
			Code.put(Code.add);
			//e1,e2,1
			Code.put(Code.dup);
			Code.load(assignment.getDesignator().obj);
			Code.put(Code.arraylength);
			//e1,e2,1,1,mLen
			Code.putFalseJump(Code.ne, 0);
			int newAdr = Code.pc-2;
			
			//e1,e2,1
			Code.putJump(top);
			Code.fixup(newAdr);
			
			Code.put(Code.pop);
			Code.put(Code.pop);
			Code.put(Code.pop);
			//
			return;
			
		}
		Struct dataType = assignment.getDesignator().obj.getType();
		if(assignment.getDesignator() instanceof DataDesignator) {
			Code.store(assignment.getDesignator().obj);
		}else if(!(assignment.getDesignator() instanceof DataDesignator) && dataType != Tab.charType) {
			Code.put(Code.astore);
		}else if(!(assignment.getDesignator() instanceof DataDesignator) && dataType == Tab.charType) {
			Code.put(Code.bastore);
		}
	}
	
	public void visit(IncrDeSt increment) {
		Code.loadConst(1);
		Code.put(Code.add);
		if(increment.getDesignator() instanceof DataDesignator) {
			Code.store(increment.getDesignator().obj);
		}else {
			Code.put(Code.astore);
		}
	}
	
	public void visit(DecrDeSt decrement) {
		Code.loadConst(1);
		Code.put(Code.sub);
		if(decrement.getDesignator() instanceof DataDesignator) {
			Code.store(decrement.getDesignator().obj);
		}else {
			Code.put(Code.astore);
		}
	}
	
	public void visit(NewArray newArray) {
		Struct dataType = newArray.struct.getElemType();
		Code.put(Code.newarray);
		if(dataType == Tab.charType) {
			Code.put(0);
		}else {
			Code.put(1);
		}
	}
	
	public void visit(NewMatrix newMatr) {
		newMatrixDeclaration = true;
		
		newMatrDataType = newMatr.struct.getElemType().getElemType();
		/* stari pokusaj
		//e1,e2
		Code.put(Code.dup_x1);
		Code.put(Code.pop);
		Code.put(Code.dup_x1);
		//e1,e2,e1
		Code.put(Code.newarray);
		Code.put(0);
		//e1,e2,mAdr
		//treba mi e1 kao brojac i e2 za svaki new array
		//ili pak arraylength
		Code.put(Code.dup_x1);
		Code.put(Code.pop);
		//e1,mAdr,e2
		//treba mi kombinacija newarray i astore;
		//samim tim treba mi mAdr, index, e2
		//nakon te dve operacije ce mi i dalje trebati madr, index, e2 u nekom vidu.
		Code.put(Code.dup_x2);
		Code.put(Code.pop);
		Code.put(Code.dup_x2);
		Code.put(Code.pop);
		//mAdr,e2,e1
		Code.put(Code.dup_x2);
		//e1,mAdr,e2,e1
		adsgadags
		*/
		
		
	}
	
	public void visit(MethName methname) {
		if(methname.getMethName() == "main") {
			mainPc = Code.pc;
		}
		methname.obj.setAdr(Code.pc);
		
		VarCounter varCounter = new VarCounter();
		methname.getParent().traverseTopDown(varCounter);
		FormParamCounter formParamCounter = new FormParamCounter();
		methname.getParent().traverseTopDown(formParamCounter);
		
		Code.put(Code.enter);
		Code.put(formParamCounter.getCount());
		Code.put(varCounter.getCount() + formParamCounter.getCount());
		
	}
	
}
