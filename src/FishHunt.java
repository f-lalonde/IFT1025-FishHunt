/*
Francis Lalonde (801363)		    TP 3 - Fish Hunt
Jean-Daniel Toupin (20046724)		2 mai 2020
*/

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.scene.canvas.Canvas;
import java.util.Arrays;

public class FishHunt extends Application{
    private Stage primaryStage;
    private Controleur controleur;

    public static final int WIDTH = 640, HEIGHT = 480;

    public static void main(String[] args) {
        launch(args);
    }

    private String konami = "xxxxxxxxxx";

    private final Canvas canvas = new Canvas(WIDTH, HEIGHT);
    private String previousScene = "[false, false, false]";
    private boolean[] currentScene;

    private Button btnPlay;
    private Button btnLeadBoard;
    private Button btnScore;
    private Button btnMenu;

    private Text description;
    private Text score;
    private Text texte;

    private TextField textFieldScore = new TextField();
    private boolean menuToLb;

    private final Image vie1 = new Image("images/fish/00.png");
    private final Image vie2 = new Image("images/fish/00.png");
    private final Image vie3 = new Image("images/fish/00.png");
    private final ImageView imageView1 = new ImageView(vie1);
    private final ImageView imageView2 = new ImageView(vie2);
    private final ImageView imageView3 = new ImageView(vie3);
    private final ImageView[] vies = {imageView1, imageView2, imageView3};

    private final Image egg = new Image("images/logoift.png");
    private final ImageView leggo = new ImageView(egg);

    private double finCompteur = 0;

    @Override
    public void start(Stage primaryStage)  {
        this.primaryStage = primaryStage;

        primaryStage.getIcons().add(new Image("/images/shark-jaws.png"));
        primaryStage.setTitle("Fish Hunt");
        primaryStage.setResizable(false);

        GraphicsContext context = canvas.getGraphicsContext2D();
        controleur = new Controleur();

        primaryStage.setScene(startup());

        AnimationTimer timer = new AnimationTimer() {
            private long lastTime = 0;

            @Override
            public void handle(long now) {
                if (lastTime == 0) {
                    lastTime = now;
                    return;
                }

                double deltaTime = (now - lastTime) * 1e-9;

                updateFishHunt(deltaTime);
                controleur.update(deltaTime);
                controleur.draw(context);

                lastTime = now;
            }
        };

        timer.start();

        primaryStage.show();
    }

    /**
     * Génère un objet Text (JavaFX)
     * @param couleur Objet Color (JavaFX)
     * @param font  Nom de la police d'écriture désirée
     * @param taille Taille de la police d'écriture
     * @param posX Position horizontale du texte dans le parent
     * @param posY Position verticale du texte dans le parent
     * @return l'objet Text avec les propriétés spécifiées
     */
    Text creerText(Color couleur, String font, int taille, int posX, int posY){
        Text text = new Text();
        text.setFont(Font.font(font, FontWeight.BOLD, taille));
        text.setFill(couleur);
        text.setY(posY);
        text.setX(posX);

        return text;
    }

    /**
     * Regroupe toutes les assiagnations de touche.
     * @param value valeur du KeyEvent
     */
    private void keyBinding(KeyEvent value){
        switch (value.getCode()) {

            case ESCAPE:
                Platform.exit();
                break;

            case UP:
                konami = konami.substring(1, 10).concat("U");
                break;

            case RIGHT:
                konami = konami.substring(1, 10).concat("R");
                break;

            case A:
                konami = konami.substring(1, 10).concat("A");
                break;

            case LEFT:
                konami = konami.substring(1, 10).concat("L");
                break;

            case DOWN:
                konami = konami.substring(1, 10).concat("D");
                break;

            case B:
                konami = konami.substring(1, 10).concat("B");
                break;

            case H:
                if(currentScene[1]){
                    controleur.debugNiveau();
                }
                break;

            case J:
                if(currentScene[1]){
                    controleur.debugScore();
                }
                break;

            case K:
                if(currentScene[1] && controleur.getNbVies() < 3){
                    controleur.debugVies();
                }
                break;

            case L:
                if(currentScene[1]){
                    controleur.debugGameOver();
                }

            default:
                konami = konami.substring(1, 10).concat("X");
        }
    }

    /**
     * Crée la scène startup.
     * @return objet Scene startup.
     */
    private Scene startup(){
        Pane root = new Pane(canvas);
        return new Scene(root, WIDTH, HEIGHT);
    }

