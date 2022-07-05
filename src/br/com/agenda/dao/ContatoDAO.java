/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.agenda.dao;

import br.com.agenda.factory.ConnectionFactory;
import br.com.agenda.model.Contato;
import java.sql.Connection;
//import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 *
 * @author loboMal
 */
public class ContatoDAO {
    
    private ArrayList<Contato> contato;

    public ArrayList<Contato> getContato() {
        return contato;
    }

    public void setContato(ArrayList<Contato> contato) {
        this.contato = contato;
    }
    

    public void save(Contato contato) {
        
        //Monta a query
        //Date data = new Date();
        
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/mm/dd"); //formato que o db espera: 2021-06-28 00:11:24
        
        String strDate = formatter.format(contato.getData()); //Tive que converter o tipo Date p/ String só assim dar pra enviar para o db
        
        String sql = "INSERT INTO contatos(nome, idade, dataNascimento) VALUES (?, ?, ?)";

        //Prepara-se para a conexão
        Connection conn = null;
        PreparedStatement pstm = null;
        
        try {
            //Cria uma conexão com o db
            conn = ConnectionFactory.createConnectionToMysql();
            pstm = conn.prepareStatement(sql);
            
            pstm.setString(1, contato.getNome());
            pstm.setInt(2, contato.getIdade());
            pstm.setString(3, strDate);
        
            //Inclui os dados na quuery
            int executeUpdate = pstm.executeUpdate();
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            //Fechar as conexões que estejam abertas
            try {
                if (pstm != null) {
                    pstm.close();
                }
                
                if (conn != null) {
                    conn.close();
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public void setSelectLast10 () {
        //Criar a query de select e retornar
        String sql = "SELECT * FROM contatos ORDER BY id DESC LIMIT 10";
        
        Connection conn = null;
        PreparedStatement pstm = null;
        
        try {
            conn = ConnectionFactory.createConnectionToMysql();
            
            pstm = conn.prepareStatement(sql);

            ResultSet rs = pstm.executeQuery();
            
            //Instanciar ArrayList de Contato
            contato = new ArrayList<Contato>();
            
            //Atribui os valores do db p/ Obj Contato
            while (rs.next()) {
                //Manda os valores do db p/ obj Contato
                //contato = new Contato(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getDate(4));
                //this.contato.add(contato);
                this.contato.add(new Contato(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getInt("idade"),
                    this.converterDataSqlAmericanaParaBrasileira(rs.getString("dataNascimento"))
                ));
                
                //System.out.println(this.converterDataSqlAmericanaParaBrasileira(rs.getString("dataNascimento")));
            }
            
            //Inverter os valores do array para ficar mais agradável na view
            Collections.reverse(contato);
            
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            //Fechar as conexões que estejam abertas
            try {
                if (pstm != null) {
                    pstm.close();
                }
                
                if (conn != null) {
                    conn.close();
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public String converterDataSqlAmericanaParaBrasileira (String sqlData) throws ParseException {
        /*
         *Ele vai chegar em formato sql e americano, então minha missão é converter para brasileiro
         */
        
        //convertendo de string p/ date
        SimpleDateFormat stringToDate = new SimpleDateFormat("yyyy-mm-dd");
        Date parse = stringToDate.parse(sqlData); // string com a data no formato ano-mês-dia
        //depois em formato brasileiro
        SimpleDateFormat brasiliansDateToAmericanDate = new SimpleDateFormat("dd/mm/yyyy");
        String brasilianFormat = brasiliansDateToAmericanDate.format(parse);
        
        return brasilianFormat;
    }
    
//    public ArrayList<Object> getSelectLast10 () {
//        //Buscar a variável no db que tem os valores
//        return this.contato;
//    }

    public void searchValues (String vl) {
        //Identificar se esta pesquisando por id ou por nome
        boolean isNumeric = true;
        
        for (int i=0; i < vl.length(); i++) {
            if (!Character.isDigit(vl.charAt(i))) {
                isNumeric = false;
            }
        }
        
        PreparedStatement pstm = null;
        Connection conn = null;
        
        try {
            conn = ConnectionFactory.createConnectionToMysql();
            
            String sql = null;
            
            //É número, pesquise pela coluna id
            if (isNumeric == true) {
                sql = "SELECT * FROM contatos WHERE id LIKE ?";
            }
            //É texto, pesquise pela coluna nome
            if (isNumeric == false) {
                sql = "SELECT * FROM contatos WHERE nome LIKE ?";
            }
            pstm = conn.prepareStatement(sql);
            
            //Para evitar ataques de SQLi (ao invés de jogar os valores diretos na query)
            pstm.setString(1, "%" + vl + "%");
            
            //Executa a query
            ResultSet rs = pstm.executeQuery();
            
            //Atribuir os valores ao obj
            this.contato = new ArrayList<Contato>();
            
            while (rs.next()) {
                
                this.contato.add(new Contato(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getInt("idade"),
                    this.converterDataSqlAmericanaParaBrasileira(rs.getString("dataNascimento"))
                ));
                
            }
            
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            //Fechar as conexões que estejam abertas
            try {
                if (pstm != null) {
                    pstm.close();
                }
                
                if (conn != null) {
                    conn.close();
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        
    }

    public void atualizarContato(Contato contato) {
        //Monta a query
        String query = "UPDATE contatos SET nome=?, idade=?, dataNascimento=? WHERE id=?";
        
        //Atualiza os dados no banco de dados
        Connection conn = null;
        PreparedStatement pstm = null;
        try {
            conn = ConnectionFactory.createConnectionToMysql();
            pstm = conn.prepareStatement(query);
            
            pstm.setString(1, contato.getNome());
            pstm.setInt(2, contato.getIdade());
            pstm.setString(3, contato.getStringBrasilianData());
            pstm.setInt(4, contato.getId());
            
            pstm.executeUpdate();
            
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (pstm != null) {
                    pstm.close();
                }
                if (conn != null) {
                    conn.close();
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void excluirContato(ArrayList<String> dadosDaLinhaSelecionada) {
        //Monta a query
        String query = "DELETE FROM contatos WHERE id=?";
        
        //Excluir o contato
        //Atualiza os dados no banco de dados
        Connection conn = null;
        PreparedStatement pstm = null;
        try {
            conn = ConnectionFactory.createConnectionToMysql();
            pstm = conn.prepareStatement(query);
            
            pstm.setString(1, dadosDaLinhaSelecionada.get(0));
            
            pstm.execute();
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (pstm != null) {
                    pstm.close();
                }
                if (conn != null) {
                    conn.close();
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
