package dados;

import java.io.File;

public class Musica {
	private int codMusica;
	private String nome;
	private int duracao;
	private File arqMusica;
	private int visualizacoes;
	private String letra;
	private String nomeArtista;
	
	public Musica(int codMusica, String nome, int duracao, int visualizacoes, String letra, String nomeArtista){
		setNome(nome);
		setCodMusica(codMusica);
		setDuracao(duracao);
		setVisualizacoes(visualizacoes);
		setLetra(letra);
		setNome(nomeArtista);
		
	}

	public Musica(){

	}
	
	

//	@Override
//	 public boolean equals(Object o) {
//		if(o instanceof Musica) {
//			Musica m = (Musica) o;
//			if (this.nome.equals(m.getNome()) && this.album.equals(m.getAlbum()) && this.estilo.equals(m.getEstilo())) return true;
//		}
//         return false;
//	}

	public int getCodMusica() {
		return codMusica;
	}

	public void setCodMusica(int codMusica) {
		this.codMusica = codMusica;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getDuracao() {
		return duracao;
	}

	public void setDuracao(int duracao) {
		this.duracao = duracao;
	}

	public File getArqMusica() {
		return arqMusica;
	}

	public void setArqMusica(File arqMusica) {
		this.arqMusica = arqMusica;
	}

	public int getVisualizacoes() {
		return visualizacoes;
	}

	public void setVisualizacoes(int visualizacoes) {
		this.visualizacoes = visualizacoes;
	}

	public String getLetra() {
		return letra;
	}

	public void setLetra(String letra) {
		this.letra = letra;
	}

	public String getNomeArtista() {
		return nomeArtista;
	}

	public void setNomeArtista(String nomeArtista) {
		this.nomeArtista = nomeArtista;
	}

}