package cmm.functions;


public class WordParse {
	private String in="";
	private String temp="";
	private int type=0;
	private String outputString="";
	private String[] inLine=new String[1000];		//���д洢
	private int lineCount=0;						//�����˶�����
	private int linePointer=0;						//�к�
	private int charIndex=0;						//�ڼ����ַ�������\r
	private String currentLine="";					//��ǰ�е�����
	private int p=0;								//��ǰ�еĵڼ����ַ�
	private CMMToken[] cmmTokens=new CMMToken[2000];	//��ȷ���ַ�
	private CMMToken[] errorCMMTokens=new CMMToken[1000];	//������ַ�
	private int errorPointer=0;	//�����ַ��Ĵ洢ָ��
	private int tokenPointer=0;
	private boolean hasError=false;	//��û�д���
	
	public WordParse(String in){
		this.in=in;
		for(int i=0;i<100;i++){
		}
	}
	
	//��ʼ����
	public void start(){
		tokenPointer=0;
		errorPointer=0;
		intoLines();	//���в�
		linePointer=0;
		while(linePointer<lineCount){
			outputLine("��"+(linePointer+1)+"��:");
			LineAnalyse();
			linePointer++;
		}
	}
	
	//��ʼ����һ��
	private void LineAnalyse(){
		currentLine=inLine[linePointer];
		p=-1;	//��δ��ʼ
		
		TokenAnalyse();
	}
	
	//��ʼ����һ��token
	private void TokenAnalyse(){
		temp="";
		char c=' ';
		if(moveP()==false) return;
		c=getChar();
		if(isNum(c)) {
			intTest();
		}else if(isLetter(c)||c=='_') {
			identTest();
		}else if(c=='>'||c=='<'||c=='=') {
			doubleSCharTest();
		}else if(isSpecialChar(c)) {
			singleSCharTest();
		}else if(isSpace(c)){
			TokenAnalyse();
		}
		else{
			errorOutput(c);	//���ǰ�ÿһ��������Ϊ��һ���ַ��Ŀ�ʼ������
		}
		TokenAnalyse();	//�����Լ�ѭ��
		
	}
	
	//����
	private void intTest(){
		charIndex=p;
		char c=' ';
		if(moveP()==false) {
			intEnd();
			return;
		}
		c=getChar();
		if(c=='.'){
			realTest();
			return;
		}
		else if(isSpace(c)){
			intEnd();
			return;
		}else if(isSpecialChar(c)){
			backP();
			intEnd();
			return;
		}else if(isNum(c)){
			intTest();
			return;
		}
		else {
			backP();
			intEnd();
			return;
		}
		
	}
	
	private void intEnd(){
		type=1;
		saveToken();
		return;
	}
	
	//ʵ��
	private void realTest(){
		charIndex=p;
		char c=' ';
		if(moveP()==false) {
			realEnd();
			return;
		}
		c=getChar();
		if(isSpace(c)){
			realEnd();
			return;
		}else if(isSpecialChar(c)){
			backP();
			realEnd();
			return;
		}else if(isNum(c)){
			realTest();
			return;
		}
		else {
			backP();
			realEnd();
			return;
		}
		
	}
	
	private void realEnd(){
		type=2;
		saveToken();
		return;
	}
	
	//��ʶ��
	private void identTest(){
		charIndex=p;
		char c=' ';
		if(moveP()==false) {
			identEnd();
			return;
		}
		c=getChar();
		if(isSpace(c)){
			identEnd();
			return;
		}else if(isSpecialChar(c)){
			backP();
			identEnd();
			return;
		}else if(isNum(c)||isLetter(c)||c=='_'){
			identTest();
			return;
		}
		else {
			backP();
			identEnd();
			return;
		}
	}
	
	private void identEnd(){
		keywordTest();
	}
	
	//���������ַ�
	private void singleSCharTest(){
		charIndex=p;
		if(getCurrentChar()=='/'){	//ע��
			char c=' ';
			if(moveP()==false) {
				identEnd();
				return;
			}
			
			c=getChar();
			if(c=='/'){
				singleLineComment();
				return;
			}else if(c=='*'){
				mutiLineComment();
				return;
			}
			else{
				backP();
			}
		}

		singleSCharEnd();
		return;
	}
	private void singleSCharEnd(){
		type=4;
		saveToken();
		return;
	}
	
	//˫�����ַ�
	private void doubleSCharTest(){
		charIndex=p+1;		//ע��˴���ͬ
		char c=' ';
		if(moveP()==false) {
			doubleSCharEnd();
			return;
		}
		c=getChar();
		if(c=='='){
			doubleSCharEnd();
			return;
		}else{
			backP();
			singleSCharEnd();
			return;
		}
	}
	private void doubleSCharEnd(){
		type=4;
		saveToken();
		return;
	}
	
	//�ؼ���
	private void keywordTest(){
		if(isKeyWord(temp)){
			keywordEnd();
			return;
		}
		else{
			type=3;
			saveToken();
			return;
		}
	}
	private void keywordEnd(){
		type=5;
		saveToken();
		return;
	}
	
	//����ע��
	private void singleLineComment(){
		charIndex=inLine[linePointer].length()-1;	//ע������������Ĳ�һ��
		temp+=currentLine.substring(p+1);
		p=currentLine.length();	//ָ��ָ��ĩβ
		type=6;
		saveToken();
		return;
	}
	
