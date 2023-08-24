// generated with ast extension for cup
// version 0.8
// 24/5/2023 11:26:8


package rs.ac.bg.etf.pp1.ast;

public interface Visitor { 

    public void visit(Designator Designator);
    public void visit(ConstListUnit ConstListUnit);
    public void visit(Factor Factor);
    public void visit(Mulop Mulop);
    public void visit(DesignatorStatement DesignatorStatement);
    public void visit(Declaration Declaration);
    public void visit(VarList VarList);
    public void visit(ReturnType ReturnType);
    public void visit(Declarations Declarations);
    public void visit(Expr Expr);
    public void visit(FormPars FormPars);
    public void visit(Array Array);
    public void visit(MethVarDecl MethVarDecl);
    public void visit(Addop Addop);
    public void visit(Statement Statement);
    public void visit(FormParsList FormParsList);
    public void visit(ConstVal ConstVal);
    public void visit(ConstList ConstList);
    public void visit(Term Term);
    public void visit(StatementList StatementList);
    public void visit(BoolConst BoolConst);
    public void visit(CharConst CharConst);
    public void visit(NumConst NumConst);
    public void visit(ConstBool ConstBool);
    public void visit(ConstChar ConstChar);
    public void visit(ConstInt ConstInt);
    public void visit(Type Type);
    public void visit(NotArray NotArray);
    public void visit(Arr Arr);
    public void visit(Matrix Matrix);
    public void visit(ArrayName ArrayName);
    public void visit(DataDesignator DataDesignator);
    public void visit(ArrayDesignator ArrayDesignator);
    public void visit(MatrixDesignator MatrixDesignator);
    public void visit(BoolFactor BoolFactor);
    public void visit(CharFactor CharFactor);
    public void visit(NumFactor NumFactor);
    public void visit(NewMatrix NewMatrix);
    public void visit(NewArray NewArray);
    public void visit(ExprFactor ExprFactor);
    public void visit(DesignatorFactor DesignatorFactor);
    public void visit(ModOp ModOp);
    public void visit(DivOp DivOp);
    public void visit(MultOp MultOp);
    public void visit(FactorTerm FactorTerm);
    public void visit(TermList TermList);
    public void visit(MinusOp MinusOp);
    public void visit(PlusOp PlusOp);
    public void visit(TermExpr TermExpr);
    public void visit(NegationExpr NegationExpr);
    public void visit(ExprList ExprList);
    public void visit(DecrDeSt DecrDeSt);
    public void visit(IncrDeSt IncrDeSt);
    public void visit(AssignDeStError AssignDeStError);
    public void visit(AssignDeSt AssignDeSt);
    public void visit(StmtBlock StmtBlock);
    public void visit(PrintNumStmt PrintNumStmt);
    public void visit(PrintStmt PrintStmt);
    public void visit(ReadStmt ReadStmt);
    public void visit(RetStmt RetStmt);
    public void visit(RetExprStmt RetExprStmt);
    public void visit(DesigStmt DesigStmt);
    public void visit(NoStatement NoStatement);
    public void visit(Statements Statements);
    public void visit(VarListUnit VarListUnit);
    public void visit(VarListElem VarListElem);
    public void visit(VarsList VarsList);
    public void visit(VarDecl VarDecl);
    public void visit(NoMethVars NoMethVars);
    public void visit(MethVars MethVars);
    public void visit(ConstUnitError ConstUnitError);
    public void visit(SingleConstDecl SingleConstDecl);
    public void visit(ConstListElem ConstListElem);
    public void visit(Constants Constants);
    public void visit(ConstDecl ConstDecl);
    public void visit(VarDeclarations VarDeclarations);
    public void visit(ConstDeclarations ConstDeclarations);
    public void visit(NoDeclaration NoDeclaration);
    public void visit(DeclarationList DeclarationList);
    public void visit(FormParamUnit FormParamUnit);
    public void visit(FormParamElem FormParamElem);
    public void visit(FormParamList FormParamList);
    public void visit(NoFormPars NoFormPars);
    public void visit(FormParams FormParams);
    public void visit(MethName MethName);
    public void visit(VoidReturnType VoidReturnType);
    public void visit(ConcreteReturnType ConcreteReturnType);
    public void visit(MethodDecl MethodDecl);
    public void visit(ProgName ProgName);
    public void visit(Program Program);

}
