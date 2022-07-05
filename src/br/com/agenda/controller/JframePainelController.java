/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.agenda.controller;

import br.com.agenda.dao.ContatoDAO;
import br.com.agenda.helper.JframePainelHelper;
import br.com.agenda.model.Contato;
import br.com.agenda.view.JframePainel;
import java.text.ParseException;
import java.time.Clock;
import java.util.ArrayList;

/**
 *
 * @author loboMal
 */
public class JframePainelController {
    
    public final JframePainelHelper helper;
    private final ContatoDAO dao;
    private final JframePainel view;
    
    public JframePainelController (JframePainel view) {
        
        this.view = view;
        
        this.helper = new JframePainelHelper(view);
        
        this.dao = new ContatoDAO();
        
        //Atualizar tabela
        this.atualizaTabela();
    }
    
    
    public void cadastrarContatos () {
        //Verificar se os campos estão corretos
        this.helper.formataCamposCadastroDeContatos();
        
        if (this.helper.permissaoParaProsseguir() == true) {
            //Pegar os dados dos campos primeiro
            Contato contato = this.helper.obterModelo();
            
            //Solicitar ao DAO o cadastramento
            ContatoDAO contatoDAO = new ContatoDAO();
            contatoDAO.save(contato);
            
            //Depois de salvo, volto para os inputs e limpo os campos!
            this.helper.limparOsInputsDeCadastro();
            
            //Mostra mensagem de que o cadastro foi efetuado com sucesso
            this.helper.gerarMsgDeSucesso("O cadastro foi efetuado com sucesso!");
        }
        
        //Ver o que estão nos valores valores...
//        System.out.println(contato.getNome() + " " + contato.getIdade() + " " + contato.getData());
        //System.out.println(this.helper.getTextCampoDataNascimento());
        
    }

    public void solicitaAtualizacaoContatoNaTabela () {    
        // Solicitar que o DAO pegue dados do db e mande para contato
        //this.dao.selectLast10();
        
        //Depois retornar esses dados
        Contato contato = this.helper.obterModelo();
        
        System.out.println(contato.getId());
        
    }
    
    public void atualizaTabela () {
        //Missão do controller e pegar e trazer os dados p/ o helper...
        //Missão pegar os dados:
        //Ativar buscar dos dez últimos
        this.dao.setSelectLast10 ();
        //Pegar os dez últimos
        ArrayList<Contato> contato = this.dao.getContato();
        
        //Missão trazer os dados:
        this.helper.preencherTabela(contato);
    }

    public void solicitaPesquisaDeContatos() {
        //Pegar o que o view quer que busque
        String txtSearchPut = this.helper.obterTextoDoCampoProcurarDoModel ();
        
        //Mandar os dados do campo de procura para o DAO - Ativar a buscar pelos valores
        this.dao.searchValues(txtSearchPut);
        
        //Retornar o obj com os valores
        ArrayList<Contato> contato = this.dao.getContato();
        
        //Retornar os dados para o helper
        this.helper.mostrarResultadosDaProcuraNoModal(contato);
        
    }

    public void solicitaAtualizacaoDoContato () {
        //Faz todas as formatações bem como mandar as info. para a classe Contato
        this.helper.defineDadosDoFormEditDoModalEFormata();
        
        if (this.helper.permissaoParaProsseguirModalEdit() == true) {
            //Obter os dados da classe Contato e envia para o DAO
            Contato contato = this.helper.getContatoTemporario();
            this.dao.atualizarContato (contato);
            
            //Exibe a msg de que foi atualizado com sucesso!
            this.helper.gerarMsgDeSucesso("Contato atualizado com sucesso!");
            
            this.solicitaPesquisaDeContatos(); //Vai atualizar a TABELA de pesquisa (modal alteração)
            
            this.atualizaTabela(); //Vai atualizar a TABELA do modal principal
        }
    }

    public void solicitaExclusaoDeContato() {
        //Pegar os dados com o helper
        ArrayList<String> dadosDaLinhaSelecionada = this.helper.obterLinhaSelecionadaModAlteracao();
        
        if (this.helper.situacaoCampos != false) { //Se ele for false é por que cliquei em "não" para exclusão
            //Passar para o DAO e solicitar para exclusao
            this.dao.excluirContato(dadosDaLinhaSelecionada);

            //Retorna e diz que excluiu e atualiza as tabelas para não mostrar mais o que já foi excluído
            this.helper.gerarMsgDeSucesso("Contato excluído com sucesso!");
            
            this.solicitaPesquisaDeContatos(); //Vai atualizar a TABELA de pesquisa (modal alteração)
            
            this.atualizaTabela(); //Vai atualizar a TABELA do modal principal
        }
    }
}
