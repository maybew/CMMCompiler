package cmm.functions;

import java.util.Enumeration;
import java.util.Vector;


import javax.swing.JTextArea;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;


import cmm.ui.MainFrame;
import cmm.ui.components.InputFrame;
import cmm.ui.components.VarsTable;

/**
 * �������
 * 
 * @author axun
 */
public class SyntaxParse {
	private DefaultTreeModel treeModel = null;
	private Vector<VarObject> vos = new Vector<VarObject>();
	private String outputString = ""; // ������
	private String debugOutputString = "";	
	private JTextArea textArea = null; // �������
	private MyTable table=new MyTable();
	private VarsTable varsTable=new VarsTable();
	private int type=1;	//��ʱ��ס����
	private int layer=0;	//��ʱ��ס���
	private double returnVar=0;	//��ʱ��ס����ֵ

	public SyntaxParse(DefaultTreeModel treeModel) {
		this.treeModel = treeModel;
	}

	public void start() {
		program((TreeNode) treeModel.getRoot());
	}

	private void program(TreeNode node) {
		Enumeration<TreeNode> e = node.children();
		if (node.getChildCount() > 0) {
			while (e.hasMoreElements()) {
				TreeNode n = e.nextElement();
				state(n);
			}
		}
	}

	/**
	 * ��䣬��������
	 * 
	 * @param node
	 */
	private void state(TreeNode node) {
		if (node.toString().equalsIgnoreCase("intState")) { // int���
			intState(node);
		}
		if (node.toString().equalsIgnoreCase("realState")) { // int���
			realState(node);
		}
		if (node.toString().equalsIgnoreCase("returnState")) { // int���
			returnState(node);
		}
		if (node.toString().equalsIgnoreCase("writeState")) {
			writeState(node);
		}
		if (node.toString().equalsIgnoreCase("readState")) {
			readState(node);
		}
		if (node.toString().equalsIgnoreCase("ifState")) {
			ifState(node);
		}
		if (node.toString().equalsIgnoreCase("whileState")) {
			whileState(node);
		}
		if (node.toString().equalsIgnoreCase("assignState")) {
			assignState(node);
		}
		if (node.toString().equalsIgnoreCase("functionDefine")) {
			functionDefine(node);
		}
		if (node.toString().equalsIgnoreCase("block")) {
			block(node);
		}
	}

	/**
	 * ����
	 */
	private void block(TreeNode node) {
		layer++;
		Enumeration<TreeNode> e = node.children();
		if (node.getChildCount() > 0) {
			while (e.hasMoreElements()) {
				TreeNode n = e.nextElement();
				state(n);
				if (node.toString().equalsIgnoreCase("returnState")) { // return��䣬����block
					
					layer--;
					deleteVars(layer);
					return;
				}
				
			}
		}
		layer--;
		//�� ���������پֲ�����
		deleteVars(layer);
	}

	/**
	 * int���
	 * 
	 * @param node
	 */
	private void intState(TreeNode node) {
		if (node.getChildCount() == 1) { // ֻ��һ���ӽڵ�,û�и�ֵ
			if(node.getChildAt(0).toString().equalsIgnoreCase("array")){
				VarObject vo=new VarObject();
				vo.setType(1);
				vo.setLayer(layer);
				createArray(node.getChildAt(0),vo);
			}else{
				VarObject vo=new VarObject();
				vo.setType(1);
				vo.setVarString(node.getChildAt(0).toString());
				vo.setLayer(layer);
				addVar(vo);
			}
		} else if (node.getChildCount() == 2) {
			if(node.getChildAt(0).toString().equalsIgnoreCase("array")){
				VarObject vo=new VarObject();
				vo.setType(1);
				vo.setLayer(layer);
				createArray(node.getChildAt(0),vo);
			}
			VarObject vo=new VarObject();
			vo.setType(1);
			vo.setVarString(node.getChildAt(0).toString());
			vo.setLayer(layer);
			assignVar(node,vo);
		}
	}

