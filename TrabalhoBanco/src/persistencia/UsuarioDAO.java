package persistencia;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import dados.Playlist;
import dados.Usuario;
import dados.Artista;
import dados.Musica;

public class UsuarioDAO implements DAO<Usuario> {
	
	
	private static UsuarioDAO instance = null;

	private PreparedStatement selectNewId;
	private PreparedStatement select;
	private PreparedStatement selectAll;
	private PreparedStatement insert;
	private PreparedStatement insertUsuarioArtista;
	private PreparedStatement insertUsuarioMusica;
	private PreparedStatement insertUsuarioPlaylist;
	private PreparedStatement delete;
	private PreparedStatement deleteUsuarioMusica;
	private PreparedStatement deleteUsuarioPlaylist;
	private PreparedStatement deleteUsuarioArtista;
	private PreparedStatement update;
	private PreparedStatement selectAllUsuarioArtista;
	private PreparedStatement selectAllUsuarioMusica;

	private PreparedStatement selectAllUsuarioPlaylist;


	

	private UsuarioDAO() throws ClassNotFoundException, SQLException{

		Connection conexao = Conexao.getConexao();

		selectNewId = conexao.prepareStatement("select nextval('id_usuario_seq')");
		select = conexao.prepareStatement("select * from usuario where email = ? and senha = ?");
		selectAll = conexao.prepareStatement("select * from usuario");
		insert = conexao.prepareStatement("insert into usuario values (?,?,?,?,?,?)");
		insertUsuarioArtista = conexao.prepareStatement("insert into usuario_artista values ((select nextval('id_usuario_artista_seq')),?,?) ");
		insertUsuarioMusica = conexao.prepareStatement("insert into usuario_musica values ((select nextval('id_usuario_musica_seq')),?,?) ");
		insertUsuarioPlaylist = conexao.prepareStatement("insert into usuario_playlist values ((select nextval('id_usuario_playlist_seq')),?,?) ");
		delete = conexao.prepareStatement("delete from usuario where id_usuario = ?");
		deleteUsuarioMusica = conexao.prepareStatement("delete from usuario_musica where id_usuario = ?");
		deleteUsuarioPlaylist = conexao.prepareStatement("delete from usuario_playlist where id_usuario = ?");
		deleteUsuarioArtista = conexao.prepareStatement("delete from usuario_artista where id_usuario = ?");
		
		selectAllUsuarioArtista = conexao.prepareStatement("select * from usuario_artista");
		selectAllUsuarioMusica = conexao.prepareStatement("select * from usuario_musica");
		selectAllUsuarioPlaylist = conexao.prepareStatement("select * from usuario_playlist");



		update = conexao.prepareStatement("update usuario set email = ?, nome = ?, cpf = ?, senha = ?, moderador = ?  where id_usuario = ?");


	}

