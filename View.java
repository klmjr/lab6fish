import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.event.*;
import javafx.scene.input.*;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.animation.AnimationTimer;
import javafx.geometry.Rectangle2D;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;

import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.util.ArrayList;

// NOTE: will not be needed once lab is complete
import java.util.Random;

/**
 * This is a drop in replacement View for the ORC MVC Lab. It will animate
 * a poorly-drawn striped bass in a similar manner to the orc. However, 
 * instead of loading prexisting images of different directions, 
 * we will instead rotate and flip the image to get the desired direction.
 * This is nice, since we only need to find one image for the bass character, 
 * but this will only work for simple character designs. This would not work for the orc.
 *
 * The bass has been caught and placed in a fish tank. It is not sure what happend, 
 * so it is randomly swimming around right now, and randomly changing moods (modes)
 * every time it runs into the glass wall. It is either in the DEFAULT mode, ATTAC mode
 * or CONFUSE mode. 
 *
 * Your task: 
 *  1) Drop this View into your ORC MVC lab. If you did it correctly and left the Controller
 *     alone as well as made a Direction enum, then this will be as easy as deleting the old file
 *     and replacing it with this one. Additionally, you must place the new images in 
 *     the correct location in the project directory.
 *
 *  2) Control the motion of the bass by either making it follow the mouse (either continuously
 *     or to a clicked location is fine), or by using the arrow keys or WASD. Make sure to 
 *     specify which one you used in the comments on Canvas.
 *
 *  3) Control the bass's mood and speed. Pick three keys that will control the bass's 
 *     mood. For example, '1' can be CONFUSE, '2' can be DEFAULT, and '3' can be ATTAC.
 *     For CONFUSE mode, make the bass go at half speed, for ATTAC mode, make it double speed.
 *     DEFAULT mode is the reference speed.
 *
 **/

public class View {


	int picNum = -1; // index for which picture to use
    int picCount = 4; // number of pics in animation 

	// value of the height and width of screen
	int canvasWidth = 1380;
	int canvasHeight = 940;
	// value of the size of the image
	static final int imgWidthOrig = 100;
	static final int imgHeightOrig = 100;

	int imgWidth = 300;
	int imgHeight = 300;
    
    GraphicsContext gc;

    Image background;
	// array of wide png images
    Image[] animationSequence;

	// Used to determine the direction to warp the bass image into
	Direction currentDirection, lastDirection;
    int modeInd = -1;
    // Used to index the animationSequence outter array. 
    BassMode bassMode; 
    Direction dir = Direction.EAST;
    boolean move = true;
	
	//variables to determine the location of image
	int x = 0;
	int y = 0;
	
