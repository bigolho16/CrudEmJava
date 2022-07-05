/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.agenda.helper;

import br.com.agenda.model.Contato;
import br.com.agenda.view.JframePainel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author loboMal
 */
public class JframePainelHelper {
    
    private String textCampoNome = "";
    private int textCampoIdade = 0;
    private Date textCampoDataNascimento = new Date();
    
    private final String dados[][] = {
            {"Jack","19","Masculino"},
            {"Eddie","56","Masculino"},
            {"Gina","34","Feminino"},
            {"Klaus","18","Masculino"},
            {"Erika","20","Feminino"},
            {"Roberto","29","Masculino"},
            {"Maria","30","Feminino"}};
    
    /*
     * Preparando a entrada da view aqui no helper
     */
    private final JframePainel view;
    private JScrollPane scroll;
    private JTable tabela;
    private String[] colunas;
    private JPanel jPanelPaiDaTabelaDoModalAlteracao;
    private JTextField putNomeModEdit;
    private JTextField putIdadeModEdit;
    private JTextField putDataNascimentoModEdit;
    private JTextField putIdModEdit;
    
    public JframePainelHelper (JframePainel view) {
        this.view = view;
    }
    
    public void preencherTabela (ArrayList<Contato> contatos) {
        //Listar os valores, que veio de contato ativado pelo controller
        //Trazer o modelo para ativar os métodos da tabela que vai fazer receber os dados (como se usasse uma classe intermediária para fazer as coisas para a tabela)
        DefaultTableModel modeloTabela = ((DefaultTableModel) this.view.getjTableContatos().getModel());
        
        //Zerar primeiro os campos para caso eu clique em atualizar ele esteja zerado antes de atualizar
        modeloTabela.setRowCount(0);
        
        //Para adicionar os títulos da tabela(desnecessário pois ja fiz no NetBeans), deixar aqui como anotação...
        //modeloTabela.addColumn("id");
        //modeloTabela.addColumn("nome");

        for (Contato contato: contatos) {
            modeloTabela.addRow(new Object[] {
                contato.getId(),
                contato.getNome(),
                contato.getIdade(),
                contato.getStringBrasilianData()
            });
        }
    }
    
    public void defineTodosOsTitulosDaTabelaComoNaoEditaveis (JTable tabela) {
        tabela.setModel(new DefaultTableModel(new Object [][] {}, colunas) {
            
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };
            
            @Override
            public boolean isCellEditable (int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
            
        });
    }
    
    public void mostrarResultadosDaProcuraNoModal(ArrayList<Contato> contatos) {
        //Pegando a tabela
        tabela = this.view.getTabelaDoModal();
        
        //Cria o modelo e dicionar os dados da Tabela
        DefaultTableModel modeloDaTabela = ((DefaultTableModel) tabela.getModel());
        
        //Zerando as linhas da Tabela
        modeloDaTabela.setRowCount(0);
        
        //Adiciona os valores
        for (Contato contato : contatos) {
            modeloDaTabela.addRow(new Object [] {
                contato.getId(),
                contato.getNome(),
                contato.getIdade(),
                contato.getStringBrasilianData()
            });
        }
    }
    
    public void passarResultadosDaTabelaParaModalDentroDoModal () {
        //Pegando a tabela
        tabela = this.view.getTabelaDoModal();
        
        //Caso eu tenha selecionado e clicado no botao editar ele edita normal, caso contrário ele cai no else! 
        if (tabela.getSelectedRow() != -1) {
            //Pegando os inputs do Modal que vai se abrir (Edit)
            putIdModEdit = this.view.getjTextFieldIdModalEdit();
            putNomeModEdit = this.view.getjTextFieldNomeModalEdit();
            putIdadeModEdit = this.view.getJFormattedTextFieldIdadeModalEdit();
            putDataNascimentoModEdit = this.view.getjFormattedTextFieldDataNascimentoModalEdit();

            //Pegar os dados da Tabela
            ArrayList<String> contatoClicado = new ArrayList<String>();

            for (int x=0; x < tabela.getRowCount(); x++) { //x=linha
                for (int y =0; y < tabela.getColumnCount(); y++) { //y=coluna
                    if (x == tabela.getSelectedRow()) {
                        //Passar os resultados da linha para um array, pois não é possível adicionar nos inputs agora
                        contatoClicado.add(tabela.getValueAt(x, y).toString());
                    }
                }
            }

            putIdModEdit.setText(contatoClicado.get(0));
            putNomeModEdit.setText(contatoClicado.get(1));
            putIdadeModEdit.setText(contatoClicado.get(2));
            putDataNascimentoModEdit.setText(contatoClicado.get(3));

            //Passar para os campos (input) que irei criar no Model

            //Ativar exibição da tabela
            JDialog dialogEditarUnderAlter = this.view.getjDialogEditarContatosDentroDeAlterarContatos();
            dialogEditarUnderAlter.setLocationRelativeTo(this.view);
            dialogEditarUnderAlter.setVisible(true);
        }else {
            this.gerarMsgDeAlerta("Por favor, selecione um contato na tabela para editar.");
        }
    }
    
