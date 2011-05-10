package com.ixonos.eservices.koku.kks.mock;

import java.util.ArrayList;
import java.util.List;

/**
 * Mock model for KKS
 * 
 * @author tuomape
 *
 */
public class KKSModel {

	
	private List<Child> childs;
	private List<KKSEntry> entries;
	
	public KKSModel() {
		childs = new ArrayList<Child>();
		entries = new ArrayList<KKSEntry>();		
	}
	
	public void addChild( Child c ) {
		childs.add(c);
	}
	
	public void addEntry( KKSEntry e ) {
		entries.add(e);
	}

	public List<Child> getChilds() {
		return childs;
	}

	public void setChilds(List<Child> childs) {
		this.childs = childs;
	}

	public List<KKSEntry> getEntries() {
		return entries;
	}

	public void setEntries(List<KKSEntry> entries) {
		this.entries = entries;
	}
	
	
}
