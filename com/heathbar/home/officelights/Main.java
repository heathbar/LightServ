package com.heathbar.home.officelights;

import java.io.IOException;


public class Main {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) {

		Bookshelf shelf = new Bookshelf();
		
		//channelWalk(shelf);
		hueLoop(shelf);

	    shelf.close();
	    System.out.println("Closed");
	}
	
	public static void channelWalk(Bookshelf shelf){
		
		for (int i=0; i<48; i++){
			shelf.setAll(0);			
			shelf.set(i, 4095);
			shelf.update();
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}
	
	public static void hueLoop(Bookshelf shelf){
		HSV baseColor = new HSV();
		int j = 0;
		while (j < 1200){
			baseColor.hue += 2;
			for (int i=0; i<16; i++){
				
				HSV deltaColor = HSV.copy(baseColor);
				deltaColor.hue += 5 * i;
				
				shelf.set(i, deltaColor);
			}
			shelf.update();
			
			try {
				Thread.sleep(30);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			j++;
			if (j == 360)
				j=0;
		}			
	}
	
	
	public static void fakeMeter(Bookshelf shelf){
		
		for (int k=0; k<100; k++){
			for (int i=10; i >=6; i--){
				shelf.setAll(0);
				for (int j=10; j>=i; j--){
					HSV led = new HSV();
					led.hue = (double)k*2;
					shelf.set(j, led);
				}
				shelf.update();
				
				try {
					Thread.sleep(30);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			for (int i=6; i<10; i++){
				shelf.setAll(0);
				for (int j=10; j>=i; j--){
					HSV led = new HSV();
					led.hue = (double)k*2;
					shelf.set(j, led);
				}
				shelf.update();
				
				try {
					Thread.sleep(60);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
