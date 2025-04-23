package com.github.nancho313.videoloaderapi.infrastructure.persistence.customquery;

import com.github.nancho313.videoloaderapi.infrastructure.persistence.entity.UserEntity;
import com.github.nancho313.videoloaderapi.infrastructure.persistence.entity.VoteEntity;
import com.github.nancho313.videoloaderapi.infrastructure.persistence.projection.RankingProjection;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class VoteCustomQuery {

  private final EntityManager entityManager;

  public VoteCustomQuery(EntityManager entityManager) {
    this.entityManager = entityManager;
  }
  public List<RankingProjection> getRankingWithFilters(Map<String, String> filters) {

    List<Predicate> predicates = new ArrayList<>();

    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<RankingProjection> query = cb.createQuery(RankingProjection.class);

    Root<UserEntity> userRoot = query.from(UserEntity.class);
    Root<VoteEntity> voteRoot = query.from(VoteEntity.class);

    Predicate joinCondition = cb.equal(voteRoot.get("videoOwnerId"), userRoot.get("id"));

    predicates.add(joinCondition);

    if(filters!= null) {

      filters.forEach((field, value) -> predicates.add(cb.equal(userRoot.get(field), value)));
    }

    Expression<Long> voteCountExpr = cb.count(voteRoot.get("id"));

    query.select(cb.construct(
                    RankingProjection.class,
                    userRoot.get("name"),
                    userRoot.get("email"),
                    userRoot.get("id"),
                    userRoot.get("city"),
                    userRoot.get("country"),
                    voteCountExpr
            ))
            .where(cb.and(predicates.toArray(Predicate[]::new)))
            .orderBy(cb.desc(voteCountExpr))
            .groupBy(userRoot.get("name"), userRoot.get("email"), userRoot.get("id"));

    return entityManager.createQuery(query).getResultList();
  }
}
