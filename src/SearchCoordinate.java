import java.util.LinkedList;
import java.util.List;

public class SearchCoordinate extends SimpleCoordinate {
	private int gValue;
	private SearchCoordinate prior;

	public SearchCoordinate(int x, int y, int gValue, SearchCoordinate prior) {
		super(x, y);
		this.gValue = gValue;
		this.prior = prior;
	}
	
	public SearchCoordinate(Coordinate c, int gValue, SearchCoordinate prior) {
		super(c.getX(), c.getY());
		this.gValue = gValue;
		this.prior = prior;
	}
	
	public SearchCoordinate(int x, int y, SearchCoordinate prior) {
		super(x, y);
		this.gValue = prior.getGValue() + 1;
		this.prior = prior;
	}
	
	public SearchCoordinate(SearchCoordinate c) {
		super(c.getX(), c.getY());
		this.gValue = c.getGValue();
		this.prior = c.getPrior();
	}
	
	public int getGValue() {
		return gValue;
	}
	
	public SearchCoordinate getPrior() {
		if (prior == null) {
			return null;
		}
		return new SearchCoordinate(prior);
	}
	
	public List<SearchCoordinate> getNextCoordinates(Maze maze) {
		List<SearchCoordinate> nextList = new LinkedList<SearchCoordinate>();
		
		if (this.getX() - 1 < maze.getWidth() && this.getY() < maze.getHeight() && !maze.getWall(this, Maze.LEFT)) {
			SearchCoordinate left = new SearchCoordinate(this.getX() - 1, this.getY(), this);
			nextList.add(left);
		}
		if (this.getX() + 1 < maze.getWidth() && this.getY() < maze.getHeight() && !maze.getWall(this, Maze.RIGHT)) {
			SearchCoordinate right = new SearchCoordinate(this.getX() + 1, this.getY(), this);
			nextList.add(right);
		}
		if (this.getX() < maze.getWidth() && this.getY() - 1 < maze.getHeight() && !maze.getWall(this, Maze.UP)) {
			SearchCoordinate up = new SearchCoordinate(this.getX(), this.getY() - 1, this);
			nextList.add(up);
		}
		if (this.getX() < maze.getWidth() && this.getY() + 1 < maze.getHeight() && !maze.getWall(this, Maze.DOWN)) {
			SearchCoordinate down = new SearchCoordinate(this.getX(), this.getY() + 1, this);
			nextList.add(down);
		}
		
		return nextList;
		
	}
	
}