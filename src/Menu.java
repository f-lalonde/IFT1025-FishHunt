import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Affiche le logo et quelques bulles décoratives.
 */
public class Menu {

    public static final int WIDTH = FishHunt.WIDTH, HEIGHT = FishHunt.HEIGHT;

    private final FichierImg fishHunt = new FichierImg(
            51, 1,
            "/images/logo.png",
            538, 367);

    private final MenuBulle[] bulles = new MenuBulle[]{
            new MenuBulle(Math.random()*(WIDTH-30), HEIGHT+40,
                    "/images/bubble.png", 30, 30, Math.random()*40 + 20),
            new MenuBulle(Math.random()*(WIDTH-30), HEIGHT+40,
                    "/images/bubble.png", 30, 30, Math.random()*40 + 20),
            new MenuBulle(Math.random()*(WIDTH-30), HEIGHT+40,
                    "/images/bubble.png", 30, 30, Math.random()*40 + 20)
    };

    private final boolean[] bulleReady = {true, true, true};

    public Menu(){
    }

    public void update(double deltaTime){

        fishHunt.update(deltaTime);
        double bulleFlag = Math.random()*300;

        for(int i = 0; i<bulles.length; ++i){

            if(bulleFlag > 3*i && bulleFlag <= 3*i+3 && bulleReady[i]){
                bulles[i] = new MenuBulle(Math.random()*(WIDTH-30), HEIGHT+40,
                        "/images/bubble.png", 30, 30, Math.random()*1.5+.2);
                bulles[i].setVitY(-100);
                bulleReady[i] = false;
            }
            if(bulles[i].getPosY() < -35){
                bulles[i].setVitY(0);
                bulleReady[i] = true;
            }
            if(!bulleReady[i]){
                bulles[i].update(deltaTime);
            }
        }
    }

    public void draw(GraphicsContext context){
        context.setFill(Color.DARKBLUE);
        context.fillRect(0, 0, WIDTH, HEIGHT);

        bulles[1].draw(context);
        fishHunt.draw(context);
        bulles[0].draw(context);
        bulles[2].draw(context);
    }

}
