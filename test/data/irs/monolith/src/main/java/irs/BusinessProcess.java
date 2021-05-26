package irs;

import java.util.*;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BusinessProcess {

	private static final Logger logger;
	static {
		System.setProperty("java.util.logging.SimpleFormatter.format",
				"[%1$tF %1$tT] [%4$-7s] [%2$s] %5$s %n");
		logger = Logger.getLogger(BusinessProcess.class.getSimpleName());
		logger.addHandler(new ConsoleHandler());
		logger.setLevel(Level.INFO);
	}

	public List<Employer> getAllEmployers() {
		List<Employer> employerList = new ArrayList<Employer>();
		System.out.println("initialize employee list: ");
		//ClusterObjectManagerEmployer.printTable();
		Employee employee1 = new Employee(11, "Alice", 40, 32);
		System.out.println("1st employee created: ");
		System.out.println("1st employee rate: "+String.valueOf(employee1.getRate()));
		//ClusterObjectManagerEmployer.printTable();
		Employee employee2 = new Employee(22, "Bob", 40, 45);
		System.out.println("2nd employee created: ");
		System.out.println("2nd employee rate: "+String.valueOf(employee2.getRate()));
		System.out.println("Employee static level is: "+Employee.getLevel());
		//ClusterObjectManagerEmployer.printTable();
		
		List<Employee> employeeList = new ArrayList<Employee>();
		employeeList.add(employee1);
		employeeList.add(employee2);
		
		Employer employer = new Employer(1, "ACME", employeeList);
		
		Employee.setLevel("ABC");
		
		Employee employee3 = new Employee(45, "Charles", 40, 47);
		System.out.println("3rd employee rate: "+String.valueOf(employee3.getRate()));
		Employee employee4 = new Employee(25, "Dana", 40, 50);
		System.out.println("4th employee rate: "+String.valueOf(employee4.getRate()));
		System.out.println("Employee static level is: "+Employee.getLevel());
		
		List<Employee> employeeList1 = new ArrayList<Employee>();
		employeeList1.add(employee3);
		employeeList1.add(employee4);
		
		Employer employer1 = new Employer(2, "EMCA", employeeList);
		
		employerList.add(employer);
		employerList.add(employer1);
		
		return employerList;
	}
	
	public void genSalarySlip(IRS irsInst) {
		List<Employer> employerList = getAllEmployers();
		System.out.println("after getAllEmployers(): "+irsInst.getYear());
		List<Salary> salaryList = irsInst.getSalaryList();
		System.out.println("after getSalaryList(): "+irsInst.getYear());
		
		for(Employer employer: employerList){
			int employerId = employer.getEmployerId();
			List<Employee> employeeList = employer.getEmployees();
			System.out.println("in employer loop, getEmployees(): "+irsInst.getYear());
			for(Employee employee:employeeList) {
				int employeeId = employee.getEmployeeId();
				Salary instSalary = new Salary();
				System.out.println("in employee list loop: "+irsInst.getYear());
				instSalary.setEmployerId(employerId);
				instSalary.setEmployeeId(employeeId);
				instSalary.setSalary(employee.getRate()*employee.getHours());
				salaryList.add(instSalary);
			}			
		}
		
		System.out.println("before setSalaryList: "+irsInst.getYear());
		irsInst.setSalaryList(salaryList);
		System.out.println("after setSalaryList: "+irsInst.getYear());
		
	}
	
	public static void main(String[] args) {
		BusinessProcess instBP = new BusinessProcess();
		instBP.test_2();
	}
	
	public static void test_irs() {
		IRS irsInst = new IRS();
		irsInst.setYear(100);
		irsInst.setSalaryList(new ArrayList<Salary>());
		irsInst.getSalaryList();
	}

	public static void test_employee() {
		logger.info("Constructing Employee");
		Employee employee = new Employee();
		employee.setHours(60);
		System.out.println("Employee hours: "+employee.getHours());
	}

	public static List<Employee> test_employer() {
		List<Employee> employeeList = Arrays.asList(
				new Employee(22, "Bob", 40, 45),
				new Employee(11, "Alice", 40, 32)
		);
		Employer employer = new Employer(1, "ACME", employeeList);
		System.out.println("Created employer object: "+employer);
		employer.setEmployerId(99);
		System.out.println("Employer ID: "+employer.getEmployerId());
		return employer.getEmployees();
	}

	public static Employer test_employer2() {
		List<Employee> employeeList = Arrays.asList(
			new Employee(22, "Bob", 40, 45),
			new Employee(11, "Alice", 40, 32)
		);
		Employer employer = new Employer();
		employer.setEmployerAttributes(1, "ACME", employeeList);
		System.out.println("Employer ID: "+employer.getEmployerId());
		System.out.println("Employer name: "+employer.getEmployerName());
		return employer;
	}

	public static int test_employer3() {
		Employer employer = new Employer();
		employer.setEmployerId(1);
		employer.setEmployerName("ACME");
		employer.addEmployees(
			new Employee(22, "Bob", 40, 45),
			new Employee(11, "Alice", 40, 32),
			new Employee(33, "Jane", 45, 80)
		);
		System.out.println("Employer ID: "+employer.getEmployerId());
		System.out.println("Employer name: "+employer.getEmployerName());
		return employer.getEmployees().size();
	}

	public static void test_salary() {
		Salary salary = new Salary(111, 222, 100000);
		salary.setSalary(110000.00);
		System.out.println(salary.getSalary());
	}

	public static void test_irs_salary_set_map() {
		List<Salary> empSalList = new LinkedList<>();
		int empId = 123;
		empSalList.add(new Salary(empId, 456, 50000));
		empSalList.add(new Salary(empId, 789, 60000));
		empSalList.add(new Salary(999, 789, 60000));
		IRS irs = new IRS();
		irs.setSalaryList(empSalList);
		System.out.println(irs.getSalarySet(empId));
		System.out.println(irs.getAllSalarySets());
	}

	public void test_2(){
		IRS irsInst = new IRS();
		irsInst.setYear(100);
		irsInst.setSalaryList(new ArrayList<Salary>());
		System.out.println("1: "+irsInst.getYear());
		this.genSalarySlip(irsInst);
		
		System.out.println("2: "+irsInst.getYear());
		List<Salary> salaryList = irsInst.getSalaryList();
		System.out.println("3: "+irsInst.getYear());
		System.out.println("In BuisnessProcess, test_2");
		//ClusterObjectManagerEmployer.printTable();
		for(Salary salary:salaryList) {
			System.out.println(String.valueOf(salary.getEmployerId())+","+String.valueOf(salary.getEmployeeId())+","+String.valueOf(salary.getSalary()));
		}
		
		Employee employeeKate = new Employee(33, "Kate", 40, 32);
		salaryList = irsInst.getSalaryList();
		Salary salaryKate = new Salary();
		salaryKate.setEmployerId(1);
		salaryKate.setEmployeeId(employeeKate.getEmployeeId());
		salaryKate.setSalary(employeeKate.getRate()*employeeKate.getHours());
		salaryList.add(salaryKate);
		irsInst.setSalaryList(salaryList);
		System.out.println("In BuisnessProcess, test_2");
		for(Salary salary:salaryList) {
			System.out.println(String.valueOf(salary.getEmployerId())+","+String.valueOf(salary.getEmployeeId())+","+String.valueOf(salary.getSalary()));
		}
		salaryKate = irsInst.getSalaryfromIRS(employeeKate.getEmployeeId());
		System.out.println("Salary for "+employeeKate.getName()+": "+String.valueOf(salaryKate.getSalary()));
	}
	
}
