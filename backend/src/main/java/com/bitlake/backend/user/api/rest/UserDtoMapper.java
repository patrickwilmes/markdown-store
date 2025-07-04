package com.bitlake.backend.user.api.rest;

import com.bitlake.backend.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.core.convert.converter.Converter;

@Mapper(componentModel = "spring")
public interface UserDtoMapper  {
  @Mapping(source = "hashedPassword", target = "password")
  UserDto toDto(User user);

  @Mapping(source = "password", target = "hashedPassword")
  User toDomain(UserDto userDto);
}