	//View constructor initialize the starting position for the image
	//Called in controller
	public View(Stage theStage) {
        theStage.setTitle("Bass");

        Group root = new Group();
        Button StopnGo = new Button("Stop");
        //Label l = new Label("Stop");
		
		
		
        Scene theScene = new Scene(root);
        theStage.setScene(theScene);

        Canvas canvas = new Canvas(canvasWidth, canvasHeight);
        root.getChildren().add(canvas);
        root.getChildren().add(StopnGo);
        StopnGo.setTranslateX(canvasWidth / 2);
        StopnGo.setTranslateY(canvasHeight / 2);
        //root.getChildren().add(l);
        gc = canvas.getGraphicsContext2D();
        
		
		

		// bassMode starts with DEFAULT
        bassMode = BassMode.DEFAULT;
		importImages();
		
		EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() { 
            public void handle(ActionEvent e) 
            { 
                if(move == true) {
                	move = false;
                	StopnGo.setText("Start");
                }
                else {
                	move = true;
                	StopnGo.setText("Stop");
                }
            } 
        };
        
        StopnGo.setOnAction(event);
  
		
		theScene.setOnKeyPressed(new EventHandler<KeyEvent>(){
			@Override
			public void handle(KeyEvent e) {
				switch(e.getCode()) {
				case UP:
					if (dir==Direction.SOUTH||dir==Direction.NORTHWEST||dir==Direction.NORTHEAST) {
        				dir=Direction.NORTH;
        			}
        			if (dir==Direction.EAST||dir==Direction.SOUTHEAST) {
        				dir=Direction.NORTHEAST;
        			}
        			if (dir==Direction.WEST||dir==Direction.SOUTHWEST) {
        				dir=Direction.NORTHWEST;
        			}
        			break;
				case DOWN:
        			if (dir==Direction.NORTH||dir==Direction.SOUTHWEST||dir==Direction.SOUTHEAST) {
        				dir=Direction.SOUTH;
        			}
        			if (dir==Direction.NORTHWEST||dir==Direction.WEST) {
        				dir=Direction.SOUTHWEST;
        			}
        			if (dir==Direction.NORTHEAST||dir==Direction.EAST) {
        				dir=Direction.SOUTHEAST;
        			}
        			break;
        		case LEFT:
        			if (dir==Direction.EAST||dir==Direction.SOUTHWEST||dir==Direction.NORTHWEST) {
        				dir=Direction.WEST;
        			}
        			if (dir==Direction.NORTH||dir==Direction.NORTHEAST) {
        				dir=Direction.NORTHWEST;
        			}
        			if (dir==Direction.SOUTH||dir==Direction.SOUTHEAST) {
        				dir=Direction.SOUTHWEST;
        			}
        			break;
        		case RIGHT:
        			if (dir==Direction.NORTHEAST||dir==Direction.WEST||dir==Direction.SOUTHEAST) {
        				dir=Direction.EAST;
        			}
        			if (dir==Direction.NORTH||dir==Direction.NORTHWEST) {
        				dir=Direction.NORTHEAST;
        			}
        			if (dir==Direction.SOUTH||dir==Direction.SOUTHWEST) {
        				dir=Direction.SOUTHEAST;
        			}
        			break;
        		case Q:
        			bassMode=BassMode.DEFAULT;
        			break;
        		case W:
        			bassMode=BassMode.CONFUSE;
        			break;
        		case E:
        			bassMode=BassMode.ATTAC;
        			break;        			
        		}
        	}
			
			
        });
	}	
					
				
		public Direction viewDirect() {
				return dir;
			}
		public BassMode getviewMode() {
			return bassMode;
		}
		public boolean getMoveValue() {
			return move;
		}

	
	//Method used to import the images into the 2D image array
	private void importImages() {
		
		//Create array of the images. Each image pixel map contains
        // multiple images of the animate at different time steps
        
        // Eclipse will look for <path/to/project>/bin/<relative path specified>
        String img_file_base = "\\images\\";
        String bass_file_base = img_file_base + "drop-the-bass\\";
        String ext = ".png";

        // Infer number of modes from BassMode enum
		animationSequence = new Image[BassMode.values().length]; 
			
		for(BassMode mode : BassMode.values())
		{
            // Use ordinal to index array, then getName to get the string 
            // specified in the enum definition above
			
			animationSequence[mode.ordinal()] = createImage(bass_file_base
                                                        + mode.getName() + ext);
		}		  	

        // Now we have the wide pngs for each mode stored in animationSequence
        
        // Load background
        background = createImage(img_file_base + "underwater" + ext);
	}
	
    //Read image from file and return
    private Image createImage(String image_file) {
        Image img = new Image(image_file);
        return img;   	
    }

