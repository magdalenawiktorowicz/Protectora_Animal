package es.studium.losamigosdeviky.ayuntamientos;

import java.util.ArrayList;

public interface AyuntamientoCallback {
    void onResult(ArrayList<Ayuntamiento> ayuntamientos);
    void onOperacionCorrectaUpdated(boolean resultado); // Keep this if you need it for other operations
}