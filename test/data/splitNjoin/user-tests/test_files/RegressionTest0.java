import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RegressionTest0 {

    public static boolean debug = false;

    @Test
    public void test001() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test001");
        splitNjoin.list.LinkedList linkedList0 = null;
        // The following exception was thrown during execution in test generation
        try {
            java.lang.String str1 = splitNjoin.utilities.StringUtils.join(linkedList0);
            org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException; message: null");
        } catch (java.lang.NullPointerException e) {
            // Expected exception.
        }
    }

    @Test
    public void test002() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test002");
        splitNjoin.app.App app0 = new splitNjoin.app.App();
    }

    @Test
    public void test003() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test003");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        java.lang.Class<?> wildcardClass2 = linkedList1.getClass();
    }

    @Test
    public void test004() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test004");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        // The following exception was thrown during execution in test generation
        try {
            java.lang.String str3 = linkedList1.get((int) (short) 0);
            org.junit.Assert.fail("Expected exception of type java.lang.IndexOutOfBoundsException; message: Index is out of range");
        } catch (java.lang.IndexOutOfBoundsException e) {
            // Expected exception.
        }
    }

    @Test
    public void test005() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test005");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        java.lang.Class<?> wildcardClass4 = linkedList1.getClass();
    }

    @Test
    public void test006() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test006");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        // The following exception was thrown during execution in test generation
        try {
            java.lang.String str5 = linkedList1.get(1);
            org.junit.Assert.fail("Expected exception of type java.lang.IndexOutOfBoundsException; message: Index is out of range");
        } catch (java.lang.IndexOutOfBoundsException e) {
            // Expected exception.
        }
    }

    @Test
    public void test007() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test007");
        splitNjoin.list.LinkedList linkedList0 = new splitNjoin.list.LinkedList();
        java.lang.Class<?> wildcardClass1 = linkedList0.getClass();
    }

    @Test
    public void test008() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test008");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        linkedList1.add("hi!");
        java.lang.Class<?> wildcardClass6 = linkedList1.getClass();
    }

    @Test
    public void test009() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test009");
        java.lang.Object obj0 = new java.lang.Object();
        java.lang.Class<?> wildcardClass1 = obj0.getClass();
    }

    @Test
    public void test010() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test010");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("hi!");
        java.lang.Class<?> wildcardClass2 = linkedList1.getClass();
    }

    @Test
    public void test011() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test011");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        // The following exception was thrown during execution in test generation
        try {
            java.lang.String str5 = linkedList1.get((int) '4');
            org.junit.Assert.fail("Expected exception of type java.lang.IndexOutOfBoundsException; message: Index is out of range");
        } catch (java.lang.IndexOutOfBoundsException e) {
            // Expected exception.
        }
    }

    @Test
    public void test012() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test012");
        splitNjoin.list.LinkedList linkedList0 = new splitNjoin.list.LinkedList();
        // The following exception was thrown during execution in test generation
        try {
            java.lang.String str2 = linkedList0.get((int) (short) 0);
            org.junit.Assert.fail("Expected exception of type java.lang.IndexOutOfBoundsException; message: Index is out of range");
        } catch (java.lang.IndexOutOfBoundsException e) {
            // Expected exception.
        }
    }

    @Test
    public void test013() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test013");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        linkedList1.add("hi!");
        int int6 = linkedList1.size();
        int int7 = linkedList1.size();
        int int8 = linkedList1.size();
        // The following exception was thrown during execution in test generation
        try {
            java.lang.String str10 = linkedList1.get(10);
            org.junit.Assert.fail("Expected exception of type java.lang.IndexOutOfBoundsException; message: Index is out of range");
        } catch (java.lang.IndexOutOfBoundsException e) {
            // Expected exception.
        }
    }

    @Test
    public void test014() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test014");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        linkedList1.add("hi!");
        int int6 = linkedList1.size();
        int int7 = linkedList1.size();
        java.lang.String str8 = splitNjoin.utilities.StringUtils.join(linkedList1);
        // The following exception was thrown during execution in test generation
        try {
            java.lang.String str10 = linkedList1.get((int) (short) 10);
            org.junit.Assert.fail("Expected exception of type java.lang.IndexOutOfBoundsException; message: Index is out of range");
        } catch (java.lang.IndexOutOfBoundsException e) {
            // Expected exception.
        }
    }

    @Test
    public void test015() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test015");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        linkedList1.add("hi!");
        int int6 = linkedList1.size();
        int int7 = linkedList1.size();
        java.lang.String str8 = splitNjoin.utilities.StringUtils.join(linkedList1);
        java.lang.String str9 = splitNjoin.utilities.StringUtils.join(linkedList1);
        // The following exception was thrown during execution in test generation
        try {
            java.lang.String str11 = linkedList1.get(2);
            org.junit.Assert.fail("Expected exception of type java.lang.IndexOutOfBoundsException; message: Index is out of range");
        } catch (java.lang.IndexOutOfBoundsException e) {
            // Expected exception.
        }
    }

    @Test
    public void test016() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test016");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        boolean boolean3 = linkedList1.remove("hi!");
        java.lang.Class<?> wildcardClass4 = linkedList1.getClass();
    }

    @Test
    public void test017() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test017");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        boolean boolean3 = linkedList1.remove("hi!");
        // The following exception was thrown during execution in test generation
        try {
            java.lang.String str5 = linkedList1.get((int) (byte) 10);
            org.junit.Assert.fail("Expected exception of type java.lang.IndexOutOfBoundsException; message: Index is out of range");
        } catch (java.lang.IndexOutOfBoundsException e) {
            // Expected exception.
        }
    }

    @Test
    public void test018() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test018");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        boolean boolean3 = linkedList1.remove("hi!");
        // The following exception was thrown during execution in test generation
        try {
            java.lang.String str5 = linkedList1.get(100);
            org.junit.Assert.fail("Expected exception of type java.lang.IndexOutOfBoundsException; message: Index is out of range");
        } catch (java.lang.IndexOutOfBoundsException e) {
            // Expected exception.
        }
    }

    @Test
    public void test019() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test019");
        splitNjoin.list.LinkedList linkedList0 = new splitNjoin.list.LinkedList();
        boolean boolean2 = linkedList0.remove("hi!");
        // The following exception was thrown during execution in test generation
        try {
            java.lang.String str4 = linkedList0.get((int) (short) 1);
            org.junit.Assert.fail("Expected exception of type java.lang.IndexOutOfBoundsException; message: Index is out of range");
        } catch (java.lang.IndexOutOfBoundsException e) {
            // Expected exception.
        }
    }

    @Test
    public void test020() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test020");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        linkedList1.add("");
        // The following exception was thrown during execution in test generation
        try {
            java.lang.String str7 = linkedList1.get(10);
            org.junit.Assert.fail("Expected exception of type java.lang.IndexOutOfBoundsException; message: Index is out of range");
        } catch (java.lang.IndexOutOfBoundsException e) {
            // Expected exception.
        }
    }

    @Test
    public void test021() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test021");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        linkedList1.add("hi!");
        int int6 = linkedList1.size();
        int int7 = linkedList1.size();
        java.lang.String str8 = splitNjoin.utilities.StringUtils.join(linkedList1);
        java.lang.Class<?> wildcardClass9 = linkedList1.getClass();
    }

    @Test
    public void test022() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test022");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        // The following exception was thrown during execution in test generation
        try {
            java.lang.String str3 = linkedList1.get(1);
            org.junit.Assert.fail("Expected exception of type java.lang.IndexOutOfBoundsException; message: Index is out of range");
        } catch (java.lang.IndexOutOfBoundsException e) {
            // Expected exception.
        }
    }

    @Test
    public void test023() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test023");
        splitNjoin.list.LinkedList linkedList0 = new splitNjoin.list.LinkedList();
        linkedList0.add("hi!");
        java.lang.Class<?> wildcardClass3 = linkedList0.getClass();
    }

    @Test
    public void test024() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test024");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        int int4 = linkedList1.size();
        int int5 = linkedList1.size();
        java.lang.String str6 = splitNjoin.utilities.StringUtils.join(linkedList1);
        boolean boolean8 = linkedList1.remove("");
    }

    @Test
    public void test025() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test025");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        boolean boolean3 = linkedList1.remove("hi!");
        // The following exception was thrown during execution in test generation
        try {
            java.lang.String str5 = linkedList1.get((int) '#');
            org.junit.Assert.fail("Expected exception of type java.lang.IndexOutOfBoundsException; message: Index is out of range");
        } catch (java.lang.IndexOutOfBoundsException e) {
            // Expected exception.
        }
    }

    @Test
    public void test026() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test026");
        splitNjoin.list.LinkedList linkedList0 = new splitNjoin.list.LinkedList();
        boolean boolean2 = linkedList0.remove("hi!");
        java.lang.Class<?> wildcardClass3 = linkedList0.getClass();
    }

    @Test
    public void test027() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test027");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        linkedList1.add("hi!");
        int int6 = linkedList1.size();
        // The following exception was thrown during execution in test generation
        try {
            java.lang.String str8 = linkedList1.get((int) '4');
            org.junit.Assert.fail("Expected exception of type java.lang.IndexOutOfBoundsException; message: Index is out of range");
        } catch (java.lang.IndexOutOfBoundsException e) {
            // Expected exception.
        }
    }

    @Test
    public void test028() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test028");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        linkedList1.add("hi!");
        boolean boolean7 = linkedList1.remove("");
        boolean boolean9 = linkedList1.remove("hi!");
        int int10 = linkedList1.size();
        java.lang.String str11 = splitNjoin.utilities.StringUtils.join(linkedList1);
        // The following exception was thrown during execution in test generation
        try {
            java.lang.String str13 = linkedList1.get((int) '#');
            org.junit.Assert.fail("Expected exception of type java.lang.IndexOutOfBoundsException; message: Index is out of range");
        } catch (java.lang.IndexOutOfBoundsException e) {
            // Expected exception.
        }
    }

    @Test
    public void test029() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test029");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        linkedList1.add("hi!");
        int int6 = linkedList1.size();
        int int7 = linkedList1.size();
        java.lang.String str8 = splitNjoin.utilities.StringUtils.join(linkedList1);
        // The following exception was thrown during execution in test generation
        try {
            java.lang.String str10 = linkedList1.get(2);
            org.junit.Assert.fail("Expected exception of type java.lang.IndexOutOfBoundsException; message: Index is out of range");
        } catch (java.lang.IndexOutOfBoundsException e) {
            // Expected exception.
        }
    }

    @Test
    public void test030() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test030");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("hi!");
        boolean boolean3 = linkedList1.remove("");
        // The following exception was thrown during execution in test generation
        try {
            java.lang.String str5 = linkedList1.get((int) (short) 10);
            org.junit.Assert.fail("Expected exception of type java.lang.IndexOutOfBoundsException; message: Index is out of range");
        } catch (java.lang.IndexOutOfBoundsException e) {
            // Expected exception.
        }
    }

    @Test
    public void test031() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test031");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("hi!");
        java.lang.Class<?> wildcardClass4 = linkedList1.getClass();
    }

    @Test
    public void test032() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test032");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("hi!");
        int int4 = linkedList1.size();
        linkedList1.add("hi!");
        linkedList1.add("hi!");
    }

    @Test
    public void test033() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test033");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        boolean boolean5 = linkedList1.remove("hi!");
        java.lang.String str6 = splitNjoin.utilities.StringUtils.join(linkedList1);
        // The following exception was thrown during execution in test generation
        try {
            java.lang.String str8 = linkedList1.get(10);
            org.junit.Assert.fail("Expected exception of type java.lang.IndexOutOfBoundsException; message: Index is out of range");
        } catch (java.lang.IndexOutOfBoundsException e) {
            // Expected exception.
        }
    }

    @Test
    public void test034() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test034");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        linkedList1.add("");
        linkedList1.add("hi!");
        java.lang.String str8 = splitNjoin.utilities.StringUtils.join(linkedList1);
        java.lang.Class<?> wildcardClass9 = linkedList1.getClass();
    }

    @Test
    public void test035() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test035");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        int int4 = linkedList1.size();
        int int5 = linkedList1.size();
        java.lang.String str6 = splitNjoin.utilities.StringUtils.join(linkedList1);
        java.lang.String str7 = splitNjoin.utilities.StringUtils.join(linkedList1);
        java.lang.Class<?> wildcardClass8 = linkedList1.getClass();
    }

    @Test
    public void test036() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test036");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("hi!");
        boolean boolean3 = linkedList1.remove("");
        boolean boolean5 = linkedList1.remove("");
        boolean boolean7 = linkedList1.remove("hi!");
        // The following exception was thrown during execution in test generation
        try {
            java.lang.String str9 = linkedList1.get((int) (short) 0);
            org.junit.Assert.fail("Expected exception of type java.lang.IndexOutOfBoundsException; message: Index is out of range");
        } catch (java.lang.IndexOutOfBoundsException e) {
            // Expected exception.
        }
    }

    @Test
    public void test037() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test037");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        linkedList1.add("hi!");
        int int6 = linkedList1.size();
        int int7 = linkedList1.size();
        java.lang.String str8 = splitNjoin.utilities.StringUtils.join(linkedList1);
        java.lang.String str9 = splitNjoin.utilities.StringUtils.join(linkedList1);
        java.lang.String str10 = splitNjoin.utilities.StringUtils.join(linkedList1);
        java.lang.Class<?> wildcardClass11 = linkedList1.getClass();
    }

    @Test
    public void test038() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test038");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        linkedList1.add("hi!");
        boolean boolean7 = linkedList1.remove("");
        boolean boolean9 = linkedList1.remove("hi!");
        java.lang.String str10 = splitNjoin.utilities.StringUtils.join(linkedList1);
        linkedList1.add("hi!");
        java.lang.String str14 = linkedList1.get((int) (short) -1);
        linkedList1.add("hi!");
    }

    @Test
    public void test039() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test039");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        linkedList1.add("hi!");
        boolean boolean7 = linkedList1.remove("");
        boolean boolean9 = linkedList1.remove("hi!");
        int int10 = linkedList1.size();
        // The following exception was thrown during execution in test generation
        try {
            java.lang.String str12 = linkedList1.get(0);
            org.junit.Assert.fail("Expected exception of type java.lang.IndexOutOfBoundsException; message: Index is out of range");
        } catch (java.lang.IndexOutOfBoundsException e) {
            // Expected exception.
        }
    }

    @Test
    public void test040() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test040");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        linkedList1.add("hi!");
        // The following exception was thrown during execution in test generation
        try {
            java.lang.String str7 = linkedList1.get(100);
            org.junit.Assert.fail("Expected exception of type java.lang.IndexOutOfBoundsException; message: Index is out of range");
        } catch (java.lang.IndexOutOfBoundsException e) {
            // Expected exception.
        }
    }

    @Test
    public void test041() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test041");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("hi!");
        java.lang.String str4 = splitNjoin.utilities.StringUtils.join(linkedList1);
    }

    @Test
    public void test042() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test042");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        linkedList1.add("hi!");
        int int6 = linkedList1.size();
        int int7 = linkedList1.size();
        java.lang.String str8 = splitNjoin.utilities.StringUtils.join(linkedList1);
        java.lang.String str9 = splitNjoin.utilities.StringUtils.join(linkedList1);
        java.lang.Class<?> wildcardClass10 = linkedList1.getClass();
    }

    @Test
    public void test043() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test043");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        boolean boolean3 = linkedList1.remove("hi!");
        boolean boolean5 = linkedList1.remove("hi!");
        java.lang.Class<?> wildcardClass6 = linkedList1.getClass();
    }

    @Test
    public void test044() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test044");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        linkedList1.add("hi!");
        boolean boolean7 = linkedList1.remove("hi!");
    }

    @Test
    public void test045() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test045");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        int int4 = linkedList1.size();
        linkedList1.add("");
        // The following exception was thrown during execution in test generation
        try {
            java.lang.String str8 = linkedList1.get((int) (short) 10);
            org.junit.Assert.fail("Expected exception of type java.lang.IndexOutOfBoundsException; message: Index is out of range");
        } catch (java.lang.IndexOutOfBoundsException e) {
            // Expected exception.
        }
    }

    @Test
    public void test046() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test046");
        splitNjoin.list.LinkedList linkedList0 = new splitNjoin.list.LinkedList();
        boolean boolean2 = linkedList0.remove("hi!");
        boolean boolean4 = linkedList0.remove("hi!");
        java.lang.Class<?> wildcardClass5 = linkedList0.getClass();
    }

    @Test
    public void test047() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test047");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        linkedList1.add("hi!");
        int int6 = linkedList1.size();
        int int7 = linkedList1.size();
        int int8 = linkedList1.size();
        java.lang.String str9 = splitNjoin.utilities.StringUtils.join(linkedList1);
        // The following exception was thrown during execution in test generation
        try {
            java.lang.String str11 = linkedList1.get((int) (short) 10);
            org.junit.Assert.fail("Expected exception of type java.lang.IndexOutOfBoundsException; message: Index is out of range");
        } catch (java.lang.IndexOutOfBoundsException e) {
            // Expected exception.
        }
    }

    @Test
    public void test048() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test048");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        linkedList1.add("hi!");
        java.lang.String str6 = splitNjoin.utilities.StringUtils.join(linkedList1);
        int int7 = linkedList1.size();
        java.lang.Class<?> wildcardClass8 = linkedList1.getClass();
    }

    @Test
    public void test049() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test049");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("hi!");
        int int4 = linkedList1.size();
        java.lang.Class<?> wildcardClass5 = linkedList1.getClass();
    }

    @Test
    public void test050() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test050");
        splitNjoin.list.LinkedList linkedList0 = new splitNjoin.list.LinkedList();
        linkedList0.add("hi!");
        // The following exception was thrown during execution in test generation
        try {
            java.lang.String str4 = linkedList0.get((int) (byte) 100);
            org.junit.Assert.fail("Expected exception of type java.lang.IndexOutOfBoundsException; message: Index is out of range");
        } catch (java.lang.IndexOutOfBoundsException e) {
            // Expected exception.
        }
    }

    @Test
    public void test051() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test051");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        java.lang.String str2 = splitNjoin.utilities.StringUtils.join(linkedList1);
        boolean boolean4 = linkedList1.remove("hi!");
        java.lang.String str5 = splitNjoin.utilities.StringUtils.join(linkedList1);
    }

    @Test
    public void test052() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test052");
        splitNjoin.list.LinkedList linkedList0 = new splitNjoin.list.LinkedList();
        linkedList0.add("hi!");
        linkedList0.add("");
        java.lang.String str5 = splitNjoin.utilities.StringUtils.join(linkedList0);
        java.lang.String str7 = linkedList0.get((int) (byte) -1);
    }

    @Test
    public void test053() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test053");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        int int4 = linkedList1.size();
        int int5 = linkedList1.size();
        java.lang.String str6 = splitNjoin.utilities.StringUtils.join(linkedList1);
        // The following exception was thrown during execution in test generation
        try {
            java.lang.String str8 = linkedList1.get(1);
            org.junit.Assert.fail("Expected exception of type java.lang.IndexOutOfBoundsException; message: Index is out of range");
        } catch (java.lang.IndexOutOfBoundsException e) {
            // Expected exception.
        }
    }

    @Test
    public void test054() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test054");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("hi!");
        int int4 = linkedList1.size();
        linkedList1.add("");
        java.lang.Class<?> wildcardClass7 = linkedList1.getClass();
    }

    @Test
    public void test055() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test055");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        linkedList1.add("hi!");
        boolean boolean7 = linkedList1.remove("");
        boolean boolean9 = linkedList1.remove("hi!");
        int int10 = linkedList1.size();
        // The following exception was thrown during execution in test generation
        try {
            java.lang.String str12 = linkedList1.get((int) (short) 100);
            org.junit.Assert.fail("Expected exception of type java.lang.IndexOutOfBoundsException; message: Index is out of range");
        } catch (java.lang.IndexOutOfBoundsException e) {
            // Expected exception.
        }
    }

    @Test
    public void test056() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test056");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        linkedList1.add("hi!");
        java.lang.String str6 = splitNjoin.utilities.StringUtils.join(linkedList1);
        // The following exception was thrown during execution in test generation
        try {
            java.lang.String str8 = linkedList1.get((int) (short) 100);
            org.junit.Assert.fail("Expected exception of type java.lang.IndexOutOfBoundsException; message: Index is out of range");
        } catch (java.lang.IndexOutOfBoundsException e) {
            // Expected exception.
        }
    }

    @Test
    public void test057() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test057");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        linkedList1.add("hi!");
        int int6 = linkedList1.size();
        int int7 = linkedList1.size();
        java.lang.String str8 = splitNjoin.utilities.StringUtils.join(linkedList1);
        java.lang.String str9 = splitNjoin.utilities.StringUtils.join(linkedList1);
        int int10 = linkedList1.size();
        int int11 = linkedList1.size();
        java.lang.Class<?> wildcardClass12 = linkedList1.getClass();
    }

    @Test
    public void test058() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test058");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("hi!");
        int int4 = linkedList1.size();
        linkedList1.add("hi!");
        linkedList1.add("hi! hi!");
    }

    @Test
    public void test059() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test059");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("hi!");
        boolean boolean3 = linkedList1.remove("");
        boolean boolean5 = linkedList1.remove("");
        boolean boolean7 = linkedList1.remove("");
        // The following exception was thrown during execution in test generation
        try {
            java.lang.String str9 = linkedList1.get((int) (byte) 1);
            org.junit.Assert.fail("Expected exception of type java.lang.IndexOutOfBoundsException; message: Index is out of range");
        } catch (java.lang.IndexOutOfBoundsException e) {
            // Expected exception.
        }
    }

    @Test
    public void test060() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test060");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        linkedList1.add("hi!");
        boolean boolean7 = linkedList1.remove("");
        boolean boolean9 = linkedList1.remove("hi!");
        java.lang.String str10 = splitNjoin.utilities.StringUtils.join(linkedList1);
        linkedList1.add("hi!");
        boolean boolean14 = linkedList1.remove("hi! ");
    }

    @Test
    public void test061() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test061");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        linkedList1.add("hi!");
        java.lang.String str6 = splitNjoin.utilities.StringUtils.join(linkedList1);
        java.lang.Class<?> wildcardClass7 = linkedList1.getClass();
    }

    @Test
    public void test062() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test062");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        linkedList1.add("hi!");
        boolean boolean7 = linkedList1.remove("");
        boolean boolean9 = linkedList1.remove("hi!");
        java.lang.String str10 = splitNjoin.utilities.StringUtils.join(linkedList1);
        linkedList1.add("hi!");
        // The following exception was thrown during execution in test generation
        try {
            java.lang.String str14 = linkedList1.get((int) (byte) 1);
            org.junit.Assert.fail("Expected exception of type java.lang.IndexOutOfBoundsException; message: Index is out of range");
        } catch (java.lang.IndexOutOfBoundsException e) {
            // Expected exception.
        }
    }

    @Test
    public void test063() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test063");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        linkedList1.add("hi!");
        boolean boolean7 = linkedList1.remove("");
        boolean boolean9 = linkedList1.remove("hi!");
        linkedList1.add("hi!");
    }

    @Test
    public void test064() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test064");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        linkedList1.add("hi!");
        boolean boolean7 = linkedList1.remove("");
        boolean boolean9 = linkedList1.remove("hi!");
        int int10 = linkedList1.size();
        java.lang.String str11 = splitNjoin.utilities.StringUtils.join(linkedList1);
        java.lang.String str12 = splitNjoin.utilities.StringUtils.join(linkedList1);
        linkedList1.add("");
    }

    @Test
    public void test065() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test065");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("hi! ");
        // The following exception was thrown during execution in test generation
        try {
            java.lang.String str3 = linkedList1.get(100);
            org.junit.Assert.fail("Expected exception of type java.lang.IndexOutOfBoundsException; message: Index is out of range");
        } catch (java.lang.IndexOutOfBoundsException e) {
            // Expected exception.
        }
    }

    @Test
    public void test066() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test066");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        linkedList1.add("hi!");
        int int6 = linkedList1.size();
        int int7 = linkedList1.size();
        java.lang.String str8 = splitNjoin.utilities.StringUtils.join(linkedList1);
        java.lang.String str9 = splitNjoin.utilities.StringUtils.join(linkedList1);
        java.lang.String str10 = splitNjoin.utilities.StringUtils.join(linkedList1);
        linkedList1.add("hi! ");
        linkedList1.add("");
    }

    @Test
    public void test067() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test067");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        linkedList1.add("hi!");
        java.lang.String str7 = linkedList1.get((-1));
    }

    @Test
    public void test068() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test068");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        linkedList1.add("hi!");
        java.lang.String str6 = splitNjoin.utilities.StringUtils.join(linkedList1);
        int int7 = linkedList1.size();
        java.lang.String str8 = splitNjoin.utilities.StringUtils.join(linkedList1);
    }

    @Test
    public void test069() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test069");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        boolean boolean3 = linkedList1.remove("hi!");
        int int4 = linkedList1.size();
        // The following exception was thrown during execution in test generation
        try {
            java.lang.String str6 = linkedList1.get((int) '4');
            org.junit.Assert.fail("Expected exception of type java.lang.IndexOutOfBoundsException; message: Index is out of range");
        } catch (java.lang.IndexOutOfBoundsException e) {
            // Expected exception.
        }
    }

    @Test
    public void test070() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test070");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        boolean boolean5 = linkedList1.remove("hi!");
        java.lang.String str7 = linkedList1.get((int) (short) -1);
        java.lang.String str9 = linkedList1.get((int) (short) -1);
        boolean boolean11 = linkedList1.remove("");
        java.lang.Class<?> wildcardClass12 = linkedList1.getClass();
    }

    @Test
    public void test071() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test071");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        linkedList1.add("hi!");
        java.lang.String str6 = splitNjoin.utilities.StringUtils.join(linkedList1);
        linkedList1.add("");
        linkedList1.add("");
    }

    @Test
    public void test072() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test072");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        linkedList1.add("hi!");
        boolean boolean7 = linkedList1.remove("");
        boolean boolean9 = linkedList1.remove("hi!");
        java.lang.String str10 = splitNjoin.utilities.StringUtils.join(linkedList1);
        java.lang.Class<?> wildcardClass11 = linkedList1.getClass();
    }

    @Test
    public void test073() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test073");
        splitNjoin.list.LinkedList linkedList0 = new splitNjoin.list.LinkedList();
        linkedList0.add("hi!");
        boolean boolean4 = linkedList0.remove("hi! ");
        java.lang.Class<?> wildcardClass5 = linkedList0.getClass();
    }

    @Test
    public void test074() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test074");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        int int4 = linkedList1.size();
        int int5 = linkedList1.size();
        java.lang.String str6 = splitNjoin.utilities.StringUtils.join(linkedList1);
        int int7 = linkedList1.size();
        int int8 = linkedList1.size();
    }

    @Test
    public void test075() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test075");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        linkedList1.add("hi!");
        boolean boolean7 = linkedList1.remove("");
        boolean boolean9 = linkedList1.remove("hi!");
        java.lang.String str10 = splitNjoin.utilities.StringUtils.join(linkedList1);
        linkedList1.add("hi!");
        java.lang.String str14 = linkedList1.get((int) (short) -1);
        java.lang.Class<?> wildcardClass15 = linkedList1.getClass();
    }

    @Test
    public void test076() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test076");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("hi!");
        int int4 = linkedList1.size();
        linkedList1.add("");
        java.lang.String str7 = splitNjoin.utilities.StringUtils.join(linkedList1);
    }

    @Test
    public void test077() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test077");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        int int4 = linkedList1.size();
        int int5 = linkedList1.size();
        java.lang.String str6 = splitNjoin.utilities.StringUtils.join(linkedList1);
        int int7 = linkedList1.size();
        linkedList1.add("hi!");
        java.lang.Class<?> wildcardClass10 = linkedList1.getClass();
    }

    @Test
    public void test078() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test078");
        splitNjoin.list.LinkedList linkedList0 = new splitNjoin.list.LinkedList();
        linkedList0.add("hi!");
        linkedList0.add("");
        int int5 = linkedList0.size();
        java.lang.Class<?> wildcardClass6 = linkedList0.getClass();
    }

    @Test
    public void test079() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test079");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        java.lang.String str4 = splitNjoin.utilities.StringUtils.join(linkedList1);
        linkedList1.add("");
        java.lang.String str7 = splitNjoin.utilities.StringUtils.join(linkedList1);
        int int8 = linkedList1.size();
    }

    @Test
    public void test080() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test080");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        linkedList1.add("hi!");
        boolean boolean7 = linkedList1.remove("");
        boolean boolean9 = linkedList1.remove("hi!");
        java.lang.String str10 = splitNjoin.utilities.StringUtils.join(linkedList1);
        int int11 = linkedList1.size();
        int int12 = linkedList1.size();
    }

    @Test
    public void test081() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test081");
        splitNjoin.list.LinkedList linkedList0 = new splitNjoin.list.LinkedList();
        boolean boolean2 = linkedList0.remove("hi!");
        java.lang.String str3 = splitNjoin.utilities.StringUtils.join(linkedList0);
        java.lang.Class<?> wildcardClass4 = linkedList0.getClass();
    }

    @Test
    public void test082() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test082");
        splitNjoin.utilities.StringUtils stringUtils0 = new splitNjoin.utilities.StringUtils();
        java.lang.Class<?> wildcardClass1 = stringUtils0.getClass();
    }

    @Test
    public void test083() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test083");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        java.lang.String str2 = splitNjoin.utilities.StringUtils.join(linkedList1);
        int int3 = linkedList1.size();
        linkedList1.add("");
    }

    @Test
    public void test084() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test084");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        boolean boolean3 = linkedList1.remove("hi!");
        int int4 = linkedList1.size();
        int int5 = linkedList1.size();
        java.lang.Class<?> wildcardClass6 = linkedList1.getClass();
    }

    @Test
    public void test085() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test085");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        linkedList1.add("hi!");
        int int6 = linkedList1.size();
        linkedList1.add("hi!");
        int int9 = linkedList1.size();
        java.lang.Class<?> wildcardClass10 = linkedList1.getClass();
    }

    @Test
    public void test086() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test086");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("hi!");
        int int4 = linkedList1.size();
        linkedList1.add("");
        linkedList1.add("hi!");
        int int9 = linkedList1.size();
    }

    @Test
    public void test087() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test087");
        splitNjoin.list.LinkedList linkedList0 = new splitNjoin.list.LinkedList();
        linkedList0.add("hi!");
        linkedList0.add("");
        boolean boolean6 = linkedList0.remove("");
        java.lang.Class<?> wildcardClass7 = linkedList0.getClass();
    }

    @Test
    public void test088() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test088");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        linkedList1.add("hi!");
        boolean boolean7 = linkedList1.remove("");
        boolean boolean9 = linkedList1.remove("hi!");
        int int10 = linkedList1.size();
        linkedList1.add("hi!");
        // The following exception was thrown during execution in test generation
        try {
            java.lang.String str14 = linkedList1.get((int) (byte) 100);
            org.junit.Assert.fail("Expected exception of type java.lang.IndexOutOfBoundsException; message: Index is out of range");
        } catch (java.lang.IndexOutOfBoundsException e) {
            // Expected exception.
        }
    }

    @Test
    public void test089() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test089");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        boolean boolean3 = linkedList1.remove("hi!");
        int int4 = linkedList1.size();
        boolean boolean6 = linkedList1.remove("hi!");
    }

    @Test
    public void test090() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test090");
        splitNjoin.list.LinkedList linkedList0 = new splitNjoin.list.LinkedList();
        boolean boolean2 = linkedList0.remove("hi!");
        java.lang.String str3 = splitNjoin.utilities.StringUtils.join(linkedList0);
        int int4 = linkedList0.size();
        linkedList0.add("hi!");
    }

    @Test
    public void test091() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test091");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        java.lang.String str2 = splitNjoin.utilities.StringUtils.join(linkedList1);
        boolean boolean4 = linkedList1.remove("hi!");
        linkedList1.add("");
        // The following exception was thrown during execution in test generation
        try {
            java.lang.String str8 = linkedList1.get((int) 'a');
            org.junit.Assert.fail("Expected exception of type java.lang.IndexOutOfBoundsException; message: Index is out of range");
        } catch (java.lang.IndexOutOfBoundsException e) {
            // Expected exception.
        }
    }

    @Test
    public void test092() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test092");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        boolean boolean5 = linkedList1.remove("hi!");
        java.lang.String str7 = linkedList1.get((int) (short) -1);
        int int8 = linkedList1.size();
        java.lang.Class<?> wildcardClass9 = linkedList1.getClass();
    }

    @Test
    public void test093() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test093");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        linkedList1.add("hi!");
        boolean boolean7 = linkedList1.remove("");
        boolean boolean9 = linkedList1.remove("hi!");
        int int10 = linkedList1.size();
        java.lang.String str11 = splitNjoin.utilities.StringUtils.join(linkedList1);
        java.lang.String str12 = splitNjoin.utilities.StringUtils.join(linkedList1);
        boolean boolean14 = linkedList1.remove("hi! ");
        linkedList1.add("hi! ");
        java.lang.String str17 = splitNjoin.utilities.StringUtils.join(linkedList1);
    }

    @Test
    public void test094() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test094");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        linkedList1.add("hi!");
        int int6 = linkedList1.size();
        int int7 = linkedList1.size();
        java.lang.String str8 = splitNjoin.utilities.StringUtils.join(linkedList1);
        java.lang.String str9 = splitNjoin.utilities.StringUtils.join(linkedList1);
        int int10 = linkedList1.size();
        java.lang.String str11 = splitNjoin.utilities.StringUtils.join(linkedList1);
        java.lang.String str12 = splitNjoin.utilities.StringUtils.join(linkedList1);
        java.lang.String str13 = splitNjoin.utilities.StringUtils.join(linkedList1);
    }

    @Test
    public void test095() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test095");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        linkedList1.add("");
        linkedList1.add("hi!");
        java.lang.String str8 = splitNjoin.utilities.StringUtils.join(linkedList1);
        int int9 = linkedList1.size();
        java.lang.Class<?> wildcardClass10 = linkedList1.getClass();
    }

    @Test
    public void test096() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test096");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        linkedList1.add("hi!");
        boolean boolean7 = linkedList1.remove("");
        boolean boolean9 = linkedList1.remove("hi!");
        java.lang.String str10 = splitNjoin.utilities.StringUtils.join(linkedList1);
        linkedList1.add("hi!");
        boolean boolean14 = linkedList1.remove("");
        int int15 = linkedList1.size();
    }

    @Test
    public void test097() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test097");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        linkedList1.add("hi!");
        int int6 = linkedList1.size();
        int int7 = linkedList1.size();
        java.lang.String str8 = splitNjoin.utilities.StringUtils.join(linkedList1);
        java.lang.String str9 = splitNjoin.utilities.StringUtils.join(linkedList1);
        java.lang.String str11 = linkedList1.get((int) (byte) 0);
    }

    @Test
    public void test098() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test098");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        boolean boolean5 = linkedList1.remove("");
        java.lang.Class<?> wildcardClass6 = linkedList1.getClass();
    }

    @Test
    public void test099() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test099");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        int int4 = linkedList1.size();
        linkedList1.add("");
        java.lang.String str8 = linkedList1.get((int) (byte) 1);
    }

    @Test
    public void test100() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test100");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        boolean boolean5 = linkedList1.remove("hi!");
        java.lang.String str6 = splitNjoin.utilities.StringUtils.join(linkedList1);
        boolean boolean8 = linkedList1.remove("hi! hi!");
        boolean boolean10 = linkedList1.remove("hi! hi!");
    }

    @Test
    public void test101() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test101");
        splitNjoin.list.LinkedList linkedList0 = new splitNjoin.list.LinkedList();
        linkedList0.add("hi!");
        linkedList0.add("hi! ");
        java.lang.Class<?> wildcardClass5 = linkedList0.getClass();
    }

    @Test
    public void test102() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test102");
        splitNjoin.list.LinkedList linkedList0 = new splitNjoin.list.LinkedList();
        linkedList0.add("hi!");
        linkedList0.add("");
        boolean boolean6 = linkedList0.remove("");
        java.lang.String str7 = splitNjoin.utilities.StringUtils.join(linkedList0);
        // The following exception was thrown during execution in test generation
        try {
            java.lang.String str9 = linkedList0.get((int) (byte) 1);
            org.junit.Assert.fail("Expected exception of type java.lang.IndexOutOfBoundsException; message: Index is out of range");
        } catch (java.lang.IndexOutOfBoundsException e) {
            // Expected exception.
        }
    }

    @Test
    public void test103() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test103");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        linkedList1.add("hi!");
        java.lang.String str6 = splitNjoin.utilities.StringUtils.join(linkedList1);
        linkedList1.add("");
        linkedList1.add("hi! ");
    }

    @Test
    public void test104() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test104");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        int int4 = linkedList1.size();
        int int5 = linkedList1.size();
        java.lang.String str6 = splitNjoin.utilities.StringUtils.join(linkedList1);
        int int7 = linkedList1.size();
        java.lang.String str8 = splitNjoin.utilities.StringUtils.join(linkedList1);
    }

    @Test
    public void test105() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test105");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("hi!");
        boolean boolean3 = linkedList1.remove("");
        boolean boolean5 = linkedList1.remove("");
        boolean boolean7 = linkedList1.remove("hi!");
        // The following exception was thrown during execution in test generation
        try {
            java.lang.String str9 = linkedList1.get(3);
            org.junit.Assert.fail("Expected exception of type java.lang.IndexOutOfBoundsException; message: Index is out of range");
        } catch (java.lang.IndexOutOfBoundsException e) {
            // Expected exception.
        }
    }

    @Test
    public void test106() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test106");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        linkedList1.add("hi!");
        int int6 = linkedList1.size();
        int int7 = linkedList1.size();
        java.lang.String str8 = splitNjoin.utilities.StringUtils.join(linkedList1);
        java.lang.String str9 = splitNjoin.utilities.StringUtils.join(linkedList1);
        java.lang.String str11 = linkedList1.get(1);
        java.lang.Class<?> wildcardClass12 = linkedList1.getClass();
    }

    @Test
    public void test107() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test107");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        linkedList1.add("hi!");
        boolean boolean7 = linkedList1.remove("");
        boolean boolean9 = linkedList1.remove("hi!");
        // The following exception was thrown during execution in test generation
        try {
            java.lang.String str11 = linkedList1.get((int) (short) -1);
            org.junit.Assert.fail("Expected exception of type java.lang.IndexOutOfBoundsException; message: Index is out of range");
        } catch (java.lang.IndexOutOfBoundsException e) {
            // Expected exception.
        }
    }

    @Test
    public void test108() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test108");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("hi!");
        int int4 = linkedList1.size();
        java.lang.String str6 = linkedList1.get((int) (short) 0);
        boolean boolean8 = linkedList1.remove("hi!");
        java.lang.String str9 = splitNjoin.utilities.StringUtils.join(linkedList1);
        int int10 = linkedList1.size();
        boolean boolean12 = linkedList1.remove("hi! ");
    }

    @Test
    public void test109() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test109");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        java.lang.String str2 = splitNjoin.utilities.StringUtils.join(linkedList1);
        java.lang.Class<?> wildcardClass3 = linkedList1.getClass();
    }

    @Test
    public void test110() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test110");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("hi!");
        int int4 = linkedList1.size();
        java.lang.String str6 = linkedList1.get((int) (short) 0);
        boolean boolean8 = linkedList1.remove("hi!");
        // The following exception was thrown during execution in test generation
        try {
            java.lang.String str10 = linkedList1.get((int) (short) 0);
            org.junit.Assert.fail("Expected exception of type java.lang.IndexOutOfBoundsException; message: Index is out of range");
        } catch (java.lang.IndexOutOfBoundsException e) {
            // Expected exception.
        }
    }

    @Test
    public void test111() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test111");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        linkedList1.add("hi!");
        int int6 = linkedList1.size();
        int int7 = linkedList1.size();
        java.lang.String str8 = splitNjoin.utilities.StringUtils.join(linkedList1);
        java.lang.String str9 = splitNjoin.utilities.StringUtils.join(linkedList1);
        int int10 = linkedList1.size();
        java.lang.String str11 = splitNjoin.utilities.StringUtils.join(linkedList1);
        int int12 = linkedList1.size();
        int int13 = linkedList1.size();
    }

    @Test
    public void test112() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test112");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        int int4 = linkedList1.size();
        linkedList1.add("");
        int int7 = linkedList1.size();
        // The following exception was thrown during execution in test generation
        try {
            java.lang.String str9 = linkedList1.get(3);
            org.junit.Assert.fail("Expected exception of type java.lang.IndexOutOfBoundsException; message: Index is out of range");
        } catch (java.lang.IndexOutOfBoundsException e) {
            // Expected exception.
        }
    }

    @Test
    public void test113() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test113");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        linkedList1.add("hi!");
        boolean boolean7 = linkedList1.remove("");
        boolean boolean9 = linkedList1.remove("hi!");
        int int10 = linkedList1.size();
        java.lang.String str11 = splitNjoin.utilities.StringUtils.join(linkedList1);
        java.lang.String str12 = splitNjoin.utilities.StringUtils.join(linkedList1);
        boolean boolean14 = linkedList1.remove("hi! ");
        java.lang.Class<?> wildcardClass15 = linkedList1.getClass();
    }

    @Test
    public void test114() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test114");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        int int4 = linkedList1.size();
        linkedList1.add("");
        int int7 = linkedList1.size();
        int int8 = linkedList1.size();
        java.lang.Class<?> wildcardClass9 = linkedList1.getClass();
    }

    @Test
    public void test115() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test115");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        linkedList1.add("hi!");
        int int6 = linkedList1.size();
        int int7 = linkedList1.size();
        int int8 = linkedList1.size();
        boolean boolean10 = linkedList1.remove("");
        int int11 = linkedList1.size();
        boolean boolean13 = linkedList1.remove("");
        // The following exception was thrown during execution in test generation
        try {
            java.lang.String str15 = linkedList1.get((int) (short) 1);
            org.junit.Assert.fail("Expected exception of type java.lang.IndexOutOfBoundsException; message: Index is out of range");
        } catch (java.lang.IndexOutOfBoundsException e) {
            // Expected exception.
        }
    }

    @Test
    public void test116() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test116");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        int int4 = linkedList1.size();
        int int5 = linkedList1.size();
        int int6 = linkedList1.size();
        java.lang.String str7 = splitNjoin.utilities.StringUtils.join(linkedList1);
    }

    @Test
    public void test117() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test117");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        int int4 = linkedList1.size();
        linkedList1.add("");
        int int7 = linkedList1.size();
        int int8 = linkedList1.size();
        linkedList1.add("hi! hi! ");
    }

    @Test
    public void test118() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test118");
        splitNjoin.list.LinkedList linkedList0 = new splitNjoin.list.LinkedList();
        boolean boolean2 = linkedList0.remove("hi!");
        java.lang.String str3 = splitNjoin.utilities.StringUtils.join(linkedList0);
        // The following exception was thrown during execution in test generation
        try {
            java.lang.String str5 = linkedList0.get((int) (short) 10);
            org.junit.Assert.fail("Expected exception of type java.lang.IndexOutOfBoundsException; message: Index is out of range");
        } catch (java.lang.IndexOutOfBoundsException e) {
            // Expected exception.
        }
    }

    @Test
    public void test119() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test119");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("hi! hi! ");
    }

    @Test
    public void test120() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test120");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("hi! hi!");
        int int2 = linkedList1.size();
        int int3 = linkedList1.size();
    }

    @Test
    public void test121() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test121");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        linkedList1.add("hi!");
        int int6 = linkedList1.size();
        int int7 = linkedList1.size();
        java.lang.String str8 = splitNjoin.utilities.StringUtils.join(linkedList1);
        java.lang.String str9 = splitNjoin.utilities.StringUtils.join(linkedList1);
        java.lang.String str10 = splitNjoin.utilities.StringUtils.join(linkedList1);
        linkedList1.add("hi! ");
        java.lang.String str13 = splitNjoin.utilities.StringUtils.join(linkedList1);
        // The following exception was thrown during execution in test generation
        try {
            java.lang.String str15 = linkedList1.get((int) ' ');
            org.junit.Assert.fail("Expected exception of type java.lang.IndexOutOfBoundsException; message: Index is out of range");
        } catch (java.lang.IndexOutOfBoundsException e) {
            // Expected exception.
        }
    }

    @Test
    public void test122() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test122");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("hi!");
        boolean boolean3 = linkedList1.remove("");
        boolean boolean5 = linkedList1.remove("");
        boolean boolean7 = linkedList1.remove("hi!");
        // The following exception was thrown during execution in test generation
        try {
            java.lang.String str9 = linkedList1.get(2);
            org.junit.Assert.fail("Expected exception of type java.lang.IndexOutOfBoundsException; message: Index is out of range");
        } catch (java.lang.IndexOutOfBoundsException e) {
            // Expected exception.
        }
    }

    @Test
    public void test123() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test123");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("hi! ");
        boolean boolean3 = linkedList1.remove("hi!");
    }

    @Test
    public void test124() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test124");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        linkedList1.add("hi!");
        int int6 = linkedList1.size();
        int int7 = linkedList1.size();
        java.lang.String str8 = splitNjoin.utilities.StringUtils.join(linkedList1);
        java.lang.String str9 = splitNjoin.utilities.StringUtils.join(linkedList1);
        int int10 = linkedList1.size();
        java.lang.String str11 = splitNjoin.utilities.StringUtils.join(linkedList1);
        java.lang.String str12 = splitNjoin.utilities.StringUtils.join(linkedList1);
        // The following exception was thrown during execution in test generation
        try {
            java.lang.String str14 = linkedList1.get((int) (short) 100);
            org.junit.Assert.fail("Expected exception of type java.lang.IndexOutOfBoundsException; message: Index is out of range");
        } catch (java.lang.IndexOutOfBoundsException e) {
            // Expected exception.
        }
    }

    @Test
    public void test125() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test125");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("hi!");
        int int4 = linkedList1.size();
        java.lang.String str6 = linkedList1.get((int) (short) 0);
        boolean boolean8 = linkedList1.remove("hi!");
        java.lang.String str9 = splitNjoin.utilities.StringUtils.join(linkedList1);
        java.lang.String str10 = splitNjoin.utilities.StringUtils.join(linkedList1);
    }

    @Test
    public void test126() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test126");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("hi!");
        boolean boolean3 = linkedList1.remove("");
        boolean boolean5 = linkedList1.remove("");
        boolean boolean7 = linkedList1.remove("");
        linkedList1.add("hi! hi!");
    }

    @Test
    public void test127() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test127");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        linkedList1.add("");
        linkedList1.add("hi!");
        java.lang.String str8 = splitNjoin.utilities.StringUtils.join(linkedList1);
        int int9 = linkedList1.size();
        linkedList1.add("");
        java.lang.Class<?> wildcardClass12 = linkedList1.getClass();
    }

    @Test
    public void test128() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test128");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        boolean boolean3 = linkedList1.remove("hi!");
        boolean boolean5 = linkedList1.remove("hi!");
        java.lang.String str6 = splitNjoin.utilities.StringUtils.join(linkedList1);
    }

    @Test
    public void test129() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test129");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("hi!");
        int int4 = linkedList1.size();
        java.lang.String str6 = linkedList1.get((int) (short) 0);
        boolean boolean8 = linkedList1.remove("");
        boolean boolean10 = linkedList1.remove("");
    }

    @Test
    public void test130() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test130");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("hi!");
        boolean boolean3 = linkedList1.remove("");
        boolean boolean5 = linkedList1.remove("");
        boolean boolean7 = linkedList1.remove("");
        boolean boolean9 = linkedList1.remove("hi! hi!");
        // The following exception was thrown during execution in test generation
        try {
            java.lang.String str11 = linkedList1.get((int) '4');
            org.junit.Assert.fail("Expected exception of type java.lang.IndexOutOfBoundsException; message: Index is out of range");
        } catch (java.lang.IndexOutOfBoundsException e) {
            // Expected exception.
        }
    }

    @Test
    public void test131() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test131");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        boolean boolean3 = linkedList1.remove("hi!");
        boolean boolean5 = linkedList1.remove("hi!");
        // The following exception was thrown during execution in test generation
        try {
            java.lang.String str7 = linkedList1.get((int) (byte) 100);
            org.junit.Assert.fail("Expected exception of type java.lang.IndexOutOfBoundsException; message: Index is out of range");
        } catch (java.lang.IndexOutOfBoundsException e) {
            // Expected exception.
        }
    }

    @Test
    public void test132() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test132");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("hi! hi!");
        int int2 = linkedList1.size();
        // The following exception was thrown during execution in test generation
        try {
            java.lang.String str4 = linkedList1.get((int) (short) 100);
            org.junit.Assert.fail("Expected exception of type java.lang.IndexOutOfBoundsException; message: Index is out of range");
        } catch (java.lang.IndexOutOfBoundsException e) {
            // Expected exception.
        }
    }

    @Test
    public void test133() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test133");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        linkedList1.add("hi!");
        int int6 = linkedList1.size();
        int int7 = linkedList1.size();
        int int8 = linkedList1.size();
        linkedList1.add("hi! hi!");
    }

    @Test
    public void test134() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test134");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        linkedList1.add("hi!");
        int int6 = linkedList1.size();
        int int7 = linkedList1.size();
        java.lang.String str8 = splitNjoin.utilities.StringUtils.join(linkedList1);
        java.lang.String str9 = splitNjoin.utilities.StringUtils.join(linkedList1);
        int int10 = linkedList1.size();
        int int11 = linkedList1.size();
        // The following exception was thrown during execution in test generation
        try {
            java.lang.String str13 = linkedList1.get((int) (short) 10);
            org.junit.Assert.fail("Expected exception of type java.lang.IndexOutOfBoundsException; message: Index is out of range");
        } catch (java.lang.IndexOutOfBoundsException e) {
            // Expected exception.
        }
    }

    @Test
    public void test135() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test135");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        linkedList1.add("hi!");
        int int6 = linkedList1.size();
        int int7 = linkedList1.size();
        java.lang.String str8 = splitNjoin.utilities.StringUtils.join(linkedList1);
        java.lang.String str9 = splitNjoin.utilities.StringUtils.join(linkedList1);
        int int10 = linkedList1.size();
        linkedList1.add("");
        java.lang.String str13 = splitNjoin.utilities.StringUtils.join(linkedList1);
        int int14 = linkedList1.size();
    }

    @Test
    public void test136() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test136");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        linkedList1.add("hi!");
        int int6 = linkedList1.size();
        int int7 = linkedList1.size();
        java.lang.String str8 = splitNjoin.utilities.StringUtils.join(linkedList1);
        java.lang.String str9 = splitNjoin.utilities.StringUtils.join(linkedList1);
        java.lang.String str10 = splitNjoin.utilities.StringUtils.join(linkedList1);
        linkedList1.add("hi! ");
        java.lang.String str13 = splitNjoin.utilities.StringUtils.join(linkedList1);
        java.lang.String str14 = splitNjoin.utilities.StringUtils.join(linkedList1);
    }

    @Test
    public void test137() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test137");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        linkedList1.add("hi!");
        boolean boolean7 = linkedList1.remove("");
        boolean boolean9 = linkedList1.remove("hi!");
        java.lang.String str10 = splitNjoin.utilities.StringUtils.join(linkedList1);
        linkedList1.add("hi!");
        java.lang.String str14 = linkedList1.get((int) (short) -1);
        int int15 = linkedList1.size();
    }

    @Test
    public void test138() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test138");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("hi!");
        boolean boolean3 = linkedList1.remove("");
        boolean boolean5 = linkedList1.remove("");
        boolean boolean7 = linkedList1.remove("hi!");
        int int8 = linkedList1.size();
        int int9 = linkedList1.size();
    }

    @Test
    public void test139() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test139");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        linkedList1.add("hi!");
        int int6 = linkedList1.size();
        int int7 = linkedList1.size();
        java.lang.String str8 = splitNjoin.utilities.StringUtils.join(linkedList1);
        java.lang.String str9 = splitNjoin.utilities.StringUtils.join(linkedList1);
        int int10 = linkedList1.size();
        java.lang.String str11 = splitNjoin.utilities.StringUtils.join(linkedList1);
        java.lang.String str12 = splitNjoin.utilities.StringUtils.join(linkedList1);
        // The following exception was thrown during execution in test generation
        try {
            java.lang.String str14 = linkedList1.get((int) ' ');
            org.junit.Assert.fail("Expected exception of type java.lang.IndexOutOfBoundsException; message: Index is out of range");
        } catch (java.lang.IndexOutOfBoundsException e) {
            // Expected exception.
        }
    }

    @Test
    public void test140() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test140");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        linkedList1.add("hi!");
        int int6 = linkedList1.size();
        int int7 = linkedList1.size();
        int int8 = linkedList1.size();
        boolean boolean10 = linkedList1.remove("");
        java.lang.String str11 = splitNjoin.utilities.StringUtils.join(linkedList1);
    }

    @Test
    public void test141() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test141");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        linkedList1.add("hi!");
        int int6 = linkedList1.size();
        int int7 = linkedList1.size();
        int int8 = linkedList1.size();
        boolean boolean10 = linkedList1.remove("hi!");
        int int11 = linkedList1.size();
    }

    @Test
    public void test142() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test142");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("hi! ");
        boolean boolean3 = linkedList1.remove("");
        int int4 = linkedList1.size();
    }

    @Test
    public void test143() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test143");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("hi!");
        boolean boolean3 = linkedList1.remove("");
        java.lang.String str4 = splitNjoin.utilities.StringUtils.join(linkedList1);
        java.lang.Class<?> wildcardClass5 = linkedList1.getClass();
    }

    @Test
    public void test144() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test144");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("hi!");
        boolean boolean3 = linkedList1.remove("");
        boolean boolean5 = linkedList1.remove("");
        boolean boolean7 = linkedList1.remove("");
        linkedList1.add("hi!");
        int int10 = linkedList1.size();
    }

    @Test
    public void test145() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test145");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        linkedList1.add("hi!");
        java.lang.String str6 = splitNjoin.utilities.StringUtils.join(linkedList1);
        java.lang.String str8 = linkedList1.get((int) (byte) -1);
    }

    @Test
    public void test146() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test146");
        splitNjoin.list.LinkedList linkedList0 = new splitNjoin.list.LinkedList();
        linkedList0.add("hi!");
        linkedList0.add("");
        boolean boolean6 = linkedList0.remove("");
        java.lang.String str7 = splitNjoin.utilities.StringUtils.join(linkedList0);
        java.lang.String str8 = splitNjoin.utilities.StringUtils.join(linkedList0);
    }

    @Test
    public void test147() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test147");
        splitNjoin.list.LinkedList linkedList0 = new splitNjoin.list.LinkedList();
        boolean boolean2 = linkedList0.remove("hi!");
        java.lang.String str3 = splitNjoin.utilities.StringUtils.join(linkedList0);
        int int4 = linkedList0.size();
        int int5 = linkedList0.size();
        boolean boolean7 = linkedList0.remove("hi!");
    }

    @Test
    public void test148() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test148");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        boolean boolean5 = linkedList1.remove("hi!");
        java.lang.String str6 = splitNjoin.utilities.StringUtils.join(linkedList1);
        boolean boolean8 = linkedList1.remove("hi! hi!");
        linkedList1.add("hi! hi! ");
    }

    @Test
    public void test149() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test149");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        linkedList1.add("hi!");
        int int6 = linkedList1.size();
        int int7 = linkedList1.size();
        java.lang.String str8 = splitNjoin.utilities.StringUtils.join(linkedList1);
        java.lang.String str9 = splitNjoin.utilities.StringUtils.join(linkedList1);
        int int10 = linkedList1.size();
        java.lang.String str11 = splitNjoin.utilities.StringUtils.join(linkedList1);
        java.lang.String str12 = splitNjoin.utilities.StringUtils.join(linkedList1);
        int int13 = linkedList1.size();
        java.lang.String str14 = splitNjoin.utilities.StringUtils.join(linkedList1);
    }

    @Test
    public void test150() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test150");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("hi! ");
        boolean boolean3 = linkedList1.remove("hi! ");
        // The following exception was thrown during execution in test generation
        try {
            java.lang.String str5 = linkedList1.get((int) (byte) 100);
            org.junit.Assert.fail("Expected exception of type java.lang.IndexOutOfBoundsException; message: Index is out of range");
        } catch (java.lang.IndexOutOfBoundsException e) {
            // Expected exception.
        }
    }

    @Test
    public void test151() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test151");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        boolean boolean5 = linkedList1.remove("hi!");
        java.lang.String str6 = splitNjoin.utilities.StringUtils.join(linkedList1);
        boolean boolean8 = linkedList1.remove("hi! hi!");
        java.lang.String str9 = splitNjoin.utilities.StringUtils.join(linkedList1);
    }

    @Test
    public void test152() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test152");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        linkedList1.add("hi!");
        int int6 = linkedList1.size();
        linkedList1.add("hi!");
        java.lang.String str9 = splitNjoin.utilities.StringUtils.join(linkedList1);
        boolean boolean11 = linkedList1.remove("hi! hi! ");
        boolean boolean13 = linkedList1.remove("hi!");
    }

    @Test
    public void test153() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test153");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        linkedList1.add("hi!");
        boolean boolean7 = linkedList1.remove("");
        boolean boolean9 = linkedList1.remove("hi!");
        int int10 = linkedList1.size();
        // The following exception was thrown during execution in test generation
        try {
            java.lang.String str12 = linkedList1.get((int) (byte) 1);
            org.junit.Assert.fail("Expected exception of type java.lang.IndexOutOfBoundsException; message: Index is out of range");
        } catch (java.lang.IndexOutOfBoundsException e) {
            // Expected exception.
        }
    }

    @Test
    public void test154() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test154");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        linkedList1.add("hi!");
        java.lang.String str6 = splitNjoin.utilities.StringUtils.join(linkedList1);
        linkedList1.add("");
        java.lang.String str9 = splitNjoin.utilities.StringUtils.join(linkedList1);
    }

    @Test
    public void test155() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test155");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("hi!");
        boolean boolean3 = linkedList1.remove("");
        boolean boolean5 = linkedList1.remove("");
        boolean boolean7 = linkedList1.remove("hi!");
        int int8 = linkedList1.size();
        java.lang.String str9 = splitNjoin.utilities.StringUtils.join(linkedList1);
        java.lang.String str10 = splitNjoin.utilities.StringUtils.join(linkedList1);
        linkedList1.add("hi! hi! ");
        int int13 = linkedList1.size();
        int int14 = linkedList1.size();
    }

    @Test
    public void test156() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test156");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        linkedList1.add("");
        linkedList1.add("hi!");
        int int8 = linkedList1.size();
        java.lang.String str10 = linkedList1.get((-1));
        java.lang.Class<?> wildcardClass11 = linkedList1.getClass();
    }

    @Test
    public void test157() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test157");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        linkedList1.add("hi!");
        int int6 = linkedList1.size();
        int int7 = linkedList1.size();
        java.lang.String str8 = splitNjoin.utilities.StringUtils.join(linkedList1);
        java.lang.String str9 = splitNjoin.utilities.StringUtils.join(linkedList1);
        java.lang.String str11 = linkedList1.get(1);
        int int12 = linkedList1.size();
        int int13 = linkedList1.size();
    }

    @Test
    public void test158() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test158");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        boolean boolean5 = linkedList1.remove("hi!");
        java.lang.String str7 = linkedList1.get((int) (short) -1);
        java.lang.String str9 = linkedList1.get((int) (short) -1);
        boolean boolean11 = linkedList1.remove("");
        boolean boolean13 = linkedList1.remove("hi! hi! ");
    }

    @Test
    public void test159() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test159");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        linkedList1.add("hi!");
        int int6 = linkedList1.size();
        int int7 = linkedList1.size();
        int int8 = linkedList1.size();
        boolean boolean10 = linkedList1.remove("");
        int int11 = linkedList1.size();
        boolean boolean13 = linkedList1.remove("");
        boolean boolean15 = linkedList1.remove("hi!");
    }

    @Test
    public void test160() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test160");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        linkedList1.add("hi!");
        java.lang.String str6 = splitNjoin.utilities.StringUtils.join(linkedList1);
        linkedList1.add("");
        boolean boolean10 = linkedList1.remove("hi! ");
    }

    @Test
    public void test161() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test161");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("hi! hi!");
        int int2 = linkedList1.size();
        linkedList1.add("hi!");
        linkedList1.add("hi!");
    }

    @Test
    public void test162() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test162");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("hi!");
        linkedList1.add("");
    }

    @Test
    public void test163() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test163");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        boolean boolean5 = linkedList1.remove("");
        linkedList1.add("hi!");
    }

    @Test
    public void test164() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test164");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("hi!");
        boolean boolean5 = linkedList1.remove("hi!");
        // The following exception was thrown during execution in test generation
        try {
            java.lang.String str7 = linkedList1.get(0);
            org.junit.Assert.fail("Expected exception of type java.lang.IndexOutOfBoundsException; message: Index is out of range");
        } catch (java.lang.IndexOutOfBoundsException e) {
            // Expected exception.
        }
    }

    @Test
    public void test165() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test165");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("hi!");
        boolean boolean3 = linkedList1.remove("");
        boolean boolean5 = linkedList1.remove("");
        boolean boolean7 = linkedList1.remove("hi!");
        int int8 = linkedList1.size();
        java.lang.String str9 = splitNjoin.utilities.StringUtils.join(linkedList1);
        linkedList1.add("hi! hi! ");
    }

    @Test
    public void test166() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test166");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("hi!");
        boolean boolean3 = linkedList1.remove("");
        boolean boolean5 = linkedList1.remove("");
        boolean boolean7 = linkedList1.remove("hi!");
        int int8 = linkedList1.size();
        java.lang.String str9 = splitNjoin.utilities.StringUtils.join(linkedList1);
        java.lang.String str10 = splitNjoin.utilities.StringUtils.join(linkedList1);
        linkedList1.add("hi! hi! ");
        linkedList1.add("hi! ");
    }

    @Test
    public void test167() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test167");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        linkedList1.add("hi!");
        int int6 = linkedList1.size();
        int int7 = linkedList1.size();
        java.lang.String str8 = splitNjoin.utilities.StringUtils.join(linkedList1);
        java.lang.String str9 = splitNjoin.utilities.StringUtils.join(linkedList1);
        java.lang.String str11 = linkedList1.get(1);
        int int12 = linkedList1.size();
        java.lang.Class<?> wildcardClass13 = linkedList1.getClass();
    }

    @Test
    public void test168() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test168");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        linkedList1.add("hi!");
        int int6 = linkedList1.size();
        int int7 = linkedList1.size();
        java.lang.String str8 = splitNjoin.utilities.StringUtils.join(linkedList1);
        java.lang.String str9 = splitNjoin.utilities.StringUtils.join(linkedList1);
        java.lang.String str10 = splitNjoin.utilities.StringUtils.join(linkedList1);
        linkedList1.add("hi! ");
        java.lang.String str14 = linkedList1.get(0);
        java.lang.String str16 = linkedList1.get(1);
        // The following exception was thrown during execution in test generation
        try {
            java.lang.String str18 = linkedList1.get((int) (short) 100);
            org.junit.Assert.fail("Expected exception of type java.lang.IndexOutOfBoundsException; message: Index is out of range");
        } catch (java.lang.IndexOutOfBoundsException e) {
            // Expected exception.
        }
    }

    @Test
    public void test169() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test169");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("hi!");
        boolean boolean3 = linkedList1.remove("");
        boolean boolean5 = linkedList1.remove("");
        boolean boolean7 = linkedList1.remove("");
        boolean boolean9 = linkedList1.remove("hi! hi!");
        int int10 = linkedList1.size();
    }

    @Test
    public void test170() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test170");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        boolean boolean5 = linkedList1.remove("hi!");
        java.lang.String str7 = linkedList1.get((int) (short) -1);
        // The following exception was thrown during execution in test generation
        try {
            java.lang.String str9 = linkedList1.get(3);
            org.junit.Assert.fail("Expected exception of type java.lang.IndexOutOfBoundsException; message: Index is out of range");
        } catch (java.lang.IndexOutOfBoundsException e) {
            // Expected exception.
        }
    }

    @Test
    public void test171() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test171");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("hi!");
        int int4 = linkedList1.size();
        java.lang.String str6 = linkedList1.get((int) (short) 0);
        boolean boolean8 = linkedList1.remove("hi!");
        java.lang.String str9 = splitNjoin.utilities.StringUtils.join(linkedList1);
        int int10 = linkedList1.size();
        boolean boolean12 = linkedList1.remove("");
        int int13 = linkedList1.size();
    }

    @Test
    public void test172() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test172");
        splitNjoin.list.LinkedList linkedList0 = new splitNjoin.list.LinkedList();
        linkedList0.add("hi!");
        linkedList0.add("");
        int int5 = linkedList0.size();
        int int6 = linkedList0.size();
        boolean boolean8 = linkedList0.remove("hi! hi! ");
    }

    @Test
    public void test173() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test173");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        linkedList1.add("hi!");
        boolean boolean7 = linkedList1.remove("");
        boolean boolean9 = linkedList1.remove("hi!");
        int int10 = linkedList1.size();
        java.lang.String str11 = splitNjoin.utilities.StringUtils.join(linkedList1);
        boolean boolean13 = linkedList1.remove("");
    }

    @Test
    public void test174() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test174");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        linkedList1.add("hi!");
        int int6 = linkedList1.size();
        int int7 = linkedList1.size();
        java.lang.String str8 = splitNjoin.utilities.StringUtils.join(linkedList1);
        java.lang.String str9 = splitNjoin.utilities.StringUtils.join(linkedList1);
        java.lang.String str10 = splitNjoin.utilities.StringUtils.join(linkedList1);
        linkedList1.add("hi! ");
        java.lang.String str13 = splitNjoin.utilities.StringUtils.join(linkedList1);
        boolean boolean15 = linkedList1.remove("hi! hi!");
        boolean boolean17 = linkedList1.remove("");
    }

    @Test
    public void test175() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test175");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        linkedList1.add("");
        linkedList1.add("hi!");
        java.lang.String str8 = splitNjoin.utilities.StringUtils.join(linkedList1);
        java.lang.String str10 = linkedList1.get((int) (byte) 1);
        boolean boolean12 = linkedList1.remove("");
    }

    @Test
    public void test176() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test176");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        linkedList1.add("hi!");
        int int6 = linkedList1.size();
        int int7 = linkedList1.size();
        linkedList1.add("hi!");
        linkedList1.add("hi!");
    }

    @Test
    public void test177() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test177");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        linkedList1.add("");
        linkedList1.add("hi!");
        java.lang.String str8 = splitNjoin.utilities.StringUtils.join(linkedList1);
        int int9 = linkedList1.size();
        java.lang.String str10 = splitNjoin.utilities.StringUtils.join(linkedList1);
    }

    @Test
    public void test178() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test178");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        linkedList1.add("");
        linkedList1.add("hi!");
        java.lang.String str6 = splitNjoin.utilities.StringUtils.join(linkedList1);
        int int7 = linkedList1.size();
        boolean boolean9 = linkedList1.remove("");
        boolean boolean11 = linkedList1.remove("hi! hi!");
    }

    @Test
    public void test179() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test179");
        splitNjoin.list.LinkedList linkedList1 = splitNjoin.utilities.StringUtils.split("");
        boolean boolean3 = linkedList1.remove("hi!");
        boolean boolean5 = linkedList1.remove("");
    }
}

