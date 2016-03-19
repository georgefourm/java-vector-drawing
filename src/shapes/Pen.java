package shapes;

import java.awt.Graphics;
import java.awt.Point;
import java.util.LinkedList;

import javax.swing.JOptionPane;

import cgproject.AppWindow;

public class Pen extends Shape {

	private static final long serialVersionUID = 7296214068118531097L;
	private LinkedList<Point> points;
	private LinkedList<Line> lines;
	private boolean finished=false;
	public Pen(){
		points=new LinkedList<Point>();
		lines=new LinkedList<Line>();
	}
	
	@Override
	public void draw(Graphics g) {
		//Instantiate lines only once
		if(!finished){
			for(int i=0;i<points.size()-1;i++){
				lines.add(new Line(points.get(i),points.get(i+1)));
			}
		}
		
		for(Line line:lines){
			line.setCurrColor(getCurrColor());
			line.draw(g);
		}
	}

	@Override
	public void fill(Graphics g) {
		// Can't fill line
	}
	
	
	@Override
	public void setShapeEnd(Point toAdd){
		points.add(toAdd);
	}
	
	@Override
	public void setFilled(boolean filled){
		JOptionPane.showMessageDialog(AppWindow.getFrames()[0],"Can't Fill line!");
	}
	
	public String toString(){
		//Only called when added to the list
		finished=true;
		return points.size()+"-point Free Line";
	}

}
