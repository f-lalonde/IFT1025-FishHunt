import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 * Crée de jolis petits poissons normaux qui agissent tous de la même façon même s'ils ont un look différent.
 */
public class PoissonNormal extends Poissons {

    private Image img;

    public PoissonNormal(double posX, double posY, double niveau){
        super(posX,posY, niveau);

        this.setAccY(100);
        this.setVitY(-(Math.random()*100 +100));

        // les noms des images sont 00.png, 01.png, ... , 08.png
        img = new Image("/images/fish/0" + (int) Math.floor(Math.random() * 8)+ ".png");

        // taille des poissons peut varier entre 10% - 20% de la taille des images originales
        double hasard = Math.max(Math.random()*.2, 0.1);

        this.setLargeur(img.getWidth()*hasard);
        this.setHauteur(img.getHeight()*hasard);

        if(!ArriveDeGauche()){
            // le poisson arrive de la droite et regarde à gauche
            img = flop(img);
        }

        // on choisit une couleur au hasard entre RGB(102,102,102) et RGB(255,255,255). R, G et B sont indépendants.
        Color couleur = Color.rgb(
                102+(int)Math.ceil(Math.random()*153),
                102+(int)Math.ceil(Math.random()*153),
                102+(int)Math.ceil(Math.random()*153));
        img = colorize(img, couleur);

    }

    @Override
    public void draw(GraphicsContext context){
        context.drawImage(img, getPosX(), getPosY(), getLargeur(), getHauteur());
    }
}
