package examples.domain;

import java.io.Serializable;

/**
 * Class that represents a ladder in Snakes & ladders game. Immutable
 * once build. Serializable in order to be used in remote EJB. 
 * {@link #hashCode()} and {@link #equals(Object)} overridden to avoid
 * duplicates in the board game.
 */
public class LadderElement implements Serializable {
	private static final long serialVersionUID = 1228691934275071534L;
	
	private int bottom;
	private int top;
	
	public LadderElement(int bottom, int top) {
		super();
		this.bottom = bottom;
		this.top = top;
	}
	
	public int getBottom() {
		return bottom;
	}
	
	public int getTop() {
		return top;
	}

	@Override
	public String toString() {
		return "LadderElement [bottom=" + bottom + ", top=" + top + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + bottom;
		result = prime * result + top;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LadderElement other = (LadderElement) obj;
		if (bottom != other.bottom)
			return false;
		if (top != other.top)
			return false;
		return true;
	}
	
}
