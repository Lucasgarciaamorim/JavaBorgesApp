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
import java.util.Optional;

import static com.borgesnotas.controller.SheetsQuickstart.adicionarDadosFixos;

public class BorgesAppGuiController extends BorgesAppGuiApplication {

    private String numeroNota;
    private String cnpjEmpresa;


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
    void OnClickedConfirm(ActionEvent event) {


    }


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


    @FXML
    void confirmEnvio(ActionEvent event) {
        Sheets service = null;
        try {
            service = sheetsQuickstart.getSheetsService();
            String spreadsheetId = sheetsQuickstart.getSpreadsheetId();

            String chaveNFe = txtChaveDanfe.getText();
            String campoChaveNFe = "CHAVE DA NFE";

            String numeroNota = extractNFeNumber(chaveNFe);
            String cnpjEmpresa = extractCNPJ(chaveNFe);

            if (numeroNota != null && cnpjEmpresa != null) {
                adicionarDadosFixos(service, spreadsheetId, campoChaveNFe, chaveNFe, numeroNota, cnpjEmpresa);
               
            } else {
                // Trate o caso em que a extração de dados falha, por exemplo, exibindo uma mensagem de erro
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
        // Implemente a extração do número da nota fiscal a partir da chave aqui
        if (chave.length() == 44 && chave.matches("\\d+")) {
            return chave.substring(25, 34);
        }
        return null; // Retorne null se a extração falhar
    }

    private String extractCNPJ(String chave) {
        // Implemente a extração do CNPJ da empresa a partir da chave aqui
        if (chave.length() == 44 && chave.matches("\\d+")) {
            return chave.substring(6, 20);
        }
        return null; // Retorne null se a extração falhar


    }


    public static boolean onCloseQuery() {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Pergunta");
        alerta.setHeaderText("Deseja Sair?");
        ButtonType botaoNao = ButtonType.NO;
        ButtonType botaoSim = ButtonType.YES;
        alerta.getButtonTypes().setAll(botaoSim, botaoNao);
        Optional<ButtonType> opcaoClicada = alerta.showAndWait();

        return opcaoClicada.get() == botaoSim;

    }

    @FXML
    private TextField txtChaveDanfe;

//    @FXML
//    void initialize() {
//        // Adicione um ChangeListener ao TextField txtChaveDanfe
//        txtChaveDanfe.textProperty().addListener(new ChangeListener<String>() {
//            @Override
//            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
//                // Este código será executado sempre que o valor de txtChaveDanfe for alterado
//                System.out.println("Valor digitado: " + newValue);
//                // Você pode realizar a validação da chave aqui
//
//            }
//
//

}













