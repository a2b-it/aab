package ma.cam.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/trace")
public class TraceControler {

	@GetMapping("/applog")
	public String getAllTrace (){
		String textTrace="";
		Scanner scanner;
		try {
			scanner = new Scanner(new File("./logs/app.log"));
			while (scanner.hasNextLine()) {
				   String line = scanner.nextLine();
				   textTrace+="<br />"+line;
				}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		
	   return textTrace;
	}
	
	@GetMapping("/errorlog")
	public String getAllErrorTrace (){
		String textTrace="";
		Scanner scanner;
		try {
			scanner = new Scanner(new File("./logs/app-error.log"));
			while (scanner.hasNextLine()) {
				   String line = scanner.nextLine();
				   textTrace+="<br />"+line;
				}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		
	   return textTrace;
	}
	
	@GetMapping("/infolog")
	public String getAllInfoTrace (){
		String textTrace="";
		Scanner scanner;
		try {
			scanner = new Scanner(new File("./logs/app-info.log"));
			while (scanner.hasNextLine()) {
				   String line = scanner.nextLine();
				   textTrace+="<br />"+line;
				}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		
	   return textTrace;
	}
	
	@GetMapping("/debuglog")
	public String getAllDebugTrace (){
		String textTrace="";
		Scanner scanner;
		try {
			scanner = new Scanner(new File("./logs/app-debug.log"));
			while (scanner.hasNextLine()) {
				   String line = scanner.nextLine();
				   textTrace+="<br />"+line;
				}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		
	   return textTrace;
	}
}
