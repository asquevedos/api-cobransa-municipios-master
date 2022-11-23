package mbd.infrastructure.service.login;

import java.util.List;

import mbd.model.login.Usuario;


public interface IUsuarioService {

	public Usuario findByEmail(String email);
	public Usuario findByEmailCri(String emailCipted);
	public Usuario save(Usuario usuario);
	public List<Usuario> findByNombreAndApellido(String terminoBusqueda);
    public Usuario findUsuarioByCedulaAndEmail(String cedula, String email);
}
