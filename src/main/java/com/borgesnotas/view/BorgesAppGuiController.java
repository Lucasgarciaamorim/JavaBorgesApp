package com.borgesnotas.view;

import com.borgesnotas.controller.SheetsQuickstart;
import com.google.api.services.sheets.v4.Sheets;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

@SuppressWarnings("OptionalGetWithoutIsPresent")
public class BorgesAppGuiController extends BorgesAppGuiApplication {


    private final SheetsQuickstart sheetsQuickstart;

    @FXML
    private ComboBox<String> cbLoja;

    @FXML
    private DatePicker dtDatePicker;


    @FXML
    private DatePicker dtDatePickerEmissao;


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
    private ComboBox<String> cbMarca;


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
            return true;
        }
        return false;
    }


    @FXML
    void confirmEnvio(ActionEvent event) {
        Sheets service = null;
        try {
            service = sheetsQuickstart.getSheetsService();
            String spreadsheetId = sheetsQuickstart.getSpreadsheetId();

            String chaveNFe = txtChaveDanfe.getText();
            String campoChaveNFe = "CHAVE DA NFE";
            String marcaSelecionada = cbMarca.getValue();
            String lojaSelecionada = cbLoja.getValue();


            String Name = txtName.getText();
            String numeroNota = extractNFeNumber(chaveNFe);

            LocalDate selectedDate = dtDatePicker.getValue();
            LocalDate selectDateEmissao = dtDatePickerEmissao.getValue();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String formattedDate = selectedDate.format(formatter);

            DateTimeFormatter formatterEmissao = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String formattedDateEmissao = selectDateEmissao.format(formatterEmissao);


            if (numeroNota != null && Name != null) {
                adicionarDadosFixos(service,
                        spreadsheetId,
                        campoChaveNFe,
                        chaveNFe,
                        numeroNota,
                        Name,
                        formattedDate,
                        marcaSelecionada,
                        lojaSelecionada,
                        formattedDateEmissao
                );

                txtChaveDanfe.clear();

                exibirAlerta("Nota enviada", "Os dados foram enviados com sucesso!", Alert.AlertType.INFORMATION);
            } else {
                exibirAlerta("Nota não enviada", "Não foi possível extrair os dados da NFe. Verifique a chave da NFe e tente novamente.", Alert.AlertType.ERROR);
            }
        } catch (IOException | GeneralSecurityException e) {
            exibirAlerta("Erro", "Ocorreu um erro ao enviar a nota: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void exibirAlerta(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private String extractNFeNumber(String chave) {

        if (chave.length() == 44 && chave.matches("\\d+")) {
            return chave.substring(25, 34);
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

    public void initialize() {

        ObservableList<String> marcaOptions = FXCollections.observableArrayList(
                "ACTIVITA", "ADDAN", "AKAZZO", "AKAZZO(NEFESH)", "BEIRA RIO", "BLITZZ",
                "BOTINHO", "BOX", "CARTAGO", "GRENDENE", "HOST", "IZALU", "KANXA", "LICITTO",
                "LOOK FASHION", "MENDII", "MISSISIPI", "MIZUNO", "MODA LEVE", "MODARE", "MOLECA",
                "MOLEKINHA", "MRS ATACADO", "NEFESH", "OLYMPIKUS - VULCABRAS", "ONITY",
                "OPEN", "ORTOBESSA", "PARTHENON", "PEGADA", "PLUMAX", "POLO ENERGY",
                "RAYON", "REED", "SELENE", "SERENITY", "STAR CHIC", "TOOPER", "VIA VIP",
                "VIVANZ", "VIZZANO", "VULCABRAS");

        cbMarca.setItems(marcaOptions);

        ObservableList<String> lojaOptions = FXCollections.observableArrayList(
                "001 - LOJA 152", "002 - LOJA 216", "003 - LOJA 27",
                "004 - LOJA 93", "005 - LOJA 238", "006 - LOJA 851",
                "007 - LOJA 29", "008 - LOJA 92", "009 - LOJA 356",
                "010 - LOJA GAB", "013 - LOJA 22", "014 - LOJA 256",
                "016 - LOJA 16", "017 - LOJA 57", "018 - LOJA 37",
                "019 - LOJA 322", "020 - LOJA 391"
        );
        cbLoja.setItems((lojaOptions));


    }
}