	/**
	 * real���
	 * 
	 * @param node
	 */
	private void realState(TreeNode node) {
		if (node.getChildCount() == 1) { // ֻ��һ���ӽڵ�,û�и�ֵ
			if(node.getChildAt(0).toString().equalsIgnoreCase("array")){
				VarObject vo=new VarObject();
				vo.setType(2);
				vo.setLayer(layer);
				createArray(node.getChildAt(0),vo);
			}else{
				VarObject vo=new VarObject();
				vo.setType(2);
				vo.setVarString(node.getChildAt(0).toString());
				vo.setLayer(layer);
				addVar(vo);
			}
		} else if (node.getChildCount() == 2) {
			if(node.getChildAt(0).toString().equalsIgnoreCase("array")){
				VarObject vo=new VarObject();
				vo.setType(2);
				vo.setLayer(layer);
				createArray(node.getChildAt(0),vo);
			}
			VarObject vo=new VarObject();
			vo.setType(2);
			vo.setVarString(node.getChildAt(0).toString());
			vo.setLayer(layer);
			assignVar(node,vo);
		}
	}
	
	/**
	 * return ���
	 */
	private void returnState(TreeNode node){
		returnVar=expression(node.getChildAt(0));
	}

	/**
	 * if���
	 */
	private void ifState(TreeNode node) {
		boolean bool = bool(node.getChildAt(0));
		if (bool){
			if(node.getChildCount() >= 2)	//�����ǿ�if���
				state(node.getChildAt(1));
		}
			
		else {
			if (node.getChildCount() >= 3)
				state(node.getChildAt(2));
		}
	}

	/**
	 * while���
	 */
	public void whileState(TreeNode node) {
		while (true) {
			if (!bool(node.getChildAt(0)))
				break;
			else
				state(node.getChildAt(1));
		}
	}

	/**
	 * read���
	 */
	private void readState(TreeNode node) {
		assignVar(node);
	}

	/**
	 * write���
	 */
	private void writeState(TreeNode node) {
		double result=expression(node.getChildAt(0));
		if(type==1){
			outputln("" + (int)(result));
		}else{
			outputln(""+result);
		}
	}

	/**
	 * ��ֵ���
	 */
	private void assignState(TreeNode node) {
		assignVar(node);
	}
	/**
	 * �����������,���溯�����������б����Ϣ
	 */
	private void functionDefine(TreeNode node){
		TreeNode node1=node.getChildAt(0);
		int functionType=0;
		if(node1.toString().equals("void")){
			functionType=0;
		}
		if(node1.toString().equals("int")){
			functionType=1;
		}
		if(node1.toString().equals("real")){
			functionType=2;
		}
		//����
		int childNum=node1.getChildAt(0).getChildCount();
		Vector<VarObject> vector=new Vector<VarObject>();
		for(int i=0;i<childNum;i++){
			VarObject var=new VarObject();
			if(node1.getChildAt(0).getChildAt(i).toString().equalsIgnoreCase("int")) var.setType(1);
			if(node1.getChildAt(0).getChildAt(i).toString().equalsIgnoreCase("real")) var.setType(2);
			var.setVarString(node1.getChildAt(0).getChildAt(i).getChildAt(0).toString());
			vector.add(var);
		}
		
		VarObject vo=new VarObject();
		vo.setType(10+functionType);
		vo.setVarString(node1.getChildAt(0).toString());
		vo.setLayer(layer);
		vo.setTreeNode(node1);
		vo.setArgNum(childNum);
		vo.setArgListVector(vector);
		addVar(vo);
	}
	/**
	 * ���㲼�����ʽ��ֵ
	 */
	private boolean bool(TreeNode node) {
		boolean result = false;
		double result1 = expression(node.getChildAt(0));
		double result2 = expression(node.getChildAt(2));
		String string = node.getChildAt(1).toString();
		int action = 1; // 1== 2> 3< 4>= 5<=
		if (string.equalsIgnoreCase("=="))
			action = 1;
		if (string.equalsIgnoreCase(">"))
			action = 2;
		if (string.equalsIgnoreCase("<"))
			action = 3;
		if (string.equalsIgnoreCase(">="))
			action = 4;
		if (string.equalsIgnoreCase("<="))
			action = 5;

		switch (action) {
		case 1:
			if (result1 == result2)
				result = true;
			else
				result = false;
			break;
		case 2:
			if (result1 > result2)
				result = true;
			else
				result = false;
			break;
		case 3:
			if (result1 < result2)
				result = true;
			else
				result = false;
			break;
		case 4:
			if (result1 >= result2)
				result = true;
			else
				result = false;
			break;
		case 5:
			if (result1 <= result2)
				result = true;
			else
				result = false;
			break;
		default:
			break;
		}
		return result;
	}

