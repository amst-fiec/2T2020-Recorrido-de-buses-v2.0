package Clases;

public class User {

    private int tipo;
    private String email;
    private String name;
    private int edad;
    private String photo;
    private String pass;



    public User() {
        tipo=1;
        email="";
        name="";
        edad=0;
        photo="";
        pass="";
    }

    public int getTipo() {
        return tipo;
    }
    public String getEmail() {
        return email;
    }


    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
