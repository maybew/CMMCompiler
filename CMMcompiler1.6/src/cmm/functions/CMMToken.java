package cmm.functions;
/*
 * �ʷ�����
 */
public class CMMToken {
	private String tokenString="";
	private int type=0;	//0 ����1 ���ͳ�����2 ʵ������ 3 ��ʶ��,4 �����ַ� 5,�ؼ���,6 ע�ͣ� 7���ַ���
	private int x=0;	//�кţ���1��ʼ
	private int y=0;	//�кţ���1��ʼ
	private int charIndex=0;	//
	
	public CMMToken(){
		
	}
	
	public CMMToken(String tokenString,int type,int x,int y,int charIndex){
		this.tokenString=tokenString;
		this.type=type;
		this.x=x;
		this.y=y;
		this.charIndex=charIndex;
	}

	public String getToken() {
		return tokenString;
	}

	public void setToken(String token) {
		this.tokenString = token;
	}

	public int getType() {
		return type;
	}
	
	public String getTypeString() {
		switch(type){
		case 0:return "ERROR";
		case 1:return "INTERGER";
		case 2:return "REAL";
		case 3:return "IDENT";
		case 4:return "SPECIALCHAR";
		case 5:return "KEYWORD";
		case 6:return "COMMENT";
		default:return "EORRO";
		}
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getCharIndex() {
		return charIndex;
	}

	public void setCharIndex(int charIndex) {
		this.charIndex = charIndex;
	}
}
