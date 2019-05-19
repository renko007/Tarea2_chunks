import java.io.DataInputStream;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.Scanner;



public class Serverchunks {

	public static void main(String args[]) throws IOException{


	    InetAddress address=InetAddress.getByName("10.2.199.1");
	    Socket s1=null;
	    DataInputStream  is = null;
	    DataOutputStream os=null;

	    try {
	        s1=new Socket(address, 4445); // You can use static final constant PORT_NUM
	        is = new DataInputStream(s1.getInputStream()); 
	        os = new DataOutputStream(s1.getOutputStream()); 
	    }
	    catch (IOException e){
	        e.printStackTrace();
	        System.err.print("IO Exception");
	    }

	    System.out.println("VM Address : "+address);
	    

	    try{
	    	
	    	is = new DataInputStream(s1.getInputStream());
	        os = new DataOutputStream(s1.getOutputStream());
	        while(true){
	        	String operation="aaaa";
	        	try{
	        		operation=is.readUTF();
	        	}
	        	catch (Exception exc){
	        		
	        	}
	            if(operation.contains("put")){
	            	InputStream in = null;
	                OutputStream out = null;
	            	try {
	                    in = s1.getInputStream();
	                } catch (IOException ex) {
	                    System.out.println("Can't get socket input stream. ");
	                }
	            	String f=operation.split(" ")[1];
	                try {
	                    out = new FileOutputStream(f);
	                } catch (FileNotFoundException ex) {
	                    System.out.println("File not found. ");
	                    continue;
	                }

	                byte[] bytes = new byte[64*1024];
	                in.read(bytes, 0, 64*1024);
	                out.write(bytes, 0,64*1024);
	                out.flush();
	                out.close();
	                os.writeUTF("Downloaded "+f);
	                System.out.println("Downloaded "+f);
	            }
		//COMANDO put
	            else if(operation.contains("get")) {
	            	String[] parts = operation.split(" ");
	            	String name=parts[1];
	                byte [] bytes =new byte [16*1024];
	                InputStream in = new FileInputStream(name);
	                in.read(bytes,0,64*1024);
	                os.write(bytes, 0, 64*1024);
	                System.out.println("Subida realizada");
	                in.close();
	                System.out.println("Sended "+name);
	            }
		//comando delete
	            else if(operation.contains("delete")) {
	            	System.out.println(is.readUTF());
	            	String del=operation.split(" ")[1];
	            	File file=new File(del);
	            	file.delete();
	            	System.out.println("Deleted "+del);
	            }
	            else if (operation.contains("ls")) {
	            	String del=operation.split(" ")[1];
	            	File f = new File(del);
	            	if(f.exists()) { 
	            	   os.writeUTF("existe"); 
	            	}
	            	else{
	            		os.writeUTF("no existe"); 
	            	}
	            }
	        }
	    }
	    catch(IOException e){
	        e.printStackTrace();
	        System.out.println("Socket read Error");
	    }
	    finally{
	    	is.close();
	    	os.close();
	    	s1.close();
	    	System.out.println("Connection Closed");
	    }

	}
}
