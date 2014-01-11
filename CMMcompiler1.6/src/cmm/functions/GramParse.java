package cmm.functions;

import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.HeaderTokenizer.Token;

import sun.reflect.generics.tree.Tree;

/*
 * �﷨����
 */
/**
 * ������ʹ���쳣�����ǲ��Ǽ򵥵㣿
 */
public class GramParse {
	private CMMToken[] cmmTokens = new CMMToken[2000];
	ArrayList<CMMToken> errorTokens=new ArrayList<CMMToken>();
	private int p = 0;
	private int size = 0; // token �ĸ���

	private Stack stack = new Stack(); // �������������������

	private String outputString = "";
	private String debugOutputString="";
	private String inString = ""; // ����
	private boolean hasError = false;
	private boolean hasReturn=false;	//��ʱ��ס����û��return���

	// �﷨��
	JTree tree;
	DefaultTreeModel treeModel;
	DefaultMutableTreeNode root;
	private int nodeNum = 0; // �ڵ���ţ������õ�
	

	/**
	 * �ȵ��ôʷ����������token
	 * 
	 * @param in
	 */
	public GramParse(String in, CMMToken[] cmmTokens, int size) {
		this.cmmTokens = cmmTokens;
		this.size = size;
		this.inString = in;
		initialize();
	}

	public void initialize() {
		outputLine("���ôʷ���������");
		outputLine("���ʸ�����" + size);
		p = -1;
		outputLine("��ʼ���﷨������");

		root = new DefaultMutableTreeNode("program");
		treeModel = new DefaultTreeModel(root);
		tree = new JTree(treeModel);
		tree.setToggleClickCount(1); // ������չ���ڵ�
	}

	/**
	 * ��ʼ
	 */
	public void start() {
		program();
	}

	/**
	 * ������������
	 */
	public void program() {
		while (true) {
			state(root);
			if (p >= size - 1)
				break; // ������
		}
		outputLine("�﷨������������");
	}

	/**
	 * ��䣬���������������
	 */
	public void state(DefaultMutableTreeNode node) {
		if (moveP() == false) {
			return;
		}
		CMMToken cmmToken = getToken();
		if (cmmToken.getToken().equals("int")
				|| cmmToken.getToken().equals("real")) {
			backP();
			var_define_state(node);
		} else if (cmmToken.getToken().equals("void")) {
			backP();
			void_state(node);
		} else if (cmmToken.getToken().equals("return")) {
			backP();
			return_state(node);
		} else if (cmmToken.getToken().equals("if")) {
			backP();
			if_state(node);
		} else if (cmmToken.getToken().equals("while")) {
			backP();
			while_state(node);
		} else if (cmmToken.getTypeString().equals("IDENT")) {
			backP();
			assign_state(node);
		} else if (cmmToken.getToken().equals("write")) {
			backP();
			write_state(node);
		} else if (cmmToken.getToken().equals("read")) {
			backP();
			read_state(node);
		} else if (cmmToken.getToken().equals(";")) { // �����
			return;
		} else if (cmmToken.getToken().equals("{")) { // ���飬�ȼ������
			backP();
			block(node);
		} else {
			return;
		}

	}

	/**
	 * ����,������return���
	 * 
	 */
	public void block(DefaultMutableTreeNode node) {
		hasReturn=false;
		if (moveP() == false) {
			return;
		}
		if (!getToken().getToken().equals("{")) {
			error(getToken(), "Ӧ����'{'");
		}
		if (moveP() == false) {
			return;
		}
		if (getToken().getToken().equals("}")) { // ��block
			return;
		}
		backP();
		pushVar(getToken(), 0, false, 0,-1,null,null); // �Ѵ�����ѹ��ջ��
		DefaultMutableTreeNode blockNode = new DefaultMutableTreeNode("block");
		// blockNode.add(new DefaultMutableTreeNode("{")); //û�б�Ҫ��
		node.add(blockNode);
		state(blockNode);
		// ��ƥ���������
		while (true) {
			if (moveP() == false) {
				error(getToken(), "����ȱ��'}'");
				return;
			}
			CMMToken cmmToken1 = getToken();
			if (cmmToken1.getToken().equals("}")) {
				popVar(); // ����} ���͵�����ֱ��{
				// blockNode.add(new DefaultMutableTreeNode("}"));
				return;
			}
			backP(); // ע��ָ����ǰ��һλ
			state(blockNode); // û�н���������ƥ�����
			if(hasReturn){
				error(getToken(), "���ܰ�����������return���");
			}

		}
		
		
	}
	
