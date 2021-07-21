package com.miccha.server.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Table("email_change")
@AllArgsConstructor
public class EmailChangeRequest implements Persistable<String> {
    @Id
    private String token;
    private Long userId;
    private String email;

    @Override
    public String getId() {
        return token;
    }

    @Override
    public boolean isNew() {
        return true;
    }
}
