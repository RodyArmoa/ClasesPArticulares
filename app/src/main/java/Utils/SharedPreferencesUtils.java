package Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtils {
    private static final String PREF_NAME = "AlumnoPreferences";
    private static final String KEY_ALUMNO_ID = "alumno_id";

    // Método para guardar la ID del alumno en SharedPreferences
    public static void guardarIdAlumno(Context context, Long idAlumno) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(KEY_ALUMNO_ID, idAlumno);
        editor.apply();
    }

    // Método para obtener la ID del alumno desde SharedPreferences
    public static Long obtenerIdAlumno(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getLong(KEY_ALUMNO_ID, -1); // -1 es el valor predeterminado si la clave no existe
    }
}
