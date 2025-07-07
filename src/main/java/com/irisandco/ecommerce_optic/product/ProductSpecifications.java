package com.irisandco.ecommerce_optic.product;

import com.irisandco.ecommerce_optic.category.Category;
import jakarta.persistence.criteria.*;
import org.hibernate.type.EntityType;
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
                    // Join the categories collection
                    Join<EntityType, Category> categoriesJoin = root.join("categories");
                    // Join on the 'name' field of Category
                    Path<String> categoryNamePath = categoriesJoin.get("name");
                    // Create an IN predicate on the category names
                    CriteriaBuilder.In<String> inClause = cb.in(categoryNamePath);
                    for (String categoryName : categories) {
                        inClause.value(categoryName);
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
