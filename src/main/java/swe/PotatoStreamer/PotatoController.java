package swe.PotatoStreamer;

import java.sql.SQLException;
import java.util.Map;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.slf4j.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;




@SuppressWarnings("unused")
@Controller
public class PotatoController
{	
	User newUser = new User();
	User existingUser = new User();
	DBInteract conn = null;
	private String result = "";
	Logger logger = LoggerFactory.getLogger(PotatoController.class);
    @RequestMapping(value = "/")
    public String testing(Model model)
    {	
    	//*/
       	try 
       	{
    		conn.makeConn();
//    		conn.dbSetup();
//        	conn.addUser("Michael", "password1");
//        	conn.printAllUsers();
    	} catch (SQLException e) 
       	{
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    	
    	if(conn.authenticate("Michael", "password1"))
    		System.out.println("Authentication successful!");
    	else
    		System.out.println("Unable to authenticate.");
    	
    	/*/
    	String mp3Path = "/Users/Michael/Horizon.mp3";
    	AudioFile afile = new AudioFile(mp3Path);
    	afile.play();
    	//*/
    	return homeRender(model);
    }
	
    @GetMapping("/home")
    public String homeRender(Model model) 
    {
       	try 
       	{
    		conn.makeConn();
    	} catch (SQLException e) 
       	{
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
       	
       	model.addAttribute("existingUser", existingUser);
       	
    	return "home";
    }
    
    @PostMapping("/home")
    public String homeLogic(@ModelAttribute("existingUser") User existingUser, Model model) throws SQLException {
    	
    	if(conn.authenticate(existingUser.getId(), existingUser.getPwd())) {
    		return libraryRender(existingUser, model);
    	}
    	result = "Invalid login credentials.";
    	model.addAttribute("result", result);
    	return homeRender(model);
    }
    
    
    @GetMapping("/register")
    public String registerRender(Model model)
    {
    	model.addAttribute("newUser", newUser);
    	
    	return "register";
    }
    
    @PostMapping("/register")
    public String registerLogic(@ModelAttribute("newUser") User newUser, Model model) throws SQLException
    {
    	if(conn.addUser(newUser.getId(), newUser.getPwd())) {
    		result = "Registration success! Return to login.";
    		model.addAttribute("result", result);
    	} else {
    		result = "Registration failed. Username already in use.";
    		model.addAttribute("result", result);
    	}
    	
    	return "register";
    }
    
   // If existing user reaching library for first time
    @GetMapping("/library")
    public String libraryRender(@ModelAttribute User existingUser, Model model) throws SQLException
    {
    	return "library";
    }
    
    // For returning to library after swapping pages
    @RequestMapping(value = "/library")
    public String libraryRedirect(@ModelAttribute User existingUser, Model model)
    {
    	return "library";
    }
    
    @RequestMapping(value = "/upload")
    public String uploadRender(Model model)
    {
    	AudioFile newFile = new AudioFile("");
    	
    	model.addAttribute("myFile", newFile);
    	
    	return "upload";
    }
}
