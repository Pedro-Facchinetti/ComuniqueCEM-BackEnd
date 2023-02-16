package com.comunique.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.comunique.dto.InstituicoesDTO;
import com.comunique.model.Admins;
import com.comunique.model.AdminsMaster;
import com.comunique.model.Instituicoes;
import com.comunique.service.AdminsMasterService;
import com.comunique.service.AdminsService;
import com.comunique.service.InstituicoesService;

@RestController
@RequestMapping(value = "/Instituicoes", produces = { MediaType.APPLICATION_JSON_VALUE })
@CrossOrigin
public class InstituicoesController {

    @Autowired
    InstituicoesService instituicoesService;
    @Autowired
    AdminsMasterService adminsMasterService;
    @Autowired
    AdminsService adminsService;

    @GetMapping("/admin/{nomeAdmin}/{senhaAdmin}/{id}")
    public ResponseEntity<Object> findInstituicaoAdmin(@PathVariable(value = "nomeAdmin") String nomeAdmin,
            @PathVariable(value = "senhaAdmin") String senhaAdmin, @PathVariable(value = "id") UUID id) {
        Optional<Admins> admin = adminsService.Login(nomeAdmin, senhaAdmin);
        Optional<Instituicoes> instituicao = instituicoesService.getInstituicao(id);
        if (admin.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else if (instituicao.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {

            return new ResponseEntity<>(instituicao.get(), HttpStatus.OK);
        }
    }

    @GetMapping("/adminMaster/{nomeAdmin}/{senhaAdmin}/{id}")
    public ResponseEntity<Object> findInstituicaoAdminMaster(@PathVariable(value = "nomeAdmin") String nomeAdmin,
            @PathVariable(value = "senhaAdmin") String senhaAdmin, @PathVariable(value = "id") UUID id) {
        Optional<AdminsMaster> admin = adminsMasterService.Login(nomeAdmin, senhaAdmin);
        Optional<Instituicoes> instituicao = instituicoesService.getInstituicao(id);
        if (admin.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else if (instituicao.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {

            return new ResponseEntity<>(instituicao.get(), HttpStatus.OK);
        }
    }

    @GetMapping("/getAll/{nomeAdmin}/{senhaAdmin}")
    public ResponseEntity<Object> findAll(@PathVariable(value = "nomeAdmin") String nomeAdmin,
            @PathVariable(value = "senhaAdmin") String senhaAdmin) {
        Optional<AdminsMaster> admin = adminsMasterService.Login(nomeAdmin, senhaAdmin);
        if (admin.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            try {
                List<Instituicoes> listInstituicoes = instituicoesService.getAll();
                return new ResponseEntity<>(listInstituicoes, HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

            }
        }
    }

    @PostMapping("/{nomeAdmin}/{senhaAdmin}")
    public ResponseEntity<Object> adicionarInstituicao(@PathVariable(value = "nomeAdmin") String nomeAdmin,
            @PathVariable(value = "senhaAdmin") String senhaAdmin,
            @RequestBody @Valid InstituicoesDTO instituicaoDto) {
        Optional<AdminsMaster> admin = adminsMasterService.Login(nomeAdmin, senhaAdmin);
        if (admin.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            try {
                Instituicoes instituicao = new Instituicoes();
                BeanUtils.copyProperties(instituicaoDto, instituicao);
                Instituicoes instituicaoCadastro = instituicoesService.Cadastrar(instituicao);
                return new ResponseEntity<>(instituicaoCadastro, HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

            }
        }
    }

    @PutMapping("/{nomeAdmin}/{senhaAdmin}/{id}")
    public ResponseEntity<Object> alterarInstituicao(@PathVariable(value = "nomeAdmin") String nomeAdmin,
            @PathVariable(value = "senhaAdmin") String senhaAdmin, @PathVariable(value = "id") UUID id,
            @RequestBody @Valid InstituicoesDTO instituicaoDto) {
        Optional<Instituicoes> instituicao = instituicoesService.getInstituicao(id);
        Optional<AdminsMaster> admin = adminsMasterService.Login(nomeAdmin, senhaAdmin);
        if (admin.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else if (instituicao.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            try {
                BeanUtils.copyProperties(instituicaoDto, instituicao.get());
                Instituicoes instituicaoCadastro = instituicoesService.Cadastrar(instituicao.get());
                return new ResponseEntity<>(instituicaoCadastro, HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

            }
        }
    }

    @DeleteMapping("/{adminNome}/{senhaAdmin}/{id}")
    public ResponseEntity<Object> alterarInstituicao(@PathVariable(value = "nomeAdmin") String nomeAdmin,
            @PathVariable(value = "senhaAdmin") String senhaAdmin, @PathVariable(value = "id") UUID id) {
        Optional<Instituicoes> instituicao = instituicoesService.getInstituicao(id);
        Optional<AdminsMaster> admin = adminsMasterService.Login(nomeAdmin, senhaAdmin);
        if (admin.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else if (instituicao.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            try {
                instituicoesService.Deletar(instituicao.get().getIdInstituicao());
                return new ResponseEntity<>(HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

            }
        }
    }

}