	/**
	 * ���飬��return��䣬Ϊ������
	 * 
	 */
	public void blockWithReturn(DefaultMutableTreeNode node) {
		hasReturn=false;
		block(node);
		if(hasReturn==false){
			error(getToken(),"ȱ��return��䣡");
		}
		hasReturn=false;
		return;
	}
	
	/**
	 * return ���
	 */
	private void return_state(DefaultMutableTreeNode node1){
		if (moveP() == false) {
			error(getToken() ,"Ӧ����return");
			return;
		}
		if (!getToken().getToken().equals("return")) {
			error(getToken(), "Ӧ����'return'");
		}
		DefaultMutableTreeNode node=new DefaultMutableTreeNode("returnState");
		node1.add(node);
		
		if(expression(node)==false){
			System.out.println(getToken().getToken());
			if (!getToken().getToken().equals(";")) {
				error(getToken(), "Ӧ����';'");
				return;
			}
			return;
			
		}
		if (moveP() == false) {
			error(getToken(),"Ӧ����';'");
			return;
		}
		if (!getToken().getToken().equals(";")) {
			error(getToken(), "Ӧ����';'");
			return;
		}
		hasReturn=true;
		return;
	}

	/**
	 * ��������&��ֵ���
	 */
	private void var_define_state(DefaultMutableTreeNode node) {
		if (moveP() == false) {
			return;
		}
		if (getToken().getToken().equals("int")) {
			backP();
			int_define_state(node);
		} else if (getToken().getToken().equals("real")) {
			backP();
			real_define_state(node);
		} else {
			error(getToken(),"Ӧ����int����real");
		}
	}

	/**
	 * ��������&��ֵ���,int
	 */
	private void int_define_state(DefaultMutableTreeNode node1) {
		DefaultMutableTreeNode node = new DefaultMutableTreeNode("intState");
		node1.add(node);
		if (moveP() == false) {
			return;
		}
		if (!getToken().getToken().equals("int")) {
			error(getToken(), "ӦΪint");
			return;
		}

		if (moveP() == false) {
			error(getToken(),"Ӧ���Ǳ�ʶ��");
			return;
		}
		CMMToken cmmToken = getToken();
		if (!(cmmToken.getTypeString().equals("IDENT"))) {
			error(getToken(), "Ӧ����'��ʶ��'");
			return;
		}

		// ��ӽڵ�
		DefaultMutableTreeNode identNode=new DefaultMutableTreeNode(getToken().getToken());
		CMMToken identToken=getToken();
		
		useIdent(node,identNode,identToken);

		if (moveP() == false) {
			error(getToken(), "Ӧ����';'");
			return;
		}
		CMMToken cmmToken1 = getToken();
		// �ǲ��Ǻ�������
		if (getToken().getToken().equals("(")) {

			backP();

			node1.remove(node);
			DefaultMutableTreeNode functionNode = new DefaultMutableTreeNode(
					"functionDefine");
			node1.add(functionNode);
			functionDefine(functionNode, cmmToken,1);
			return;
		}
		//�ǲ�������
		if(getToken().getToken().equals("[")){
			backP();
			node.remove(identNode);
			DefaultMutableTreeNode arrayNode = new DefaultMutableTreeNode(
					"array");
			node.add(arrayNode);
			createArray(arrayNode, cmmToken);
			return;
		}
		pushVar(cmmToken, 1, false, -1,-1,null,null);
		if (getToken().getToken().equals(";")) {

			return;

		}
		
		if (!(getToken().getToken().equals("="))) {
			error(getToken(), "Ӧ����'='");
			return;
		}

		if (expression(node) == false) {
			error(getToken(),"���ʽ����");
		} else if (moveP() == false) {
			error(getToken(), "����ȱ��';'");
			backP();
			return;
		}
		if (getToken().getToken().equals(";")) {

			return; // ��ȷ�Ķ���&��ֵ���
		} else {
			error(getToken(), "Ӧ����';'");
		}

	}

