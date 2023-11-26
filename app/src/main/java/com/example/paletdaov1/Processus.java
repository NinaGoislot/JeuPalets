package com.example.paletdaov1;

public class Processus implements Runnable {
    private boolean isRunning = false;
    private CanvasView canvasView;
    public Processus(CanvasView canvasView) {
        this.canvasView = canvasView;
    }

    @Override
    public void run() {
        while (isRunning) {
            // Redessinez le jeu avec les nouvelles positions des éléments
            canvasView.drawGameElements();
            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}



