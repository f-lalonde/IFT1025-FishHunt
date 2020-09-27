import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.util.Arrays;

/**
 * Fait le lien entre la mécanique du jeu et la classe principale
 */
public class Controleur {

    // s'assurer de n'avoir qu'un élément à true dans ce tableau!
    boolean[] switchScene = {false,false,false}; // 0 = menu, 1 = jeu, 2 = scoreboard.
    boolean initDone = false;
    double mouseX, mouseY;
    Startup start;
    Jeu jeu;
    Menu menu;
    LeaderBoard leaderBoard;

    public Controleur(){
        start = new Startup();
    }

    /**
     * Génère un objet Menu et appelle le changement de scène vers celui-ci
     */
    void menu(){
        menu = new Menu();
        setSwitchScene("menu");
    }


    /**
     * Génère un objet Jeu et appelle le changement de scène vers celui-ci
     */
    void debutJeu(){
        jeu = new Jeu();
        setSwitchScene("fishOn");
    }

    /**
     * Génère un objet LeaderBoard et appelle le changement de scène vers celui-ci
     */
    void leaderBoard() throws Exception {
        leaderBoard = new LeaderBoard();
        setSwitchScene("leaderBoard");
    }

    void draw(GraphicsContext context){
        if(start.isEnCoursStartup()){
            start.draw(context);
        } else if (switchScene[0]){
            menu.draw(context);
        } else if(switchScene[1]){
            jeu.mouseLink(mouseX, mouseY);
            jeu.draw(context);
        } else if(switchScene[2]){
            leaderBoard.draw(context);
        }
    }

    void update(double deltaTime){

        if (switchScene[0]){
            menu.update(deltaTime);
        } else if(switchScene[1]){
            jeu.update(deltaTime);
        }

        if(start.isEnCoursStartup()) {
            start.update(deltaTime);
        } else if(!initDone) {
            menu();
            initDone = true;
        }
    }

    void addScore(String nom, int score){
        leaderBoard.addScore(nom, score);
    }

    void printScore(ListView<String> listView){
        leaderBoard.printScore(listView);
    }

    // ↓ Getters et setters ↓

    public boolean[] getSwitchScene() {
        return switchScene;
    }

    private void setSwitchScene(String nomScene) {
        switch(nomScene){
            case "menu":
                switchScene[0] = true;
                switchScene[1] = false;
                switchScene[2] = false;
                break;
            case "fishOn":
                switchScene[0] = false;
                switchScene[1] = true;
                switchScene[2] = false;
                break;
            case "leaderBoard":
                switchScene[0] = false;
                switchScene[1] = false;
                switchScene[2] = true;
                break;
            default:
                switchScene[0] = false;
                switchScene[1] = false;
                switchScene[2] = false;
                break;
        }
    }

    void mouseLink(double mouseX, double mouseY){
        this.mouseX = mouseX;
        this.mouseY = mouseY;
    }

    void pewPew(){
        jeu.pewPew();
    }

    int getScore(){
        return jeu.getScore();
    }

    int getNbVies(){
        return jeu.getNbVies();
    }

    int getNiveau() {
        return jeu.getNiveau();
    }

    boolean isNiveauAjour(){
        if(jeu == null){
            return true;
        } else {
            return jeu.niveauAjour;
        }
    }

    boolean isGameOver(){
        if(jeu == null){
            return false;
        } else {
            return jeu.isGameOver();
        }
    }

    void debugVies(){
        jeu.debugNbVies();
    }

    void debugScore(){
        jeu.debugScore();
    }

    void debugNiveau(){
        jeu.debugNiveau();

    }

    void debugGameOver(){
        jeu.debugGameOver();
    }

}
