import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

/**
 * Classe abstraite, étandant Corps, à étendre à tous les poissons.
 */
public abstract class Poissons extends Corps{
    public static final int WIDTH = FishHunt.WIDTH;
    private final boolean arriveDeGauche;

    public Poissons(double posX, double posY, double niveau){
        super(posX, posY);

        this.setVitX(100*Math.cbrt(niveau) + 200);

        if(Math.random() > 0.5){
            arriveDeGauche = false;
            this.setVitX(-this.getVitX());
            // un peu de variation dans la posX initiale modifiera légèrement le comportement des poissons à l'écran
            this.setPosX(WIDTH + 50);
        } else {
            arriveDeGauche = true;
            this.setPosX(-50);
        }
        setRestictionY(true);
    }

    /**
     * Inversion verticale d'une image.
     *
     * @param img L'image à inverser
     * @return Une nouvelle image contenant une inversion horizontale des pixels
     * de l'image originale
     */
    public static Image flop(Image img) {
        int w = (int) img.getWidth();
        WritableImage output = new WritableImage(w, (int) img.getHeight());

        PixelReader reader = img.getPixelReader();
        PixelWriter writer = output.getPixelWriter();

        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                Color color = reader.getColor(x, y);
                writer.setColor(w - 1 - x, y, color);
            }
        }
        return output;
    }

    /**
     * Recolorie une image avec une couleur donnée. Tous les pixels
     * non-transparents de l'image img se font attribuer la couleur color passée
     * en paramètre.
     *
     * @param img L'image originale
     * @param color La nouvelle couleur à utiliser
     * @return Une nouvelle image contenant une version re-coloriée de l'image
     * originale
     */
    public static Image colorize(Image img, Color color) {
        WritableImage output = new WritableImage((int) img.getWidth(), (int) img.getHeight());

        PixelReader reader = img.getPixelReader();
        PixelWriter writer = output.getPixelWriter();

        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                if (reader.getColor(x, y).getOpacity() > 0) {
                    writer.setColor(x, y, color);
                }
            }
        }
        return output;
    }

    public boolean ArriveDeGauche() {
        return arriveDeGauche;
    }
}
