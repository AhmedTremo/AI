package code.mission;

public class cellhealthtuple {
	//A class to define an IMF member with it's current health and position on the map.
	Cell imf_position;
	int imf_health;

	public cellhealthtuple(Cell imf_position, int imf_health) {
		this.imf_health = imf_health;
		this.imf_position = imf_position;
	}

	
	//Setters and getters for the position initially and the varied health throughout the series of actions.
	public Cell getImf_position() {
		return imf_position;
	}

	public void setImf_position(Cell imf_position) {
		this.imf_position = imf_position;
	}

	public int getImf_health() {
		return imf_health;
	}

	public void setImf_health(int imf_health) {
		this.imf_health = imf_health;
	}

}
