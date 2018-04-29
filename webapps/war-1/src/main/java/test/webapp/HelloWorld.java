package test.webapp;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class HelloWorld extends HttpServlet {
  private static final long serialVersionUID = 1L;

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    response.setContentType("text/html");
    PrintWriter pw = response.getWriter();
    pw.println("<html><body>");
    pw.println("<b>HelloWorld: msg</b><br/><br/><br/>othermsg");
    pw.println("</body></html>");
  }
}
