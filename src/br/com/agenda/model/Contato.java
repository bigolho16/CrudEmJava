/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.agenda.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author loboMal
 */
public class Contato {
    private int id;
    private String nome;
    private int idade;
    private Date data;
    private String StringBrasilianData;
    
    
    public Contato (String nome, int idade, Date data) {
        this.nome = nome;
        this.idade = idade;
        this.data = data;
    }
    
    public Contato (int id, String nome, int idade, Date data) {
        this.id = id;
        this.nome = nome;
        this.idade = idade;
        this.data = data;
    }
    
    public Contato (int id, String nome, int idade, String StringBrasilianData) {
        this.id = id;
        this.nome = nome;
        this.idade = idade;
        this.StringBrasilianData = StringBrasilianData;
    }

    public Contato() {
        
    }

    public String getStringBrasilianData() {
        return StringBrasilianData;
    }

    public void setStringBrasilianData(String StringBrasilianData) {
        this.StringBrasilianData = StringBrasilianData;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }
    
}
