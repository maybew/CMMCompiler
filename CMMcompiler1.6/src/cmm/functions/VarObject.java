package cmm.functions;

import java.util.ArrayList;
import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

public class VarObject {
	private String varString=null;	//������
	private int type=0;	//1�����ͣ�2��������,10,void������11���ͺ�����12�����ͺ���
	private String valueString=null;
	private int layer=0;	//��
	private boolean array=false;	//�Ƿ�������
	private int arraySize=-1;	//�����С
	private ArrayList<Double> arrayElementsList=null;	//����Ԫ��
	private int argNum=0;		//������������
	private Vector<VarObject> argListVector=null;	//�����б�
	private DefaultMutableTreeNode node=null;	//�������﷨���ϵ�λ��
	private TreeNode treeNode=null;	//�������﷨���ϵ�λ��
	public VarObject(){
		
	}
	
	public VarObject(VarObject vo){
		this.varString=vo.getVarString();
		this.type=vo.getType();
		this.valueString=vo.getValueString();
		this.layer=vo.getLayer();
		this.array=vo.isArray();
		this.arraySize=vo.getArraySize();
		this.arrayElementsList=vo.getArrayElementsList();
		this.argNum=vo.getArgNum();
		this.argListVector=vo.getArgListVector();
		this.node=vo.getNode();
		this.treeNode=vo.getTreeNode();
	}
	
	public String getVarString() {
		return varString;
	}
	public void setVarString(String varString) {
		this.varString = varString;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
		
	}
	public String getValueString() {
		return valueString;
	}
	public void setValueString(String valueString) {
		this.valueString = valueString;
	}
	public boolean isArray() {
		return array;
	}
	public void setArray(boolean array) {
		this.array = array;
	}

	public int getArraySize() {
		return arraySize;
	}
	public void setArraySize(int arraySize) {
		this.arraySize = arraySize;
		if(array){
			arrayElementsList=new ArrayList<Double>();
			for(int i=0;i<arraySize;i++){
				arrayElementsList.add(0.0);
			}
		}
	}
	
	public String getTypeString(){
		switch(type){
		case(1): return "INT";
		case(2): return "REAL";
		
		case 10:return "VOID_FUNCTION";
		case(11): return "INT_FUNCTION";
		case 12: 	return "REAL_FUNCTION";
		default : return "ERROR";
		}
	}
	
	/**
	 * ������Ԫ�ز���
	 * 
	 */
	public boolean setArrayElement(double value,int index){
		if(index>=arraySize) return false;	//����Խ��
		arrayElementsList.set(index, value);
		return true;
	}
	public double getArrayElement(int index){
		if(index>=arraySize) return 0;	//����Խ��
		else return arrayElementsList.get(index);
	}
	public int getLayer() {
		return layer;
	}
	public void setLayer(int layer) {
		this.layer = layer;
	}
	public int getArgNum() {
		return argNum;
	}
	public void setArgNum(int argNum) {
		this.argNum = argNum;
	}

	public DefaultMutableTreeNode getNode() {
		return node;
	}
	public void setNode(DefaultMutableTreeNode node) {
		this.node = node;
	}
	public TreeNode getTreeNode() {
		return treeNode;
	}
	public void setTreeNode(TreeNode treeNode) {
		this.treeNode = treeNode;
	}
	public Vector<VarObject> getArgListVector() {
		return argListVector;
	}
	public void setArgListVector(Vector<VarObject> argListVector) {
		this.argListVector = argListVector;
	}

	public ArrayList<Double> getArrayElementsList() {
		return arrayElementsList;
	}

	public void setArrayElementsList(ArrayList<Double> arrayElementsList) {
		this.arrayElementsList = arrayElementsList;
	}
}
