import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Jeu {
    // Variables purement du jeu
    public static final int WIDTH = FishHunt.WIDTH, HEIGHT = FishHunt.HEIGHT;

    private int score;

    private int debugNiv;
    private int niveau;
    private int niveauChk;
    private int nbVies;
    private boolean gameOver;
    // Objets créés

    private final Bulles[][] bulles = {new Bulles[5], new Bulles[5], new Bulles[5]};
    private final FichierImg cible = new FichierImg(0,0, "/images/cible.png",50,50);

    // Variables pour la génération d'objets

    private PoissonNormal poissonNormal;
    private Poissons poissonSpecial;
    private final Projectile[] balles = new Projectile[11];
    private int prochaineBalle = 0;

    private double tempsBulles, tempsPoisson, tempsCrabe, tempsNiveau;

    // Variables de gestion de la mise à jour des objets en lien avec le jeu
    double mouseX, mouseY;
    boolean poissonFrais, poissonMeurtri, crabeFrais, crabeMeurtri;

    boolean niveauAjour;


    /**
     * Constructeur de jeu. Réinitialise les variables de jeu et génère les premières bulles.
     */
    public Jeu(){
        //initialization des variables
        niveau = 1;
        niveauChk = 0;
        nbVies = 3;
        score = 0;
        debugNiv = 0;
        gameOver = false;
        poissonFrais = false;
        poissonMeurtri = false;
        crabeFrais = false;
        crabeMeurtri = false;
        niveauAjour = true;

        genererBulles(0);
        genererBulles(1);
        genererBulles(2);
    }

    /**
     * Génère un groupe de bulles selon la grandeur du tableau de bulles envoyé.
     * Choisi au hasard un diamètre pour chaque bulle, et une position horizontale pour le groupe de bulles.
     * @param groupe tableau d'objets Bulles
     */
    private void genererBulles(int groupe){
        double bullesPosX = Math.random()*WIDTH;
        double bullesPosY = Math.random()*20 + HEIGHT + 40;

        for(int i=0; i<bulles[groupe].length; ++i){
            double diametre = Math.random()*30 + 10;
            bulles[groupe][i] = new Bulles(bullesPosX, bullesPosY, diametre);
        }
    }

    /**
     * Génère les poissons normaux. Calcule aléatoirement leur position Y initiale.
     * @return un objet de type PoissonNormal avec des caractéristiques aléatoires
     */
    private PoissonNormal genererPoisson(){
        double posY = Math.max((4*Math.random()/5)*HEIGHT, (double)(1/5)*HEIGHT);
        poissonNormal = new PoissonNormal(WIDTH, posY, niveau);
        return poissonNormal;
    }

    /**
     * Génère les poissons spéciaux : 50 % crabe - 50 % étoile de mer. Calcule aléatoirement leur position Y initiale.
     * @return un objet Crabe ou Starfish, casté en Poissons.
     */
    private Poissons genererSpecial(){
        double posY = Math.max((4*Math.random()/5)*HEIGHT, (double)(1/5)*HEIGHT);
        if(Math.random() > 0.5){
            poissonSpecial = new Crabe(WIDTH, posY, niveau);
        } else {
            poissonSpecial = new Starfish(WIDTH, posY, niveau);
        }
        return poissonSpecial;
    }

    /**
     * Génère les projectiles à la position de la souris au moment de l'appel de la fonction.
     * @param posX position X de la souris
     * @param posY position Y de la souris
     * @return un objet de type Projectile
     */
    private Projectile genererProjectile(double posX, double posY){
        Projectile balle = new Projectile(posX, posY);

        // centrer le projective sur le curseur
        balle.setPosX(balle.getPosX() - balle.getRayon());
        balle.setPosY(balle.getPosY() - balle.getRayon());

        return balle;
    }

    /**
     * Reçois l'appel de lancer un projectile depuis le contrôleur. Place les projectiles dans un tableau de
     * Projectiles. Évite de faire disparaître les projectiles toujours en jeu, sauf le dernier.
     */
    public void pewPew(){
        for(int i=0; i<balles.length -1; ++i){
            if(balles[i] == null || balles[i].isFini()){
                prochaineBalle = i;
                break;
            } else {
                prochaineBalle = 10;
            }
        }
        balles[prochaineBalle] = genererProjectile(mouseX, mouseY);
    }

    /**
     * Reçois les informations de la position de la souris
     * @param mouseX pos horizontale
     * @param mouseY pos verticale
     */
    public void mouseLink(double mouseX, double mouseY){
        this.mouseX = mouseX;
        this.mouseY = mouseY;
    }


    /**
     * Effectue la mise à jour de tous les éléments du jeu
     * @param deltaTime temps écoulé depuis le dernier update(), en secondes.
     */
    public void update(double deltaTime) {

        niveau = 1 + (score + debugNiv)/5;
        if(niveau != niveauChk){
            tempsNiveau = 0;
            niveauAjour = false;
            niveauChk = niveau;
            poissonFrais = false;
            crabeFrais = false;
            tempsCrabe = 0;
            tempsPoisson = 0;
        }

        tempsNiveau += deltaTime;
        if(!niveauAjour && tempsNiveau >= 3){
            niveauAjour = true;
        }

        // On génère des bulles aux 6 secondes
        tempsBulles += deltaTime;
        if (tempsBulles >= 6) {
            genererBulles(0);
            genererBulles(1);
            genererBulles(2);
            tempsBulles = 0;
        }

        for (Bulles[] bulle : bulles) {
            for (Bulles value : bulle) {
                value.update(deltaTime);
            }
        }

        if(!gameOver) {
            cible.setPosX(mouseX - cible.getLargeur() / 2);
            cible.setPosY(mouseY - cible.getHauteur() / 2);
            cible.update(deltaTime);

            for (Projectile balle : balles) {
                if (!(balle == null)) {
                    balle.update(deltaTime);
                    if (poissonFrais) {
                        poissonMeurtri = balle.testAtteint(poissonNormal);
                        if (poissonMeurtri) {
                            poissonFrais = false;
                            score++;
                        }
                    }

                    if (crabeFrais) {
                        crabeMeurtri = balle.testAtteint(poissonSpecial);
                        if (crabeMeurtri) {
                            crabeFrais = false;
                            score++;
                        }
                    }
                }
            }

            if(niveauAjour) {
                if (poissonFrais) {
                    poissonNormal.update(deltaTime);
                    if (poissonNormal.getPosX() > WIDTH + 75 || poissonNormal.getPosX() < -75) {
                        if (nbVies > 0) {
                            nbVies--;
                        }
                        poissonFrais = false;
                    }
                }
                if (crabeFrais) {
                    poissonSpecial.update(deltaTime);
                    if (poissonSpecial.getPosX() > WIDTH + 75 || poissonSpecial.getPosX() < -75) {
                        if (nbVies > 0) {
                            nbVies--;
                        }
                        crabeFrais = false;
                    }
                }

                tempsPoisson += deltaTime;
                if (tempsPoisson >= 3) {
                    poissonNormal = genererPoisson();
                    poissonFrais = true;
                    tempsPoisson = 0;
                }

                tempsCrabe += deltaTime;
                if (niveau > 1 && tempsCrabe >= 5) {
                    poissonSpecial = genererSpecial();
                    crabeFrais = true;
                    tempsCrabe = 0;
                }

                if (nbVies == 0) {
                    gameOver = true;
                }
            }
        }
    }

    public int getNiveau() {
        return niveau;
    }

    public int getScore(){
        return score;
    }

    public int getNbVies() {
        return nbVies;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void debugGameOver() {
        this.gameOver = true;
    }

    public void debugScore() {
        this.score++;
    }

    public void debugNiveau() {
        this.debugNiv += 5;
    }

    public void debugNbVies() {
        this.nbVies++;
    }

    public void draw(GraphicsContext context){
        context.setFill(Color.DARKBLUE);
        context.fillRect(0,0, WIDTH, HEIGHT);

        for (Bulles[] bulle : bulles) {
            for (Bulles value : bulle) {
                value.draw(context);
            }
        }
        if(!gameOver) {
            if (poissonFrais) {
                poissonNormal.draw(context);
            }
            if (crabeFrais) {
                poissonSpecial.draw(context);
            }
            for (Projectile balle : balles) {
                if (!(balle == null)) {
                    balle.draw(context);
                }
            }
            cible.draw(context);
        }
    }

}
