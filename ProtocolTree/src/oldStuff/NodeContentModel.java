package oldStuff;

public class NodeContentModel {


	String local = "dummy local";
	String value = "DUMMY STRING";
	String range = "dummy range";
	String domain = "DUMMY domain";

	NodeContentModel(String local, String value, String range, String domain) {
		this.local = local;
		this.value = value;
		this.range = range;
		this.domain = domain;
	}


	public String getLocal() {
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getRange() {
		return range;
	}

	public void setRange(String range) {
		this.range = range;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}


}
