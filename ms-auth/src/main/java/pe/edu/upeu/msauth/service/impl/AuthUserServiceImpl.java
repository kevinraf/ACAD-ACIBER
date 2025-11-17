package pe.edu.upeu.msauth.service.impl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pe.edu.upeu.msauth.dto.AuthUserDto;
import pe.edu.upeu.msauth.dto.TokenDto;
import pe.edu.upeu.msauth.entity.AuthUser;
import pe.edu.upeu.msauth.repository.AuthUserRepository;
import pe.edu.upeu.msauth.security.JwtProvider;
import pe.edu.upeu.msauth.service.AuthUserService;
import java.util.Optional;

@Service
public class AuthUserServiceImpl implements AuthUserService {

    @Autowired
    AuthUserRepository authUserRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JwtProvider jwtProvider;

    @Override
    public AuthUser save(AuthUserDto authUserDto) {
        System.out.println("AuthUserDto: " + authUserDto);

        Optional<AuthUser> user = authUserRepository.findByUserName(authUserDto.getUserName());
        System.out.println("Existe? " + user);

        if (user.isPresent()) {
            // 👉 Lanzamos excepción controlada (la atrapará GlobalExceptionHandler)
            throw new IllegalArgumentException("El usuario '" + authUserDto.getUserName() + "' ya existe");
        }

        String password = passwordEncoder.encode(authUserDto.getPassword());
        System.out.println("Password hash: " + password);

        AuthUser authUser = AuthUser.builder()
                .userName(authUserDto.getUserName())
                .password(password)
                .build();

        System.out.println("authUser.toString(): " + authUser);

        return authUserRepository.save(authUser);
    }

    @Override
    public TokenDto login(AuthUserDto authUserDto) {
        Optional<AuthUser> user = authUserRepository.findByUserName(authUserDto.getUserName());
        if (!user.isPresent()) {
            throw new IllegalArgumentException("Usuario o contraseña incorrectos");
        }
        if (!passwordEncoder.matches(authUserDto.getPassword(), user.get().getPassword())) {
            throw new IllegalArgumentException("Usuario o contraseña incorrectos");
        }
        return new TokenDto(jwtProvider.createToken(user.get()));
    }

    @Override
    public TokenDto validate(String token) {
        if (!jwtProvider.validate(token)) {
            throw new IllegalArgumentException("Token inválido");
        }
        String username = jwtProvider.getUserNameFromToken(token);
        if (!authUserRepository.findByUserName(username).isPresent()) {
            throw new IllegalArgumentException("Usuario no encontrado para el token");
        }
        return new TokenDto(token);
    }
}
