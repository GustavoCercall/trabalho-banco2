package persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import dados.Musica;


public class MusicaDAO implements DAO<Musica>  {

	private static MusicaDAO instance = null;

	private PreparedStatement selectNewId;
	private PreparedStatement select;
	private PreparedStatement selectAll;
	private PreparedStatement selectMusicasPlaylist;
	private PreparedStatement insert;
	private PreparedStatement delete;
	private PreparedStatement deleteMusicaUsuario;
	private PreparedStatement deleteMusicaPlaylist;
	private PreparedStatement update;

	private MusicaDAO() throws ClassNotFoundException, SQLException{

		Connection conexao = Conexao.getConexao();

		selectNewId = conexao.prepareStatement("select nextval('id_musica_seq')");
		select = conexao.prepareStatement("select * from musica where id_artista = ?");
		selectAll = conexao.prepareStatement("select * from musica");
		selectMusicasPlaylist = conexao.prepareStatement("select * from musica_playlist where id_playlist = ? ");
		insert = conexao.prepareStatement("insert into musica values (?,?,?)");
		delete = conexao.prepareStatement("delete from musica where id_musica = ?");
		deleteMusicaUsuario = conexao.prepareStatement("delete from usuario_musica where id_musica = ?");
		deleteMusicaPlaylist = conexao.prepareStatement("delete from musica_playlist where id_musica = ?");
		
		update = conexao.prepareStatement("update musica set nome = ?, id_artista = ? where id_musica = ?");

	}

	public static MusicaDAO getInstance() throws ClassNotFoundException, SQLException {
		if (instance == null) {
			instance = new MusicaDAO();
		}
		return instance;
	}

	private int selectNewId() throws Exception {
			ResultSet rs = selectNewId.executeQuery();
			if (rs.next()) {
				return rs.getInt(1);
			}
		
		return 0;
	}

	public void insert(Musica musica) throws Exception {

		
			insert.setInt(1, selectNewId());
			insert.setString(2, musica.getNome());
			insert.setInt(3, musica.getId_artista());
			insert.executeUpdate();
		
	}

	public List<Musica> selectMusicasArtista(int artista) throws Exception {
		
		select.setInt(1, artista);
		ResultSet rs = select.executeQuery();
	    List<Musica> musicas = new LinkedList<Musica>();

			while(rs.next()) {

				int id = rs.getInt(1);
				String nome = rs.getString(2);
				
				Musica m = new Musica(id, nome, artista);
				musicas.add(m);

			}

		return musicas;

	}
	
	public List<Integer> selectMusicasPlaylist(int playlist) throws Exception{ //retorna uma lista de ids das músicas da playlist passada como parâmetro
		selectMusicasPlaylist.setInt(1, playlist);
		ResultSet rs = selectMusicasPlaylist.executeQuery();
	    List<Integer> ids_musicas = new LinkedList<Integer>();
	    while(rs.next()) {

			int id_musica = rs.getInt(2);
			ids_musicas.add(id_musica);

		}

	    return ids_musicas;
	    
	}

	public void delete(Musica musica) throws Exception {
		
			deleteMusicaUsuario.setInt(1, musica.getId());
			deleteMusicaUsuario.executeUpdate();
			deleteMusicaPlaylist.setInt(1, musica.getId());
			deleteMusicaPlaylist.executeUpdate();
			delete.setInt(1, musica.getId());
			delete.executeUpdate();
		

	}

	public void update(Musica musica) throws Exception {
		
			update.setString(1, musica.getNome());
			update.setInt(2, musica.getId_artista());
			update.setInt(3, musica.getId());
			update.executeUpdate();
		
	}
	
	public List<Musica> selectAll() throws Exception {
		List<Musica> musicas = new LinkedList<Musica>();
			ResultSet rs = selectAll.executeQuery();
			while (rs.next()) {
				int id = rs.getInt(1);
				String nome = rs.getString(2);
				System.out.println(nome);
				int id_artista = rs.getInt(3);
				
				musicas.add(new Musica(id, nome, id_artista));
			}
		
		return musicas;
	}

}
