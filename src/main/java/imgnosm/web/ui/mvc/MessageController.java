
package imgnosm.web.ui.mvc;

import groovyjarjarasm.asm.tree.IntInsnNode;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import imgnosm.web.ui.Message;
import imgnosm.web.ui.MessageRepository;
import imgnosm.web.ui.HttpRequest;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItem;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.JsonNodeFactory;

import imgnosm.imghash.Fingerprint;

@Controller
@RequestMapping("/")
public class MessageController {

	private final MessageRepository messageRepository;


	public MessageController(MessageRepository messageRepository) {
		this.messageRepository = messageRepository;
	}

	@RequestMapping
	public String index() {
		return "index";
	}


	@RequestMapping(value = "getHash", params = "hash", method = RequestMethod.GET)
	public ModelAndView getHash(@ModelAttribute("hash") String hash) {
		String[] results = null;
		return new ModelAndView("oauth", "texts", results);
	}
	
	@RequestMapping(value = "searchHash", params = "hash", method = RequestMethod.GET)
	public ModelAndView searchHash(@ModelAttribute("hash") String hash) {
		String[] results = null;
		return new ModelAndView("oauth", "texts", results);
	}
	
	@RequestMapping(value = "upload", method = RequestMethod.GET)
	public String uploadView() {
		return "upload";
	}
	
    @RequestMapping(value = "upload", headers = "content-type=multipart/*", method = RequestMethod.POST)  
    public @ResponseBody String fileUpload(@RequestParam("file") MultipartFile file) { 
    	
        // 判断文件是否为空  
        if (!file.isEmpty()) {  
            try {  

                // 文件保存路径  
                String filePath = System.getProperty("user.dir") + "\\img\\" + file.getOriginalFilename();  
                // 转存文件  
                file.transferTo(new File(filePath));  
                String hash = Fingerprint.getFingerprintPhash(filePath);
            } catch (Exception e) {  
                e.printStackTrace();  
                return "failed";
            }  
        }  

        return "succeed";  
    }  
    
    
    public static void readAllFile(String filePath, String hash, List<File> list) {  
        File f = null;  
        f = new File(filePath);  
        File[] files = f.listFiles(); // 得到f文件夹下面的所有文件。  
        for (File file : files) {  
            if(file.isDirectory()) {  
                //如何当前路劲是文件夹，则循环读取这个文件夹下的所有文件  
                readAllFile(file.getAbsolutePath(), hash, list);  
            } else {  
                list.add(file);  
            }  
        }  
    }  
    
    public static String searchAllFile(String filePath, String hash) {  
        List<File> list = new ArrayList<File>();  

        readAllFile(filePath, hash, list);
        
        for(File file : list) {  
        	String file_path = file.getAbsolutePath();
            if (hash.equals(Fingerprint.getFingerprintPhash(file_path)))
            	return file_path;
        }  
        
        return "null";
    }  
    
    
    @RequestMapping(value = "imgSearch", headers = "content-type=multipart/*", method = RequestMethod.POST)  
    public @ResponseBody String imgSearch(@RequestParam("file") MultipartFile file) { 
    	
    	String result = "";
    	
        // 判断文件是否为空  
        if (!file.isEmpty()) {  
            try {  
            	String nowPath = System.getProperty("user.dir");
                // 文件保存路径  
                String filePath = nowPath + "\\upload\\" + file.getOriginalFilename();  
                // 转存文件  
                file.transferTo(new File(filePath));  
                String hash = Fingerprint.getFingerprintPhash(filePath);
                
                result = searchAllFile(nowPath + "\\img", hash);
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        }  

        return "-> " + result;  
    }  

    /**
     * 通过url请求返回图像的字节流
     */
    @RequestMapping("img/{cateogry}")
    public void getIcon(@PathVariable("cateogry") String cateogry,
                        HttpServletRequest request,
                        HttpServletResponse response) throws IOException {

        if(StringUtils.isEmpty(cateogry)) {
            cateogry = "";
        }

        String fileName = System.getProperty("user.dir") + "\\img\\" + cateogry + ".jpg";

        File file = new File(fileName);


        FileInputStream inputStream = new FileInputStream(file);
        byte[] data = new byte[(int)file.length()];
        int length = inputStream.read(data);
        inputStream.close();

        response.setContentType("image/jpg");

        OutputStream stream = response.getOutputStream();
        stream.write(data);
        stream.flush();
        stream.close();
    }
}
