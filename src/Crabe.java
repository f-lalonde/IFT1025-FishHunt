import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * Crée un beau petit crabe oscillant, de gauche à droite, ou de droite à gauche, c'est selon.
 */
public class Crabe extends Poissons {

    private final Image img;
    //private double radMod;
    private double timeCounter;
    // private double centreOscillation;

    public Crabe(double posX, double posY, double niveau) {
        super(posX, posY, niveau);

        img = new Image("/images/crabe.png");

        // taille des crabes peut varier initialement de 10% - 20% de la taille des images originales, mais
        // peuvent devenir de plus en plus petit alors que les niveaux avancent.
        double min = Math.max(.08, 0.15 - (Math.sqrt(niveau)) / 100);
        double hasard = Math.max(Math.random() * .2, min);

        //radMod = 0;
        timeCounter = 0;

        setVitX(1.3*getVitX());
        // centreOscillation = getPosX();
        this.setLargeur(img.getWidth() * hasard);
        this.setHauteur(img.getHeight() * hasard);

    }

    @Override
    public void update(double deltaTime){
        /*if(radMod > 2*Math.PI){                                       // Pour fonctions trigo.
            radMod -= 2*Math.PI;
        }
        if(radMod <= Math.PI){
            radMod += 2*Math.PI*deltaTime;
        } else {
            radMod += 4*Math.PI*deltaTime;
        }*/

        timeCounter += deltaTime;                                        // Variation "carrée" d'ici... ↓
        if(timeCounter <= .5){
            setPosX(getPosX() + deltaTime*getVitX());
        } else if(timeCounter <= .75){
            setPosX(getPosX() + -deltaTime*getVitX());
        } else {
            setPosX(getPosX() + deltaTime*getVitX());
            timeCounter -= 0.75;
        }                                                                // jusqu'à ici ↑

        // centreOscillation += getVitX()*deltaTime;                     // Base pour variation trigonométrique
        // setPosX(centreOscillation + Math.sin(radMod)*50);             // Sinusoidale
        // setPosX(centreOscillation + Math.asin(Math.sin(radMod))*75);  // Triangulaire
        setVitY(getVitY() + deltaTime * getAccY());
        setPosY(getPosY()+deltaTime*getVitY());
    }

    @Override
    public void draw(GraphicsContext context){
        context.drawImage(img, getPosX(), getPosY(), getLargeur(), getHauteur());
    }
}