	/**
	 * ������ʽ��ֵ
	 * ÿ���ȼ�ס����type=1���������double����type=2
	 * @param s
	 */
	private double expression(TreeNode node) {
		type=1;
		double result = 0;
		Enumeration<TreeNode> e = node.children();
		int action = 1; // 0 û�У�1+��2- Ĭ����+
		while (e.hasMoreElements()) {
			TreeNode n = e.nextElement();
			if (n.toString().equalsIgnoreCase("term")) {
				switch (action) {
				case 1:
					result += term(n);
					break;
				case 2:
					result -= term(n);
					break;
				}
			}
			if (e.hasMoreElements()) {
				TreeNode n1 = e.nextElement();
				if (n1.toString().equalsIgnoreCase("+")) {
					action = 1;
				} else if (n1.toString().equalsIgnoreCase("-")) {
					action = 2;
				}
			} else
				break;
		}
		// outputln("expression:"+result);
		return result;
	}

	private double term(TreeNode node) {
		double result = 1;
		Enumeration<TreeNode> e = node.children();
		int action = 1; // 0 û�У�1*��2/ Ĭ����*
		while (e.hasMoreElements()) {
			TreeNode n = e.nextElement();
			if (n.toString().equalsIgnoreCase("unary")) {
				switch (action) {
				case 1:
					result *= unary(n);
					break;
				case 2:
					double unaryResult=unary(n);
					if(Math.abs(unaryResult)<0.0000001) {
						error("��������Ϊ�㣡");
						return 0;
					}
					result /= unaryResult;
					break;
				}
			}
			if (e.hasMoreElements()) {
				TreeNode n1 = e.nextElement();
				if (n1.toString().equalsIgnoreCase("*")) {
					action = 1;
				} else if (n1.toString().equalsIgnoreCase("/")) {
					action = 2;
				}
			} else
				break;
		}
		// outputln("term:"+result);
		return result;
	}

	private double unary(TreeNode node) {
		TreeNode n = node.getChildAt(0);
		String string = n.toString();
		if (string.equalsIgnoreCase("expression"))
			return expression(n);
		else if (string.equalsIgnoreCase("array"))
			return useArray(n);
		else {
			boolean hasVar = false;
			for (int i = 0; i < n.toString().length(); i++) {
				if ((string.charAt(i) >= 65 && string.charAt(i) <= 90)
						|| (string.charAt(i) >= 97 && string.charAt(i) <= 122)) {
					hasVar = true;
				}
			}
			if (hasVar) {
				double r= useVar(n);
				debugOutputln("ʹ�ñ�����"+n+"="+r);
				return r;
			} else{
				for(int i=0;i<string.length();i++) if(string.charAt(i)=='.'){
					type=2;	//������
					;
				}
				return Double.parseDouble(string);
			}
				
		}
	}
	
