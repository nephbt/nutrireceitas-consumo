package org.example.consumonutrireceitas.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class CadastroController {

    @FXML
    private TextField nomeField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField senhaField;
    @FXML
    private Button loginButton;
    @FXML
    public void initialize() {
        loginButton.setOnAction(event -> cadastrarUsuario());
    }

    private void cadastrarUsuario() {
        String nome = nomeField.getText();
        String email = emailField.getText();
        String senha = senhaField.getText();

        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
            mostrarAlerta("Erro!", "Todos os campos devem ser preenchidos.");
            return;
        }

        try {
            URL url = new URL("http://localhost:8080/usuario/salvar");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String jsonInputString = String.format(
                    "{\"nome\": \"%s\", \"email\": \"%s\", \"senha\": \"%s\"}",
                    nome, email, senha
            );

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_CREATED || responseCode == HttpURLConnection.HTTP_OK) {
                mostrarAlertaSucesso("Feito", "Cadastro realizado com sucesso");

                nomeField.clear();
                emailField.clear();
                senhaField.clear();

            } else if (responseCode == HttpURLConnection.HTTP_CONFLICT) {
                mostrarAlerta("Email já cadastrado", "O email informado já está em uso. Tente outro.");
            } else {
                mostrarAlerta("Erro ao cadastrar", "Código de erro: " + responseCode);
            }

            conn.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void mostrarAlertaSucesso(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

}
