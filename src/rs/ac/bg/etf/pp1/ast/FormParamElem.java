// generated with ast extension for cup
// version 0.8
// 24/5/2023 11:26:8


package rs.ac.bg.etf.pp1.ast;

public class FormParamElem extends FormParsList {

    private FormParamUnit FormParamUnit;

    public FormParamElem (FormParamUnit FormParamUnit) {
        this.FormParamUnit=FormParamUnit;
        if(FormParamUnit!=null) FormParamUnit.setParent(this);
    }

    public FormParamUnit getFormParamUnit() {
        return FormParamUnit;
    }

    public void setFormParamUnit(FormParamUnit FormParamUnit) {
        this.FormParamUnit=FormParamUnit;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(FormParamUnit!=null) FormParamUnit.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(FormParamUnit!=null) FormParamUnit.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(FormParamUnit!=null) FormParamUnit.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FormParamElem(\n");

        if(FormParamUnit!=null)
            buffer.append(FormParamUnit.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FormParamElem]");
        return buffer.toString();
    }
}