	/**
	 * ��������&��ֵ��� real
	 */
	private void real_define_state(DefaultMutableTreeNode node1) {
		DefaultMutableTreeNode node = new DefaultMutableTreeNode("realState");
		node1.add(node);
		if (moveP() == false) {
			return;
		} else {
			if (!getToken().getToken().equals("real")) {
				error(getToken(), "ӦΪint");
				return;
			}
		}

		if (moveP() == false) {
			error(getToken(),"ȱ�ٱ�ʶ����");
			return;
		}
		CMMToken cmmToken = getToken();
		if (!(cmmToken.getTypeString().equals("IDENT"))) {

			error(getToken(),"ȱ�ٱ�ʶ����");
			return;
		}

		// ��ӽڵ�
		DefaultMutableTreeNode identNode=new DefaultMutableTreeNode(getToken().getToken());
		CMMToken identToken=getToken();
		useIdent(node,identNode,identToken);
		
		if (moveP() == false) {
			pushVar(cmmToken, 2, false, 0,-1,null,null);
			error(getToken(),"��ʶ������");
			return;
		}
		CMMToken cmmToken1 = getToken();
		// �ǲ��Ǻ�������
		if (getToken().getToken().equals("(")) {

			backP();
			// ʶ������
			node1.remove(node);
			DefaultMutableTreeNode functionNode = new DefaultMutableTreeNode(
					"functionDefine");
			node1.add(functionNode);
			functionDefine(functionNode, cmmToken,2);
			return;
		}
		// �ǲ�������
		if (getToken().getToken().equals("[")) {
			backP();
			// ʶ������
			node.remove(identNode);
			DefaultMutableTreeNode arrayNode = new DefaultMutableTreeNode(
					"array");
			node.add(arrayNode);
			createArray(arrayNode, cmmToken);
			return;
		}
		pushVar(cmmToken, 2, false, -1,-1,null,null);

		if (cmmToken1.getToken().equals(";")) {
			return;
		}
		if (!(cmmToken1.getToken().equals("="))) {
			error(getToken(),"Ӧ����'='");
			return;
		}

		if (expression(node) == false) {
			error(getToken(),"���ʽ����");
		} else if (moveP() == false) {
			error(getToken(), "����ȱ��';'");
			backP();
		} else {
			if (getToken().getToken().equals(";")) {
				// DefaultMutableTreeNode fhNode = new
				// DefaultMutableTreeNode(
				// getToken().getToken());
				// node.add(fhNode);
				return; // ��ȷ�Ķ���&��ֵ���
			} else {
				error(getToken(), "Ӧ����';'");
			}
		}

	}
	
	/**
	 * �˺���ֻ��Ԥ����Ȼ�����functionDefine����
	 * @param node1
	 */
	private void void_state(DefaultMutableTreeNode node1){
		if (moveP() == false) {
			return;
		}
		if (!getToken().getToken().equals("void")) {
			error(getToken(), "ӦΪvoid");
			return;
		}

		if (moveP() == false) {
			return;
		}
		if (!(getToken().getTypeString().equalsIgnoreCase("IDENT"))) {

			error(getToken(), "Ӧ��Ϊһ����ʶ��");
		}
		DefaultMutableTreeNode functionNode = new DefaultMutableTreeNode(
		"functionDefine");
		node1.add(functionNode);
		functionDefine(functionNode, getToken(),0);
	}

	/**
	 * write ��䣬дһ�����ʽ
	 */
	private void write_state(DefaultMutableTreeNode node1) {
		DefaultMutableTreeNode node = new DefaultMutableTreeNode("writeState");
		node1.add(node);
		if (moveP() == false) {
			return;
		}
		if (!getToken().getToken().equals("write")) {
			error(getToken(), "ӦΪwrite");
			return;
		}

		if (moveP() == false) {
			return;
		}
		if (!(getToken().getToken().equals("("))) {

			error(getToken(), "Ӧ��Ϊ'('");
		}

		expression(node);
		if (moveP() == false) {
			error(getToken(), "Ӧ��Ϊ')'");
			return;
		}
		if (!(getToken().getToken().equals(")"))) {
			error(getToken(), "Ӧ��Ϊ')'");
			return;
		}

		if (moveP() == false) {
			error(getToken(), "����ȱ��';'");
			backP();
			return;
		}
		if (getToken().getToken().equals(";")) {
			return; // ��ȷ�Ķ���&��ֵ���
		} else {
			error(getToken(), "Ӧ����';'");
		}

		return;

	}

