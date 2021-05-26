package irs;

import com.ibm.cardinal.util.KluInterface;

import java.util.*;

public class IRS implements KluInterface {
    //Cardinal generated attributes
    private String klu__referenceID = "";

    //Cardinal generated method
    public String getKlu__referenceID() {
        return klu__referenceID;
    }

    //Cardinal generated method
    public void setKlu__referenceID(String refID) {
        klu__referenceID = refID;
    }




	private int yearr;
	private List<Salary> salaryList;
	private Map<Integer, Set<Salary>> employerSalarySet = new HashMap<>();

	public int getYear() {
		return yearr;
	}

	public void setYear(int yearr) {
		this.yearr = yearr;
	}
	
	public List<Salary> getSalaryList() {
		return salaryList;
	}

	public void setSalaryList(List<Salary> salaryList) {
		this.salaryList = salaryList;
		for (Salary sal : salaryList) {
			int empId = sal.getEmployerId();
			if (!this.employerSalarySet.containsKey(empId)) {
				this.employerSalarySet.put(empId, new HashSet<Salary>());
			}
			this.employerSalarySet.get(empId).add(sal);
		}
		System.out.println("Employee salary set: "+this.employerSalarySet);
	}

	public Salary getSalaryfromIRS(int employeeId) {
		for(Salary salary : this.salaryList) {
			if (salary.getEmployeeId() == employeeId) {
				return salary;
			}
		}
		return null;
	}

	public Set<Salary> getSalarySet(int employeeId) {
		return this.employerSalarySet.get(employeeId);
	}

	public Map<Integer, Set<Salary>> getAllSalarySets() {
		return this.employerSalarySet;
	}
	
	public void setAllSalarySets(Map<Integer, Set<Salary>> employerSalarySet) {
		this.employerSalarySet = employerSalarySet;
	}
}

