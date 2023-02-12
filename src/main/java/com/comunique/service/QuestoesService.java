package com.comunique.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.comunique.model.Questoes;
import com.comunique.repository.QuestoesRepository;

import jakarta.transaction.Transactional;

@Service
public class QuestoesService {
    @Autowired
    QuestoesRepository questoesRepository;

    @Transactional
    public Questoes Cadastrar(Questoes questoes) {
        return questoesRepository.save(questoes);
    }

    public Optional<Questoes> getQuestao(UUID idQuestao) {
        return questoesRepository.findById(idQuestao);
    }

    public List<Questoes> getAllQuestoes() {
        return questoesRepository.findAll();
    }

    public List<Questoes> findRandomRowsLimited(int limit) {
        return questoesRepository.findRandomRowsLimited(limit);
    }

    @Transactional
    public void Deletar(UUID questao) {
        questoesRepository.deleteById(questao);
    }
}
