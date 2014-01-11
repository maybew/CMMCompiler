package cmm.functions;

import javax.print.attribute.standard.OutputDeviceAssigned;

/*
 * ʵ�ֶ�ջ����
 */
public class Stack {
	private int size=0;	//��ջ��С
	private VarObject[] stack=null;	//��ջ��������ʵ�֣��洢����cmmtoken
	private int p=-1;	//��ջָ��,-1��ʾû��
	
	public Stack(){
		this.size=100;	//Ĭ��100
		initialize();
	}
	
	public Stack(int size){
		this.size=size;
		initialize();
	}
	
	//��ʼ��
	private void initialize(){
		this.stack=new VarObject[size];
		p=-1;
	}
	
	//������ֱ��{
	public void pop(){
		if(p==-1) return;
		else{
			while(true){
				System.out.println("pop:"+stack[p].getVarString());
				if(stack[p].getVarString().equals("{")) {
					p--;
					break;
				}
				p--;
				if(p<0) break;
			}
		}
	}
	//������һ��
	public void popOne(){
		if(p==-1) return;
		p--;
	}
	
	//ѹ��
	public boolean push(VarObject vo){
		if(p>=size) return false;
		p++;
		stack[p]=vo;
		System.out.println("push:"+stack[p].getVarString());
		return true;
	}
	
	//��⵱ǰstack���Ƿ���ĳ��vo,�����{
	public boolean hasVo(VarObject vo){
		for(int i=0;i<=p;i++){
			if(stack[i].getVarString().equals("{")) continue;
			if(vo.isArray()){
				if(stack[i].getVarString().equals(vo.getVarString())&&stack[i].isArray()) return true;
			}else{
				if(stack[i].getVarString().equals(vo.getVarString())) return true;
			}
		}
		return false;
	}
	
	//
	public VarObject get(int index){
		if(index>p) return null;
		return stack[index];
	}
	
	public int size(){
		return p+1;
	}
}
