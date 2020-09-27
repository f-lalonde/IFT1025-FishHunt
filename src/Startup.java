import javafx.scene.canvas.GraphicsContext;
// import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
// import java.io.File;

/**
 * Affiche l'écran d'accueil, et transitionne vers le menu, OLD NES GAME style.
 */
public class Startup {
    public static final double WIDTH = FishHunt.WIDTH, HEIGHT = FishHunt.HEIGHT;
    private final Rectangle fondNoir = new Rectangle(0,0,WIDTH, HEIGHT);
    private final FichierImg logo = new FichierImg(
            WIDTH/2 -110, HEIGHT/2 -60,
            "/images/logoift.png",
            220, 120);

    private final FichierImg fishHunt = new FichierImg(
            51, HEIGHT,
            "/images/logo.png",
            538, 367);

    // private final AudioClip coins = new AudioClip(new File("sons/coins.wav").toURI().toString());

    private double tempDelais;
    private boolean sonLogojoue = false;
    private boolean enCoursStartup;
    private boolean onBouge = false;
    public Startup(){
        enCoursStartup = true;
    }

    public void update(double deltaTime){
        fondNoir.update(deltaTime);
        logo.update(deltaTime);
        fishHunt.update(deltaTime);

        tempDelais += deltaTime;

        if (tempDelais > 1 && !sonLogojoue) {
            //coins.play();
            sonLogojoue = true;
        }

        if (tempDelais > 3 && !onBouge) {
            fondNoir.setVitY(-120);
            logo.setVitY(-120);
            fishHunt.setVitY(-120);
            onBouge = true;
        }

        if (fishHunt.getPosY() < 1 && enCoursStartup) {
            fondNoir.setVitY(0);
            logo.setVitY(0);
            fishHunt.setVitY(0);
            enCoursStartup = false;
        }
    }

    public void draw(GraphicsContext context){
        context.setFill(Color.DARKBLUE);
        context.fillRect(0, 0, WIDTH, HEIGHT);
        fondNoir.draw(context);
        logo.draw(context);
        fishHunt.draw(context);
    }

    public boolean isEnCoursStartup() {
        return enCoursStartup;
    }
}
