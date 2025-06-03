package org.example.consumonutrireceitas.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.consumonutrireceitas.model.Receita;
import org.example.consumonutrireceitas.session.UsuarioSession;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

public class ReceitaController {

    @FXML private TextField nomeField;
    @FXML private TextField ingredientesField;
    @FXML private TextField serveField;
    @FXML private TextField descricaoField;
    @FXML private TextField tempoPreparoField;

    @FXML private TableView<Receita> tabelaReceitas;
    @FXML private TableColumn<Receita, String> nomeColumn;
    @FXML private TableColumn<Receita, String> ingredientesColumn;
    @FXML private TableColumn<Receita, Integer> serveColumn;
    @FXML private TableColumn<Receita, String> descricaoColumn;
    @FXML private TableColumn<Receita, String> tempoColumn;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ObservableList<Receita> receitas = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Configura colunas
        nomeColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));
        ingredientesColumn.setCellValueFactory(new PropertyValueFactory<>("ingredientes"));
        serveColumn.setCellValueFactory(new PropertyValueFactory<>("serve"));
        descricaoColumn.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        tempoColumn.setCellValueFactory(new PropertyValueFactory<>("tempoPreparo"));

        tabelaReceitas.setItems(receitas);

        carregarReceitas();
    }

    @FXML
    private void salvarReceita() {
        Receita nova = new Receita();
        nova.setNome(nomeField.getText());
        nova.setIngredientes(ingredientesField.getText());
        nova.setServe(Integer.valueOf(serveField.getText()));
        nova.setDescricao(descricaoField.getText());
        nova.setTempoPreparo(Integer.valueOf(tempoPreparoField.getText()));
        nova.setUsuarioId(UsuarioSession.getInstance().getUsuario().getId());

        try {
            URL url = new URL("http://localhost:8080/receita/salvar");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                objectMapper.writeValue(os, nova);
            }

            if (conn.getResponseCode() == 201 || conn.getResponseCode() == 200) {
                carregarReceitas();
                limparCampos();
            } else {
                exibirAlerta("Erro ao salvar", "Código: " + conn.getResponseCode());
            }

        } catch (IOException e) {
            e.printStackTrace();
            exibirAlerta("Erro", "Erro ao enviar requisição.");
        }
    }

    private void carregarReceitas() {
        try {
            int usuarioId = UsuarioSession.getInstance().getUsuario().getId();
            URL url = new URL("http://localhost:8080/receita/usuario/" + usuarioId);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() == 200) {
                List<Receita> lista = objectMapper.readValue(
                        conn.getInputStream(), new TypeReference<List<Receita>>() {}
                );
                receitas.setAll(lista);
            } else {
                exibirAlerta("Erro", "Erro ao carregar receitas. Código: " + conn.getResponseCode());
            }

        } catch (IOException e) {
            e.printStackTrace();
            exibirAlerta("Erro", "Falha ao obter receitas.");
        }
    }

    private void limparCampos() {
        nomeField.clear();
        ingredientesField.clear();
        descricaoField.clear();
        tempoPreparoField.clear();
        serveField.clear();
    }

    private void exibirAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
