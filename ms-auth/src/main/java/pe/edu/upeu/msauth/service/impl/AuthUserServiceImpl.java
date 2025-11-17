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
        System.out.println(authUserDto);
        Optional<AuthUser> user = authUserRepository.findByUserName(authUserDto.getUserName());
        System.out.println(user.toString());
        if (user.isPresent())
            return null;
        String password = passwordEncoder.encode(authUserDto.getPassword());
        System.out.println(password);
        AuthUser authUser = AuthUser.builder()
                .userName(authUserDto.getUserName())
                .password(password)
                .build();
        System.out.println("authUser.toString(): "+authUser.toString());
        return authUserRepository.save(authUser);
    }


    @Override
    public TokenDto login(AuthUserDto authUserDto) {
        Optional<AuthUser> user = authUserRepository.findByUserName(authUserDto.getUserName());
        if (!user.isPresent())
            return null;
        if (passwordEncoder.matches(authUserDto.getPassword(), user.get().getPassword()))
            return new TokenDto(jwtProvider.createToken(user.get()));
        return null;
    }




    @Override
    public TokenDto validate(String token) {
        if (!jwtProvider.validate(token))
            return null;
        String username = jwtProvider.getUserNameFromToken(token);
        if (!authUserRepository.findByUserName(username).isPresent())
            return null;
        return new TokenDto(token);
    }
}
