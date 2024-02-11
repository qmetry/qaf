package org.testng;

/**
 * This is added to support TestNG below 7 and above 7 versions. TestNG 7.1+ had merged {@link IInvokedMethodListener2} into {@link IInvokedMethodListener} 
 */
public interface IInvokedMethodListener2 extends IInvokedMethodListener {

  /**
   * To be implemented if the method needs a handle to contextual information.
   */
  void beforeInvocation(IInvokedMethod method, ITestResult testResult,
      ITestContext context);

  void afterInvocation(IInvokedMethod method, ITestResult testResult,
      ITestContext context);

}