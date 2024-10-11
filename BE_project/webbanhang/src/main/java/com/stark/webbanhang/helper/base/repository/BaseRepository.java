package com.stark.webbanhang.helper.base.repository;


import com.stark.webbanhang.helper.base.construct.IBaseRepository;
import com.stark.webbanhang.utils.object.NullAwareBeanUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import java.io.Serializable;
import java.util.List;
import java.util.function.Function;

@Repository
@NoArgsConstructor
@Component
public class BaseRepository<T, ID extends Serializable> implements IBaseRepository<T, ID> {
    private JpaRepository<T, ID> repository;
    protected Class<T> entityClass;

    public BaseRepository(JpaRepository<T, ID> repository, Class<T> entityClass) {
        this.repository = repository;
        this.entityClass = entityClass;
    }

    @Override
    @Transactional(readOnly = true)
    public T get(ID id) {
        try {
            return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Not found : " + id));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error get: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> getAll() {
        try {
            return repository.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error get all : " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public T save(T entity) {
        try {
            return repository.save(entity);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error occurred while saving the entity: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public T save(T entity, Function<T, T> callback) {
        try {
            T result = repository.save(entity);
            if (callback != null) {
                return callback.apply(result);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error occurred while saving the entity: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public T update(ID id, T source) {
        try {
            T entity = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Not found : " + id));
            NullAwareBeanUtils.copyNonNullProperties(source, entity);
            return repository.save(entity);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error occurred while saving the entity: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public T update(ID id, T source, Function<T, T> callback) {
        try {
            T entity = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Not found : " + id));
            NullAwareBeanUtils.copyNonNullProperties(source, entity);
            T result = repository.save(entity);
            if (callback != null) {
                return callback.apply(result);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error occurred while saving the entity: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public T delete(ID id) {
        T model = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Not found : " + id));
        repository.deleteById(id);
        return model;
    }

    @Override
    @Transactional
    public T delete(ID id, Function<T, T> callback) {
        T model = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Not found : " + id));
        repository.deleteById(id);
        if (callback != null) {
            return callback.apply(model);
        }
        return model;
    }

}