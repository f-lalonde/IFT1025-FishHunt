import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * Crée une étoile de mer, qui avance en ondulant selon une fonction sinusoïdale.
 */
public class Starfish extends Poissons{

    private final Image img;
    private double rad;
    private final double posYinit;

    public Starfish(double posX, double posY, double niveau) {
        super(posX, posY, niveau);

        img = new Image("/images/star.png");
        posYinit = getPosY();

        // taille des étoiles de mer peut varier initialement de 10% - 20% de la taille des images originales, mais
        // peuvent devenir de plus en plus petit alors que les niveaux avancent.
        double min = Math.max(.08, 0.15 - (Math.sqrt(niveau)) / 100);
        double hasard = Math.max(Math.random() * .2, min);
        rad = 0;
        setVitY(0);

        this.setLargeur(img.getWidth() * hasard);
        this.setHauteur(img.getHeight() * hasard);
    }

    @Override
    public void update(double deltaTime){
        // Transforme les valeurs de deltaTime directement en radian.
        rad += 2*Math.PI*deltaTime;
        setPosX(getPosX() + getVitX()*deltaTime);
        setPosY(Math.max(posYinit + 50*Math.sin(rad), 0));
    }

    @Override
    public void draw(GraphicsContext context){
        context.drawImage(img, getPosX(), getPosY(), getLargeur(), getHauteur());
    }
}
