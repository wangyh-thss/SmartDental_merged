package com.edu.thss.smartdental.model.general;

import java.util.ArrayList;

import android.R.integer;
import android.graphics.Bitmap;

import android.graphics.Color;

public class DrawNotateClass {
	private ArrayList<Integer> num_of_point;
	private ArrayList<ArrayList<Integer>> pointX;
	private ArrayList<ArrayList<Integer>> pointY;
	
	public DrawNotateClass(ArrayList<Integer> n, ArrayList<ArrayList<Integer>> px, ArrayList<ArrayList<Integer>> py) {
		// TODO Auto-generated constructor stub
		this.num_of_point = n;
		this.pointX = px;
		this.pointY = py;
	}
	
	public void drawPoint(int x, int y, int thickness, Bitmap tmp, int color){
    	if(thickness == 0)
    		return;
    	int width = tmp.getWidth();
    	int height = tmp.getHeight();
    	//thickness length  ii+jj<?
    	//    1       1        2
    	//    2       3        3
    	//    3       5        4
    	//    4       7        5
    	//    5       9        6
    	for(int ii = -1*thickness+1; ii < thickness; ii++){
			for(int jj = -1*thickness+1; jj < thickness; jj++){
				if(x+ii >= width || x+ii < 0)
					continue;
				else if(y+jj >= height || y+jj < 0)
					continue;

				if(Math.abs(ii) + Math.abs(jj) < thickness+1)
					tmp.setPixel(x+ii, y+jj, color);
			}
		}
    }
    
    public void drawSegment(int x1, int y1, int x2, int y2, int thickness, Bitmap tmp, int color){
    	if(x1 == x2){
    		if(y1 > y2){
    			int t = x1;  x1 = x2;	x2 = t;
    			t = y1;		 y1 = y2;	y2 = t;	    			
    		}
    		for(int j = y1; j <= y2; j++){
    			drawPoint(x1, j, thickness, tmp, color);
    		}
    		return;
    	}
    	if(y1 == y2){
    		if(x1 > x2){
    			int t = x1;  x1 = x2;	x2 = t;
    			t = y1;		 y1 = y2;	y2 = t;	    			
    		}
    		for(int i = x1; i <= x2; i++){
    			drawPoint(i, y1, thickness, tmp, color);
    		}
    		return;
    	}
    	if(Math.abs(x1-x2) > Math.abs(y1-y2)){
    		if(x1 > x2){
    			int t = x1;  x1 = x2;	x2 = t;
    			t = y1;		 y1 = y2;	y2 = t;	    			
    		}
    		for(int i = x1; i <= x2; i++){
    			int drawedX = i;
    			double k = ((double)(y2-y1))/((double)(x2-x1));
    			int drawedY = (int)(k*(i-x1)+y1);
    			drawPoint(drawedX, drawedY, thickness, tmp, color);
    		}
    		return;
    	}
    	else{
    		if(y1 > y2){
    			int t = x1;  x1 = x2;	x2 = t;
    			t = y1;		y1 = y2;	y2 = t;	    			
    		}
    		for(int j = y1; j <= y2; j++){
    			int drawedY = j;
    			double k = ((double)(x2-x1))/((double)(y2-y1));
    			int drawedX = (int)(k*(j-y1)+x1);
    			drawPoint(drawedX, drawedY, thickness, tmp, color);
    		}
    		return;
    	}
    }
    
    public void drawNotate(int index, int thickness, Bitmap tmp, int color)
    {
    	for(int j = 1; j < num_of_point.get(index); j++){
	    	int x1 = pointX.get(index).get(j-1);
			int y1 = pointY.get(index).get(j-1);
			int x2 = pointX.get(index).get(j);
			int y2 = pointY.get(index).get(j);
			drawSegment(x1, y1, x2, y2, thickness, tmp, color);
		}
    }
}
