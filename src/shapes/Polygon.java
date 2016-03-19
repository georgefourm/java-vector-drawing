package shapes;

import java.awt.Graphics;
import java.awt.Point;
import java.util.Collections;
import java.util.LinkedList;

public class Polygon extends Shape {

	private static final long serialVersionUID = 1L;
	private LinkedList<Point> points;
	private LinkedList<Line> edges;
	private boolean closed = false;

	LinkedList<Line> spans = new LinkedList<Line>();
	int minY, maxY;

	public Polygon() {
		points = new LinkedList<Point>();
		edges = new LinkedList<Line>();
	}

	@Override
	public void draw(Graphics g) {
		// add edges only once
		if (!closed || edges.size() < points.size()) {
			edges.clear();
			for (int i = 0; i < points.size() - 1; i++) {
				Line edge = new Line(points.get(i), points.get(i + 1));
				edges.add(edge);
			}
		}

		for (Line edge : edges) {
			edge.setCurrColor(getCurrColor());
			edge.draw(g);
		}
	}

	@Override
	public void fill(Graphics g) {
		if(!closed)
			return;
		//Calculate spans only once
		if(spans.isEmpty())
			findScanlines();
		
		for(Line span:spans){
			span.setCurrColor(getCurrColor());
			span.draw(g);
		}
	}
	
	private void findScanlines(){
		LinkedList<Point> intersections = new LinkedList<Point>();
		//For Every edge
		for (int i = 0; i < edges.size(); i++) {
			// For every scanline
			Line edge = edges.get(i);
			for (int y = minY; y <= maxY; y++) {			
				// Exclude horizontal edges
				if (edge.getShapeStart().y == edge.getShapeEnd().y)
					continue;
				// Find intersections
				Point intersection = edge.intersectsAt(y);
				// Is neither null nor the edge's upper point
				if (intersection != null) {
					if (!intersection.equals(edge.getUpperEnd()))
						intersections.add(intersection);
				}
			}
		}
		// Sort by y
		Collections.sort(intersections, new PointComparator());
		// Draw spans between pairs of them
		for (int i = 0; i < intersections.size() - 1; i+=2) {
			Point start = intersections.get(i);
			Point end = intersections.get(i + 1);
			spans.add(new Line(start, end));
		}
	}

	public boolean isClosed() {
		return closed;
	}

	@Override
	public void setShapeEnd(Point toAdd) {
		points.add(toAdd);
		//keep min and max points for filling
		if (points.size() == 1) {
			minY = toAdd.y;
			maxY = toAdd.y;
			setShapeStart(toAdd);
		} else {
			if (toAdd.y > maxY)
				maxY = toAdd.y;
			if (toAdd.y < minY)
				minY = toAdd.y;
		}
		if (toAdd.distance(getShapeStart()) <= 3 && points.size() > 1) {
			closed = true;
			points.remove(toAdd);
			points.add(getShapeStart());
			super.setShapeEnd(getShapeStart());
		}
	}

	public String toString() {
		if(!closed)
			return points.size()+"-Point Crooked Line"; 
		// last point is start point
		if (points.size() == 4)
			return "Triangle";
		return points.size() - 1 + "-point Polygon";
	}

}
