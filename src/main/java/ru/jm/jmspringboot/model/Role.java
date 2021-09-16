package ru.jm.jmspringboot.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

import java.util.Set;


@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "roleid", nullable = false)
    private Long id;

    private String roleName;

    /*
    @Transient
    @ManyToMany(mappedBy = "roles")
    private Set<User> user;
*/
      @Override
     public String toString(){
        return this.roleName;
    }

    @Override
    public String getAuthority() {
        return roleName;
    }

}
