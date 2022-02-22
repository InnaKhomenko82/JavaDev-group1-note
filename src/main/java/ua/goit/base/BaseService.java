package ua.goit.base;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
abstract public class BaseService<E extends BaseEntity<UUID>, D extends BaseDto> {

    private final Class<E> entityClass;
    private final Class<D> dtoClass;
    protected JpaRepository<E, UUID> repository;
    protected ModelMapper modelMapper;

    @SuppressWarnings("unchecked")
    protected BaseService() {
        Type[] params = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments();
        entityClass = (Class<E>) params[0];
        dtoClass = (Class<D>) params[1];
    }

    @Autowired
    public void setRepository(JpaRepository<E, UUID> repository) {
        this.repository = repository;
    }

    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public List<D> findAll() {
        List<D> dtoList = new ArrayList<>();
        repository.findAll().forEach(item -> dtoList.add(modelMapper.map(item, dtoClass)));
        return dtoList;
    }

    public D find(UUID id) {
        D dto = null;
        try {
            dto = dtoClass.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return repository.findById(id).map(entity -> modelMapper.map(entity, dtoClass)).orElse(dto);
    }

    @Transactional
    public D create(D dto) {
        E model = modelMapper.map(dto, entityClass);
        return modelMapper.map(repository.save(model), dtoClass);
    }

    @Transactional
    public void update(UUID id, D dto) {
        repository.findById(id)
                .map(user -> {
                    modelMapper.map(dto, user);
                    return user;
                }).ifPresent(user -> repository.save(user));
    }

    @Transactional
    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public boolean isExist(BaseDto dto) {
        return false;
    }
}
