package telas;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import regras.ControleBotoesSelecionados;
import regras.EstadosBotoes;

public class TelaPrincipal extends JFrame {
	
	private static final int QUANTIDADE_JOGADAS = 2;
	private int jogadas = 0;
	
	private JPanel painel;
	
	
	private List<ControleBotoesSelecionados> listaControle =  new ArrayList<>();
	private List<ControleBotoesSelecionados> listaControlesSelecionados = new ArrayList<>();
	
	private ActionListener acaoBotoes;
	
	
	public TelaPrincipal() {
		super("Jogo da Memoria");
		
		
		 acaoBotoes = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JButton botoes = (JButton) e.getSource();
				for(ControleBotoesSelecionados cont : listaControle)
				if (cont.getReferenciaBotoes().get(botoes) != null) {
					jogadas ++;
					cont.executarAcaoBotoes((JButton) e.getSource(), EstadosBotoes.SELECIONADO);
					//controle de inclusao
					if (!listaControlesSelecionados.contains(cont)) {
						listaControlesSelecionados.add(cont);
					}
					if (jogadas == QUANTIDADE_JOGADAS) {
						//Acabaram as jogadas
						if (listaControlesSelecionados.size() > 1) {
						// Deixar os botoes com estado incial
							for(ControleBotoesSelecionados cbs : listaControlesSelecionados) {
								CompletableFuture.delayedExecutor(1,  TimeUnit.SECONDS).execute(() -> {
									cbs.zerarSelecoes();
								});
								
							}
						} 
						jogadas = 0;
						listaControlesSelecionados.clear();
					}
					break;
				}	
			}
		};
		
		
		painel =  new JPanel();
		this.add(painel);
		painel.setLayout(null);
		
		
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		criarJogo(10);
		this.setBounds(250, 250, 500 , 500);
		this.setVisible(true);
	}
	
	private void criarJogo(int qntPares) {
		//Quantidade de botoes
		ControleBotoesSelecionados controle = null;
		List<Rectangle> posicionamentos = new ArrayList<>();
		int posX = 10;
		int posY = 10;
		
		Random rand = new Random();
		
		int j = 0;
		int i = 0;
		
		for(i = 0; i < (qntPares * 2); i++) {
			Rectangle rec = new Rectangle(posX, posY, 85, 85);
			posicionamentos.add(rec);
			if (i % 5 == 0 && i > 0) {
				posY += 90;
				posX = 10;
			}else {
				posX += 90;
			}
		}
		
		for(i = 0 ; i < (qntPares * 2); i++) {
			//Quantidade de controles
			 if (i % 2 == 0) {
				 j++;
				controle = new ControleBotoesSelecionados();
				controle.setNmBotao("B" + j);
				this.listaControle.add(controle);
			}
			JButton botao = new JButton("Carta");
			//Colocar botoes na tela
			this.painel.add(botao);
			botao.addActionListener(this.acaoBotoes);
			//adicionar posicao
		
			int pos = rand.nextInt(((posicionamentos.size() - 1) > 0 ? posicionamentos.size() - 1 : 1));
			botao.setBounds(posicionamentos.get(pos));
			posicionamentos.remove(pos);
			
			controle.adicionarBotao(botao);
		}
		
		
		// Adaptar tamanho da tela
		//Randomizar o posicionamento dos botoes
	}

}
