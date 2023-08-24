package rs.ac.bg.etf.pp1;

import rs.ac.bg.etf.pp1.ast.FormParamUnit;
import rs.ac.bg.etf.pp1.ast.VarListUnit;
import rs.ac.bg.etf.pp1.ast.VisitorAdaptor;

public class CounterVisitor extends VisitorAdaptor {
	protected int count;
	public int getCount(){
		return count;
	}
	
	public void setCount(int count) {
		this.count = count;
	}
	
	public static class FormParamCounter extends CounterVisitor{
		
		public void visit(FormParamUnit formParamDecl) {
			count++;
		}
	}
	
	public static class VarCounter extends CounterVisitor{
		public void visit(VarListUnit varDecl) {
			count++;
		}
	}
	
}
