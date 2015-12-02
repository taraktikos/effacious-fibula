package com.user.web.mapping;

import com.user.persistence.entity.Article;
import com.user.persistence.entity.User;
import com.user.web.dto.ArticleDto;
import com.user.web.dto.UserDto;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.converter.ConverterFactory;
import ma.glasnost.orika.converter.builtin.PassThroughConverter;
import ma.glasnost.orika.impl.ConfigurableMapper;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class Mapper extends ConfigurableMapper {

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    public Mapper() {
        super(false);
    }

    @Override
    @PostConstruct
    public final void init() {
        super.init();
    }

    @Override
    protected void configure(MapperFactory factory) {
        ConverterFactory converterFactory = factory.getConverterFactory();
        converterFactory.registerConverter(new PassThroughConverter(DateTime.class));

        factory.classMap(User.class, UserDto.class)
                .fieldMap("photo").mapNullsInReverse(false).add()
                .exclude("password")
                .byDefault()
                .customize(new CustomMapper<User, UserDto>() {
                    @Override
                    public void mapAtoB(User user, UserDto dto, MappingContext context) {
                        dto.setPassword(null);
                    }

                    @Override
                    public void mapBtoA(UserDto dto, User user, MappingContext context) {
                        if (!dto.getPassword().isEmpty()) {
                            user.setPassword(passwordEncoder.encode(dto.getPassword()));
                        }
                    }
                }).register();

        factory.classMap(Article.class, ArticleDto.class)
                .fieldMap("image").mapNullsInReverse(false).add()
                .fieldMap("createdAt").mapNullsInReverse(false).add()
                .byDefault().register();
    }

    public <S, T> Page<T> mapAsPage(Page<S> source, Class<T> target) {
        List<T> content = mapAsList(source.getContent(), target);
        Pageable pageable = new PageRequest(source.getNumber(), source.getSize(), source.getSort());
        return new PageImpl<>(content, pageable, source.getTotalElements());
    }

}
