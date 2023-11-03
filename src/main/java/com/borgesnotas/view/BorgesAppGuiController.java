package com.borgesnotas.view;

import com.borgesnotas.controller.SheetsQuickstart;
import com.google.api.services.sheets.v4.Sheets;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static com.borgesnotas.controller.SheetsQuickstart.adicionarDadosFixos;

public class BorgesAppGuiController extends BorgesAppGuiApplication {


    private SheetsQuickstart sheetsQuickstart;

    @FXML
    private ComboBox<?> cbLoja;

    @FXML
    private DatePicker dtDatePicker;

    @FXML
    private Label lblDetailNumberNota;

    @FXML
    private TextField txtName;

    @FXML
    private VBox pnlPrincipal;

    @FXML
    private AnchorPane AncherBottom;

    @FXML
    private Button btnClear;

    @FXML
    private Label lblDetailLoja;

    @FXML
    private Button btnConfirm;

    @FXML
    private Label lblDetailDataEmissao;

    @FXML
    private Label lblName;


    @FXML
    private AnchorPane pnlTopPart;

    @FXML
    private Label lblDetailCnpjFornecedor;

    @FXML
    private Label lblDetailChaveDanfe;

    @FXML
    private Label lblDetailNotaFiscal;

    @FXML
    private Label lblLoja;

    @FXML
    private Label lblChaveDanfe;

    @FXML
    private Label lblDate;

    @FXML
    private SplitPane pnlSplitPlane;

    @FXML
    private Label lblMarca;

    @FXML
    private ComboBox<?> cbMarca;

    @FXML
    private Font x1;

    @FXML
    private HBox pnlBottom;

    @FXML
    private CheckBox checkDanfe;

    @FXML
    private TextField txtChaveDanfe;


    public BorgesAppGuiController() {
        sheetsQuickstart = new SheetsQuickstart();
    }

    public boolean validateExtractNFeNumberAndCNPJ(String chave, StringBuilder numeroNotaFiscal, StringBuilder cnpj) {
        if (chave.length() != 44) {
            return false;
        }

        if (!chave.matches("\\d+")) {
            return false;
        }

        int digitoVerificador = Integer.parseInt(chave.substring(43));
        String chaveSemDigito = chave.substring(0, 43);

        int peso = 2;
        int soma = 0;

        for (int i = chaveSemDigito.length() - 1; i >= 0; i--) {
            int digito = Integer.parseInt(chaveSemDigito.substring(i, i + 1));
            soma += digito * peso;
            peso = peso == 9 ? 2 : peso + 1;
        }

        int resto = soma % 11;
        int digitoCalculado = 11 - resto;

        if (digitoCalculado >= 10) {
            digitoCalculado = 0;
        }

        if (digitoCalculado == digitoVerificador) {
            numeroNotaFiscal.append(chave, 25, 34);
            cnpj.append(chave, 6, 20);
            return true; // Chave válida
        }
        return false; // Chave inválida
    }

    DatePicker datePicker = new DatePicker();


    @FXML
    void confirmEnvio(ActionEvent event) {
        Sheets service = null;
        try {
            service = sheetsQuickstart.getSheetsService();
            String spreadsheetId = sheetsQuickstart.getSpreadsheetId();

            String chaveNFe = txtChaveDanfe.getText();
            String campoChaveNFe = "CHAVE DA NFE";


            String Name = txtName.getText();
            String numeroNota = extractNFeNumber(chaveNFe);
            String cnpjEmpresa = extractCNPJ(chaveNFe);

            LocalDate selectedDate = dtDatePicker.getValue();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String formattedDate = selectedDate.format(formatter);


            if (numeroNota != null && cnpjEmpresa != null && Name != null && formattedDate != null) {
                adicionarDadosFixos(service,
                        spreadsheetId,
                        campoChaveNFe,
                        chaveNFe,
                        numeroNota,
                        cnpjEmpresa,
                        Name,
                        formattedDate);


            } else {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erro de Extração de Dados");
                alert.setHeaderText("Não foi possível extrair os dados da NFe");
                alert.setContentText("Por favor, verifique a chave da NFe e tente novamente.");
                alert.showAndWait();
            }
        } catch (IOException | GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    private String extractNFeNumber(String chave) {

        if (chave.length() == 44 && chave.matches("\\d+")) {
            return chave.substring(25, 34);
        }
        return null;
    }

    private String extractCNPJ(String chave) {
        if ("88379771004170".equals(chave)) {
            return "MODARE";
        }

        if (chave.length() == 44 && chave.matches("\\d+")) {
            return chave.substring(6, 20);

        }
        return null;
    }

    public static boolean onCloseQuery() {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Exit");
        alerta.setHeaderText("Deseja Sair?");
        ButtonType botaoNao = ButtonType.NO;
        ButtonType botaoSim = ButtonType.YES;
        alerta.getButtonTypes().setAll(botaoSim, botaoNao);
        Optional<ButtonType> opcaoClicada = alerta.showAndWait();

        return opcaoClicada.get() == botaoSim;

    }

}