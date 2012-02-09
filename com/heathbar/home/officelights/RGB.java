package com.heathbar.home.officelights;

public class RGB {
	public int r,g,b;
	
	public byte[] toByteArray(){
		byte[] output = new byte[6];
		
		output[0] = (byte) (r >> 8);
		output[1] = (byte) r;

		output[2] = (byte) (g >> 8);
		output[3] = (byte) g;
		
		output[4] = (byte) (b >> 8);
		output[5] = (byte) b;
		
		return output;
	}
}
