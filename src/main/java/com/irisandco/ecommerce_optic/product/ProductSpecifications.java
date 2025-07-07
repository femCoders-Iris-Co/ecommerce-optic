package com.irisandco.ecommerce_optic.product;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProductSpecifications {
    public static Specification<Product> filterByParams(
            String name,
            List<String> categories,
            BigDecimal minimumPrice,
            BigDecimal maximumPrice,
            Boolean isFeatured) {
        return new Specification<Product>() {
            @Override
            public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();

                if (name != null && !name.isEmpty()) {
                    predicates.add(cb.like(root.get("name"), "%" + name + "%"));
                }

                if (categories != null && !categories.isEmpty()) {
                    CriteriaBuilder.In<String> inClause = cb.in(root.get("category"));
                    for (String category : categories) {
                        inClause.value(category);
                    }
                    predicates.add(inClause);
                }

                if (minimumPrice != null) {
                    predicates.add(cb.ge(root.get("price"), minimumPrice));
                }

                if (maximumPrice != null) {
                    predicates.add(cb.le(root.get("price"), maximumPrice));
                }

                if (isFeatured != null) {
                    predicates.add(cb.equal(root.get("featured"), isFeatured));
                }

                return cb.and(predicates.toArray(new Predicate[0]));
            }
        };
    }
}
