package persistencia;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import dados.Musica;
import dados.Playlist;
import exceptions.DeleteException;
import exceptions.InsertException;
import exceptions.SelectException;
import exceptions.UpdateException;

public class PlaylistDAO {
	private static PlaylistDAO instance = null;
	private PreparedStatement selectNewIdPlaylist;
	private PreparedStatement selectNewIdMusicasPlaylist;
	private PreparedStatement insertPlaylist;
	private PreparedStatement insertMusicaPlaylist;
	private PreparedStatement deletePlaylist;
	private PreparedStatement deleteMusicas;
	private PreparedStatement deleteMusicaPlaylist;
	private PreparedStatement update;
	private PreparedStatement selectPlaylists;
	private PreparedStatement selectExcetoNaPlaylist;
	private PreparedStatement selectMusicasPlaylist;
	private PreparedStatement selectArtistaMaisPresentePlaylists;
	
	
	public static PlaylistDAO getInstance() throws ClassNotFoundException, SQLException, SelectException{
		if(instance==null) instance=new PlaylistDAO();
		return instance;
	}
	
	private PlaylistDAO() throws ClassNotFoundException, SQLException, SelectException{
		Connection conexao = Conexao.getConexao();
		//inserir
		selectNewIdPlaylist = conexao.prepareStatement("select nextval('id_playlist')");
		selectNewIdMusicasPlaylist = conexao.prepareStatement("select nextval('id_musicas_playlist')");
		insertPlaylist =  conexao.prepareStatement("insert into playlist values (?,?,?)");
		insertMusicaPlaylist =  conexao.prepareStatement("insert into musicas_playlist values (?,?,?)");
		
		//remover
		deletePlaylist = conexao.prepareStatement("delete from playlist where codPlaylist=?");
		deleteMusicas = conexao.prepareStatement("delete from musicas_playlist where id_playlist = ?");	
		deleteMusicaPlaylist = conexao.prepareStatement("delete from musicas_playlist where id_playlist=? and id_musica=?");	
		
		//update
		update = conexao.prepareStatement( "update playlist set nome=? where codPlaylist=?" ) ;

		//select
		selectPlaylists = conexao.prepareStatement("select * from playlist");
		selectExcetoNaPlaylist = conexao.prepareStatement("select codMusica, nome, nomeArtista from musica where codMusica not in (select codMusica from musica m join musicas_playlist p on p.id_musica=m.codMusica where id_playlist=?)");
		selectMusicasPlaylist = conexao.prepareStatement("select m.codMusica, m.nome, m.nomeArtista from musica m join musicas_playlist p on p.id_musica=m.codMusica where id_playlist=?");
		selectArtistaMaisPresentePlaylists = conexao.prepareStatement("select nomeArtista, count(*) as vezes from musicas_playlist mp join musica m on m.codMusica=mp.id_musica group by nomeArtista order by vezes desc limit 1");
	}
	
// select nextval('id_playlist')
	private int selectNewIdMusicasPlaylist() throws SelectException{
		try {
			ResultSet rs= selectNewIdMusicasPlaylist.executeQuery();
			if(rs.next()) return rs.getInt(1);
		}catch(SQLException e) {
			throw new SelectException("Erro ao buscar novo ID da tabela musicas_playlist");
		}
		return 0;
	}
	
// select nextval('id_musicas_playlist')
	private int selectNewIdPlaylist() throws SelectException{
		try {
			ResultSet rs= selectNewIdPlaylist.executeQuery();
			if(rs.next()) return rs.getInt(1);
		}catch(SQLException e) {
			throw new SelectException("Erro ao buscar novo ID da tabela playlist");
		}
		return 0;
	}
	
//	"select nomeArtista, count(*) as vezes from musicas_playlist mp join musica m on m.codMusica=mp.id_musica group by nomeArtista order by vezes desc limit 1");
	public String selectArtistaMaisPresentePlaylists() throws SelectException {
		 String a = null;
		try {
			ResultSet rs = selectArtistaMaisPresentePlaylists.executeQuery();
			while(rs.next()) {
				a=rs.getString(1)+" - "+rs.getString(2)+" vezes";
			}
		}catch(SQLException e) {
			throw new SelectException("Erro ao buscar artista mais presente em playlists");
		}
		return a;
	}	
	
	
// insert into playlist values (?,?,?)
	public void insertPlaylist(Playlist playlist) throws InsertException, SelectException{
		try {
			insertPlaylist.setInt(1, selectNewIdPlaylist());
			insertPlaylist.setString(2, playlist.getNome());
			insertPlaylist.setString(3, playlist.getDescricao());
			insertPlaylist.executeUpdate();
		}catch (SQLException e) {
			throw new InsertException("Erro ao inserir playlist.");
		}
}
	

// insert into musicas_playlist values (?,?,?)
	public void insertMusicaPlaylist(int musica, int playlist) throws InsertException, SelectException{
		try {
			insertMusicaPlaylist.setInt(1, selectNewIdMusicasPlaylist());
			insertMusicaPlaylist.setInt(2, musica);
			insertMusicaPlaylist.setInt(3, playlist);
			insertMusicaPlaylist.executeUpdate();
		}catch (SQLException e) {
			throw new InsertException("Erro ao inserir música na playlist.");
		}
	}
		
	
// deletePlaylist = conexao.prepareStatement("delete from playlist where codPlaylist=?");
	public void deletePlaylist(int playlist) throws DeleteException, SelectException{
		deleteMusicas(playlist);
		try {
			deletePlaylist.setInt(1, playlist);
			deletePlaylist.executeUpdate();
		}catch(SQLException e) {
			throw new DeleteException("Erro ao deletar playlist");
		}
		
	}
	
// delete from musicas_playlist where id_playlist = ?
	public void deleteMusicas(int playlist) throws DeleteException{		
		try {
			deleteMusicas.setInt(1, playlist);
			deleteMusicas.executeUpdate();
		}catch(SQLException e) {
			throw new DeleteException("Erro ao deletar músicas da playlist");
		}
	}


// delete from musicas_playlist where id_playlist=? and id_musica=?
	public void deleteMusicaPlaylist(int playlist, int musica) throws DeleteException, SelectException{
		try {
			deleteMusicaPlaylist.setInt(1, playlist);
			deleteMusicaPlaylist.setInt(2, musica);
			deleteMusicaPlaylist.executeUpdate();	
		}catch(SQLException e) {
			throw new DeleteException("Erro ao deletar m�sica da playlist");
		}
	}
		
	
// update playlist set nome=?, descricao=? where codPlaylist=?
	public void update(Playlist playlist) throws UpdateException, SelectException{
			try {
				update.setString(1, playlist.getNome());
				update.setString(2, playlist.getDescricao());
				update.setInt(3, playlist.getCodPlaylist());
				update.executeUpdate();
			}catch (SQLException e) {
				throw new UpdateException("Erro ao atualizar música.");
			}
	}

	
// select codPlaylist, nome, descricao from playlist
	public List<Object> selectPlaylists() throws SelectException {
		 List<Object> playlists = new ArrayList<Object>();
		try {
			ResultSet rs = selectPlaylists.executeQuery();
			while(rs.next()) {
				Object[] linha = {rs.getInt(1), rs.getString(2), rs.getString(3)};
				playlists.add(linha);
			}
		}catch(SQLException e) {
			throw new SelectException("Erro ao buscar playlist");
		}
		return playlists;
	}	

	
//select m.codMusica, m.nome, m.nomeArtista from musica m join musicas_playlist p on p.id_musica=m.codMusica where id_playlist=?
	public List<Object> selectMusicasPlaylist(int playlist) throws SelectException {
		List<Object> musicas = new ArrayList<Object>();
		try {
			selectMusicasPlaylist.setInt(1, playlist);
			ResultSet rs = selectMusicasPlaylist.executeQuery();
			
			while(rs.next()) {
				Object[] linha = {rs.getInt(1), rs.getString(2), rs.getString(3)};
				musicas.add(linha);
			}
			
		}catch(SQLException e) {
			throw new SelectException("Erro ao buscar playlist");
		}
		return musicas;
	}
	
	
//select codMusica, nome, nomeArtista from musica where codMusica not in (select codMusica from musica m join musicas_playlist p on p.id_musica=m.codMusica where id_playlist=?"
	public List<Object>selectExcetoNaPlaylist(int playlist) throws SelectException {
		List<Object> musicas = new ArrayList<Object>();
		try {

			selectExcetoNaPlaylist.setInt(1, playlist);

			ResultSet rs = selectExcetoNaPlaylist.executeQuery();
			System.out.println("aqui "+ playlist);

			
			while(rs.next()) {
				Object[] linha = {rs.getInt(1), rs.getString(2), rs.getString(3)};
				musicas.add(linha);
			}
			
		}catch(SQLException e) {
			throw new SelectException("Erro ao buscar músicas para adicionar a playlist");
		}
		return musicas;
	}

	

}
	