	//����ע��
	private void  mutiLineComment(){
		
		type= 6;

		if(currentLine.indexOf("*/")!=-1)	{
			temp+=inLine[linePointer].substring(p+1,currentLine.indexOf("*/")+2);
			p=currentLine.indexOf("*/");
			charIndex=p+1;
			saveToken();
			return;	//ֻ��һ��
		}else{
			temp+=inLine[linePointer].substring(p+1);
		}
		while(true){
			p=0;
			linePointer++;
			if(linePointer>=lineCount){
				outputLine("����ע��δ����");
				
				return;
			}
			currentLine=inLine[linePointer];
			if(currentLine.indexOf("*/")!=-1){
				temp+=currentLine.substring(0,currentLine.indexOf("*/")+2);
				p=currentLine.indexOf("*/")+1;
				charIndex=p;
				saveToken();
				return;
			}else {
				temp+=currentLine;
				continue;
			}
			
		}
		
	}
	
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	//�洢token
	private void saveToken(){
		for(int i=0;i<linePointer;i++){
			charIndex+=inLine[i].length();
		}

		charIndex=charIndex-temp.length()+1;
		
		cmmTokens[tokenPointer]=new CMMToken(temp,type,linePointer+1,p+1,charIndex+1);
		outputLine(cmmTokens[tokenPointer].getToken()+"  :  "+cmmTokens[tokenPointer].getTypeString()
				+"("+cmmTokens[tokenPointer].getCharIndex()+")");

		tokenPointer++;
	}
	
	//�������
	private void errorOutput(char c){
		charIndex=p;
		hasError=true;
		
		outputLine("�����ַ���   '"+c+"'    λ�ã���"+(linePointer+1)+"�У�"+p+"��");

		for(int i=0;i<linePointer;i++){
			charIndex+=inLine[i].length();
		}

		charIndex=charIndex-temp.length()+1;
		errorCMMTokens[errorPointer]=new CMMToken(currentLine.charAt(p)+"",0,linePointer+1,p+1,charIndex+1);
		errorPointer++;
	}
	
	//�������ַ������в�
	private void intoLines(){
		inLine=in.split("\n");
		lineCount=inLine.length;

		outputLine("�ܹ���"+lineCount+"��");
	}
	//ָ�������ƶ�һλ
	private boolean moveP(){
		p++;
		
		if(p>=currentLine.length()) return false;
		else {
			return true;
		}
	}
	//��ȡһ���ַ� ͬʱ�洢,���洢�ո�
	private char getChar(){
		if(!isSpace(currentLine.charAt(p))){
			temp+=currentLine.charAt(p);
		}
		return currentLine.charAt(p);
	}
	//��ȡ��ǰ�ַ�
	private char getCurrentChar(){
		return currentLine.charAt(p);
	}
	
	//ָ�뷵��
	private boolean backP(){
		p--;

		if(p<0) {
			return false;
		}
		else {
			/*
			 * !!!!!!!!!!!
			 * ע��˴�������ֱ��temp��ȥ���һ������Ϊ�п��ܱ���Ҫ�ӵ���һ���ո񣬽�����Թ�ȥ�ˣ�Ȼ��ѿո�ǰ����ַ���ɾ��
			 */
			if(!isSpace(currentLine.charAt(p+1))){
				temp=temp.substring(0, temp.length()-1);
				
			}return true;
		}
	}
	
	
	//���,�����ڴ˷�����ض������
	public void outputLine(String s){
		outputString+=(s+"\n");
	}
	
	public void output(String s){
		outputString+=(s);
	}
	
	//��ȡ���
	public String getOutputString(){
		return outputString;
	}
	
	//�Ƿ�������
	public boolean isNum(char c){
		if(c>=48&&c<=57) return true;
		else return false;
	}
	
	//�Ƿ�����ĸ	a-z 97-122 ,A-Z 65-90
	public boolean isLetter(char c){
		if((c>=97&&c<=122)||(c>=65&&c<=90)) return true;
		else return false;
	}
	
	//�Ƿ��ǿհ׷�
	public boolean isSpace(char c){
		if(c=='\n'||c==' '||c=='\t'||c=='\r') return true;
		else return false;
	}
	
	//�Ƿ��������ַ�
	private boolean isSpecialChar(char c){
		if(c==';'||c=='{'||c=='}'||c=='['||c==']'||c=='('||c==')'||c=='>'||c=='<'||c=='='||c=='+'||c=='-'||c=='*'||c=='/'||c==',')
			return true;
		else return false;
	}
	
	//�Ƿ��Ǳ�ʶ��
	private boolean isKeyWord(String s){
		if(s.equals("if")||s.equals("else")||s.equals("for")||s.equals("while")||s.equals("int")||s.equals("real")||s.equals("read")||s.equals("write")||s.equals("void")||s.equals("return")) {
			return true;
		}
		else {
			return false;
		}
	}

	public CMMToken[] getCmmTokens() {
		return cmmTokens;
	}
	
	public int getTokenAmount(){
		return tokenPointer;
	}

	public CMMToken[] getErrorCMMTokens() {
		return errorCMMTokens;
	}
	public int getErrorTokenAmount(){
		return errorPointer;
	}
	
	public boolean hasError(){
		return hasError;
	}
}
