package swe.PotatoStreamer;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
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




@Controller
public class PotatoController
{	
	User newUser = new User();
	User existingUser = new User();
	private String result = "";
	
    @RequestMapping(value = "/")
    public String testing(Model model) throws InterruptedException
    {	
    	//*/
       	try 
       	{
    		DBInteract.makeConn();
        	DBInteract.addUser("Michael", "password1");
        	DBInteract.getUserArray();
    	} catch (SQLException e) 
       	{
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    	
    	if(DBInteract.authenticate("Michael", "password1"))
    		System.out.println("Authentication successful!");
    	else
    		System.out.println("Unable to authenticate.");
    	
    	//*/
    	String mp3Path = "/Users/Michael/Horizon.mp3";
    	AudioFile afile = new AudioFile(mp3Path);
    	afile.play();
    	Thread.sleep(5000);
    	afile.stop();
    	Thread.sleep(2000);
    	afile.play();
    	
    	
    	//*/
    	return homeRender(model);
    }
	
    @GetMapping("/home")
    public String homeRender(Model model) 
    {
       	try 
       	{
    		DBInteract.makeConn();
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
    	
    	if(DBInteract.authenticate(existingUser.getId(), existingUser.getPwd())) {
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
    	if(DBInteract.addUser(newUser.getId(), newUser.getPwd())) {
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
    	String mp3Path = "C:\\Users\\Horizon.mp3";
    	AudioFile afile = new AudioFile(mp3Path);
    	ArrayList<AudioFile> audioFiles = new ArrayList<AudioFile>();
    	audioFiles.add(afile);
    	model.addAttribute("audioFiles", audioFiles);
    	
    	return "library";
    }//
    
    // For returning to library after swapping pages
    @PostMapping("/library")
    public String libraryRedirect(@ModelAttribute User existingUser, Model model) throws SQLException
    {
    	//model.addAttribute(DBInteract.getUserArray());
    	
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
