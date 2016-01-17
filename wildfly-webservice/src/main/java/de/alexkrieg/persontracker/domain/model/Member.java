package de.alexkrieg.persontracker.domain.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(insertable=false, updatable=false,
           columnDefinition="BigSerial not null")
    private Long id;

    @NotNull
    @Size(min = 1, max = 25)
    @Pattern(regexp = "[A-Za-z ]*", message = "must contain only letters and spaces")
    private String name;

    /** using hibernate5 validators **/
    @NotNull
    @NotEmpty
    @Email
    private String email;

    @NotNull
    @Size(min = 9, max = 12)
    @Digits(fraction = 0, integer = 12)
    @Column(name = "phone_number")
    private String phoneNumber;

    private String address;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public static final class Builder {

        private final String email;
        private String name;
        private String address;
        private String phoneNumber;

        public Builder(final String email) {
            Validate.notNull(email, "Email is required");
            this.email = email;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder livingIn(String address) {
            this.address = address;
            return this;
        }

        public Builder hasPhone(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public Member build() {
            Member member = new Member();
            member.setEmail(email);
            member.setAddress(address);
            member.setName(name);
            member.setPhoneNumber(phoneNumber);
            return member;
        }

    }

}