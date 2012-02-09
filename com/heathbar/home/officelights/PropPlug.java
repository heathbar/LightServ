package com.heathbar.home.officelights;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;

public class PropPlug implements Runnable {		
		
	protected boolean dontStopRockin = true;
	protected SerialPort port;
	protected javax.swing.event.EventListenerList listenerList = new javax.swing.event.EventListenerList();
	private int eventId = 0;
	
	public PropPlug() { }
	
	public PropPlug(String name){
		openSerialPort(name);
	}
	
	
	public void openSerialPort(String name){
		try{
	        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(name);
	        if(portIdentifier.isCurrentlyOwned()){
	            System.out.println("Error: Port is currently in use");
	        }else {
		        CommPort comPort = portIdentifier.open(this.getClass().getName(),2000);
		        if(comPort instanceof SerialPort ){
		        	port = (SerialPort) comPort;
		        	port.setSerialPortParams(
		        		    38400,
		        		    SerialPort.DATABITS_8,
		        		    SerialPort.STOPBITS_1,
		        		    SerialPort.PARITY_NONE);
		            System.out.println("Prop Port:"+port);
		        }else{
		        	System.out.println("was not a serial port");
		        }
	        }
        }
		catch(gnu.io.NoSuchPortException Ex) {
			System.out.println("No such HPad port.\nAvailable ports:");
			
			for (Enumeration<CommPortIdentifier> e = CommPortIdentifier.getPortIdentifiers(); e.hasMoreElements();)
				System.out.println(e.nextElement().getName());
		}
		catch(Exception Ex){ Ex.printStackTrace(); }
	}
	
	public void reset(){
		try {
			port.setDTR(true);
			Thread.sleep(100);
			port.setDTR(false);
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	public void write(String msg){
		try{ port.getOutputStream().write(msg.getBytes()); }
		catch (Exception e) { e.printStackTrace(); System.exit(0); }
	}
	
	public void write(byte msg){
		try{ port.getOutputStream().write(msg); } 
		catch (Exception e) { e.printStackTrace(); System.exit(0); }
	}
	
	public void write(byte[] msg){
		try{ 
			for (byte b: msg)
				port.getOutputStream().write(b); 
		} catch (Exception e) { e.printStackTrace(); System.exit(0); }
	}
	
	
	
		
		
    // This methods allows classes to register for feedback from the propeller
    public void addActionListener(ActionListener listener) {
        listenerList.add(ActionListener.class, listener);
    }

    // This methods allows classes to unregister for feedback from the propeller
    public void removeActionListener(ActionListener listener) {
        listenerList.remove(ActionListener.class, listener);
    }

    // This private class is used to fire feedback events
    void fireMyEvent(ActionEvent evt) {
        Object[] listeners = listenerList.getListenerList();
        // Each listener occupies two elements - the first is the listener class
        // and the second is the listener instance
        for (int i=0; i<listeners.length; i+=2) {
            if (listeners[i]==ActionListener.class) {
                ((ActionListener)listeners[i+1]).actionPerformed(evt);
            }
        }
    }

	    
	public InputStream getInputStream(){
		try {
			return port.getInputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	    
		
	public void run(){
		byte input[]; // buffer to hold input data from serial port
			
		System.out.println("Thread Running");
		while(dontStopRockin){
			
			try {
				
				if (port.getInputStream().available() > 0){
					input = new byte[64];
					port.getInputStream().read(input); // this will never read more than input.length bytes - which is good
					
					StringBuffer inputText = new StringBuffer();

					int i=0;
					while ((char)input[i] != '\r' && i < 64)
						inputText.append((char)input[i++]);

					System.out.println(inputText);
					fireMyEvent(new ActionEvent(this, eventId++, String.valueOf(inputText)));
				}			
				
			}catch (Exception e) { e.printStackTrace(); }

		}
		closeSerialPort();
	}
		
	public void stop(){
		dontStopRockin = false;
	}
	
	public void closeSerialPort(){
	    try{
	    	write((byte) 0xF0);		// All channels
	    	write((byte) 0x00);		// 0 - MSB value
	    	write((byte) 0x00);		// 0 - LSB value
	    	write((byte) 0xFF);		// Update
	    	port.close();
    	} catch(Exception E){ E.printStackTrace(); }
    }

}

