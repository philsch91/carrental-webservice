package at.fhcampuswien.sde.carrentalwebservice.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Column;

@Entity
public class User {

    @Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "uid", updatable = false, nullable = false)
    private long id;
    private String name;
    private String password;

    protected User(){}

    public User(String name){
        this.name = name;
    }

    public User(long id, String name){
        this.id = id;
        this.name = name;
    }

    public User(long id, String name, String password){
        this.id = id;
        this.name = name;
        this.password = password;
    }

    public long getId() {
        return this.id;
    }
    public void setId(Long id){
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return String.format("User[id=%d, name='%s', password='%s']",id , name, password);
    }
}