	/**
	 * ��������
	 * @param vo
	 * @return
	 */
	private double callFunction(VarObject vo,TreeNode node){
		System.out.print("���ú�����"+node.toString()+",�㣺"+layer+",������");
		//�޲κ���
		if(vo.getArgNum()<=0){
			if(vo.getTreeNode().getChildCount()>1) block(vo.getTreeNode().getChildAt(1));
			if(vo.getType()==12) type=2;
			return returnVar;
		}
		//�в����ĺ�����
		else if(vo.getArgNum()>0){
			layer++;
			//�ȸ��ݲ�����������
			Vector<VarObject> vars=vo.getArgListVector();
			for(int i=0;i<vars.size();i++){
				VarObject var=new VarObject(vars.get(i));
				var.setLayer(layer);
				layer--;	/*
					ע�������һ���������ڻ������ã���Ϊ�����½�һ������x����ʹ��ʱ���ͻ��ԭ�����е�x©������Ϊ�½���x��ԭ�����е����ȼ���*/
				var.setValueString(""+expression(node.getChildAt(i)));
				layer++;
				addVar(var);
				System.out.print(var.getValueString()+",");
			}
			System.out.println();
			if(vo.getTreeNode().getChildCount()>1) block(vo.getTreeNode().getChildAt(1));
			if(vo.getType()==12) type=2;
			layer--;
			deleteVars(layer);	//���ٲ���
			return returnVar;
		}
		System.out.println("��������ʧ�ܣ�����0");
		return 0;
	}

	/**
	 * ʹ�ñ�����������������
	 * 
	 * @param s
	 */
	private double useVar(TreeNode node) {
		String s = node.toString();
		//���ҵ�ǰ��ı������Ҳ����Ļ�������һ��ģ���������ϲ��
		for(int t=0;t<=layer;t++)
		for (int i = 0; i < vos.size(); i++) {
			
			if (s.equals(vos.get(i).getVarString())&&(vos.get(i).getLayer()==(layer-t))) {	
				debugOutputln(s+",�㣺"+vos.get(i).getLayer()+",��ǰ�㣺"+layer);
				if (vos.get(i).isArray() == false){
					if(vos.get(i).getType()==2) type=2;
					if(vos.get(i).getType()==10){
						error(vos.get(i).getVarString()+"��void���ͣ�");
						return 0;
					}else
					if(vos.get(i).getType()>=10){
						
						return callFunction(vos.get(i),node);
					}else
					if(vos.get(i).getValueString()==null){
						error("����'"+vos.get(i).getVarString()+"'δ��ʼ��");
					}
					else return Double.parseDouble(vos.get(i).getValueString());
				}
			}
		}
		
		if (s.equals("array")) {
			return useArray(node);
		}
		debugOutputln("����ʧ�ܣ�����0");
		return 0;
	}

	/**
	 * ������ֵ
	 * 
	 * @param s
	 */
	private void assignVar(TreeNode node,VarObject vo) {
		String s = node.getChildAt(0).toString();
		String v = null;
		double result;
			
			if (node.getChildCount() > 1) {
				result=expression(node.getChildAt(1));
				//double��ֵ��int ����
				if(vo.getType()==1&&type==2){
					warning("��real���͸�ֵ��int���ͣ����ܻ���ʧ���ȣ�");
				}
			} else {
				result=Double.parseDouble(new InputFrame(vo.getType()).getInputString());
			}
		
		if(!s.equalsIgnoreCase("array")){
			vo.setValueString("" + result);
			addVar(vo);

		}else{
			assignArray(node.getChildAt(0), v);
		}
	}
	
	/**
	 * ������ֵ������void��������
	 * @param node
	 */
	private void assignVar(TreeNode node) {
		String s = node.getChildAt(0).toString();
		String v = null;
		
		//��������
		for (int i = 0; i < vos.size(); i++) {
			if (s.equals(vos.get(i).getVarString())) {
				if (vos.get(i).isArray() == false){
					type=vos.get(i).getType(); 
					if(vos.get(i).getType()==10){
						callFunction(vos.get(i),node.getChildAt(0));
						return;
					}
				}
			}
		}
		
		if (node.getChildCount() > 1) {
			v = "" + expression(node.getChildAt(1));
		} else {
			v = new InputFrame(type).getInputString();
		}
		
		if(!s.equalsIgnoreCase("array")){
		for(int t=0;t<=layer;t++){
			for (int i = 0; i < vos.size(); i++) {
				if (s.equals(vos.get(i).getVarString())&&(vos.get(i).getLayer()==(layer-t))) {
					vos.get(i).setValueString(""+v);
					refreshVarsTable();
					return;
				}
			}
		}
		}else{
			assignArray(node.getChildAt(0), v);
		}
	}
	
