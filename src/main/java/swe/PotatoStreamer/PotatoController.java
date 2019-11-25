package swe.PotatoStreamer;

import java.sql.SQLException;
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
	
    @RequestMapping(value = "/")
    public String setupDataBase(Model model)
    {	
    	//*/
       	try 
       	{
    		DBInteract.makeConn();
        	DBInteract.addUser("Michael", "password1");
        	DBInteract.printAllUsers();
    	} catch (SQLException e) 
       	{
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    	
    	if(DBInteract.authenticate("Michael", "password1"))
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
    		return "home";
    	}
    	
    	return "home";
    }
    
    
    @GetMapping("/register")
    public String registerRender(Model model)
    {
    	model.addAttribute("newUser", newUser);
    	// check data, should be null
    	System.out.println("new: " + newUser.getId() + " || " + newUser.getPwd());
    	
    	return "register";
    }
    
    @PostMapping("/register")
    public String registerLogic(@ModelAttribute("newUser") User newUser, Model model) throws SQLException
    {
    	// check data, should be good data things
    	System.out.println("new: " + newUser.getId() + " || " + newUser.getPwd());
    	DBInteract.addUser(newUser.getId(), newUser.getPwd());
    	
    	return homeLogic(newUser, model);
    }
    
   // If existing user reaching library for first time
    @GetMapping("/library")
    public String libraryRender(@ModelAttribute User existingUser, Model model) throws SQLException
    {
    	System.out.println("check: " + existingUser.getId() + " || " + existingUser.getPwd());
    	if(DBInteract.authenticate(existingUser.getId(), existingUser.getPwd()) == false) {
    		// incorrect info error display
    		return homeRender(model);
    	};
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