    /**
     * Crée la scène menu et ses objets, et défini également leurs fonctions.
     * @return objet Scene menu.
     */
    private Scene menu(){
        Pane root = new Pane(canvas);
        Scene menu = new Scene(root, WIDTH, HEIGHT);
        root.setOnKeyPressed(this::keyBinding);

        description = creerText(Color.WHITE, "Helvetica", 14, 0, HEIGHT-25);
        description.setText("");
        description.setTextAlignment(TextAlignment.CENTER);
        description.setWrappingWidth(WIDTH);

        btnPlay = new Button("Nouvelle partie!");
        btnPlay.setLayoutY(HEIGHT - 100);
        btnPlay.setLayoutX(92);
        btnPlay.setPrefSize(125, 50);
        btnPlay.setOnMouseClicked(e -> controleur.debutJeu());

        btnLeadBoard = new Button("Meilleurs Scores");
        btnLeadBoard.setLayoutY(HEIGHT - 100);
        btnLeadBoard.setLayoutX(btnPlay.getLayoutX() + btnPlay.getPrefWidth() + 206); // + 40 avec 3e bouton
        btnLeadBoard.setPrefSize(125, 50);
        btnLeadBoard.setOnMouseClicked(e -> {
            menuToLb = true;
            try {
                controleur.leaderBoard();
            } catch (Exception err) {
                err.printStackTrace();
            }
            description.setText("");
        });

        imageViewSetter(leggo, 0,0,165,90);
        leggo.setVisible(false);

        root.getChildren().addAll(btnPlay, btnLeadBoard, leggo, description);
        root.requestFocus();

        return menu;
    }

    /**
     * Crée la scène fishOn (Jeu) et ses objets, et défini également leurs fonctions.
     * @return objet Scene fishOn.
     */
    private Scene fishOn(){
        Pane root = new Pane(canvas);
        Scene fishOn = new Scene(root, WIDTH, HEIGHT);

        score = creerText(Color.WHITE, "Segoe UI", 20, 0, 40);
        score.setWrappingWidth(WIDTH);
        score.setTextAlignment(TextAlignment.CENTER);

        texte = creerText(Color.FIREBRICK, "Consolas", 32, 0, HEIGHT+30);
        texte.setText("MORT DE FAIM");
        texte.setWrappingWidth(WIDTH);
        texte.setTextAlignment(TextAlignment.CENTER);

        description = creerText(Color.WHITE, "Segoe UI", 40, 0, 200);
        description.setWrappingWidth(WIDTH);
        description.setTextAlignment(TextAlignment.CENTER);

        fishOn.setOnMouseMoved(e -> controleur.mouseLink(e.getX(), e.getY()));
        fishOn.setOnMouseDragged(e -> controleur.mouseLink(e.getX(), e.getY()));
        fishOn.setOnMouseClicked(e -> controleur.pewPew());
        fishOn.setOnKeyPressed(this::keyBinding);
        imageViewSetter(vies[2], 250,80, 30, 26);
        imageViewSetter(vies[1], 305,80, 30, 26);
        imageViewSetter(vies[0], 360,80, 30, 26);
        imageViewSetter(leggo, 0,0,165,90);
        leggo.setVisible(false);
        root.getChildren().addAll(score, imageView1, imageView2, imageView3, texte, description, leggo);
        return fishOn;
    }

    /**
     * Crée la scène leaderBoard (Meilleurs scores) et ses objets, et défini également leurs fonctions.
     * @return objet Scene leaderBoard.
     */
    private Scene leaderBoard(){
        Pane root = new Pane(canvas);
        Scene leaderBoard = new Scene(root, WIDTH, HEIGHT);

        description.setFill(Color.BLACK);

        Text votreNom = creerText(Color.BLACK, "Segoe UI", 12, 120, HEIGHT - 136);
        votreNom.setText("Votre nom : ");
        votreNom.setVisible(controleur.isGameOver() && !menuToLb);

        description = creerText(Color.BLACK, "Helvetica", 14, 0, HEIGHT-25);
        description.setText("");
        description.setTextAlignment(TextAlignment.CENTER);
        description.setWrappingWidth(WIDTH);

        textFieldScore = new TextField();
        textFieldScore.setPrefSize(140, 30);
        textFieldScore.setLayoutX(200);
        textFieldScore.setLayoutY(HEIGHT - 155);
        textFieldScore.setPromptText("Inscrivez votre nom");
        textFieldScore.setAlignment(Pos.CENTER);
        textFieldScore.setDisable(!controleur.isGameOver() && menuToLb);
        textFieldScore.setVisible(controleur.isGameOver() && !menuToLb);

        score = creerText(Color.BLACK, "Segoe UI", 12, 355, HEIGHT - 136);
        if(controleur.isGameOver() && !menuToLb){
            score.setText("a fait "+controleur.getScore()+" points!");
            score.setVisible(true);
        } else {
            score.setVisible(false);
        }

        ListView<String> top10 = new ListView<>();
        controleur.printScore(top10);
        top10.setLayoutY(65);
        top10.setLayoutX(50);
        top10.setPrefHeight(235);
        top10.setPrefWidth(540);
        texte = creerText(Color.BLACK, "Segoe UI", 40, WIDTH/2 - 150, 50);
        texte.setText("Meilleurs scores");

        btnScore = new Button("Ajouter");
        btnScore.setLayoutY(HEIGHT - 155);
        btnScore.setLayoutX(460);
        btnScore.setPrefSize(75, 30);
        btnScore.setDisable(!controleur.isGameOver() && menuToLb);
        btnScore.setVisible(controleur.isGameOver() && !menuToLb);
        btnScore.setOnMouseClicked(e -> {
            controleur.addScore(textFieldScore.getText(), controleur.getScore());
            top10.getItems().clear();
            controleur.printScore(top10);
            textFieldScore.setDisable(true);
            btnScore.setDisable(true);
            description.setText("");
        });

        btnPlay = new Button("Nouvelle partie!");
        btnPlay.setLayoutY(HEIGHT - 100);
        btnPlay.setLayoutX(92);
        btnPlay.setPrefSize(125, 50);
        btnPlay.setOnMouseClicked(e -> controleur.debutJeu());

        btnMenu = new Button("Menu principal");
        btnMenu.setLayoutY(HEIGHT - 100);
        btnMenu.setLayoutX(btnPlay.getLayoutX() + btnPlay.getPrefWidth() + 206);
        btnMenu.setPrefSize(125, 50);
        btnMenu.setOnMouseClicked(e -> {
            controleur.menu();
            description.setText("");
        });

        imageViewSetter(leggo, 0,0,165,90);
        leggo.setVisible(false);

        root.getChildren().addAll(texte, top10, btnPlay, description, btnMenu,
                                    textFieldScore, btnScore, votreNom, score, leggo);
        textFieldScore.requestFocus();
        textFieldScore.setOnKeyPressed(this::keyBinding);
        top10.setOnKeyPressed(this::keyBinding);
        leaderBoard.setOnKeyPressed(this::keyBinding);
        return leaderBoard;
    }

