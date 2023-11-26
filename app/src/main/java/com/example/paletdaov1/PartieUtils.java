package com.example.paletdaov1;

public class PartieUtils {
    private static Partie partie;

    public static void setPartie(Partie partie) {
        PartieUtils.partie = partie;
    }

    public static Partie getPartie() {
        return partie;
    }
}
