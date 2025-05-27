package com.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "credential")
@EntityListeners(AuditingEntityListener.class)
public class Credential {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;
    @Column(nullable = false)
    private String passwordHash;
    @Column(name = "last_modified_date")
    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdatedOn;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    @JsonIgnore
    private User user;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Credential that = (Credential) o;
        return Objects.equals(id, that.id) && Objects.equals(passwordHash, that.passwordHash) && Objects.equals(lastUpdatedOn, that.lastUpdatedOn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, passwordHash, lastUpdatedOn);
    }
}