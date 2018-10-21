package sjtu.edu;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Created by Pengfei Jin on 2018/10/20.
 */
@WebServlet(name = "BufferParseServlet")
public class BufferParseServlet extends HttpServlet {
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("get access to servlet");

		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		String result = null;
		try {
			List<FileItem> list = upload.parseRequest(request);
			BufferedImage bufferedImage = ImageIO.read(list.get(0).getInputStream());
			System.out.println(bufferedImage);
			ITesseract instance = new Tesseract();
			instance.setDatapath("D:\\Github\\jar\\Tess4J\\tessdata");
			instance.setLanguage("eng");//chi_sim ：简体中文， eng	根据需求选择语言库

			result =  instance.doOCR(bufferedImage);
		} catch (Exception e) {
			result = e.getMessage();
		}
		System.out.println("result: ");
		System.out.println(result);
		result = result.replace("\n","<br>");
		//以流的方式将结果响应到AJAX异步对象中
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter pw = response.getWriter();
		pw.write(result);
		pw.flush();
		pw.close();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request,response);
	}

}
