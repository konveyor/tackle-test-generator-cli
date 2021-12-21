import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RegressionTest0 {

    public static boolean debug = false;

    @Test
    public void test01() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test01");
        irs.Employer employer0 = irs.BusinessProcess.test_employer2();
        employer0.setEmployerName("hi!");
    }

    @Test
    public void test02() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test02");
        irs.BusinessProcess.test_irs_salary_set_map();
    }

    @Test
    public void test03() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test03");
        irs.Employee employee0 = new irs.Employee();
        employee0.setEmployeeId(1);
        java.lang.String str3 = employee0.getLevel();
    }

    @Test
    public void test04() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test04");
        irs.BusinessProcess.test_salary();
    }

    @Test
    public void test05() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test05");
        int int0 = irs.BusinessProcess.test_employer3();
    }

    @Test
    public void test06() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test06");
        irs.IRS iRS0 = new irs.IRS();
        java.util.Set<irs.Salary> salarySet2 = iRS0.getSalarySet((int) (short) 1);
        java.util.Set<irs.Salary> salarySet4 = iRS0.getSalarySet((int) (byte) 0);
    }

    @Test
    public void test07() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test07");
        irs.Employer employer0 = irs.BusinessProcess.test_employer2();
        java.lang.String str1 = employer0.getEmployerName();
    }

    @Test
    public void test08() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test08");
        irs.BusinessProcess.test_employee();
    }

    @Test
    public void test09() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test09");
        irs.Employee employee0 = new irs.Employee();
        java.lang.String str1 = employee0.getName();
        java.lang.String str2 = employee0.getLevel();
    }

    @Test
    public void test10() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test10");
        irs.BusinessProcess businessProcess0 = new irs.BusinessProcess();
        java.util.List<irs.Employer> employerList1 = businessProcess0.getAllEmployers();
        java.util.List<irs.Employer> employerList2 = businessProcess0.getAllEmployers();
    }

    @Test
    public void test11() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test11");
        irs.Employer employer0 = irs.BusinessProcess.test_employer2();
        employer0.setEmployerName("");
        irs.Employee employee3 = new irs.Employee();
        java.lang.String str4 = employee3.getName();
        irs.Employee[] employeeArray5 = new irs.Employee[] { employee3 };
        java.util.ArrayList<irs.Employee> employeeList6 = new java.util.ArrayList<irs.Employee>();
        boolean boolean7 = java.util.Collections.addAll((java.util.Collection<irs.Employee>) employeeList6, employeeArray5);
        employer0.setEmployees((java.util.List<irs.Employee>) employeeList6);
    }

    @Test
    public void test12() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test12");
        irs.IRS iRS0 = new irs.IRS();
        java.util.List<irs.Salary> salaryList1 = iRS0.getSalaryList();
        int int2 = iRS0.getYear();
    }

    @Test
    public void test13() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test13");
        irs.Employee employee0 = new irs.Employee();
        employee0.setEmployeeId(1);
        employee0.setEmployeeId((int) ' ');
    }

    @Test
    public void test14() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test14");
        irs.Employer employer0 = irs.BusinessProcess.test_employer2();
        employer0.setEmployerName("");
        employer0.setEmployerName("hi!");
        java.lang.String str5 = employer0.getEmployerName();
    }

    @Test
    public void test15() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test15");
        irs.Salary salary0 = new irs.Salary();
        salary0.setEmployerId((-1));
    }

    @Test
    public void test16() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test16");
        irs.BusinessProcess businessProcess0 = new irs.BusinessProcess();
        java.util.List<irs.Employer> employerList1 = businessProcess0.getAllEmployers();
        businessProcess0.test_2();
        businessProcess0.test_2();
    }

    @Test
    public void test17() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test17");
        irs.BusinessProcess.test_irs();
    }

    @Test
    public void test18() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test18");
        irs.IRS iRS0 = new irs.IRS();
        java.util.Set<irs.Salary> salarySet2 = iRS0.getSalarySet((int) (short) 1);
        java.util.Map<java.lang.Integer, java.util.Set<irs.Salary>> intMap3 = iRS0.getAllSalarySets();
    }

    @Test
    public void test19() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test19");
        irs.Salary salary0 = new irs.Salary();
        double double1 = salary0.getSalary();
        double double2 = salary0.getSalary();
        salary0.setSalary(0.0d);
    }

    @Test
    public void test20() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test20");
        irs.IRS iRS0 = new irs.IRS();
        iRS0.setYear((int) '4');
        int int3 = iRS0.getYear();
    }

    @Test
    public void test21() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test21");
        irs.Salary salary3 = new irs.Salary((int) '#', 0, (-1.0d));
    }

    @Test
    public void test22() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test22");
        irs.Employee employee0 = new irs.Employee();
        employee0.setEmployeeId(1);
        java.lang.String str3 = employee0.getName();
        employee0.setLevel("");
        double double6 = employee0.getRate();
        employee0.setEmployeeId(0);
    }

    @Test
    public void test23() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test23");
        irs.Salary salary0 = new irs.Salary();
        double double1 = salary0.getSalary();
        double double2 = salary0.getSalary();
        salary0.setEmployeeId((int) ' ');
        salary0.setEmployerId((int) ' ');
        int int7 = salary0.getEmployeeId();
        salary0.setSalary((double) (byte) 100);
    }

    @Test
    public void test24() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test24");
        irs.Salary salary0 = new irs.Salary();
        double double1 = salary0.getSalary();
        double double2 = salary0.getSalary();
        salary0.setEmployerId((int) (short) 0);
    }

    @Test
    public void test25() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test25");
        irs.IRS iRS0 = new irs.IRS();
        java.util.Map<java.lang.Integer, java.util.Set<irs.Salary>> intMap1 = null;
        iRS0.setAllSalarySets(intMap1);
        java.util.List<irs.Salary> salaryList3 = iRS0.getSalaryList();
        irs.Salary salary4 = new irs.Salary();
        double double5 = salary4.getSalary();
        double double6 = salary4.getSalary();
        salary4.setEmployeeId((int) ' ');
        salary4.setEmployerId((int) ' ');
        irs.Salary salary14 = new irs.Salary((int) '#', 0, (-1.0d));
        irs.Salary salary15 = new irs.Salary();
        double double16 = salary15.getSalary();
        double double17 = salary15.getSalary();
        salary15.setSalary(0.0d);
        irs.Salary salary20 = new irs.Salary();
        double double21 = salary20.getSalary();
        double double22 = salary20.getSalary();
        salary20.setEmployeeId((int) ' ');
        salary20.setEmployerId((int) ' ');
        int int27 = salary20.getEmployeeId();
        irs.Salary salary28 = new irs.Salary();
        double double29 = salary28.getSalary();
        double double30 = salary28.getSalary();
        irs.Salary salary31 = new irs.Salary();
        double double32 = salary31.getSalary();
        double double33 = salary31.getSalary();
        irs.Salary[] salaryArray34 = new irs.Salary[] { salary4, salary14, salary15, salary20, salary28, salary31 };
        java.util.ArrayList<irs.Salary> salaryList35 = new java.util.ArrayList<irs.Salary>();
        boolean boolean36 = java.util.Collections.addAll((java.util.Collection<irs.Salary>) salaryList35, salaryArray34);
        // The following exception was thrown during execution in test generation
        try {
            iRS0.setSalaryList((java.util.List<irs.Salary>) salaryList35);
            org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException; message: null");
        } catch (java.lang.NullPointerException e) {
            // Expected exception.
        }
    }

    @Test
    public void test26() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test26");
        irs.Salary salary3 = new irs.Salary(1, (int) 'a', (double) (byte) -1);
    }

    @Test
    public void test27() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test27");
        irs.Employer employer0 = irs.BusinessProcess.test_employer2();
        employer0.setEmployerName("");
        irs.Employee employee3 = new irs.Employee();
        employee3.setEmployeeId(1);
        java.lang.String str6 = employee3.getName();
        irs.Employee employee7 = new irs.Employee();
        employee7.setEmployeeId(1);
        java.lang.String str10 = employee7.getLevel();
        irs.Employee employee11 = new irs.Employee();
        employee11.setEmployeeId(1);
        java.lang.String str14 = employee11.getName();
        employee11.setLevel("");
        double double17 = employee11.getRate();
        employee11.setEmployeeId(0);
        irs.Employee employee20 = new irs.Employee();
        employee20.setEmployeeId(1);
        irs.Employee employee23 = new irs.Employee();
        employee23.setEmployeeId(1);
        java.lang.String str26 = employee23.getLevel();
        irs.Employee[] employeeArray27 = new irs.Employee[] { employee3, employee7, employee11, employee20, employee23 };
        java.util.ArrayList<irs.Employee> employeeList28 = new java.util.ArrayList<irs.Employee>();
        boolean boolean29 = java.util.Collections.addAll((java.util.Collection<irs.Employee>) employeeList28, employeeArray27);
        employer0.setEmployees((java.util.List<irs.Employee>) employeeList28);
        employer0.setEmployerName("");
    }

    @Test
    public void test28() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test28");
        irs.Employee employee0 = new irs.Employee();
        double double1 = employee0.getRate();
        employee0.setLevel("hi!");
        employee0.setEmployeeId((int) (byte) 0);
        employee0.setName("hi!");
        double double8 = employee0.getRate();
    }

    @Test
    public void test29() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test29");
        irs.Employer employer0 = irs.BusinessProcess.test_employer2();
        java.util.List<irs.Employee> employeeList1 = employer0.getEmployees();
        java.lang.String str2 = employer0.getEmployerName();
        irs.Employer employer3 = irs.BusinessProcess.test_employer2();
        employer3.setEmployerName("");
        irs.Employee employee6 = new irs.Employee();
        employee6.setEmployeeId(1);
        java.lang.String str9 = employee6.getName();
        irs.Employee employee10 = new irs.Employee();
        employee10.setEmployeeId(1);
        java.lang.String str13 = employee10.getLevel();
        irs.Employee employee14 = new irs.Employee();
        employee14.setEmployeeId(1);
        java.lang.String str17 = employee14.getName();
        employee14.setLevel("");
        double double20 = employee14.getRate();
        employee14.setEmployeeId(0);
        irs.Employee employee23 = new irs.Employee();
        employee23.setEmployeeId(1);
        irs.Employee employee26 = new irs.Employee();
        employee26.setEmployeeId(1);
        java.lang.String str29 = employee26.getLevel();
        irs.Employee[] employeeArray30 = new irs.Employee[] { employee6, employee10, employee14, employee23, employee26 };
        java.util.ArrayList<irs.Employee> employeeList31 = new java.util.ArrayList<irs.Employee>();
        boolean boolean32 = java.util.Collections.addAll((java.util.Collection<irs.Employee>) employeeList31, employeeArray30);
        employer3.setEmployees((java.util.List<irs.Employee>) employeeList31);
        employer0.setEmployees((java.util.List<irs.Employee>) employeeList31);
    }
}

