package org.example.consumonutrireceitas.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.consumonutrireceitas.model.Usuario;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.consumonutrireceitas.session.UsuarioSession;

public class InicioController {

    @FXML
    private TextField emailField;
    @FXML private TextField senhaField;
    @FXML private Button loginButton;
    @FXML private Button cadastreButton;

    @FXML
    public void initialize(){

        loginButton.setOnAction(event -> {
            try {
                login();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        cadastreButton.setOnAction(actionEvent -> {
            try {
                abrirTelaCadastro();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void login() throws IOException {
        String email = emailField.getText();
        String senha = senhaField.getText();

        if (email.isEmpty() || senha.isEmpty()) {
            mostrarAlerta("Erro de Login", "Email e senha devem ser preenchidos.");
            return;
        }

        String jsonBody = String.format("{\"email\":\"%s\", \"senha\":\"%s\"}", email, senha);

        URL url = new URL("http://localhost:8080/usuario/login");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonBody.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int responseCode = conn.getResponseCode();

        InputStream is = (responseCode == 200) ? conn.getInputStream() : conn.getErrorStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
        StringBuilder response = new StringBuilder();
        String responseLine;
        while ((responseLine = br.readLine()) != null) {
            response.append(responseLine.trim());
        }

        if (responseCode == 200) {
            ObjectMapper mapper = new ObjectMapper();
            Usuario usuario = mapper.readValue(response.toString(), Usuario.class);
            UsuarioSession.getInstance().setUsuario(usuario);
            abrirReceitas(usuario);
        } else if (responseCode == 401) {
            mostrarAlerta("Erro de Login", "Email ou senha incorretos.");
        } else {
            mostrarAlerta("Erro", "Erro ao conectar com o servidor. Código: " + responseCode);
        }

        conn.disconnect();


    }

    private void abrirReceitas(Usuario usuario) throws IOException {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/consumonutrireceitas/tela-receitas.fxml"));
            Parent root = loader.load();


            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

            ((Stage) emailField.getScene().getWindow()).close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void abrirTelaCadastro() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/consumonutrireceitas/tela-cadastro.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