	/**
	 * read ���,ֻ�ܶ�һ������
	 */
	private void read_state(DefaultMutableTreeNode node1) {
		DefaultMutableTreeNode node = new DefaultMutableTreeNode("readState");
		node1.add(node);
		if (moveP() == false) {
			return;
		}
		if (!getToken().getToken().equals("read")) {
			error(getToken(), "ӦΪread");
			return;
		}
		if (moveP() == false) {
			return;
		}
		if (!(getToken().getToken().equals("("))) {
			error(getToken(), "Ӧ��Ϊ'('");
		}

		if (moveP() == false) {
			error(getToken(), "Ӧ��Ϊһ������");
			return;
		}
		if (!getToken().getTypeString().equalsIgnoreCase("IDENT")) {
			error(getToken(), "Ӧ����һ����ʶ��");
		}
		DefaultMutableTreeNode identNode=new DefaultMutableTreeNode(getToken().getToken());
		CMMToken identToken=getToken();
		useIdent(node,identNode,identToken);
		if (moveP() == false) {
			useVar(identToken, false);
			return;
		}
		CMMToken cmmToken1 = getToken();
		// �ǲ�������
		if (getToken().getToken().equals("[")) {
			useVar(identToken, true); // ʹ�ñ���ʱ����Ҫ���ô˷������Լ������Ƿ�Ϸ�
			backP();
			// ʶ������
			node.remove(identNode);
			DefaultMutableTreeNode arrayNode = new DefaultMutableTreeNode(
					"array");
			node.add(arrayNode);
			arrayNode.add(identNode);
			useArray(arrayNode, getToken());
		} else {
			useVar(identToken, false); // ʹ�ñ���ʱ����Ҫ���ô˷������Լ������Ƿ�Ϸ�
			backP();
		}
		if (moveP() == false) {
			return;
		}
		if (!(getToken().getToken().equals(")"))) {
			error(getToken(), "Ӧ��Ϊ')'");
			return;
		}

		if (moveP() == false) {
			error(getToken(), "����ȱ��';'");
			backP();
		} else {
			if (getToken().getToken().equals(";")) {

				return; // ��ȷ�Ķ���&��ֵ���
			} else {
				error(getToken(), "Ӧ����';'");
			}
		}
		return;

	}

	/**
	 * if ���
	 * 
	 * @param s
	 */
	private void if_state(DefaultMutableTreeNode node1) {
		DefaultMutableTreeNode node = new DefaultMutableTreeNode("ifState");
		node1.add(node);
		if (moveP() == false) {
			return;
		}
		if (!getToken().getToken().equals("if")) {
			error(getToken(),"Ӧ����if");
			return;
		}

		if (moveP() == false) {
			error(getToken(),"Ӧ����'('");
			return;
		}
		if (!(getToken().getToken().equals("("))) {
			error(getToken(), "Ӧ��Ϊ��'('");
			return;
		}

		if (bool(node) == false) {
			error(getToken(), "Ӧ��Ϊһ��bool���ʽ");
		} else if (moveP() == false) {
			error(getToken(), "Ӧ��Ϊ')'");
			return;
		}
		if (!(getToken().getToken().equals(")"))) {
			error(getToken(), "Ӧ��Ϊ')'");
			return;
		}

		state(node);
		if (moveP() == false) {
			return;
		}
		if (getToken().getToken().equals("else")) {
			state(node);
			return;
		}
		backP();
		return;

	}

	/**
	 * while ��䣬�ṹ���� if���
	 * 
	 * @param s
	 */
	private void while_state(DefaultMutableTreeNode node1) {
		DefaultMutableTreeNode node = new DefaultMutableTreeNode("whileState");
		node1.add(node);
		if (moveP() == false) {
			return;
		}
		if (!getToken().getToken().equals("while")) {
			error(getToken(),"Ӧ����while");
			return;
		}

		if (moveP() == false) {
			error(getToken(),"Ӧ����'('");
			return;
		}
		if (!(getToken().getToken().equals("("))) {
			error(getToken(), "Ӧ��Ϊ��'('");
			return;
		}

		if (bool(node) == false) {
			error(getToken(), "Ӧ��Ϊһ��bool���ʽ");
		} else if (moveP() == false) {
			error(getToken(), "Ӧ��Ϊ')'");
			return;
		}
		if (!(getToken().getToken().equals(")"))) {
			error(getToken(), "Ӧ��Ϊ')'");
			return;
		}

		state(node);

		return;

	}

