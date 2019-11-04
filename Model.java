import javafx.scene.image.Image;
public class Model {
	

		int xloc = 0;
	    int yloc = 0;
	    double xIncr = 8;
	    double yIncr = 2;
	    static int canvasWidth;
	    static int canvasHeight;
	    static int iWidth;
	    static int iHeight;
	    Direction dir = Direction.EAST;
	    BassMode mode = BassMode.DEFAULT;
	    boolean movement = true;
		public Model(int width, int height, int imageWidth, int imageHeight) {
			
			this.canvasWidth = width;
			this.canvasHeight = height;
			this.iWidth = imageWidth;
			this.iHeight = imageHeight;
		}
		public void updateMDM(Direction dirP, BassMode modeP, boolean move) {
			dir = dirP;
			mode = modeP;
			movement = move;
		}

		public void updateLocationandDirection() {
			
			
			if (mode==BassMode.DEFAULT) {
				xIncr=8;
				yIncr=2;
				//System.out.println("Default");
			}
			else if (mode==BassMode.CONFUSE) {
				xIncr=4;
				yIncr=1;
				//System.out.println("Confuse");
			}
			else if (mode==BassMode.ATTAC) {
				xIncr=16;
				yIncr=4;
				//System.out.println("Attack");
			}
			if (dir==Direction.NORTH) {
				xIncr=0;
				yIncr=-yIncr;
				//System.out.println("North");
			}
			else if (dir==Direction.NORTHEAST) {
				yIncr=-yIncr;
				//System.out.println("North East");
			}
			else if (dir==Direction.EAST) {
				yIncr=0;
				//System.out.println("East");
			}
			else if (dir==Direction.SOUTH) {
				xIncr=0;
				//System.out.println("South");
			}
			else if (dir==Direction.SOUTHWEST) {
				xIncr=-xIncr;
				//System.out.println("Southwest");
			}
			else if (dir==Direction.WEST) {
				xIncr=-xIncr;
				yIncr=0;
				//System.out.println("West");
			}
			else if (dir==Direction.NORTHWEST) {
				xIncr=-xIncr;
				yIncr=-yIncr;
				//System.out.println("Northwest");
			}			
			if(movement == false) {
				xIncr = 0;
				yIncr = 0;
			}
			xloc+=xIncr;
			//System.out.println("xloc increase:" + xIncr);
			yloc+=yIncr;
			//System.out.println("yloc increase:" + yIncr);
			getDirection();
			
	    	
		}
		


		public int getX() {
			return xloc;
		}
		
		public int getY() {
			return yloc;
		}

		public Direction getDirection() {
			return dir;
		}


	}


