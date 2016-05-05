
package imgnosm.web.ui.mvc;

import groovyjarjarasm.asm.tree.IntInsnNode;

import java.io.IOException;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.Valid;

import imgnosm.web.ui.Message;
import imgnosm.web.ui.MessageRepository;
import imgnosm.web.ui.HttpRequest;

import org.springframework.stereotype.Controller;
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
	public String list() {
		return "index";
	}

	@RequestMapping(value = "oauth", params = "code", method = RequestMethod.GET)
	public ModelAndView oauth(@ModelAttribute("code") String code) {


		return new ModelAndView("oauth", "texts", null);
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
	
    @RequestMapping(value = "upload", headers = "content-type=multipart/*", method = RequestMethod.POST)  
    public @ResponseBody String fileUpload(@RequestParam("file") MultipartFile file) { 
    	
    	String hash = "";
    	
        // 判断文件是否为空  
        if (!file.isEmpty()) {  
            try {  

                // 文件保存路径  
                String filePath = System.getProperty("user.dir") + "\\upload\\" + file.getOriginalFilename();  
                // 转存文件  
                file.transferTo(new File(filePath));  
                hash = Fingerprint.getFingerprintPhash(filePath);
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        }  

        return "hash : " + hash;  
    }  

    @RequestMapping(value = "imgSearch", headers = "content-type=multipart/*", method = RequestMethod.POST)  
    public @ResponseBody String imgSearch(@RequestParam("file") MultipartFile file) { 
    	
        // 判断文件是否为空  
        if (!file.isEmpty()) {  
            try {  

                // 文件保存路径  
                String filePath = System.getProperty("user.dir") + "\\upload\\" + file.getOriginalFilename();  
                // 转存文件  
                file.transferTo(new File(filePath));  
                String hash = Fingerprint.getFingerprintPhash(filePath);
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        }  

        return "-> ";  
    }  

    
}
