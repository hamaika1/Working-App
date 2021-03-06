package ar.com.wapp.business;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ar.com.wapp.model.Usuario;
import ar.com.wapp.model.persistence.UsuarioRepository;

@Service
public class UsuarioBusiness implements IUsuarioBusiness {

	@Autowired
	private UsuarioRepository usuarioDAO;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	public Usuario load(long id) throws BusinessException, NotFoundException {
		Optional<Usuario> o;
		try {
			o = usuarioDAO.findById(id);
		} catch (Exception e) {
			throw new BusinessException(e);
		}
		if (o.isPresent())
			return o.get();
		else
			throw new NotFoundException();
	}

	@Override
	public Usuario add(Usuario usuario) throws BusinessException {

		try {
			usuario.setAccountNonExpired(true);
			usuario.setAccountNonLocked(true);
			usuario.setCredentialsNonExpired(true);
			usuario.setEnabled(true);
			usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
			return usuarioDAO.save(usuario);
		} catch (Exception e) {
			throw new BusinessException(e);
		}

	}

	@Override
	public void delete(long id) throws BusinessException {
		try {
			usuarioDAO.deleteById(id);
		} catch (Exception e) {
			throw new BusinessException(e);
		}
	}

	@Override
	public Usuario update(Usuario usuario) throws BusinessException {

		try {
			usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
			usuario.setAccountNonExpired(true);
			usuario.setAccountNonLocked(true);
			usuario.setCredentialsNonExpired(true);
			usuario.setEnabled(true);
			return usuarioDAO.save(usuario);
		} catch (Exception e) {
			throw new BusinessException(e);
		}

	}

	@Override
	public List<Usuario> list() throws BusinessException {
		try {
			return usuarioDAO.findAll();
		} catch (Exception e) {
			throw new BusinessException(e);
		}
	}

	@Override
	public List<Usuario> list(String parteDelNombre) throws BusinessException {
		try {
			if (parteDelNombre != null && parteDelNombre.trim().length() > 2) {
				return usuarioDAO.findByFirstNameLikeAndEnabledTrueOrderByUsername("%" + parteDelNombre + "%");
			} else {
				throw new BusinessException("Parámetros incorrectos");
			}
		} catch (Exception e) {
			throw new BusinessException(e);
		}
	}

	@Override
	public Usuario load(String usernameOrEmail) throws BusinessException, NotFoundException {
		List<Usuario> l;
		try {
			l = usuarioDAO.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
		if (l.size() == 0)
			throw new NotFoundException("No se encuentra el usuari@ con nombre/email=" + usernameOrEmail);

		return l.get(0);
	}

}
