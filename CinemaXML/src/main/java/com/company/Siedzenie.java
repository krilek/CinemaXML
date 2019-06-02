package com.company;

import java.util.Scanner;

public class Siedzenie {
    public String rzad;
    public String numerSiedzenia;
    public static Siedzenie NoweSiedzenie(){
        Siedzenie siedzenie = new Siedzenie();
        Scanner s = new Scanner(System.in);
        System.out.print("Podaj rzad:");
        siedzenie.rzad = s.next();
        System.out.print("Podaj numer siedzenia:");
        siedzenie.numerSiedzenia = s.next();
        return siedzenie;
    }
}
