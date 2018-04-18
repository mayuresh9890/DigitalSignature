package com.controller;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.businessFunctions.DigitalSign;
import com.gnostice.pdfone.PdfException;
import com.model.ReturnResponce;
import com.utils.Utility;


@Path("/DSservice")
public class MainController {
	private static Logger log = Logger.getLogger(MainController.class);
	
	Utility utlis = new Utility();
	DigitalSign sign = new DigitalSign();
	
	public MainController(){
		utlis.loadLogPropertFile();
	}
	
	 @GET
	 @Path("/generate/{path1}/{path2}")
	 @Produces(MediaType.APPLICATION_JSON)  
	 public ReturnResponce getIt(@PathParam("path1") String unsignPath, @PathParam("path2") String signPath){
		 ReturnResponce returnVal = null;
		 log.info(unsignPath);
		 log.info(signPath);
	     try {
	    	 returnVal = sign.signDocuments(unsignPath,signPath);
		} catch (IOException e) {
			log.error("IOException:- " +e);
			e.printStackTrace();
		} catch (PdfException e) {
			log.error("PdfException:- " +e);
			e.printStackTrace();
		}
		return returnVal;
	 }

}
