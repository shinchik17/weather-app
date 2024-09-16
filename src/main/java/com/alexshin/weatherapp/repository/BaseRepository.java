package com.alexshin.weatherapp.repository;

import com.alexshin.weatherapp.exception.BaseRepositoryException;
import com.alexshin.weatherapp.model.entity.BaseEntity;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

@RequiredArgsConstructor
public class BaseRepository<K extends Serializable, E extends BaseEntity<K>> implements Repository<K, E> {

    protected final Class<E> clazz;
    private final SessionFactory sessionFactory;

    @Override
    public E save(E entity) {
        runWithinTx(session -> session.persist(entity));
        return entity;
    }

    @Override
    public void update(E entity) {
        runWithinTx(
                session -> {
                    var e = session.find(clazz, entity.getId());
                    if (e != null) {
                        session.merge(entity);
                    }
                }
        );
    }

    @Override
    public void delete(K id) {
        runWithinTx(
                session -> session.remove(session.find(clazz, id))
        );
    }

    @Override
    public Optional<E> findById(K id) {
        return Optional.ofNullable(runWithinTxAndReturn(em -> em.find(clazz, id)));
    }

    @Override
    public List<E> findAll() {
        return runWithinTxAndReturn(
                session -> {
                    Query<E> query = session.createQuery("FROM " + clazz.getSimpleName(), clazz);
                    return query.getResultList();
                }
        );
    }


    protected void runWithinTx(Consumer<Session> sessionConsumer) {
        runWithinTxAndReturn(session -> {
                    sessionConsumer.accept(session);
                    return null;
                }
        );

    }


    protected <R> R runWithinTxAndReturn(Function<Session, R> sessionFunc) {
        var session = sessionFactory.openSession();
        R result;
        Transaction transaction = session.beginTransaction();
        try {
            result = sessionFunc.apply(session);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw new BaseRepositoryException(e);
        } finally {
            session.close();
        }

        return result;
    }


}
