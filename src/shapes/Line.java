package shapes;

import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JOptionPane;

import cgproject.AppWindow;

public class Line extends Shape {

	private static final long serialVersionUID = -8329662510338977129L;

	public Line(Point start, Point end){
		setShapeStart(start);
		setShapeEnd(end);
	}
	
	public Line(){
		setShapeStart(new Point(0,0));
		setShapeEnd(new Point(0,0));
	}


	/**
	 * Draws a line using Bresenham's Algorithm
	 */
	@Override
	public void draw(Graphics g) {		
		int x1,y1,x2,y2;
		
			x1=getShapeStart().x;
			y1=getShapeStart().y;
		
			x2=getShapeEnd().x;
			y2=getShapeEnd().y;
			
		if(x1==x2&&y1==y2){
			return;
		}
		
		// delta of exact value and rounded value of the dependant variable
        int d = 0;
        int dy = Math.abs(y2 - y1);
        int dx = Math.abs(x2 - x1);
 
        //Integer division by 2 using left shift
        int dy2 = (dy << 1);
        int dx2 = (dx << 1);
 
        int ix = x1 < x2 ? 1 : -1; // increment direction
        int iy = y1 < y2 ? 1 : -1;
 
        if (dy <= dx) {
            do{
            	drawPoint(g,x1, y1);
                x1 += ix;
                d += dy2;
                if (d > dx) {
                    y1 += iy;
                    d -= dx2;
                }
            }while(x1!=x2);
        } else {
           do{
                drawPoint(g,x1, y1);
                y1 += iy;
                d += dx2;
                if (d > dy) {
                    x1 += ix;
                    d -= dy2;
                }
            }while(y1!=y2);
        }

	}
	
	/**
	 * Determines where a scanline intersects this line
	 * @param y The scanline's y coordinate
	 * @return The Point of intersection if it exists, null otherwise
	 */
	
	public Point intersectsAt(int y){
		int x1,y1,x2,y2;
		
		x1=getShapeStart().x;
		y1=getShapeStart().y;
	
		x2=getShapeEnd().x;
		y2=getShapeEnd().y;
		
		if(x1==x2&&y1==y2){
			return null;
		}
		
		if(y1==y)
			return getShapeStart();
		if(y2==y)
			return getShapeEnd();
	
		// delta of exact value and rounded value of the dependant variable
	    int d = 0;
	    int dy = Math.abs(y2 - y1);
	    int dx = Math.abs(x2 - x1);
	
	    //Integer division by 2 using left shift
	    int dy2 = (dy << 1);
	    int dx2 = (dx << 1);
	
	    int ix = x1 < x2 ? 1 : -1; // increment direction
	    int iy = y1 < y2 ? 1 : -1;
	
	    if (dy <= dx) {
	        do{
	        	if(y1==y)
	        		return new Point(x1,y1);
	            x1 += ix;
	            d += dy2;
	            if (d > dx) {
	                y1 += iy;
	                d -= dx2;
	            }
	        }while(x1!=x2);
	    } else {
	       do{
	    	   if(y1==y)
	        		return new Point(x1,y1);
	            y1 += iy;
	            d += dx2;
	            if (d > dy) {
	                x1 += ix;
	                d -= dy2;
	            }
	        }while(y1!=y2);
	    }
	    
	    return null;
	}
	
	//Overridden to display properly in shapelist
	@Override
	public String toString(){
		return "Line: ("+getShapeStart().x+","+getShapeStart().y+")"
				+ "-("+getShapeEnd().x+","+getShapeEnd().y+")";
	}

	@Override
	public void fill(Graphics g) {
		//Line can't be filled
	}
	
	public Point getUpperEnd(){
		if(getShapeEnd().getY()<getShapeStart().getY()){
			return getShapeEnd();
		}else{
			return getShapeStart();
		}
	}
	
	@Override
	public void setFilled(boolean filled){
		JOptionPane.showMessageDialog(AppWindow.getFrames()[0], "Can't Fill Line!");
	}

}
