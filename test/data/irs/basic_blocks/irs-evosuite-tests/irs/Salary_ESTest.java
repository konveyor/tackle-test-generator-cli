/*
 * This file was automatically generated by EvoSuite
 * Mon Jan 10 10:46:52 GMT 2022
 */

package irs;

import org.junit.Test;
import static org.junit.Assert.*;
import irs.Salary;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class) @EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true, useJEE = true) 
public class Salary_ESTest extends Salary_ESTest_scaffolding {

  @Test(timeout = 4000)
  public void test0()  throws Throwable  {
      Salary salary0 = new Salary(0, 3060, 0);
      salary0.setSalary((-245.0276));
  }

  @Test(timeout = 4000)
  public void test1()  throws Throwable  {
      Salary salary0 = new Salary();
  }

  @Test(timeout = 4000)
  public void test2()  throws Throwable  {
      Salary salary0 = new Salary(0, 3060, 0);
      double double0 = salary0.getSalary();
  }

  @Test(timeout = 4000)
  public void test3()  throws Throwable  {
      Salary salary0 = new Salary(0, 3060, 0);
      salary0.setEmployeeId((-590));
  }

  @Test(timeout = 4000)
  public void test4()  throws Throwable  {
      Salary salary0 = new Salary(0, 3060, 0);
      int int0 = salary0.getEmployeeId();
  }

  @Test(timeout = 4000)
  public void test5()  throws Throwable  {
      Salary salary0 = new Salary(0, 3060, 0);
      salary0.setEmployerId(0);
  }

  @Test(timeout = 4000)
  public void test6()  throws Throwable  {
      Salary salary0 = new Salary(0, 3060, 0);
      int int0 = salary0.getEmployerId();
  }
}
