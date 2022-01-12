import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class irs_BusinessProcess0 {

    public static boolean debug = false;

    @Test
    public void test01() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "irs_BusinessProcess0.test01");
        irs.BusinessProcess.test_employee();
    }

    @Test
    public void test02() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "irs_BusinessProcess0.test02");
        irs.BusinessProcess.test_salary();
    }

    @Test
    public void test03() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "irs_BusinessProcess0.test03");
        irs.BusinessProcess.test_irs_salary_set_map();
    }

    @Test
    public void test04() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "irs_BusinessProcess0.test04");
        java.util.List<irs.Employee> employeeList0 = irs.BusinessProcess.test_employer();
        java.lang.Class<?> wildcardClass1 = employeeList0.getClass();
    }

    @Test
    public void test05() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "irs_BusinessProcess0.test05");
        irs.BusinessProcess.test_irs();
    }

    @Test
    public void test06() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "irs_BusinessProcess0.test06");
        int int0 = irs.BusinessProcess.test_employer3();
    }

    @Test
    public void test07() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "irs_BusinessProcess0.test07");
        irs.BusinessProcess businessProcess0 = new irs.BusinessProcess();
        irs.IRS iRS1 = null;
        // The following exception was thrown during execution in test generation
        try {
            businessProcess0.genSalarySlip(iRS1);
            org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException; message: null");
        } catch (java.lang.NullPointerException e) {
            // Expected exception.
        }
    }

    @Test
    public void test08() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "irs_BusinessProcess0.test08");
        irs.Employer employer0 = irs.BusinessProcess.test_employer2();
    }

    @Test
    public void test09() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "irs_BusinessProcess0.test09");
        irs.BusinessProcess businessProcess0 = new irs.BusinessProcess();
        businessProcess0.test_2();
        businessProcess0.test_2();
        irs.IRS iRS3 = null;
        // The following exception was thrown during execution in test generation
        try {
            businessProcess0.genSalarySlip(iRS3);
            org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException; message: null");
        } catch (java.lang.NullPointerException e) {
            // Expected exception.
        }
    }

    @Test
    public void test10() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "irs_BusinessProcess0.test10");
        irs.BusinessProcess businessProcess0 = new irs.BusinessProcess();
        businessProcess0.test_2();
        java.util.List<irs.Employer> employerList2 = businessProcess0.getAllEmployers();
        java.lang.Class<?> wildcardClass3 = businessProcess0.getClass();
    }

    @Test
    public void test11() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "irs_BusinessProcess0.test11");
        irs.BusinessProcess businessProcess0 = new irs.BusinessProcess();
        businessProcess0.test_2();
        businessProcess0.test_2();
        java.lang.Class<?> wildcardClass3 = businessProcess0.getClass();
    }

    @Test
    public void test12() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "irs_BusinessProcess0.test12");
        irs.BusinessProcess businessProcess0 = new irs.BusinessProcess();
        java.util.List<irs.Employer> employerList1 = businessProcess0.getAllEmployers();
        java.lang.Class<?> wildcardClass2 = businessProcess0.getClass();
    }

    @Test
    public void test13() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "irs_BusinessProcess0.test13");
        irs.BusinessProcess businessProcess0 = new irs.BusinessProcess();
        businessProcess0.test_2();
        businessProcess0.test_2();
        businessProcess0.test_2();
    }

    @Test
    public void test14() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "irs_BusinessProcess0.test14");
        irs.BusinessProcess businessProcess0 = new irs.BusinessProcess();
        businessProcess0.test_2();
        java.lang.Class<?> wildcardClass2 = businessProcess0.getClass();
    }

    @Test
    public void test15() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "irs_BusinessProcess0.test15");
        irs.BusinessProcess businessProcess0 = new irs.BusinessProcess();
        java.util.List<irs.Employer> employerList1 = businessProcess0.getAllEmployers();
        businessProcess0.test_2();
        businessProcess0.test_2();
    }

    @Test
    public void test16() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "irs_BusinessProcess0.test16");
        irs.BusinessProcess businessProcess0 = new irs.BusinessProcess();
        java.util.List<irs.Employer> employerList1 = businessProcess0.getAllEmployers();
        irs.IRS iRS2 = null;
        // The following exception was thrown during execution in test generation
        try {
            businessProcess0.genSalarySlip(iRS2);
            org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException; message: null");
        } catch (java.lang.NullPointerException e) {
            // Expected exception.
        }
    }
}

