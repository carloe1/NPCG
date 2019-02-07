package npcg;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Download
 */
@WebServlet("/Download")
public class Download extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Download() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String DEST = request.getServletContext().getRealPath("/") + "PDF/";
		String filename = (String) request.getParameter("path");
		String GeneratedPDF = DEST + filename + ".pdf";
		String name = (String) request.getParameter("name");
		
		// File Download
        response.setContentType("application/pdf");
        response.setHeader("Content-disposition","attachment; " + GeneratedPDF);
		
        
        File my_file = new File(GeneratedPDF);

        // This should send the file to browser
        OutputStream out = response.getOutputStream();
        FileInputStream in = new FileInputStream(my_file);
        byte[] buffer = new byte[4096];
        int length;
        while ((length = in.read(buffer)) > 0){
           out.write(buffer, 0, length);
        }
        in.close();
        out.flush();
        
        
        // Redirect To the Home Page if Success
 		System.out.println(name);
 		System.out.println(GeneratedPDF);
 		request.setAttribute("NPC_NAME", name);
 		request.setAttribute("NPC", filename);
 		RequestDispatcher dispatch = request.getRequestDispatcher("NPC.jsp");
 		dispatch.forward(request, response);
        
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
