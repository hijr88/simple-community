package me.yh.community.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "persistent_logins")
@Entity
public class PersistentLogins {

    @Id
    @Column(length = 64)
    private String series;

    @Column(length = 50, nullable = false)
    private String username;

    @Column(length = 64, nullable = false)
    private String token;

    @Column(length = 64, nullable = false)
    private String lastUsed;
}