	/**
	 * �������飬�����Զ���ʼ��Ϊ0
	 */
	private void createArray(TreeNode node ,VarObject vo){
		vo.setVarString(node.getChildAt(0).toString());
		vo.setArray(true);
		vo.setArraySize(Integer.parseInt(node.getChildAt(1).toString()));
		addVar(vo);
	}

	/**
	 * ʹ������
	 * 
	 * @param s
	 */
	private double useArray(TreeNode node) {
		String name = node.getChildAt(0).toString();
		int index = (int) expression(node.getChildAt(1));
		for (int i = 0; i < vos.size(); i++) {
			if (name.equals(vos.get(i).getVarString())) {
				type=vos.get(i).getType();
				return vos.get(i).getArrayElement(index);
			}
		}
		return 0;
	}

	public void assignArray(TreeNode node, String v) {

		String name = node.getChildAt(0).toString();
		int index = (int) expression(node.getChildAt(1));
		
		for (int i = 0; i < vos.size(); i++) {
			if (name.equals(vos.get(i).getVarString())) {
				if (index >= vos.get(i).getArraySize()) {
					error("��������Խ�磡");
				}
				vos.get(i).setArrayElement(Double.parseDouble(v), index);
			}
		}
		refreshVarsTable();
	}
	/**
	 * ��������
	 */
	private void addVar(VarObject vo){
		vos.add(vo);
		debugOutputln("����������"+vo.getVarString()+",�㣺"+vo.getLayer()+",ֵ:"+vo.getValueString());
		printStack();
		refreshVarsTable();
	}
	/**
	 * ���ٱ���
	 * @param s
	 */
	private void deleteVars(int layer){
		debugOutput("���ٱ�����");
		for (int i = 0; i < vos.size(); i++) {
			if (vos.get(i).getLayer()>layer) {
				debugOutput(vos.get(i).getVarString()+",�㣺"+vos.get(i).getLayer()+",ֵ:"+vos.get(i).getValueString()+";");
				vos.remove(i);
				i--;	//ע��˴�����Ȼ�޷���ȫɾ������Ϊ������ɺ󣬺���Ļ���ǰ��һ��
			}
		}
		debugOutputln("");
		printStack();
		refreshVarsTable();
	}
	
	
	public void warning(String s){
		outputln("���棺" + s);
	}
	public void error(String s) {
		outputln("����" + s);
	}

	private void output(String s) {
		outputString += s;
		textArea.setText(outputString);
	}

	private void outputln(String s) {
		output(s + "\r\n");
	}

	/**
	 * �����������
	 */
	public void setOutputObject(JTextArea textArea) {
		this.textArea = textArea;
	}
	/**
	 * ����̨���
	 * @param s
	 */
	private void debugOutput(String s){
		debugOutputString+=s;
		MainFrame.outputPanel.DebugOutput(debugOutputString);
	}
	private void debugOutputln(String s){
		debugOutputString+=s+"\n";
		MainFrame.outputPanel.DebugOutput(debugOutputString);
	}
	private void printStack(){
		debugOutput("������");
		for (int i = 0; i < vos.size(); i++) {
			debugOutput("("+vos.get(i).getVarString()+","+vos.get(i).getValueString()+","+vos.get(i).getLayer()+")");
		}
		debugOutputln("");
	}
	
	/**
	 * ���������
	 * @return
	 */
	public String getOutputString() {
		return outputString;
	}
	/**
	 * ��ʾ������
	 */
	public void showTable() {
		varsTable.setVisible(true);
	}
	
	public void refreshVarsTable(){
		//��ʾ����
		table.removeAll();
		for(int i=0;i<vos.size();i++){
			table.addRow(vos.get(i));
		}
		varsTable.refresh(table);
	}
}
