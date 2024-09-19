package com.progmoviles.appusersqlite;

import static java.lang.Double.parseDouble;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    //Instaneciar los objetos que tienen ids en el archivo activity_main
    EditText etNombre, etcodigo, etcoste;
    Spinner spDisponibilidad;
    ImageButton ibSave, ibSearch;

    // Definir el arreglo para los roles
    String[] arole = {"Estado","Disponible","No Disponible"};

    //Instanciar la clase bdUser para que se pueda utilizar en los diferentes metodos
    bdUsers baseUsers = new bdUsers(this, "dbUsers", null, 1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Referenciar los objetos instanciaos con sus ids respectivos
        etNombre = findViewById(R.id.etnombre);
        etcodigo = findViewById(R.id.etcodigo);
        etcoste = findViewById(R.id.etcoste);
        spDisponibilidad = findViewById(R.id.spdisponibilidad);
        ibSave = findViewById(R.id.ibsave);
        ibSearch = findViewById(R.id.ibsearch);

        //Llenar el spinner que mostrara los roles
        spDisponibilidad.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,arole));

        //Eventos
        ibSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Buscar el email
                SQLiteDatabase dbusersread = baseUsers.getReadableDatabase();
                String sql = "Select nombre, codigo, coste, role from user where nombre = '"+etNombre.getText().toString()+"'";
                Cursor crUser = dbusersread.rawQuery(sql, null);
                if (crUser.moveToFirst()){ //Encuentra el usuario a traves del email
                    //Llenar los datos en el formulario
                    etcodigo.setText(crUser.getString(1));
                    etcoste.setText(crUser.getString(2));
                    spDisponibilidad.setSelection(crUser.getString(3).equals("Si") ? 1 : 2);
                }else{
                    Toast.makeText(getApplicationContext(), "El libro no existe o no esta registrado. Intenta con otro", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ibSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //validar que toda la informacion este diligenciada
                String nombre = etNombre.getText().toString();
                String codigo = etcodigo.getText().toString();
                String coste = etcoste.getText().toString();
                String role = spDisponibilidad.getSelectedItem().toString();
                if(!nombre.isEmpty() && !codigo.isEmpty() && !coste.isEmpty() && !role.equals("Disponibilidad")){
                    saveUser(nombre, codigo, coste, role);
                }else{
                    //Notificacion para el usuario
                    Toast.makeText(getApplicationContext(), "Debes ingresar todos los datos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveUser(String nombre, String codigo, String coste, String role) {
        //Buscar el nombre
        SQLiteDatabase dbusersread = baseUsers.getReadableDatabase();

        //Crear tabla cursor con los datos del usuario que se busca por nombre
        String query = "Select nombre from user where nombre = '"+nombre+"'";
        Cursor cUsers = dbusersread.rawQuery(query, null);
        if (!cUsers.moveToFirst()){ //No lo encuentra
                SQLiteDatabase dbuserswrite = baseUsers.getWritableDatabase();
                //Crear tabla con valores para agregar el registro
                ContentValues cvUsers = new ContentValues();
                cvUsers.put("nombre", nombre);
                cvUsers.put("codigo", codigo);
                cvUsers.put("coste", coste);
                cvUsers.put("role", role);
                //Agregar el registro a traves del contentvalues cvUsers
                dbuserswrite.insert("user",null,cvUsers);
                dbuserswrite.close();
                //Mensaje para el usuario que se guarda exitosamente
                Toast.makeText(this, "Libro agregado exitosamente", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Libro existente. Intente con otro", Toast.LENGTH_SHORT).show();
        }
        dbusersread.close();
    }
}