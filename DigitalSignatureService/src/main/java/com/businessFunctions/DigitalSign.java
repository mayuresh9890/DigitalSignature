package com.businessFunctions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;

import org.apache.log4j.Logger;

import com.gnostice.pdfone.PdfDocument;
import com.gnostice.pdfone.PdfException;
import com.gnostice.pdfone.PdfFormSignatureField;
import com.gnostice.pdfone.PdfPage;
import com.gnostice.pdfone.PdfPageElement;
import com.gnostice.pdfone.PdfRect;
import com.gnostice.pdfone.PdfSignature;
import com.model.ReturnResponce;
import com.utils.Utility;

public class  DigitalSign{
	private static Logger log = Logger.getLogger(DigitalSign.class);
	static Utility utils = new Utility();
	ReturnResponce res = new ReturnResponce();
	File folder = null;
	public ReturnResponce signDocuments(String unsignPath, String signPath) throws IOException, PdfException {

		String path1 = new String(Base64.getDecoder().decode(unsignPath));              //server directory
		String path2 = new String(Base64.getDecoder().decode(signPath));                //server directory
//		String path1 = "E:/Mayuresh/DigitalSignature/unsign";                           //Local
//		String path2 = "E:/Mayuresh/DigitalSignature/sign";                             //Local
		folder = new File(path1);
		res.setMessage(folder+"");
		File[] listOfFiles = folder.listFiles();
		    for (int i = 0; i < listOfFiles.length; i++) {
		    	String updPath1 = "";
		    	String updPath2 = "";
		      if (listOfFiles[i].isFile()) {
		    	  updPath1 = path1 +""+ listOfFiles[i].getName(); 
		    	  updPath2 = path2 +""+ listOfFiles[i].getName();
		    	  generateDocuments(updPath1, updPath2);
		      } else {
		    	  res.setMessage("No File Found In '"+path1+"'");
		      }
		    }
		    
		return res;
	}
	
	private ReturnResponce generateDocuments(String sourcePath,String destPath){
		PdfDocument doc = new PdfDocument();
		try {
			doc.load(sourcePath);
			
			// Get the last page of the document.
			int lastPageNum = doc.getPageCount();
			PdfPage p = doc.getPage(lastPageNum);
			
			// identify end of page position ie., the last element's endX and endY
			double endPosX = 0;
			double endPosY = 0;
			ArrayList list = (ArrayList) doc.getPageElements(lastPageNum,PdfPageElement.ELEMENT_TYPE_ALL);
			PdfPageElement ele = null;
			for(int i = 0; i < list.size(); i++)
			{
				ele = (PdfPageElement) list.get(i);
				
				if (endPosX < ele.getX())
					endPosX = ele.getX() + ele.getBoundingRect().getWidth();
				
				if (endPosY < ele.getY())
					endPosY = ele.getY() + ele.getBoundingRect().getHeight();
			}
			PdfSignature sig = new PdfSignature(utils.getProperty("keypath"), utils.getProperty("keypassword"), "Test", "Vasai", "test@example.com", lastPageNum);

			// specify the size of the signature field
			double sigFldWidth = 300;
			double sigFldHeight = 120;
			
			// for positioning the signature horizontally center identify
			// the centerX by considering the pageWidth and field width.
			double sigFldX = (p.getCropBoxWidth() - sigFldWidth) / 2;
			
			// leave some extra gap after last element.
			double offsetY = 2;
			double sigFldY = endPosY + offsetY;
			
			// adjust the sigFldHeight if it is larger than the available
			// page height after last element.
			if ((p.getCropBoxHeight() - sigFldY) < sigFldHeight)
			{
				sigFldHeight = p.getCropBoxHeight() - sigFldY;
			}
			
			// create the signature field using the calculated position and size.
			PdfFormSignatureField signatureField = new PdfFormSignatureField(new PdfRect(sigFldX, sigFldY, sigFldWidth, sigFldHeight));
			signatureField.setName("sigfld");
			//signatureField.setBackgroundColor(Color.YELLOW);
			signatureField.fill(sig);
			
			// add the signature field to the page.
			p.addFormField(signatureField);
			
			//doc.setOpenAfterSave(true);
			doc.save(destPath);
			doc.close();
			log.info(folder);
			File f = new File(sourcePath);
			f.delete();
			res.setMessage("Documents ready with DigitalSignature");
		} catch (IOException e) {
			log.info("IOException inside Business" +e);
			res.setMessage("Please check file path");
			e.printStackTrace();
		} catch (PdfException e) {
			log.info("PdfException inside Business" +e);
			res.setMessage("Folder is empty");
			e.printStackTrace();
		}
		return res;
	}
}
