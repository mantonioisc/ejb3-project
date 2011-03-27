package examples.domain;

import java.io.Serializable;

/**
 * Class that represents a snake in Snakes & ladders game. Immutable
 * once build. Serializable in order to be used in remote EJB.
 * {@link #hashCode()} and {@link #equals(Object)} overridden to avoid
 * duplicates in the board game. 
 */
public class SnakeElement implements Serializable {
	
	private static final long serialVersionUID = -4323110112500598136L;
	
	private int tail;
	private int head;
	
	public SnakeElement(int tail, int head) {
		super();
		this.tail = tail;
		this.head = head;
	}

	public int getTail() {
		return tail;
	}

	public int getHead() {
		return head;
	}

	@Override
	public String toString() {
		return "SnakeElement [tail=" + tail + ", head=" + head + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + head;
		result = prime * result + tail;
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
		SnakeElement other = (SnakeElement) obj;
		if (head != other.head)
			return false;
		if (tail != other.tail)
			return false;
		return true;
	}
	
}
