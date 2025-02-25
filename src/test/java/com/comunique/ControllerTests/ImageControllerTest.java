package com.comunique.ControllerTests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import com.comunique.controller.ImageController;
import com.comunique.functions.AleatoryString;
import com.comunique.functions.ModelCadastrosTests;
import com.comunique.functions.Reflections;
import com.comunique.model.Admins;
import com.comunique.model.Chat;
import com.comunique.model.Instituicoes;
import com.comunique.model.Usuarios;
import com.comunique.service.AdminsService;
import com.comunique.service.ChatService;
import com.comunique.service.ImageService;
import com.comunique.service.InstituicoesService;
import com.comunique.service.MensagensService;
import com.comunique.service.UsuariosService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ImageControllerTest {
    @Autowired
    ImageService imageService;
    @Autowired
    AdminsService adminsService;
    @Autowired
    UsuariosService usuariosService;
    @Autowired
    ChatService chatService;
    @Autowired
    MensagensService mensagensService;
    @Autowired
    InstituicoesService instituicoesService;
    @Autowired
    TestRestTemplate testRestTemplate;
    private Instituicoes instituicao;
    private Admins admin;
    private String senhaAdmin = AleatoryString.getAlphaNumericString(7);
    private Usuarios user1;
    private String senhaUser1 = AleatoryString.getAlphaNumericString(7);
    private Usuarios user2;
    private String senhaUser2 = AleatoryString.getAlphaNumericString(7);
    private Chat chat;
    private static String GlobalPath = "src/main/resources/static/images/";

    public static String removeSpecialCharacters(String email) {
        return email.replaceAll("[^a-zA-Z0-9]+", "");
    }

    @Before
    public void setUp() {
        this.instituicao = ModelCadastrosTests.CadastarInstituicoes(instituicoesService);
        this.admin = ModelCadastrosTests.CadastrarAdmin(instituicao, adminsService, senhaAdmin);
        this.user1 = ModelCadastrosTests.CadastrarUsuario(instituicao, usuariosService, senhaUser1);
        this.user2 = ModelCadastrosTests.CadastrarUsuario(instituicao, usuariosService, senhaUser2);
        this.chat = ModelCadastrosTests.CadastrarChat(user1, user2, chatService);
    }

    @After
    public void setDown() {
        mensagensService.DeleteForChat(chat);
        chatService.Deletar(chat);
        usuariosService.DeletarAllByInstituicao(instituicao);
        adminsService.DeletarAllByInstituicao(instituicao);
        instituicoesService.Deletar(instituicao.getIdInstituicao());
    }

    @Test
    public void mensagensImagensTest() throws IOException, NoSuchMethodException, SecurityException {
        Path imagePath = Paths.get("src/test/resources/images/test.jpg");

        FileSystemResource imageResource = new FileSystemResource(imagePath.toFile());
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image", imageResource);
        assertEquals(true, Files.exists(imagePath));
        String URI = Reflections.getURI(ImageController.class, "uploadChatImage");
        System.out.println(URI);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        String Mensagem = AleatoryString.getAlphaNumericString(7);
        String encoderMensagem = URLEncoder.encode(Mensagem, "UTF-8");
        ResponseEntity<Object> response = testRestTemplate.exchange(
                URI,
                HttpMethod.POST,
                requestEntity,
                Object.class, user1.getEmail(), senhaUser1, chat.getIdChat(), encoderMensagem);
        Path newPath = Paths.get(GlobalPath + user1.getInstituicao().getNome() + "/"
                + removeSpecialCharacters(chat.getIdChat().toString()) + "/" + imageResource.getFilename());
        assertEquals(true, Files.exists(newPath));
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

}
