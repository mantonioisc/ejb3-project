package examples.domain;

import java.io.Serializable;

public class Player implements Serializable {
	private static final long serialVersionUID = -7430701972340097790L;
	
	private String name;
	private ChipColor chip;
	
	public Player(String name, ChipColor chip) {
		this.name = name;
		this.chip = chip;
	}

	public Player() {
		
	}

	public final String getName() {
		return name;
	}

	public final ChipColor getChip() {
		return chip;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((chip == null) ? 0 : chip.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Player other = (Player) obj;
		if (chip != other.chip)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Player [name=" + name + ", chip=" + chip + "]";
	}
	
}
