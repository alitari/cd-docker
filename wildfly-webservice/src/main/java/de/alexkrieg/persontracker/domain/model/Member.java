package de.alexkrieg.persontracker.domain.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang.Validate;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

/*The Model uses JPA Entity as well as Hibernate Validators
 *
 */

@Entity
@Table(name = "Member", uniqueConstraints = @UniqueConstraint(columnNames = "id") )
public class Member implements Serializable {
    /** Default value included to remove warning. Remove or modify at will. **/
    private static final long serialVersionUID = 1L;

    @SequenceGenerator(allocationSize = 1, initialValue = 1, sequenceName = "member_id_seq", name = "member_id_seq")
    @GeneratedValue(generator = "member_id_seq", strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Long id;

    /** using hibernate5 validators **/
    @NotNull
    @NotEmpty
    @Email
    private String email;

    @NotNull
    @Size(min = 8, max = 12)
    @Column(name = "password")
    private String password;

    @Size(max = 40)
    private String authToken;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String passsword) {
        this.password = passsword;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public static final class Builder {

        private final String email;
        private String password;
        private String authToken;

        public Builder(final String email) {
            Validate.notNull(email, "Email is required");
            this.email = email;
        }

        public Builder withPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder authorize(String authToken) {
            this.authToken = authToken;
            return this;
        }

        public Member build() {
            Member member = new Member();
            member.setEmail(email);
            member.setPassword(password);
            member.setAuthToken(authToken);
            return member;
        }

    }

}