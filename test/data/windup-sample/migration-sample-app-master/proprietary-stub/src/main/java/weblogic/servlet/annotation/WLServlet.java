package weblogic.servlet.annotation;

public @interface WLServlet {
   String name();
   String runAs();
   WLInitParam[] initParams();
   String[] mapping();
}
