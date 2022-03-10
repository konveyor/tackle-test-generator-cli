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
        java.lang.Object obj0 = new java.lang.Object();
        java.lang.Class<?> wildcardClass1 = obj0.getClass();
        org.junit.Assert.assertNotNull(wildcardClass1);
    }

    @Test
    public void test002() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test002");
        failing.Failing2 failing2_0 = new failing.Failing2();
        int int1 = failing2_0.myIntInput;
        java.lang.Class<?> wildcardClass2 = failing2_0.getClass();
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 0 + "'", int1 == 0);
        org.junit.Assert.assertNotNull(wildcardClass2);
    }

    @Test
    public void test003() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test003");
        failing.Failing2 failing2_0 = new failing.Failing2();
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException((int) (short) 0);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
    }

    @Test
    public void test004() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test004");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (short) 1;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException((int) (short) 1);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
    }

    @Test
    public void test005() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test005");
        failing.Failing2 failing2_0 = new failing.Failing2();
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException((int) (byte) 100);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
    }

    @Test
    public void test006() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test006");
        failing.Failing2 failing2_0 = new failing.Failing2();
        int int1 = failing2_0.myIntInput;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException((int) (byte) 10);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 0 + "'", int1 == 0);
    }

    @Test
    public void test007() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test007");
        failing.Failing2 failing2_0 = new failing.Failing2();
        int int1 = failing2_0.myIntInput;
        failing2_0.myIntInput = 'a';
        int int4 = failing2_0.myIntInput;
        java.lang.Class<?> wildcardClass5 = failing2_0.getClass();
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 0 + "'", int1 == 0);
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + 97 + "'", int4 == 97);
        org.junit.Assert.assertNotNull(wildcardClass5);
    }

    @Test
    public void test008() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test008");
        failing.Failing2 failing2_0 = new failing.Failing2();
        int int1 = failing2_0.myIntInput;
        failing2_0.myIntInput = 'a';
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException(0);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 0 + "'", int1 == 0);
    }

    @Test
    public void test009() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test009");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (short) 1;
        java.lang.Class<?> wildcardClass3 = failing2_0.getClass();
        org.junit.Assert.assertNotNull(wildcardClass3);
    }

    @Test
    public void test010() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test010");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (short) 1;
        int int3 = failing2_0.myIntInput;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException(1);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + 1 + "'", int3 == 1);
    }

    @Test
    public void test011() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test011");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        java.lang.Class<?> wildcardClass3 = failing2_0.getClass();
        org.junit.Assert.assertNotNull(wildcardClass3);
    }

    @Test
    public void test012() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test012");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (short) 1;
        int int3 = failing2_0.myIntInput;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException((int) (short) -1);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + 1 + "'", int3 == 1);
    }

    @Test
    public void test013() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test013");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        failing2_0.myIntInput = 1;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException(10);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
    }

    @Test
    public void test014() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test014");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        int int3 = failing2_0.myIntInput;
        int int4 = failing2_0.myIntInput;
        java.lang.Class<?> wildcardClass5 = failing2_0.getClass();
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + (-1) + "'", int3 == (-1));
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + (-1) + "'", int4 == (-1));
        org.junit.Assert.assertNotNull(wildcardClass5);
    }

    @Test
    public void test015() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test015");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        int int3 = failing2_0.myIntInput;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException((int) (byte) 100);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + (-1) + "'", int3 == (-1));
    }

    @Test
    public void test016() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test016");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (short) 1;
        int int3 = failing2_0.myIntInput;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException(10);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + 1 + "'", int3 == 1);
    }

    @Test
    public void test017() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test017");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        int int3 = failing2_0.myIntInput;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException(0);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + (-1) + "'", int3 == (-1));
    }

    @Test
    public void test018() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test018");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        failing2_0.myIntInput = 1;
        java.lang.Class<?> wildcardClass5 = failing2_0.getClass();
        org.junit.Assert.assertNotNull(wildcardClass5);
    }

    @Test
    public void test019() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test019");
        failing.Failing2 failing2_0 = new failing.Failing2();
        int int1 = failing2_0.myIntInput;
        failing2_0.myIntInput = 'a';
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException(97);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 0 + "'", int1 == 0);
    }

    @Test
    public void test020() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test020");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        failing2_0.myIntInput = 1;
        int int5 = failing2_0.myIntInput;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException(97);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int5 + "' != '" + 1 + "'", int5 == 1);
    }

    @Test
    public void test021() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test021");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        failing2_0.myIntInput = 1;
        int int5 = failing2_0.myIntInput;
        failing2_0.myIntInput = '4';
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException(52);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int5 + "' != '" + 1 + "'", int5 == 1);
    }

    @Test
    public void test022() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test022");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (short) 1;
        int int3 = failing2_0.myIntInput;
        int int4 = failing2_0.myIntInput;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException((int) '4');
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + 1 + "'", int3 == 1);
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + 1 + "'", int4 == 1);
    }

    @Test
    public void test023() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test023");
        failing.Failing2 failing2_0 = new failing.Failing2();
        int int1 = failing2_0.myIntInput;
        failing2_0.myIntInput = 'a';
        int int4 = failing2_0.myIntInput;
        failing2_0.myIntInput = 97;
        int int7 = failing2_0.myIntInput;
        failing2_0.myIntInput = '4';
        int int10 = failing2_0.myIntInput;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException((int) (short) -1);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 0 + "'", int1 == 0);
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + 97 + "'", int4 == 97);
        org.junit.Assert.assertTrue("'" + int7 + "' != '" + 97 + "'", int7 == 97);
        org.junit.Assert.assertTrue("'" + int10 + "' != '" + 52 + "'", int10 == 52);
    }

    @Test
    public void test024() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test024");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        failing2_0.myIntInput = 1;
        failing2_0.myIntInput = 0;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException((-1));
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
    }

    @Test
    public void test025() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test025");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        failing2_0.myIntInput = 1;
        int int5 = failing2_0.myIntInput;
        failing2_0.myIntInput = '4';
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException(1);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int5 + "' != '" + 1 + "'", int5 == 1);
    }

    @Test
    public void test026() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test026");
        failing.Failing2 failing2_0 = new failing.Failing2();
        int int1 = failing2_0.myIntInput;
        int int2 = failing2_0.myIntInput;
        java.lang.Class<?> wildcardClass3 = failing2_0.getClass();
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 0 + "'", int1 == 0);
        org.junit.Assert.assertTrue("'" + int2 + "' != '" + 0 + "'", int2 == 0);
        org.junit.Assert.assertNotNull(wildcardClass3);
    }

    @Test
    public void test027() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test027");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (short) 1;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException((int) (short) 0);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
    }

    @Test
    public void test028() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test028");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        failing2_0.myIntInput = 1;
        failing2_0.myIntInput = 0;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException(0);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
    }

    @Test
    public void test029() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test029");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        int int3 = failing2_0.myIntInput;
        int int4 = failing2_0.myIntInput;
        failing2_0.myIntInput = ' ';
        failing2_0.myIntInput = (byte) 0;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException((int) (byte) -1);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + (-1) + "'", int3 == (-1));
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + (-1) + "'", int4 == (-1));
    }

    @Test
    public void test030() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test030");
        failing.Failing2 failing2_0 = new failing.Failing2();
        int int1 = failing2_0.myIntInput;
        int int2 = failing2_0.myIntInput;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException(52);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 0 + "'", int1 == 0);
        org.junit.Assert.assertTrue("'" + int2 + "' != '" + 0 + "'", int2 == 0);
    }

    @Test
    public void test031() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test031");
        failing.Failing2 failing2_0 = new failing.Failing2();
        int int1 = failing2_0.myIntInput;
        failing2_0.myIntInput = 'a';
        int int4 = failing2_0.myIntInput;
        failing2_0.myIntInput = 97;
        int int7 = failing2_0.myIntInput;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException((-1));
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 0 + "'", int1 == 0);
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + 97 + "'", int4 == 97);
        org.junit.Assert.assertTrue("'" + int7 + "' != '" + 97 + "'", int7 == 97);
    }

    @Test
    public void test032() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test032");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        int int3 = failing2_0.myIntInput;
        int int4 = failing2_0.myIntInput;
        failing2_0.myIntInput = ' ';
        java.lang.Class<?> wildcardClass7 = failing2_0.getClass();
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + (-1) + "'", int3 == (-1));
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + (-1) + "'", int4 == (-1));
        org.junit.Assert.assertNotNull(wildcardClass7);
    }

    @Test
    public void test033() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test033");
        failing.Failing2 failing2_0 = new failing.Failing2();
        int int1 = failing2_0.myIntInput;
        failing2_0.myIntInput = 'a';
        int int4 = failing2_0.myIntInput;
        failing2_0.myIntInput = (short) -1;
        java.lang.Class<?> wildcardClass7 = failing2_0.getClass();
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 0 + "'", int1 == 0);
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + 97 + "'", int4 == 97);
        org.junit.Assert.assertNotNull(wildcardClass7);
    }

    @Test
    public void test034() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test034");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException((int) (short) 10);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
    }

    @Test
    public void test035() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test035");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        failing2_0.myIntInput = 1;
        int int5 = failing2_0.myIntInput;
        failing2_0.myIntInput = '4';
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException((int) (byte) 0);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int5 + "' != '" + 1 + "'", int5 == 1);
    }

    @Test
    public void test036() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test036");
        failing.Failing2 failing2_0 = new failing.Failing2();
        int int1 = failing2_0.myIntInput;
        failing2_0.myIntInput = 'a';
        int int4 = failing2_0.myIntInput;
        failing2_0.myIntInput = 97;
        int int7 = failing2_0.myIntInput;
        failing2_0.myIntInput = '4';
        int int10 = failing2_0.myIntInput;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException((int) (byte) -1);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 0 + "'", int1 == 0);
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + 97 + "'", int4 == 97);
        org.junit.Assert.assertTrue("'" + int7 + "' != '" + 97 + "'", int7 == 97);
        org.junit.Assert.assertTrue("'" + int10 + "' != '" + 52 + "'", int10 == 52);
    }

    @Test
    public void test037() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test037");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        int int3 = failing2_0.myIntInput;
        int int4 = failing2_0.myIntInput;
        failing2_0.myIntInput = ' ';
        failing2_0.myIntInput = (byte) 0;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException((int) 'a');
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + (-1) + "'", int3 == (-1));
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + (-1) + "'", int4 == (-1));
    }

    @Test
    public void test038() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test038");
        failing.Failing2 failing2_0 = new failing.Failing2();
        int int1 = failing2_0.myIntInput;
        int int2 = failing2_0.myIntInput;
        failing2_0.myIntInput = (short) 1;
        java.lang.Class<?> wildcardClass5 = failing2_0.getClass();
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 0 + "'", int1 == 0);
        org.junit.Assert.assertTrue("'" + int2 + "' != '" + 0 + "'", int2 == 0);
        org.junit.Assert.assertNotNull(wildcardClass5);
    }

    @Test
    public void test039() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test039");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        int int3 = failing2_0.myIntInput;
        int int4 = failing2_0.myIntInput;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException((int) (short) -1);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + (-1) + "'", int3 == (-1));
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + (-1) + "'", int4 == (-1));
    }

    @Test
    public void test040() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test040");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        failing2_0.myIntInput = 1;
        failing2_0.myIntInput = 0;
        int int7 = failing2_0.myIntInput;
        java.lang.Class<?> wildcardClass8 = failing2_0.getClass();
        org.junit.Assert.assertTrue("'" + int7 + "' != '" + 0 + "'", int7 == 0);
        org.junit.Assert.assertNotNull(wildcardClass8);
    }

    @Test
    public void test041() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test041");
        failing.Failing2 failing2_0 = new failing.Failing2();
        int int1 = failing2_0.myIntInput;
        failing2_0.myIntInput = 'a';
        int int4 = failing2_0.myIntInput;
        failing2_0.myIntInput = 97;
        int int7 = failing2_0.myIntInput;
        failing2_0.myIntInput = (byte) 0;
        java.lang.Class<?> wildcardClass10 = failing2_0.getClass();
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 0 + "'", int1 == 0);
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + 97 + "'", int4 == 97);
        org.junit.Assert.assertTrue("'" + int7 + "' != '" + 97 + "'", int7 == 97);
        org.junit.Assert.assertNotNull(wildcardClass10);
    }

    @Test
    public void test042() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test042");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        failing2_0.myIntInput = 1;
        failing2_0.myIntInput = 0;
        int int7 = failing2_0.myIntInput;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException(97);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int7 + "' != '" + 0 + "'", int7 == 0);
    }

    @Test
    public void test043() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test043");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        failing2_0.myIntInput = 1;
        int int5 = failing2_0.myIntInput;
        failing2_0.myIntInput = '4';
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException((int) (short) 1);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int5 + "' != '" + 1 + "'", int5 == 1);
    }

    @Test
    public void test044() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test044");
        failing.Failing2 failing2_0 = new failing.Failing2();
        int int1 = failing2_0.myIntInput;
        failing2_0.myIntInput = 'a';
        failing2_0.myIntInput = 0;
        java.lang.Class<?> wildcardClass6 = failing2_0.getClass();
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 0 + "'", int1 == 0);
        org.junit.Assert.assertNotNull(wildcardClass6);
    }

    @Test
    public void test045() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test045");
        failing.Failing2 failing2_0 = new failing.Failing2();
        int int1 = failing2_0.myIntInput;
        failing2_0.myIntInput = 'a';
        int int4 = failing2_0.myIntInput;
        failing2_0.myIntInput = (short) -1;
        int int7 = failing2_0.myIntInput;
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 0 + "'", int1 == 0);
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + 97 + "'", int4 == 97);
        org.junit.Assert.assertTrue("'" + int7 + "' != '" + (-1) + "'", int7 == (-1));
    }

    @Test
    public void test046() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test046");
        failing.Failing2 failing2_0 = new failing.Failing2();
        java.lang.Class<?> wildcardClass1 = failing2_0.getClass();
        org.junit.Assert.assertNotNull(wildcardClass1);
    }

    @Test
    public void test047() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test047");
        failing.Failing2 failing2_0 = new failing.Failing2();
        int int1 = failing2_0.myIntInput;
        failing2_0.myIntInput = 'a';
        int int4 = failing2_0.myIntInput;
        failing2_0.myIntInput = 97;
        int int7 = failing2_0.myIntInput;
        failing2_0.myIntInput = '4';
        int int10 = failing2_0.myIntInput;
        java.lang.Class<?> wildcardClass11 = failing2_0.getClass();
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 0 + "'", int1 == 0);
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + 97 + "'", int4 == 97);
        org.junit.Assert.assertTrue("'" + int7 + "' != '" + 97 + "'", int7 == 97);
        org.junit.Assert.assertTrue("'" + int10 + "' != '" + 52 + "'", int10 == 52);
        org.junit.Assert.assertNotNull(wildcardClass11);
    }

    @Test
    public void test048() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test048");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        failing2_0.myIntInput = 10;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException((int) (byte) 0);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
    }

    @Test
    public void test049() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test049");
        failing.Failing2 failing2_0 = new failing.Failing2();
        int int1 = failing2_0.myIntInput;
        failing2_0.myIntInput = 'a';
        int int4 = failing2_0.myIntInput;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException(10);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 0 + "'", int1 == 0);
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + 97 + "'", int4 == 97);
    }

    @Test
    public void test050() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test050");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        failing2_0.myIntInput = 1;
        failing2_0.myIntInput = 0;
        int int7 = failing2_0.myIntInput;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException((int) '4');
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int7 + "' != '" + 0 + "'", int7 == 0);
    }

    @Test
    public void test051() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test051");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        int int3 = failing2_0.myIntInput;
        int int4 = failing2_0.myIntInput;
        failing2_0.myIntInput = ' ';
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException((int) (byte) 0);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + (-1) + "'", int3 == (-1));
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + (-1) + "'", int4 == (-1));
    }

    @Test
    public void test052() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test052");
        failing.Failing2 failing2_0 = new failing.Failing2();
        int int1 = failing2_0.myIntInput;
        failing2_0.myIntInput = 'a';
        failing2_0.myIntInput = 0;
        failing2_0.myIntInput = (short) 100;
        java.lang.Class<?> wildcardClass8 = failing2_0.getClass();
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 0 + "'", int1 == 0);
        org.junit.Assert.assertNotNull(wildcardClass8);
    }

    @Test
    public void test053() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test053");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        int int3 = failing2_0.myIntInput;
        failing2_0.myIntInput = 0;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException((int) (byte) 1);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + (-1) + "'", int3 == (-1));
    }

    @Test
    public void test054() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test054");
        failing.Failing2 failing2_0 = new failing.Failing2();
        int int1 = failing2_0.myIntInput;
        failing2_0.myIntInput = 'a';
        int int4 = failing2_0.myIntInput;
        failing2_0.myIntInput = 97;
        int int7 = failing2_0.myIntInput;
        failing2_0.myIntInput = '4';
        failing2_0.myIntInput = (short) 100;
        int int12 = failing2_0.myIntInput;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException((int) (short) 0);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 0 + "'", int1 == 0);
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + 97 + "'", int4 == 97);
        org.junit.Assert.assertTrue("'" + int7 + "' != '" + 97 + "'", int7 == 97);
        org.junit.Assert.assertTrue("'" + int12 + "' != '" + 100 + "'", int12 == 100);
    }

    @Test
    public void test055() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test055");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        int int3 = failing2_0.myIntInput;
        int int4 = failing2_0.myIntInput;
        failing2_0.myIntInput = ' ';
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException((int) (short) 0);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + (-1) + "'", int3 == (-1));
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + (-1) + "'", int4 == (-1));
    }

    @Test
    public void test056() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test056");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        int int3 = failing2_0.myIntInput;
        int int4 = failing2_0.myIntInput;
        failing2_0.myIntInput = '4';
        failing2_0.myIntInput = (short) 0;
        failing2_0.myIntInput = ' ';
        java.lang.Class<?> wildcardClass11 = failing2_0.getClass();
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + (-1) + "'", int3 == (-1));
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + (-1) + "'", int4 == (-1));
        org.junit.Assert.assertNotNull(wildcardClass11);
    }

    @Test
    public void test057() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test057");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        failing2_0.myIntInput = 1;
        failing2_0.myIntInput = (short) -1;
        java.lang.Class<?> wildcardClass7 = failing2_0.getClass();
        org.junit.Assert.assertNotNull(wildcardClass7);
    }

    @Test
    public void test058() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test058");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        int int3 = failing2_0.myIntInput;
        int int4 = failing2_0.myIntInput;
        failing2_0.myIntInput = '4';
        failing2_0.myIntInput = (short) 0;
        failing2_0.myIntInput = 'a';
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException((int) (byte) -1);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + (-1) + "'", int3 == (-1));
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + (-1) + "'", int4 == (-1));
    }

    @Test
    public void test059() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test059");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        int int3 = failing2_0.myIntInput;
        int int4 = failing2_0.myIntInput;
        failing2_0.myIntInput = ' ';
        failing2_0.myIntInput = (byte) 0;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException(52);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + (-1) + "'", int3 == (-1));
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + (-1) + "'", int4 == (-1));
    }

    @Test
    public void test060() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test060");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        int int3 = failing2_0.myIntInput;
        failing2_0.myIntInput = (short) 1;
        java.lang.Class<?> wildcardClass6 = failing2_0.getClass();
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + (-1) + "'", int3 == (-1));
        org.junit.Assert.assertNotNull(wildcardClass6);
    }

    @Test
    public void test061() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test061");
        failing.Failing2 failing2_0 = new failing.Failing2();
        int int1 = failing2_0.myIntInput;
        int int2 = failing2_0.myIntInput;
        failing2_0.myIntInput = (byte) -1;
        failing2_0.myIntInput = 97;
        failing2_0.myIntInput = (byte) 100;
        java.lang.Class<?> wildcardClass9 = failing2_0.getClass();
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 0 + "'", int1 == 0);
        org.junit.Assert.assertTrue("'" + int2 + "' != '" + 0 + "'", int2 == 0);
        org.junit.Assert.assertNotNull(wildcardClass9);
    }

    @Test
    public void test062() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test062");
        failing.Failing2 failing2_0 = new failing.Failing2();
        int int1 = failing2_0.myIntInput;
        failing2_0.myIntInput = 'a';
        int int4 = failing2_0.myIntInput;
        failing2_0.myIntInput = 97;
        int int7 = failing2_0.myIntInput;
        failing2_0.myIntInput = (byte) 0;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException((int) (short) 100);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 0 + "'", int1 == 0);
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + 97 + "'", int4 == 97);
        org.junit.Assert.assertTrue("'" + int7 + "' != '" + 97 + "'", int7 == 97);
    }

    @Test
    public void test063() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test063");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        int int3 = failing2_0.myIntInput;
        int int4 = failing2_0.myIntInput;
        failing2_0.myIntInput = '4';
        failing2_0.myIntInput = (short) 0;
        java.lang.Class<?> wildcardClass9 = failing2_0.getClass();
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + (-1) + "'", int3 == (-1));
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + (-1) + "'", int4 == (-1));
        org.junit.Assert.assertNotNull(wildcardClass9);
    }

    @Test
    public void test064() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test064");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        int int3 = failing2_0.myIntInput;
        int int4 = failing2_0.myIntInput;
        failing2_0.myIntInput = '4';
        failing2_0.myIntInput = (short) 0;
        failing2_0.myIntInput = '#';
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException((int) (short) 10);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + (-1) + "'", int3 == (-1));
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + (-1) + "'", int4 == (-1));
    }

    @Test
    public void test065() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test065");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        failing2_0.myIntInput = 1;
        failing2_0.myIntInput = 0;
        java.lang.Class<?> wildcardClass7 = failing2_0.getClass();
        org.junit.Assert.assertNotNull(wildcardClass7);
    }

    @Test
    public void test066() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test066");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        int int3 = failing2_0.myIntInput;
        int int4 = failing2_0.myIntInput;
        failing2_0.myIntInput = 'a';
        java.lang.Class<?> wildcardClass7 = failing2_0.getClass();
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + (-1) + "'", int3 == (-1));
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + (-1) + "'", int4 == (-1));
        org.junit.Assert.assertNotNull(wildcardClass7);
    }

    @Test
    public void test067() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test067");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        int int3 = failing2_0.myIntInput;
        java.lang.Class<?> wildcardClass4 = failing2_0.getClass();
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + (-1) + "'", int3 == (-1));
        org.junit.Assert.assertNotNull(wildcardClass4);
    }

    @Test
    public void test068() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test068");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        int int3 = failing2_0.myIntInput;
        failing2_0.myIntInput = (byte) 10;
        failing2_0.myIntInput = (byte) 10;
        java.lang.Class<?> wildcardClass8 = failing2_0.getClass();
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + (-1) + "'", int3 == (-1));
        org.junit.Assert.assertNotNull(wildcardClass8);
    }

    @Test
    public void test069() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test069");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        int int3 = failing2_0.myIntInput;
        int int4 = failing2_0.myIntInput;
        failing2_0.myIntInput = '4';
        failing2_0.myIntInput = (short) 0;
        failing2_0.myIntInput = 'a';
        java.lang.Class<?> wildcardClass11 = failing2_0.getClass();
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + (-1) + "'", int3 == (-1));
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + (-1) + "'", int4 == (-1));
        org.junit.Assert.assertNotNull(wildcardClass11);
    }

    @Test
    public void test070() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test070");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        failing2_0.myIntInput = 1;
        failing2_0.myIntInput = 0;
        int int7 = failing2_0.myIntInput;
        failing2_0.myIntInput = '#';
        int int10 = failing2_0.myIntInput;
        java.lang.Class<?> wildcardClass11 = failing2_0.getClass();
        org.junit.Assert.assertTrue("'" + int7 + "' != '" + 0 + "'", int7 == 0);
        org.junit.Assert.assertTrue("'" + int10 + "' != '" + 35 + "'", int10 == 35);
        org.junit.Assert.assertNotNull(wildcardClass11);
    }

    @Test
    public void test071() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test071");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        int int3 = failing2_0.myIntInput;
        failing2_0.myIntInput = (-1);
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + (-1) + "'", int3 == (-1));
    }

    @Test
    public void test072() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test072");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (short) 1;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException((int) (short) 100);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
    }

    @Test
    public void test073() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test073");
        failing.Failing2 failing2_0 = new failing.Failing2();
        int int1 = failing2_0.myIntInput;
        failing2_0.myIntInput = 'a';
        int int4 = failing2_0.myIntInput;
        failing2_0.myIntInput = 97;
        java.lang.Class<?> wildcardClass7 = failing2_0.getClass();
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 0 + "'", int1 == 0);
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + 97 + "'", int4 == 97);
        org.junit.Assert.assertNotNull(wildcardClass7);
    }

    @Test
    public void test074() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test074");
        failing.Failing2 failing2_0 = new failing.Failing2();
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException((int) '#');
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
    }

    @Test
    public void test075() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test075");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        int int3 = failing2_0.myIntInput;
        int int4 = failing2_0.myIntInput;
        failing2_0.myIntInput = '4';
        java.lang.Class<?> wildcardClass7 = failing2_0.getClass();
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + (-1) + "'", int3 == (-1));
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + (-1) + "'", int4 == (-1));
        org.junit.Assert.assertNotNull(wildcardClass7);
    }

    @Test
    public void test076() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test076");
        failing.Failing2 failing2_0 = new failing.Failing2();
        int int1 = failing2_0.myIntInput;
        failing2_0.myIntInput = 'a';
        failing2_0.myIntInput = 0;
        failing2_0.myIntInput = 'a';
        java.lang.Class<?> wildcardClass8 = failing2_0.getClass();
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 0 + "'", int1 == 0);
        org.junit.Assert.assertNotNull(wildcardClass8);
    }

    @Test
    public void test077() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test077");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        int int3 = failing2_0.myIntInput;
        int int4 = failing2_0.myIntInput;
        failing2_0.myIntInput = 'a';
        failing2_0.myIntInput = 'a';
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException(10);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + (-1) + "'", int3 == (-1));
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + (-1) + "'", int4 == (-1));
    }

    @Test
    public void test078() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test078");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (short) 1;
        failing2_0.myIntInput = (-1);
    }

    @Test
    public void test079() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test079");
        failing.Failing2 failing2_0 = new failing.Failing2();
        int int1 = failing2_0.myIntInput;
        int int2 = failing2_0.myIntInput;
        int int3 = failing2_0.myIntInput;
        java.lang.Class<?> wildcardClass4 = failing2_0.getClass();
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 0 + "'", int1 == 0);
        org.junit.Assert.assertTrue("'" + int2 + "' != '" + 0 + "'", int2 == 0);
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + 0 + "'", int3 == 0);
        org.junit.Assert.assertNotNull(wildcardClass4);
    }

    @Test
    public void test080() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test080");
        failing.Failing2 failing2_0 = new failing.Failing2();
        int int1 = failing2_0.myIntInput;
        int int2 = failing2_0.myIntInput;
        failing2_0.myIntInput = (byte) -1;
        failing2_0.myIntInput = (short) 0;
        int int7 = failing2_0.myIntInput;
        failing2_0.myIntInput = (byte) 0;
        java.lang.Class<?> wildcardClass10 = failing2_0.getClass();
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 0 + "'", int1 == 0);
        org.junit.Assert.assertTrue("'" + int2 + "' != '" + 0 + "'", int2 == 0);
        org.junit.Assert.assertTrue("'" + int7 + "' != '" + 0 + "'", int7 == 0);
        org.junit.Assert.assertNotNull(wildcardClass10);
    }

    @Test
    public void test081() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test081");
        failing.Failing2 failing2_0 = new failing.Failing2();
        int int1 = failing2_0.myIntInput;
        failing2_0.myIntInput = 'a';
        int int4 = failing2_0.myIntInput;
        failing2_0.myIntInput = 97;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException(52);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 0 + "'", int1 == 0);
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + 97 + "'", int4 == 97);
    }

    @Test
    public void test082() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test082");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        failing2_0.myIntInput = 10;
        java.lang.Class<?> wildcardClass5 = failing2_0.getClass();
        org.junit.Assert.assertNotNull(wildcardClass5);
    }

    @Test
    public void test083() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test083");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        failing2_0.myIntInput = 1;
        failing2_0.myIntInput = 0;
        int int7 = failing2_0.myIntInput;
        int int8 = failing2_0.myIntInput;
        java.lang.Class<?> wildcardClass9 = failing2_0.getClass();
        org.junit.Assert.assertTrue("'" + int7 + "' != '" + 0 + "'", int7 == 0);
        org.junit.Assert.assertTrue("'" + int8 + "' != '" + 0 + "'", int8 == 0);
        org.junit.Assert.assertNotNull(wildcardClass9);
    }

    @Test
    public void test084() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test084");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = 1;
        failing2_0.myIntInput = (byte) -1;
        java.lang.Class<?> wildcardClass5 = failing2_0.getClass();
        org.junit.Assert.assertNotNull(wildcardClass5);
    }

    @Test
    public void test085() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test085");
        failing.Failing2 failing2_0 = new failing.Failing2();
        int int1 = failing2_0.myIntInput;
        failing2_0.myIntInput = 'a';
        int int4 = failing2_0.myIntInput;
        failing2_0.myIntInput = (short) -1;
        failing2_0.myIntInput = (byte) 100;
        int int9 = failing2_0.myIntInput;
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 0 + "'", int1 == 0);
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + 97 + "'", int4 == 97);
        org.junit.Assert.assertTrue("'" + int9 + "' != '" + 100 + "'", int9 == 100);
    }

    @Test
    public void test086() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test086");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        failing2_0.myIntInput = 1;
        failing2_0.myIntInput = 0;
        int int7 = failing2_0.myIntInput;
        failing2_0.myIntInput = 'a';
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException((int) '#');
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int7 + "' != '" + 0 + "'", int7 == 0);
    }

    @Test
    public void test087() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test087");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        failing2_0.myIntInput = 1;
        failing2_0.myIntInput = (byte) 1;
        int int7 = failing2_0.myIntInput;
        java.lang.Class<?> wildcardClass8 = failing2_0.getClass();
        org.junit.Assert.assertTrue("'" + int7 + "' != '" + 1 + "'", int7 == 1);
        org.junit.Assert.assertNotNull(wildcardClass8);
    }

    @Test
    public void test088() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test088");
        failing.Failing2 failing2_0 = new failing.Failing2();
        int int1 = failing2_0.myIntInput;
        failing2_0.myIntInput = 'a';
        int int4 = failing2_0.myIntInput;
        failing2_0.myIntInput = 97;
        int int7 = failing2_0.myIntInput;
        failing2_0.myIntInput = (byte) 0;
        int int10 = failing2_0.myIntInput;
        java.lang.Class<?> wildcardClass11 = failing2_0.getClass();
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 0 + "'", int1 == 0);
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + 97 + "'", int4 == 97);
        org.junit.Assert.assertTrue("'" + int7 + "' != '" + 97 + "'", int7 == 97);
        org.junit.Assert.assertTrue("'" + int10 + "' != '" + 0 + "'", int10 == 0);
        org.junit.Assert.assertNotNull(wildcardClass11);
    }

    @Test
    public void test089() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test089");
        failing.Failing2 failing2_0 = new failing.Failing2();
        int int1 = failing2_0.myIntInput;
        failing2_0.myIntInput = 'a';
        failing2_0.myIntInput = 0;
        failing2_0.myIntInput = 97;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException((int) '#');
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 0 + "'", int1 == 0);
    }

    @Test
    public void test090() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test090");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        int int3 = failing2_0.myIntInput;
        failing2_0.myIntInput = (short) 1;
        failing2_0.myIntInput = 100;
        java.lang.Class<?> wildcardClass8 = failing2_0.getClass();
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + (-1) + "'", int3 == (-1));
        org.junit.Assert.assertNotNull(wildcardClass8);
    }

    @Test
    public void test091() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test091");
        failing.Failing2 failing2_0 = new failing.Failing2();
        int int1 = failing2_0.myIntInput;
        int int2 = failing2_0.myIntInput;
        failing2_0.myIntInput = (byte) -1;
        failing2_0.myIntInput = (short) 0;
        int int7 = failing2_0.myIntInput;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException((int) (short) 100);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 0 + "'", int1 == 0);
        org.junit.Assert.assertTrue("'" + int2 + "' != '" + 0 + "'", int2 == 0);
        org.junit.Assert.assertTrue("'" + int7 + "' != '" + 0 + "'", int7 == 0);
    }

    @Test
    public void test092() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test092");
        failing.Failing2 failing2_0 = new failing.Failing2();
        int int1 = failing2_0.myIntInput;
        failing2_0.myIntInput = 'a';
        int int4 = failing2_0.myIntInput;
        failing2_0.myIntInput = 97;
        int int7 = failing2_0.myIntInput;
        failing2_0.myIntInput = '4';
        failing2_0.myIntInput = (short) 100;
        int int12 = failing2_0.myIntInput;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException(100);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 0 + "'", int1 == 0);
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + 97 + "'", int4 == 97);
        org.junit.Assert.assertTrue("'" + int7 + "' != '" + 97 + "'", int7 == 97);
        org.junit.Assert.assertTrue("'" + int12 + "' != '" + 100 + "'", int12 == 100);
    }

    @Test
    public void test093() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test093");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        failing2_0.myIntInput = 1;
        failing2_0.myIntInput = (byte) 1;
        failing2_0.myIntInput = '4';
    }

    @Test
    public void test094() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test094");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        failing2_0.myIntInput = 1;
        failing2_0.myIntInput = (short) 100;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException((int) (byte) 0);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
    }

    @Test
    public void test095() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test095");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        failing2_0.myIntInput = 1;
        failing2_0.myIntInput = (short) -1;
        failing2_0.myIntInput = (-1);
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException((int) '#');
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
    }

    @Test
    public void test096() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test096");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (short) 1;
        int int3 = failing2_0.myIntInput;
        failing2_0.myIntInput = (byte) 0;
        failing2_0.myIntInput = 0;
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + 1 + "'", int3 == 1);
    }

    @Test
    public void test097() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test097");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = 1;
        failing2_0.myIntInput = (byte) -1;
        failing2_0.myIntInput = 0;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException(0);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
    }

    @Test
    public void test098() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test098");
        failing.Failing2 failing2_0 = new failing.Failing2();
        int int1 = failing2_0.myIntInput;
        int int2 = failing2_0.myIntInput;
        failing2_0.myIntInput = (byte) -1;
        failing2_0.myIntInput = (short) 0;
        int int7 = failing2_0.myIntInput;
        int int8 = failing2_0.myIntInput;
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 0 + "'", int1 == 0);
        org.junit.Assert.assertTrue("'" + int2 + "' != '" + 0 + "'", int2 == 0);
        org.junit.Assert.assertTrue("'" + int7 + "' != '" + 0 + "'", int7 == 0);
        org.junit.Assert.assertTrue("'" + int8 + "' != '" + 0 + "'", int8 == 0);
    }

    @Test
    public void test099() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test099");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        failing2_0.myIntInput = 1;
        failing2_0.myIntInput = (short) -1;
        failing2_0.myIntInput = (-1);
        failing2_0.myIntInput = (byte) 10;
    }

    @Test
    public void test100() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test100");
        failing.Failing2 failing2_0 = new failing.Failing2();
        int int1 = failing2_0.myIntInput;
        failing2_0.myIntInput = 'a';
        failing2_0.myIntInput = 0;
        failing2_0.myIntInput = (short) 100;
        failing2_0.myIntInput = ' ';
        java.lang.Class<?> wildcardClass10 = failing2_0.getClass();
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 0 + "'", int1 == 0);
        org.junit.Assert.assertNotNull(wildcardClass10);
    }

    @Test
    public void test101() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test101");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        int int3 = failing2_0.myIntInput;
        int int4 = failing2_0.myIntInput;
        failing2_0.myIntInput = ' ';
        failing2_0.myIntInput = (byte) 0;
        int int9 = failing2_0.myIntInput;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException(0);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + (-1) + "'", int3 == (-1));
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + (-1) + "'", int4 == (-1));
        org.junit.Assert.assertTrue("'" + int9 + "' != '" + 0 + "'", int9 == 0);
    }

    @Test
    public void test102() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test102");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        failing2_0.myIntInput = 1;
        failing2_0.myIntInput = (byte) 1;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException((int) (short) 1);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
    }

    @Test
    public void test103() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test103");
        failing.Failing2 failing2_0 = new failing.Failing2();
        int int1 = failing2_0.myIntInput;
        int int2 = failing2_0.myIntInput;
        int int3 = failing2_0.myIntInput;
        failing2_0.myIntInput = 0;
        int int6 = failing2_0.myIntInput;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException((int) (byte) -1);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 0 + "'", int1 == 0);
        org.junit.Assert.assertTrue("'" + int2 + "' != '" + 0 + "'", int2 == 0);
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + 0 + "'", int3 == 0);
        org.junit.Assert.assertTrue("'" + int6 + "' != '" + 0 + "'", int6 == 0);
    }

    @Test
    public void test104() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test104");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        failing2_0.myIntInput = 1;
        failing2_0.myIntInput = (short) -1;
        failing2_0.myIntInput = (-1);
        java.lang.Class<?> wildcardClass9 = failing2_0.getClass();
        org.junit.Assert.assertNotNull(wildcardClass9);
    }

    @Test
    public void test105() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test105");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (short) 1;
        int int3 = failing2_0.myIntInput;
        failing2_0.myIntInput = 0;
        failing2_0.myIntInput = 1;
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + 1 + "'", int3 == 1);
    }

    @Test
    public void test106() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test106");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        failing2_0.myIntInput = 1;
        failing2_0.myIntInput = (short) -1;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException(0);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
    }

    @Test
    public void test107() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test107");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        failing2_0.myIntInput = 1;
        int int5 = failing2_0.myIntInput;
        failing2_0.myIntInput = '4';
        java.lang.Class<?> wildcardClass8 = failing2_0.getClass();
        org.junit.Assert.assertTrue("'" + int5 + "' != '" + 1 + "'", int5 == 1);
        org.junit.Assert.assertNotNull(wildcardClass8);
    }

    @Test
    public void test108() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test108");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        failing2_0.myIntInput = 1;
        failing2_0.myIntInput = 0;
        int int7 = failing2_0.myIntInput;
        failing2_0.myIntInput = 35;
        failing2_0.myIntInput = (short) 1;
        java.lang.Class<?> wildcardClass12 = failing2_0.getClass();
        org.junit.Assert.assertTrue("'" + int7 + "' != '" + 0 + "'", int7 == 0);
        org.junit.Assert.assertNotNull(wildcardClass12);
    }

    @Test
    public void test109() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test109");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        int int3 = failing2_0.myIntInput;
        int int4 = failing2_0.myIntInput;
        int int5 = failing2_0.myIntInput;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException((int) (short) 0);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + (-1) + "'", int3 == (-1));
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + (-1) + "'", int4 == (-1));
        org.junit.Assert.assertTrue("'" + int5 + "' != '" + (-1) + "'", int5 == (-1));
    }

    @Test
    public void test110() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test110");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (short) 1;
        int int3 = failing2_0.myIntInput;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException(100);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + 1 + "'", int3 == 1);
    }

    @Test
    public void test111() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test111");
        failing.Failing2 failing2_0 = new failing.Failing2();
        int int1 = failing2_0.myIntInput;
        int int2 = failing2_0.myIntInput;
        failing2_0.myIntInput = (byte) -1;
        failing2_0.myIntInput = (short) 0;
        int int7 = failing2_0.myIntInput;
        failing2_0.myIntInput = (byte) 0;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException((int) 'a');
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 0 + "'", int1 == 0);
        org.junit.Assert.assertTrue("'" + int2 + "' != '" + 0 + "'", int2 == 0);
        org.junit.Assert.assertTrue("'" + int7 + "' != '" + 0 + "'", int7 == 0);
    }

    @Test
    public void test112() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test112");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (short) 1;
        int int3 = failing2_0.myIntInput;
        int int4 = failing2_0.myIntInput;
        failing2_0.myIntInput = (byte) 100;
        java.lang.Class<?> wildcardClass7 = failing2_0.getClass();
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + 1 + "'", int3 == 1);
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + 1 + "'", int4 == 1);
        org.junit.Assert.assertNotNull(wildcardClass7);
    }

    @Test
    public void test113() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test113");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        failing2_0.myIntInput = 1;
        failing2_0.myIntInput = (short) -1;
        failing2_0.myIntInput = (-1);
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException(35);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
    }

    @Test
    public void test114() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test114");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        failing2_0.myIntInput = 1;
        failing2_0.myIntInput = (short) 100;
        int int7 = failing2_0.myIntInput;
        failing2_0.myIntInput = '#';
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException((int) (byte) -1);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int7 + "' != '" + 100 + "'", int7 == 100);
    }

    @Test
    public void test115() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test115");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        int int3 = failing2_0.myIntInput;
        int int4 = failing2_0.myIntInput;
        failing2_0.myIntInput = 'a';
        int int7 = failing2_0.myIntInput;
        java.lang.Class<?> wildcardClass8 = failing2_0.getClass();
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + (-1) + "'", int3 == (-1));
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + (-1) + "'", int4 == (-1));
        org.junit.Assert.assertTrue("'" + int7 + "' != '" + 97 + "'", int7 == 97);
        org.junit.Assert.assertNotNull(wildcardClass8);
    }

    @Test
    public void test116() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test116");
        failing.Failing2 failing2_0 = new failing.Failing2();
        int int1 = failing2_0.myIntInput;
        failing2_0.myIntInput = 'a';
        int int4 = failing2_0.myIntInput;
        int int5 = failing2_0.myIntInput;
        failing2_0.myIntInput = (short) -1;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException((int) (byte) 1);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 0 + "'", int1 == 0);
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + 97 + "'", int4 == 97);
        org.junit.Assert.assertTrue("'" + int5 + "' != '" + 97 + "'", int5 == 97);
    }

    @Test
    public void test117() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test117");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        failing2_0.myIntInput = 1;
        int int5 = failing2_0.myIntInput;
        int int6 = failing2_0.myIntInput;
        java.lang.Class<?> wildcardClass7 = failing2_0.getClass();
        org.junit.Assert.assertTrue("'" + int5 + "' != '" + 1 + "'", int5 == 1);
        org.junit.Assert.assertTrue("'" + int6 + "' != '" + 1 + "'", int6 == 1);
        org.junit.Assert.assertNotNull(wildcardClass7);
    }

    @Test
    public void test118() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test118");
        failing.Failing2 failing2_0 = new failing.Failing2();
        int int1 = failing2_0.myIntInput;
        failing2_0.myIntInput = 'a';
        int int4 = failing2_0.myIntInput;
        failing2_0.myIntInput = 97;
        int int7 = failing2_0.myIntInput;
        failing2_0.myIntInput = '4';
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException((int) (byte) 10);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 0 + "'", int1 == 0);
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + 97 + "'", int4 == 97);
        org.junit.Assert.assertTrue("'" + int7 + "' != '" + 97 + "'", int7 == 97);
    }

    @Test
    public void test119() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test119");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        int int3 = failing2_0.myIntInput;
        failing2_0.myIntInput = 0;
        failing2_0.myIntInput = (short) 100;
        int int8 = failing2_0.myIntInput;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException((int) (short) -1);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + (-1) + "'", int3 == (-1));
        org.junit.Assert.assertTrue("'" + int8 + "' != '" + 100 + "'", int8 == 100);
    }

    @Test
    public void test120() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test120");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        int int3 = failing2_0.myIntInput;
        failing2_0.myIntInput = (short) 1;
        failing2_0.myIntInput = 100;
        failing2_0.myIntInput = (byte) 100;
        java.lang.Class<?> wildcardClass10 = failing2_0.getClass();
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + (-1) + "'", int3 == (-1));
        org.junit.Assert.assertNotNull(wildcardClass10);
    }

    @Test
    public void test121() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test121");
        failing.Failing2 failing2_0 = new failing.Failing2();
        int int1 = failing2_0.myIntInput;
        failing2_0.myIntInput = 'a';
        failing2_0.myIntInput = 0;
        failing2_0.myIntInput = (short) 100;
        failing2_0.myIntInput = 10;
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 0 + "'", int1 == 0);
    }

    @Test
    public void test122() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test122");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        int int3 = failing2_0.myIntInput;
        failing2_0.myIntInput = 0;
        failing2_0.myIntInput = (short) 100;
        int int8 = failing2_0.myIntInput;
        java.lang.Class<?> wildcardClass9 = failing2_0.getClass();
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + (-1) + "'", int3 == (-1));
        org.junit.Assert.assertTrue("'" + int8 + "' != '" + 100 + "'", int8 == 100);
        org.junit.Assert.assertNotNull(wildcardClass9);
    }

    @Test
    public void test123() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test123");
        failing.Failing2 failing2_0 = new failing.Failing2();
        int int1 = failing2_0.myIntInput;
        failing2_0.myIntInput = 'a';
        failing2_0.myIntInput = 0;
        failing2_0.myIntInput = 'a';
        int int8 = failing2_0.myIntInput;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException((int) (byte) 100);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 0 + "'", int1 == 0);
        org.junit.Assert.assertTrue("'" + int8 + "' != '" + 97 + "'", int8 == 97);
    }

    @Test
    public void test124() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test124");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        int int3 = failing2_0.myIntInput;
        int int4 = failing2_0.myIntInput;
        failing2_0.myIntInput = ' ';
        failing2_0.myIntInput = (byte) 0;
        failing2_0.myIntInput = (byte) 1;
        java.lang.Class<?> wildcardClass11 = failing2_0.getClass();
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + (-1) + "'", int3 == (-1));
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + (-1) + "'", int4 == (-1));
        org.junit.Assert.assertNotNull(wildcardClass11);
    }

    @Test
    public void test125() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test125");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        failing2_0.myIntInput = 1;
        int int5 = failing2_0.myIntInput;
        failing2_0.myIntInput = (byte) 100;
        int int8 = failing2_0.myIntInput;
        org.junit.Assert.assertTrue("'" + int5 + "' != '" + 1 + "'", int5 == 1);
        org.junit.Assert.assertTrue("'" + int8 + "' != '" + 100 + "'", int8 == 100);
    }

    @Test
    public void test126() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test126");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        failing2_0.myIntInput = 1;
        failing2_0.myIntInput = 0;
        int int7 = failing2_0.myIntInput;
        failing2_0.myIntInput = 'a';
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException((int) (byte) 1);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int7 + "' != '" + 0 + "'", int7 == 0);
    }

    @Test
    public void test127() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test127");
        failing.Failing2 failing2_0 = new failing.Failing2();
        int int1 = failing2_0.myIntInput;
        failing2_0.myIntInput = 'a';
        java.lang.Class<?> wildcardClass4 = failing2_0.getClass();
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 0 + "'", int1 == 0);
        org.junit.Assert.assertNotNull(wildcardClass4);
    }

    @Test
    public void test128() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test128");
        failing.Failing2 failing2_0 = new failing.Failing2();
        int int1 = failing2_0.myIntInput;
        failing2_0.myIntInput = 'a';
        int int4 = failing2_0.myIntInput;
        failing2_0.myIntInput = 97;
        int int7 = failing2_0.myIntInput;
        java.lang.Class<?> wildcardClass8 = failing2_0.getClass();
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 0 + "'", int1 == 0);
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + 97 + "'", int4 == 97);
        org.junit.Assert.assertTrue("'" + int7 + "' != '" + 97 + "'", int7 == 97);
        org.junit.Assert.assertNotNull(wildcardClass8);
    }

    @Test
    public void test129() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test129");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        int int3 = failing2_0.myIntInput;
        int int4 = failing2_0.myIntInput;
        failing2_0.myIntInput = '4';
        failing2_0.myIntInput = (short) 0;
        int int9 = failing2_0.myIntInput;
        int int10 = failing2_0.myIntInput;
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + (-1) + "'", int3 == (-1));
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + (-1) + "'", int4 == (-1));
        org.junit.Assert.assertTrue("'" + int9 + "' != '" + 0 + "'", int9 == 0);
        org.junit.Assert.assertTrue("'" + int10 + "' != '" + 0 + "'", int10 == 0);
    }

    @Test
    public void test130() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test130");
        failing.Failing2 failing2_0 = new failing.Failing2();
        int int1 = failing2_0.myIntInput;
        failing2_0.myIntInput = 'a';
        int int4 = failing2_0.myIntInput;
        failing2_0.myIntInput = 97;
        int int7 = failing2_0.myIntInput;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException(100);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 0 + "'", int1 == 0);
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + 97 + "'", int4 == 97);
        org.junit.Assert.assertTrue("'" + int7 + "' != '" + 97 + "'", int7 == 97);
    }

    @Test
    public void test131() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test131");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        int int3 = failing2_0.myIntInput;
        int int4 = failing2_0.myIntInput;
        failing2_0.myIntInput = '#';
        failing2_0.myIntInput = (short) 100;
        java.lang.Class<?> wildcardClass9 = failing2_0.getClass();
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + (-1) + "'", int3 == (-1));
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + (-1) + "'", int4 == (-1));
        org.junit.Assert.assertNotNull(wildcardClass9);
    }

    @Test
    public void test132() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test132");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (short) 1;
        failing2_0.myIntInput = (byte) 0;
    }

    @Test
    public void test133() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test133");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        failing2_0.myIntInput = 1;
        failing2_0.myIntInput = (short) 100;
        int int7 = failing2_0.myIntInput;
        failing2_0.myIntInput = '#';
        int int10 = failing2_0.myIntInput;
        failing2_0.myIntInput = '#';
        org.junit.Assert.assertTrue("'" + int7 + "' != '" + 100 + "'", int7 == 100);
        org.junit.Assert.assertTrue("'" + int10 + "' != '" + 35 + "'", int10 == 35);
    }

    @Test
    public void test134() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test134");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        int int3 = failing2_0.myIntInput;
        failing2_0.myIntInput = (byte) 10;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException(35);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + (-1) + "'", int3 == (-1));
    }

    @Test
    public void test135() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test135");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (short) 10;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException(52);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
    }

    @Test
    public void test136() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test136");
        failing.Failing2 failing2_0 = new failing.Failing2();
        int int1 = failing2_0.myIntInput;
        failing2_0.myIntInput = 'a';
        int int4 = failing2_0.myIntInput;
        failing2_0.myIntInput = 97;
        int int7 = failing2_0.myIntInput;
        int int8 = failing2_0.myIntInput;
        int int9 = failing2_0.myIntInput;
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 0 + "'", int1 == 0);
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + 97 + "'", int4 == 97);
        org.junit.Assert.assertTrue("'" + int7 + "' != '" + 97 + "'", int7 == 97);
        org.junit.Assert.assertTrue("'" + int8 + "' != '" + 97 + "'", int8 == 97);
        org.junit.Assert.assertTrue("'" + int9 + "' != '" + 97 + "'", int9 == 97);
    }

    @Test
    public void test137() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test137");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (short) 1;
        failing2_0.myIntInput = (short) 1;
        failing2_0.myIntInput = (byte) 10;
    }

    @Test
    public void test138() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test138");
        failing.Failing2 failing2_0 = new failing.Failing2();
        int int1 = failing2_0.myIntInput;
        failing2_0.myIntInput = 'a';
        int int4 = failing2_0.myIntInput;
        failing2_0.myIntInput = 97;
        int int7 = failing2_0.myIntInput;
        int int8 = failing2_0.myIntInput;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException((int) (byte) 1);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 0 + "'", int1 == 0);
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + 97 + "'", int4 == 97);
        org.junit.Assert.assertTrue("'" + int7 + "' != '" + 97 + "'", int7 == 97);
        org.junit.Assert.assertTrue("'" + int8 + "' != '" + 97 + "'", int8 == 97);
    }

    @Test
    public void test139() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test139");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        int int3 = failing2_0.myIntInput;
        int int4 = failing2_0.myIntInput;
        int int5 = failing2_0.myIntInput;
        failing2_0.myIntInput = (short) 1;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException((int) ' ');
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + (-1) + "'", int3 == (-1));
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + (-1) + "'", int4 == (-1));
        org.junit.Assert.assertTrue("'" + int5 + "' != '" + (-1) + "'", int5 == (-1));
    }

    @Test
    public void test140() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test140");
        failing.Failing2 failing2_0 = new failing.Failing2();
        int int1 = failing2_0.myIntInput;
        int int2 = failing2_0.myIntInput;
        failing2_0.myIntInput = (byte) -1;
        failing2_0.myIntInput = 97;
        failing2_0.myIntInput = (byte) 100;
        failing2_0.myIntInput = (short) 100;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException((int) (short) 0);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 0 + "'", int1 == 0);
        org.junit.Assert.assertTrue("'" + int2 + "' != '" + 0 + "'", int2 == 0);
    }

    @Test
    public void test141() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test141");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        failing2_0.myIntInput = 1;
        failing2_0.myIntInput = (byte) 1;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException((int) '#');
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
    }

    @Test
    public void test142() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test142");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        int int3 = failing2_0.myIntInput;
        failing2_0.myIntInput = 0;
        failing2_0.myIntInput = (short) 100;
        int int8 = failing2_0.myIntInput;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException((int) (short) 0);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + (-1) + "'", int3 == (-1));
        org.junit.Assert.assertTrue("'" + int8 + "' != '" + 100 + "'", int8 == 100);
    }

    @Test
    public void test143() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test143");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = 1;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException(0);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
    }

    @Test
    public void test144() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test144");
        failing.Failing2 failing2_0 = new failing.Failing2();
        int int1 = failing2_0.myIntInput;
        int int2 = failing2_0.myIntInput;
        int int3 = failing2_0.myIntInput;
        int int4 = failing2_0.myIntInput;
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 0 + "'", int1 == 0);
        org.junit.Assert.assertTrue("'" + int2 + "' != '" + 0 + "'", int2 == 0);
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + 0 + "'", int3 == 0);
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + 0 + "'", int4 == 0);
    }

    @Test
    public void test145() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test145");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        int int3 = failing2_0.myIntInput;
        failing2_0.myIntInput = (byte) 10;
        failing2_0.myIntInput = (byte) 10;
        failing2_0.myIntInput = (byte) 10;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException(52);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + (-1) + "'", int3 == (-1));
    }

    @Test
    public void test146() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test146");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = 1;
        int int3 = failing2_0.myIntInput;
        java.lang.Class<?> wildcardClass4 = failing2_0.getClass();
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + 1 + "'", int3 == 1);
        org.junit.Assert.assertNotNull(wildcardClass4);
    }

    @Test
    public void test147() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test147");
        failing.Failing2 failing2_0 = new failing.Failing2();
        int int1 = failing2_0.myIntInput;
        failing2_0.myIntInput = 'a';
        int int4 = failing2_0.myIntInput;
        failing2_0.myIntInput = 97;
        int int7 = failing2_0.myIntInput;
        failing2_0.myIntInput = (byte) 0;
        int int10 = failing2_0.myIntInput;
        int int11 = failing2_0.myIntInput;
        failing2_0.myIntInput = 52;
        java.lang.Class<?> wildcardClass14 = failing2_0.getClass();
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 0 + "'", int1 == 0);
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + 97 + "'", int4 == 97);
        org.junit.Assert.assertTrue("'" + int7 + "' != '" + 97 + "'", int7 == 97);
        org.junit.Assert.assertTrue("'" + int10 + "' != '" + 0 + "'", int10 == 0);
        org.junit.Assert.assertTrue("'" + int11 + "' != '" + 0 + "'", int11 == 0);
        org.junit.Assert.assertNotNull(wildcardClass14);
    }

    @Test
    public void test148() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test148");
        failing.Failing2 failing2_0 = new failing.Failing2();
        int int1 = failing2_0.myIntInput;
        failing2_0.myIntInput = 'a';
        int int4 = failing2_0.myIntInput;
        int int5 = failing2_0.myIntInput;
        failing2_0.myIntInput = (short) -1;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException((int) (short) 0);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 0 + "'", int1 == 0);
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + 97 + "'", int4 == 97);
        org.junit.Assert.assertTrue("'" + int5 + "' != '" + 97 + "'", int5 == 97);
    }

    @Test
    public void test149() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test149");
        failing.Failing2 failing2_0 = new failing.Failing2();
        int int1 = failing2_0.myIntInput;
        failing2_0.myIntInput = 'a';
        int int4 = failing2_0.myIntInput;
        failing2_0.myIntInput = 97;
        int int7 = failing2_0.myIntInput;
        failing2_0.myIntInput = (byte) 0;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException((int) ' ');
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 0 + "'", int1 == 0);
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + 97 + "'", int4 == 97);
        org.junit.Assert.assertTrue("'" + int7 + "' != '" + 97 + "'", int7 == 97);
    }

    @Test
    public void test150() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test150");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        failing2_0.myIntInput = 1;
        int int5 = failing2_0.myIntInput;
        failing2_0.myIntInput = '4';
        int int8 = failing2_0.myIntInput;
        int int9 = failing2_0.myIntInput;
        java.lang.Class<?> wildcardClass10 = failing2_0.getClass();
        org.junit.Assert.assertTrue("'" + int5 + "' != '" + 1 + "'", int5 == 1);
        org.junit.Assert.assertTrue("'" + int8 + "' != '" + 52 + "'", int8 == 52);
        org.junit.Assert.assertTrue("'" + int9 + "' != '" + 52 + "'", int9 == 52);
        org.junit.Assert.assertNotNull(wildcardClass10);
    }

    @Test
    public void test151() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test151");
        failing.Failing2 failing2_0 = new failing.Failing2();
        int int1 = failing2_0.myIntInput;
        failing2_0.myIntInput = 'a';
        int int4 = failing2_0.myIntInput;
        failing2_0.myIntInput = 97;
        int int7 = failing2_0.myIntInput;
        int int8 = failing2_0.myIntInput;
        java.lang.Class<?> wildcardClass9 = failing2_0.getClass();
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 0 + "'", int1 == 0);
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + 97 + "'", int4 == 97);
        org.junit.Assert.assertTrue("'" + int7 + "' != '" + 97 + "'", int7 == 97);
        org.junit.Assert.assertTrue("'" + int8 + "' != '" + 97 + "'", int8 == 97);
        org.junit.Assert.assertNotNull(wildcardClass9);
    }

    @Test
    public void test152() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test152");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        failing2_0.myIntInput = 1;
        failing2_0.myIntInput = 0;
        int int7 = failing2_0.myIntInput;
        int int8 = failing2_0.myIntInput;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException(0);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int7 + "' != '" + 0 + "'", int7 == 0);
        org.junit.Assert.assertTrue("'" + int8 + "' != '" + 0 + "'", int8 == 0);
    }

    @Test
    public void test153() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test153");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        failing2_0.myIntInput = 1;
        int int5 = failing2_0.myIntInput;
        failing2_0.myIntInput = (byte) 100;
        java.lang.Class<?> wildcardClass8 = failing2_0.getClass();
        org.junit.Assert.assertTrue("'" + int5 + "' != '" + 1 + "'", int5 == 1);
        org.junit.Assert.assertNotNull(wildcardClass8);
    }

    @Test
    public void test154() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test154");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = 1;
        failing2_0.myIntInput = (byte) -1;
        int int5 = failing2_0.myIntInput;
        org.junit.Assert.assertTrue("'" + int5 + "' != '" + (-1) + "'", int5 == (-1));
    }

    @Test
    public void test155() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test155");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        int int3 = failing2_0.myIntInput;
        failing2_0.myIntInput = (short) 1;
        failing2_0.myIntInput = 100;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException(35);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + (-1) + "'", int3 == (-1));
    }

    @Test
    public void test156() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test156");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (byte) 10;
        java.lang.Class<?> wildcardClass3 = failing2_0.getClass();
        org.junit.Assert.assertNotNull(wildcardClass3);
    }

    @Test
    public void test157() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test157");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        int int3 = failing2_0.myIntInput;
        failing2_0.myIntInput = (short) 1;
        failing2_0.myIntInput = (short) 0;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException(32);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + (-1) + "'", int3 == (-1));
    }

    @Test
    public void test158() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test158");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = 1;
        failing2_0.myIntInput = (byte) -1;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException(32);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
    }

    @Test
    public void test159() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test159");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        int int3 = failing2_0.myIntInput;
        int int4 = failing2_0.myIntInput;
        failing2_0.myIntInput = '4';
        failing2_0.myIntInput = (short) 0;
        failing2_0.myIntInput = '#';
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException((-1));
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + (-1) + "'", int3 == (-1));
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + (-1) + "'", int4 == (-1));
    }

    @Test
    public void test160() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test160");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        failing2_0.myIntInput = 10;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException(32);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
    }

    @Test
    public void test161() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test161");
        failing.Failing2 failing2_0 = new failing.Failing2();
        int int1 = failing2_0.myIntInput;
        failing2_0.myIntInput = 'a';
        int int4 = failing2_0.myIntInput;
        int int5 = failing2_0.myIntInput;
        failing2_0.myIntInput = (short) -1;
        failing2_0.myIntInput = (-1);
        failing2_0.myIntInput = 35;
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 0 + "'", int1 == 0);
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + 97 + "'", int4 == 97);
        org.junit.Assert.assertTrue("'" + int5 + "' != '" + 97 + "'", int5 == 97);
    }

    @Test
    public void test162() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test162");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        int int3 = failing2_0.myIntInput;
        int int4 = failing2_0.myIntInput;
        failing2_0.myIntInput = '4';
        failing2_0.myIntInput = (short) 0;
        failing2_0.myIntInput = ' ';
        int int11 = failing2_0.myIntInput;
        java.lang.Class<?> wildcardClass12 = failing2_0.getClass();
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + (-1) + "'", int3 == (-1));
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + (-1) + "'", int4 == (-1));
        org.junit.Assert.assertTrue("'" + int11 + "' != '" + 32 + "'", int11 == 32);
        org.junit.Assert.assertNotNull(wildcardClass12);
    }

    @Test
    public void test163() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test163");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        int int3 = failing2_0.myIntInput;
        failing2_0.myIntInput = (short) 1;
        int int6 = failing2_0.myIntInput;
        failing2_0.myIntInput = 97;
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + (-1) + "'", int3 == (-1));
        org.junit.Assert.assertTrue("'" + int6 + "' != '" + 1 + "'", int6 == 1);
    }

    @Test
    public void test164() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test164");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        int int3 = failing2_0.myIntInput;
        failing2_0.myIntInput = (short) 1;
        failing2_0.myIntInput = (byte) -1;
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + (-1) + "'", int3 == (-1));
    }

    @Test
    public void test165() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test165");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        int int3 = failing2_0.myIntInput;
        failing2_0.myIntInput = (short) 1;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException((int) (byte) 1);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + (-1) + "'", int3 == (-1));
    }

    @Test
    public void test166() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test166");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        failing2_0.myIntInput = 1;
        failing2_0.myIntInput = (short) 100;
        int int7 = failing2_0.myIntInput;
        failing2_0.myIntInput = '#';
        int int10 = failing2_0.myIntInput;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException((int) (short) -1);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int7 + "' != '" + 100 + "'", int7 == 100);
        org.junit.Assert.assertTrue("'" + int10 + "' != '" + 35 + "'", int10 == 35);
    }

    @Test
    public void test167() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test167");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        failing2_0.myIntInput = 1;
        failing2_0.myIntInput = (short) 100;
        int int7 = failing2_0.myIntInput;
        java.lang.Class<?> wildcardClass8 = failing2_0.getClass();
        org.junit.Assert.assertTrue("'" + int7 + "' != '" + 100 + "'", int7 == 100);
        org.junit.Assert.assertNotNull(wildcardClass8);
    }

    @Test
    public void test168() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test168");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        int int3 = failing2_0.myIntInput;
        int int4 = failing2_0.myIntInput;
        failing2_0.myIntInput = ' ';
        failing2_0.myIntInput = (byte) 0;
        failing2_0.myIntInput = (byte) 1;
        failing2_0.myIntInput = (short) 0;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException((int) (short) 10);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + (-1) + "'", int3 == (-1));
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + (-1) + "'", int4 == (-1));
    }

    @Test
    public void test169() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test169");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        failing2_0.myIntInput = 1;
        failing2_0.myIntInput = 0;
        int int7 = failing2_0.myIntInput;
        failing2_0.myIntInput = '#';
        int int10 = failing2_0.myIntInput;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException(1);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int7 + "' != '" + 0 + "'", int7 == 0);
        org.junit.Assert.assertTrue("'" + int10 + "' != '" + 35 + "'", int10 == 35);
    }

    @Test
    public void test170() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test170");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        failing2_0.myIntInput = 1;
        failing2_0.myIntInput = (short) -1;
        failing2_0.myIntInput = (-1);
        int int9 = failing2_0.myIntInput;
        int int10 = failing2_0.myIntInput;
        org.junit.Assert.assertTrue("'" + int9 + "' != '" + (-1) + "'", int9 == (-1));
        org.junit.Assert.assertTrue("'" + int10 + "' != '" + (-1) + "'", int10 == (-1));
    }

    @Test
    public void test171() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test171");
        failing.Failing2 failing2_0 = new failing.Failing2();
        int int1 = failing2_0.myIntInput;
        failing2_0.myIntInput = 'a';
        failing2_0.myIntInput = 0;
        failing2_0.myIntInput = 97;
        java.lang.Class<?> wildcardClass8 = failing2_0.getClass();
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 0 + "'", int1 == 0);
        org.junit.Assert.assertNotNull(wildcardClass8);
    }

    @Test
    public void test172() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test172");
        failing.Failing2 failing2_0 = new failing.Failing2();
        int int1 = failing2_0.myIntInput;
        failing2_0.myIntInput = 'a';
        failing2_0.myIntInput = 0;
        failing2_0.myIntInput = 'a';
        int int8 = failing2_0.myIntInput;
        failing2_0.myIntInput = 35;
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 0 + "'", int1 == 0);
        org.junit.Assert.assertTrue("'" + int8 + "' != '" + 97 + "'", int8 == 97);
    }

    @Test
    public void test173() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test173");
        failing.Failing2 failing2_0 = new failing.Failing2();
        int int1 = failing2_0.myIntInput;
        int int2 = failing2_0.myIntInput;
        failing2_0.myIntInput = (byte) -1;
        failing2_0.myIntInput = (short) 0;
        int int7 = failing2_0.myIntInput;
        failing2_0.myIntInput = (byte) 0;
        failing2_0.myIntInput = (byte) 10;
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 0 + "'", int1 == 0);
        org.junit.Assert.assertTrue("'" + int2 + "' != '" + 0 + "'", int2 == 0);
        org.junit.Assert.assertTrue("'" + int7 + "' != '" + 0 + "'", int7 == 0);
    }

    @Test
    public void test174() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test174");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        failing2_0.myIntInput = 1;
        failing2_0.myIntInput = 0;
        int int7 = failing2_0.myIntInput;
        failing2_0.myIntInput = '#';
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException((int) (byte) 0);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int7 + "' != '" + 0 + "'", int7 == 0);
    }

    @Test
    public void test175() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test175");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        failing2_0.myIntInput = 1;
        int int5 = failing2_0.myIntInput;
        int int6 = failing2_0.myIntInput;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException((int) (byte) 100);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int5 + "' != '" + 1 + "'", int5 == 1);
        org.junit.Assert.assertTrue("'" + int6 + "' != '" + 1 + "'", int6 == 1);
    }

    @Test
    public void test176() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test176");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        failing2_0.myIntInput = 1;
        int int5 = failing2_0.myIntInput;
        java.lang.Class<?> wildcardClass6 = failing2_0.getClass();
        org.junit.Assert.assertTrue("'" + int5 + "' != '" + 1 + "'", int5 == 1);
        org.junit.Assert.assertNotNull(wildcardClass6);
    }

    @Test
    public void test177() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test177");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        int int3 = failing2_0.myIntInput;
        int int4 = failing2_0.myIntInput;
        failing2_0.myIntInput = 'a';
        failing2_0.myIntInput = 'a';
        java.lang.Class<?> wildcardClass9 = failing2_0.getClass();
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + (-1) + "'", int3 == (-1));
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + (-1) + "'", int4 == (-1));
        org.junit.Assert.assertNotNull(wildcardClass9);
    }

    @Test
    public void test178() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test178");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        int int3 = failing2_0.myIntInput;
        failing2_0.myIntInput = (byte) 10;
        failing2_0.myIntInput = (byte) 10;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException((int) (byte) 100);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + (-1) + "'", int3 == (-1));
    }

    @Test
    public void test179() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test179");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        failing2_0.myIntInput = 1;
        failing2_0.myIntInput = (short) 100;
        int int7 = failing2_0.myIntInput;
        failing2_0.myIntInput = '#';
        failing2_0.myIntInput = 97;
        java.lang.Class<?> wildcardClass12 = failing2_0.getClass();
        org.junit.Assert.assertTrue("'" + int7 + "' != '" + 100 + "'", int7 == 100);
        org.junit.Assert.assertNotNull(wildcardClass12);
    }

    @Test
    public void test180() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test180");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        failing2_0.myIntInput = 1;
        failing2_0.myIntInput = 0;
        int int7 = failing2_0.myIntInput;
        failing2_0.myIntInput = 35;
        failing2_0.myIntInput = (short) 1;
        int int12 = failing2_0.myIntInput;
        int int13 = failing2_0.myIntInput;
        org.junit.Assert.assertTrue("'" + int7 + "' != '" + 0 + "'", int7 == 0);
        org.junit.Assert.assertTrue("'" + int12 + "' != '" + 1 + "'", int12 == 1);
        org.junit.Assert.assertTrue("'" + int13 + "' != '" + 1 + "'", int13 == 1);
    }

    @Test
    public void test181() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test181");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = 1;
        failing2_0.myIntInput = (byte) -1;
        failing2_0.myIntInput = 0;
        java.lang.Class<?> wildcardClass7 = failing2_0.getClass();
        org.junit.Assert.assertNotNull(wildcardClass7);
    }

    @Test
    public void test182() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test182");
        failing.Failing2 failing2_0 = new failing.Failing2();
        int int1 = failing2_0.myIntInput;
        int int2 = failing2_0.myIntInput;
        failing2_0.myIntInput = (byte) -1;
        failing2_0.myIntInput = (short) 0;
        int int7 = failing2_0.myIntInput;
        failing2_0.myIntInput = (byte) 0;
        failing2_0.myIntInput = 0;
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 0 + "'", int1 == 0);
        org.junit.Assert.assertTrue("'" + int2 + "' != '" + 0 + "'", int2 == 0);
        org.junit.Assert.assertTrue("'" + int7 + "' != '" + 0 + "'", int7 == 0);
    }

    @Test
    public void test183() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test183");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        int int3 = failing2_0.myIntInput;
        int int4 = failing2_0.myIntInput;
        failing2_0.myIntInput = '4';
        failing2_0.myIntInput = (short) 0;
        failing2_0.myIntInput = '#';
        failing2_0.myIntInput = (short) 10;
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + (-1) + "'", int3 == (-1));
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + (-1) + "'", int4 == (-1));
    }

    @Test
    public void test184() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test184");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (short) 1;
        failing2_0.myIntInput = 97;
    }

    @Test
    public void test185() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test185");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        failing2_0.myIntInput = 10;
        int int5 = failing2_0.myIntInput;
        org.junit.Assert.assertTrue("'" + int5 + "' != '" + 10 + "'", int5 == 10);
    }

    @Test
    public void test186() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test186");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        failing2_0.myIntInput = 10;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException((int) (short) 1);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
    }

    @Test
    public void test187() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test187");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        int int3 = failing2_0.myIntInput;
        failing2_0.myIntInput = (byte) 10;
        failing2_0.myIntInput = (byte) 10;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException((int) (short) 0);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + (-1) + "'", int3 == (-1));
    }

    @Test
    public void test188() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test188");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        int int3 = failing2_0.myIntInput;
        failing2_0.myIntInput = (short) 1;
        failing2_0.myIntInput = 100;
        failing2_0.myIntInput = (byte) 100;
        int int10 = failing2_0.myIntInput;
        failing2_0.myIntInput = (byte) 100;
        java.lang.Class<?> wildcardClass13 = failing2_0.getClass();
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + (-1) + "'", int3 == (-1));
        org.junit.Assert.assertTrue("'" + int10 + "' != '" + 100 + "'", int10 == 100);
        org.junit.Assert.assertNotNull(wildcardClass13);
    }

    @Test
    public void test189() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test189");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        int int3 = failing2_0.myIntInput;
        failing2_0.myIntInput = (byte) 10;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException((int) 'a');
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + (-1) + "'", int3 == (-1));
    }

    @Test
    public void test190() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test190");
        failing.Failing2 failing2_0 = new failing.Failing2();
        int int1 = failing2_0.myIntInput;
        failing2_0.myIntInput = 'a';
        int int4 = failing2_0.myIntInput;
        int int5 = failing2_0.myIntInput;
        failing2_0.myIntInput = (short) -1;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException((int) (byte) 100);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 0 + "'", int1 == 0);
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + 97 + "'", int4 == 97);
        org.junit.Assert.assertTrue("'" + int5 + "' != '" + 97 + "'", int5 == 97);
    }

    @Test
    public void test191() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test191");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        int int3 = failing2_0.myIntInput;
        int int4 = failing2_0.myIntInput;
        int int5 = failing2_0.myIntInput;
        failing2_0.myIntInput = (short) 1;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException(32);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + (-1) + "'", int3 == (-1));
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + (-1) + "'", int4 == (-1));
        org.junit.Assert.assertTrue("'" + int5 + "' != '" + (-1) + "'", int5 == (-1));
    }

    @Test
    public void test192() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test192");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        int int3 = failing2_0.myIntInput;
        int int4 = failing2_0.myIntInput;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException((int) '4');
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + (-1) + "'", int3 == (-1));
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + (-1) + "'", int4 == (-1));
    }

    @Test
    public void test193() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test193");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = 1;
        failing2_0.myIntInput = (byte) -1;
        failing2_0.myIntInput = 0;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException(52);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
    }

    @Test
    public void test194() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test194");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        failing2_0.myIntInput = 10;
        failing2_0.myIntInput = (short) -1;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException((int) (byte) 100);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
    }

    @Test
    public void test195() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test195");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        int int3 = failing2_0.myIntInput;
        failing2_0.myIntInput = 0;
        int int6 = failing2_0.myIntInput;
        int int7 = failing2_0.myIntInput;
        failing2_0.myIntInput = (byte) -1;
        int int10 = failing2_0.myIntInput;
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + (-1) + "'", int3 == (-1));
        org.junit.Assert.assertTrue("'" + int6 + "' != '" + 0 + "'", int6 == 0);
        org.junit.Assert.assertTrue("'" + int7 + "' != '" + 0 + "'", int7 == 0);
        org.junit.Assert.assertTrue("'" + int10 + "' != '" + (-1) + "'", int10 == (-1));
    }

    @Test
    public void test196() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test196");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        int int3 = failing2_0.myIntInput;
        int int4 = failing2_0.myIntInput;
        failing2_0.myIntInput = 'a';
        int int7 = failing2_0.myIntInput;
        int int8 = failing2_0.myIntInput;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException((int) (short) 10);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + (-1) + "'", int3 == (-1));
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + (-1) + "'", int4 == (-1));
        org.junit.Assert.assertTrue("'" + int7 + "' != '" + 97 + "'", int7 == 97);
        org.junit.Assert.assertTrue("'" + int8 + "' != '" + 97 + "'", int8 == 97);
    }

    @Test
    public void test197() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test197");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        int int3 = failing2_0.myIntInput;
        int int4 = failing2_0.myIntInput;
        failing2_0.myIntInput = '4';
        failing2_0.myIntInput = (short) 0;
        failing2_0.myIntInput = ' ';
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException(100);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + (-1) + "'", int3 == (-1));
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + (-1) + "'", int4 == (-1));
    }

    @Test
    public void test198() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test198");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        int int3 = failing2_0.myIntInput;
        failing2_0.myIntInput = (short) 1;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException(100);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + (-1) + "'", int3 == (-1));
    }

    @Test
    public void test199() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test199");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        int int3 = failing2_0.myIntInput;
        int int4 = failing2_0.myIntInput;
        failing2_0.myIntInput = ' ';
        failing2_0.myIntInput = (byte) 0;
        int int9 = failing2_0.myIntInput;
        int int10 = failing2_0.myIntInput;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException(100);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + (-1) + "'", int3 == (-1));
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + (-1) + "'", int4 == (-1));
        org.junit.Assert.assertTrue("'" + int9 + "' != '" + 0 + "'", int9 == 0);
        org.junit.Assert.assertTrue("'" + int10 + "' != '" + 0 + "'", int10 == 0);
    }

    @Test
    public void test200() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test200");
        failing.Failing2 failing2_0 = new failing.Failing2();
        int int1 = failing2_0.myIntInput;
        failing2_0.myIntInput = 'a';
        int int4 = failing2_0.myIntInput;
        failing2_0.myIntInput = 97;
        failing2_0.myIntInput = (short) 10;
        java.lang.Class<?> wildcardClass9 = failing2_0.getClass();
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 0 + "'", int1 == 0);
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + 97 + "'", int4 == 97);
        org.junit.Assert.assertNotNull(wildcardClass9);
    }

    @Test
    public void test201() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test201");
        failing.Failing2 failing2_0 = new failing.Failing2();
        int int1 = failing2_0.myIntInput;
        failing2_0.myIntInput = 'a';
        int int4 = failing2_0.myIntInput;
        failing2_0.myIntInput = (short) -1;
        failing2_0.myIntInput = (byte) 100;
        failing2_0.myIntInput = '4';
        int int11 = failing2_0.myIntInput;
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 0 + "'", int1 == 0);
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + 97 + "'", int4 == 97);
        org.junit.Assert.assertTrue("'" + int11 + "' != '" + 52 + "'", int11 == 52);
    }

    @Test
    public void test202() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test202");
        failing.Failing2 failing2_0 = new failing.Failing2();
        int int1 = failing2_0.myIntInput;
        failing2_0.myIntInput = 'a';
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException((int) (byte) 10);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 0 + "'", int1 == 0);
    }

    @Test
    public void test203() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test203");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = 1;
        failing2_0.myIntInput = (byte) -1;
        failing2_0.myIntInput = 0;
        int int7 = failing2_0.myIntInput;
        java.lang.Class<?> wildcardClass8 = failing2_0.getClass();
        org.junit.Assert.assertTrue("'" + int7 + "' != '" + 0 + "'", int7 == 0);
        org.junit.Assert.assertNotNull(wildcardClass8);
    }

    @Test
    public void test204() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test204");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        failing2_0.myIntInput = 1;
        failing2_0.myIntInput = 0;
        failing2_0.myIntInput = 0;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException(0);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
    }

    @Test
    public void test205() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test205");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        int int3 = failing2_0.myIntInput;
        int int4 = failing2_0.myIntInput;
        failing2_0.myIntInput = '#';
        failing2_0.myIntInput = (short) 100;
        failing2_0.myIntInput = 1;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException((int) (byte) 1);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + (-1) + "'", int3 == (-1));
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + (-1) + "'", int4 == (-1));
    }

    @Test
    public void test206() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test206");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = 1;
        int int3 = failing2_0.myIntInput;
        failing2_0.myIntInput = 0;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException(35);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + 1 + "'", int3 == 1);
    }

    @Test
    public void test207() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test207");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        int int3 = failing2_0.myIntInput;
        failing2_0.myIntInput = (byte) 10;
        failing2_0.myIntInput = 1;
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + (-1) + "'", int3 == (-1));
    }

    @Test
    public void test208() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test208");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        int int3 = failing2_0.myIntInput;
        failing2_0.myIntInput = (byte) 10;
        int int6 = failing2_0.myIntInput;
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + (-1) + "'", int3 == (-1));
        org.junit.Assert.assertTrue("'" + int6 + "' != '" + 10 + "'", int6 == 10);
    }

    @Test
    public void test209() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test209");
        failing.Failing2 failing2_0 = new failing.Failing2();
        int int1 = failing2_0.myIntInput;
        failing2_0.myIntInput = 'a';
        int int4 = failing2_0.myIntInput;
        int int5 = failing2_0.myIntInput;
        failing2_0.myIntInput = (short) -1;
        failing2_0.myIntInput = (-1);
        failing2_0.myIntInput = 0;
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 0 + "'", int1 == 0);
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + 97 + "'", int4 == 97);
        org.junit.Assert.assertTrue("'" + int5 + "' != '" + 97 + "'", int5 == 97);
    }

    @Test
    public void test210() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test210");
        failing.Failing2 failing2_0 = new failing.Failing2();
        int int1 = failing2_0.myIntInput;
        int int2 = failing2_0.myIntInput;
        failing2_0.myIntInput = ' ';
        int int5 = failing2_0.myIntInput;
        java.lang.Class<?> wildcardClass6 = failing2_0.getClass();
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 0 + "'", int1 == 0);
        org.junit.Assert.assertTrue("'" + int2 + "' != '" + 0 + "'", int2 == 0);
        org.junit.Assert.assertTrue("'" + int5 + "' != '" + 32 + "'", int5 == 32);
        org.junit.Assert.assertNotNull(wildcardClass6);
    }

    @Test
    public void test211() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test211");
        failing.Failing2 failing2_0 = new failing.Failing2();
        int int1 = failing2_0.myIntInput;
        failing2_0.myIntInput = 'a';
        int int4 = failing2_0.myIntInput;
        failing2_0.myIntInput = (short) -1;
        failing2_0.myIntInput = (byte) 100;
        java.lang.Class<?> wildcardClass9 = failing2_0.getClass();
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 0 + "'", int1 == 0);
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + 97 + "'", int4 == 97);
        org.junit.Assert.assertNotNull(wildcardClass9);
    }

    @Test
    public void test212() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test212");
        failing.Failing2 failing2_0 = new failing.Failing2();
        int int1 = failing2_0.myIntInput;
        failing2_0.myIntInput = 'a';
        int int4 = failing2_0.myIntInput;
        failing2_0.myIntInput = 97;
        int int7 = failing2_0.myIntInput;
        failing2_0.myIntInput = (byte) 0;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException((int) (short) 0);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 0 + "'", int1 == 0);
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + 97 + "'", int4 == 97);
        org.junit.Assert.assertTrue("'" + int7 + "' != '" + 97 + "'", int7 == 97);
    }

    @Test
    public void test213() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test213");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        int int3 = failing2_0.myIntInput;
        int int4 = failing2_0.myIntInput;
        failing2_0.myIntInput = '4';
        failing2_0.myIntInput = (short) 0;
        failing2_0.myIntInput = 'a';
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException((int) (short) 1);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + (-1) + "'", int3 == (-1));
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + (-1) + "'", int4 == (-1));
    }

    @Test
    public void test214() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test214");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        failing2_0.myIntInput = 1;
        int int5 = failing2_0.myIntInput;
        failing2_0.myIntInput = '4';
        int int8 = failing2_0.myIntInput;
        int int9 = failing2_0.myIntInput;
        failing2_0.myIntInput = (byte) 1;
        java.lang.Class<?> wildcardClass12 = failing2_0.getClass();
        org.junit.Assert.assertTrue("'" + int5 + "' != '" + 1 + "'", int5 == 1);
        org.junit.Assert.assertTrue("'" + int8 + "' != '" + 52 + "'", int8 == 52);
        org.junit.Assert.assertTrue("'" + int9 + "' != '" + 52 + "'", int9 == 52);
        org.junit.Assert.assertNotNull(wildcardClass12);
    }

    @Test
    public void test215() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test215");
        failing.Failing2 failing2_0 = new failing.Failing2();
        int int1 = failing2_0.myIntInput;
        failing2_0.myIntInput = 'a';
        failing2_0.myIntInput = 0;
        failing2_0.myIntInput = 'a';
        failing2_0.myIntInput = (short) 1;
        java.lang.Class<?> wildcardClass10 = failing2_0.getClass();
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 0 + "'", int1 == 0);
        org.junit.Assert.assertNotNull(wildcardClass10);
    }

    @Test
    public void test216() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test216");
        failing.Failing2 failing2_0 = new failing.Failing2();
        int int1 = failing2_0.myIntInput;
        failing2_0.myIntInput = 'a';
        int int4 = failing2_0.myIntInput;
        failing2_0.myIntInput = 97;
        int int7 = failing2_0.myIntInput;
        failing2_0.myIntInput = (byte) 0;
        int int10 = failing2_0.myIntInput;
        int int11 = failing2_0.myIntInput;
        int int12 = failing2_0.myIntInput;
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 0 + "'", int1 == 0);
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + 97 + "'", int4 == 97);
        org.junit.Assert.assertTrue("'" + int7 + "' != '" + 97 + "'", int7 == 97);
        org.junit.Assert.assertTrue("'" + int10 + "' != '" + 0 + "'", int10 == 0);
        org.junit.Assert.assertTrue("'" + int11 + "' != '" + 0 + "'", int11 == 0);
        org.junit.Assert.assertTrue("'" + int12 + "' != '" + 0 + "'", int12 == 0);
    }

    @Test
    public void test217() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test217");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = 1;
        failing2_0.myIntInput = (byte) -1;
        failing2_0.myIntInput = 0;
        int int7 = failing2_0.myIntInput;
        int int8 = failing2_0.myIntInput;
        org.junit.Assert.assertTrue("'" + int7 + "' != '" + 0 + "'", int7 == 0);
        org.junit.Assert.assertTrue("'" + int8 + "' != '" + 0 + "'", int8 == 0);
    }

    @Test
    public void test218() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test218");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        int int3 = failing2_0.myIntInput;
        int int4 = failing2_0.myIntInput;
        failing2_0.myIntInput = 'a';
        failing2_0.myIntInput = 'a';
        failing2_0.myIntInput = (short) 0;
        int int11 = failing2_0.myIntInput;
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + (-1) + "'", int3 == (-1));
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + (-1) + "'", int4 == (-1));
        org.junit.Assert.assertTrue("'" + int11 + "' != '" + 0 + "'", int11 == 0);
    }

    @Test
    public void test219() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test219");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = 1;
        int int3 = failing2_0.myIntInput;
        failing2_0.myIntInput = 0;
        java.lang.Class<?> wildcardClass6 = failing2_0.getClass();
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + 1 + "'", int3 == 1);
        org.junit.Assert.assertNotNull(wildcardClass6);
    }

    @Test
    public void test220() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test220");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (short) 1;
        int int3 = failing2_0.myIntInput;
        failing2_0.myIntInput = 0;
        java.lang.Class<?> wildcardClass6 = failing2_0.getClass();
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + 1 + "'", int3 == 1);
        org.junit.Assert.assertNotNull(wildcardClass6);
    }

    @Test
    public void test221() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test221");
        failing.Failing2 failing2_0 = new failing.Failing2();
        int int1 = failing2_0.myIntInput;
        int int2 = failing2_0.myIntInput;
        int int3 = failing2_0.myIntInput;
        failing2_0.myIntInput = 0;
        int int6 = failing2_0.myIntInput;
        java.lang.Class<?> wildcardClass7 = failing2_0.getClass();
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 0 + "'", int1 == 0);
        org.junit.Assert.assertTrue("'" + int2 + "' != '" + 0 + "'", int2 == 0);
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + 0 + "'", int3 == 0);
        org.junit.Assert.assertTrue("'" + int6 + "' != '" + 0 + "'", int6 == 0);
        org.junit.Assert.assertNotNull(wildcardClass7);
    }

    @Test
    public void test222() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test222");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        failing2_0.myIntInput = 10;
        failing2_0.myIntInput = (short) -1;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException((int) (byte) 0);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
    }

    @Test
    public void test223() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test223");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        failing2_0.myIntInput = 1;
        failing2_0.myIntInput = 0;
        int int7 = failing2_0.myIntInput;
        failing2_0.myIntInput = 35;
        failing2_0.myIntInput = (byte) 100;
        org.junit.Assert.assertTrue("'" + int7 + "' != '" + 0 + "'", int7 == 0);
    }

    @Test
    public void test224() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test224");
        failing.Failing2 failing2_0 = new failing.Failing2();
        int int1 = failing2_0.myIntInput;
        failing2_0.myIntInput = 'a';
        int int4 = failing2_0.myIntInput;
        failing2_0.myIntInput = 97;
        int int7 = failing2_0.myIntInput;
        failing2_0.myIntInput = '4';
        failing2_0.myIntInput = (short) 100;
        int int12 = failing2_0.myIntInput;
        int int13 = failing2_0.myIntInput;
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 0 + "'", int1 == 0);
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + 97 + "'", int4 == 97);
        org.junit.Assert.assertTrue("'" + int7 + "' != '" + 97 + "'", int7 == 97);
        org.junit.Assert.assertTrue("'" + int12 + "' != '" + 100 + "'", int12 == 100);
        org.junit.Assert.assertTrue("'" + int13 + "' != '" + 100 + "'", int13 == 100);
    }

    @Test
    public void test225() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test225");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        int int3 = failing2_0.myIntInput;
        int int4 = failing2_0.myIntInput;
        failing2_0.myIntInput = '4';
        failing2_0.myIntInput = 97;
        int int9 = failing2_0.myIntInput;
        int int10 = failing2_0.myIntInput;
        int int11 = failing2_0.myIntInput;
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + (-1) + "'", int3 == (-1));
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + (-1) + "'", int4 == (-1));
        org.junit.Assert.assertTrue("'" + int9 + "' != '" + 97 + "'", int9 == 97);
        org.junit.Assert.assertTrue("'" + int10 + "' != '" + 97 + "'", int10 == 97);
        org.junit.Assert.assertTrue("'" + int11 + "' != '" + 97 + "'", int11 == 97);
    }

    @Test
    public void test226() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test226");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = 0;
        int int3 = failing2_0.myIntInput;
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + 0 + "'", int3 == 0);
    }

    @Test
    public void test227() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test227");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        int int3 = failing2_0.myIntInput;
        int int4 = failing2_0.myIntInput;
        failing2_0.myIntInput = '#';
        failing2_0.myIntInput = (short) 100;
        failing2_0.myIntInput = 1;
        failing2_0.myIntInput = (byte) -1;
        java.lang.Class<?> wildcardClass13 = failing2_0.getClass();
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + (-1) + "'", int3 == (-1));
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + (-1) + "'", int4 == (-1));
        org.junit.Assert.assertNotNull(wildcardClass13);
    }

    @Test
    public void test228() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test228");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        int int3 = failing2_0.myIntInput;
        int int4 = failing2_0.myIntInput;
        failing2_0.myIntInput = ' ';
        int int7 = failing2_0.myIntInput;
        int int8 = failing2_0.myIntInput;
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + (-1) + "'", int3 == (-1));
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + (-1) + "'", int4 == (-1));
        org.junit.Assert.assertTrue("'" + int7 + "' != '" + 32 + "'", int7 == 32);
        org.junit.Assert.assertTrue("'" + int8 + "' != '" + 32 + "'", int8 == 32);
    }

    @Test
    public void test229() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test229");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        int int3 = failing2_0.myIntInput;
        failing2_0.myIntInput = (byte) 10;
        failing2_0.myIntInput = (byte) 10;
        failing2_0.myIntInput = (byte) 10;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException((int) (short) -1);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + (-1) + "'", int3 == (-1));
    }

    @Test
    public void test230() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test230");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (short) 1;
        int int3 = failing2_0.myIntInput;
        failing2_0.myIntInput = 0;
        int int6 = failing2_0.myIntInput;
        int int7 = failing2_0.myIntInput;
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + 1 + "'", int3 == 1);
        org.junit.Assert.assertTrue("'" + int6 + "' != '" + 0 + "'", int6 == 0);
        org.junit.Assert.assertTrue("'" + int7 + "' != '" + 0 + "'", int7 == 0);
    }

    @Test
    public void test231() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test231");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = 1;
        java.lang.Class<?> wildcardClass3 = failing2_0.getClass();
        org.junit.Assert.assertNotNull(wildcardClass3);
    }

    @Test
    public void test232() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test232");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        failing2_0.myIntInput = 1;
        failing2_0.myIntInput = (byte) 1;
        failing2_0.myIntInput = '#';
        int int9 = failing2_0.myIntInput;
        org.junit.Assert.assertTrue("'" + int9 + "' != '" + 35 + "'", int9 == 35);
    }

    @Test
    public void test233() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test233");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = 1;
        int int3 = failing2_0.myIntInput;
        int int4 = failing2_0.myIntInput;
        int int5 = failing2_0.myIntInput;
        failing2_0.myIntInput = 100;
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + 1 + "'", int3 == 1);
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + 1 + "'", int4 == 1);
        org.junit.Assert.assertTrue("'" + int5 + "' != '" + 1 + "'", int5 == 1);
    }

    @Test
    public void test234() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test234");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        int int3 = failing2_0.myIntInput;
        failing2_0.myIntInput = 0;
        int int6 = failing2_0.myIntInput;
        int int7 = failing2_0.myIntInput;
        failing2_0.myIntInput = (byte) -1;
        failing2_0.myIntInput = (short) -1;
        int int12 = failing2_0.myIntInput;
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + (-1) + "'", int3 == (-1));
        org.junit.Assert.assertTrue("'" + int6 + "' != '" + 0 + "'", int6 == 0);
        org.junit.Assert.assertTrue("'" + int7 + "' != '" + 0 + "'", int7 == 0);
        org.junit.Assert.assertTrue("'" + int12 + "' != '" + (-1) + "'", int12 == (-1));
    }

    @Test
    public void test235() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test235");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (short) 1;
        int int3 = failing2_0.myIntInput;
        failing2_0.myIntInput = 0;
        failing2_0.myIntInput = (byte) 10;
        int int8 = failing2_0.myIntInput;
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + 1 + "'", int3 == 1);
        org.junit.Assert.assertTrue("'" + int8 + "' != '" + 10 + "'", int8 == 10);
    }

    @Test
    public void test236() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test236");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = 1;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException(32);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
    }

    @Test
    public void test237() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test237");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (short) 1;
        int int3 = failing2_0.myIntInput;
        int int4 = failing2_0.myIntInput;
        failing2_0.myIntInput = (byte) 100;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException((int) (byte) 10);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + 1 + "'", int3 == 1);
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + 1 + "'", int4 == 1);
    }

    @Test
    public void test238() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test238");
        failing.Failing2 failing2_0 = new failing.Failing2();
        int int1 = failing2_0.myIntInput;
        int int2 = failing2_0.myIntInput;
        failing2_0.myIntInput = (byte) -1;
        failing2_0.myIntInput = (short) 0;
        int int7 = failing2_0.myIntInput;
        java.lang.Class<?> wildcardClass8 = failing2_0.getClass();
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 0 + "'", int1 == 0);
        org.junit.Assert.assertTrue("'" + int2 + "' != '" + 0 + "'", int2 == 0);
        org.junit.Assert.assertTrue("'" + int7 + "' != '" + 0 + "'", int7 == 0);
        org.junit.Assert.assertNotNull(wildcardClass8);
    }

    @Test
    public void test239() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test239");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        int int3 = failing2_0.myIntInput;
        failing2_0.myIntInput = (byte) 10;
        failing2_0.myIntInput = (byte) 10;
        failing2_0.myIntInput = (byte) 10;
        int int10 = failing2_0.myIntInput;
        java.lang.Class<?> wildcardClass11 = failing2_0.getClass();
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + (-1) + "'", int3 == (-1));
        org.junit.Assert.assertTrue("'" + int10 + "' != '" + 10 + "'", int10 == 10);
        org.junit.Assert.assertNotNull(wildcardClass11);
    }

    @Test
    public void test240() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test240");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = 1;
        failing2_0.myIntInput = (byte) -1;
        failing2_0.myIntInput = 0;
        failing2_0.myIntInput = (short) 100;
        failing2_0.myIntInput = 0;
    }

    @Test
    public void test241() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test241");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        failing2_0.myIntInput = 1;
        int int5 = failing2_0.myIntInput;
        failing2_0.myIntInput = '4';
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException(10);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int5 + "' != '" + 1 + "'", int5 == 1);
    }

    @Test
    public void test242() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test242");
        failing.Failing2 failing2_0 = new failing.Failing2();
        int int1 = failing2_0.myIntInput;
        int int2 = failing2_0.myIntInput;
        failing2_0.myIntInput = (byte) -1;
        java.lang.Class<?> wildcardClass5 = failing2_0.getClass();
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 0 + "'", int1 == 0);
        org.junit.Assert.assertTrue("'" + int2 + "' != '" + 0 + "'", int2 == 0);
        org.junit.Assert.assertNotNull(wildcardClass5);
    }

    @Test
    public void test243() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test243");
        failing.Failing2 failing2_0 = new failing.Failing2();
        int int1 = failing2_0.myIntInput;
        failing2_0.myIntInput = 'a';
        int int4 = failing2_0.myIntInput;
        failing2_0.myIntInput = 97;
        int int7 = failing2_0.myIntInput;
        failing2_0.myIntInput = (byte) 0;
        int int10 = failing2_0.myIntInput;
        int int11 = failing2_0.myIntInput;
        failing2_0.myIntInput = (byte) 1;
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 0 + "'", int1 == 0);
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + 97 + "'", int4 == 97);
        org.junit.Assert.assertTrue("'" + int7 + "' != '" + 97 + "'", int7 == 97);
        org.junit.Assert.assertTrue("'" + int10 + "' != '" + 0 + "'", int10 == 0);
        org.junit.Assert.assertTrue("'" + int11 + "' != '" + 0 + "'", int11 == 0);
    }

    @Test
    public void test244() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test244");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = 0;
        java.lang.Class<?> wildcardClass3 = failing2_0.getClass();
        org.junit.Assert.assertNotNull(wildcardClass3);
    }

    @Test
    public void test245() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test245");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        int int3 = failing2_0.myIntInput;
        int int4 = failing2_0.myIntInput;
        failing2_0.myIntInput = '4';
        failing2_0.myIntInput = (short) 0;
        failing2_0.myIntInput = ' ';
        int int11 = failing2_0.myIntInput;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException((int) (byte) 1);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + (-1) + "'", int3 == (-1));
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + (-1) + "'", int4 == (-1));
        org.junit.Assert.assertTrue("'" + int11 + "' != '" + 32 + "'", int11 == 32);
    }

    @Test
    public void test246() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test246");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        int int3 = failing2_0.myIntInput;
        int int4 = failing2_0.myIntInput;
        failing2_0.myIntInput = '#';
        failing2_0.myIntInput = (short) 100;
        failing2_0.myIntInput = 1;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException((int) (short) -1);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + (-1) + "'", int3 == (-1));
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + (-1) + "'", int4 == (-1));
    }

    @Test
    public void test247() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test247");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        int int3 = failing2_0.myIntInput;
        int int4 = failing2_0.myIntInput;
        failing2_0.myIntInput = '4';
        int int7 = failing2_0.myIntInput;
        failing2_0.myIntInput = '4';
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + (-1) + "'", int3 == (-1));
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + (-1) + "'", int4 == (-1));
        org.junit.Assert.assertTrue("'" + int7 + "' != '" + 52 + "'", int7 == 52);
    }

    @Test
    public void test248() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test248");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        int int3 = failing2_0.myIntInput;
        int int4 = failing2_0.myIntInput;
        failing2_0.myIntInput = '#';
        failing2_0.myIntInput = (short) 100;
        failing2_0.myIntInput = 1;
        java.lang.Class<?> wildcardClass11 = failing2_0.getClass();
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + (-1) + "'", int3 == (-1));
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + (-1) + "'", int4 == (-1));
        org.junit.Assert.assertNotNull(wildcardClass11);
    }

    @Test
    public void test249() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test249");
        failing.Failing2 failing2_0 = new failing.Failing2();
        int int1 = failing2_0.myIntInput;
        failing2_0.myIntInput = 'a';
        int int4 = failing2_0.myIntInput;
        failing2_0.myIntInput = 97;
        int int7 = failing2_0.myIntInput;
        failing2_0.myIntInput = 97;
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 0 + "'", int1 == 0);
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + 97 + "'", int4 == 97);
        org.junit.Assert.assertTrue("'" + int7 + "' != '" + 97 + "'", int7 == 97);
    }

    @Test
    public void test250() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test250");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = 1;
        int int3 = failing2_0.myIntInput;
        int int4 = failing2_0.myIntInput;
        int int5 = failing2_0.myIntInput;
        failing2_0.myIntInput = ' ';
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + 1 + "'", int3 == 1);
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + 1 + "'", int4 == 1);
        org.junit.Assert.assertTrue("'" + int5 + "' != '" + 1 + "'", int5 == 1);
    }

    @Test
    public void test251() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test251");
        failing.Failing2 failing2_0 = new failing.Failing2();
        int int1 = failing2_0.myIntInput;
        int int2 = failing2_0.myIntInput;
        failing2_0.myIntInput = (byte) -1;
        failing2_0.myIntInput = 97;
        failing2_0.myIntInput = (byte) 100;
        failing2_0.myIntInput = (short) 100;
        java.lang.Class<?> wildcardClass11 = failing2_0.getClass();
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 0 + "'", int1 == 0);
        org.junit.Assert.assertTrue("'" + int2 + "' != '" + 0 + "'", int2 == 0);
        org.junit.Assert.assertNotNull(wildcardClass11);
    }

    @Test
    public void test252() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test252");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        int int3 = failing2_0.myIntInput;
        failing2_0.myIntInput = (short) 1;
        failing2_0.myIntInput = 100;
        int int8 = failing2_0.myIntInput;
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + (-1) + "'", int3 == (-1));
        org.junit.Assert.assertTrue("'" + int8 + "' != '" + 100 + "'", int8 == 100);
    }

    @Test
    public void test253() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test253");
        failing.Failing2 failing2_0 = new failing.Failing2();
        int int1 = failing2_0.myIntInput;
        int int2 = failing2_0.myIntInput;
        failing2_0.myIntInput = (byte) -1;
        failing2_0.myIntInput = (short) 0;
        int int7 = failing2_0.myIntInput;
        failing2_0.myIntInput = (byte) 0;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException((int) (byte) 100);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 0 + "'", int1 == 0);
        org.junit.Assert.assertTrue("'" + int2 + "' != '" + 0 + "'", int2 == 0);
        org.junit.Assert.assertTrue("'" + int7 + "' != '" + 0 + "'", int7 == 0);
    }

    @Test
    public void test254() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test254");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        int int3 = failing2_0.myIntInput;
        failing2_0.myIntInput = (byte) 10;
        failing2_0.myIntInput = (byte) 10;
        failing2_0.myIntInput = (byte) 10;
        java.lang.Class<?> wildcardClass10 = failing2_0.getClass();
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + (-1) + "'", int3 == (-1));
        org.junit.Assert.assertNotNull(wildcardClass10);
    }

    @Test
    public void test255() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test255");
        failing.Failing2 failing2_0 = new failing.Failing2();
        int int1 = failing2_0.myIntInput;
        failing2_0.myIntInput = 'a';
        failing2_0.myIntInput = 0;
        failing2_0.myIntInput = (short) 100;
        int int8 = failing2_0.myIntInput;
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 0 + "'", int1 == 0);
        org.junit.Assert.assertTrue("'" + int8 + "' != '" + 100 + "'", int8 == 100);
    }

    @Test
    public void test256() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test256");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        failing2_0.myIntInput = 1;
        int int5 = failing2_0.myIntInput;
        failing2_0.myIntInput = '4';
        int int8 = failing2_0.myIntInput;
        int int9 = failing2_0.myIntInput;
        failing2_0.myIntInput = (byte) 1;
        failing2_0.myIntInput = (short) 1;
        org.junit.Assert.assertTrue("'" + int5 + "' != '" + 1 + "'", int5 == 1);
        org.junit.Assert.assertTrue("'" + int8 + "' != '" + 52 + "'", int8 == 52);
        org.junit.Assert.assertTrue("'" + int9 + "' != '" + 52 + "'", int9 == 52);
    }

    @Test
    public void test257() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test257");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (short) 1;
        int int3 = failing2_0.myIntInput;
        failing2_0.myIntInput = 0;
        failing2_0.myIntInput = 100;
        int int8 = failing2_0.myIntInput;
        int int9 = failing2_0.myIntInput;
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + 1 + "'", int3 == 1);
        org.junit.Assert.assertTrue("'" + int8 + "' != '" + 100 + "'", int8 == 100);
        org.junit.Assert.assertTrue("'" + int9 + "' != '" + 100 + "'", int9 == 100);
    }

    @Test
    public void test258() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test258");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        int int3 = failing2_0.myIntInput;
        failing2_0.myIntInput = (short) 1;
        int int6 = failing2_0.myIntInput;
        java.lang.Class<?> wildcardClass7 = failing2_0.getClass();
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + (-1) + "'", int3 == (-1));
        org.junit.Assert.assertTrue("'" + int6 + "' != '" + 1 + "'", int6 == 1);
        org.junit.Assert.assertNotNull(wildcardClass7);
    }

    @Test
    public void test259() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test259");
        failing.Failing2 failing2_0 = new failing.Failing2();
        int int1 = failing2_0.myIntInput;
        failing2_0.myIntInput = 'a';
        int int4 = failing2_0.myIntInput;
        int int5 = failing2_0.myIntInput;
        failing2_0.myIntInput = ' ';
        failing2_0.myIntInput = 52;
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 0 + "'", int1 == 0);
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + 97 + "'", int4 == 97);
        org.junit.Assert.assertTrue("'" + int5 + "' != '" + 97 + "'", int5 == 97);
    }

    @Test
    public void test260() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test260");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        int int3 = failing2_0.myIntInput;
        int int4 = failing2_0.myIntInput;
        failing2_0.myIntInput = '4';
        int int7 = failing2_0.myIntInput;
        int int8 = failing2_0.myIntInput;
        java.lang.Class<?> wildcardClass9 = failing2_0.getClass();
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + (-1) + "'", int3 == (-1));
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + (-1) + "'", int4 == (-1));
        org.junit.Assert.assertTrue("'" + int7 + "' != '" + 52 + "'", int7 == 52);
        org.junit.Assert.assertTrue("'" + int8 + "' != '" + 52 + "'", int8 == 52);
        org.junit.Assert.assertNotNull(wildcardClass9);
    }

    @Test
    public void test261() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test261");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        int int3 = failing2_0.myIntInput;
        failing2_0.myIntInput = (byte) 10;
        java.lang.Class<?> wildcardClass6 = failing2_0.getClass();
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + (-1) + "'", int3 == (-1));
        org.junit.Assert.assertNotNull(wildcardClass6);
    }

    @Test
    public void test262() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test262");
        failing.Failing2 failing2_0 = new failing.Failing2();
        int int1 = failing2_0.myIntInput;
        failing2_0.myIntInput = 'a';
        int int4 = failing2_0.myIntInput;
        failing2_0.myIntInput = 97;
        int int7 = failing2_0.myIntInput;
        failing2_0.myIntInput = (byte) 0;
        int int10 = failing2_0.myIntInput;
        int int11 = failing2_0.myIntInput;
        failing2_0.myIntInput = 52;
        int int14 = failing2_0.myIntInput;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException((int) '#');
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 0 + "'", int1 == 0);
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + 97 + "'", int4 == 97);
        org.junit.Assert.assertTrue("'" + int7 + "' != '" + 97 + "'", int7 == 97);
        org.junit.Assert.assertTrue("'" + int10 + "' != '" + 0 + "'", int10 == 0);
        org.junit.Assert.assertTrue("'" + int11 + "' != '" + 0 + "'", int11 == 0);
        org.junit.Assert.assertTrue("'" + int14 + "' != '" + 52 + "'", int14 == 52);
    }

    @Test
    public void test263() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test263");
        failing.Failing2 failing2_0 = new failing.Failing2();
        int int1 = failing2_0.myIntInput;
        failing2_0.myIntInput = 'a';
        int int4 = failing2_0.myIntInput;
        int int5 = failing2_0.myIntInput;
        failing2_0.myIntInput = ' ';
        int int8 = failing2_0.myIntInput;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException(0);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 0 + "'", int1 == 0);
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + 97 + "'", int4 == 97);
        org.junit.Assert.assertTrue("'" + int5 + "' != '" + 97 + "'", int5 == 97);
        org.junit.Assert.assertTrue("'" + int8 + "' != '" + 32 + "'", int8 == 32);
    }

    @Test
    public void test264() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test264");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        int int3 = failing2_0.myIntInput;
        failing2_0.myIntInput = (short) 1;
        failing2_0.myIntInput = 100;
        failing2_0.myIntInput = (byte) 100;
        failing2_0.myIntInput = ' ';
        int int12 = failing2_0.myIntInput;
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + (-1) + "'", int3 == (-1));
        org.junit.Assert.assertTrue("'" + int12 + "' != '" + 32 + "'", int12 == 32);
    }

    @Test
    public void test265() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test265");
        failing.Failing2 failing2_0 = new failing.Failing2();
        int int1 = failing2_0.myIntInput;
        int int2 = failing2_0.myIntInput;
        failing2_0.myIntInput = (byte) -1;
        failing2_0.myIntInput = (short) 0;
        int int7 = failing2_0.myIntInput;
        failing2_0.myIntInput = 1;
        int int10 = failing2_0.myIntInput;
        java.lang.Class<?> wildcardClass11 = failing2_0.getClass();
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 0 + "'", int1 == 0);
        org.junit.Assert.assertTrue("'" + int2 + "' != '" + 0 + "'", int2 == 0);
        org.junit.Assert.assertTrue("'" + int7 + "' != '" + 0 + "'", int7 == 0);
        org.junit.Assert.assertTrue("'" + int10 + "' != '" + 1 + "'", int10 == 1);
        org.junit.Assert.assertNotNull(wildcardClass11);
    }

    @Test
    public void test266() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test266");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        int int3 = failing2_0.myIntInput;
        int int4 = failing2_0.myIntInput;
        failing2_0.myIntInput = '4';
        failing2_0.myIntInput = (short) 0;
        failing2_0.myIntInput = 97;
        int int11 = failing2_0.myIntInput;
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + (-1) + "'", int3 == (-1));
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + (-1) + "'", int4 == (-1));
        org.junit.Assert.assertTrue("'" + int11 + "' != '" + 97 + "'", int11 == 97);
    }

    @Test
    public void test267() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test267");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        int int3 = failing2_0.myIntInput;
        failing2_0.myIntInput = 0;
        failing2_0.myIntInput = ' ';
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + (-1) + "'", int3 == (-1));
    }

    @Test
    public void test268() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test268");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        failing2_0.myIntInput = 1;
        failing2_0.myIntInput = (byte) 1;
        failing2_0.myIntInput = 0;
    }

    @Test
    public void test269() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test269");
        failing.Failing2 failing2_0 = new failing.Failing2();
        int int1 = failing2_0.myIntInput;
        int int2 = failing2_0.myIntInput;
        failing2_0.myIntInput = (byte) -1;
        failing2_0.myIntInput = 0;
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 0 + "'", int1 == 0);
        org.junit.Assert.assertTrue("'" + int2 + "' != '" + 0 + "'", int2 == 0);
    }

    @Test
    public void test270() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test270");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        failing2_0.myIntInput = 1;
        failing2_0.myIntInput = (short) 100;
        int int7 = failing2_0.myIntInput;
        int int8 = failing2_0.myIntInput;
        org.junit.Assert.assertTrue("'" + int7 + "' != '" + 100 + "'", int7 == 100);
        org.junit.Assert.assertTrue("'" + int8 + "' != '" + 100 + "'", int8 == 100);
    }

    @Test
    public void test271() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test271");
        failing.Failing2 failing2_0 = new failing.Failing2();
        int int1 = failing2_0.myIntInput;
        failing2_0.myIntInput = 'a';
        int int4 = failing2_0.myIntInput;
        failing2_0.myIntInput = 97;
        int int7 = failing2_0.myIntInput;
        failing2_0.myIntInput = '4';
        failing2_0.myIntInput = (short) 100;
        failing2_0.myIntInput = 32;
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 0 + "'", int1 == 0);
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + 97 + "'", int4 == 97);
        org.junit.Assert.assertTrue("'" + int7 + "' != '" + 97 + "'", int7 == 97);
    }

    @Test
    public void test272() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test272");
        failing.Failing2 failing2_0 = new failing.Failing2();
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException(100);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
    }

    @Test
    public void test273() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test273");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = 1;
        failing2_0.myIntInput = (byte) -1;
        failing2_0.myIntInput = 0;
        failing2_0.myIntInput = (short) 100;
        failing2_0.myIntInput = (byte) 0;
    }

    @Test
    public void test274() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test274");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (short) 1;
        int int3 = failing2_0.myIntInput;
        java.lang.Class<?> wildcardClass4 = failing2_0.getClass();
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + 1 + "'", int3 == 1);
        org.junit.Assert.assertNotNull(wildcardClass4);
    }

    @Test
    public void test275() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test275");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (short) 1;
        int int3 = failing2_0.myIntInput;
        failing2_0.myIntInput = 0;
        int int6 = failing2_0.myIntInput;
        failing2_0.myIntInput = '4';
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException((int) (short) 1);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + 1 + "'", int3 == 1);
        org.junit.Assert.assertTrue("'" + int6 + "' != '" + 0 + "'", int6 == 0);
    }

    @Test
    public void test276() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test276");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        failing2_0.myIntInput = 1;
        int int5 = failing2_0.myIntInput;
        failing2_0.myIntInput = '4';
        int int8 = failing2_0.myIntInput;
        int int9 = failing2_0.myIntInput;
        failing2_0.myIntInput = (byte) 1;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException(100);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int5 + "' != '" + 1 + "'", int5 == 1);
        org.junit.Assert.assertTrue("'" + int8 + "' != '" + 52 + "'", int8 == 52);
        org.junit.Assert.assertTrue("'" + int9 + "' != '" + 52 + "'", int9 == 52);
    }

    @Test
    public void test277() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test277");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        int int3 = failing2_0.myIntInput;
        int int4 = failing2_0.myIntInput;
        failing2_0.myIntInput = '4';
        failing2_0.myIntInput = 97;
        int int9 = failing2_0.myIntInput;
        failing2_0.myIntInput = '4';
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + (-1) + "'", int3 == (-1));
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + (-1) + "'", int4 == (-1));
        org.junit.Assert.assertTrue("'" + int9 + "' != '" + 97 + "'", int9 == 97);
    }

    @Test
    public void test278() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test278");
        failing.Failing2 failing2_0 = new failing.Failing2();
        int int1 = failing2_0.myIntInput;
        failing2_0.myIntInput = 'a';
        failing2_0.myIntInput = 0;
        failing2_0.myIntInput = 97;
        int int8 = failing2_0.myIntInput;
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 0 + "'", int1 == 0);
        org.junit.Assert.assertTrue("'" + int8 + "' != '" + 97 + "'", int8 == 97);
    }

    @Test
    public void test279() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test279");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (short) 1;
        int int3 = failing2_0.myIntInput;
        int int4 = failing2_0.myIntInput;
        java.lang.Class<?> wildcardClass5 = failing2_0.getClass();
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + 1 + "'", int3 == 1);
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + 1 + "'", int4 == 1);
        org.junit.Assert.assertNotNull(wildcardClass5);
    }

    @Test
    public void test280() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test280");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        failing2_0.myIntInput = 1;
        int int5 = failing2_0.myIntInput;
        failing2_0.myIntInput = '4';
        int int8 = failing2_0.myIntInput;
        java.lang.Class<?> wildcardClass9 = failing2_0.getClass();
        org.junit.Assert.assertTrue("'" + int5 + "' != '" + 1 + "'", int5 == 1);
        org.junit.Assert.assertTrue("'" + int8 + "' != '" + 52 + "'", int8 == 52);
        org.junit.Assert.assertNotNull(wildcardClass9);
    }

    @Test
    public void test281() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test281");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        int int3 = failing2_0.myIntInput;
        failing2_0.myIntInput = (short) 1;
        failing2_0.myIntInput = 1;
        java.lang.Class<?> wildcardClass8 = failing2_0.getClass();
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + (-1) + "'", int3 == (-1));
        org.junit.Assert.assertNotNull(wildcardClass8);
    }

    @Test
    public void test282() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test282");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        failing2_0.myIntInput = 1;
        failing2_0.myIntInput = (short) -1;
        failing2_0.myIntInput = (-1);
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException((int) (byte) 10);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
    }

    @Test
    public void test283() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test283");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        int int3 = failing2_0.myIntInput;
        int int4 = failing2_0.myIntInput;
        failing2_0.myIntInput = 'a';
        failing2_0.myIntInput = 'a';
        failing2_0.myIntInput = (short) 0;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException((int) (short) 0);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + (-1) + "'", int3 == (-1));
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + (-1) + "'", int4 == (-1));
    }

    @Test
    public void test284() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test284");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        int int3 = failing2_0.myIntInput;
        int int4 = failing2_0.myIntInput;
        failing2_0.myIntInput = '4';
        int int7 = failing2_0.myIntInput;
        failing2_0.myIntInput = (short) 100;
        java.lang.Class<?> wildcardClass10 = failing2_0.getClass();
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + (-1) + "'", int3 == (-1));
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + (-1) + "'", int4 == (-1));
        org.junit.Assert.assertTrue("'" + int7 + "' != '" + 52 + "'", int7 == 52);
        org.junit.Assert.assertNotNull(wildcardClass10);
    }

    @Test
    public void test285() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test285");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        failing2_0.myIntInput = 1;
        failing2_0.myIntInput = 0;
        int int7 = failing2_0.myIntInput;
        failing2_0.myIntInput = '#';
        int int10 = failing2_0.myIntInput;
        failing2_0.myIntInput = '4';
        failing2_0.myIntInput = 35;
        org.junit.Assert.assertTrue("'" + int7 + "' != '" + 0 + "'", int7 == 0);
        org.junit.Assert.assertTrue("'" + int10 + "' != '" + 35 + "'", int10 == 35);
    }

    @Test
    public void test286() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test286");
        failing.Failing2 failing2_0 = new failing.Failing2();
        int int1 = failing2_0.myIntInput;
        int int2 = failing2_0.myIntInput;
        failing2_0.myIntInput = (byte) -1;
        failing2_0.myIntInput = 97;
        int int7 = failing2_0.myIntInput;
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 0 + "'", int1 == 0);
        org.junit.Assert.assertTrue("'" + int2 + "' != '" + 0 + "'", int2 == 0);
        org.junit.Assert.assertTrue("'" + int7 + "' != '" + 97 + "'", int7 == 97);
    }

    @Test
    public void test287() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test287");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        int int3 = failing2_0.myIntInput;
        failing2_0.myIntInput = (byte) 10;
        failing2_0.myIntInput = (byte) 10;
        failing2_0.myIntInput = (byte) 10;
        int int10 = failing2_0.myIntInput;
        int int11 = failing2_0.myIntInput;
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + (-1) + "'", int3 == (-1));
        org.junit.Assert.assertTrue("'" + int10 + "' != '" + 10 + "'", int10 == 10);
        org.junit.Assert.assertTrue("'" + int11 + "' != '" + 10 + "'", int11 == 10);
    }

    @Test
    public void test288() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test288");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        int int3 = failing2_0.myIntInput;
        int int4 = failing2_0.myIntInput;
        failing2_0.myIntInput = '4';
        failing2_0.myIntInput = (short) 0;
        failing2_0.myIntInput = ' ';
        int int11 = failing2_0.myIntInput;
        failing2_0.myIntInput = (byte) -1;
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + (-1) + "'", int3 == (-1));
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + (-1) + "'", int4 == (-1));
        org.junit.Assert.assertTrue("'" + int11 + "' != '" + 32 + "'", int11 == 32);
    }

    @Test
    public void test289() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test289");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        failing2_0.myIntInput = 1;
        failing2_0.myIntInput = 0;
        int int7 = failing2_0.myIntInput;
        failing2_0.myIntInput = 35;
        java.lang.Class<?> wildcardClass10 = failing2_0.getClass();
        org.junit.Assert.assertTrue("'" + int7 + "' != '" + 0 + "'", int7 == 0);
        org.junit.Assert.assertNotNull(wildcardClass10);
    }

    @Test
    public void test290() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test290");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (short) 1;
        int int3 = failing2_0.myIntInput;
        int int4 = failing2_0.myIntInput;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException((int) (byte) 100);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + 1 + "'", int3 == 1);
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + 1 + "'", int4 == 1);
    }

    @Test
    public void test291() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test291");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        failing2_0.myIntInput = 1;
        failing2_0.myIntInput = (byte) 1;
        int int7 = failing2_0.myIntInput;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException(52);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int7 + "' != '" + 1 + "'", int7 == 1);
    }

    @Test
    public void test292() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test292");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        int int3 = failing2_0.myIntInput;
        failing2_0.myIntInput = 0;
        failing2_0.myIntInput = 32;
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + (-1) + "'", int3 == (-1));
    }

    @Test
    public void test293() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test293");
        failing.Failing2 failing2_0 = new failing.Failing2();
        int int1 = failing2_0.myIntInput;
        int int2 = failing2_0.myIntInput;
        failing2_0.myIntInput = (byte) -1;
        failing2_0.myIntInput = (short) 0;
        int int7 = failing2_0.myIntInput;
        failing2_0.myIntInput = (byte) 0;
        // The following exception was thrown during execution in test generation
        try {
            failing2_0.throwCaughtException(100);
            org.junit.Assert.fail("Expected exception of type java.io.IOException; message: Throwing IOException");
        } catch (java.io.IOException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 0 + "'", int1 == 0);
        org.junit.Assert.assertTrue("'" + int2 + "' != '" + 0 + "'", int2 == 0);
        org.junit.Assert.assertTrue("'" + int7 + "' != '" + 0 + "'", int7 == 0);
    }

    @Test
    public void test294() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test294");
        failing.Failing2 failing2_0 = new failing.Failing2();
        int int1 = failing2_0.myIntInput;
        failing2_0.myIntInput = 'a';
        int int4 = failing2_0.myIntInput;
        int int5 = failing2_0.myIntInput;
        failing2_0.myIntInput = (short) -1;
        failing2_0.myIntInput = 97;
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 0 + "'", int1 == 0);
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + 97 + "'", int4 == 97);
        org.junit.Assert.assertTrue("'" + int5 + "' != '" + 97 + "'", int5 == 97);
    }

    @Test
    public void test295() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test295");
        failing.Failing2 failing2_0 = new failing.Failing2();
        failing2_0.myIntInput = (-1);
        int int3 = failing2_0.myIntInput;
        failing2_0.myIntInput = 0;
        failing2_0.myIntInput = (short) 0;
        int int8 = failing2_0.myIntInput;
        failing2_0.myIntInput = (short) 0;
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + (-1) + "'", int3 == (-1));
        org.junit.Assert.assertTrue("'" + int8 + "' != '" + 0 + "'", int8 == 0);
    }
}

