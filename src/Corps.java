import javafx.scene.canvas.GraphicsContext;

/**
 * Classe abstraite à étendre pour tous les objets qui ont une présence physique dans le jeu.
 * Il faut définir la méthode draw() selon les caractéristiques que l'on veut que l'objet prenne.
 */
public abstract class Corps {

    private double largeur, hauteur;
    private double posX, posY;

    private double vitX, vitY;
    private double accX, accY;

    private boolean restictionY = false;

    /**
     *
     * @param posX  Position horizontale où est générée l'objet
     * @param posY  Position verticale où est générée l'objet
     */
    public Corps(double posX, double posY){
        this.posX = posX;
        this.posY = posY;
    }
    /**
     * Met à jour la position et la vitesse de l'objet
     * @param deltaTime Temps écoulé depuis le dernier update() en secondes
     */
    public void update(double deltaTime){
        vitX += deltaTime * accX;
        vitY += deltaTime * accY;
        posX += deltaTime * vitX;
        posY += deltaTime * vitY;

        if(this.restictionY){
            // Si on a besoin de mettre des restriction  en Y. Ici, seulement au sommet de l'écran.
            // Attention lors de modifications ultérieures
            this.posY = Math.max(this.posY, 0);
        } else {
            // on permet une certaine zone où les objets peuvent exister en haut et en bas de la zone de jeu.
            posY = Math.min(posY, 1.5*FishHunt.HEIGHT);
            posY = Math.max(posY, -FishHunt.HEIGHT);
        }

    }

    public double getLargeur(){
        return this.largeur;
    }

    public void setLargeur(double largeur) {
        this.largeur = largeur;
    }

    public double getHauteur(){
        return this.hauteur;
    }

    public void setHauteur(double hauteur) {
        this.hauteur = hauteur;
    }

    public double getPosX(){
        return this.posX;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public double getPosY(){
        return this.posY;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    public double getVitX(){
        return this.vitX;
    }

    public void setVitX(double vitX) {
        this.vitX = vitX;
    }

    public double getVitY(){
        return this.vitY;
    }

    public void setVitY(double vitY) {
        this.vitY = vitY;
    }

    /*public double getAccX(){
        return this.accX;
    }*/

    public void setAccX(double accX) {
        this.accX = accX;
    }

    public double getAccY(){
        return this.accY;
    }

    public void setAccY(double accY) {
        this.accY = accY;
    }

    public void setRestictionY(boolean restictionY) {
        this.restictionY = restictionY;
    }

    public abstract void draw(GraphicsContext context);
}