    /*
     * Vai receber os dados do formulário e retornar esse contato
     */
    public Contato obterModelo () {
        //Obter os valores dos campos e formata de acordo com seus tipos
        this.textCampoNome = this.view.getJFormattedNome().getText().trim();
        this.textCampoIdade = Integer.parseInt(this.view.getInputIdade().getText().trim());
        
        try {
            String txtcmpdate = this.view.getJFormattedDataNascimento().getText().trim();
            
            //convertendo de string p/ date, depois em formato americano
            SimpleDateFormat ff = new SimpleDateFormat("dd/mm/yyyy");
            Date ffdatabr = ff.parse(txtcmpdate);
            
            
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/mm/dd");
            String txttodate = formatter.format(ffdatabr);
            
            this.textCampoDataNascimento = formatter.parse(txttodate);
            
            //Convertendo de formato brasileiro para o americano
            //this.textCampoDataNascimento = formatter.parse(formatter.format(txttodate));
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        //Monta o obj e retorna
        Contato contato = new Contato(this.textCampoNome, this.textCampoIdade, this.textCampoDataNascimento);
        
        return contato;
    }

    public String getTextCampoNome() {
        return textCampoNome;
    }

    public void setTextCampoNome(String textCampoNome) {
        this.textCampoNome = textCampoNome;
    }

    public int getTextCampoIdade() {
        return textCampoIdade;
    }

    public void setTextCampoIdade(int textCampoIdade) {
        this.textCampoIdade = textCampoIdade;
    }

    public Date getTextCampoDataNascimento() {
        return textCampoDataNascimento;
    }

    public void setTextCampoDataNascimento(Date textCampoDataNascimento) {
        this.textCampoDataNascimento = textCampoDataNascimento;
    }

    public String obterTextoDoCampoProcurarDoModel() {
        //Obter texto do campo de pesquisa
        String jTextFieldDePesquisaNoAlterarContatos = this.view.getjTextFieldDePesquisaNoEditarContatos().getText();
        
        return jTextFieldDePesquisaNoAlterarContatos;
    }

    private boolean situacaoCamposModalEdit = true;
    public boolean permissaoParaProsseguirModalEdit () { return situacaoCamposModalEdit; }
    public void defineDadosDoFormEditDoModalEFormata () {
        try {
            //Pega o campos
            JTextField jTextFieldIdModalEdit = this.view.getjTextFieldIdModalEdit();
            JTextField jTextFieldNomeModalEdit = this.view.getjTextFieldNomeModalEdit();
            JTextField jTextFieldIdadeModalEdit = this.view.getJFormattedTextFieldIdadeModalEdit();
            JTextField jTextFieldDataNascimentoModalEdit = this.view.getjFormattedTextFieldDataNascimentoModalEdit();
            
            boolean ifdgt = this.view.getjFormattedTextFieldDataNascimentoModalEdit().isEditValid();
            
            //1°Cam. de verificação (se são vazios)
            if (jTextFieldNomeModalEdit.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Preencha o campo nome.", "ERRO", JOptionPane.INFORMATION_MESSAGE);
                situacaoCamposModalEdit = false;
                return;
            }else if (jTextFieldIdadeModalEdit.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Preencha o campo idade.", "ERRO", JOptionPane.INFORMATION_MESSAGE);
                situacaoCamposModalEdit = false;
                return;
            }else if (!this.view.getJFormattedTextFieldIdadeModalEdit().isEditValid()) {
                JOptionPane.showMessageDialog(null, "Campo idade inválido.", "ERRO", JOptionPane.INFORMATION_MESSAGE);
                situacaoCamposModalEdit = false;
                return;
            }else if (!ifdgt) {
                JOptionPane.showMessageDialog(null, "Campo data de nascimento vazio.", "ERRO", JOptionPane.INFORMATION_MESSAGE);
                situacaoCamposModalEdit = false;
                return;
            }
            
            //2°Cam. de verificação (se são válidos, ex: se data é uma data válida)
            boolean mtDataNascimento = jTextFieldDataNascimentoModalEdit.getText().matches("([0-2][0-9]||3[0-1])/(0[0-9]||1[0-2])/([0-9][0-9])?[0-9][0-9]"); //Validação do formato dd/mm/yyyy
            if (!mtDataNascimento) {
                JOptionPane.showMessageDialog(null, "Campo data de nascimento inválido.", "ERRO", JOptionPane.INFORMATION_MESSAGE);
                situacaoCamposModalEdit = false;
                return;
            }
            
            //3°Cam. fz alterações no intuito de deixar os dados limpos
            String cmpIdModEdit = jTextFieldIdModalEdit.getText().trim();
            String cmpNomeModEdit = jTextFieldNomeModalEdit.getText().trim();
            String cmpIdadeModEdit = jTextFieldIdadeModalEdit.getText().trim();
            String cmpDataDeNascimentoModEdit = jTextFieldDataNascimentoModalEdit.getText().trim();
            
            //Converte de tipo String para Date
            SimpleDateFormat strtodate = new SimpleDateFormat("dd/MM/yyyy");
            Date data = strtodate.parse(cmpDataDeNascimentoModEdit); // string com a data no formato dia-mês-ano
            
            //Converte de formato Brasileiro para Americano
            SimpleDateFormat formatoDestino = new SimpleDateFormat("yyyy/MM/dd");
            String formatoAnoMesDiaComTracos = formatoDestino.format(data);
            
            Contato contato = new Contato (Integer.parseInt(cmpIdModEdit), cmpNomeModEdit, Integer.parseInt(cmpIdadeModEdit), formatoAnoMesDiaComTracos);
            
            //Pode prosseguir no controller!
            situacaoCamposModalEdit = true;
            
            this.setContatoTemporario(contato);
            
        } catch (ParseException ex) {
            Logger.getLogger(JframePainelHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private Contato contatoTemporario;
    public Contato getContatoTemporario () {
        return contatoTemporario;
    }
    public void setContatoTemporario (Contato contatoTemporario) {
        this.contatoTemporario = contatoTemporario;
    }

    public boolean situacaoCampos = true;
    public boolean permissaoParaProsseguir () { return situacaoCampos; }
    public void formataCamposCadastroDeContatos() {
        JTextField inputNome = this.view.getJFormattedNome();
        JTextField inputIdade = this.view.getInputIdade();
        JFormattedTextField jFormattedDataNascimento = this.view.getJFormattedDataNascimento();
        final boolean ifdgt = this.view.getJFormattedDataNascimento().isEditValid();
        
        //1°Cam. de verificação (se são vazios)
        if (inputNome.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Preencha o campo nome.", "ERRO", JOptionPane.INFORMATION_MESSAGE);
            situacaoCampos = false;
            return;
        }else if (inputIdade.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Preencha o campo idade.", "ERRO", JOptionPane.INFORMATION_MESSAGE);
            situacaoCampos = false;
            return;
        }else if (!ifdgt) {
            JOptionPane.showMessageDialog(null, "Campo data de nascimento vazio.", "ERRO", JOptionPane.INFORMATION_MESSAGE);
            situacaoCampos = false;
            return;
        }
        
        //2°Cam. de verificação (se são válidos, ex: se data é uma data válida)
        boolean mtDataNascimento = jFormattedDataNascimento.getText().matches("([0-2][0-9]||3[0-1])/(0[0-9]||1[0-2])/([0-9][0-9])?[0-9][0-9]"); //Validação do formato dd/mm/yyyy
        if (!mtDataNascimento) {
            JOptionPane.showMessageDialog(null, "Campo data de nascimento inválido.", "ERRO", JOptionPane.INFORMATION_MESSAGE);
            situacaoCampos = false;
            return;
        }
        
        situacaoCampos = true;
    }

    public void limparOsInputsDeCadastro() {
        this.view.getJFormattedNome().setText("");
        this.view.getJFormattedIdade().setText("");
        this.view.getJFormattedDataNascimento().setText("");
    }

    public void gerarMsgDeSucesso(String msg) {
        JOptionPane.showMessageDialog(null, "Mensagem: " + msg, "Success!", JOptionPane.WARNING_MESSAGE);
    }

    private void gerarMsgDeAlerta(String msg) {
        JOptionPane.showMessageDialog(null, "Mensagem: " + msg, "Aviso!", JOptionPane.WARNING_MESSAGE);
    }

    public ArrayList<String> obterLinhaSelecionadaModAlteracao () {
        //Pegar o dado que esta selecionado na tabela
        tabela = this.view.getTabelaDoModal();
        
        //Caso eu tenha selecionado e clicado no botao editar ele edita normal, caso contrário ele cai no else! 
        ArrayList<String> contatoSelecionado = new ArrayList<String>();
        
        if (tabela.getSelectedRow() != -1) {
            
            int confirm = JOptionPane.showConfirmDialog(null, "Você tem certeja que deseja excluir este contato?", "Atenção!", JOptionPane.YES_NO_OPTION);
            
            switch (confirm) {
                case 0: //Cai aqui caso o usuário clique em sim (o java fecha a janela automáticamente)
                    for (int x=0; x < tabela.getRowCount(); x++) { //x=linha
                        for (int y =0; y < tabela.getColumnCount(); y++) { //y=coluna
                            if (x == tabela.getSelectedRow()) {
                                //Passar os resultados da linha para um array, pois não é possível adicionar nos inputs agora
                                contatoSelecionado.add(tabela.getValueAt(x, y).toString());
                            }
                        }
                    }
                    situacaoCampos = true;
                    break;
                case 1: //Cai aqui caso o usuário clique em não (o java fecha a janela automáticamente)
                    situacaoCampos = false; //Para o controller não prosseguir pois foi clicado em "não"
                    break;
            }
        }else {
            this.gerarMsgDeAlerta("Por favor, selecione um contato na tabela para excluir.");
            situacaoCampos = false; //Para caso eu não tenha selecionado nada (e evitar do situacaoCampos continuar sendo true e passar no controller)
        }
        
        return contatoSelecionado;
    }
}
