package com.unialfa.locadora;

import javax.swing.*;
import java.awt.*;

public class LocadoraForm extends JFrame {

    private LocadoraService service = new LocadoraService();    //iniciando o service antes de ser utilizado
    private JLabel labelFilme;
    private JTextField campoFilme;
    private JLabel labelDiretor;
    private JTextField campoDiretor;
    private JLabel labelDuracaoMinuto;
    private JTextField campoDuracaoMinuto;
    private JLabel labelElenco;
    private JTextField campoElenco;
    private JLabel labelClassificacao;
    private JTextField campoClassificacao;
    private JButton botaoSalvar;
    private JList<Filme> listaDeFilmes;
    private Boolean permitirExcecao = Boolean.FALSE;

    public LocadoraForm() {
        setTitle("Filme");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 550);

        JPanel painelEntrada = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5);

        labelFilme = new JLabel("Nome do Filme:");
        constraints.gridx = 0;
        constraints.gridy = 0;
        painelEntrada.add(labelFilme, constraints);

        campoFilme = new JTextField(20);
        constraints.gridx = 1;
        constraints.gridy = 0;
        painelEntrada.add(campoFilme, constraints);

        labelDiretor = new JLabel("Diretor:");
        constraints.gridx = 0;
        constraints.gridy = 1;
        painelEntrada.add(labelDiretor, constraints);

        campoDiretor = new JTextField(20);
        constraints.gridx = 1;
        constraints.gridy = 1;
        painelEntrada.add(campoDiretor, constraints);

        labelDuracaoMinuto = new JLabel("Duração em minutos:");
        constraints.gridx = 0;
        constraints.gridy = 2;
        painelEntrada.add(labelDuracaoMinuto, constraints);

        campoDuracaoMinuto = new JTextField(20);
        constraints.gridx = 1;
        constraints.gridy = 2;
        painelEntrada.add(campoDuracaoMinuto, constraints);

        labelElenco = new JLabel("Elenco:");
        constraints.gridx = 0;
        constraints.gridy = 3;
        painelEntrada.add(labelElenco, constraints);

        campoElenco = new JTextField(20);
        constraints.gridx = 1;
        constraints.gridy = 3;
        painelEntrada.add(campoElenco, constraints);

        labelClassificacao = new JLabel("Classificação:");
        constraints.gridx = 0;
        constraints.gridy = 4;
        painelEntrada.add(labelClassificacao, constraints);

        campoClassificacao = new JTextField(20);
        constraints.gridx = 1;
        constraints.gridy = 4;
        painelEntrada.add(campoClassificacao, constraints);

        botaoSalvar = new JButton("Salvar");
        botaoSalvar.addActionListener(e -> executarAcaoDoBotao());
        constraints.gridx = 1;
        constraints.gridy = 5;
        constraints.gridwidth = 2;
        painelEntrada.add(botaoSalvar, constraints);

        JPanel painelSaida = new JPanel(new BorderLayout());

        listaDeFilmes = new JList<>(carregarFilmes());
        listaDeFilmes.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        JScrollPane scrollPane = new JScrollPane(listaDeFilmes);
        painelSaida.add(scrollPane, BorderLayout.CENTER);

        getContentPane().add(painelEntrada, BorderLayout.NORTH);
        getContentPane().add(painelSaida, BorderLayout.CENTER);

        //pack();
        setLocationRelativeTo(null);
    }

    // Criar modelo e preenchê-lo com os filmes obtidos do service
    private DefaultListModel<Filme> getListModel(){
        var model = new DefaultListModel<Filme>();
        service.listarFilmes().forEach(model::addElement);
        return model;
    }

    //obter o modelo de lista de filmes acima
    private DefaultListModel<Filme> carregarFilmes(){
        return getListModel();
    }

    //verificar e executar ações do botão
    private void executarAcaoDoBotao() {
        permitirExcecao = Boolean.TRUE;
        verificaCampos(campoFilme.getText(), "Nome do Filme");
        verificaCampos(campoDiretor.getText(), "Nome do Diretor");
        verificaDuracao(campoDuracaoMinuto.getText());
        verificaCampos(campoElenco.getText(), "Elenco do Filme");
        verificaCampos(campoClassificacao.getText(), "Classificação do Filme");

        if (permitirExcecao) {
            service.salvar(new Filme(campoFilme.getText(),campoDiretor.getText(), campoDuracaoMinuto.getText(), campoElenco.getText(), campoClassificacao.getText()));
            campoFilme.setText("");
            campoDiretor.setText("");
            campoDuracaoMinuto.setText("");
            campoElenco.setText("");
            campoClassificacao.setText("");
            listaDeFilmes.setModel(getListModel());
        }
    }

    private void verificaCampos(String valor, String campo){
        try{
            if (valor.isEmpty()) throw new RuntimeException("Campo "+campo+" não pode ser vazio");
            if (valor.isBlank()) throw new RuntimeException("Campo "+campo+" não pode ter apenas espaços");
        } catch (RuntimeException re){
            JOptionPane.showMessageDialog(this, re.getMessage());
            permitirExcecao = Boolean.FALSE;
        }
    }

    private void verificaDuracao(String valor) {
        try{
            if (valor.isEmpty()) throw new RuntimeException("Campo Duração em Minutos não pode ser vazio");
            if (valor.isBlank()) throw new RuntimeException("Campo Duração em Minutos não pode ter apenas espaços");
        } catch (NumberFormatException e){
            JOptionPane.showMessageDialog(this, "A duração do filme deve estar em minutos");
            permitirExcecao = Boolean.FALSE;
        } catch (RuntimeException re){
            JOptionPane.showMessageDialog(this, re.getMessage());
            permitirExcecao = Boolean.FALSE;
        }
    }
}