	//method used to repaint on the image and called in controller
	public void update(int xLoc, int yLoc, Direction direction) {
		x = xLoc;
		y = yLoc;
		currentDirection = direction;
    
        if (modeInd == -1 || currentDirection != lastDirection) {
            Random rand = new Random();
            modeInd = rand.nextInt(4);
            lastDirection = currentDirection;
        }

        // TODO fix bassMode to be based on key presses
		Image pics = animationSequence[bassMode.ordinal()];
		picNum = (picNum + 1) % picCount;

        // Clear the canvas
        gc.clearRect(0, 0, canvasWidth, canvasHeight);

        // draw background
        gc.drawImage(background, 0, 0, canvasWidth, canvasHeight);

        // First, crop the wide png into image so we can rotate and flip it!!
        //
        // Define the Rectangle to crop by (x,y,widht,height)
        Rectangle2D croppedPortion = new Rectangle2D(imgWidthOrig*picNum, 0, 
                                                 imgWidthOrig, imgHeightOrig);
        // Define an ImageView with the wide png image 'pics'
        ImageView imageView = new ImageView(pics);
        imageView.setViewport(croppedPortion);
        imageView.setFitWidth(imgWidthOrig);
        imageView.setFitHeight(imgHeightOrig);
        imageView.setSmooth(false);
        // Crop!
        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);
        Image croppedImage = imageView.snapshot(params, null);

        // Now rotate and flip it based  on direction, then draw to canvas
        transformAndDraw(gc, croppedImage, x, y, direction);

	}
	
	//getter methods to get the frame dimensions and image dimensions
	public int getWidth() {
		return canvasWidth;
	}
	
	public int getHeight() {
		return canvasHeight;
	}
	
	public int getImageWidth() {
		return imgWidth;
	}
	
	public int getImageHeight() {
		return imgHeight;
	}

    // If the bass is facing to the WEST, we must flip it, then rotate accordingly
    // for NORTH/SOUTH
    // Then draw to gc
    private void transformAndDraw(GraphicsContext gc, Image image, 
            double x, double y, Direction d) {
        // clockwise rotation angle
        // Why clockwise? I don't know. It should probably be counter-clockwise
        double theta = 0.0;
        boolean isFlipped = false;
        // Switch statemement is good to use for enums
        switch (d) {
            case NORTH: {
                theta = -60.0;
                break;
            } case NORTHEAST: {
                theta = -30.0;
                break;
            } case EAST: {
                theta = 0.0;
                break;
            } case SOUTHEAST: {
                theta = 30.0;
                break;
            } case SOUTH: {
                theta = 60.0;
                break;
            } case SOUTHWEST: {
                isFlipped = true;
                theta = -30.0;
                break;
            } case WEST: {
                isFlipped = true;
                break;
            } case NORTHWEST: {
                isFlipped = true;
                theta = 30.0;
                break;
            }
        }

        // Setting x scale to -1 will flip it horizontally
        if (isFlipped) {
            ImageView iv = new ImageView(image);
            iv.setScaleX(-1.0);   
            SnapshotParameters params = new SnapshotParameters();
            params.setFill(Color.TRANSPARENT);
            image = iv.snapshot(params, null);
        }

        // Rotate the CANVAS and NOT the Image, because rotating the image
        // will crop part of the bass's tail for certain angles

        gc.save(); // saves the current state on stack, including the current transform

        // set canvas rotation about the point (x+imageWidth/2, y+imageWidth/2) by angle theta
        Rotate r = new Rotate(theta, x+imgWidth/2, y+imgWidth/2);
        // Transform gc by the rotation
        gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());

        // draw the subimage from the original png to animate the bass's motion
        // The arguments for drawImage are:
        // 
        // drawImage(sourceImage, sX, sY, sWidth, sHeight, dX, dY, dWidth, dHeight)
        //
        // This means that a rectangle of size (sWidth, sHeight) will be CROPPED from sourceImage
        // at location (sX, sY), RESIZED to (dWidth, dHeight), and drawn to the 
        // parent of the GraphicsContext at location (dX, dY).
        gc.drawImage(image, 0, 0, imgWidthOrig, imgHeightOrig,
                            x, y, imgWidth, imgHeight);
                        
        gc.restore(); // back to original state (before rotation)
    }

}
