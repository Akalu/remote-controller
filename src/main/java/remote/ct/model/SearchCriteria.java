package remote.ct.model;

import java.io.Serializable;

public class SearchCriteria implements Serializable {
	private static final long serialVersionUID = -4342101257158966784L;
	private String criteria;
	private String signature;
	
	public SearchCriteria() {
	}
	
	public SearchCriteria(String criteria, String signature) {
		this.criteria = criteria;
		this.signature = signature;
	}

	public String getCriteria() {
		return criteria;
	}
	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	
	

}
