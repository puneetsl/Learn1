package me.puneetsingh.learn1.bean;


public class Word {


    private int id = 0;
    private String word;
    private String meaning;
    private String POS;
    private String example;
    private double difScore;
    private double lmScore;
    private double score;

    public String getWord() {
        return word;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public String getPOS() {
        return POS;
    }

    public void setPOS(String POS) {
        this.POS = POS;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public double getDifScore() {
        return difScore;
    }

    public void setDifScore(double difScore) {
        this.difScore = difScore;
    }

    public double getLmScore() {
        return lmScore;
    }

    public void setLmScore(double lmScore) {
        this.lmScore = lmScore;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
