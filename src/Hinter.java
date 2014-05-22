import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;


public class Hinter {
	
	public static List<SimpleCoordinate> getPath(Maze maze, final Coordinate start, Coordinate end) {
		List<SimpleCoordinate> pathList = new LinkedList<SimpleCoordinate>();
		
		Set<SimpleCoordinate> seen = new HashSet<SimpleCoordinate>();
		
		PriorityQueue<SearchCoordinate> queue = new PriorityQueue<SearchCoordinate>(10, new Comparator<SearchCoordinate>() {
			@Override
			public int compare(SearchCoordinate arg0, SearchCoordinate arg1) {
				// Manhattan distance
				int hValue0 = Math.abs(arg0.getX() - start.getX()) + Math.abs(arg0.getY() - start.getY());
				int hValue1 = Math.abs(arg1.getX() - start.getX()) + Math.abs(arg1.getY() - start.getY());
				
				// it's an a* so we do this
				// well, it's an a* because we do this
				int fValue0 = arg0.getGValue() + hValue0;
				int fValue1 = arg1.getGValue() + hValue1;
				
				return fValue0 - fValue1;
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
	
	/**
	 * Find the best move for an <code>entity</code> in a given <code>maze</code>
	 * that is trying to intercept a <code>target</code>
	 * taking an optimal path to the <code>targetGoal</code>
	 * @return The direction to move or -1 if no move is found
	 */
	public static Coordinate bestMove(Coordinate entity, Coordinate target, Coordinate targetGoal, Maze maze) {
		
		// guess the target's path. We don't know they're moving
		// optimally, but we think they are.
		List<SimpleCoordinate> targetPath = Hinter.getPath(maze, target, targetGoal);
		
		int bestDistance = maze.getHeight() * maze.getWidth(); // can't be longer than that
		SimpleCoordinate bestMove = null;
		
		for (SimpleCoordinate interceptPoint : targetPath) {
			List<SimpleCoordinate> entityPath = Hinter.getPath(maze, entity, interceptPoint);
			int entityDistance = entityPath.size() - 1; // path includes current location
			
			if (entityDistance < bestDistance) {
				bestDistance = entityDistance;
				if (entityPath.size() > 2) {
					bestMove = entityPath.get(entityPath.size() - 2);
				} else {
					// we're already on their best path
					// head towards them
					List<SimpleCoordinate> toTarget = Hinter.getPath(maze, entity, target);
					if (toTarget.size() > 2) {
						return toTarget.get(toTarget.size() - 2);
					} else {
						return entity;
					}
				}
			}
		}
		
		if (bestMove != null) {
			return bestMove;
		}
		
		// we didn't find anything, so just head towards the target's goal
		
		List<SimpleCoordinate> entityPath = Hinter.getPath(maze, entity, targetGoal);
		if (entityPath.size() < 2) {
			return entity;
		}
		SimpleCoordinate move = entityPath.get(entityPath.size() - 2);
		return move;		
	}
	
}
