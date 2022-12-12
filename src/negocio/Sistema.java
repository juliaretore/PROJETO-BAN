package negocio;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import persistencia.*;
import java.util.List;
import dados.*;
import exceptions.DeleteException;
import exceptions.InsertException;
import exceptions.SelectException;
import exceptions.UpdateException;

public class Sistema {
	private static MusicaDAO musicaDAO;
	private static PlaylistDAO playlistDAO;
	
	public Sistema() throws ClassNotFoundException, SQLException, SelectException{
		musicaDAO = MusicaDAO.getInstance();
		playlistDAO = PlaylistDAO.getInstance();		
	}
	

	
	//MUSICA

	public List<Object> listarMusicas() throws SelectException{
		return musicaDAO.selectAll();
	}
		

	public List<Object> listarTop5() throws SelectException{
		return musicaDAO.selectTop5();
	}
	
	public void uploadMusicas(Musica musica) throws InsertException, SelectException, IOException {
		musicaDAO.insert(musica);
	}
	
	public void excluirMusica(int musica) throws DeleteException, SelectException {
		musicaDAO.delete(musica);
	}
		
	public void alterarMusica(Musica musica) throws UpdateException, SelectException, SQLException {
		musicaDAO.update(musica);
	}
	
	
	public void visualizacoes(int musica) throws UpdateException, SelectException, SQLException {
		musicaDAO.updateVisualizacao(musica);
		
	}
	
	public List<Object> listarArtistaMaisOuvido() throws SelectException{
		return musicaDAO.selectArtistaMaisOuvido();
	}
	
	//PLAYLIST
 	public void criarNovaPlaylist(Playlist playlist) throws InsertException, SelectException {
		playlistDAO.insertPlaylist(playlist);
	}
	
	public void excluirPlaylist(int playlist) throws DeleteException, SelectException {
		playlistDAO.deletePlaylist(playlist);
	}
	
	public List<Object> listarPlaylists() throws SelectException {
		return playlistDAO.selectPlaylists();	
	}
	
	public List<Object> listarMusicasPlaylist(int playlist) throws SelectException {
		return playlistDAO.selectMusicasPlaylist(playlist);
	}
	
	public void adicionarMusicasPlaylist(int musica, int playlist) throws InsertException, SelectException {
		playlistDAO.insertMusicaPlaylist(musica, playlist);
	}

	public List<Object> listarMusicasAdiconarPlaylist(int playlist) throws SelectException {
		return playlistDAO.selectExcetoNaPlaylist(playlist);
	}
	
	
	public String selectArtistaMaisPresentePlaylists() throws SelectException {
		return playlistDAO.selectArtistaMaisPresentePlaylists();
	}
	
	public void removerMusicasPlaylist(int playlist, int musica) throws DeleteException, SelectException {
		playlistDAO.deleteMusicaPlaylist(playlist,musica);
	}
	
	public void alterarPlaylist(Playlist playlist) throws UpdateException, SelectException {
		playlistDAO.update(playlist);
	}

	
	


}
