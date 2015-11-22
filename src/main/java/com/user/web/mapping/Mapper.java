package com.user.web.mapping;

import com.user.persistence.entity.User;
import com.user.web.dto.UserDto;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class Mapper extends ConfigurableMapper {

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
        factory.classMap(User.class, UserDto.class)
                .fieldMap("photo", "photo").mapNullsInReverse(false).add()
                .byDefault().register();
    }

    public <S, T> Page<T> mapAsPage(Page<S> source, Class<T> target) {
        List<T> content = mapAsList(source.getContent(), target);
        Pageable pageable = new PageRequest(source.getNumber(), source.getSize(), source.getSort());
        return new PageImpl<>(content, pageable, source.getTotalElements());
    }

}
