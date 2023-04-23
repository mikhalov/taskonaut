package com.mikhalov.taskonaut.model;

import com.mikhalov.taskonaut.model.enums.UserRole;
import io.hypersistence.utils.hibernate.type.basic.PostgreSQLEnumType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Table(name = "users")
@TypeDef(name = "pgsql_enum", typeClass = PostgreSQLEnumType.class)
public class User {

    @Id @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)

    private String password;

    @Column(name = "telegram_chat_id")
    private Long telegramChatId;

    @Enumerated(EnumType.STRING)
    @Type(type = "pgsql_enum")
    private UserRole role;

    public void removeChatId() {
        this.telegramChatId = null;
    }
}
