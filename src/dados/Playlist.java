package dados;

public class Playlist {
	private int codPlaylist;
	private String nome;
	private String descricao;
	
	
	public Playlist(int codPlaylist, String nome, String descricao) {
		setCodPlaylist(codPlaylist);
		setDescricao(descricao);
		setNome(nome);
	}
	
	
	public Playlist() {
	}

	public int getCodPlaylist() {
		return codPlaylist;
	}

	public void setCodPlaylist(int codPlaylist) {
		this.codPlaylist = codPlaylist;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}


}
