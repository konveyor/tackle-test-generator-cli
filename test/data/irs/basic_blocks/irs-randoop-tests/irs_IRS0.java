import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class irs_IRS0 {

    public static boolean debug = false;

    @Test
    public void test01() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "irs_IRS0.test01");
        java.lang.Object obj0 = new java.lang.Object();
        java.lang.Class<?> wildcardClass1 = obj0.getClass();
    }

    @Test
    public void test02() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "irs_IRS0.test02");
        irs.IRS iRS0 = new irs.IRS();
        java.lang.Class<?> wildcardClass1 = iRS0.getClass();
    }

    @Test
    public void test03() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "irs_IRS0.test03");
        irs.IRS iRS0 = new irs.IRS();
        int int1 = iRS0.getYear();
        java.lang.Class<?> wildcardClass2 = iRS0.getClass();
    }

    @Test
    public void test04() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "irs_IRS0.test04");
        irs.IRS iRS0 = new irs.IRS();
        int int1 = iRS0.getYear();
        irs.Salary[] salaryArray2 = new irs.Salary[] {};
        java.util.ArrayList<irs.Salary> salaryList3 = new java.util.ArrayList<irs.Salary>();
        boolean boolean4 = java.util.Collections.addAll((java.util.Collection<irs.Salary>) salaryList3, salaryArray2);
        iRS0.setSalaryList((java.util.List<irs.Salary>) salaryList3);
        java.util.List<irs.Salary> salaryList6 = iRS0.getSalaryList();
        java.util.Set<irs.Salary> salarySet8 = iRS0.getSalarySet((int) '#');
        java.util.List<irs.Salary> salaryList9 = iRS0.getSalaryList();
    }

    @Test
    public void test05() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "irs_IRS0.test05");
        irs.IRS iRS0 = new irs.IRS();
        irs.Salary[] salaryArray1 = new irs.Salary[] {};
        java.util.ArrayList<irs.Salary> salaryList2 = new java.util.ArrayList<irs.Salary>();
        boolean boolean3 = java.util.Collections.addAll((java.util.Collection<irs.Salary>) salaryList2, salaryArray1);
        iRS0.setSalaryList((java.util.List<irs.Salary>) salaryList2);
        iRS0.setYear((int) (byte) 100);
        int int7 = iRS0.getYear();
    }

    @Test
    public void test06() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "irs_IRS0.test06");
        irs.IRS iRS0 = new irs.IRS();
        int int1 = iRS0.getYear();
        irs.Salary[] salaryArray2 = new irs.Salary[] {};
        java.util.ArrayList<irs.Salary> salaryList3 = new java.util.ArrayList<irs.Salary>();
        boolean boolean4 = java.util.Collections.addAll((java.util.Collection<irs.Salary>) salaryList3, salaryArray2);
        iRS0.setSalaryList((java.util.List<irs.Salary>) salaryList3);
        java.util.List<irs.Salary> salaryList6 = iRS0.getSalaryList();
        java.util.Set<irs.Salary> salarySet8 = iRS0.getSalarySet((int) '#');
        iRS0.setYear((int) ' ');
        iRS0.setYear((-1));
    }

    @Test
    public void test07() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "irs_IRS0.test07");
        irs.IRS iRS0 = new irs.IRS();
        int int1 = iRS0.getYear();
        irs.Salary[] salaryArray2 = new irs.Salary[] {};
        java.util.ArrayList<irs.Salary> salaryList3 = new java.util.ArrayList<irs.Salary>();
        boolean boolean4 = java.util.Collections.addAll((java.util.Collection<irs.Salary>) salaryList3, salaryArray2);
        iRS0.setSalaryList((java.util.List<irs.Salary>) salaryList3);
        java.util.List<irs.Salary> salaryList6 = iRS0.getSalaryList();
        java.util.Set<irs.Salary> salarySet8 = iRS0.getSalarySet((int) '#');
        java.util.Set<irs.Salary> salarySet10 = iRS0.getSalarySet(100);
    }

    @Test
    public void test08() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "irs_IRS0.test08");
        irs.IRS iRS0 = new irs.IRS();
        int int1 = iRS0.getYear();
        irs.Salary[] salaryArray2 = new irs.Salary[] {};
        java.util.ArrayList<irs.Salary> salaryList3 = new java.util.ArrayList<irs.Salary>();
        boolean boolean4 = java.util.Collections.addAll((java.util.Collection<irs.Salary>) salaryList3, salaryArray2);
        iRS0.setSalaryList((java.util.List<irs.Salary>) salaryList3);
        java.util.List<irs.Salary> salaryList6 = iRS0.getSalaryList();
        java.util.Map<java.lang.Integer, java.util.Set<irs.Salary>> intMap7 = null;
        iRS0.setAllSalarySets(intMap7);
        irs.Salary[] salaryArray9 = new irs.Salary[] {};
        java.util.ArrayList<irs.Salary> salaryList10 = new java.util.ArrayList<irs.Salary>();
        boolean boolean11 = java.util.Collections.addAll((java.util.Collection<irs.Salary>) salaryList10, salaryArray9);
        iRS0.setSalaryList((java.util.List<irs.Salary>) salaryList10);
        java.util.List<irs.Salary> salaryList13 = iRS0.getSalaryList();
        int int14 = iRS0.getYear();
    }

    @Test
    public void test09() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "irs_IRS0.test09");
        irs.IRS iRS0 = new irs.IRS();
        int int1 = iRS0.getYear();
        irs.Salary[] salaryArray2 = new irs.Salary[] {};
        java.util.ArrayList<irs.Salary> salaryList3 = new java.util.ArrayList<irs.Salary>();
        boolean boolean4 = java.util.Collections.addAll((java.util.Collection<irs.Salary>) salaryList3, salaryArray2);
        iRS0.setSalaryList((java.util.List<irs.Salary>) salaryList3);
        int int6 = iRS0.getYear();
        iRS0.setYear((int) (byte) 10);
    }

    @Test
    public void test10() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "irs_IRS0.test10");
        irs.IRS iRS0 = new irs.IRS();
        int int1 = iRS0.getYear();
        iRS0.setYear((int) (short) 1);
    }

    @Test
    public void test11() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "irs_IRS0.test11");
        irs.IRS iRS0 = new irs.IRS();
        irs.Salary[] salaryArray1 = new irs.Salary[] {};
        java.util.ArrayList<irs.Salary> salaryList2 = new java.util.ArrayList<irs.Salary>();
        boolean boolean3 = java.util.Collections.addAll((java.util.Collection<irs.Salary>) salaryList2, salaryArray1);
        iRS0.setSalaryList((java.util.List<irs.Salary>) salaryList2);
        iRS0.setYear((int) (byte) 100);
        irs.Salary salary8 = iRS0.getSalaryfromIRS(100);
    }

    @Test
    public void test12() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "irs_IRS0.test12");
        irs.IRS iRS0 = new irs.IRS();
        irs.Salary[] salaryArray1 = new irs.Salary[] {};
        java.util.ArrayList<irs.Salary> salaryList2 = new java.util.ArrayList<irs.Salary>();
        boolean boolean3 = java.util.Collections.addAll((java.util.Collection<irs.Salary>) salaryList2, salaryArray1);
        iRS0.setSalaryList((java.util.List<irs.Salary>) salaryList2);
        iRS0.setYear((int) (byte) 100);
        irs.IRS iRS7 = new irs.IRS();
        irs.Salary[] salaryArray8 = new irs.Salary[] {};
        java.util.ArrayList<irs.Salary> salaryList9 = new java.util.ArrayList<irs.Salary>();
        boolean boolean10 = java.util.Collections.addAll((java.util.Collection<irs.Salary>) salaryList9, salaryArray8);
        iRS7.setSalaryList((java.util.List<irs.Salary>) salaryList9);
        java.util.Map<java.lang.Integer, java.util.Set<irs.Salary>> intMap12 = iRS7.getAllSalarySets();
        iRS0.setAllSalarySets(intMap12);
    }

    @Test
    public void test13() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "irs_IRS0.test13");
        irs.IRS iRS0 = new irs.IRS();
        int int1 = iRS0.getYear();
        irs.Salary[] salaryArray2 = new irs.Salary[] {};
        java.util.ArrayList<irs.Salary> salaryList3 = new java.util.ArrayList<irs.Salary>();
        boolean boolean4 = java.util.Collections.addAll((java.util.Collection<irs.Salary>) salaryList3, salaryArray2);
        iRS0.setSalaryList((java.util.List<irs.Salary>) salaryList3);
        java.util.List<irs.Salary> salaryList6 = iRS0.getSalaryList();
        java.util.Set<irs.Salary> salarySet8 = iRS0.getSalarySet((int) '#');
        iRS0.setYear((int) ' ');
        java.util.List<irs.Salary> salaryList11 = iRS0.getSalaryList();
    }

    @Test
    public void test14() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "irs_IRS0.test14");
        irs.IRS iRS0 = new irs.IRS();
        int int1 = iRS0.getYear();
        irs.Salary[] salaryArray2 = new irs.Salary[] {};
        java.util.ArrayList<irs.Salary> salaryList3 = new java.util.ArrayList<irs.Salary>();
        boolean boolean4 = java.util.Collections.addAll((java.util.Collection<irs.Salary>) salaryList3, salaryArray2);
        iRS0.setSalaryList((java.util.List<irs.Salary>) salaryList3);
        java.util.Set<irs.Salary> salarySet7 = iRS0.getSalarySet((int) (byte) 0);
        java.util.List<irs.Salary> salaryList8 = iRS0.getSalaryList();
        irs.IRS iRS9 = new irs.IRS();
        int int10 = iRS9.getYear();
        irs.Salary[] salaryArray11 = new irs.Salary[] {};
        java.util.ArrayList<irs.Salary> salaryList12 = new java.util.ArrayList<irs.Salary>();
        boolean boolean13 = java.util.Collections.addAll((java.util.Collection<irs.Salary>) salaryList12, salaryArray11);
        iRS9.setSalaryList((java.util.List<irs.Salary>) salaryList12);
        java.util.List<irs.Salary> salaryList15 = iRS9.getSalaryList();
        java.util.Map<java.lang.Integer, java.util.Set<irs.Salary>> intMap16 = null;
        iRS9.setAllSalarySets(intMap16);
        irs.Salary[] salaryArray18 = new irs.Salary[] {};
        java.util.ArrayList<irs.Salary> salaryList19 = new java.util.ArrayList<irs.Salary>();
        boolean boolean20 = java.util.Collections.addAll((java.util.Collection<irs.Salary>) salaryList19, salaryArray18);
        iRS9.setSalaryList((java.util.List<irs.Salary>) salaryList19);
        iRS0.setSalaryList((java.util.List<irs.Salary>) salaryList19);
    }

    @Test
    public void test15() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "irs_IRS0.test15");
        irs.IRS iRS0 = new irs.IRS();
        int int1 = iRS0.getYear();
        irs.Salary[] salaryArray2 = new irs.Salary[] {};
        java.util.ArrayList<irs.Salary> salaryList3 = new java.util.ArrayList<irs.Salary>();
        boolean boolean4 = java.util.Collections.addAll((java.util.Collection<irs.Salary>) salaryList3, salaryArray2);
        iRS0.setSalaryList((java.util.List<irs.Salary>) salaryList3);
        java.util.List<irs.Salary> salaryList6 = iRS0.getSalaryList();
        java.util.Map<java.lang.Integer, java.util.Set<irs.Salary>> intMap7 = null;
        iRS0.setAllSalarySets(intMap7);
        irs.Salary[] salaryArray9 = new irs.Salary[] {};
        java.util.ArrayList<irs.Salary> salaryList10 = new java.util.ArrayList<irs.Salary>();
        boolean boolean11 = java.util.Collections.addAll((java.util.Collection<irs.Salary>) salaryList10, salaryArray9);
        iRS0.setSalaryList((java.util.List<irs.Salary>) salaryList10);
        irs.IRS iRS13 = new irs.IRS();
        int int14 = iRS13.getYear();
        irs.Salary[] salaryArray15 = new irs.Salary[] {};
        java.util.ArrayList<irs.Salary> salaryList16 = new java.util.ArrayList<irs.Salary>();
        boolean boolean17 = java.util.Collections.addAll((java.util.Collection<irs.Salary>) salaryList16, salaryArray15);
        iRS13.setSalaryList((java.util.List<irs.Salary>) salaryList16);
        java.util.List<irs.Salary> salaryList19 = iRS13.getSalaryList();
        java.util.Map<java.lang.Integer, java.util.Set<irs.Salary>> intMap20 = null;
        iRS13.setAllSalarySets(intMap20);
        irs.Salary[] salaryArray22 = new irs.Salary[] {};
        java.util.ArrayList<irs.Salary> salaryList23 = new java.util.ArrayList<irs.Salary>();
        boolean boolean24 = java.util.Collections.addAll((java.util.Collection<irs.Salary>) salaryList23, salaryArray22);
        iRS13.setSalaryList((java.util.List<irs.Salary>) salaryList23);
        java.util.List<irs.Salary> salaryList26 = iRS13.getSalaryList();
        iRS0.setSalaryList(salaryList26);
        // The following exception was thrown during execution in test generation
        try {
            java.util.Set<irs.Salary> salarySet29 = iRS0.getSalarySet((int) '4');
            org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException; message: null");
        } catch (java.lang.NullPointerException e) {
            // Expected exception.
        }
    }
}

