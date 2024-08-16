package com.alexshin.weatherapp.repository;

import com.alexshin.weatherapp.entity.BaseEntity;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

@RequiredArgsConstructor
public class BaseRepository<K extends Serializable, E extends BaseEntity<K>> implements Repository<K, E> {

    private final Class<E> clazz;
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
    public Optional<E> findById(K id, Map<String, Object> properties) {
        return Optional.ofNullable(runWithinTxAndReturn(em -> em.find(clazz, id)));
    }

    @Override
    public List<E> findAll() {
        return runWithinTxAndReturn(
                session -> {
                    var criteria = session.getCriteriaBuilder().createQuery(clazz);
                    criteria.from(clazz);
                    return session.createQuery(criteria).getResultList();
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
        R result;
        var session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try (session) {
            result = sessionFunc.apply(session);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException(e);
        }

        return result;
    }


}
