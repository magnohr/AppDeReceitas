package com.example.appdereceita2.Model;

public class Ingredientes {
    private int      image1;
    private String   NomeReceita;
    private String   ingre1;
    private String   ingre2;
    private String   ingre3;
    private String   ingre4;
    private String   ingre5;
    private String   ingre6;
    private String   link1;

    public int getImage1() {
        return image1;
    }

    public void setImage1(int image1) {
        this.image1 = image1;
    }

    public String getNomeReceita(){
        return NomeReceita;
    }

    public void setNomeReceita(String NomeReceita){
        this.NomeReceita = NomeReceita;
    }

    public String getIngre1() {
        return ingre1;
    }

    public void setIngre1(String ingre1) {
        this.ingre1 = ingre1;
    }

    public String getIngre2() {
        return ingre2;
    }

    public void setIngre2(String ingre2) {
        this.ingre2 = ingre2;
    }

    public String getIngre3() {
        return ingre3;
    }

    public void setIngre3(String ingre3) {
        this.ingre3 = ingre3;
    }

    public String getIngre4() {
        return ingre4;
    }

    public void setIngre4(String ingre4) {
        this.ingre4 = ingre4;
    }

    public String getIngre5() {
        return ingre5;
    }

    public void setIngre5(String ingre5) {
        this.ingre5 = ingre5;
    }

    public String getIngre6() {
        return ingre6;
    }

    public void setIngre6(String ingre6) {
        this.ingre6 = ingre6;
    }

    public String getLink1() {
        return link1;
    }

    public void setLink1(String link1) {
        this.link1 = link1;
    }

    public Ingredientes(int image1,String NomeReceita, String ingre1, String ingre2, String ingre3, String ingre4, String ingre5, String ingre6, String link1) {
        this.image1 = image1;
        this.NomeReceita = NomeReceita;
        this.ingre1 = ingre1;
        this.ingre2 = ingre2;
        this.ingre3 = ingre3;
        this.ingre4 = ingre4;
        this.ingre5 = ingre5;
        this.ingre6 = ingre6;
        this.link1 = link1;



    }
}
