package sjtu.edu;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Pengfei Jin on 2018/10/20.
 */
@WebServlet(name = "ParseServlet")
public class ParseServlet extends HttpServlet {
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String path = request.getParameter("path");		//解析图片文件夹

		File file = new File(path);
		ITesseract instance = new Tesseract();

		/**
		 *  获取项目根路径，例如： D:\IDEAWorkSpace\tess4J
		 */
		File directory = new File("D:\\Github\\ORCTest");
		String courseFile = null;
		try {
			courseFile = directory.getCanonicalPath();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//设置训练库的位置
		instance.setDatapath(courseFile + "//tessdata");

		instance.setLanguage("chi_sim");//chi_sim ：简体中文， eng	根据需求选择语言库
		String result = null;
		try {
			long startTime = System.currentTimeMillis();
			result =  instance.doOCR(file);
			long endTime = System.currentTimeMillis();
			System.out.println("Time is：" + (endTime - startTime) + " 毫秒");
		} catch (TesseractException e) {
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
