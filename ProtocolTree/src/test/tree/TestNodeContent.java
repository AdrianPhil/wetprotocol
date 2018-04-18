package test.tree;

public class TestNodeContent{


	String field1;
	String field2="DUMMY STRING";
	TestNodeContent(String s){
		field1 =s;
	}
	public String getString1() {
		return field1;
	}

	public void setString1(String string1) {
		this.field1 = string1;
	}
	public String getString2() {
		return field2;
	}

	public void setString2(String string2) {
		this.field2 = string2;
	}
	@Override
	public String toString() {
		return "NodeContent [field1=" + field1 + ", field2=" + field2 + "]";
	}
}
