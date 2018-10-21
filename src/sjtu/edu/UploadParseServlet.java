package sjtu.edu;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.UUID;

/**
 * Created by Pengfei Jin on 2018/10/20.
 */
@WebServlet(name = "ParseServlet")
public class UploadParseServlet extends HttpServlet {
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println(request.getQueryString());
		String filename = this.uploadImage(request,response);
		this.outputResult(filename, request, response);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request,response);
	}
	protected String uploadImage(HttpServletRequest request, HttpServletResponse response){
		String filename = null;
		try {
			// 1. 文件上传工厂
			FileItemFactory factory = new DiskFileItemFactory();
			// 2. 创建文件上传核心工具类
			ServletFileUpload upload = new ServletFileUpload(factory);

			// 一、设置单个文件允许的最大的大小： 30M
			upload.setFileSizeMax(30*1024*1024);
			// 二、设置文件上传表单允许的总大小: 80M
			upload.setSizeMax(80*1024*1024);
			// 三、 设置上传表单文件名的编码
			// 相当于：request.setCharacterEncoding("UTF-8");
			upload.setHeaderEncoding("UTF-8");


			// 3. 判断： 当前表单是否为文件上传表单
			if (upload.isMultipartContent(request)){
				// 4. 把请求数据转换为一个个FileItem对象，再用集合封装
				List<FileItem> list = upload.parseRequest(request);
				//FileItem item = list.get(0);
				// 遍历： 得到每一个上传的数据
				for (FileItem item: list){
					// 判断：普通文本数据
					if (item.isFormField()){
						// 普通文本数据
						String fieldName = item.getFieldName();	// 表单元素名称
						String content = item.getString();		// 表单元素名称， 对应的数据
						//item.getString("UTF-8");  指定编码
						System.out.println(fieldName + " " + content);
					}
					// 上传文件(文件流) ----> 上传到upload目录下
					else {
						// 普通文本数据
						String fieldName = item.getFieldName();	// 表单元素名称

						filename = item.getName();			// 文件名
						int index=filename.lastIndexOf("\\");
						if(index!=-1) {
							filename=filename.substring(index+1);
						}
						String content = item.getString();		// 表单元素名称， 对应的数据
						String type = item.getContentType();	// 文件类型
						InputStream in = item.getInputStream(); // 上传文件流

						/*
						 *  四、文件名重名
						 *  对于不同用户readme.txt文件，不希望覆盖！
						 *  后台处理： 给用户添加一个唯一标记!
						 */
						// a. 随机生成一个唯一标记
						//String id = UUID.randomUUID().toString();
						// b. 与文件名拼接
						//name = id +"#"+ name;

						// 获取上传基路径
						String path = "D:/upload";
						// 创建目标文件
						File file = new File(path,filename);

						// 工具类，文件上传
						item.write(file);
						item.delete();   //删除系统产生的临时文件

						System.out.println("完成文件上传");
					}

				}
			}
			else {
				System.out.println("当前表单不是文件上传表单，处理失败！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(filename);
		return filename;
	}

	protected void outputResult(String filename, HttpServletRequest request, HttpServletResponse response) throws IOException {
		String basePath = "D:/upload/";		//获取上传到服务器内待解析图片文件夹

		File file = new File(basePath+filename);
		ITesseract instance = new Tesseract();

		//设置训练库的位置
		instance.setDatapath("D:\\Github\\jar\\Tess4J\\tessdata");

		instance.setLanguage("eng");//chi_sim ：简体中文， eng	根据需求选择语言库
		String result = null;
		try {
			result =  instance.doOCR(file);
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
}