	public static UsuarioDAO getInstance() throws ClassNotFoundException, SQLException {
		if (instance == null) {
			instance = new UsuarioDAO();
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
	
	
	
	
	
	@Override
	public List<Usuario> selectAll() throws Exception {
		List<Usuario> usuarios = new LinkedList<Usuario>();
		ResultSet rs = selectAll.executeQuery();
		while (rs.next()) {
			int id = rs.getInt(1);
			String email = rs.getString(2);
			System.out.println(email);
			String nome = rs.getString(3);
			System.out.println(nome);
			Long cpf = rs.getLong(4);
			System.out.println(cpf);
			String senha = rs.getString(5);
			System.out.println(senha);
			Boolean moderador = rs.getBoolean(6);
			System.out.println(moderador);

			
			usuarios.add(new Usuario(id, nome, email,senha, cpf,moderador));
		}
	
		return usuarios;
	}
	
	
	public Usuario select(String email, String senha) throws Exception {
		
		Usuario u = new Usuario();
		ResultSet rs = select.executeQuery();
		int id = rs.getInt(1);
		String email1 = rs.getString(2);
		System.out.println(email1);
		String nome = rs.getString(3);
		System.out.println(nome);
		long cpf = rs.getLong(4);
		System.out.println(cpf);
		String senha1 = rs.getString(5);
		System.out.println(senha1);
		boolean moderador = rs.getBoolean(6);
		System.out.println(moderador);
		u.setId(id);
		u.setEmail(email1);
		u.setNome(nome);
		u.setCpf(cpf);
		u.setSenha(senha1);
		u.setModerador(moderador);
		return u;
	
	}

	@Override
	public void insert(Usuario usuario) throws Exception {
		usuario.setId(selectNewId());
		insert.setInt(1, usuario.getId());
		insert.setString(2, usuario.getEmail());
		insert.setString(3, usuario.getNome());
		insert.setLong(4, usuario.getCpf());
		insert.setString(5, usuario.getSenha());
		insert.setBoolean(6, usuario.isModerador());
		insert.executeUpdate();
		
	}

	@Override
	public void delete(Usuario usuario) throws Exception {
		deleteUsuarioMusica.setInt(1,usuario.getId());
		deleteUsuarioMusica.executeUpdate();

		deleteUsuarioPlaylist.setInt(1,usuario.getId());
		deleteUsuarioPlaylist.executeUpdate();

		deleteUsuarioArtista.setInt(1,usuario.getId());
		deleteUsuarioArtista.executeUpdate();
		
		delete.setInt(1, usuario.getId());
		delete.executeUpdate();		
	}

	@Override
	public void update(Usuario usuario) throws Exception {
		update.setString(1, usuario.getEmail());
		update.setString(2, usuario.getNome());
		update.setLong(3, usuario.getCpf());
		update.setString(4, usuario.getSenha());
		update.setBoolean(5, usuario.isModerador());
		update.setInt(6, usuario.getId());
		update.executeUpdate();
		
	}
	
	public void favoritarArtista(Usuario u, Artista a) throws Exception{
		
		insertUsuarioArtista.setInt(1,u.getId());
		insertUsuarioArtista.setInt(2, a.getId());
		insertUsuarioArtista.executeUpdate();
		
		
	}
	
	public List<Integer> selectAllUsuarioArtista(Usuario u){
		List<Integer> ids = new LinkedList<Integer>();
			
				try {
					ResultSet rs = selectAllUsuarioArtista.executeQuery();
					while(rs.next()) {
					int idArtista = rs.getInt(3);
					if(u.getId() == rs.getInt(2)) {
						ids.add(idArtista);
					}
					 
				}
					}catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return ids;
				
	}
	
	public List<Integer> selectAllUsuarioMusica(Usuario u){
		List<Integer> ids = new LinkedList<Integer>();
			
				try {
					ResultSet rs = selectAllUsuarioMusica.executeQuery();
						while(rs.next()) {
							int idMusica = rs.getInt(3);
							if(u.getId() == rs.getInt(2)) {
								ids.add(idMusica);
							}
						 
						}
					}catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return ids;
	}
	
	public List<Integer> selectAllUsuarioPlaylist(Usuario u){
		List<Integer> ids = new LinkedList<Integer>();
			
				try {
						ResultSet rs = selectAllUsuarioPlaylist.executeQuery();
						while(rs.next()) {
							int idPlaylist = rs.getInt(3);
							if(u.getId() == rs.getInt(2)) {
								ids.add(idPlaylist);
							}
						 
						}
					}catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return ids;
	}
	
	
	
	public void favoritarMusica(Usuario u, Musica m) throws Exception{
		
		insertUsuarioMusica.setInt(1,u.getId());
		insertUsuarioMusica.setInt(2, m.getId());
		insertUsuarioMusica.executeUpdate();
		
		
	}
	
	public void insertUsuarioPlaylist(Usuario u, Playlist p) throws Exception{
		
		insertUsuarioPlaylist.setInt(1,u.getId());
		insertUsuarioPlaylist.setInt(2, p.getId());
		insertUsuarioPlaylist.executeUpdate();
		
		
	}
	
	
	
	
	
	
	
	
	
	

}
