package npcg;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.UUID;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Servlet implementation class Generate
 */
@WebServlet("/Generate")
public class Generate extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static final String SRC  = "PDF/";
	private static final String NPCTEMPLATE = "CoC-M-7E.pdf";
      
	public void checkIfNull(String value) {
		if (value == null) {
			value = "";
		}
	}
	
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Generate() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		/*
		// Verify Google reCATPCHA
        String gRecaptchaResponse = request.getParameter("g-recaptcha-response");
        boolean valid = VerifyUtils.verify(gRecaptchaResponse);
        if (!valid && checkreCaptcha){
        	// Redirect to the login page if not successful and show error message
        	String recaptchaError = "Invalid reCAPTCHA";
			request.getRequestDispatcher("index.html").include(request,response);
        	out.println("<div class='ERROR'>"+ recaptchaError + "</div>"); 
        	return;
        }
        */
		
		_die die = new _die();
		
		// Get User Submitted Parameter
		String name = request.getParameter("Investigator_Name");
		String player = request.getParameter("Player_Name");
		String occupation = request.getParameter("Occupation");
		String residence = request.getParameter("Residence");
		String birthplace = request.getParameter("Birthplace");
		String age = request.getParameter("Age");
		String sex = request.getParameter("Sex");
		
		this.checkIfNull(name);
		this.checkIfNull(player);
		this.checkIfNull(occupation);
		this.checkIfNull(residence);
		this.checkIfNull(birthplace);
		this.checkIfNull(sex);
		
		if (age == null) {
			age = "" + (die.roll(75) + 14);
		}
		
		// Get the path of the source pdf file that we will be filling out 
		String source = getServletContext().getRealPath(SRC);
		String PDFpath = source + NPCTEMPLATE;
		
		// Get the path of the destination pdf file, the generated NPC
		String DEST = request.getServletContext().getRealPath("/") + "PDF/";
		String ID = String.format("%06d", (int) (Math.random() * 100000));
		String GeneratedPDF = DEST + ID + "_" +name + ".pdf";
		
		PdfDocument pdf = new PdfDocument(new PdfReader(PDFpath), new PdfWriter(GeneratedPDF));
		PdfAcroForm form = PdfAcroForm.getAcroForm(pdf, true);
		Map<String, PdfFormField> fields = form.getFormFields();
		
		// Fill in Names
		fields.get("Investigator_Name").setValue(name);
		fields.get("Player_Name").setValue(player);
		fields.get("Occupation").setValue(occupation);
		fields.get("Residence").setValue(residence);
		fields.get("Birthplace").setValue(birthplace);
		fields.get("Age").setValue(age);
		fields.get("Sex").setValue(sex);
		
		// Generate Characteristics
		String resources = getServletContext().getRealPath("Resources");
		_characteristics.GenerateCharacteristics(fields, resources);
		
		// Close the PDF
		pdf.close();		
		
		//String GeneratedPDF = DEST + ID + "_" +name + ".pdf";
		// Redirect To the Home Page if Success
		System.out.println(name);
		System.out.println(GeneratedPDF);
		request.setAttribute("NPC_NAME", name);
		request.setAttribute("NPC", ID + "_" + name);
		RequestDispatcher dispatch = request.getRequestDispatcher("NPC.jsp");
		dispatch.forward(request, response);
		
		
		
		/*
		try{
			// Incorporate MySQL driver
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection connection = DriverManager.getConnection(MovieDB.loginURL, MovieDB.loginUser, MovieDB.loginPassword);
			Statement select = connection.createStatement();
			ResultSet movie = select.executeQuery(query);
			
			// Insert the NPC into the DB
			movieID 	= UUID.randomUUID().toString().replaceAll("-", "").substring(0, 10);
			String insertStatement = String.format("INSERT INTO movies VALUES ('%s', '%s', %d, '%s')", movieID, title, year, director);
			int insertSuccess = select.executeUpdate(insertStatement);
			if (insertSuccess == 1){
				finalMessage += String.format("Movie(%s, %s, %d, %s) added to movies table<br>",movieID, title, year, director);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		*/
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
