// generated with ast extension for cup
// version 0.8
// 24/5/2023 11:26:8


package rs.ac.bg.etf.pp1.ast;

public class ConstListElem extends ConstList {

    private ConstListUnit ConstListUnit;

    public ConstListElem (ConstListUnit ConstListUnit) {
        this.ConstListUnit=ConstListUnit;
        if(ConstListUnit!=null) ConstListUnit.setParent(this);
    }

    public ConstListUnit getConstListUnit() {
        return ConstListUnit;
    }

    public void setConstListUnit(ConstListUnit ConstListUnit) {
        this.ConstListUnit=ConstListUnit;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ConstListUnit!=null) ConstListUnit.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ConstListUnit!=null) ConstListUnit.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ConstListUnit!=null) ConstListUnit.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConstListElem(\n");

        if(ConstListUnit!=null)
            buffer.append(ConstListUnit.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstListElem]");
        return buffer.toString();
    }
}
