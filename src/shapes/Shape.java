package shapes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Comparator;

public abstract class Shape implements java.io.Serializable {
	

	private static final long serialVersionUID = 6518403246966301072L;
	private Point shapeStart,shapeEnd;
	private Color currColor=Color.BLACK;
	private boolean filled=false;
	
	public void drawPoint(Graphics g,int x,int y){
		g.drawLine(x,y,x,y);
	}
	
	public abstract void draw(Graphics g);

	public abstract void fill(Graphics g);

	public Point getShapeStart() {
		return shapeStart;
	}

	public void setShapeStart(Point shapeStart) {
		this.shapeStart = shapeStart;
	}

	public Point getShapeEnd() {
		return shapeEnd;
	}

	public void setShapeEnd(Point shapeEnd) {
		this.shapeEnd = shapeEnd;
	}


	public void setCurrColor(Color currColor) {
		this.currColor = currColor;
	}
	
	protected Color getCurrColor() {
		return currColor;
	}

	public boolean isFilled() {
		return filled;
	}

	public void setFilled(boolean filled) {
		this.filled = filled;
	}
	
	protected class PointComparator implements Comparator<Point> {
	    public int compare(final Point a, final Point b) {
	        if (a.y > b.y) {
	            return 1;
	        }
	        else if (a.y < b.y) {
	            return -1;
	        }
	        else {
	        	if (a.x > b.x) {
		            return 1;
		        }
		        else if (a.x < b.x) {
		            return -1;
		        }
		        else {
		        	return 0;
		        }
	        }
	    }
	}
}
