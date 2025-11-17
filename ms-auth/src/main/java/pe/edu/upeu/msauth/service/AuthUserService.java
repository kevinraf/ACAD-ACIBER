package pe.edu.upeu.msauth.service;

import pe.edu.upeu.msauth.dto.AuthUserDto;
import pe.edu.upeu.msauth.dto.TokenDto;
import pe.edu.upeu.msauth.entity.AuthUser;

public interface AuthUserService {
    public AuthUser save(AuthUserDto authUserDto);


    public TokenDto login(AuthUserDto authUserDto);


    public TokenDto validate(String token);
}
