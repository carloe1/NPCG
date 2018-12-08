package npcg;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
	private static final String DEST = "C:\\Users\\carlo\\Desktop\\";
       
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
		
		// Get User Submitted Parameter
		String name = request.getParameter("Investigator_Name");
		String player = request.getParameter("Player_Name");
		String occupation = request.getParameter("Occupation");
		String residence = request.getParameter("Residence");
		String birthplace = request.getParameter("Birthplace");
		String age = request.getParameter("Age");
		String sex = request.getParameter("Sex");

		// Get the path of the source pdf file that we will be filling out 
		String source = getServletContext().getRealPath(SRC);
		String PDFpath = source + NPCTEMPLATE;
		
		// Get the path of the destination pdf file, the generated NPC
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
		_characteristics.GenerateCharacteristics(fields);
		
		// Generate Health and Sanity
		_health.GenerateHealth(fields);
		
		// Close the PDF
		pdf.close();
		
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
        

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
