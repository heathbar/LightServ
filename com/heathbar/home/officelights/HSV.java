package com.heathbar.home.officelights;

public class HSV {

	public double hue, saturation, value;
	private int max_range = 4095; // Defines the maximum brightness for color leveling
	
	public HSV(){
		hue = 0.0;
		saturation = value = 1.0;
	}
	
	public HSV(double h, double s, double v){
		hue = h;
		saturation = s;
		value = v;
	}
	
	public static HSV copy(HSV source){
		return new HSV(source.hue, source.saturation, source.value);
	}
	public RGB toRGB(){

		int i;
		double f, p, q, t, hTemp;

		double h = hue;
		double s = saturation;
		double v = value;
		
		RGB rgb = new RGB();

		double r, g, b;
		if (v <= 0.0) {
			rgb.r = rgb.g = rgb.b = 0;
			return rgb;
		}
		
		//Handle Clipping for Color Conversions
		if (v > 1.0) {
			v = 1.0;
		}
		if (s < 0.0) {
			s = 0.0;
		} else if (s > 1.0) {
			s = 1.0;
		}
		while (h < 0.0) {
			h += 360.0;
		}
		while (h >= 360.0) {
			h -= 360.0;
		}	
		
		if (s == 0.0) {
				rgb.r = rgb.g = rgb.b = (int) (v / 3.0 * max_range);
			return rgb;
		}
		
		hTemp = h / 60.0;
		i = (int) Math.floor(hTemp);        // which sector
		f = hTemp - i;                      // how far through sector
		p = v * ( 1 - s );
		q = v * ( 1 - s * f );
		t = v * ( 1 - s * ( 1 - f ) );

		switch (i) {
			case 0: r = v; g = t; b = p; break;
			case 1: r = q; g = v; b = p; break;
			case 2: r = p; g = v; b = t; break;
			case 3: r = p; g = q; b = v; break;
			case 4: r = t; g = p; b = v; break;
			case 5: r = v; g = p; b = q; break;
			default: r=0 ; g=0 ; b=0 ; break ; // Should never occur
		}
		
		//set r,g,b on the range of max
		double sum = r + g + b;
		rgb.r = (int) ((r * (r / sum)) * max_range);
		rgb.g = (int) ((g * (g / sum)) * max_range);
		rgb.b = (int) ((b * (b / sum)) * max_range);
	  
		return rgb;
	}

}
