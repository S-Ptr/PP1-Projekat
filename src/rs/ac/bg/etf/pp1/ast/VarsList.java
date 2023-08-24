// generated with ast extension for cup
// version 0.8
// 24/5/2023 11:26:8


package rs.ac.bg.etf.pp1.ast;

public class VarsList extends VarList {

    private VarList VarList;
    private VarListUnit VarListUnit;

    public VarsList (VarList VarList, VarListUnit VarListUnit) {
        this.VarList=VarList;
        if(VarList!=null) VarList.setParent(this);
        this.VarListUnit=VarListUnit;
        if(VarListUnit!=null) VarListUnit.setParent(this);
    }

    public VarList getVarList() {
        return VarList;
    }

    public void setVarList(VarList VarList) {
        this.VarList=VarList;
    }

    public VarListUnit getVarListUnit() {
        return VarListUnit;
    }

    public void setVarListUnit(VarListUnit VarListUnit) {
        this.VarListUnit=VarListUnit;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(VarList!=null) VarList.accept(visitor);
        if(VarListUnit!=null) VarListUnit.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(VarList!=null) VarList.traverseTopDown(visitor);
        if(VarListUnit!=null) VarListUnit.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(VarList!=null) VarList.traverseBottomUp(visitor);
        if(VarListUnit!=null) VarListUnit.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("VarsList(\n");

        if(VarList!=null)
            buffer.append(VarList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(VarListUnit!=null)
            buffer.append(VarListUnit.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [VarsList]");
        return buffer.toString();
    }
}
