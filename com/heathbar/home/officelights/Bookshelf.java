package com.heathbar.home.officelights;

import java.io.InputStream;

public class Bookshelf {

	private PropPlug propeller;
	
	public Bookshelf(){
		propeller = new PropPlug();
		propeller.openSerialPort("/dev/ttyUSB0");
		Reset();
	}
	
	
	public void Reset(){
		System.out.println("Resetting...");
		propeller.reset();
		
		try {
			InputStream is = propeller.getInputStream();
			System.out.print("Waiting");
			while (is.available() < 8){			// Propeller will print "Ready..." when it has finished booting
				System.out.print(".");
				Thread.sleep(200);
			}
			System.out.println();
			
			// clear the input buffer
			propeller.getInputStream().skip(8);
			
		} catch (Exception e){ e.printStackTrace(); }
		
		System.out.println("Started");

	}
	
	public void set(int channel, int value){
		propeller.write((byte) channel);
		propeller.write((byte) (value >> 8));
		propeller.write((byte) value);		
	}
	
	public void set(int channel, HSV color){
		channel *= 3;
		byte[] colorBytes = color.toRGB().toByteArray();
		
		propeller.write((byte) (channel));
		propeller.write(colorBytes[0]);
		propeller.write(colorBytes[1]);

		propeller.write((byte) (channel+1));
		propeller.write(colorBytes[2]);
		propeller.write(colorBytes[3]);
		
		propeller.write((byte) (channel+2));
		propeller.write(colorBytes[4]);
		propeller.write(colorBytes[5]);
		
	}
	
	public void setAll(int value){
		set(0xF0, value);
	}
	
	public void update(){
		propeller.write((byte) 0xFF);
	}
	
	
	public void close(){
		propeller.closeSerialPort();
	}
}
