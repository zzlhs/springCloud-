package com.zzlhs.springcloud.entities;

import java.io.Serializable;

public class Dept implements Serializable{

	private Long deptnc;
	private String dname;
	private String db_source;
	
	
	public Dept() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Dept(String dname) {
		super();
		this.dname = dname;
	}

	public Dept(Long deptnc, String dname, String db_source) {
		super();
		this.deptnc = deptnc;
		this.dname = dname;
		this.db_source = db_source;
	}

	public Long getDeptnc() {
		return deptnc;
	}

	public void setDeptnc(Long deptnc) {
		this.deptnc = deptnc;
	}

	public String getDname() {
		return dname;
	}

	public void setDname(String dname) {
		this.dname = dname;
	}

	public String getDb_source() {
		return db_source;
	}

	public void setDb_source(String db_source) {
		this.db_source = db_source;
	}

	@Override
	public String toString() {
		return "Dept [deptnc=" + deptnc + ", dname=" + dname + ", db_source=" + db_source + "]";
	}
}