	/**
	 * �򵥵ĸ�ֵ���
	 */
	private void assign_state(DefaultMutableTreeNode node1) {
		DefaultMutableTreeNode node = new DefaultMutableTreeNode("assignState");
		node1.add(node);
		if (moveP() == false) {
			return;
		}
		if (!getToken().getTypeString().equalsIgnoreCase("IDENT")) {
			error(getToken(),"Ӧ���Ǳ�ʶ��");
			return;
		}
		DefaultMutableTreeNode identNode=new DefaultMutableTreeNode(getToken().getToken());
		CMMToken identToken=getToken();
		useIdent(node, identNode,identToken);

		if (moveP() == false) {
			error(getToken(),"ȱ��';'");
			return;
		}
		CMMToken cmmToken1 = getToken();
		// �ǲ�������
		if (getToken().getToken().equals("[")) {
			useVar(identToken, true);
			backP();
			// ʶ������
			node.remove(identNode);
			DefaultMutableTreeNode arrayNode = new DefaultMutableTreeNode(
					"array");
			node.add(arrayNode);
			arrayNode.add(identNode);
			useArray(arrayNode, getToken());
		} else {
			useVar(identToken, false);
			backP();
		}

		if (moveP() == false) {
			return;
		}
		if(getToken().getToken().equals(";")){
			return;
		}
		
		if (!(getToken().getToken().equals("="))) {
			error(getToken(),"Ӧ����'='");
			return;
		}
		if (moveP() == false) {
			error(getToken(),"ȱ�ٱ��ʽ");
			return;
		}
		backP();
		expression(node);
		if (moveP() == false) {
			error(getToken(), "����ȱ��';'");
			return;
		} else if (getToken().getToken().equals(";")) {

			return; // ��ȷ�ĸ�ֵ���
		} else {
			error(getToken(), "����ȱ��';'");
			return;
		}

	}
	
	
	/**
	 * ��������,�����������Ƿ������� 0,void,1��int��2 real
	 * ע��˴��ı����������־��̫һ��
	 * {����block}
	 */
	private void functionDefine(DefaultMutableTreeNode node1,CMMToken cmmToken,int type ){
		DefaultMutableTreeNode node2=null;
		if(type==0){
			node2=new DefaultMutableTreeNode("void");
		}else if(type==1){
			node2=new DefaultMutableTreeNode("int");
		}else if(type==2){
			node2=new DefaultMutableTreeNode("real");
		}
		node1.add(node2);
		DefaultMutableTreeNode node=new DefaultMutableTreeNode(cmmToken.getToken());	//��������
		
		node2.add(node);
		if(moveP()==false){
			error(getToken(),"Ӧ����'('");
			return;
		}
		if(!getToken().getToken().equals("(")){
			error(getToken(),"Ӧ����'('");
			return;
		}
		
		
		if(moveP()==false){
			error(getToken(),"Ӧ����')'");
			return;
		}
		//�޲���
		if(getToken().getToken().equals(")")){
			pushVar(cmmToken, 10+type, false, -1,0,null,node2);
			if (type==0) block(node2); else blockWithReturn(node2);
			return;
		}
		
		
		//�������ĺ�������
		if(getToken().getToken().equals("int")||getToken().getToken().equals("real")){
			backP();
			
			args(node,cmmToken,type);
			
			if (type==0) block(node2); else blockWithReturn(node2);
			popVar();
			return;
		}
		
		
		
	}
	
	/**
	 * ��������
	 */
	private void args(DefaultMutableTreeNode node,CMMToken cmmToken,int type){
		int argNum=0;
		Vector<VarObject> argList=new Vector<VarObject>();
		Vector<CMMToken> varTokenVector=new Vector<CMMToken>();

		while(true){
			
			if(moveP()==false){
				return;
			}
			//INT����
			if(getToken().getToken().equals("int")){
				if(moveP()==false){
					error(getToken(),"Ӧ����һ����ʶ����");
					return;
				}
				if(!(getToken().getTypeString().equalsIgnoreCase("IDENT"))){
					error(getToken(),"Ӧ����һ����ʶ����");
					return;
				}
				DefaultMutableTreeNode intNode=new DefaultMutableTreeNode("int");
				node.add(intNode);
				DefaultMutableTreeNode identNode=new DefaultMutableTreeNode(getToken().getToken());
				intNode.add(identNode);
				VarObject vo=new VarObject();
				vo.setType(1);
				vo.setVarString(getToken().getToken());
				argList.add(vo);
				argNum++;
				//
			
				varTokenVector.add(getToken());
				
			}
			
			//real
			if(getToken().getToken().equals("real")){
				if(moveP()==false){
					error(getToken(),"Ӧ����һ����ʶ����");
					return;
				}
				if(!(getToken().getTypeString().equalsIgnoreCase("IDENT"))){
					error(getToken(),"Ӧ����һ����ʶ����");
					return;
				}
				DefaultMutableTreeNode realNode=new DefaultMutableTreeNode("real");
				node.add(realNode);
				DefaultMutableTreeNode identNode=new DefaultMutableTreeNode(getToken().getToken());
				realNode.add(identNode);
				VarObject vo=new VarObject();
				vo.setType(2);
				vo.setVarString(getToken().getToken());
				argList.add(vo);
				argNum++;
				//
				CMMToken token=new CMMToken();
				token.setToken(vo.getVarString());
				token.setType(2);
				pushVar(token, 2, false, -1, 0, null, null);
			}
			
			if(moveP()==false){
				error(getToken(),"Ӧ����')'");
				return;
			}
			if(getToken().getToken().equals(",")) continue;
			if(getToken().getToken().equals(")")) break;
			
			break;
		}
		
		pushVar(cmmToken, 10+type, false, -1, argNum, argList, node);
		//����һ�������ţ��ѱ����ͺ�����һ��Ž�ȥ
		CMMToken braceToken=new CMMToken();
		braceToken.setToken("{");
		braceToken.setType(4);
		
		pushVar(braceToken, 4, false, -1, 0, null, null);
		for(int i=0;i<varTokenVector.size();i++){
			pushVar(varTokenVector.get(i), 1, false, -1, 0, null, null);
		}
	}
	/**
	 * �������ʽ�����ǵ��������
	 */
	private boolean bool(DefaultMutableTreeNode node1) {
		DefaultMutableTreeNode node = new DefaultMutableTreeNode("boolExpr");
		node1.add(node);
		if (expression(node) == false)
			return false;
		if (moveP() == false) {
			return false;
		} if (getToken().getToken().equals(">")) {
			DefaultMutableTreeNode dyNode = new DefaultMutableTreeNode(
					getToken().getToken());
			node.add(dyNode);
			return expression(node);
		} else if (getToken().getToken().equals("<")) {
			DefaultMutableTreeNode dyNode = new DefaultMutableTreeNode(
					getToken().getToken());
			node.add(dyNode);
			return expression(node);
		} else if (getToken().getToken().equals(">=")) {
			DefaultMutableTreeNode dyNode = new DefaultMutableTreeNode(
					getToken().getToken());
			node.add(dyNode);
			return expression(node);
		} else if (getToken().getToken().equals("<=")) {
			DefaultMutableTreeNode dyNode = new DefaultMutableTreeNode(
					getToken().getToken());
			node.add(dyNode);
			return expression(node);
		} else if (getToken().getToken().equals("==")) {
			DefaultMutableTreeNode dyNode = new DefaultMutableTreeNode(
					getToken().getToken());
			node.add(dyNode);
			return expression(node);
		} else if (getToken().getToken().equals("<>")) {
			DefaultMutableTreeNode dyNode = new DefaultMutableTreeNode(
					getToken().getToken());
			node.add(dyNode);
			return expression(node);
		} else
			return false;
	}

