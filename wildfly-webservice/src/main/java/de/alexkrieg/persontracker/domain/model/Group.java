package de.alexkrieg.persontracker.domain.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.validator.constraints.NotEmpty;

/*The Model uses JPA Entity as well as Hibernate Validators
 *
 */

@Entity
@Table(name = "TGroup", uniqueConstraints = @UniqueConstraint(columnNames = "group_id") )
public class Group implements Serializable {
    /** Default value included to remove warning. Remove or modify at will. **/
    private static final long serialVersionUID = 1L;

    @SequenceGenerator(allocationSize = 1, initialValue = 1, sequenceName = "group_id_seq", name = "group_id_seq")
    @GeneratedValue(generator = "group_id_seq", strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "group_id")
    private Long id;

    /** using hibernate5 validators **/
    @NotNull
    @NotEmpty
    @Size(max = 40)
    private String name;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "groups")
    private Set<Member> members = new HashSet<Member>();

    public Set<Member> getMembers() {
        return this.members;
    }

    public void setMembers(Set<Member> members) {
        this.members = members;
    }

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
    

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this,new String[] {"members"});
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj, new String[] {"members"});
    }



    public static final class Builder {

        private final String name;
        private Set<Member> members = new HashSet<Member>();

        public Builder(final String name) {
            Validate.notNull(name, "Name is required");
            this.name = name;
        }

        public Builder withMembers(List<Member> members) {
            Validate.notNull(members," member must not be null");
            Validate.notEmpty(members," member must not be empty ");
            Validate.noNullElements(members);
            this.members.addAll(members);
            return this;
        }
        
        public Builder withMembers(Member... member) {
            return withMembers(Arrays.asList(member));
        }

        public Group build() {
            Group group = new Group();
            group.setName(name);
            group.setMembers(members);
            return group;
        }

    }

}