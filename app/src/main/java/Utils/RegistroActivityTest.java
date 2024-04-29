package Utils;


import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import com.rodyandroid.clasesparticulares.Registro.RegistroActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


//@RunWith(RobolectricTestRunner.class)
/*public class RegistroActivityTest {

    private RegistroActivity activity;
    private Button btnRegistrar;
    private EditText editTextNombre, editTextApellido, editTextEmailRegistro, editTextContraseñaRegistro, editTextRepetirContraseña;

    @Before
    public void setUp() {
        activity = Robolectric.buildActivity(RegistroActivity.class)
                .create()
                .resume()
                .get();

        btnRegistrar = activity.findViewById(R.id.btnRegistrar);
        editTextNombre = activity.findViewById(R.id.editTextRegistroNombre);
        editTextApellido = activity.findViewById(R.id.editTextregistroApellido);
        editTextEmailRegistro = activity.findViewById(R.id.editTextEmailRegistro);
        editTextContraseñaRegistro = activity.findViewById(R.id.editTextContraseñaRegistro);
        editTextRepetirContraseña = activity.findViewById(R.id.editTextreptirContraseña);
    }

    @Test
    public void registrarUsuario_CamposVacios_MuestraToast() {
        // Simular que los campos están vacíos
        editTextNombre.setText("");
        editTextApellido.setText("");
        editTextEmailRegistro.setText("");
        editTextContraseñaRegistro.setText("");
        editTextRepetirContraseña.setText("");

        // Simular clic en el botón de registro
        btnRegistrar.performClick();

        // Verificar que se muestra el Toast correcto
        assertEquals("Por favor complete todos los campos", ShadowToast.getTextOfLatestToast());
    }
}*/