	/**
	 * �������ʽ,���ǵ�������� expression=term[+|-term]*
	 * 
	 * @param s
	 */
	public boolean expression(DefaultMutableTreeNode node1) {
		DefaultMutableTreeNode node = new DefaultMutableTreeNode("expression");
		node1.add(node);
		int count = 0;
		DefaultMutableTreeNode termNode = new DefaultMutableTreeNode("term");
		node.add(termNode);

		if (term(termNode))
			count++;
		while (true) {
			if (moveP() == false) {
				return count > 0;
			} else if (getToken().getToken().equals("+")
					|| getToken().getToken().equals("-")) {
				DefaultMutableTreeNode fNode = new DefaultMutableTreeNode(
						getToken().getToken());
				node.add(fNode);
				DefaultMutableTreeNode term2Node = new DefaultMutableTreeNode(
						"term");
				node.add(term2Node);
				if (term(term2Node) == false) {
					return count > 0;
				} else {
					count++;
					continue;
				}
			} else {
				backP();
				return count > 0;
			}
		}
	}

	/**
	 * �� term=unary[*|/unary]*
	 * 
	 * @param s
	 */
	public boolean term(DefaultMutableTreeNode node) {
		int count = 0;
		DefaultMutableTreeNode unaryNode = new DefaultMutableTreeNode("unary");
		node.add(unaryNode);
		if (unary(unaryNode))
			count++;
		while (true) {

			if (moveP() == false) {
				return count > 0;
			} else if (getToken().getToken().equals("*")
					|| getToken().getToken().equals("/")) {
				DefaultMutableTreeNode fNode = new DefaultMutableTreeNode(
						getToken().getToken());
				node.add(fNode);
				DefaultMutableTreeNode unary2Node = new DefaultMutableTreeNode(
						"unary");
				node.add(unary2Node);
				if (unary(unary2Node)) {
					count++;
					continue;
				}

			} else {
				backP();
				return count > 0;
			}
		}
	}

	/**
	 * ���� unary=[-]element
	 * 
	 * @param s
	 */
	public boolean unary(DefaultMutableTreeNode node) {
		int count = 0;
		while (true) {

			if (moveP() == false) {
				return count > 0;
			} else if (getToken().getToken().equals("-")) {
				DefaultMutableTreeNode fNode = new DefaultMutableTreeNode(
						getToken().getToken());
				node.add(fNode);
				if (element(node))
					count++;
				return count > 0;
			} else {
				backP();
				if (element(node))
					count++;
				return count > 0;
			}
		}
	}

