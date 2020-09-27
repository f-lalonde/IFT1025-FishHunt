import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class LeaderBoard {
    Map<Integer, String> scoreMap;

    String nomsEgaux;
    String scoreFile = "src/highScore.dat";

    ObjectOutputStream objOut;
    ObjectInputStream objIn;

    public LeaderBoard() throws IOException, ClassNotFoundException {
        // Vérifie l'existence du fichier, sinon le crée et y ajoute une valeur.
        File scoreChk = new File(scoreFile);
        if(scoreChk.createNewFile()){
            Map<Integer, String> temp = new TreeMap<>();
            temp.put(0, "Tata Boutlamine");
            objOut = new ObjectOutputStream(new FileOutputStream(scoreFile));
            objOut.writeObject(temp);
            objOut.flush();
            objOut.close();
        }
        scoreMap = readScore();
    }

    /**
     * Ajoute le nom saisi par l'utilisateur et son score au fichier des meilleurs scores.
     * Si le nombre de scores enregistré est plus grand que 10, on efface le score le plus bas, ou
     * le plus ancien, dans cet ordre de priorité.
     * @param nom Nom saisi par l'utilisateur
     * @param score Score de l'utilisateur
     */
    public void addScore(String nom, int score){

        // ajoute le nom au score. Si la valeur de score existait déjà, le nouveau nom se trouve dans les plus anciens
        nomsEgaux = scoreMap.put(score, nom);
        if(nomsEgaux != null){
            nomsEgaux = scoreMap.get(score) + "##;##" + nomsEgaux;
            scoreMap.put(score, nomsEgaux);
        }

        // On veut limiter la liste à 10 noms max. On commence par calculer le nombre de noms dans la map après l'ajout

        int size = 0;
        Object[] keySet = scoreMap.keySet().toArray();
        for (Object o : keySet) {
            // on utilise ##;## comme séparateur (choix arbitraire)
            if (scoreMap.get((int)o).contains("##;##")) {
                size += scoreMap.get((int)o).split("##;##").length;
            } else {
                size++;
            }
        }

        // Si size <= 10, alors on a terminé. Sinon, on doit enlever le plus petit score.
        // Si il y a plusieurs entrées avec le même score à la dernière position, on doit enlever le plus ancien,
        // qui se trouve après le dernier séparateur ##;##
        if(size>10){
            while(size>10) {
                if (scoreMap.get((int)keySet[0]).contains("##;##")) {
                    String[] currentVal = scoreMap.get((int)keySet[0]).split("##;##");
                    String oneLessEntry = "";
                    for (int i = 0; i < currentVal.length-1; ++i) {

                        if (i == currentVal.length - 2) {
                            oneLessEntry = oneLessEntry.concat(currentVal[i]);
                        } else {
                            oneLessEntry = oneLessEntry.concat(currentVal[i] + "##;##");
                        }
                    }
                    scoreMap.put((int)keySet[0], oneLessEntry);

                } else {
                    scoreMap.remove((int)keySet[0]);
                }
                size--;
                keySet = scoreMap.keySet().toArray();
            }
        }
        saveScore();
    }

    /**
     * Enregistre l'objet Map sérialisé dans un fichier
     */
    private void saveScore(){
        try{
            objOut = new ObjectOutputStream(new FileOutputStream(scoreFile));
            objOut.writeObject(scoreMap);
            objOut.flush();
            objOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Va lire l'objet Map dans le fichier
     * @return l'objet Map rétabli
     */
    private Map<Integer, String> readScore(){
        try {
            objIn = new ObjectInputStream(new FileInputStream(scoreFile));
            scoreMap = (TreeMap<Integer, String>)objIn.readObject();
            objIn.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return scoreMap;
    }

    /**
     * classe les score en ordre du plus grand au plus petit et les met dans le listView en string
     * @param listView le listView du leaderboard
     */
    public void printScore(ListView<String> listView){

        scoreMap = readScore();
        ArrayList<Integer> keyList = new ArrayList<>(scoreMap.keySet());
        int temp = 0;
        for (int i=Math.min(keyList.size()-1, 9); i>=0; --i) {
            String nom = scoreMap.get(keyList.get(i));
            if(nom.contains("##;##")){
                String[] nomsMultiple = nom.split("##;##");

                for (String s : nomsMultiple) {
                    listView.getItems().add("#" + (keyList.size() - i + temp) + " - " + s + " - " + keyList.get(i));
                    temp++;
                }
                temp--;
            } else {
                listView.getItems().add("#" + (keyList.size() - i + temp) + " - " + nom + " - " + keyList.get(i));
            }
        }
    }

    public void draw(GraphicsContext context){
        context.setFill(Color.PAPAYAWHIP);
        context.fillRect(0, 0, FishHunt.WIDTH, FishHunt.HEIGHT);

    }

}

