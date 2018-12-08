package npcg;

import java.util.Random;

public class _die {
	
	public Random rand;
	
	public _die() {
		this.rand = new Random();
	}
	
	public int nroll(int n, int type) {
		if (n == 0) {
			return 0;
		}
		else {
			return roll(type) + nroll(n-1, type);
		}
	}
	
	public int roll(int type) {
		return rand.nextInt(type) + 1;
	}
	
}