	/**
	 * Ԫ�أ�int��real��ident�������ʽ�� element=int/real/ident/(expression)
	 * 
	 * @param s
	 */
	public boolean element(DefaultMutableTreeNode node) {
		int count = 0;
		if (moveP() == false) {

			return false;
		} 
		if (getToken().getTypeString().equalsIgnoreCase("INTERGER")) {
			count++;
			DefaultMutableTreeNode fNode = new DefaultMutableTreeNode(
					getToken().getToken());
			node.add(fNode);
			return true;
		} else if (getToken().getTypeString().equalsIgnoreCase("REAL")) {
			count++;
			DefaultMutableTreeNode fNode = new DefaultMutableTreeNode(
					getToken().getToken());
			node.add(fNode);
			return true;
		} else if (getToken().getTypeString().equalsIgnoreCase("IDENT")) {
			CMMToken idenToken = getToken();
			count++;
			DefaultMutableTreeNode identNode=new DefaultMutableTreeNode(getToken().getToken());
			CMMToken identToken=getToken();
			useIdent(node,identNode,identToken);
			if (moveP() == false) {
				useVar(idenToken, false);
				return true;
			} else {
				CMMToken cmmToken1 = getToken();
				// �ǲ�������
				if (getToken().getToken().equals("[")) {
					useVar(idenToken, true);
					backP();
					// ʶ������
					node.remove(identNode);
					DefaultMutableTreeNode arrayNode = new DefaultMutableTreeNode(
							"array");
					node.add(arrayNode);
					arrayNode.add(identNode);
					useArray(arrayNode, getToken());
					return true;
				} else {
					useVar(idenToken, false);
					backP();
					return true;
				}
			}
		} else if (getToken().getToken().equals("(")) {
			expression(node);
			// ��ƥ��� ��
			if (moveP() == false) {
				error(getToken(), "ȱ�� )");
				return true;
			} else if (getToken().getToken().equals(")")) {

				return true;
			} else {
				error(getToken(), "����ȱ�� )");
				return true;
			}

		} else {
			return false;
		}
	}

	// ������صģ�ʶ��Ƭ�Σ���������
	private void createArray(DefaultMutableTreeNode node, CMMToken token) {
		if (moveP() == false) {
			error(getToken(), "Ӧ����'['");
			return;
		}
		if (!(getToken().getToken().equals("["))) {
			error(getToken(), "Ӧ����'['");
			return;
		}
		if (moveP() == false) {
			error(getToken(), "ȱ��һ�����ͳ���");
			return;
		}
		if (!(getToken().getTypeString().equalsIgnoreCase("INTERGER"))) {
			error(getToken(), "Ӧ����һ�����ͳ���");
			return;
		}
		pushVar(token, 1, true, Integer.parseInt(getToken().getToken()),-1,null,null);
		if (moveP() == false) {
			error(getToken(), "ȱ��һ��']'");
			return;
		}
		if (!(getToken().getToken().equals("]"))) {

			error(getToken(), "Ӧ����һ��']'");
			return;
		}
		DefaultMutableTreeNode aNode = new DefaultMutableTreeNode(token
				.getToken());
		DefaultMutableTreeNode bNode = new DefaultMutableTreeNode(
				cmmTokens[p - 1].getToken());
		node.add(aNode);
		node.add(bNode);

		if (moveP() == false) {
			error(getToken(), "ȱ��һ��';'");
			return;
		} else if (getToken().getToken().equals(";")) {

			return;
		} else {
			error(getToken(), "Ӧ���ǣ�';'");
		}

	}
	
	/**
	 * ʹ�ñ�ʶ����������������
	 */
	public void useIdent(DefaultMutableTreeNode node,DefaultMutableTreeNode identNode,CMMToken identToken){

		node.add(identNode);
		VarObject vo=null;
		for(int i=0;i<stack.size();i++){
			if(stack.get(i).getVarString().equals(identToken.getToken())) vo=stack.get(i);
		}
		if(vo==null) return;
		if(vo.getType()<10) return;
		
		if(vo.getType()>=10){	//��������
			if(moveP()==false){
				error(getToken(), "ȱ��'('");
				return;
			}
			if(!getToken().getToken().equals("(")){
				error(getToken(), "Ӧ����'('");
				return;
			}
			argConvey(identNode, vo);
			if(moveP()==false){
				error(getToken(), "ȱ��')'");
				return;
			}
			if(!getToken().getToken().equals(")")){
				error(getToken(), "Ӧ����')'");
				return;
			}
		}
	}
	
	/**
	 * ���ݲ�������
	 * @param node
	 * @param token
	 */
	private void argConvey(DefaultMutableTreeNode node,VarObject vo){
		
		int argNum=vo.getArgNum();
		Vector<VarObject> argList=vo.getArgListVector();
		if(argNum<=0) return;
		for(int i=0;i<argList.size();i++){
			expression(node);
			if(moveP()==false){
				error(getToken(), "ȱ��')'");
				return;
			}
			if(getToken().getToken().equals(",")) continue;
			if(getToken().getToken().equals(")")){
				backP();
				return;
			}
		}
	}

