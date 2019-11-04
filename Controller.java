import javafx.application.Application;
import javafx.stage.Stage;
import javafx.animation.AnimationTimer;

public class Controller extends Application {
	//data fields hold Model and View
	private Model model;
	private View view;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
	public void start(Stage theStage) {
        view = new View(theStage);
		model = new Model(view.getWidth(), view.getHeight(), 
                view.getImageWidth(), view.getImageHeight());
        new AnimationTimer() {
            public void handle(long currentNanoTime)
            {
                //increment the x and y coordinates, alter direction if necessary
               model.updateMDM(view.viewDirect(), view.getviewMode(), view.getMoveValue());
               
            	model.updateLocationandDirection();
                //input the x coordinates, y coordinates, and direction picture
                view.update(model.getX(), model.getY(), model.getDirection());
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        theStage.show();
    }

}