    /**
     * Mets à jour des éléments dynamiques dans les scènes.
     * @param deltaTime variation dans le temps calculée par AnimationTimer
     */
    private void updateFishHunt(double deltaTime){
        currentScene = controleur.getSwitchScene();
        String currentStr = Arrays.toString(currentScene);
        /* Pour une raison quelconque, si je comparais des boolean[] avec Arrays.equal,
        ça ne fontionnit qu'une seule fois*/
        if(!currentStr.equals(previousScene)){

            if(currentScene[0]) {
                primaryStage.setScene(menu());

            } else if(currentScene[1]){
                primaryStage.setScene(fishOn());

            } else if(currentScene[2]){
                primaryStage.setScene(leaderBoard());

            }
            previousScene = currentStr;
        }
        if(currentScene[0]) {
            if (btnPlay.isHover()) {
                description.setText("Commencer une nouvelle partie.");

            } else if (btnLeadBoard.isHover()) {
                description.setText("Consulter le tableau des meilleurs scores.");

            } else {
                description.setText("");
            }
        }

        if(currentScene[2]) {
            if (btnPlay.isHover()) {
                description.setText("Commencer une nouvelle partie.");

            } else if (btnMenu.isHover()) {
                description.setText("Retourner au menu principal");
            } else if (btnScore.isHover() && !btnScore.isDisable()){
                description.setText("Ajouter votre score au tableau.");
            }
        }

        if(currentScene[1]){
            score.setText(controleur.getScore()+"");
            for(int i = 0; i < 3 - controleur.getNbVies(); ++i){
                vies[i].setVisible(false);
            }
            for(int i = 2; i >= 3 - controleur.getNbVies(); --i){
                vies[i].setVisible(true);
            }
            if(controleur.isGameOver() && texte.getY() > HEIGHT/3f){
                texte.setY(texte.getY() - 150*deltaTime);
            } else if(controleur.isGameOver() && texte.getY() <= HEIGHT/3f){
                finCompteur += deltaTime;
                if(finCompteur >= 3){
                    try {
                        menuToLb = false;
                        controleur.leaderBoard();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    finCompteur = 0;
                }
            }
            if(!controleur.isNiveauAjour()){
                description.setText("Niveau " + controleur.getNiveau());
            } else {
                description.setText("");
            }
        }
        if(konami.equals("UUDDLRLRBA")){
            leggo.setVisible(!leggo.isVisible());
            konami = "XXXXXXXXXX";
        }
    }

    /**
     * Prend un objet ImageView et lui applique les paramètres demandés. Rend le code plus lisible.
     * @param iv objet ImageView
     * @param posX position horizontale
     * @param posY position verticale
     * @param width largeur souhaité du ImageView
     * @param height hauteur souhaitée du ImageView
     */
    private void imageViewSetter(ImageView iv, double posX, double posY, double width, double height){
        iv.setX(posX);
        iv.setY(posY);
        iv.setFitWidth(width);
        iv.setFitHeight(height);
    }
}

