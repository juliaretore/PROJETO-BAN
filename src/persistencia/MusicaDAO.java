package persistencia;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import dados.Musica;
import exceptions.DeleteException;
import exceptions.InsertException;
import exceptions.SelectException;
import exceptions.UpdateException;

public class MusicaDAO {
	private static MusicaDAO instance = null;
	private PreparedStatement selectNewId;
	private PreparedStatement selectAll;
	private PreparedStatement insert;
	private PreparedStatement delete;
	private PreparedStatement update;
	private PreparedStatement deleteMusicaPlaylist;
	private PreparedStatement updateVisualizacao;
	private PreparedStatement  selectTop5;
	private PreparedStatement  selectArtistaMaisOuvido;
	
	public static MusicaDAO getInstance() throws ClassNotFoundException, SQLException, SelectException{
		if(instance==null) instance=new MusicaDAO();
		return instance;
	}
	
	private MusicaDAO() throws ClassNotFoundException, SQLException, SelectException{
		Connection conexao = Conexao.getConexao();
		//insert
		selectNewId = conexao.prepareStatement("select nextval('id_musica')");
		insert =  conexao.prepareStatement("insert into musica values (?,?,?,?,?,?,?)");
		
		//select
		selectAll = conexao.prepareStatement("select codMusica, nome, duracao, visualizacoes, letra, nomeArtista from musica");
		selectTop5 = conexao.prepareStatement("select nome, nomeArtista, visualizacoes from musica order by visualizacoes desc limit 5");
		selectArtistaMaisOuvido= conexao.prepareStatement("select nomeArtista, sum(visualizacoes) from musica group by nomeArtista order by sum desc limit 3");
		
		//update
		update = conexao.prepareStatement( "update musica set nome=?, duracao=?, letra=?, nomeArtista=? where codMusica=?") ;
		updateVisualizacao = conexao.prepareStatement("update musica set visualizacoes=visualizacoes+1 where codMusica=?");
		
		//delete
		delete = conexao.prepareStatement("delete from musica where id=?");
		deleteMusicaPlaylist = conexao.prepareStatement("delete from musicas_playlist where id_musica=?");	
		
		
		
		
	}
	
	
//	update musica set nome=?, duracao=?,  letra=?, nomeArtista=? where codMusica=?
	public void update(Musica musica) throws UpdateException, SelectException, SQLException{
			update.setString(1, musica.getNome());
			update.setInt(2, musica.getDuracao());
			update.setString(3, musica.getLetra());
			update.setString(4, musica.getNomeArtista());
			update.setInt(5, musica.getCodMusica());
			update.executeUpdate();
	}

// update musica set visualizacoes=visualizacoes+1 where codMusica=?
	public void updateVisualizacao (int musica) throws UpdateException, SelectException, SQLException{
		updateVisualizacao.setInt(1, musica);
		updateVisualizacao.executeUpdate();
}

	
	
// select nextval('id_musica')
	private int selectNewId() throws SelectException{
		try {
			ResultSet rs= selectNewId.executeQuery();
			if(rs.next()) return rs.getInt(1);
		}catch(SQLException e) {
			throw new SelectException("Erro ao buscar novo ID da música");
		}
		return 0;
	}

	
// insert into musica values (?,?,?,?,?,?,?)
	public void insert(Musica musica) throws InsertException, SelectException, IOException{
		try {
			insert.setInt(1, selectNewId());
			insert.setString(2, musica.getNome());
			insert.setInt(3, musica.getDuracao());
			//arquivo
			insert.setNull(4, 0, null);
			insert.setInt(5, musica.getVisualizacoes());
			insert.setString(6, musica.getLetra());
			insert.setString(7, musica.getNomeArtista());
			insert.executeUpdate();
		}catch (SQLException e) {
			throw new InsertException("Erro ao inserir música.");
		}
	
}
	

// select codMusica, nome, duracao, visualizacoes, letra, nomeArtista from musica
	public List<Object> selectAll() throws SelectException {
		List<Object> musicas = new LinkedList<Object>();
		try {
			ResultSet rs = selectAll.executeQuery();
			while(rs.next()) {
				Object[] linha = {rs.getInt(1), rs.getString(2),rs.getInt(3), rs.getInt(4),rs.getString(5),rs.getString(6)};
				musicas.add(linha);
			}
		}catch(SQLException e) {
			throw new SelectException("Erro ao buscar músicas");
		}
		return musicas;
	}
	
// select nome, nomeArtista, visualizacoes from musica order by visualizacoes desc limit 5
		public List<Object> selectTop5() throws SelectException {
			List<Object> musicas = new LinkedList<Object>();
			try {
				ResultSet rs = selectTop5.executeQuery();
				while(rs.next()) {
					Object[] linha = {rs.getString(1), rs.getString(2), rs.getInt(3)};
					musicas.add(linha);
				}
			}catch(SQLException e) {
				throw new SelectException("Erro ao gerar top 5");
			}
			return musicas;
		}

// select nomeArtista, sum(visualizacoes) from musica group by nomeArtista order by sum desc limit 1	
		public List<Object> selectArtistaMaisOuvido() throws SelectException {
			List<Object> musicas = new LinkedList<Object>();
			try {
				ResultSet rs = selectArtistaMaisOuvido.executeQuery();
				while(rs.next()) {
					Object[] linha = {rs.getString(1), rs.getInt(2)};
					musicas.add(linha);
				}
			}catch(SQLException e) {
				throw new SelectException("Erro ao gerar artista mais ouvido");
			}
			return musicas;
		}



//	delete from musica where id=?
	public void delete(int IDmusica) throws DeleteException, SelectException{
		deleteMusicaPlaylist(IDmusica);
		try {
			delete.setInt(1, IDmusica);
			delete.executeUpdate();
		}catch(SQLException e) {
			throw new DeleteException("Erro ao deletar música");
		}
	}

	
//	delete from musicas_playlist where id_musica=?
	public void deleteMusicaPlaylist(int musica) throws DeleteException{
		try {
			deleteMusicaPlaylist.setInt(1, musica);
			deleteMusicaPlaylist.executeUpdate();
		}catch(SQLException e) {
			throw new DeleteException("Erro ao deletar música das playlists");
		}
	}
	
	
}
