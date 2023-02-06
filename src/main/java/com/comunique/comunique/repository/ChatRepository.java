package com.comunique.comunique.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.comunique.comunique.model.Chat;
import com.comunique.comunique.model.Usuarios;
@Repository
public interface ChatRepository extends JpaRepository<Chat, Usuarios> {

}