	// ʹ������
	private void useArray(DefaultMutableTreeNode node, CMMToken token) {
		if (moveP() == false) {
			error(getToken(), "Ӧ����'['");
			return;
		}
		if (!(getToken().getToken().equals("["))) {
			error(getToken(), "Ӧ����'['");
			return;
		}
		if (moveP() == false) {
			error(getToken(), "ȱ��һ�����ʽ");
			return;
		}
		backP();
		if (!(expression(node))) {

			error(getToken(), "Ӧ����һ�����ͳ���");
			return;
		}
		if (moveP() == false) {
			error(getToken(), "ȱ��һ��']'");
			return;
		} else if (getToken().getToken().equals("]")) {
			// node.add(new DefaultMutableTreeNode("]"));
			return;
		} else {
			error(getToken(), "Ӧ����һ��']'");
			return;
		}

	}

	/**
	 * �����ļ��
	 * 
	 * @param token
	 * @param message
	 */
	/**
	 * ���һ������,0,���ǣ�1�����ͣ�2��ʵ��
	 */
	public void pushVar(CMMToken token, int type, boolean isArray, int arraySize,int argNum,Vector<VarObject> argList,DefaultMutableTreeNode node) {
		VarObject vo = new VarObject();
		vo.setVarString(token.getToken());
		vo.setType(type);
		vo.setArray(isArray);
		vo.setArraySize(arraySize);
		vo.setArgNum(argNum);
		vo.setArgListVector(argList);
		vo.setNode(node);
		if (stack.hasVo(vo)) {
			error(token, "���������ظ���");
		} else {
			stack.push(vo);
		}
	}

	/**
	 * ʹ��һ������
	 * 
	 * @param token
	 * @param message
	 */
	public void useVar(CMMToken token, boolean isArray) {
		VarObject vo = new VarObject();
		vo.setVarString(token.getToken());
		vo.setArray(isArray);
		if (stack.hasVo(vo)) {
			return;
		} else {
			error(token, "δ�����ı�����");
		}
	}

	/**
	 * ����������ֱ��{,���������������
	 * 
	 * @param token
	 * @param message
	 */
	public void popVar() {
		stack.pop();
	}

	/**
	 * ����һ������
	 * 
	 */
	public void popOne() {
		stack.popOne();
	}

	/**
	 * ��ʾ�﷨��
	 * 
	 * @param token
	 * @param message
	 */
	public void showGramTree() {
		JFrame frame = new JFrame("�﷨��");
		JScrollPane jScrollPane = new JScrollPane(tree);
		frame.add(jScrollPane);
		frame.setSize(800, 600);
		frame.setVisible(true);
		//		
	}

	//����﷨��
	public DefaultTreeModel getTreeModel() {
		return treeModel;
	}

	private String getNodeName() {
		nodeNum++;
		return "node" + nodeNum;

	}

	// ����1
	private void error(CMMToken token, String message) {
		hasError = true;
		outputLine("����'" + token.getToken() + "'����" + token.getX() + "��,��"
				+ token.getY() + "��");
		outputLine("	" + message);
		errorTokens.add(token);
	}

	// ����2
	private void error(CMMToken token) {
		hasError = true;
		outputLine("����'" + token.getToken() + "'����" + token.getX() + "��,��"
				+ token.getY() + "��");
		errorTokens.add(token);
	}

	// ָ���ƶ����Զ�����ע��
	private boolean moveP() {
		if (p >= size - 1)
			return false;
		else {
			p++;
			while (true) {
				if (getToken().getTypeString().equalsIgnoreCase("COMMENT")) {
					if (p < size - 1) {
						p++;
						continue;
					} else
						break;
				} else
					break;
			}
			return true;
		}
	}
	//ע�⣬�������һֱ���ص�-1
	private boolean backP() {
		if (p <0)	
			return false;
		else {
			p--;
			while (true) {
				if (p>=0&&getToken().getTypeString().equalsIgnoreCase("COMMENT")) {
					if (p >=0) {
						p--;
						continue;
					} else
						break;
				} else
					break;
			}
			return true;
		}
	}

	//���ص�ǰָ�����token
	private CMMToken getToken() {
		return cmmTokens[p];
	}

	//���,�����ڴ˷�����ض������
	public void outputLine(String s) {
		outputString += (s + "\n");
	}

	public void output(String s) {
		outputString += (s);
	}

	public String getOutputString() {
		return outputString;
	}

	public boolean hasError() {
		return hasError;
	}

	public ArrayList<CMMToken> getErrorTokens() {
		return errorTokens;
	}
}
