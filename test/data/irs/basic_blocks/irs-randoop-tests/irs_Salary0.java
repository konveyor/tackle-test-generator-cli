import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class irs_Salary0 {

    public static boolean debug = false;

    @Test
    public void test1() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "irs_Salary0.test1");
        java.lang.Object obj0 = new java.lang.Object();
        java.lang.Class<?> wildcardClass1 = obj0.getClass();
    }

    @Test
    public void test2() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "irs_Salary0.test2");
        irs.Salary salary0 = new irs.Salary();
        java.lang.Class<?> wildcardClass1 = salary0.getClass();
    }

    @Test
    public void test3() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "irs_Salary0.test3");
        irs.Salary salary0 = new irs.Salary();
        salary0.setEmployerId((int) (short) 0);
        int int3 = salary0.getEmployeeId();
    }

    @Test
    public void test4() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "irs_Salary0.test4");
        irs.Salary salary0 = new irs.Salary();
        int int1 = salary0.getEmployeeId();
        int int2 = salary0.getEmployerId();
        int int3 = salary0.getEmployeeId();
    }

    @Test
    public void test5() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "irs_Salary0.test5");
        irs.Salary salary3 = new irs.Salary((int) (short) 0, 0, (double) (byte) 100);
    }

    @Test
    public void test6() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "irs_Salary0.test6");
        irs.Salary salary0 = new irs.Salary();
        salary0.setEmployerId((int) (short) 0);
        salary0.setSalary((double) 1);
        int int5 = salary0.getEmployerId();
    }

    @Test
    public void test7() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "irs_Salary0.test7");
        irs.Salary salary0 = new irs.Salary();
        salary0.setEmployerId((int) (short) 0);
        salary0.setEmployeeId((int) '#');
    }

    @Test
    public void test8() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "irs_Salary0.test8");
        irs.Salary salary3 = new irs.Salary(1, (int) (short) -1, (double) 100.0f);
        salary3.setEmployeeId((int) 'a');
    }

    @Test
    public void test9() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "irs_Salary0.test9");
        irs.Salary salary0 = new irs.Salary();
        salary0.setEmployerId((int) (short) 0);
        salary0.setSalary((double) 1);
        salary0.setEmployeeId((int) (byte) -1);
    }
}

