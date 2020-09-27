import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Projectile {

    private double rayon;
    private final double accZ;

    private double posX, posY;
    private double vitY;
    private final double accY;

    private boolean aDestination, fin;

    public Projectile(double posX, double posY){
        this.posX = posX;
        this.posY = posY;
        this.rayon = 50;
        this.vitY = -300;
        this.accY = 1200;
        this.accZ = 100;
        this.aDestination = false;
        this.fin = false;
    }

    /**
     * Appelle la vérification de collision entre un objet de type Poisson et le projectile, et s'assure de ne le faire
     * qu'une seule fois pas projectile.
     * @param poisson Objet de type Poissons
     * @return true si atteint, false sinon.
     */
    public boolean testAtteint(Poissons poisson){
        if(aDestination){
            aDestination=false;
            fin = true;
            return intersect(poisson);
        } else {
            return false;
        }
    }

    /**
     * Vérifie la position de l'objet de type Poissons par rapport à celui du projectile.
     * @param poisson objet de type Poissons
     * @return true si il y a collision, false sinon.
     */
    private boolean intersect(Poissons poisson){
        return !(   // Un des objets est à gauche de l’autre
                    this.getPosX() < poisson.getPosX() || poisson.getPosX() + poisson.getLargeur() < this.getPosX()
                    // Un des objets est en haut de l’autre
                    || this.getPosY() < poisson.getPosY() || poisson.getPosY() + poisson.getHauteur() < this.getPosY());
    }

    public void update(double deltaTime){
        if(rayon > 0) {
            rayon = rayon - accZ*deltaTime;
            posX = posX + accZ*deltaTime;
            posY += (accZ + vitY) * deltaTime;
            vitY += deltaTime * accY;

        } else if(!fin){
            rayon = 0;
            aDestination = true;
        }
    }

    public void draw(GraphicsContext context){
        context.setFill(Color.GRAY);
        context.fillOval(posX, posY,2*rayon, 2*rayon);
    }

    public double getRayon() {
        return rayon;
    }

    public boolean isFini() {
        return fin;
    }

    public double getPosX() {
        return posX;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public double getPosY() {
        return posY;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }
}
