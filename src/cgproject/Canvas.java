package cgproject;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import shapes.Shape;

public class Canvas extends JPanel {
	private static final long serialVersionUID = -4506044406261200469L;
	public Class<?> shapeType;
	public Shape currShape;
	private boolean polygonDrawingMode=false;
	private Color drawingColor = Color.BLACK;
	
	public DefaultListModel<Shape> listmodel=new DefaultListModel<Shape>();
	
 	public Canvas(Color bg){
 		this.setBackground(bg);
 		this.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
            	try {
            		//Instantiate new shape
            		if(!polygonDrawingMode){
            			currShape=(Shape) shapeType.newInstance();
            		}	
					currShape.setCurrColor(drawingColor);
				} catch (InstantiationException e1) {
					e1.printStackTrace();
				} catch (IllegalAccessException e1) {
					e1.printStackTrace();
				} catch (NullPointerException e3){
					JOptionPane.showMessageDialog(getParent(), "No Shape Selected!");
				}
            	if(currShape!=null&&!polygonDrawingMode){
            		currShape.setShapeStart(e.getPoint());
            	}
            	
            }
            
            public void mouseClicked(MouseEvent e){            	
            	if(currShape instanceof shapes.Polygon){
            		currShape.setShapeEnd(e.getPoint());
            		if(((shapes.Polygon) currShape).isClosed()){
            			listmodel.addElement(currShape);
                		currShape=new shapes.Polygon();
            		}
            		repaint();
            	}	
            }
            
            public void mouseReleased(MouseEvent e){
            	if(currShape!=null&&!polygonDrawingMode){
            		listmodel.addElement(currShape);
            		currShape=null;
                	repaint();
            	}
            }
        });
 		
 		this.addMouseMotionListener(new MouseAdapter() {           
            public void mouseDragged(MouseEvent e){
            	if(currShape!=null&&!polygonDrawingMode){
            		 currShape.setShapeEnd(e.getPoint());
                     repaint();
            	}
            }
        });

 	}
 	/**
 	 * Sets the class of the shape to draw
 	 * If the class is Polygon, also starts 
 	 * polygon drawing mode
 	 * @param toDraw The class of the shape to draw
 	 */
 	public void setCurrShape(Class<?> toDraw){
 		if(toDraw==shapes.Polygon.class){
 			polygonDrawingMode=true;
 			currShape=new shapes.Polygon();
 		}else{
 			polygonDrawingMode=false;
 		}
 		shapeType=toDraw;
 	}
 	
 	public void changeShapePoints(Point newStart,Point newEnd,int shapeIndex){
 		Shape shaperef=listmodel.elementAt(shapeIndex);
 		if(shaperef instanceof shapes.Polygon)
 			return;
 		shaperef.setShapeStart(newStart);
 		shaperef.setShapeEnd(newEnd);
 	}
 	/**
 	 * Clears the list of shapes and repaints the component
 	 */
 	public void clear(){
 		listmodel.clear();
 		repaint();
 	}
 	
 	/**
 	 * Moves a shape up or down the List, and repaints the canvas
 	 * @param index The Shape index
 	 * @param moveUp Moves the shape up if true
 	 * @return The new index of the Shape
 	 */
 	public int moveShape(int index,boolean moveUp){
 		Shape temp= listmodel.get(index);
 		int nextInd=moveUp?index+1:index-1;
 		//Wrap around if reached end
 		nextInd=nextInd%listmodel.getSize();
 		//Swap Shapes
 		listmodel.setElementAt(listmodel.get(nextInd), index);
 		listmodel.setElementAt(temp, nextInd);
 		repaint();
 		return nextInd;
 	}
 	
 	public void setDrawingColor(Color toDraw){
 		drawingColor=toDraw;
 	}
 	
 	/**
 	 * Removes active drawing shape and repaints the component.
 	 * If the active drawing shape is a polygon and it is not closed,
 	 * the resulting line is added to the list
 	 */
 	public void cancelDrawing(){
 		if(currShape instanceof shapes.Polygon){
 			//Only when open line
 			if(!((shapes.Polygon) currShape).isClosed()){
    			listmodel.addElement(currShape);
    		}
 		}
 		currShape=null;
 		shapeType=null;
 		repaint();
 	}
 	
    public void paint(Graphics g) {
    	super.paint(g);
    	//Draw other Shapes
        for(int i=0;i<listmodel.getSize();i++){
        	listmodel.elementAt(i).draw(g);
        	if(listmodel.elementAt(i).isFilled()){
        		listmodel.elementAt(i).fill(g);
        	}
        }
        
      //Draw current Shape preview
    	if(currShape!=null){
    		currShape.draw(g);
    	}
    }
    
  /**
   * Saves the current state of the Canvas in given file,
   * serializing all shapes and the canvas' color and size
   * @param filename the File to save
   */
    public void saveState(String filename){
    	ObjectOutputStream out = null;
		try {
			out = new ObjectOutputStream(new FileOutputStream(filename));
			out.writeObject(this.getBackground());
			out.writeObject(this.getSize());
	    	for(int i=0;i<listmodel.getSize();i++){
				out.writeObject(listmodel.elementAt(i));
	        }
	    	JOptionPane.showMessageDialog(getParent(), "State Saved");   
	    	out.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

    }
    
    /**
     * Loads the current state of the Canvas in given file,
     * de-serializing all shapes and the canvas' color and size
     * @param filename the File to save
     */
    public void loadState(String filename){
    	ObjectInputStream in = null;
		try {
			in = new ObjectInputStream(new FileInputStream(filename));
		} catch (FileNotFoundException e1) {
			JOptionPane.showMessageDialog(getParent(), "No saved State found!");
			return;
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		listmodel.clear();

		try {
    		Color storedbg=(Color)in.readObject();
    		this.setBackground(storedbg);
    		this.setSize((Dimension)in.readObject());
    		while(listmodel.size()>-1){
    			listmodel.addElement((Shape)in.readObject());
    		}
    		in.close();  		
    	}catch(EOFException e){
    		repaint();
    		revalidate(); 		
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
