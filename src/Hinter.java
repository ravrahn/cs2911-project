import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;


public class Hinter {
	
	public static List<SimpleCoordinate> getPath(Maze maze, Coordinate start, Coordinate end) {
		List<SimpleCoordinate> pathList = new LinkedList<SimpleCoordinate>();
		
		Set<SimpleCoordinate> seen = new HashSet<SimpleCoordinate>();
		
		PriorityQueue<SearchCoordinate> queue = new PriorityQueue<SearchCoordinate>(10, new Comparator<SearchCoordinate>() {
			@Override
			public int compare(SearchCoordinate arg0, SearchCoordinate arg1) {
				return 0;
			}
		});
		// we're going from end to start because the end is almost certainly in the corner
//		 but the start can be anywhere.
		queue.offer(new SearchCoordinate(end, 0, null));
		
		SearchCoordinate path = null;
		
		while (!queue.isEmpty()) {
			SearchCoordinate node = queue.poll();
			seen.add(new SimpleCoordinate(node.getX(), node.getY()));
			
			// add the children to the queue
			for (SearchCoordinate child : node.getNextCoordinates(maze)) {
				SimpleCoordinate simpleChild = new SimpleCoordinate(child.getX(), child.getY());
				boolean childSeen = false;
				for (SimpleCoordinate c : seen) {
					if (c.equals(simpleChild)) {
						childSeen = true;
						break;
					}
				}
				if (!childSeen) {
					queue.offer(child);
				}
			}
			
			if (node.getX() == start.getX() && node.getY() == start.getY()) {
				path = node;
				break;
			}
			
		}
		
		while (path != null) {
			pathList.add(0, new SimpleCoordinate(path.getX(), path.getY()));
			path = path.getPrior();
		}
		
		return pathList;
	}
	
}
